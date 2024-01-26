package pl.umk.mat.mappic.maplist

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import pl.umk.mat.mappic.db.DeleteInterface
import pl.umk.mat.mappic.MapOptionsPopup
import pl.umk.mat.mappic.clist

import pl.umk.mat.mappic.databinding.FragmentMapItemBinding
import pl.umk.mat.mappic.viewmap.ViewMapActivity

/**
 * ADAPTER.
 * It has to have these methods implemented: onCreateViewHolder, onBindViewHolder, getItemCount.
 *
 * Warning! I added additional constructor parameter:
 * @param deleteMapFun elements in recycleView list have little menu with two actions - delete and edit.
 * Delete action needs to be provided via interface (this parameter). This interface has 2 parameters -
 * context and mapid.
 *
 * [RecyclerView.Adapter] that can display a [RecycleMap].
 * nTODO: Replace the implementation with code for your data type.
 */

class MyMapItemRecyclerViewAdapter(
    private val values: List<RecycleMap>, //navigateToMapView: Signal
    private val deleteMapFun: DeleteInterface
) : RecyclerView.Adapter<MyMapItemRecyclerViewAdapter.ViewHolder>() {

    private lateinit var parentContext: Context
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
        parentContext = parent.context
        holder.nameView.setOnClickListener { holder.openMap(it) }
        holder.thumbnailView.setOnClickListener { holder.openMap(it) }
        //  moreView.onclickListener is set in onBindViewHolder()
        return holder
    }

    /**
     * Associates ViewHolder with data.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var rePosition = position
        if(position >= values.size) {
            Log.wtf(clist.MyMapItemRecyclerViewAdapter,"In onBindViewHolder()"
                    + " \'position\' parameter is out of bounds for values. (values: List<RecycleMap>)")
            rePosition = values.size - 1
        }
        val item: RecycleMap = values[rePosition]
        holder.nameView.text = item.name
        holder.mapid = item.mapid

        // FUTURE_TODO bind thumbnail
        // FUTURE_TODO options

        // set onClickListener
        val popup = MapOptionsPopup(parentContext, holder.mapid!!)
        popup.setMapName(holder.nameView.text.toString())   // set name
        popup.setDeleteFun({ con, id ->
            deleteMapFun.delete(con, id)
            holder.itemView.visibility = View.GONE
        })                                              // set delete fun
        holder.moreView.setOnClickListener {
            popup.openPopupMenu(it) // finally, setting openPopup as onClickListener
        }
    }

    override fun getItemCount(): Int = values.size

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
        var mapid: Long? = null

        /**
         * Display map, open ViewMapActivity
         */
        fun openMap(view: View) {
            if(mapid == null) {
                return
            }
            val gotoMapView = Intent(view.context, ViewMapActivity::class.java)
            gotoMapView.putExtra("whichmap", mapid!!)
            view.context.startActivity(gotoMapView)
        }

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