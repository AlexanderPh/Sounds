@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.testing.core
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.SeekBar
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.widget.NestedScrollView
import androidx.drawerlayout.widget.DrawerLayout

inline fun android.view.View.onClick(noinline l: (v: android.view.View?) -> Unit) {
    setOnClickListener(l)
}

inline fun SwitchCompat.onCheckedChange(noinline l : (c : CompoundButton, isChecked : Boolean) -> Unit){
    setOnCheckedChangeListener(l)
}

inline fun NestedScrollView.onScrollChange(noinline l: (v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) -> Unit) {
    setOnScrollChangeListener(l)
}

fun android.widget.TextView.textChangedListener(init: __TextWatcher.() -> Unit) {
    val listener = __TextWatcher()
    listener.init()
    addTextChangedListener(listener)
}


class __TextWatcher : android.text.TextWatcher {

    private var _beforeTextChanged: ((CharSequence?, Int, Int, Int) -> Unit)? = null

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        _beforeTextChanged?.invoke(s, start, count, after)
    }

    fun beforeTextChanged(listener: (CharSequence?, Int, Int, Int) -> Unit) {
        _beforeTextChanged = listener
    }

    private var _onTextChanged: ((CharSequence?, Int, Int, Int) -> Unit)? = null

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        _onTextChanged?.invoke(s, start, before, count)
    }

    fun onTextChanged(listener: (CharSequence?, Int, Int, Int) -> Unit) {
        _onTextChanged = listener
    }

    private var _afterTextChanged: ((android.text.Editable?) -> Unit)? = null

    override fun afterTextChanged(s: android.text.Editable?) {
        _afterTextChanged?.invoke(s)
    }

    fun afterTextChanged(listener: (android.text.Editable?) -> Unit) {
        _afterTextChanged = listener
    }
}



fun DrawerLayout.drawerListener(init : __DrawerListener.() -> Unit) {
    val listener = __DrawerListener()
    listener.init()
    addDrawerListener(listener)
}

class __DrawerListener : DrawerLayout.DrawerListener{


    private var _onDrawerStateChanged: ((Int) -> Unit)? = null

    override fun onDrawerStateChanged(newState: Int) {
        _onDrawerStateChanged?.invoke(newState)
    }

    fun onDrawerStateChanged(listener : (newState: Int) -> Unit) {
        _onDrawerStateChanged = listener
    }

    private var _onDrawerSlide : ((View, Float) -> Unit)? = null

    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
        _onDrawerSlide?.invoke(drawerView, slideOffset)
    }

    fun onDrawerSlide(listener : (drawerView: View, slideOffset: Float) -> Unit) {
        _onDrawerSlide = listener
    }

    private var _onDrawerClosed : ((View) -> Unit)? = null

    override fun onDrawerClosed(drawerView: View) {
        _onDrawerClosed?.invoke(drawerView)
    }

    fun onDrawerClosed(listener : (drawerView : View ) -> Unit){
        _onDrawerClosed = listener
    }

    private var _onDrawerOpened : ((View) -> Unit)? = null

    override fun onDrawerOpened(drawerView: View) {
        _onDrawerOpened?.invoke(drawerView)
    }

    fun onDrawerOpened(listener : (drawerView : View ) -> Unit){
        _onDrawerOpened = listener
    }

}


fun SearchView.queryTextListener(init : __OnQueryTextListener.() -> Unit) {
    val listener = __OnQueryTextListener()
    listener.init()
    setOnQueryTextListener(listener)
}


class __OnQueryTextListener : SearchView.OnQueryTextListener {

    private var onQueryTextSubmit : ((String) -> Boolean)? = null
    private var onQueryTextChanged : ((String) -> Boolean)? = null


    override fun onQueryTextSubmit(query: String?): Boolean {
        return onQueryTextSubmit?.invoke(query ?: "") ?: false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return onQueryTextChanged?.invoke(newText ?: "") ?: false
    }

    fun onQueryTextSubmit(listener: (String) -> Boolean){
        onQueryTextSubmit = listener
    }

    fun onQueryTextChanged(listener: (String) -> Boolean){
        onQueryTextChanged = listener
    }
}



fun SeekBar.onSeekBarChangeListener(init : __OnSeekBarChangeListener.() -> Unit){
    val listener = __OnSeekBarChangeListener()
    listener.init()
    setOnSeekBarChangeListener(listener)
}


class __OnSeekBarChangeListener : SeekBar.OnSeekBarChangeListener {

    private var _onProgressChanged : ((SeekBar, Int, Boolean) -> Unit)? = null
    private var _onStartTrackingTouch : ((SeekBar) -> Unit)? = null
    private var _onStopTrackingTouch : ((SeekBar) -> Unit)? = null


    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        _onProgressChanged?.invoke(seekBar, progress, fromUser)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
        _onStartTrackingTouch?.invoke(seekBar)
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        _onStopTrackingTouch?.invoke(seekBar)
    }


    fun onProgressChanged(listener : ((SeekBar, Int, Boolean) -> Unit)?){
        _onProgressChanged = listener
    }
    fun onStartTrackingTouch(listener: ((SeekBar) -> Unit)?){
        _onStartTrackingTouch = listener
    }
    fun onStopTrackingTouch(listener: ((SeekBar) -> Unit)?){
        _onStopTrackingTouch = listener
    }
}

fun ViewGroup.onHierarchyChangeListener(init : __OnHierarchyChangeListener.() -> Unit) {
    val listener = __OnHierarchyChangeListener()
    listener.init()
    setOnHierarchyChangeListener(listener)
}


class __OnHierarchyChangeListener : ViewGroup.OnHierarchyChangeListener{

    private var _onChildViewRemoved : ((View, View) -> Unit)? = null
    private var _onChildViewAdded : ((View, View) -> Unit)? = null


    override fun onChildViewRemoved(parent: View, child: View) {
        _onChildViewRemoved?.invoke(parent, child)
    }

    override fun onChildViewAdded(parent: View, child: View) {
        _onChildViewAdded?.invoke(parent, child)
    }

    fun onChildViewAdded(listener : ((View, View) -> Unit)?) {
        _onChildViewAdded = listener
    }

    fun onChildViewRemoved(listener : ((View, View) -> Unit)?) {
        _onChildViewRemoved = listener
    }
}