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
        findNavController().navigate(R.id.photoAccepted)
    }

    /**
     * Navigate to previous screen
     */
    private fun onClickNo() {
        findNavController().navigate(R.id.nav_host_fragment_2)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}