import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

//Abstraction so TTS and AI voices can be switched
interface VoiceProvider {
    fun speak(text: String)
    fun stop()
    fun release()
}

class TtsVoiceProvider(
    context: Context,
    private val language: Locale = Locale.GERMAN
) : VoiceProvider {

    private var tts: TextToSpeech? = null
    private var isReady: Boolean = false

    init {
        // Use applicationContext to avoid leaking an Activity
        val appContext = context.applicationContext

        tts = TextToSpeech(appContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts?.setLanguage(language)

                // Check if language is supported
                if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED
                ) {
                    isReady = false
                } else {
                    isReady = true
                }
            } else {
                isReady = false
            }
        }
    }

    override fun speak(text: String) {
        // If TTS is not ready yet, just ignore the call
        if (!isReady) return

        // API 21+ speak signature
        tts?.speak(
            text,
            TextToSpeech.QUEUE_FLUSH,
            null,
            "german_learning_app_utterance"
        )
    }

    override fun stop() {
        tts?.stop()
    }

    override fun release() {
        tts?.stop()
        tts?.shutdown()
        isReady = false
    }
}
