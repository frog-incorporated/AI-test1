# Hexo Voice Assistant

Hexo is a hands‑free, always‑listening voice assistant for Android. It uses
on‑device wake‑word detection, speech‑to‑text (STT), local tool execution
and large language models running via Ollama to answer your queries. Once
launched, Hexo listens continuously for the wake word **“Hexo”** and then
handles your command without requiring any additional taps.

## Features

- **Wake word:** always listening for “Hexo” using a pluggable engine
  (OpenWakeWord or Vosk).  The default implementation is a stub which you
  can replace with a real TFLite model.
- **Speech‑to‑text (STT):** choose between Android’s built‑in
  `SpeechRecognizer` or offline Whisper.cpp.  The sample includes
  the Android implementation.
- **Text‑to‑speech (TTS):** speak responses using Android TTS or Piper
  offline synthesis.  The provided implementation uses Android TTS.
- **Tool chain:** built‑in tools for weather, time, timers, web search,
  news headlines, article reading, vision, and location.  Tools can be
  chained together by the router (e.g. location → weather → answer).
- **Router & brains:** a small router model (default `qwen3:0.6b`) chooses
  which tool or brain to invoke.  Brain‑1 (`qwen3:1.7b`) handles
  conversational queries and Brain‑2 (`qwen3:4b`) handles harder tasks.
  All models are served via [Ollama](https://ollama.com/).  You can
  configure your own models and endpoint via settings.
- **Search without API keys:** DuckDuckGo HTML scraping via JSoup and
  RSS feeds for news.  Articles are extracted directly from their web
  pages.
- **Settings:** persist your preferred models, STT/TTS providers, wake
  word sensitivity and more using DataStore.
- **CI pipeline:** a GitHub Actions workflow builds a signed debug APK and
  uploads it as an artifact on every push to `main`.

## Building

To build the project locally you will need Android Studio **Hedgehog** or
newer with the Android SDK platform 34. Clone this repository and open it
in Android Studio.  The project uses Kotlin, Jetpack Compose and a
multi‑module architecture:

- **app:** the entry point with a minimal Compose UI and a foreground
  service hosting the assistant.
- **core:** networking, DataStore, timers, location and search helpers.
- **ml:** interfaces and implementations for wake‑word, STT and TTS.
- **tools:** weather, time, timer, search, news, article, vision and
  location tools.
- **agent:** orchestrator tying the router, tools, brains and speech.

Run the **`assembleDebug`** Gradle task or use the run configuration to
install the debug APK on your device.  Ensure you grant microphone and
location permissions when prompted.

## Usage

1. Launch the Hexo app.  It will request microphone and location
   permissions and start a foreground service.
2. Say **“Hexo”**.  The assistant will beep (if implemented) and start
   listening to your command.
3. Speak your query (e.g. “What’s the weather in Jacksonville?”).  Hexo
   will route the request, execute any necessary tools, query the LLM
   and speak the response.
4. Speak “Hexo” again at any time to interrupt the current response and
   issue a new command.

## Configuring models & tools

Model and engine settings are stored via DataStore and can be exposed
through a settings UI or edited via code.  The default Ollama host is
`http://127.0.0.1:11434`; update `SettingsRepository` or your runtime
configuration if you run Ollama elsewhere.

To replace the stub wake‑word engine with OpenWakeWord or Vosk, implement
`WakeWordEngine` in the **ml** module and inject it into `HexoService`.
Similarly, add offline STT or TTS engines by implementing `SttEngine`
and `TtsEngine`.

## CI/CD

The `.github/workflows/android.yml` workflow checks out the code,
installs the Android SDK, builds the debug APK and uploads it as
`hexo-debug-apk` on each push to `main`.  You can extend this workflow
to publish releases or run tests.

## License

This project is provided as a reference implementation of a hands‑free
voice assistant. It includes stub implementations for many components.
Replace the stubs with production‑ready models and services before
deploying the app to end users.