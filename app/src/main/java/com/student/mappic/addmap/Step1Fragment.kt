package com.student.mappic.addmap

import android.content.res.Configuration
import android.graphics.drawable.GradientDrawable.Orientation
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

        (activity as AddMapActivity).startPreview()

        binding.PhotoFAB.setOnClickListener {
            onClickPhoto()
        }
    }

    // TODO
    private fun onClickPhoto() {
        // TODO take a photo

        // FIXME temporary
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