package pl.betoniarze.kawapp.core.data.fake

import pl.betoniarze.kawapp.core.model.BrewMethod
import pl.betoniarze.kawapp.core.model.BrewStep
import pl.betoniarze.kawapp.core.model.CoffeeRecipe
import pl.betoniarze.kawapp.core.model.FeedPost

object FakeCoffeeData {
    // --- 5 PRZYKŁADOWYCH POSTÓW NA EKRAN HOME ---
    val feedPosts = listOf(
        FeedPost(
            id = "post_1",
            authorName = "Michał Kowalski",
            authorAvatarUrl = "https://picsum.photos/seed/avatar1/200/200",
            content = "W końcu udało mi się ustawić idealny przemiał pod tę mytą Etiopię z lokalnej palarni. Na 22 klikach w Comandante wyszła czysta brzoskwinia i jaśmin!",
            imageUrls = listOf(
                "https://picsum.photos/seed/coffee1/800/600",
                "https://picsum.photos/seed/coffee2/800/600"
            )
        ),
        FeedPost(
            id = "post_2",
            authorName = "Karolina Nowak",
            authorAvatarUrl = "https://picsum.photos/seed/avatar2/200/200",
            content = "Poranny AeroPress w plenerze smakuje zupełnie inaczej. Korzystam dziś z przepisu Hoffmanna, bez odwracania (standardowa metoda) – zero stresu o poparzenie!",
            imageUrls = listOf(
                "https://picsum.photos/seed/aeropress1/800/600"
            )
        ),
        FeedPost(
            id = "post_3",
            authorName = "Bartek Wiśniewski (CoffeeGeek)",
            authorAvatarUrl = "https://picsum.photos/seed/avatar3/200/200",
            content = "Testuję dzisiaj metodę 4:6 od Tetsu Katsuyi na nowej Kenii. Zmniejszyłem pierwsze zalanie do 45 ml i kwasowość wystrzeliła w kosmos. Próbował ktoś?",
            imageUrls = emptyList() // Post tekstowy bez zdjęć - warto testować taki przypadek w UI!
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
            )
        ),
        FeedPost(
            id = "post_5",
            authorName = "Tomasz Kamiński",
            authorAvatarUrl = "https://picsum.photos/seed/avatar5/200/200",
            content = "Szybkie porównanie papierowych filtrów Hario z fabryki w Japonii vs Holandii. Różnica w czasie przepływu to prawie 25 sekund przy tym samym mieleniu.",
            imageUrls = listOf(
                "https://picsum.photos/seed/v60filter/800/600"
            )
        )
    )

    // --- 5 PRZYKŁADOWYCH PRZEPISÓW PARZENIA KAWY ---
    val coffeeRecipes = listOf(
        // 1. James Hoffmann - AeroPress
        CoffeeRecipe(
            id = "recipe_hoffmann_aeropress",
            title = "James Hoffmann – Ultimate AeroPress",
            brewMethod = BrewMethod.AEROPRESS,
            coffeeGrams = 11,
            totalWaterMl = 200,
            notes = "Metoda klasyczna (nieodwrócona). Mielenie dość drobne (bliżej espresso niż przelewu). Woda dla jasnych paleń: 95-99°C. Kluczowe jest założenie tłoka od razu po zalaniu, by wytworzyć podciśnienie.",
            steps = listOf(
                BrewStep(
                    stepNumber = 1,
                    description = "Wsyp kawę na suchy filtr, wlej całą wodę równomiernie i od razu włóż tłok na górę na głębokość ok. 1 cm, aby zatrzymać kapanie.",
                    waterAmountMl = 200,
                    timeRange = "0:00 - 0:15"
                ),
                BrewStep(
                    stepNumber = 2,
                    description = "Pozostaw kawę do zaparzenia bez mieszania.",
                    waterAmountMl = 0,
                    timeRange = "0:15 - 2:00"
                ),
                BrewStep(
                    stepNumber = 3,
                    description = "Chwyć AeroPress wraz z tłokiem i delikatnie zakręć nim kółko (swirl), aby opadła górna warstwa kawy. Odczekaj 30 sekund.",
                    waterAmountMl = 0,
                    timeRange = "2:00 - 2:30"
                ),
                BrewStep(
                    stepNumber = 4,
                    description = "Przeciskaj bardzo powoli i delikatnie aż do usłyszenia syknięcia powietrza.",
                    waterAmountMl = 0,
                    timeRange = "2:30 - 3:00"
                )
            )
        ),

        // 2. James Hoffmann - 1 Cup V60
        CoffeeRecipe(
            id = "recipe_hoffmann_v60_1cup",
            title = "James Hoffmann – 1-Cup V60",
            brewMethod = BrewMethod.V60,
            coffeeGrams = 15,
            totalWaterMl = 250,
            notes = "Przepis stworzony specjalnie pod małe porcje (15g kawy). Podział na 5 równych zalań po 50 ml pozwala utrzymać wysoką temperaturę w małym dripperze i daje powtarzalną ekstrakcję.",
            steps = listOf(
                BrewStep(
                    stepNumber = 1,
                    description = "Preinfuzja (Bloom): Wlej 50 ml wody, delikatnie zakręć dripperem (swirl) i poczekaj na odgazowanie.",
                    waterAmountMl = 50,
                    timeRange = "0:00 - 0:45"
                ),
                BrewStep(
                    stepNumber = 2,
                    description = "Pierwsze zalanie kolistymi ruchami (łącznie 100 ml na wadze). Po zalaniu 10 sekund przerwy.",
                    waterAmountMl = 50,
                    timeRange = "0:45 - 1:10"
                ),
                BrewStep(
                    stepNumber = 3,
                    description = "Drugie zalanie (łącznie 150 ml na wadze) i 10 sekund przerwy.",
                    waterAmountMl = 50,
                    timeRange = "1:10 - 1:30"
                ),
                BrewStep(
                    stepNumber = 4,
                    description = "Trzecie zalanie (łącznie 200 ml na wadze) i 10 sekund przerwy.",
                    waterAmountMl = 50,
                    timeRange = "1:30 - 1:50"
                ),
                BrewStep(
                    stepNumber = 5,
                    description = "Ostatnie zalanie (łącznie 250 ml). Delikatnie zakręć dripperem i poczekaj na całkowite przelanie wody.",
                    waterAmountMl = 50,
                    timeRange = "1:50 - 3:00"
                )
            )
        ),

        // 3. James Hoffmann - 2 Cup V60
        CoffeeRecipe(
            id = "recipe_hoffmann_v60_2cup",
            title = "James Hoffmann – Ultimate 2-Cup V60",
            brewMethod = BrewMethod.V60,
            coffeeGrams = 30,
            totalWaterMl = 500,
            notes = "Klasyczny przepis na dwie porcje (lub jeden duży kubek). Opiera się na jednym długim, ciągłym zalaniu po preinfuzji. Mielenie średnio-drobne, woda blisko wrzenia.",
            steps = listOf(
                BrewStep(
                    stepNumber = 1,
                    description = "Preinfuzja: Wlej 60 ml wody (2x waga kawy), zakręć dripperem, aby namoczyć całe ziarno, i odczekaj.",
                    waterAmountMl = 60,
                    timeRange = "0:00 - 0:45"
                ),
                BrewStep(
                    stepNumber = 2,
                    description = "Główne zalanie (etap 1): Wlewaj wodę dość energicznie, aby osiągnąć 60% łącznej objętości (300 ml na wadze).",
                    waterAmountMl = 240,
                    timeRange = "0:45 - 1:15"
                ),
                BrewStep(
                    stepNumber = 3,
                    description = "Główne zalanie (etap 2): Zwolnij strumień i dolewaj wodę delikatniej, utrzymując wysoki poziom wody (do 500 ml na wadze).",
                    waterAmountMl = 200,
                    timeRange = "1:15 - 1:45"
                ),
                BrewStep(
                    stepNumber = 4,
                    description = "Zamieszaj delikatnie łyżeczką raz w lewo, raz w prawo, zakręć lekko dripperem i poczekaj, aż cała woda spłynie.",
                    waterAmountMl = 0,
                    timeRange = "1:45 - 3:30"
                )
            )
        ),

        // 4. Tetsu Katsuya - Stary przepis V60 (Metoda 4:6)
        CoffeeRecipe(
            id = "recipe_katsuya_old_46",
            title = "Tetsu Katsuya – Stary V60 (Metoda 4:6)",
            brewMethod = BrewMethod.V60,
            coffeeGrams = 20,
            totalWaterMl = 300,
            notes = "Mistrzowski przepis z 2016 roku. Wymaga grubego mielenia (jak do Chemexa). Pierwsze 40% wody (120 ml) steruje balansem słodyczy i kwasowości, a pozostałe 60% (180 ml) odpowiada za moc naparu. Przed każdym kolejnym zalaniem woda musi całkowicie spłynąć!",
            steps = listOf(
                BrewStep(
                    stepNumber = 1,
                    description = "Pierwsze zalanie (ustawia słodycz): Wlej 50 ml wody i czekaj, aż całkowicie spłynie.",
                    waterAmountMl = 50,
                    timeRange = "0:00 - 0:45"
                ),
                BrewStep(
                    stepNumber = 2,
                    description = "Drugie zalanie (domyka pierwsze 40% wody): Wlej 70 ml (łącznie 120 ml na wadze).",
                    waterAmountMl = 70,
                    timeRange = "0:45 - 1:30"
                ),
                BrewStep(
                    stepNumber = 3,
                    description = "Trzecie zalanie (budowanie mocy): Wlej 60 ml wody (łącznie 180 ml).",
                    waterAmountMl = 60,
                    timeRange = "1:30 - 2:10"
                ),
                BrewStep(
                    stepNumber = 4,
                    description = "Czwarte zalanie: Wlej 60 ml wody (łącznie 240 ml).",
                    waterAmountMl = 60,
                    timeRange = "2:10 - 2:45"
                ),
                BrewStep(
                    stepNumber = 5,
                    description = "Piąte zalanie: Wlej ostatnie 60 ml (łącznie 300 ml) i zdejmij dripper dokładnie w 3:30.",
                    waterAmountMl = 60,
                    timeRange = "2:45 - 3:30"
                )
            )
        ),

        // 5. Tetsu Katsuya - Nowy przepis V60
        CoffeeRecipe(
            id = "recipe_katsuya_new_v60",
            title = "Tetsu Katsuya – Nowy V60 (Uproszczony)",
            brewMethod = BrewMethod.V60,
            coffeeGrams = 20,
            totalWaterMl = 300,
            notes = "Odświeżone podejście Katsuyi pod nowoczesne, lepiej rozpuszczalne ziarna specialty. Mielenie średnie (drobniejsze niż w starej metodzie 4:6), temperatura ok. 91°C. Nie czekamy już na całkowite osuszenie kawy między zalaniami – daje to gładsze body i czystszą słodycz.",
            steps = listOf(
                BrewStep(
                    stepNumber = 1,
                    description = "Preinfuzja: Wlej 60 ml wody kolistymi ruchami i pozwól kawie odgazować.",
                    waterAmountMl = 60,
                    timeRange = "0:00 - 0:40"
                ),
                BrewStep(
                    stepNumber = 2,
                    description = "Drugie zalanie: Wlej 90 ml wody (łącznie 150 ml), zaczynając od środka i zataczając kółka.",
                    waterAmountMl = 90,
                    timeRange = "0:40 - 1:15"
                ),
                BrewStep(
                    stepNumber = 3,
                    description = "Trzecie, długie zalanie: Zanim poziom wody opadnie do końca, wlej pozostałe 150 ml delikatnym strumieniem głównie w środek drippera i czekaj na przesączenie.",
                    waterAmountMl = 150,
                    timeRange = "1:15 - 2:45"
                )
            )
        )
    )
}
