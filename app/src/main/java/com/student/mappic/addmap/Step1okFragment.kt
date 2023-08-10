package com.student.mappic.addmap

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.student.mappic.databinding.FragmentStep1okBinding
import com.student.mappic.R

/**
 * A simple [Fragment], lets you review photo, before you continue creating map.
 * If you're not satisfied with photo you made, you can make another one!
 */
class Step1okFragment : Fragment() {

    private var _binding: FragmentStep1okBinding? = null

    private val viewModel: NewMapViewModel by activityViewModels()

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

        binding.imgView.setImageURI(viewModel.mapImg)

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
        // wasTODO save photo and pass reference to it, to database or temporary value holder
        // Photos are saved instantly, so there's no need to save again, just navigate to nxt fragment.
        // The only downside is, that user needs to manually delete unused photos. Could be done later.
        findNavController().navigate(R.id.action_step1ok_to_step2)
    }

    /**
     * Navigate to previous screen
     */
    private fun onClickNo() {
        /*
         * There are two cases. After making a photo, and after choosing it from gallery.
         * The simplest way would be to bypass navigation and open previous activity in the stack.
         */
        parentFragmentManager.popBackStackImmediate() // Navigates to previous step
        //findNavController().navigate(R.id.action_to_step1)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}