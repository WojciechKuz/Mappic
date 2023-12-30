package com.student.mappic.addmap

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.student.mappic.databinding.FragmentStep1Binding
import com.student.mappic.R
import com.student.mappic.addmap.features.images.CamiX
import com.student.mappic.addmap.features.permissions.PermissionManager
import com.student.mappic.clist

/**
 * This is fragment where user takes a picture of a map.
 * After clicking the button it passes picture to [Step1okFragment].
 */
class Step1Fragment : Fragment() {

    private var _binding: FragmentStep1Binding? = null
    private lateinit var camiX: CamiX
    private lateinit var addMap: AddMapActivity

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStep1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(clist.Step1Fragment, ">>> onViewCreated()")
        //if(!isPortraitMode())
        //    repositionFAB()

        addMap = (activity as AddMapActivity)
        camiX = CamiX(addMap, R.id.camView, "Mappic")

        //addmap.step1() requests permissions for Camera, and then launches camiX.startCamera()
        //addMap.permManager.grantCamPerm { camiX.startCamera() }
        //PermissionManager(addMap).grantCamPerm { camiX.startCamera() }
        addMap.getPermissionManager().grantCamPerm { camiX.startCamera() }

        /**
         * Set onclick listener for button.
         * It's done via interface, because CameraX results aren't available immediately.
         */
        binding.PhotoFAB.setOnClickListener {
            camiX.takePhoto { setMapImg(it) }
        }
    }

    private val viewModel: NewMapViewModel by activityViewModels()

    /**
     * This method receives image Uri as result from camiX.takePhoto()
     * and passes this photo reference to [Step1okFragment] through ViewModel.
     * Then, navigates to this fragment.
     */
    private fun setMapImg(recvImgUri: Uri?) {
        if(recvImgUri != null)
            viewModel.mapImg = recvImgUri

        val msg = R.string.photo_success // Old message: "Photo capture succeeded: ${viewModel.mapImg}"
        Toast.makeText(addMap.baseContext, msg, Toast.LENGTH_SHORT).show()

        // navigate to next screen
        findNavController().navigate(R.id.action_step1_to_step1ok)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        camiX.closeCamera()
    }
}