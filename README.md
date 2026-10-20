# ☕ Kawapp

> **Kawapp** to aplikacja mobilna dedykowana pasjonatom kawy – od entuzjastów metod alternatywnych po miłośników klasycznego espresso. Łączy w sobie precyzyjny dziennik parzenia z elementami platformy społecznościowej na wzór platformy Strava.

---

## 📖 O projekcie

Kawa to dla wielu nie tylko codzienny rytuał, ale prawdziwa pasja i sztuka. Z uwagi na mnogość metod zaparzania oraz różnorodności profili smakowych ziaren, uzyskanie idealnego naparu wymaga precyzji oraz zapisywania parametrów każdego przepisu.

**Kawapp** powstał po to, aby:
- Zapewnić wygodne miejsce do archiwizowania przepisów na idealną kawę.
- Śledzić własne statystyki związane ze zużyciem kawy.
- Stworzyć przestrzeń społecznościową, w której bariści i hobbyści mogą dzielić się swoimi doświadczeniami, ocenami kaw i wzajemnie inspirować.

---

## ✨ Główne funkcjonalności

### 👤 1. Użytkownicy i tożsamość
- **Logowanie i rejestracja:** Prosta autoryzacja za pomocą adresu e-mail oraz integracja z kontem **Google Sign-In**.
- **Profil kawosza:** Możliwość personalizacji profilu (zdjęcie, bio, lista ulubionych ziaren oraz preferowanych metod zaparzania).
- **Zarządzanie prywatnością:** Opcja ustawienia profilu jako **publiczny** lub **prywatny**.

### 🏃‍♂️ 2. Kawowa „Strava” (Moduł społecznościowy)
- **Logowanie sesji parzenia:** Rejestracja każdej przygotowanej kawy (data, wybór kawy, metoda, notatka, zdjęcie naparu/sprzętu).
- **Oś czasu (Feed):** Przeglądanie aktywności i świeżo parzonych kaw przez znajomych w czasie rzeczywistym.
- **Interakcje:** Zostawianie reakcji (odpowiednik kudo – **„Kawosy”**) oraz komentowanie wpisów innych użytkowników.

### 📚 3. Baza kaw, przepisów i sprzętu
- **Katalog kaw:** Dodawanie ziaren ze szczegółowymi informacjami:
   - Nazwa kawy i palarnia
   - Kraj pochodzenia i region
   - Odmiana botaniczna (*varietal*)
   - Metoda obróbki ziaren (*natural, washed, honey, anaerobik*)
   - Data palenia
   - Nuty i profil smakowy
- **Kreator przepisów:** Zapisywanie szczegółowych parametrów ekstrakcji:
   - Ilość kawy oraz wody
   - Proporcje ($g/l$)
   - Grubość / stopień zmielenia
   - Temperatura wody
   - Czas preinfuzji i całkowity czas ekstrakcji
- **Baza sprzętu:** Ewidencja własnych zaparzaczy (V60, AeroPress, Chemex, kawiarka, ekspres) oraz młynków.
- **Oceny i recenzje:** Możliwość oceniania ziaren i dodawania własnych notatek degustacyjnych.

### 📊 4. Kalendarz i statystyki
- **Kalendarz parzeń & Streak:** Wizualny kalendarz aktywności pozwalający utrzymać ciągłość parzenia („kawowy streak”).
- **Podsumowania i wykresy:**
   - Całkowita liczba zaparzonych kaw,
   - Sumaryczna waga zużytych ziaren (w gramach),
   - Najchętniej wybierane metody parzenia i najpopularniejsze ziarna.

---

## 🛠️ Technologie i narzędzia

| Komponent | Technologia / Narzędzie                                                                             |
| :--- |:----------------------------------------------------------------------------------------------------|
| **Język programowania** | [Kotlin](https://kotlinlang.org/)                                                                   |
| **Interfejs użytkownika (UI)** | [Jetpack Compose](https://developer.android.com/jetpack/compose) (nowoczesny, deklaratywny UI)      |
| **Backend / BaaS** | [Firebase](https://firebase.google.com/) (Auth, Cloud Firestore / Realtime Database, Cloud Storage) |
| **IDE** | Android Studio Rabbit 1 \| 2026.2.1                                                                 |
| **Architektura** | MVVM / Clean Architecture z wykorzystaniem StateFlow i Coroutines                                   |

---

## 🎯 Wymagania niefunkcjonalne

- **Wydajność i responsywność:** Czas startu oraz ładowania kluczowych widoków aplikacji poniżej **1.5 sekundy** przy typowym połączeniu sieciowym.
- **UI / UX & Dostępność:** Nowoczesny, czytelny design zgodny z Material Design 3, wysoki kontrast, intuicyjny przepływ informacji.
- **Bezpieczeństwo i ochrona danych (RODO):** Bezpieczna autoryzacja, szyfrowanie danych wrażliwych i haseł, respektowanie reguł prywatności użytkowników.
- **Skalowalność:** Wykorzystanie bezserwerowej infrastruktury Firebase umożliwiającej bezproblemową obsługę rosnącej bazy użytkowników i wpisów.

---

## 👥 Autorzy

Projekt zrealizowany w ramach przedmiotu **Projektowanie Interfejsów Mobilnych** przez zespół **Betoniarze**:

- **Michał Kapica** – `281194`
- **Piotr Kulej** – `281085`
- **Tomasz Chmura** – `281026`

---

