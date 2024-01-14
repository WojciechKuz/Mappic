package com.student.mappic.maplist

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.student.mappic.MapOptionsPopup.Companion.openPopupMenu

import com.student.mappic.databinding.FragmentMapItemBinding

/**
 * ADAPTER.
 * It has to have these methods implemented: onCreateViewHolder, onBindViewHolder, getItemCount.
 *
 * [RecyclerView.Adapter] that can display a [RecycleMap].
 * nTODO: Replace the implementation with code for your data type.
 */

class MyMapItemRecyclerViewAdapter(
    private val values: List<RecycleMap>
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

        // there's alternative for this method

        val holder = ViewHolder(
            FragmentMapItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        holder.nameView.setOnClickListener { openMap() }
        holder.thumbnailView.setOnClickListener { openMap() }
        holder.moreView.setOnClickListener {
            openPopupMenu(it)
        }
        return holder
    }

    /**
     * Associates ViewHolder with data.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: RecycleMap = values[position]
        //holder.idView.text = item.id
        holder.nameView.text = item.name
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
            return super.toString() + "'" + nameView.text + "'"
        }
    }

    /*
    // HERE alternative for ViewHolder and onCreateViewHolder():


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var thumbnail: ImageView? = null
        val textView: TextView

        init {
            // Define click listener for the ViewHolder's View
            textView = view.findViewById(R.id.map_name)
            thumbnail = view.findViewById(R.id.map_thumbn)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.fragment_map_item, viewGroup, false)

        return ViewHolder(view)
    }
    */
}