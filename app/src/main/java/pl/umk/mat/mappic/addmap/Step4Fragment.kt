package pl.umk.mat.mappic.addmap

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import pl.umk.mat.mappic.db.entities.DBMap
import pl.umk.mat.mappic.MainActivity
import pl.umk.mat.mappic.R
import pl.umk.mat.mappic.addmap.common.ErrTypes
import pl.umk.mat.mappic.addmap.common.Signal
import pl.umk.mat.mappic.databinding.FragmentStep4Binding

/**
 * In this fragment user types in map name.
 * Name should be verified if it not exist yet.
 * In this fragment all data typed by the user in addmap process are added to database.
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

    /**
     * Save new map to DB. If map with same name already exists, map won't be saved and
     * error will be displayed to user.
     */
    private fun saveNewMap() {
        binding.errText.text = getString(R.string.please_wait)

        // verify name - it had to be this way, because DB check is performed asynchronously
        checkIfNameExists(
            whenTrue = {
                displayErrMsg(ErrTypes.NAME_EXISTS)
            },
            whenFalse = {
                viewModel.name = binding.mapNameField.text.toString().trim()

                // save viewModel data to DB
                if (viewModel.mapid == null)
                    viewModel.addNewMap(activity as AddMapActivity)
                else
                    viewModel.editMap(activity as AddMapActivity)

                val msg = R.string.saved_to_db
                Toast.makeText(activity as AddMapActivity, msg, Toast.LENGTH_SHORT).show()
                // goto MapList activity
                val gotoList = Intent(activity as AddMapActivity, pl.umk.mat.mappic.MainActivity::class.java)
                startActivity(gotoList)
            }
        )
    }

    /**
     * Checks if map with this name exists.
     *  - name exists -> true
     *  - name doesn't exist -> false
     */
    private fun checkIfNameExists(whenTrue: Signal, whenFalse: Signal) {
        val typedName = binding.mapNameField.text
        // get map names from DB
        var dbMaps: List<DBMap>?
        viewModel.getMapList(activity as AddMapActivity) {
            dbMaps = it
            // check if name exists
            if(dbMaps!!.any{it.map_name == typedName.toString().trim()}) // true if any name from list equals to typedName
                whenTrue.startAction()
            else
                whenFalse.startAction()
        }
    }


    fun displayErrMsg(err: ErrTypes) {
        val array: Array<String> = (activity as AddMapActivity).resources.getStringArray(R.array.errTypesMessages)
        val textView = binding.errText
        textView.text = array[err.code]
        textView.visibility = TextView.VISIBLE
    }
}