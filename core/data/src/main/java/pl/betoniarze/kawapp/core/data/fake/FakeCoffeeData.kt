package pl.betoniarze.kawapp.core.data.fake

import pl.betoniarze.kawapp.core.model.BrewLog
import pl.betoniarze.kawapp.core.model.BrewMethod
import pl.betoniarze.kawapp.core.model.BrewStep
import pl.betoniarze.kawapp.core.model.CoffeeBag
import pl.betoniarze.kawapp.core.model.CoffeeRecipe
import pl.betoniarze.kawapp.core.model.FeedPost
import pl.betoniarze.kawapp.core.model.Grinder
import java.time.LocalDate

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
        // --- 25 PAŹDZIERNIKA 2026 (Niedziela - 2 wpisy) ---
        BrewLog(
            id = "log_1",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 10, 25),
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
            date = LocalDate.of(2026, 10, 25),
            time = "14:40",
            coffeeBag = bagColombiaCoffeeLab,
            recipeId = "recipe_wendelboe_aeropress",
            recipeTitle = "Tim Wendelboe – Klasyczny AeroPress",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "18 klików",
            tastingNotes = "Dużo słodyczy, przypomina czerwone jabłko. Przeciskanie trwało 35 sekund."
        ),

        // --- 26 PAŹDZIERNIKA 2026 (Poniedziałek - 1 wpis) ---
        BrewLog(
            id = "log_3",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 10, 26),
            time = "08:05",
            coffeeBag = bagEthiopiaLacava,
            recipeId = "recipe_hoffmann_v60_1cup",
            recipeTitle = "James Hoffmann – 1-Cup V60",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "23 kliki",
            tastingNotes = "Po zmianie na 23 kliki zeszło idealnie w 2:55. Zniknęła lekka cierpkość w finiszu."
        ),

        // --- 27 PAŹDZIERNIKA 2026 (Wtorek - 1 wpis) ---
        BrewLog(
            id = "log_4",
            brewMethod = BrewMethod.AEROPRESS,
            date = LocalDate.of(2026, 10, 27),
            time = "08:30",
            coffeeBag = bagPeruSmok,
            recipeId = "recipe_hoffmann_aeropress",
            recipeTitle = "James Hoffmann – Ultimate AeroPress",
            grinder = Grinder.NICHE_ZERO,
            grindSetting = "Ustawienie 36",
            tastingNotes = "Test Rwandy na Niche Zero. Gęste body, truskawkowy dżem, trochę mniejsza klarowność niż z Comandante."
        ),

        // --- 28 PAŹDZIERNIKA 2026 (Środa - 1 wpis) ---
        BrewLog(
            id = "log_5",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 10, 28),
            time = "07:50",
            coffeeBag = bagKenyaGrange,
            recipeId = "recipe_scott_rao_v60",
            recipeTitle = "Scott Rao – Classic V60 (Rao Spin)",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "24 kliki",
            tastingNotes = "Wysoka kwasowość czarnej porzeczki. Woda 96 stopni."
        ),

        // --- 29 PAŹDZIERNIKA 2026 (Czwartek - 1 wpis) ---
        BrewLog(
            id = "log_6",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 10, 29),
            time = "08:15",
            coffeeBag = bagKenyaGrange,
            recipeId = "recipe_matt_winton_5pour_v60",
            recipeTitle = "Matt Winton – Five Pour V60 (WBrC 2021)",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "25 klików",
            tastingNotes = "Obniżyłem temperaturę na ostatnie 3 zalania do 89 stopni. Dużo słodszy profil niż wczoraj."
        ),

        // --- 30 PAŹDZIERNIKA 2026 (Piątek - 1 wpis) ---
        BrewLog(
            id = "log_7",
            brewMethod = BrewMethod.AEROPRESS,
            date = LocalDate.of(2026, 10, 30),
            time = "09:00",
            coffeeBag = bagColombiaCoffeeLab,
            recipeId = "recipe_hedrick_aeropress",
            recipeTitle = "Lance Hedrick – Daily AeroPress",
            grinder = Grinder.NICHE_ZERO,
            grindSetting = "Ustawienie 40",
            tastingNotes = "Szybka kawa przed pracą. Bardzo zbalansowana, mleczna czekolada i cytrusy."
        ),

        // --- 31 PAŹDZIERNIKA 2026 (Sobota - 3 wpisy!) ---
        BrewLog(
            id = "log_8",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 10, 31),
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
            date = LocalDate.of(2026, 10, 31),
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
            date = LocalDate.of(2026, 10, 31),
            time = "16:50",
            coffeeBag = bagPeruSmok,
            recipeId = "recipe_adler_original_aeropress",
            recipeTitle = "Alan Adler – Oryginalny Przepis (80°C)",
            grinder = Grinder.NICHE_ZERO,
            grindSetting = "Ustawienie 24",
            tastingNotes = "Wersja z dolewką wody (bypass). Bardzo delikatna i herbaciana."
        ),

        // --- 1 LISTOPADA 2026 (Niedziela - 1 wpis) ---
        BrewLog(
            id = "log_11",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 11, 1),
            time = "10:10",
            coffeeBag = bagPeruSmok,
            recipeId = "recipe_katsuya_old_46",
            recipeTitle = "Tetsu Katsuya – Stary V60 (Metoda 4:6)",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "28 klików",
            tastingNotes = "Pierwsze zalanie 50ml podbiło słodycz. Bardzo soczysty napar."
        ),

        // --- 2 LISTOPADA 2026 (Poniedziałek - 1 wpis) ---
        BrewLog(
            id = "log_12",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 11, 2),
            time = "08:10",
            coffeeBag = bagPeruSmok,
            recipeId = "recipe_hedrick_121_v60",
            recipeTitle = "Lance Hedrick – 1-2-1 V60",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "21 klików",
            tastingNotes = "2-minutowy bloom wyciągnął mnóstwo owocowości."
        ),

        // --- 3 LISTOPADA 2026 (Wtorek - 2 wpisy!) ---
        BrewLog(
            id = "log_13",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 11, 3),
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
            date = LocalDate.of(2026, 11, 3),
            time = "15:15",
            coffeeBag = bagKenyaGrange,
            recipeId = "recipe_van_bunnik_wac_aeropress",
            recipeTitle = "Wendelien van Bunnik – WAC Champion (Inverted)",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "27 klików",
            tastingNotes = "Szalona doza 30g kawy. Użyłem dwóch papierowych filtrów, wyszedł prawdziwy nektar porzeczkowy."
        ),

        // (4 listopada celowo pusty - brak wpisów)

        // --- 5 LISTOPADA 2026 (Czwartek - 1 wpis) ---
        BrewLog(
            id = "log_15",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 11, 5),
            time = "08:00",
            coffeeBag = bagColombiaCoffeeLab,
            recipeId = "recipe_katsuya_new_v60",
            recipeTitle = "Tetsu Katsuya – Nowy V60 (Uproszczony)",
            grinder = Grinder.NICHE_ZERO,
            grindSetting = "Ustawienie 42",
            tastingNotes = "Świetny balans, lekka kwasowość limonki na finiszu."
        ),

        // 7 LISTOPADA 2026 (Sobota - 2 wpisy)
        BrewLog(
            id = "log_16",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 11, 7),
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
            date = LocalDate.of(2026, 11, 7),
            time = "15:00",
            coffeeBag = bagBrazilPalomaKonesso,
            recipeId = "recipe_hoffmann_aeropress",
            recipeTitle = "James Hoffmann – Ultimate AeroPress",
            grinder = Grinder.NICHE_ZERO,
            grindSetting = "Ustawienie 35",
            tastingNotes = "Popołudniowa Brazylia do ciasta. Bardzo słodka, nugatowa."
        ),

        // 9 LISTOPADA 2026 (Poniedziałek - 1 wpis)
        BrewLog(
            id = "log_18",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 11, 9),
            time = "07:45",
            coffeeBag = bagKenyaGrange,
            recipeId = "recipe_hoffmann_v60_1cup",
            recipeTitle = "James Hoffmann – 1-Cup V60",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "23 kliki",
            tastingNotes = "Szybkie poranne parzenie przed wyjściem. Wyraźna kwasowość rabarbaru."
        ),

        // 11 LISTOPADA 2026 (Środa - Święto - 2 wpisy)
        BrewLog(
            id = "log_19",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 11, 11),
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
            date = LocalDate.of(2026, 11, 11),
            time = "16:10",
            coffeeBag = bagEthiopiaLacava,
            recipeId = "recipe_wendelboe_aeropress",
            recipeTitle = "Tim Wendelboe – Klasyczny AeroPress",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "19 klików",
            tastingNotes = "Ostatnia porcja Etiopii z tej paczki (wyszło 13.5g zamiast 14g, więc odjąłem 10ml wody)."
        ),

        // 14 LISTOPADA 2026 (Sobota - 1 wpis)
        BrewLog(
            id = "log_21",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 11, 14),
            time = "09:20",
            coffeeBag = bagPeruSmok,
            recipeId = "recipe_scott_rao_v60",
            recipeTitle = "Scott Rao – Classic V60 (Rao Spin)",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "23 kliki",
            tastingNotes = "Idealnie płaskie złoże po Rao Spinie. Czas całkowity 2:50."
        ),

        // 16 LISTOPADA 2026 (Poniedziałek - 1 wpis)
        BrewLog(
            id = "log_22",
            brewMethod = BrewMethod.AEROPRESS,
            date = LocalDate.of(2026, 11, 16),
            time = "08:15",
            coffeeBag = bagBrazilPalomaKonesso,
            recipeId = "recipe_hedrick_aeropress",
            recipeTitle = "Lance Hedrick – Daily AeroPress",
            grinder = Grinder.NICHE_ZERO,
            grindSetting = "Ustawienie 39",
            tastingNotes = "Powtarzalny, gęsty napar. Temperatura wody obniżona do 90 stopni."
        ),

        // 19 LISTOPADA 2026 (Czwartek - 1 wpis)
        BrewLog(
            id = "log_23",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 11, 19),
            time = "08:05",
            coffeeBag = bagColombiaCoffeeLab,
            recipeId = "recipe_matt_winton_5pour_v60",
            recipeTitle = "Matt Winton – Five Pour V60 (WBrC 2021)",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "24 kliki",
            tastingNotes = "5 zalań wydobyło z Kolumbii niesamowitą soczystość czerwonego jabłka."
        ),

        // 21 LISTOPADA 2026 (Sobota - 2 wpisy)
        BrewLog(
            id = "log_24",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 11, 21),
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
            date = LocalDate.of(2026, 11, 21),
            time = "14:30",
            coffeeBag = bagPeruSmok,
            recipeId = "recipe_hoffmann_aeropress",
            recipeTitle = "James Hoffmann – Ultimate AeroPress",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "18 klików",
            tastingNotes = "Geisha w AeroPressie – mniej klarowna niż w V60, ale za to bardziej kremowa (brzoskwiniowy jogurt)."
        ),

        // 24 LISTOPADA 2026 (Wtorek - 1 wpis)
        BrewLog(
            id = "log_26",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 11, 24),
            time = "08:10",
            coffeeBag = bagBrazilPalomaKonesso,
            recipeId = "recipe_katsuya_new_v60",
            recipeTitle = "Tetsu Katsuya – Nowy V60 (Uproszczony)",
            grinder = Grinder.NICHE_ZERO,
            grindSetting = "Ustawienie 44",
            tastingNotes = "Brazylia Arara w V60 na niskiej temperaturze (89°C). Przyjemny smak orzechów laskowych."
        ),

        // 26 LISTOPADA 2026 (Czwartek - 1 wpis)
        BrewLog(
            id = "log_27",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 11, 26),
            time = "07:55",
            coffeeBag = bagEthiopiaLacava,
            recipeId = "recipe_hedrick_121_v60",
            recipeTitle = "Lance Hedrick – 1-2-1 V60",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "20 klików",
            tastingNotes = "Otwarta druga paczka Kelloo Lalesa. Bardzo mocna agitacja przy preinfuzji, czas 2:45."
        ),

        // 28 LISTOPADA 2026 (Sobota - 1 wpis)
        BrewLog(
            id = "log_28",
            brewMethod = BrewMethod.AEROPRESS,
            date = LocalDate.of(2026, 11, 28),
            time = "10:30",
            coffeeBag = bagColombiaCoffeeLab,
            recipeId = "recipe_gagne_10min_aeropress",
            recipeTitle = "Jonathan Gagné – 10-Minute AeroPress",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "20 klików",
            tastingNotes = "Długie parzenie w sobotni poranek. Kolumbia wyszła niesamowicie słodka, jak kompot truskawkowy."
        ),

        // 30 LISTOPADA 2026 (Poniedziałek - 1 wpis)
        BrewLog(
            id = "log_29",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 11, 30),
            time = "08:20",
            coffeeBag = bagKenyaGrange,
            recipeId = "recipe_hoffmann_v60_1cup",
            recipeTitle = "James Hoffmann – 1-Cup V60",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "23 kliki",
            tastingNotes = "Klasyczny przepis Hoffmanna nigdy nie zawodzi przy tej Kenii."
        ),

        // ==================== GRUDZIEŃ 2026 ====================

        // 3 GRUDNIA 2026 (Czwartek - 1 wpis)
        BrewLog(
            id = "log_30",
            brewMethod = BrewMethod.AEROPRESS,
            date = LocalDate.of(2026, 12, 3),
            time = "08:15",
            coffeeBag = bagBrazilPalomaKonesso,
            recipeId = "recipe_adler_original_aeropress",
            recipeTitle = "Alan Adler – Oryginalny Przepis (80°C)",
            grinder = Grinder.NICHE_ZERO,
            grindSetting = "Ustawienie 25",
            tastingNotes = "Szybki koncentrat rozcieńczony gorącą wodą. Bardzo niskie pH, delikatna dla żołądka."
        ),

        // 5 GRUDNIA 2026 (Sobota - 2 wpisy)
        BrewLog(
            id = "log_31",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 12, 5),
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
            date = LocalDate.of(2026, 12, 5),
            time = "15:20",
            coffeeBag = bagColombiaCoffeeLab,
            recipeId = "recipe_van_bunnik_wac_aeropress",
            recipeTitle = "Wendelien van Bunnik – WAC Champion (Inverted)",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "26 klików",
            tastingNotes = "Intensywny, gęsty napar na popołudnie."
        ),

        // 8 GRUDNIA 2026 (Wtorek - 1 wpis)
        BrewLog(
            id = "log_33",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 12, 8),
            time = "08:00",
            coffeeBag = bagEthiopiaLacava,
            recipeId = "recipe_scott_rao_v60",
            recipeTitle = "Scott Rao – Classic V60 (Rao Spin)",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "23 kliki",
            tastingNotes = "Przy używaniu szybszych filtrów Cafec Abaca musiałem zmielić o klik drobniej (23 zamiast 24)."
        ),

        // 10 GRUDNIA 2026 (Czwartek - 1 wpis)
        BrewLog(
            id = "log_34",
            brewMethod = BrewMethod.AEROPRESS,
            date = LocalDate.of(2026, 12, 10),
            time = "08:30",
            coffeeBag = bagKenyaGrange,
            recipeId = "recipe_wendelboe_aeropress",
            recipeTitle = "Tim Wendelboe – Klasyczny AeroPress",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "18 klików",
            tastingNotes = "Świetna równowaga między wysoką kwasowością Kenii a pełnym body z AeroPressu."
        ),

        // 13 GRUDNIA 2026 (Niedziela - 1 wpis)
        BrewLog(
            id = "log_35",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 12, 13),
            time = "10:15",
            coffeeBag = bagColombiaCoffeeLab,
            recipeId = "recipe_katsuya_old_46",
            recipeTitle = "Tetsu Katsuya – Stary V60 (Metoda 4:6)",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "28 klików",
            tastingNotes = "Niedzielny klasyk 4:6. Bardzo czysta i słodka filiżanka."
        ),

        // 16 GRUDNIA 2026 (Środa - 1 wpis)
        BrewLog(
            id = "log_36",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 12, 16),
            time = "07:50",
            coffeeBag = bagEthiopiaLacava,
            recipeId = "recipe_hoffmann_v60_1cup",
            recipeTitle = "James Hoffmann – 1-Cup V60",
            grinder = Grinder.NICHE_ZERO,
            grindSetting = "Ustawienie 40",
            tastingNotes = "Porównanie Etiopii na żarnach stożkowych Niche Zero – mniej kwiatów, więcej słodkiej brzoskwini w syropie."
        ),

        // 19 GRUDNIA 2026 (Sobota - 2 wpisy)
        BrewLog(
            id = "log_37",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 12, 19),
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
            date = LocalDate.of(2026, 12, 19),
            time = "14:45",
            coffeeBag = bagBrazilPalomaKonesso,
            recipeId = "recipe_hoffmann_aeropress",
            recipeTitle = "James Hoffmann – Ultimate AeroPress",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "19 klików",
            tastingNotes = "Klasyczna czekoladowa Brazylia po południu."
        ),

        // 21 GRUDNIA 2026 (Poniedziałek - 1 wpis)
        BrewLog(
            id = "log_39",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 12, 21),
            time = "08:15",
            coffeeBag = bagKenyaGrange,
            recipeId = "recipe_katsuya_new_v60",
            recipeTitle = "Tetsu Katsuya – Nowy V60 (Uproszczony)",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "24 kliki",
            tastingNotes = "Trzy zalania w 92 stopniach. Bardzo soczysty finisz czerwonej porzeczki."
        ),

        // 24 GRUDNIA 2026 (Czwartek - Wigilia - 1 wpis)
        BrewLog(
            id = "log_40",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 12, 24),
            time = "09:30",
            coffeeBag = bagPeruSmok,
            recipeId = "recipe_hoffmann_v60_2cup",
            recipeTitle = "James Hoffmann – Ultimate 2-Cup V60",
            grinder = Grinder.COMANDANTE_C40,
            grindSetting = "25 klików",
            tastingNotes = "Świąteczny przelew z Peru Geisha na 500 ml. Idealny start Wigilii."
        ),

        // 26 GRUDNIA 2026 (Sobota - Drugi dzień świąt - 2 wpisy)
        BrewLog(
            id = "log_41",
            brewMethod = BrewMethod.V60,
            date = LocalDate.of(2026, 12, 26),
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
            date = LocalDate.of(2026, 12, 26),
            time = "16:00",
            coffeeBag = bagBrazilPalomaKonesso,
            recipeId = "recipe_hedrick_aeropress",
            recipeTitle = "Lance Hedrick – Daily AeroPress",
            grinder = Grinder.NICHE_ZERO,
            grindSetting = "Ustawienie 38",
            tastingNotes = "Brazylia do świątecznego sernika – pasuje bezbłędnie."
        ),

        // 29 GRUDNIA 2026 (Wtorek - 1 wpis)
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

        // 31 GRUDNIA 2026 (Czwartek - Sylwester - 2 wpisy)
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
