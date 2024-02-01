package pl.umk.mat.mappic.addmap

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import pl.umk.mat.mappic.databinding.FragmentStep1okBinding
import pl.umk.mat.mappic.R
import pl.umk.mat.mappic.clist

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

        if(viewModel.fromPicker && !viewModel.fromPickerNavigated) {
            viewModel.fromPickerNavigated = true
            findNavController().navigate(R.id.action_step1ok_to_step2)
        }
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
        binding.EditFAB?.setOnClickListener {
            onClickEdit()
        }
        binding.NoFAB.setOnClickListener {
            onClickNo()
        }

        // system back button press, navigate to previous step. It's same as onClickNo()
        requireActivity().onBackPressedDispatcher.addCallback(this) { onClickNo() }
    }

    /**
     * Navigate to next screen
     */
    private fun onClickYes() {
        // Photos are saved instantly, so there's no need to save again, just navigate to nxt fragment.
        // The only downside is, that user needs to manually delete unused photos. Could be done later.
        findNavController().navigate(R.id.action_step1ok_to_step2)
    }

    /** Launch intent to edit image */
    fun onClickEdit() {
        if (viewModel.uriInitialized()) {
            val intent = Intent(Intent.ACTION_EDIT)
            intent.setDataAndType(viewModel.mapImg, "image/*")
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(Intent.createChooser(intent, null))
        }
    }

    /**
     * This simply opens previous fragment/activity in the stack.
     * Navigate to [Step1Fragment].
     */
    private fun onClickNo() {
        val intent = Intent(activity as AddMapActivity, AddMapActivity::class.java)

        if(viewModel.mapid != null) { // edit mode
            intent.putExtra("whichmap", viewModel.mapid)
            intent.putExtra("startWith", 0)
            Log.d(clist.Step1okFragment, ">>> Restart activity. edit mode")
            startActivity(intent)
            return
        }

        // if from photopicker, start from step0
        if(viewModel.fromPicker) {
            // can't use navigateBack because there's onActivity register there and back navigating there causes app crash
            if(viewModel.mapid != null) { // edit mode
                intent.putExtra("whichmap", viewModel.mapid)
                intent.putExtra("startWith", 0)
            }
            Log.d(clist.Step1okFragment, ">>> Restart activity. frompicker")
            startActivity(intent)
            return
        }
        Log.d(clist.Step1okFragment, ">>> Normal navigate back")
        parentFragmentManager.popBackStackImmediate() // Navigates to step1

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}