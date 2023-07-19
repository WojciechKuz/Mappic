package com.student.mappic.addmap

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.student.mappic.R
import com.student.mappic.databinding.FragmentStep0Binding

/**
 * Step0Fragment asks user if he/she wants to take new picture or choose one from gallery.
 * There are two buttons doing just that.
 */
class Step0Fragment : Fragment() {

    private var _binding: FragmentStep0Binding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStep0Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set onclicklisteners here
        binding.buttonCam.setOnClickListener {
            takePicture()
        }
        binding.buttonFile.setOnClickListener {
            openFromGallery()
        }
    }

    /**
     *  Triggered when 'new photo' button is clicked.
     *  Step1Fragment (taking photo screeen) is then displayed.
     */
    private fun takePicture() {
        findNavController().navigate(R.id.nav_host_fragment_2)
    }
    /**
     *  Triggered when 'Choose from gallery' button is clicked.
     *  Choose picture from gallery - open system picker and
     *  open Step1okFragment (screen asking user if it's right photo).
     */
    private fun openFromGallery() {
        //TODO open system photo picker

        //TODO pass photo reference to Step1okFragment
        findNavController().navigate(R.id.reviewPhoto)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}