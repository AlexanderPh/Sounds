package com.testing.simpleaudioplayer.list

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.testing.core.setDivider
import com.testing.simpleaudioplayer.R
import com.testing.simpleaudioplayer.list.recycler.ItemAnimator
import com.testing.simpleaudioplayer.list.recycler.MelodyListAdapter
import com.testing.simpleaudioplayer.list.viewmodel.SongListViewModel
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
        listAdapter.controlCallback = viewModel
        list.adapter = listAdapter
        list.itemAnimator = ItemAnimator()
        list.setDivider(R.drawable.melody_list_item_divider)

        viewModel.melodyList.observe(viewLifecycleOwner, Observer{
            listAdapter.items = it
        })

        viewModel.currentPlay.observe(viewLifecycleOwner, Observer{
            it?.let {
                currentPlay.bind(it)
            }
        })
        viewModel.loadList(R.raw.test_list)
    }

    override fun onResume() {
        super.onResume()
        viewModel.play()
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()

    }
}