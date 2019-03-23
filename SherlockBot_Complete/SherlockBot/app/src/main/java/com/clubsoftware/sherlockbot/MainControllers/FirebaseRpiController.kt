package com.clubsoftware.sherlockbot.MainControllers

import android.util.Log
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import com.google.firebase.database.*
import java.text.ParseException

class FirebaseRpiController {
    private var TAG:String = "GPS Firebase"
    private var dbReference:DatabaseReference = FirebaseDatabase.getInstance().reference.child("sherlock")
    private var onChangeListener = object: ChildEventListener{
        override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            Log.d("", "Child Changed $p0")
            if (p0.key.equals("camera_state")) {
                if (p0.value == "on") {
                    CameraStreamController.startStreamAction()
                } else {
                    CameraStreamController.stopStreamAction()
                }
            }
            if(p0.key.equals("movement_command"))
            {
                RPiController.setDirectionMotor(p0.value as String)
            }

        }
        override fun onCancelled(p0: DatabaseError) {
            Log.v("", "Cancelled")
        }
        override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            Log.v("", "Child Moved")
        }
        override fun onChildAdded(dataSnapshot: DataSnapshot, prevChildKey: String?) {
            Log.d("", "Background Service" + dataSnapshot.key)

        }
        override fun onChildRemoved(p0: DataSnapshot) {
            Log.d("", "Child Removed")
        }
    }
    private val mLocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            try {
                val msg = "${location.latitude};${location.longitude};speed:${location.speed}"
                Log.v(TAG, msg)
                dbReference.child("gps_coordinate").setValue(msg)

            } catch (e: ParseException) {
                Log.v(TAG, e.message)
            }
        }
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
            Log.v(TAG, "Status changed, provider: $provider , status: $status")
        }
        override fun onProviderEnabled(provider: String) {
            Log.v(TAG, "Provider enabled: $provider")
        }
        override fun onProviderDisabled(provider: String) {
            Log.v(TAG, "Provider disabled: $provider")
        }
    }

    public fun initializeDBListeners(){
        dbReference.addChildEventListener(onChangeListener)
    }

    public fun initializeGPSCapture(context:Context){
        RPiController.startGPS(context,mLocationListener)
    }

}