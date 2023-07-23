package com.student.mappic.maplist

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import com.student.mappic.R

import com.student.mappic.maplist.placeholder.PlaceholderContent.PlaceholderItem
import com.student.mappic.databinding.FragmentMapItemBinding

/**
 * ADAPTER.
 * It has to have these methods implemented: onCreateViewHolder, onBindViewHolder, getItemCount.
 *
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * nTODO: Replace the implementation with code for your data type.
 */

    // TODO rename and replace PlaceholderItem
class MyMapItemRecyclerViewAdapter(
    private val values: List<PlaceholderItem>
) : RecyclerView.Adapter<MyMapItemRecyclerViewAdapter.ViewHolder>() {

    /**
     * Used when new ViewHolder is created (element of list).
     *
     * Sets up ViewHolder and it's child elements:
     * - moreView:
     * Creates little menu (PopupMenu) for item from a list, when more button (3 dots) is clicked.
     * Adds onClickListener to options in this menu. Note .show() on the end of method.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var holder = ViewHolder(
            FragmentMapItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        holder.nameView.setOnClickListener {}
        holder.thumbnailView.setOnClickListener {}
        holder.moreView.setOnClickListener {
            // open popup menu
            var popmenu = PopupMenu(it.context, it)
            popmenu.inflate(R.menu.map_options)
            popmenu.setOnMenuItemClickListener(
                fun(mi: MenuItem): Boolean {
                    when(mi.title) {
                        "@string/edit" -> holder.onClickEdit()
                        "@string/delete" -> holder.onClickDelete()
                        else -> {}
                    }
                    return true
                }
            )
            popmenu.show()
        }
        return holder

    }

    /**
     * Associates ViewHolder with data.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        //holder.idView.text = item.id
        holder.nameView.text = item.content
        // TODO bind thumbnail
        // TODO options
    }

    override fun getItemCount(): Int = values.size

    /**
     * Display map
     */
    private fun openMap() {
        // TODO open map to view and navigate
    }
    /**
     * VIEW HOLDER
     * Class of single element in RecyclerView list
     *
     * Defined in fragment_map_item.xml
     */
    inner class ViewHolder(binding: FragmentMapItemBinding) : RecyclerView.ViewHolder(binding.root) {
        //val idView: TextView = binding.itemNumber // no id
        val nameView: TextView = binding.mapName
        val thumbnailView: ImageView = binding.mapThumbn
        val moreView: ImageButton = binding.more

        override fun toString(): String {
            return super.toString() + " '" + nameView.text + "'"
        }
        fun onClickEdit() {
            // TODO open map editing (variant of adding map) activity
        }
        fun onClickDelete() {
            // TODO delete map, maybe some 'Are u sure?' popup?
        }
    }
}