package com.testing.simpleaudioplayer.list

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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
        listAdapter.controlCallback = viewModel // клики айтемов пробрасываем сразу в модель
        list.adapter = listAdapter
        list.itemAnimator = ItemAnimator() // кастомный аниматор, чтобы отключить анимации айтемов при апдейте
        list.setDivider(R.drawable.track_list_item_divider)
        currentPlay.setUpBlurView(list)

        viewModel.tracksNew.observe(viewLifecycleOwner, {
            listAdapter.items = it
        })

        viewModel.currentTrackNew.observe(viewLifecycleOwner, {
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