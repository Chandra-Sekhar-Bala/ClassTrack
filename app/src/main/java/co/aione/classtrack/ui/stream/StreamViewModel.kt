package co.aione.classtrack.ui.stream

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

    private var _streams = MutableLiveData<List<String>>()
    val streams: LiveData<List<String>>
        get() = _streams

    private var _progress = MutableLiveData<Progress>()
    val progress: LiveData<Progress>
        get() = _progress
    fun getAllStream(uid: String) {
        viewModelScope.launch {
            ref.child(uid).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val list = mutableListOf<String>()
                        for (userSnapshot in snapshot.children) {
                            list.add(userSnapshot.key.toString())
                        }
                        _streams.value = list
                    }
                    _progress.value = Progress.FAILED
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