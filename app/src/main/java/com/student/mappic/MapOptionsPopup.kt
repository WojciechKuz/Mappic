package com.student.mappic

import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu

class MapOptionsPopup {
    companion object {
        /**
         * Shows popup menu with options 'edit map' and 'delete map'
         */
        fun openPopupMenu(elem: View) {
            // open popup menu
            var popmenu = PopupMenu(elem.context, elem)
            popmenu.inflate(R.menu.map_options)
            popmenu.setOnMenuItemClickListener(
                fun(mi: MenuItem): Boolean {
                    when(mi.title) {
                        "@string/edit" -> onClickEdit()
                        "@string/delete" -> onClickDelete()
                        else -> {}
                    }
                    return true
                }
            )
            popmenu.show()
        }
        private fun onClickEdit() {
            // TODO open map editing (variant of adding map) activity
        }
        private fun onClickDelete() {
            // TODO delete map, maybe some 'Are u sure?' popup?
        }
    }

}