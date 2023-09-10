package com.student.mappic.addmap

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.student.mappic.R
import com.student.mappic.addmap.common.AddmapSteps2and3Utility
import com.student.mappic.clist
import com.student.mappic.databinding.FragmentStep3Binding

/**
 * Step3Fragment asks user to pin another point on map image and provide it's real geolocation.
 * It is Suggested to provide point geolocation through 'Use my location' button, to avoid typing.
 */
class Step3Fragment : Fragment() {

    private var _binding: FragmentStep3Binding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: NewMapViewModel by activityViewModels()
    private lateinit var utility: AddmapSteps2and3Utility

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStep3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        utility = AddmapSteps2and3Utility(activity as AddMapActivity, clist.Step3Fragment)

        Log.d(clist.Step3Fragment, ">>> is this even executed???")
        binding.imgView.setImageURI(viewModel.mapImg)
        // Img takes too much space when img is vertical. So I have to disable adjustViewBounds.
        if(utility.isImgVerticalExif(viewModel.mapImg)) {
            Log.d(clist.Step3Fragment, ">>> turning 'adjusting view bounds' off")
            binding.imgView.adjustViewBounds = false
        }

        // set onclicklisteners here
        binding.OkFAB.setOnClickListener {
            if(utility.verifyUserInput()) {
                utility.saveUserInput()
                findNavController().navigate(R.id.action_step3_to_step4)
            }
        }
        binding.buttonReadGps.setOnClickListener {
            utility.fillGpsCoordinates()
            Log.d(clist.Step3Fragment, ">>> Action not available yet.")
        }

        binding.touchDetector?.setPassMotionEvent { utility.myViewClicked(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}