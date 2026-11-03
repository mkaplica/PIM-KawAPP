package pl.betoniarze.kawapp.core.data.fake

import pl.betoniarze.kawapp.core.model.BrewMethod
import pl.betoniarze.kawapp.core.model.BrewStep
import pl.betoniarze.kawapp.core.model.CoffeeRecipe
import pl.betoniarze.kawapp.core.model.FeedPost

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
        ),

        // 6. Tim Wendelboe – Skandynawski Klasyk
        CoffeeRecipe(
            id = "recipe_wendelboe_aeropress",
            title = "Tim Wendelboe – Klasyczny AeroPress",
            brewMethod = BrewMethod.AEROPRESS,
            coffeeGrams = 14,
            totalWaterMl = 200,
            notes = "Przepis mistrza świata baristów z Oslo pod jasno palone ziarna. Metoda klasyczna (nieodwrócona), drobne mielenie i dokładnie 3 zamieszania w przód i w tył.",
            steps = listOf(
                BrewStep(
                    stepNumber = 1,
                    description = "Przepłucz papierowy filtr gorącą wodą, wsyp kawę i wlej całą wodę (200 ml) o temperaturze ok. 96°C.",
                    waterAmountMl = 200,
                    timeRange = "0:00 - 0:15"
                ),
                BrewStep(
                    stepNumber = 2,
                    description = "Zamieszaj delikatnie łyżeczką lub mieszadłem dokładnie 3 razy (w przód i w tył), a następnie załóż tłok na górę, aby kawa nie kapała.",
                    waterAmountMl = 0,
                    timeRange = "0:15 - 1:15"
                ),
                BrewStep(
                    stepNumber = 3,
                    description = "Zdejmij tłok, zamieszaj ponownie 3 razy ruchem przód-tył, załóż tłok i przeciskaj jednostajnie, opierając ciężar przedramion.",
                    waterAmountMl = 0,
                    timeRange = "1:15 - 1:45"
                )
            )
        ),

        // 7. Alan Adler – Oryginalny przepis twórcy AeroPressu (z dolewką / Bypass)
        CoffeeRecipe(
            id = "recipe_adler_original_aeropress",
            title = "Alan Adler – Oryginalny Przepis (80°C)",
            brewMethod = BrewMethod.AEROPRESS,
            coffeeGrams = 16,
            totalWaterMl = 200,
            notes = "Przepis samego wynalazcy AeroPressu. Bardzo drobne mielenie (prawie jak do espresso) i niska temperatura wody (80°C dla ciemniejszych paleń, 85°C dla jasnych). Najpierw parzymy gęsty koncentrat, a potem dolewamy gorącej wody.",
            steps = listOf(
                BrewStep(
                    stepNumber = 1,
                    description = "Wsyp drobno zmieloną kawę i wlej 80 ml wody o temperaturze 80-85°C (do poziomu '1' na ściance AeroPressu).",
                    waterAmountMl = 80,
                    timeRange = "0:00 - 0:10"
                ),
                BrewStep(
                    stepNumber = 2,
                    description = "Energicznie mieszaj dołączonym wiosłem (paddle) przez równe 10 sekund.",
                    waterAmountMl = 0,
                    timeRange = "0:10 - 0:20"
                ),
                BrewStep(
                    stepNumber = 3,
                    description = "Włóż tłok i delikatnie przeciskaj koncentrat bezpośrednio do kubka.",
                    waterAmountMl = 0,
                    timeRange = "0:20 - 0:50"
                ),
                BrewStep(
                    stepNumber = 4,
                    description = "Bypass: Dolej 120 ml czystej, gorącej wody bezpośrednio do gotowego koncentratu w kubku i wymieszaj.",
                    waterAmountMl = 120,
                    timeRange = "0:50 - 1:00"
                )
            )
        ),

        // 8. Jonathan Gagné – 10-minutowy AeroPress
        CoffeeRecipe(
            id = "recipe_gagne_10min_aeropress",
            title = "Jonathan Gagné – 10-Minute AeroPress",
            brewMethod = BrewMethod.AEROPRESS,
            coffeeGrams = 15,
            totalWaterMl = 250,
            notes = "Przepis astrofizyka i autora książki 'The Physics of Filter Coffee'. Ekstremalnie długi czas parzenia eliminuje problem nierównej ekstrakcji i daje niesamowicie słodki, pełny napar bez goryczy. Wrzątek (100°C) i drobne mielenie.",
            steps = listOf(
                BrewStep(
                    stepNumber = 1,
                    description = "Wsyp kawę i wlej 250 ml wrzącej wody. Delikatnie zamieszaj ruchem przód-tył i od razu włóż tłok, aby utworzyć podciśnienie.",
                    waterAmountMl = 250,
                    timeRange = "0:00 - 0:30"
                ),
                BrewStep(
                    stepNumber = 2,
                    description = "Pierwsza faza parzenia. Zostaw AeroPress w spokoju.",
                    waterAmountMl = 0,
                    timeRange = "0:30 - 5:00"
                ),
                BrewStep(
                    stepNumber = 3,
                    description = "Delikatnie zakręć całym AeroPressem (swirl), aby opadła skorupa z kawy, i czekaj kolejne 4 minuty, aż drobiny osiądą na dnie.",
                    waterAmountMl = 0,
                    timeRange = "5:00 - 9:00"
                ),
                BrewStep(
                    stepNumber = 4,
                    description = "Bardzo powoli i delikatnie przeciskaj tłok przez pełną minutę.",
                    waterAmountMl = 0,
                    timeRange = "9:00 - 10:00"
                )
            )
        ),

        // 9. Lance Hedrick – Codzienny przepis (Metoda klasyczna)
        CoffeeRecipe(
            id = "recipe_hedrick_aeropress",
            title = "Lance Hedrick – Daily AeroPress",
            brewMethod = BrewMethod.AEROPRESS,
            coffeeGrams = 15,
            totalWaterMl = 250,
            notes = "Szybki, powtarzalny przepis dający bardzo czysty napar. Mielenie średnio-drobne (jak do V60). Temperatura wody ok. 93-95°C dla jasnych ziaren.",
            steps = listOf(
                BrewStep(
                    stepNumber = 1,
                    description = "Wlej całą wodę (250 ml) dość szybkim strumieniem w ciągu 15-20 sekund.",
                    waterAmountMl = 250,
                    timeRange = "0:00 - 0:20"
                ),
                BrewStep(
                    stepNumber = 2,
                    description = "Zrób 2 delikatne ruchy łyżeczką i załóż tłok na górę komory.",
                    waterAmountMl = 0,
                    timeRange = "0:20 - 1:30"
                ),
                BrewStep(
                    stepNumber = 3,
                    description = "Zakręć lekko AeroPressem (swirl), odczekaj 15 sekund na opadnięcie drobin i powoli przeciskaj przez 30-45 sekund.",
                    waterAmountMl = 0,
                    timeRange = "1:30 - 2:30"
                )
            )
        ),

        // 10. Wendelien van Bunnik – Mistrzyni Świata WAC 2019 (Metoda odwrócona + Bypass)
        CoffeeRecipe(
            id = "recipe_van_bunnik_wac_aeropress",
            title = "Wendelien van Bunnik – WAC Champion (Inverted)",
            brewMethod = BrewMethod.AEROPRESS,
            coffeeGrams = 30,
            totalWaterMl = 220,
            notes = "Mistrzowski przepis z World AeroPress Championship 2019. Metoda ODWRÓCONA (Inverted). Bardzo duża doza grubo zmielonej kawy (30g!), krótki czas kontaktu z wodą (92°C) i duża dolewka czystej wody na koniec.",
            steps = listOf(
                BrewStep(
                    stepNumber = 1,
                    description = "Ustaw AeroPress w pozycji odwróconej. Wsyp 30g grubo zmielonej kawy i wlej 100 ml wody w ciągu 10 sekund.",
                    waterAmountMl = 100,
                    timeRange = "0:00 - 0:10"
                ),
                BrewStep(
                    stepNumber = 2,
                    description = "Mieszaj energicznie drewnianymi pałeczkami lub mieszadłem przez 20 sekund, a następnie zakręć sitko z dwoma przepłukanymi filtrami.",
                    waterAmountMl = 0,
                    timeRange = "0:10 - 0:30"
                ),
                BrewStep(
                    stepNumber = 3,
                    description = "W 0:30 odwróć AeroPress na serwer i od razu przeciskaj z dużą siłą przez 30 sekund.",
                    waterAmountMl = 0,
                    timeRange = "0:30 - 1:00"
                ),
                BrewStep(
                    stepNumber = 4,
                    description = "Bypass: Dolej 120 ml gorącej wody do uzyskanego ekstraktu (do smaku).",
                    waterAmountMl = 120,
                    timeRange = "1:00 - 1:10"
                )
            )
        ),

        // 11. Scott Rao – Klasyczna metoda V60 (Rao Spin)
        CoffeeRecipe(
            id = "recipe_scott_rao_v60",
            title = "Scott Rao – Classic V60 (Rao Spin)",
            brewMethod = BrewMethod.V60,
            coffeeGrams = 22,
            totalWaterMl = 360,
            notes = "Jeden z najbardziej wpływowych przepisów w historii kawy specialty. Popularyzuje tzw. 'Rao Spin' (zakręcenie dripperem w celu uzyskania idealnie płaskiego złoża kawy) oraz wykopanie wgłębienia łyżeczką podczas preinfuzji.",
            steps = listOf(
                BrewStep(
                    stepNumber = 1,
                    description = "Preinfuzja (3x waga kawy): Wlej 66 ml wody i delikatnie przemieszaj kawę łyżeczką od dna ('excavate'), aby nie zostały suche grudki.",
                    waterAmountMl = 66,
                    timeRange = "0:00 - 0:45"
                ),
                BrewStep(
                    stepNumber = 2,
                    description = "Główne zalanie: Wlej pozostałe 294 ml wody jednym płynnym, kolistym strumieniem (łącznie 360 ml na wadze).",
                    waterAmountMl = 294,
                    timeRange = "0:45 - 1:35"
                ),
                BrewStep(
                    stepNumber = 3,
                    description = "Zamieszaj lekko łyżeczką przy ściankach, wykonaj 'Rao Spin' (zakręć dripperem) i czekaj na całkowite przelanie wody (płaskie złoże na dnie).",
                    waterAmountMl = 0,
                    timeRange = "1:35 - 3:00"
                )
            )
        ),

        // 12. Lance Hedrick – Metoda 1-2-1 V60
        CoffeeRecipe(
            id = "recipe_hedrick_121_v60",
            title = "Lance Hedrick – 1-2-1 V60",
            brewMethod = BrewMethod.V60,
            coffeeGrams = 15,
            totalWaterMl = 250,
            notes = "Nazwa '1-2-1' oznacza: 1 preinfuzja, 2 minuty czekania na pełne odgazowanie i 1 główne zalanie. Długi bloom wyciąga maksimum słodyczy z bardzo jasno palonych ziaren.",
            steps = listOf(
                BrewStep(
                    stepNumber = 1,
                    description = "Długa preinfuzja: Wlej 45 ml wody, agresywnie zakręć dripperem i czekaj aż do 2:00 na stoperze!",
                    waterAmountMl = 45,
                    timeRange = "0:00 - 2:00"
                ),
                BrewStep(
                    stepNumber = 2,
                    description = "Jedno ciągłe zalanie: Wlewaj pozostałe 205 ml wody szybkim strumieniem (6-8 ml/s) kolistymi ruchami aż do osiągnięcia 250 ml.",
                    waterAmountMl = 205,
                    timeRange = "2:00 - 2:35"
                ),
                BrewStep(
                    stepNumber = 3,
                    description = "Zakręć delikatnie dripperem, aby wyrównać pył na ściankach, i czekaj na przesączenie.",
                    waterAmountMl = 0,
                    timeRange = "2:35 - 3:30"
                )
            )
        ),

        // 13. Matt Winton – Mistrz Świata Brewers Cup 2021 (Metoda 5 zalań)
        CoffeeRecipe(
            id = "recipe_matt_winton_5pour_v60",
            title = "Matt Winton – Five Pour V60 (WBrC 2021)",
            brewMethod = BrewMethod.V60,
            coffeeGrams = 20,
            totalWaterMl = 300,
            notes = "Przepis Mistrza Świata Brewers Cup 2021. Wykorzystuje 5 zalań, przy czym każde kolejne wykonujemy w momencie, gdy woda z poprzedniego niemal dotyka powierzchni kawy (zaczyna kapać zamiast lecieć ciurkiem). W wersji mistrzowskiej pierwsze 2 zalania mają 93°C, a kolejne 88°C.",
            steps = listOf(
                BrewStep(
                    stepNumber = 1,
                    description = "Pierwsze zalanie (Bloom): Wlej 50 ml wody z niewielkiej wysokości i czekaj, aż spłynie do poziomu kawy.",
                    waterAmountMl = 50,
                    timeRange = "0:00 - 0:35"
                ),
                BrewStep(
                    stepNumber = 2,
                    description = "Drugie zalanie: Wlej 70 ml wody (łącznie 120 ml na wadze).",
                    waterAmountMl = 70,
                    timeRange = "0:35 - 1:10"
                ),
                BrewStep(
                    stepNumber = 3,
                    description = "Trzecie zalanie: Wlej 60 ml wody (łącznie 180 ml na wadze).",
                    waterAmountMl = 60,
                    timeRange = "1:10 - 1:45"
                ),
                BrewStep(
                    stepNumber = 4,
                    description = "Czwarte zalanie: Wlej 60 ml wody (łącznie 240 ml na wadze).",
                    waterAmountMl = 60,
                    timeRange = "1:45 - 2:20"
                ),
                BrewStep(
                    stepNumber = 5,
                    description = "Piąte zalanie: Wlej ostatnie 60 ml (łącznie 300 ml) i pozwól wodzie całkowicie spłynąć bez mieszania.",
                    waterAmountMl = 60,
                    timeRange = "2:20 - 3:15"
                )
            )
        )

    )
}
