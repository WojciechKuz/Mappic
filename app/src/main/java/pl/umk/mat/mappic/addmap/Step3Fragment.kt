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
import pl.umk.mat.mappic.addmap.shared_step2and3.AddmapSteps2and3Utility
import pl.umk.mat.mappic.clist
import pl.umk.mat.mappic.databinding.FragmentStep3Binding

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
        utility.step2and3.binding3 = binding

        Log.d(clist.Step3Fragment, ">>> Step 3")
        binding.imgMapView?.setImageURI(viewModel.mapImg)
        // Img takes too much space when img is vertical. So I have to disable adjustViewBounds.
        if(utility.isImgVerticalExif(viewModel.mapImg)) {
            Log.d(clist.Step3Fragment, ">>> turning 'adjusting view bounds' off")
            binding.imgMapView?.adjustViewBounds = false
        }

        // set onclicklisteners here
        binding.OkFAB.setOnClickListener {
            if(utility.saveUserInput(viewModel, 3)) {
                findNavController().navigate(R.id.action_step3_to_step4)
            }
        }
        binding.buttonReadGps.setOnClickListener {
            utility.getCoordinates()
        }

        binding.touchDetector?.setPassMotionEvent { utility.myViewClicked(it) }

        utility.fillFields(viewModel, 3)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}