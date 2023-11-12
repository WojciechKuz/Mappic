package com.student.mappic

open class clist() {
    /**
     * List of classes for faster Log.d(tag, msg) lines creation.
     * Here, classes are tags.
     */
    companion object {
        // main group
        const val MainActivity: String = "MainActivity"
        const val MapOptionsPopup: String = "MapOptionsPopup"
        const val SnackShow: String = "SnackShow"

        // viewmap group
        const val ViewMapActivity: String = "ViewMapActivity"

        // maplist group
        const val MyMapItemRecyclerViewActivity: String = "MyMapItemRecyclerViewActivity"
        const val MapItemFragment: String = "MapItemFragment"
        const val PlaceHolderContent: String = "PlaceHolderContent" // change this !!!

        // addmap group
        const val AddMapActivity: String = "AddMapActivity"
        const val CamiX: String = "CamiX"
        const val PermissionManager: String = "PermissionManager"
        const val LocationProvider: String = "LocationProvider"
        const val MyView: String = "MyView"
        const val MapGLSurfaceView = "MapGLSurfaceView"
        const val MapGLRenderer = "MapGLRenderer"
        const val PickPhoto: String = "PickPhoto"
        const val Step0Fragment: String = "Step0Fragment"
        const val Step1Fragment: String = "Step1Fragment"
        const val Step1okFragment: String = "Step1okFragment"
        const val Step2Fragment: String = "Step2Fragment"
        const val Step3Fragment: String = "Step3Fragment"
        // AddmapSteps2and3Utility uses one of two above.
        const val Step4Fragment: String = "Step4Fragment"
    }
}