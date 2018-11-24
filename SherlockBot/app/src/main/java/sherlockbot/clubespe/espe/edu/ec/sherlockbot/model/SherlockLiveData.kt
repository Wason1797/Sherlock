package sherlockbot.clubespe.espe.edu.ec.sherlockbot.model

import android.arch.lifecycle.LiveData
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

class SherlockLiveData @Inject constructor(val firebase: DatabaseReference) : LiveData<Sherlock>() {

    companion object {
        private val TAG = Sherlock::class.java.simpleName!!
        private const val FIREBASE_SHERLOCK = "sherlock"
        private const val FIREBASE_MOVEMENT = "movement_command"
        private const val FIREBASE_CAMERA = "camera_state"
        private const val FIREBASE_GPS_COORDINATE = "gps_coordinate"
    }

    private val valueEventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val movement = snapshot.child(FIREBASE_MOVEMENT).getValue(String::class.java) ?: "still"
            val camera_state = snapshot.child(FIREBASE_CAMERA).getValue(String::class.java) ?: "off"
            val gps_coordinate=snapshot.child(FIREBASE_GPS_COORDINATE).getValue(String::class.java) ?: "0;0"
            val sherlock = Sherlock(camera_state, gps_coordinate, movement)
            Log.d(TAG, "onDataChange (value=$sherlock)")
            value = sherlock
        }

        override fun onCancelled(error: DatabaseError) {
            Log.w(TAG, "onCancelled", error.toException())
        }
    }

    override fun onActive() {
        super.onActive()
        firebase.child(FIREBASE_SHERLOCK).addValueEventListener(valueEventListener)
    }

    override fun onInactive() {
        super.onInactive()
        firebase.child(FIREBASE_SHERLOCK).removeEventListener(valueEventListener)
    }
    fun saveGPS(gps_data:String){
        firebase.child(FIREBASE_SHERLOCK).child(FIREBASE_GPS_COORDINATE).setValue(gps_data);
    }
}