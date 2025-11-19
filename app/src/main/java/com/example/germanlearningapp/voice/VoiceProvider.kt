package com.example.germanlearningapp.voice

interface VoiceProvider {
    fun speak(text: String)
    fun stop()
    fun release()
}