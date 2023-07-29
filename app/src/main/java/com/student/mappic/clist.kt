package com.student.mappic

open class clist() {
    /**
     * List of classes for faster Log.d(tag, msg) lines creation.
     * Here, classes are tags.
     */
    companion object {
        // main group
        val MainActivity: String = "MainActivity"
        val MapOptionsPopup: String = "MapOptionsPopup"
        val SnackShow: String = "SnackShow"

        // viewmap group
        val ViewMapActivity: String = "ViewMapActivity"

        // maplist group
        val MyMapItemRecyclerViewActivity: String = "MyMapItemRecyclerViewActivity"
        val MapItemFragment: String = "MapItemFragment"
        val PlaceHolderContent: String = "PlaceHolderContent" // change this !!!

        // addmap group
        val AddMapActivity: String = "AddMapActivity"
        val CamiX: String = "CamiX"
        val Step0Fragment: String = "Step0Fragment"
        val Step1Fragment: String = "Step1Fragment"
        val Step1okFragment: String = "Step1okFragment"
        val Step2Fragment: String = "Step2Fragment"
        val Step3Fragment: String = "Step3Fragment"
        val Step4Fragment: String = "Step4Fragment"
    }
}