package pl.betoniarze.kawapp.feature.chime_generator

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.sin

private const val CHIME_SAMPLE_RATE = 44100

/** Which gong(s) of the repeater chime to strike. */
enum class ChimeTone {
    /** Only the first, high gong ("ding"). */
    FIRST,

    /** Only the second, low gong ("dong"). */
    SECOND,

    /** The high gong, then the low one ("ding-dong"). */
    BOTH,
}

/** Synthesized samples per [ChimeTone], built on first use and reused afterwards. */
private val chimeCache = ConcurrentHashMap<ChimeTone, ShortArray>()

/**
 * Plays the minute-repeater chime once at notification volume. [tone] picks the high gong, the
 * low gong, or both in sequence. The first call for each [tone] synthesizes the sound off the main
 * thread; later calls reuse it. Returns as soon as playback starts.
 */
suspend fun playRepeaterChime(tone: ChimeTone = ChimeTone.BOTH) {
    val pcm = withContext(Dispatchers.Default) {
        chimeCache.computeIfAbsent(tone) { buildChime(it) }
    }
    playPcm(pcm, CHIME_SAMPLE_RATE)
}

/**
 * Plays mono 16-bit [pcm] once at notification volume. The track releases itself when playback
 * ends. Audio errors are ignored, so a missing chime never crashes the timer.
 */
private fun playPcm(pcm: ShortArray, sampleRate: Int) {
    runCatching {
        val track = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setSampleRate(sampleRate)
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setBufferSizeInBytes(pcm.size * 2)
            .setTransferMode(AudioTrack.MODE_STATIC)
            .build()
        try {
            track.write(pcm, 0, pcm.size)
            track.setNotificationMarkerPosition(pcm.size)
            track.setPlaybackPositionUpdateListener(object : AudioTrack.OnPlaybackPositionUpdateListener {
                override fun onMarkerReached(t: AudioTrack) = t.release()
                override fun onPeriodicNotification(t: AudioTrack) = Unit
            })
            track.play()
        } catch (e: Exception) {
            track.release()
            throw e
        }
    }
}

/**
 * Synthesizes a minute-repeater style chime. With [ChimeTone.BOTH] it is the "ding-dong" of a
 * repeater watch's quarter strike: a high gong struck, then a low one. [ChimeTone.FIRST] and
 * [ChimeTone.SECOND] strike only one gong, starting immediately.
 *
 * Repeater gongs are thin steel wires, so their overtones are inharmonic (close to a free bar's
 * 1 : 2.76 : 5.40 : 8.93). That, a very fast hammer attack, upper partials that die out quickly,
 * and a slightly detuned twin of the fundamental that makes it shimmer, give the glassy sound.
 *
 * @param tone Which gong(s) to strike.
 * @param highHz Pitch of the first (high) gong.
 * @param lowHz Pitch of the second (low) gong.
 * @param gapSec Time between the two hammer strikes (only used for [ChimeTone.BOTH]).
 * @param ringSec How long the fundamental rings (time constant of its decay).
 * @param volume Peak level, 0–1.
 */
private fun buildChime(
    tone: ChimeTone,
    highHz: Double = 1760.0, // A6
    lowHz: Double = 1318.5, // E6
    gapSec: Double = 0.24,
    ringSec: Double = 0.55,
    volume: Double = 0.6,
    sampleRate: Int = CHIME_SAMPLE_RATE,
): ShortArray {
    val lowRingSec = ringSec * 1.15
    // Long enough for the last strike to ring down to about 4% of its level.
    val totalSec = when (tone) {
        ChimeTone.FIRST -> ringSec * 3.2
        ChimeTone.SECOND -> lowRingSec * 3.2
        ChimeTone.BOTH -> gapSec + lowRingSec * 3.2
    }
    val mix = DoubleArray((sampleRate * totalSec).toInt())
    if (tone != ChimeTone.SECOND) {
        strikeGong(mix, startSample = 0, baseHz = highHz, gain = 1.0, ringSec = ringSec, sampleRate = sampleRate)
    }
    if (tone != ChimeTone.FIRST) {
        val start = if (tone == ChimeTone.BOTH) (sampleRate * gapSec).toInt() else 0
        strikeGong(mix, start, lowHz, gain = 0.9, ringSec = lowRingSec, sampleRate = sampleRate)
    }

    // Normalize to the requested peak and fade the last 30 ms so the buffer ends at silence.
    val peak = mix.maxOf { abs(it) }.takeIf { it > 0 } ?: 1.0
    val fade = sampleRate * 3 / 100
    return ShortArray(mix.size) { i ->
        val tail = if (i > mix.size - fade) (mix.size - i).toDouble() / fade else 1.0
        (mix[i] / peak * tail * volume * Short.MAX_VALUE).toInt().toShort()
    }
}

/** Gong partials: (frequency ratio, detune in Hz, amplitude, decay as a fraction of ringSec). */
private val GONG_PARTIALS = listOf(
    Partial(1.00, 0.0, 1.00, 1.00),
    Partial(1.00, 1.3, 0.45, 0.90), // detuned twin: slow beating, the "shimmer"
    Partial(2.76, 0.0, 0.38, 0.45),
    Partial(2.76, 2.1, 0.15, 0.40),
    Partial(5.40, 0.0, 0.16, 0.15),
    Partial(8.93, 0.0, 0.07, 0.06),
)

private data class Partial(val ratio: Double, val detuneHz: Double, val amp: Double, val decay: Double)

/** Adds one hammer strike on a wire gong tuned to [baseHz] into [out], from [startSample]. */
private fun strikeGong(
    out: DoubleArray,
    startSample: Int,
    baseHz: Double,
    gain: Double,
    ringSec: Double,
    sampleRate: Int,
) {
    val nyquist = sampleRate / 2.0
    val attack = sampleRate / 2000 // 0.5 ms: a hard steel hammer, not a soft mallet
    // Deterministic noise for the hammer "tick", so the chime sounds the same every time.
    val random = java.util.Random(baseHz.toLong())
    var prevNoise = 0.0
    for (i in startSample until out.size) {
        val n = i - startSample
        val t = n.toDouble() / sampleRate
        var s = 0.0
        for (p in GONG_PARTIALS) {
            val f = baseHz * p.ratio + p.detuneHz
            if (f >= nyquist) continue
            s += p.amp * exp(-t / (ringSec * p.decay)) * sin(2 * PI * f * t)
        }
        // Short, high-passed noise burst for the click of the hammer hitting the gong.
        if (t < 0.012) {
            val noise = random.nextDouble() * 2 - 1
            s += 0.12 * exp(-t / 0.002) * (noise - prevNoise)
            prevNoise = noise
        }
        val atk = if (n < attack) n.toDouble() / attack else 1.0
        out[i] += gain * atk * s
    }
}
