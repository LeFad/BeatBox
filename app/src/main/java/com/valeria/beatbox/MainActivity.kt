package com.valeria.beatbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.valeria.beatbox.databinding.ActivityMainBinding
import com.valeria.beatbox.databinding.ListItemSoundBinding

class MainActivity : AppCompatActivity() {

    private lateinit var beatBox: BeatBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        beatBox = BeatBox(assets)

        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = SoundAdapter(beatBox.sounds) //Подключение SoundAdapter и передача звуков адаптеру
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        beatBox.release()
    }

    //объект SoundHolder, связанный с list_item_sound.xml
    private inner class SoundHolder(private val binding: ListItemSoundBinding):
            RecyclerView.ViewHolder(binding.root){
                init {
                    binding.viewModel = SoundViewModel(beatBox)
                }
                fun bind(sound: Sound){
                    binding.apply {
                        viewModel?.sound = sound
                        executePendingBindings() //обновить немедленно
                    }
                }
            }

    //Adapter, связанный с SoundHolder
    private inner class SoundAdapter(private val sounds: List<Sound>): //связ SoundAdapter со списком объектов Sound
        RecyclerView.Adapter<SoundHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundHolder {
            val binding = DataBindingUtil.inflate<ListItemSoundBinding>(
                layoutInflater,
                R.layout.list_item_sound,
                parent,
                false
            )
            return SoundHolder(binding)
        }

        override fun onBindViewHolder(holder: SoundHolder, position: Int) {
            val sound = sounds[position]
            holder.bind(sound)
        }

        override fun getItemCount() = sounds.size
    }

}