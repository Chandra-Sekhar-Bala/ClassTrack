package co.aione.classtrack.ui.student

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.aione.classtrack.Constants
import co.aione.classtrack.model.StudentModel
import co.aione.classtrack.ui.stream.Progress
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class StudentViewModel : ViewModel() {

    private val dbRef = Firebase.database
    private val ref = dbRef.getReference(Constants.USERS)

    private var _students = MutableLiveData<List<StudentModel>>()
    val students: LiveData<List<StudentModel>>
        get() = _students

    private var _progress = MutableLiveData<Progress>()
    val progress: LiveData<Progress>
        get() = _progress

    fun getStudents(stream: String, semester: String) {

        ref.child(Constants.unique_id).child(stream).child(semester).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val list = mutableListOf<StudentModel>()
                for (childSnapshot in dataSnapshot.children) {
                    val roll = childSnapshot.key
                    val value = childSnapshot.value
                    if (value is Map<*, *>) {
                        val name = value["name"] as String
                        val present = value["present"] as Long
                        if (roll != null) {
                            list.add(StudentModel(roll.toInt(), name, present.toInt()))
                        }
                    }
                }
                _students.value = list
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
                println("Error: ${databaseError.message}")
            }
        })

    }

    fun addStudent(stream: String, semester: String, name: String, roll: String) {
        viewModelScope.launch {
            val map = mapOf("name" to name, "present" to 0)
            ref.child(Constants.unique_id).child(stream).child(semester)
                .child(roll).setValue(map).addOnCompleteListener {
                    if (it.isSuccessful) {
                        _progress.value = Progress.SUCCESSFUL
                    } else {
                        _progress.value = Progress.FAILED
                    }
                }
        }
    }


}