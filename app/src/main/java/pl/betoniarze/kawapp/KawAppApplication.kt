package pl.betoniarze.kawapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import pl.betoniarze.kawapp.ui.ThemeModeStore

/** Application class: sets up Hilt and applies the saved theme mode before any Activity starts. */
@HiltAndroidApp
class KawAppApplication : Application() {

    /** Injected by Hilt in [onCreate]. */
    @Inject
    lateinit var themeModeStore: ThemeModeStore

    override fun onCreate() {
        super.onCreate() // Hilt injects fields here.
        themeModeStore.applySaved()
    }
}
