# Qidasie App

This is a native Android app (Kotlin + Jetpack Compose) for an Ethiopian Orthodox liturgy app. It plays short pre-recorded audio clips when the user taps a word/symbol. 
The app is fully offline — all audio is bundled locally, with no backend or network calls.

## Folder Structure

The project has been organized with the following key packages in `app/src/main/java/com/enoch/kidase/`:

*   **`ui/`**: Compose screens (one file per screen later).
*   **`ui/theme/`**: Compose theming (`Color.kt`, `Type.kt`, `Theme.kt`).
*   **`data/`**: Data classes and manifest loading logic (e.g., `ClipManifestLoader`).
*   **`audio/`**: Audio playback logic (e.g., `AudioPlayerViewModel`).

Additionally, these resource folders are available:

*   **`app/src/main/assets/audio/`**: Bundled audio clips.
*   **`app/src/main/res/font/`**: Ge'ez/Amharic font files.

## Development Guidelines
*   Work in ONE small, scoped feature per task.
*   Prefer editing/extending existing files over creating new ones.
*   Keep the app in a compilable, runnable state after each task.
*   The architecture is designed to be flat and simple.
