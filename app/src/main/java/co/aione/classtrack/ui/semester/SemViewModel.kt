package co.aione.classtrack.ui.semester

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.aione.classtrack.Constants
import co.aione.classtrack.ui.stream.Progress
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

enum class Progress { LOADING, SUCCESSFUL, FAILED }
class SemViewModel : ViewModel() {
    private val dbRef = Firebase.database
    private val ref = dbRef.getReference(Constants.USERS)

    private var _semesters = MutableLiveData<List<String>>()
    val semesters: LiveData<List<String>>
        get() = _semesters

    private var _progress = MutableLiveData<Progress>()
    val progress: LiveData<Progress>
        get() = _progress

    fun getAllSemester(uniqueId: String, stream: String) {
        viewModelScope.launch {
            ref.child(uniqueId).child(stream).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val list = mutableListOf<String>()
                        for (userSnapshot in snapshot.children) {
                            if (userSnapshot.key.toString() == "SEM") continue
                            list.add(userSnapshot.key.toString())
                        }
                        _semesters.value = list
                    } else {
                        _progress.value = Progress.FAILED
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }

    fun addSemester(stream: String, sem: String) {
        viewModelScope.launch {
            ref.child(Constants.unique_id).child(stream).child(sem).child("SEM")
                .setValue(sem)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        _progress.value = Progress.SUCCESSFUL
                        getAllSemester(Constants.unique_id, sem)
                    } else {
                        _progress.value = Progress.FAILED
                    }
                }
        }

    }

    fun removeSemester(stream: String, sem: String) {
        viewModelScope.launch {
            dbRef.getReference(Constants.USERS).child(Constants.unique_id).child(stream).child(sem)
                .removeValue()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        _progress.value = Progress.SUCCESSFUL
                    } else {
                        _progress.value = Progress.FAILED
                    }
                }
        }
    }

}