package com.valeria.beatbox

import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.SoundPool
import android.util.Log
import java.io.IOException
import java.lang.Exception

private const val TAG = "BeatBox"
private const val SOUNDS_FOLDER = "sample_sounds"
private const val MAX_SOUNDS = 5

class BeatBox(private val assets: AssetManager) {

    val sounds: List<Sound>
    private val soundPool = SoundPool.Builder()
            .setMaxStreams(MAX_SOUNDS)
            .build()
    init {
        sounds = loadSounds()
    }

    private fun loadSounds(): List<Sound> { //список объектов Sound
        val soundNames: Array<String>
        try {
            soundNames = assets.list(SOUNDS_FOLDER)!! //возвр список имен файлов
        } catch (e: Exception){
            Log.d(TAG,"Could not list assets",e)
            return emptyList()
        }
        val sounds = mutableListOf<Sound>()
        soundNames.forEach { filename ->
            val assetPath = "$SOUNDS_FOLDER/$filename"
            val sound = Sound(assetPath)
            try {
                load(sound)
                sounds.add(sound)
            } catch (ioe: IOException){
                Log.e(TAG,"Could not load sound $filename", ioe)
            }
        }
        return sounds
    }

// Загрузка звуков в SoundPool
    private fun load(sound: Sound){
        val afd: AssetFileDescriptor = assets.openFd(sound.assetPath)
        val soundId = soundPool.load(afd,1) // загружает файл в SoundPool для последующего воспроизведения, возвращает идентификатор(int)
        sound.soundId = soundId
    }

    fun play(sound: Sound){
        sound.soundId?.let {
            soundPool.play(it,1.0f,1.0f,1,0,1.0f)
        }
    }

    //Освобождение SoundPool
    fun release() {
        soundPool.release()
    }
}