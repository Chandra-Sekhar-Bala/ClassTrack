package co.aione.classtrack.ui.stream

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import co.aione.classtrack.databinding.FragmentStreamBinding
import co.aione.classtrack.Constants
import com.google.firebase.auth.FirebaseAuth


class StreamFragment : Fragment(), OnItemClickListener {

    private lateinit var binding: FragmentStreamBinding
    private lateinit var viewModel: StreamViewModel
    private lateinit var sharedPref: SharedPreferences
    private lateinit var adapter: StreamAdapter
    private lateinit var auth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentStreamBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//    TODO: Change the unique id with uid
        sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val uid = sharedPref.getString(Constants.UID, "default")
        auth = FirebaseAuth.getInstance()

        viewModel = ViewModelProvider(this)[StreamViewModel::class.java]
        viewModel.getAllSemester(Constants.unique_id)
        adapter = StreamAdapter(this)
        binding.recyclerStream.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerStream.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        binding.submitBtn.setOnClickListener {
            if (binding.edtStream.text.isEmpty()) {
                binding.edtStream.error = "Please give some input first"

            } else {
                viewModel.addNewSemester(binding.edtStream.text.toString())
                binding.edtStream.text.clear()
            }
        }
        viewModel.progress.observe(this) {
            Toast.makeText(
                context,
                when (it) {
                    Progress.FAILED -> "Failed"
                    Progress.SUCCESSFUL -> "Successful"
                    else -> ""
                },
                Toast.LENGTH_SHORT
            ).show()
        }

        viewModel.semesters.observe(this) {
            adapter.submitList(it)
            Log.i("TAGTAG", "ADAPTEE back: $it")
        }

    }

    override fun onDeleteButtonCLicked(sem: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete this Semester")
            .setMessage("Do you really want to delete this semester")
            .setPositiveButton("Yes"){_, _ ->
                viewModel.removeSemester(sem)
            }.setNegativeButton("No"){_,_->}
            .show()
    }


}