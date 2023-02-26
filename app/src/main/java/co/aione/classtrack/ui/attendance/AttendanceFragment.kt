package co.aione.classtrack.ui.attendance

import android.os.Binder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import co.aione.classtrack.R
import co.aione.classtrack.databinding.FragmentAttandenceBinding
import co.aione.classtrack.ui.student.OnItemClickListener
import co.aione.classtrack.ui.student.StudentAdapter

class AttendanceFragment : Fragment(), OnItemClickListener {

    private lateinit var viewModel: AttendanceViewModel
    private lateinit var binding: FragmentAttandenceBinding
    private lateinit var stream: String
    private lateinit var semester: String
    private lateinit var adapter: StudentAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAttandenceBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: AttendanceFragmentArgs by navArgs()
        stream = args.stream
        semester = args.semester
        viewModel = ViewModelProvider(this)[AttendanceViewModel::class.java]
        viewModel.getStudents(stream, semester)
        adapter = StudentAdapter(this, false)
        binding.recyclerStudent.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerStudent.adapter = adapter
    }

    override fun onResume() {
        super.onResume()

        viewModel.students.observe(this){
            adapter.submitList(it)
        }

    }

    override fun onDeleteButtonCLicked(roll: Int) {

    }

    override fun PresentORAbsent(isPresent: Boolean, roll: Int, total: Int) {
        viewModel.makePresent(isPresent, roll, stream, semester, total)
    }


}