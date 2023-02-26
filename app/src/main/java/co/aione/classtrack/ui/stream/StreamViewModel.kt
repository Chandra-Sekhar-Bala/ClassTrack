package co.aione.classtrack.ui.stream

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.aione.classtrack.Constants
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

enum class Progress { LOADING, SUCCESSFUL, FAILED }
class StreamViewModel : ViewModel() {
    private val dbRef = Firebase.database
    private val ref = dbRef.getReference(Constants.USERS)

    private var _semesters = MutableLiveData<List<String>>()
    val semesters: LiveData<List<String>>
        get() = _semesters

    private var _progress = MutableLiveData<Progress>()
    val progress: LiveData<Progress>
        get() = _progress
    fun getAllSemester(uid: String) {
        viewModelScope.launch {
            ref.child(uid).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val list = mutableListOf<String>()
                        for (userSnapshot in snapshot.children) {
                            list.add(userSnapshot.key.toString())
                        }
                        Log.i("TAGTAG", "onDataChange: ${_semesters.value}")
                        _semesters.value = list

                        Log.i("TAGTAG", "onDataChange+: ${_semesters.value}")
                    }else{
                        Log.i("TAGTAG", "Does not eist")
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }
//    TODO: Change the unique id with uid
    fun addNewSemester(sem: String) {
        dbRef.getReference(Constants.USERS).child(Constants.unique_id).child(sem).child("SEM")
            .setValue(sem)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                  _progress.value = Progress.SUCCESSFUL
                } else {
                  _progress.value = Progress.FAILED
                }
            }

    }

    fun removeSemester(sem: String){
        dbRef.getReference(Constants.USERS).child(Constants.unique_id).child(sem)
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