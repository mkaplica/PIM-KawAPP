package pl.betoniarze.kawapp.core.data.fake

import pl.betoniarze.kawapp.core.model.BrewLog
import pl.betoniarze.kawapp.core.model.BrewMethod
import pl.betoniarze.kawapp.core.model.CoffeeBag
import pl.betoniarze.kawapp.core.model.FeedPost
import pl.betoniarze.kawapp.core.model.Grinder
import java.time.LocalDate

/** Sample feed posts and brew logs (November-December 2026) shown in debug builds only. */
object FakeCoffeeData {
    val feedPosts = listOf(
        FeedPost(
            id = "post_1",
            authorName = "Michał Kowalski",
            authorAvatarUrl = "https://picsum.photos/seed/avatar1/200/200",
            content = "W końcu udało mi się ustawić idealny przemiał pod tę mytą Etiopię z lokalnej palarni. Na 22 klikach w Comandante wyszła czysta brzoskwinia i jaśmin!",
            imageUrls = listOf(
                "https://picsum.photos/seed/coffee1/800/600",
                "https://picsum.photos/seed/coffee2/800/600"
            ),
            kavosCount = 42,
            comments = listOf(
                "Brzmi sztos! Jaka temperatura wody?",
                "22 kliki to pod V60 czy Kalitę?",
                "Też mam te ziarna, spróbuję dzisiaj tego ustawienia!"
            ),
            brewLogId = "log_1",
        ),
        FeedPost(
            id = "post_2",
            authorName = "Karolina Nowak",
            authorAvatarUrl = "https://picsum.photos/seed/avatar2/200/200",
            content = "Poranny AeroPress w plenerze smakuje zupełnie inaczej. Korzystam dziś z przepisu Hoffmanna, bez odwracania (standardowa metoda) – zero stresu o poparzenie!",
            imageUrls = listOf(
                "https://picsum.photos/seed/aeropress1/800/600"
            ),
            kavosCount = 18,
            comments = listOf(
                "AeroPress w terenie to najlepsza rzecz na świecie 😍",
                "Standardowa metoda > odwrócona, zmieniłem zdanie w zeszłym roku."
            ),
            brewLogId = "log_2",
        ),
        FeedPost(
            id = "post_3",
            authorName = "Bartek Wiśniewski (CoffeeGeek)",
            authorAvatarUrl = "https://picsum.photos/seed/avatar3/200/200",
            content = "Testuję dzisiaj metodę 4:6 od Tetsu Katsuyi na nowej Kenii. Zmniejszyłem pierwsze zalanie do 45 ml i kwasowość wystrzeliła w kosmos. Próbował ktoś?",
            imageUrls = emptyList(),// Text-only post without photos - worth testing this case in the UI!
            kavosCount = 95,
            comments = listOf(
                "Metoda 4:6 uratowała mi już niejedną Kenię!",
                "Dla mnie za dużo zabawy z wagą z rana, ale efekty szanuję.",
                "Próbowałem, ale przy drobniejszym mieleniu mocno mi zatykało filtr."
            ),
            brewLogId = "log_11",
        ),
        FeedPost(
            id = "post_4",
            authorName = "Zuzanna Zielińska",
            authorAvatarUrl = "https://picsum.photos/seed/avatar4/200/200",
            content = "Nowa dostawa ziaren na weekend! Kolumbia w obróbce anaerobic pachnie jak poziomki w czekoladzie jeszcze przed zmieleniem.",
            imageUrls = listOf(
                "https://picsum.photos/seed/beans1/800/600",
                "https://picsum.photos/seed/beans2/800/600",
                "https://picsum.photos/seed/beans3/800/600"
            ),
            kavosCount = 127,
            comments = listOf(
                "Z jakiej palarni te ziarna?",
                "Anaerobiki to ostatnio mój ulubiony profil 🍓",
                "Wyślij trochę na spróbowanie!"
            ),
            brewLogId = "log_28",
        ),
        FeedPost(
            id = "post_5",
            authorName = "Tomasz Kamiński",
            authorAvatarUrl = "https://picsum.photos/seed/avatar5/200/200",
            content = "Szybkie porównanie papierowych filtrów Hario z fabryki w Japonii vs Holandii. Różnica w czasie przepływu to prawie 25 sekund przy tym samym mieleniu.",
            imageUrls = listOf(
                "https://picsum.photos/seed/v60filter/800/600"
            ),
            kavosCount = 63,
            comments = listOf(
                "Dzięki za test, od dawna podejrzewałem, że te holenderskie są wolniejsze.",
                "Przepłukujesz je wrzątkiem przed zasypaniem?"
            ),
            brewLogId = "log_5",
        )
    )

    private val bagEthiopiaLacava = CoffeeBag(
        roasterName = "LaCava",
        beanName = "Kelloo Lalesa",
        origin = "Etiopia",
        roastDate = LocalDate.of(2026, 10, 12)
    )

    private val bagKenyaGrange = CoffeeBag(
        roasterName = "COFFEE GRANGE",
        beanName = "AA SWARA WASHED",
        origin = "Kenia",
        roastDate = LocalDate.of(2026, 10, 15)
    )

    private val bagColombiaCoffeeLab = CoffeeBag(
        roasterName = "CoffeeLab",
        beanName = "Finca Quebraditas SL28",
        origin = "Kolumbia",
        roastDate = LocalDate.of(2026, 10, 18)
    )

    private val bagPeruSmok = CoffeeBag(
        roasterName = "SMOK",
        beanName = "Peru Geisha",
        origin = "Peru",
        roastDate = LocalDate.of(2026, 10, 20)
    )

    private val bagBrazilPalomaKonesso = CoffeeBag(
        roasterName = "Paloma",
        beanName = "Paloma x Konesso Brazil Arara",
        origin = "Brazylia",
        roastDate = LocalDate.of(2026, 10, 23)
    )

    val brewLogs = listOf(
        // --- 1 NOVEMBER 2026 (Sunday - 2 entries) ---
        BrewLog(
            id = "log_1",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 11, 1),
            time = "09:15",
            coffeeBag = bagEthiopiaLacava,
            recipeId = "recipe_hoffmann_v60_1cup",
            recipeTitle = "James Hoffmann – 1-Cup V60",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "22 kliki",
            tastingNotes = "Bardzo kwiatowa, wyraźna bergamotka, ale czas parzenia przeciągnął się do 3:25. Następnym razem klik grubiej."
        ),
        BrewLog(
            id = "log_2",
            brewMethod = BrewMethod.AEROPRESS,
            date = LocalDate.of(2026, 11, 1),
            time = "14:40",
            coffeeBag = bagColombiaCoffeeLab,
            recipeId = "recipe_wendelboe_aeropress",
            recipeTitle = "Tim Wendelboe – Klasyczny AeroPress",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "18 klików",
            tastingNotes = "Dużo słodyczy, przypomina czerwone jabłko. Przeciskanie trwało 35 sekund."
        ),

        // --- 2 NOVEMBER 2026 (Monday - 1 entry) ---
        BrewLog(
            id = "log_3",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 11, 2),
            time = "08:05",
            coffeeBag = bagEthiopiaLacava,
            recipeId = "recipe_hoffmann_v60_1cup",
            recipeTitle = "James Hoffmann – 1-Cup V60",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "23 kliki",
            tastingNotes = "Po zmianie na 23 kliki zeszło idealnie w 2:55. Zniknęła lekka cierpkość w finiszu."
        ),

        // --- 3 NOVEMBER 2026 (Tuesday - 1 entry) ---
        BrewLog(
            id = "log_4",
            brewMethod = BrewMethod.AEROPRESS,
            date = LocalDate.of(2026, 11, 3),
            time = "08:30",
            coffeeBag = bagPeruSmok,
            recipeId = "recipe_hoffmann_aeropress",
            recipeTitle = "James Hoffmann – Ultimate AeroPress",
            grinder = Grinder.NICHE_ZERO,
            grindSetting = "Ustawienie 36",
            tastingNotes = "Test Rwandy na Niche Zero. Gęste body, truskawkowy dżem, trochę mniejsza klarowność niż z Comandante."
        ),

        // --- 4 NOVEMBER 2026 (Wednesday - 1 entry) ---
        BrewLog(
            id = "log_5",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 11, 4),
            time = "07:50",
            coffeeBag = bagKenyaGrange,
            recipeId = "recipe_scott_rao_v60",
            recipeTitle = "Scott Rao – Classic V60 (Rao Spin)",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "24 kliki",
            tastingNotes = "Wysoka kwasowość czarnej porzeczki. Woda 96 stopni."
        ),

        // --- 5 NOVEMBER 2026 (Thursday - 2 entries) ---
        BrewLog(
            id = "log_6",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 11, 5),
            time = "08:15",
            coffeeBag = bagKenyaGrange,
            recipeId = "recipe_matt_winton_5pour_v60",
            recipeTitle = "Matt Winton – Five Pour V60 (WBrC 2021)",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "25 klików",
            tastingNotes = "Obniżyłem temperaturę na ostatnie 3 zalania do 89 stopni. Dużo słodszy profil niż wczoraj."
        ),

        BrewLog(
            id = "log_7",
            brewMethod = BrewMethod.AEROPRESS,
            date = LocalDate.of(2026, 11, 5),
            time = "09:00",
            coffeeBag = bagColombiaCoffeeLab,
            recipeId = "recipe_hedrick_aeropress",
            recipeTitle = "Lance Hedrick – Daily AeroPress",
            grinder = Grinder.NICHE_ZERO,
            grindSetting = "Ustawienie 40",
            tastingNotes = "Szybka kawa przed pracą. Bardzo zbalansowana, mleczna czekolada i cytrusy."
        ),

        // --- 6 NOVEMBER 2026 (Friday - 3 entries) ---
        BrewLog(
            id = "log_8",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 11, 6),
            time = "09:45",
            coffeeBag = bagBrazilPalomaKonesso,
            recipeId = "recipe_hoffmann_v60_2cup",
            recipeTitle = "James Hoffmann – Ultimate 2-Cup V60",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "26 klików",
            tastingNotes = "Poranna porcja na dwie osoby (30g/500ml). Przepływ idealny w 3:20."
        ),
        BrewLog(
            id = "log_9",
            brewMethod = BrewMethod.AEROPRESS,
            date = LocalDate.of(2026, 11, 6),
            time = "13:20",
            coffeeBag = bagBrazilPalomaKonesso,
            recipeId = "recipe_gagne_10min_aeropress",
            recipeTitle = "Jonathan Gagné – 10-Minute AeroPress",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "19 klików",
            tastingNotes = "Weekendowy eksperyment z 10-minutowym parzeniem. Niesamowita słodycz, zero goryczy mimo wrzątku."
        ),
        BrewLog(
            id = "log_10",
            brewMethod = BrewMethod.AEROPRESS,
            date = LocalDate.of(2026, 11, 6),
            time = "16:50",
            coffeeBag = bagPeruSmok,
            recipeId = "recipe_adler_original_aeropress",
            recipeTitle = "Alan Adler – Oryginalny Przepis (80°C)",
            grinder = Grinder.NICHE_ZERO,
            grindSetting = "Ustawienie 24",
            tastingNotes = "Wersja z dolewką wody (bypass). Bardzo delikatna i herbaciana."
        ),

        // --- 7 NOVEMBER 2026 (Saturday - 1 entry) ---
        BrewLog(
            id = "log_11",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 11, 7),
            time = "10:10",
            coffeeBag = bagPeruSmok,
            recipeId = "recipe_katsuya_old_46",
            recipeTitle = "Tetsu Katsuya – Stary V60 (Metoda 4:6)",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "28 klików",
            tastingNotes = "Pierwsze zalanie 50ml podbiło słodycz. Bardzo soczysty napar."
        ),

        // --- 8 NOVEMBER 2026 (Sunday - 1 entry) ---
        BrewLog(
            id = "log_12",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 11, 8),
            time = "08:10",
            coffeeBag = bagPeruSmok,
            recipeId = "recipe_hedrick_121_v60",
            recipeTitle = "Lance Hedrick – 1-2-1 V60",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "21 klików",
            tastingNotes = "2-minutowy bloom wyciągnął mnóstwo owocowości."
        ),

        // --- 9 NOVEMBER 2026 (Monday - 2 entries) ---
        BrewLog(
            id = "log_13",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 11, 9),
            time = "08:25",
            coffeeBag = bagEthiopiaLacava,
            recipeId = "recipe_katsuya_new_v60",
            recipeTitle = "Tetsu Katsuya – Nowy V60 (Uproszczony)",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "23 kliki",
            tastingNotes = "Temperatura 91 stopni. Bardzo gładkie body."
        ),
        BrewLog(
            id = "log_14",
            brewMethod = BrewMethod.AEROPRESS,
            date = LocalDate.of(2026, 11, 9),
            time = "15:15",
            coffeeBag = bagKenyaGrange,
            recipeId = "recipe_van_bunnik_wac_aeropress",
            recipeTitle = "Wendelien van Bunnik – WAC Champion (Inverted)",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "27 klików",
            tastingNotes = "Szalona doza 30g kawy. Użyłem dwóch papierowych filtrów, wyszedł prawdziwy nektar porzeczkowy."
        ),

        // (4 November intentionally left empty - no entries)

        // --- 11 NOVEMBER 2026 (Wednesday - 1 entry) ---
        BrewLog(
            id = "log_15",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 11, 11),
            time = "08:00",
            coffeeBag = bagColombiaCoffeeLab,
            recipeId = "recipe_katsuya_new_v60",
            recipeTitle = "Tetsu Katsuya – Nowy V60 (Uproszczony)",
            grinder = Grinder.NICHE_ZERO,
            grindSetting = "Ustawienie 42",
            tastingNotes = "Świetny balans, lekka kwasowość limonki na finiszu."
        ),

        // 7 NOVEMBER 2026 (Saturday - 2 entries)

        // --- 13 NOVEMBER 2026 (Friday - 2 entries) ---
        BrewLog(
            id = "log_16",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 11, 13),
            time = "09:30",
            coffeeBag = bagPeruSmok,
            recipeId = "recipe_hoffmann_v60_1cup",
            recipeTitle = "James Hoffmann – 1-Cup V60",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "22 kliki",
            tastingNotes = "Peru Geisha po ponad 2 tygodniach od wypału jest w szczytowej formie. Czysty kwiat pomarańczy."
        ),
        BrewLog(
            id = "log_17",
            brewMethod = BrewMethod.AEROPRESS,
            date = LocalDate.of(2026, 11, 13),
            time = "15:00",
            coffeeBag = bagBrazilPalomaKonesso,
            recipeId = "recipe_hoffmann_aeropress",
            recipeTitle = "James Hoffmann – Ultimate AeroPress",
            grinder = Grinder.NICHE_ZERO,
            grindSetting = "Ustawienie 35",
            tastingNotes = "Popołudniowa Brazylia do ciasta. Bardzo słodka, nugatowa."
        ),

        // 9 NOVEMBER 2026 (Monday - 1 entry)

        // --- 14 NOVEMBER 2026 (Saturday - 1 entry) ---
        BrewLog(
            id = "log_18",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 11, 14),
            time = "07:45",
            coffeeBag = bagKenyaGrange,
            recipeId = "recipe_hoffmann_v60_1cup",
            recipeTitle = "James Hoffmann – 1-Cup V60",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "23 kliki",
            tastingNotes = "Szybkie poranne parzenie przed wyjściem. Wyraźna kwasowość rabarbaru."
        ),

        // 11 NOVEMBER 2026 (Wednesday - Holiday - 2 entries)

        // --- 16 NOVEMBER 2026 (Monday - 2 entries) ---
        BrewLog(
            id = "log_19",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 11, 16),
            time = "10:15",
            coffeeBag = bagColombiaCoffeeLab,
            recipeId = "recipe_hoffmann_v60_2cup",
            recipeTitle = "James Hoffmann – Ultimate 2-Cup V60",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "25 klików",
            tastingNotes = "Duży przelew do rogali świętomarcińskich. SL28 świetnie przecina słodycz wypieków."
        ),
        BrewLog(
            id = "log_20",
            brewMethod = BrewMethod.AEROPRESS,
            date = LocalDate.of(2026, 11, 16),
            time = "16:10",
            coffeeBag = bagEthiopiaLacava,
            recipeId = "recipe_wendelboe_aeropress",
            recipeTitle = "Tim Wendelboe – Klasyczny AeroPress",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "19 klików",
            tastingNotes = "Ostatnia porcja Etiopii z tej paczki (wyszło 13.5g zamiast 14g, więc odjąłem 10ml wody)."
        ),

        // 14 NOVEMBER 2026 (Saturday - 1 entry)

        // --- 19 NOVEMBER 2026 (Thursday - 1 entry) ---
        BrewLog(
            id = "log_21",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 11, 19),
            time = "09:20",
            coffeeBag = bagPeruSmok,
            recipeId = "recipe_scott_rao_v60",
            recipeTitle = "Scott Rao – Classic V60 (Rao Spin)",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "23 kliki",
            tastingNotes = "Idealnie płaskie złoże po Rao Spinie. Czas całkowity 2:50."
        ),

        // 16 NOVEMBER 2026 (Monday - 1 entry)

        // --- 21 NOVEMBER 2026 (Saturday - 1 entry) ---
        BrewLog(
            id = "log_22",
            brewMethod = BrewMethod.AEROPRESS,
            date = LocalDate.of(2026, 11, 21),
            time = "08:15",
            coffeeBag = bagBrazilPalomaKonesso,
            recipeId = "recipe_hedrick_aeropress",
            recipeTitle = "Lance Hedrick – Daily AeroPress",
            grinder = Grinder.NICHE_ZERO,
            grindSetting = "Ustawienie 39",
            tastingNotes = "Powtarzalny, gęsty napar. Temperatura wody obniżona do 90 stopni."
        ),

        // 19 NOVEMBER 2026 (Thursday - 1 entry)

        // --- 23 NOVEMBER 2026 (Monday - 1 entry) ---
        BrewLog(
            id = "log_23",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 11, 23),
            time = "08:05",
            coffeeBag = bagColombiaCoffeeLab,
            recipeId = "recipe_matt_winton_5pour_v60",
            recipeTitle = "Matt Winton – Five Pour V60 (WBrC 2021)",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "24 kliki",
            tastingNotes = "5 zalań wydobyło z Kolumbii niesamowitą soczystość czerwonego jabłka."
        ),

        // 21 NOVEMBER 2026 (Saturday - 2 entries)

        // --- 25 NOVEMBER 2026 (Wednesday - 2 entries) ---
        BrewLog(
            id = "log_24",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 11, 25),
            time = "09:50",
            coffeeBag = bagKenyaGrange,
            recipeId = "recipe_katsuya_old_46",
            recipeTitle = "Tetsu Katsuya – Stary V60 (Metoda 4:6)",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "27 klików",
            tastingNotes = "Podział 50ml + 70ml na start. Bardzo słodka Kenia, nuty konfitury z dzikiej róży."
        ),
        BrewLog(
            id = "log_25",
            brewMethod = BrewMethod.AEROPRESS,
            date = LocalDate.of(2026, 11, 25),
            time = "14:30",
            coffeeBag = bagPeruSmok,
            recipeId = "recipe_hoffmann_aeropress",
            recipeTitle = "James Hoffmann – Ultimate AeroPress",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "18 klików",
            tastingNotes = "Geisha w AeroPressie – mniej klarowna niż w V60, ale za to bardziej kremowa (brzoskwiniowy jogurt)."
        ),

        // 24 NOVEMBER 2026 (Tuesday - 1 entry)

        // --- 28 NOVEMBER 2026 (Saturday - 1 entry) ---
        BrewLog(
            id = "log_26",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 11, 28),
            time = "08:10",
            coffeeBag = bagBrazilPalomaKonesso,
            recipeId = "recipe_katsuya_new_v60",
            recipeTitle = "Tetsu Katsuya – Nowy V60 (Uproszczony)",
            grinder = Grinder.NICHE_ZERO,
            grindSetting = "Ustawienie 44",
            tastingNotes = "Brazylia Arara w V60 na niskiej temperaturze (89°C). Przyjemny smak orzechów laskowych."
        ),

        // 26 NOVEMBER 2026 (Thursday - 1 entry)

        // --- 30 NOVEMBER 2026 (Monday - 1 entry) ---
        BrewLog(
            id = "log_27",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 11, 30),
            time = "07:55",
            coffeeBag = bagEthiopiaLacava,
            recipeId = "recipe_hedrick_121_v60",
            recipeTitle = "Lance Hedrick – 1-2-1 V60",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "20 klików",
            tastingNotes = "Otwarta druga paczka Kelloo Lalesa. Bardzo mocna agitacja przy preinfuzji, czas 2:45."
        ),

        // 28 NOVEMBER 2026 (Saturday - 1 entry)

        // --- 1 DECEMBER 2026 (Tuesday - 1 entry) ---
        BrewLog(
            id = "log_28",
            brewMethod = BrewMethod.AEROPRESS,
            date = LocalDate.of(2026, 12, 1),
            time = "10:30",
            coffeeBag = bagColombiaCoffeeLab,
            recipeId = "recipe_gagne_10min_aeropress",
            recipeTitle = "Jonathan Gagné – 10-Minute AeroPress",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "20 klików",
            tastingNotes = "Długie parzenie w sobotni poranek. Kolumbia wyszła niesamowicie słodka, jak kompot truskawkowy."
        ),

        // 30 NOVEMBER 2026 (Monday - 1 entry)

        // --- 3 DECEMBER 2026 (Thursday - 1 entry) ---
        BrewLog(
            id = "log_29",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 12, 3),
            time = "08:20",
            coffeeBag = bagKenyaGrange,
            recipeId = "recipe_hoffmann_v60_1cup",
            recipeTitle = "James Hoffmann – 1-Cup V60",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "23 kliki",
            tastingNotes = "Klasyczny przepis Hoffmanna nigdy nie zawodzi przy tej Kenii."
        ),

        // ==================== DECEMBER 2026 ====================

        // 3 DECEMBER 2026 (Thursday - 1 entry)

        // --- 6 DECEMBER 2026 (Sunday - 1 entry) ---
        BrewLog(
            id = "log_30",
            brewMethod = BrewMethod.AEROPRESS,
            date = LocalDate.of(2026, 12, 6),
            time = "08:15",
            coffeeBag = bagBrazilPalomaKonesso,
            recipeId = "recipe_adler_original_aeropress",
            recipeTitle = "Alan Adler – Oryginalny Przepis (80°C)",
            grinder = Grinder.NICHE_ZERO,
            grindSetting = "Ustawienie 25",
            tastingNotes = "Szybki koncentrat rozcieńczony gorącą wodą. Bardzo niskie pH, delikatna dla żołądka."
        ),

        // 5 DECEMBER 2026 (Saturday - 2 entries)

        // --- 8 DECEMBER 2026 (Tuesday - 2 entries) ---
        BrewLog(
            id = "log_31",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 12, 8),
            time = "09:40",
            coffeeBag = bagPeruSmok,
            recipeId = "recipe_matt_winton_5pour_v60",
            recipeTitle = "Matt Winton – Five Pour V60 (WBrC 2021)",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "24 kliki",
            tastingNotes = "Geisha parzona metodą 5 zalań. Niesamowita klarowność, w smaku biała brzoskwinia i trawa cytrynowa."
        ),
        BrewLog(
            id = "log_32",
            brewMethod = BrewMethod.AEROPRESS,
            date = LocalDate.of(2026, 12, 8),
            time = "15:20",
            coffeeBag = bagColombiaCoffeeLab,
            recipeId = "recipe_van_bunnik_wac_aeropress",
            recipeTitle = "Wendelien van Bunnik – WAC Champion (Inverted)",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "26 klików",
            tastingNotes = "Intensywny, gęsty napar na popołudnie."
        ),

        // 8 DECEMBER 2026 (Tuesday - 1 entry)

        // --- 10 DECEMBER 2026 (Thursday - 1 entry) ---
        BrewLog(
            id = "log_33",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 12, 10),
            time = "08:00",
            coffeeBag = bagEthiopiaLacava,
            recipeId = "recipe_scott_rao_v60",
            recipeTitle = "Scott Rao – Classic V60 (Rao Spin)",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "23 kliki",
            tastingNotes = "Przy używaniu szybszych filtrów Cafec Abaca musiałem zmielić o klik drobniej (23 zamiast 24)."
        ),

        // 10 DECEMBER 2026 (Thursday - 1 entry)

        // --- 12 DECEMBER 2026 (Saturday - 1 entry) ---
        BrewLog(
            id = "log_34",
            brewMethod = BrewMethod.AEROPRESS,
            date = LocalDate.of(2026, 12, 12),
            time = "08:30",
            coffeeBag = bagKenyaGrange,
            recipeId = "recipe_wendelboe_aeropress",
            recipeTitle = "Tim Wendelboe – Klasyczny AeroPress",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "18 klików",
            tastingNotes = "Świetna równowaga między wysoką kwasowością Kenii a pełnym body z AeroPressu."
        ),

        // 13 DECEMBER 2026 (Sunday - 1 entry)

        // --- 15 DECEMBER 2026 (Tuesday - 1 entry) ---
        BrewLog(
            id = "log_35",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 12, 15),
            time = "10:15",
            coffeeBag = bagColombiaCoffeeLab,
            recipeId = "recipe_katsuya_old_46",
            recipeTitle = "Tetsu Katsuya – Stary V60 (Metoda 4:6)",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "28 klików",
            tastingNotes = "Niedzielny klasyk 4:6. Bardzo czysta i słodka filiżanka."
        ),

        // 16 DECEMBER 2026 (Wednesday - 1 entry)

        // --- 18 DECEMBER 2026 (Friday - 1 entry) ---
        BrewLog(
            id = "log_36",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 12, 18),
            time = "07:50",
            coffeeBag = bagEthiopiaLacava,
            recipeId = "recipe_hoffmann_v60_1cup",
            recipeTitle = "James Hoffmann – 1-Cup V60",
            grinder = Grinder.NICHE_ZERO,
            grindSetting = "Ustawienie 40",
            tastingNotes = "Porównanie Etiopii na żarnach stożkowych Niche Zero – mniej kwiatów, więcej słodkiej brzoskwini w syropie."
        ),

        // 19 DECEMBER 2026 (Saturday - 2 entries)

        // --- 20 DECEMBER 2026 (Sunday - 2 entries) ---
        BrewLog(
            id = "log_37",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 12, 20),
            time = "09:10",
            coffeeBag = bagPeruSmok,
            recipeId = "recipe_hedrick_121_v60",
            recipeTitle = "Lance Hedrick – 1-2-1 V60",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "21 klików",
            tastingNotes = "Geisha w metodzie 1-2-1. Kwiatowy aromat unosił się w całej kuchni."
        ),
        BrewLog(
            id = "log_38",
            brewMethod = BrewMethod.AEROPRESS,
            date = LocalDate.of(2026, 12, 20),
            time = "14:45",
            coffeeBag = bagBrazilPalomaKonesso,
            recipeId = "recipe_hoffmann_aeropress",
            recipeTitle = "James Hoffmann – Ultimate AeroPress",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "19 klików",
            tastingNotes = "Klasyczna czekoladowa Brazylia po południu."
        ),

        // 21 DECEMBER 2026 (Monday - 1 entry)

        // --- 22 DECEMBER 2026 (Tuesday - 1 entry) ---
        BrewLog(
            id = "log_39",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 12, 22),
            time = "08:15",
            coffeeBag = bagKenyaGrange,
            recipeId = "recipe_katsuya_new_v60",
            recipeTitle = "Tetsu Katsuya – Nowy V60 (Uproszczony)",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "24 kliki",
            tastingNotes = "Trzy zalania w 92 stopniach. Bardzo soczysty finisz czerwonej porzeczki."
        ),

        // 24 DECEMBER 2026 (Thursday - Christmas Eve - 1 entry)

        // --- 25 DECEMBER 2026 (Friday - 1 entry) ---
        BrewLog(
            id = "log_40",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 12, 25),
            time = "09:30",
            coffeeBag = bagPeruSmok,
            recipeId = "recipe_hoffmann_v60_2cup",
            recipeTitle = "James Hoffmann – Ultimate 2-Cup V60",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "25 klików",
            tastingNotes = "Świąteczny przelew z Peru Geisha na 500 ml. Idealny start Wigilii."
        ),

        // 26 DECEMBER 2026 (Saturday - Boxing Day - 2 entries)

        // --- 27 DECEMBER 2026 (Sunday - 2 entries) ---
        BrewLog(
            id = "log_41",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 12, 27),
            time = "10:20",
            coffeeBag = bagColombiaCoffeeLab,
            recipeId = "recipe_hoffmann_v60_2cup",
            recipeTitle = "James Hoffmann – Ultimate 2-Cup V60",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "26 klików",
            tastingNotes = "Duża karafka Kolumbii SL28 dla gości po śniadaniu. Wszyscy zachwyceni owocowym profilem."
        ),
        BrewLog(
            id = "log_42",
            brewMethod = BrewMethod.AEROPRESS,
            date = LocalDate.of(2026, 12, 27),
            time = "16:00",
            coffeeBag = bagBrazilPalomaKonesso,
            recipeId = "recipe_hedrick_aeropress",
            recipeTitle = "Lance Hedrick – Daily AeroPress",
            grinder = Grinder.NICHE_ZERO,
            grindSetting = "Ustawienie 38",
            tastingNotes = "Brazylia do świątecznego sernika – pasuje bezbłędnie."
        ),

        // 29 DECEMBER 2026 (Tuesday - 1 entry)

        // --- 29 DECEMBER 2026 (Tuesday - 1 entry) ---
        BrewLog(
            id = "log_43",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 12, 29),
            time = "08:40",
            coffeeBag = bagEthiopiaLacava,
            recipeId = "recipe_matt_winton_5pour_v60",
            recipeTitle = "Matt Winton – Five Pour V60 (WBrC 2021)",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "24 kliki",
            tastingNotes = "Spokojny poranek między świętami a Nowym Rokiem. Bergamotka i jaśmin."
        ),

        // 31 DECEMBER 2026 (Thursday - New Year's Eve - 2 entries)

        // --- 31 DECEMBER 2026 (Thursday - 2 entries) ---
        BrewLog(
            id = "log_44",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 12, 31),
            time = "09:15",
            coffeeBag = bagKenyaGrange,
            recipeId = "recipe_scott_rao_v60",
            recipeTitle = "Scott Rao – Classic V60 (Rao Spin)",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "23 kliki",
            tastingNotes = "Poranna Kenia na pobudzenie przed sylwestrowym wieczorem."
        ),
        BrewLog(
            id = "log_45",
            brewMethod = BrewMethod.AEROPRESS,
            date = LocalDate.of(2026, 12, 31),
            time = "18:30",
            coffeeBag = bagPeruSmok,
            recipeId = "recipe_gagne_10min_aeropress",
            recipeTitle = "Jonathan Gagné – 10-Minute AeroPress",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "19 klików",
            tastingNotes = "Ostatnia kawa w 2026 roku! Peru Geisha parzona przez 10 minut – słodka, złożona i pełna kofeiny na całą noc."
        )
    )
}
