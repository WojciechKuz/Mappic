package com.student.mappic.addmap

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
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
class Step2Fragment : Fragment() {

    private var _binding: FragmentStep2Binding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: NewMapViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStep2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(clist.Step2Fragment, ">>> is this even executed???")
        binding.imgView.setImageURI(viewModel.mapImg)
        // Img takes too much space when img is vertical. So I have to disable adjustViewBounds.
        if(isImgVerticalExif(viewModel.mapImg)) {
            Log.d(clist.Step2Fragment, ">>> turning adjusting view bounds off")
            binding.imgView.adjustViewBounds = false
        }

        // set onclicklisteners here
        binding.OkFAB.setOnClickListener {
            findNavController().navigate(R.id.action_step2_to_step3)
        }
        binding.buttonReadGps.setOnClickListener {
            // TODO fill text fields with GPS coordinates
            Log.d(clist.Step3Fragment, ">>> Action not available yet.")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Decodes from given Uri size of image, and if it's height is greater than width it returns true.
     */
    private fun isImgVertical(uri: Uri): Boolean {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        val addMap = (activity as AddMapActivity)
        val istream = addMap.contentResolver.openInputStream(uri)
            ?: throw Exception("Input stream 'istream' is null!") // do it when istream is null
        Log.d(clist.Step2Fragment, ">>> Decoding size of image")
        BitmapFactory.decodeStream(istream, null, options)
        istream.close()
        Log.d(clist.Step2Fragment, ">>> Size of image: h: ${options.outHeight}, w: ${options.outWidth}")
        return (options.outHeight > options.outWidth)
    }

    private fun isImgVerticalExif(uri: Uri): Boolean {
        val addMap = (activity as AddMapActivity)
        val istream = addMap.contentResolver.openInputStream(uri)
            ?: throw Exception("Input stream 'istream' is null!") // do it when istream is null
        Log.d(clist.Step2Fragment, ">>> Decoding size of image")
        val exif = ExifInterface(istream)
        val height: Int = exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, -1)
        val width: Int = exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, -1)
        val orientation: Int = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1)
        istream.close()
        Log.d(clist.Step2Fragment, ">>> Size of image: h: ${height}, w: ${width}, o: ${orientation}") // o 6=vert, 1,3=horiz
        return (height > width)
    }

    // It's really nice solution, though it may take lot of resources to decode whole image instead of only the size.
    @RequiresApi(Build.VERSION_CODES.P)
    private fun isImgVertical29(uri: Uri): Boolean {
        val addMap = (activity as AddMapActivity)
        /*
        var size: Size
        val listener =
            OnHeaderDecodedListener { decoder, info, source -> size = info.size}
        val source = ImageDecoder.createSource(addMap.contentResolver, uri)
        val bitmap: Bitmap = ImageDecoder.decodeBitmap(source, listener)
        if (size == null)
            Log.e(clist.Step2Fragment, "")
        return (size.height > size.width)
        */
        val source = ImageDecoder.createSource(addMap.contentResolver, uri)
        val bitmap: Bitmap = ImageDecoder.decodeBitmap(source)
        return (bitmap.height > bitmap.width)
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