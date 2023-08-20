package com.student.mappic.addmap

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.student.mappic.R
import com.student.mappic.clist
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

    private val viewModel: NewMapViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStep0Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonCam.setOnClickListener {
            Log.d(clist.Step0Fragment, ">>> take new picture button clicked")
            takePicture()
        }
        binding.buttonFile.setOnClickListener {
            Log.d(clist.Step0Fragment, ">>> open photo from gallery button clicked")
            openFromGallery()
        }
    }

    /**
     *  Triggered when 'New photo' button is clicked.
     *  [Step1Fragment] (taking photo screeen) is then displayed.
     */
    private fun takePicture() {
        Log.d(clist.Step0Fragment, ">>> navController is alright!\n trying to navigate up...")
        findNavController().navigate(R.id.action_step0_to_step1)
    }

    /**
     *  Triggered when 'Choose from gallery' button is clicked.
     *  Choose picture from gallery - open system picker.
     *  After photo is picked, [photoPicked] is triggered.
     */
    private fun openFromGallery() {
        // opens system photo picker
        val pickPhoto = PickPhoto(activity as AddMapActivity)
        pickPhoto.pickPhoto( { photoPicked(it) }, { /* Do nothing. */} )
    }

    /**
     * After photo has been picked,
     * it's saved to [NewMapViewModel] (viewModel for passing data to next steps), then
     * [Step2Fragment] is opened.
     */
    private fun photoPicked(photo: Uri?) {
        if (photo != null) {
            viewModel.mapImg = photo
            findNavController().navigate(R.id.action_step0_to_step2)
        }
        else  {
            // What a Terrible Failure! This error can't happen logically
            Log.wtf(clist.Step0Fragment, ">>> uri passed by interface is null! How did this even happen?")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}