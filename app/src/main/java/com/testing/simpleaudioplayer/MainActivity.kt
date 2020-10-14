package com.testing.simpleaudioplayer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.testing.simpleaudioplayer.list.TrackListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* Решил использовать концепцию SingleActivity
           Все ключевые фичи можно хостить во фрагментах и в зависимости от предполагаемого флоу
           заменять их через основное Activity

           Плюсом данного подхода так же является меньшая ресурсоёмкость, Activity очень тяжелая
           сущность в плане создания.
        */


        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(android.R.id.content, TrackListFragment())
        transaction.commitAllowingStateLoss()
    }
}