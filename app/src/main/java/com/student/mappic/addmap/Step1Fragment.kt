package com.student.mappic.addmap

import android.content.res.Configuration
import android.graphics.drawable.GradientDrawable.Orientation
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.student.mappic.databinding.FragmentStep1Binding
import com.student.mappic.R

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
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

        if(!isPortraitMode())
            repositionFAB()

        binding.PhotoFAB.setOnClickListener {
            onClickPhoto()
        }
    }

    // TODO
    private fun onClickPhoto() {
        // TODO zrób zdjęcie
        Snackbar.make(binding.PhotoFAB, "You clicked me!", Snackbar.LENGTH_LONG)
            .setAnchorView(R.id.PhotoFAB)
            .setAction("Action", null).show()

        // nawiguj do następnego ekranu
        findNavController().navigate(R.id.nav_host_fragment_2)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Reposition FAB (camera button) to the right, in middle of height. Used when phone is in landscape mode.
     */
    private fun repositionFAB() {
        // WARNING! constraint is id of UI element (of type ConstraintLayout)
        var constr = ConstraintSet();
        constr.connect(R.id.constraint, ConstraintSet.TOP, R.id.PhotoFAB, ConstraintSet.TOP)
        constr.connect(R.id.constraint, ConstraintSet.END, R.id.PhotoFAB, ConstraintSet.END)
        constr.connect(R.id.constraint, ConstraintSet.BOTTOM, R.id.PhotoFAB, ConstraintSet.BOTTOM)
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