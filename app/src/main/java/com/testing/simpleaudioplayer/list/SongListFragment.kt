package com.testing.simpleaudioplayer.list

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.testing.simpleaudioplayer.R
import com.testing.simpleaudioplayer.list.recycler.MelodyListAdapter
import com.testing.simpleaudioplayer.list.viewmodel.SongListViewModel
import com.testing.simpleaudioplayer.list.viewmodel.SongListViewModel.Action.LoadList
import kotlinx.android.synthetic.main.fragment_song_list.*

class SongListFragment : Fragment(R.layout.fragment_song_list) {

    private val viewModel: SongListViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(SongListViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        val listAdapter = MelodyListAdapter()
        list.adapter = listAdapter

        viewModel.melodyList.observe(viewLifecycleOwner, {
            listAdapter.items = it
        })
        viewModel.onAction(LoadList(R.raw.test_list))


    }
}