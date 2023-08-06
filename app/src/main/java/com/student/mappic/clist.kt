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
        const val CamiXOld: String = "CamiXOld"
        const val CamiX: String = "CamiX"
        const val Step0Fragment: String = "Step0Fragment"
        const val Step1Fragment: String = "Step1Fragment"
        const val Step1okFragment: String = "Step1okFragment"
        const val Step2Fragment: String = "Step2Fragment"
        const val Step3Fragment: String = "Step3Fragment"
        const val Step4Fragment: String = "Step4Fragment"
    }
}