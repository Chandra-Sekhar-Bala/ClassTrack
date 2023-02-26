package co.aione.classtrack.ui.semester

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import co.aione.classtrack.Constants
import co.aione.classtrack.databinding.FragmentSemesterBinding
import co.aione.classtrack.ui.stream.OnItemClickListener
import co.aione.classtrack.ui.stream.Progress
import co.aione.classtrack.ui.stream.StreamAdapter
import co.aione.classtrack.ui.stream.StreamFragmentDirections

class SemesterFragment : Fragment(), OnItemClickListener {
    private lateinit var binding: FragmentSemesterBinding
    private lateinit var viewModel: SemViewModel
    private lateinit var stream : String
    private lateinit var adapter: StreamAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSemesterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[SemViewModel::class.java]
        val args : SemesterFragmentArgs by navArgs()
        stream = args.sem
        viewModel.getAllSemester(Constants.unique_id, stream)

        adapter = StreamAdapter(this)
        binding.recyclerSemester.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerSemester.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        binding.submitBtn.setOnClickListener{
            if (binding.edtSemester.text.isEmpty()) {
                binding.edtSemester.error = "Please give some input first"

            } else {
                viewModel.addSemester(stream, binding.edtSemester.text.toString())
                binding.edtSemester.text.clear()
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

            binding.progressbar.visibility = when(it){
                Progress.LOADING -> View.VISIBLE
                Progress.SUCCESSFUL -> View.GONE
                else -> View.GONE
            }
        }
        viewModel.semesters.observe(this){
            adapter.submitList(it)
        }
    }

    override fun onDeleteButtonCLicked(sem: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete this Semester")
            .setMessage("Do you really want to delete this semester")
            .setPositiveButton("Yes"){_, _ ->
                viewModel.removeSemester(stream, sem)
            }.setNegativeButton("No"){_,_->}
            .show()
    }

    override fun OnItemCicked(sem: String) {
//        val action  = SemesterFragmentArgs
//        findNavController().navigate(action)
    }


}