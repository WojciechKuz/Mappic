package pl.umk.mat.mappic.maplist

import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.fragment.app.activityViewModels
import pl.umk.mat.mappic.MainActivity
import pl.umk.mat.mappic.MainViewModel
import pl.umk.mat.mappic.R
import pl.umk.mat.mappic.clist
import kotlinx.coroutines.Dispatchers
import java.util.stream.Collectors

/**
 * A fragment representing a list of Items.
 */
class MapItemListFragment : Fragment() {

    private var columnCount = 1
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map_item_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)

                // when creating placeholder list here, it created IndexOutOfBoundsException outside my code
                /*
                Log.d(clist.MapItemListFragment, ">>> adapter has not been set")
                // If mapList not loaded, list of placeholders will be shown
                val placeholder = ArrayList<RecycleMap>()
                for (i in 1..25) {
                    placeholder.add(RecycleMap(i.toLong(), "Map no.${i}"))
                }
                adapter = MyMapItemRecyclerViewAdapter(placeholder) { con, id -> /* Nothing. No real map - nothing to delete. */}
                */

                // show list of maps
                viewModel.getMapList(activity as MainActivity) {
                    Log.d(clist.MapItemListFragment, ">>> setting list of maps")
                    (activity as MainActivity).runOnUiThread {
                        adapter = MyMapItemRecyclerViewAdapter(
                            ArrayList(it.stream().map{dbMap -> RecycleMap.dbMaptoRecycleMap(dbMap)}.collect(Collectors.toList()))
                        ) { con, id -> viewModel.deleteMap(con, id) }
                    }
                }
                if (adapter != null) {
                    Log.d(clist.MapItemListFragment, ">>> adapter has been set")
                }
                else {
                    Log.d(clist.MapItemListFragment, ">>> adapter has not been set yet")
                }
                //Dispatchers.Main
            }
        }
        else Log.e(clist.MapItemListFragment, ">>> This is not RecyclerView!")
        /* // Alternative way
        val recyclerView: RecyclerView = findViewById(R.id.list)
        recyclerView.adapter = recyclerView
        */
        return view
    }

    companion object {

        // Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            MapItemListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}