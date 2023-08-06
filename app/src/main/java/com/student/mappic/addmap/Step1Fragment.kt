package com.student.mappic.addmap

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.student.mappic.databinding.FragmentStep1Binding
import com.student.mappic.R
import com.student.mappic.clist

/**
 * This is fragment where user takes a picture of a map.
 * After clicking the button it should pass picture to Step1okFragment.
 */
class Step1Fragment : Fragment() {

    private var _binding: FragmentStep1Binding? = null
    private lateinit var camiX: CamiX
    private lateinit var addMap: AddMapActivity

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStep1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(clist.Step1Fragment, ">>> onViewCreated()")
        if(!isPortraitMode())
            repositionFAB()

        addMap = (activity as AddMapActivity)
        camiX = CamiX(addMap, R.id.camView)

        //addmap.step1() requests permissions for Camera, and then launches camiX.startCamera()
        addMap.step1 { camiX.startCamera() }

        binding.PhotoFAB.setOnClickListener {
            onClickPhoto()
        }
    }

    /**
     * This should serve onClickPhoto().
     * It should:
     *  - take a photo,
     *  TODO - pass photo (or its reference) to next fragment
     */
    private fun onClickPhoto() {

        // really, it just takes photo and saves to MediaStore
        camiX.takePhoto()

        // temporary
        Snackbar.make(binding.PhotoFAB, "You clicked take photo!", Snackbar.LENGTH_LONG)
            .setAnchorView(R.id.PhotoFAB)
            .setAction("Action", null).show()

        // TODO send photo reference to Step1okFragment
        // navigate to next screen
        findNavController().navigate(R.id.action_step1_to_step1ok)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        camiX.closeCamera()
    }

    /**
     * Reposition FAB (camera button) to the right, in middle of height. Used when phone is in landscape mode.
     */
    private fun repositionFAB() {
        Log.d(clist.Step1Fragment, ">>> repositioning FAB...")
        // WARNING! constraint is id of UI element (of type ConstraintLayout)
        var constr = ConstraintSet();
        constr.connect(R.id.PhotoFAB, ConstraintSet.TOP, R.id.takePhotoText, ConstraintSet.BOTTOM)
        constr.connect(R.id.PhotoFAB, ConstraintSet.END, R.id.constraint, ConstraintSet.END)
        constr.connect(R.id.PhotoFAB, ConstraintSet.BOTTOM, R.id.constraint, ConstraintSet.BOTTOM)
        constr.applyTo(binding.constraint)
    }
    private fun rotation(): Int {
        return resources.configuration.orientation
    }

    /**
     * Returns true, when phone is vertical (in portrait mode) or any other than landscape mode.
     */
    private fun isPortraitMode(): Boolean {
        return rotation() != Configuration.ORIENTATION_LANDSCAPE
    }
}