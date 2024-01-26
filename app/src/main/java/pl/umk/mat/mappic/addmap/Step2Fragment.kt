package pl.umk.mat.mappic.addmap

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import pl.umk.mat.mappic.R
import pl.umk.mat.mappic.addmap.common.AddmapSteps2and3Utility
import pl.umk.mat.mappic.clist
import pl.umk.mat.mappic.databinding.FragmentStep2Binding

/**
 * Step2Fragment asks user to pin a point on map image and provide it's real geolocation.
 * It is Suggested to provide point geolocation through 'Use my location' button, to avoid typing.
 */
class Step2Fragment : Fragment() {

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

        Log.d(clist.Step3Fragment, ">>> Step 2")
        binding.imgView.setImageURI(viewModel.mapImg)
        // Img takes too much space when img is vertical. So I have to disable adjustViewBounds.
        if(utility.isImgVerticalExif(viewModel.mapImg)) {
            Log.d(clist.Step2Fragment, ">>> turning 'adjusting view bounds' off")
            binding.imgView.adjustViewBounds = false
        }

        // set onclicklisteners here
        binding.OkFAB.setOnClickListener {
            if(utility.saveUserInput(viewModel, 2)) {
                findNavController().navigate(R.id.action_step2_to_step3)
            }
        }
        binding.buttonReadGps.setOnClickListener {
            utility.getCoordinates()
        }

        binding.touchDetector?.setPassMotionEvent { utility.myViewClicked(it) }

        utility.fillFields(viewModel, 2)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

/*
    futureTODO understand what this fragment parameters are, it might be useful

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
        // futureTODO find difference between binding.inflate and inflater.inflate
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