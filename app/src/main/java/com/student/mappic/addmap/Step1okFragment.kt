package com.student.mappic.addmap

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.student.mappic.databinding.FragmentStep1okBinding
import com.student.mappic.R
import com.student.mappic.clist

/**
 * This lets you review photo, before you continue creating map.
 * If you're not satisfied with photo you made, you can make another one!
 */
class Step1okFragment : Fragment() {

    // TODO 'edit image' button - Intent to other apps for editing image

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

        try {
            // FUTURE_FIXME: check if app has permission to open image with this uri
            binding.imgView.setImageURI(viewModel.mapImg)
        } catch (e: Exception) {
            Log.e(clist.Step1okFragment, ">>> Can't view image. It's probably due to lack of permissions.")
            println(e)
            // after catching exception it crashes anyway
        }

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
     * This simply opens previous fragment/activity in the stack.
     * Navigate to [Step1Fragment].
     */
    private fun onClickNo() {
        parentFragmentManager.popBackStackImmediate() // Navigates to previous step
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}