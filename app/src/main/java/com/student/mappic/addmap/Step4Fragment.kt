package com.student.mappic.addmap

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.student.mappic.R
import com.student.mappic.addmap.common.ErrTypes
import com.student.mappic.databinding.FragmentStep4Binding

/**
 * In this fragment user types in map name.
 * Name should be verified if it not exist yet.
 * TODO In this fragment all data typed by the user in addmap process are added to database.
 * !!! Access to database should be realised via additional utility class, not directly here. !!!
 */
class Step4Fragment : Fragment() {

    private var _binding: FragmentStep4Binding? = null

    private val binding get() = _binding!!

    private val viewModel: NewMapViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStep4Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.OkFAB.setOnClickListener { saveNewMap() }
    }

    private fun saveNewMap() {
        // verify name
        if(checkIfNameExists()) {
            // TODO display red captions 'DBMap with this name already exists'
            return;
        }
        viewModel.name = binding.mapNameField.text.toString()

        // TODO save viewModel data to DB

        // TODO goto MapList activity

    }

    /**
     * Checks if map with this name exists.
     *  - name exists -> true
     *  - name doesn't exist -> false
     */
    private fun checkIfNameExists(): Boolean {
        val typedName = binding.mapNameField.text
        // TODO check if name exists, if yes call displayErrMsg()

        return false // FIXME temporary
    }

    fun displayErrMsg(err: ErrTypes) {
        val array: Array<String> = (activity as AddMapActivity).resources.getStringArray(R.array.errTypesMessages)
        val textView = binding.errText
        textView.text = array[err.code]
        textView.visibility = TextView.VISIBLE
    }
}