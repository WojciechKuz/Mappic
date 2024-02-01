package pl.umk.mat.mappic

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import pl.umk.mat.mappic.db.DeleteInterface
import pl.umk.mat.mappic.addmap.AddMapActivity

/**
 * This class helps open MapOptions popup menu.
 * Popup menu contains two actions - edit map and delete map.
 * Popup menu is shown using openPopupMenu(), but you need to set up a couple of things beforehand.
 * When edit is pressed, map editing activity (addMap) is opened.
 * When delete is pressed, dialog asking to confirm deleting is shown.
 *
 * Setting up:
 * fill constructor parameters,
 * provide deleting function in setDeleteFun(),
 * provide map name in setMapName()
 *
 * When everything is set up, you can finally call openPopupMenu().
 */
class MapOptionsPopup(val context: Context, val mapid: Long) {

    lateinit var deleteFun: DeleteInterface
    lateinit var map_name: String

    /** sets which function is responsible for deleting map,
     * additionaly provide map name, that will be shown in dialog.
     * @param delFun delete map function
     */
    fun setDeleteFun(delFun: DeleteInterface, map_name: String = "") {
        deleteFun = delFun
        this.map_name = map_name
    }
    fun setMapName(map_name: String) {
        this.map_name = map_name
    }

    /**
     * Shows popup menu with options 'edit map' and 'delete map'
     */
    fun openPopupMenu(elem: View) {
        Log.d(clist.MapOptionsPopup, ">>> openPopupMenu")
        // open popup menu
        val popmenu = PopupMenu(context, elem)
        popmenu.inflate(R.menu.map_options)
        popmenu.setOnMenuItemClickListener {
            fun mapOptions(mi: MenuItem): Boolean {
                when (mi.itemId) {
                    R.id.edit_dropdown -> onClickEdit()
                    R.id.delete_dropdown -> onClickDelete()
                    else -> {
                        Log.e(clist.MapOptionsPopup, ">>> nothing to show \"${mi.itemId}\"")
                    }
                }
                return true
            }
            mapOptions(it)
        }
        popmenu.show()
    }

    /** open map editing (variant of adding map) activity */
    private fun onClickEdit() {
        Log.d(clist.MapOptionsPopup, ">>> onClickEdit")
        val gotoAddMap = Intent(context, AddMapActivity::class.java)
        gotoAddMap.putExtra("whichmap", mapid) // map with id 'elemid' will be edited
        context.startActivity(gotoAddMap)
    }

    /** Dialog to confirm map deletion will be shown */
    private fun onClickDelete() {
        Log.d(clist.MapOptionsPopup, ">>> onClickDelete")
        showDeleteDialog()
    }

    private fun deleteMap() {
        if(this::deleteFun.isInitialized) {
            deleteFun.delete(context, mapid) // deletes map from DB
            val msg = R.string.map_deleted
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            Log.i(clist.MapOptionsPopup, ">>> Map deleted.")
        }
        else {
            Log.e(clist.MapOptionsPopup, ">>> Deleting function has not been set")
        }
    }

    /** shows dialog asking to confirm map deletion */
    private fun showDeleteDialog() {
        val messageMapName = if (map_name.isNotEmpty()) "\"$map_name\"" else ""
        val builder = AlertDialog.Builder(context)
        builder.setMessage(context.getString(R.string.dialog_message_delete, messageMapName))
        builder.setPositiveButton(R.string.dialog_yes) { dialog, id ->
            deleteMap()
        }
        builder.setNegativeButton(R.string.dialog_no) { dialog, id ->
            /* Do nothing. */
        }
        val delDialog = builder.create()
        delDialog.show()
    }
}