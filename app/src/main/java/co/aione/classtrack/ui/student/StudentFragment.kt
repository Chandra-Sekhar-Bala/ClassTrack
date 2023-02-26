package co.aione.classtrack.ui.student

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import co.aione.classtrack.databinding.FragmentStudentBinding

class StudentFragment : Fragment(), OnItemClickListener {
    private lateinit var binding: FragmentStudentBinding
    private lateinit var vIewModel: StudentViewModel
    private lateinit var stream: String
    private lateinit var semester: String
    private lateinit var adapter: StudentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentStudentBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun verify(): Boolean {
        return binding.edtStdName.text.isNotEmpty() && binding.edtStdRoll.text.isNotEmpty()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data : StudentFragmentArgs by navArgs()
        semester = data.semester
        stream = data.stream
        vIewModel = ViewModelProvider(this)[StudentViewModel::class.java]
        vIewModel.getStudents(stream, semester)
        adapter = StudentAdapter(this)
        binding.recyclerStudent.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerStudent.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        binding.submitBtn.setOnClickListener {
            if(verify()){
                vIewModel.addStudent(stream, semester, binding.edtStdName.text.toString(), binding.edtStdRoll.text.toString())
                binding.edtStdName.text.clear()
                binding.edtStdRoll.text.clear()
            }
        }

        vIewModel.students.observe(this){
            adapter.submitList(it)
        }
        binding.takeAttendance.setOnClickListener {
            moveToAttendance()
        }
    }
    fun moveToAttendance(){
        val action = StudentFragmentDirections.actionStudentFragmentToAttendanceFragment(stream, semester)
        findNavController().navigate(action)
    }

    override fun onDeleteButtonCLicked(roll: Int) {

    }

}