package com.student.mappic.addmap

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.student.mappic.R
import com.student.mappic.clist
import com.student.mappic.databinding.FragmentStep2Binding

/**
 * Step2Fragment asks user to pin a point on map image and provide it's real geolocation.
 * It is Suggested to provide point geolocation through 'Use my location' button, to avoid typing.
 */
class Step2Fragment : Fragment(), OnTouchListener {

    private var _binding: FragmentStep2Binding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: NewMapViewModel by activityViewModels()
    private lateinit var utility: AddmapSteps2and3Utility

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStep2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        utility = AddmapSteps2and3Utility(activity as AddMapActivity, clist.Step2Fragment)

        Log.d(clist.Step2Fragment, ">>> is this even executed???")
        binding.imgView.setImageURI(viewModel.mapImg)
        // Img takes too much space when img is vertical. So I have to disable adjustViewBounds.
        if(utility.isImgVerticalExif(viewModel.mapImg)) {
            Log.d(clist.Step2Fragment, ">>> turning 'adjusting view bounds' off")
            binding.imgView.adjustViewBounds = false
        }

        // set onclicklisteners here
        binding.OkFAB.setOnClickListener {
            if(utility.verifyUserInput()) {
                utility.saveUserInput()
                findNavController().navigate(R.id.action_step2_to_step3)
            }
        }
        binding.buttonReadGps.setOnClickListener {
            utility.fillGpsCoordinates()
            Log.d(clist.Step3Fragment, ">>> Action not available yet.")
        }
        // OpenGL probably not needed. Just need to display icon/graphic and get point, where user touched

        // FIXME temporary:
        utility.displayErrMsg(binding.errorText, ErrTypes.INCORRECT_GPS)
    }

    // This does not work. Override onTouchEvent in Activity or any other View
    override fun onTouch(v: View?, motEv: MotionEvent?): Boolean {
        Log.d(clist.Step2Fragment, "> touch")
        if(motEv != null) {
            binding.gpsLatitude.setText("x: " + motEv.x + "; y: " + motEv.y)
            if(motEv.action == MotionEvent.ACTION_UP) {
                Log.d(clist.Step2Fragment, ">>> Podniesiono palec w " + "x: " + motEv.x + "; y: " + motEv.y)
            }
        }
        v?.performClick();
        return true;
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

/*
    TODO understand what this fragment parameters are, it might be useful

// nTODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Step2Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Step2Fragment : Fragment() {
    // nTODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // TODO find difference between binding.inflate and inflater.inflate
        return inflater.inflate(R.layout.fragment_step2, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Step2Fragment.
         */
        // nTODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Step2Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
*/