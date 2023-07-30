package com.student.mappic.addmap

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.navigation.fragment.findNavController
import com.student.mappic.databinding.FragmentStep1okBinding
import com.student.mappic.R

/**
 * A simple [Fragment], lets you review photo, before you continue creating map.
 * If you're not satisfied with photo you made, you can make another one!
 */
class Step1okFragment : Fragment() {

    private var _binding: FragmentStep1okBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStep1okBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.YesFAB.setOnClickListener {
            onClickYes()
        }
        binding.NoFAB.setOnClickListener {
            onClickNo()
        }
    }

    /**
     * Navigate to next screen
     */
    private fun onClickYes() {
        // TODO save photo and pass reference to it, to database or temporar value holder
        findNavController().navigate(R.id.action_step1ok_to_step2)
    }

    /**
     * Navigate to previous screen
     */
    private fun onClickNo() {
        /*
         * There are two cases. After making a photo, and after choosing it from gallery.
         * TODO navigate depending on which case it is.
         * The simplest way would be to bypass navigation and open previous activity in the stack.
         */
        findNavController().navigate(R.id.action_to_step1)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}