package com.valeria.beatbox

private const val WAV = ".wav"

class Sound(val assetPath: String, var soundId: Int? = null) { //для генерации удобочитаемого имени звука
    val name = assetPath.split("/").last().removeSuffix(WAV)
}