# German Learning App

German Learning App is a small Android flashcard app I’m building to practice real-world Android development while also making German vocabulary learning more structured. The app is centered around spaced repetition: instead of studying everything in a deck, it schedules reviews so you see cards again right before you’re likely to forget them.

## Features (current)

### Spaced repetition study flow
- Uses an SM-2 style scheduling approach.
- Rating a card updates its review state (interval/ease/next due time).

### Deck-based studying
- Study one deck at a time.
- Deck list shows counts for:
    - **Due** cards (review cards)
    - **New** cards (available within the daily limit)

### Daily new-card limit
- New cards are limited per deck (default: **10/day**).
- The limit can be adjusted in settings (slider).
- This prevents sessions from turning into “study the entire deck”.

### Local persistence (offline-first)
- Progress is stored locally using **Room**.
- No account or server required.

### Settings and basic app structure
- Settings screen is in place and used for study-related configuration.
- The app is structured into data/domain/ui layers to keep logic and UI separate as the project grows.

## What’s still in progress
- Better statistics (e.g., trends over time, more meaningful progress summaries).
- Card/deck editing inside the app.
- Import/export (so decks can be shared or backed up).
- More test coverage and general polishing, especially around edge cases.

## Tech stack
- **Kotlin**
- **Jetpack Compose** (UI)
- **Navigation Compose**
- **Room** (SQLite)
- **DataStore (Preferences)** for settings
- **KSP** (Room code generation)

## Project structure (high level)
The project follows a “clean-ish” structure inspired by MVVM / clean architecture ideas:

- `ui/` — Compose screens and ViewModels
- `domain/` — models and use cases (e.g., “get next card”, “rate card”)
- `data/` — Room entities/DAOs and repositories

It’s not meant to be over-engineered, but it keeps things readable while features are still changing.

## Getting started
1. Clone the repo
2. Open it in Android Studio
3. Let Gradle sync
4. Run on an emulator or device

## Technical report
There’s a short technical report included that explains the architecture and the SRS logic:

[**Download the Technical Report (PDF)**](./app/App.pdf?download=true)
