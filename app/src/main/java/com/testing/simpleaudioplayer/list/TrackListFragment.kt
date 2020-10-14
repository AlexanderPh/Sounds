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
import com.testing.simpleaudioplayer.list.recycler.TrackListAdapter
import com.testing.simpleaudioplayer.list.viewmodel.TrackListViewModel
import kotlinx.android.synthetic.main.fragment_song_list.*

class TrackListFragment : Fragment(R.layout.fragment_song_list) {

    private val viewModel: TrackListViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(TrackListViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(toolbar)


        val listAdapter = TrackListAdapter()
        listAdapter.controlCallback = viewModel
        list.adapter = listAdapter
        list.itemAnimator = ItemAnimator()
        list.setDivider(R.drawable.track_list_item_divider)

        viewModel.tracks.observe(viewLifecycleOwner, Observer{
            listAdapter.updateItems(it)
        })

        viewModel.currentTrack.observe(viewLifecycleOwner, Observer{
            currentPlay.bind(it)

        })
        currentPlay.controlCallback = viewModel
        viewModel.loadList(R.raw.test_list)
    }

    override fun onResume() {
        super.onResume()
        viewModel.onLifeCycleResume()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onLifeCyclePause()
    }

    override fun onDestroy() {
        viewModel.onDestroy()
        super.onDestroy()
    }
}