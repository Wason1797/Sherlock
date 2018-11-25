package com.example.wason.sherlockbot

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.pedro.rtplibrary.rtmp.RtmpCamera2
import net.ossrs.rtmp.ConnectCheckerRtmp

/**
 * Skeleton of an Android Things activity.
 *
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 *
 * <pre>{@code
 * val service = PeripheralManagerService()
 * val mLedGpio = service.openGpio("BCM6")
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
 * mLedGpio.value = true
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 *
 */
class MainActivity : Activity(), ConnectCheckerRtmp,
    View.OnClickListener, SurfaceHolder.Callback {

    private var rtmpCamera2: RtmpCamera2? = null
    private var button_start_stop: Button? = null
    private var button_camera: Button? = null
    private var etUrl: String = "your rtmp video url"
    private var gpsTaskBuilder: GpsTaskBuilder? = null
    private var mDatabaseRef: DatabaseReference? = null

    private val motorControll : BoardController?=
        BoardController()

    private val dbChangeListener = object : ChildEventListener {
        override fun onCancelled(p0: DatabaseError) {
            Log.v("", "Cancelled")
        }

        override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            Log.v("", "Child Moved")
        }

        override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            Log.d("", "Child Changed $p0")
            if (p0.key.equals("camera_state")) {
                if (p0.value == "on") {
                    startStreamAction()
                } else {
                    stopStreamAction()
                }
            }
            if(p0.key.equals("movement_command"))
            {
                motorControll!!.setDirectionMotor(p0.value as String)
            }

        }

        override fun onChildAdded(dataSnapshot: DataSnapshot, prevChildKey: String?) {
            Log.d("", "Background Service" + dataSnapshot.key)

        }

        override fun onChildRemoved(p0: DataSnapshot) {
            Log.d("", "Child Removed")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val surfaceView = findViewById<SurfaceView>(R.id.surfaceView)
        button_start_stop = findViewById<Button>(R.id.button_play)
        button_start_stop!!.setOnClickListener(this)
        button_camera = findViewById(R.id.camera_on_off)
        button_camera!!.setOnClickListener(this)
        rtmpCamera2 = RtmpCamera2(surfaceView, this)
        surfaceView.holder.addCallback(this)

        mDatabaseRef = FirebaseDatabase.getInstance().reference.child("sherlock")
        mDatabaseRef!!.addChildEventListener(dbChangeListener)

        /*gpsTaskBuilder = GpsTaskBuilder(
            context = this,
            port = "UART0",
            baud_rate = 9600,
            accuracy = 2.5f,
            _TAG = "GPS",
            _dbReference = mDatabaseRef!!
        )
        gpsTaskBuilder!!.registerGPSDriver()*/


    }

    override fun onDestroy() {
        super.onDestroy()
        stopStreamAction()
        if (rtmpCamera2!!.isOnPreview) {
            rtmpCamera2!!.stopPreview()
        }

    }

    override fun onConnectionSuccessRtmp() {
        runOnUiThread {
            Toast.makeText(this, "Connection success", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onConnectionFailedRtmp(reason: String) {
        runOnUiThread {
            Toast.makeText(
                this, "Connection failed. $reason",
                Toast.LENGTH_SHORT
            ).show()
            stopStreamAction()
            button_start_stop!!.text = "Start"
        }
    }

    override fun onDisconnectRtmp() {
        runOnUiThread { Toast.makeText(this@MainActivity, "Disconnected", Toast.LENGTH_SHORT).show() }
    }

    override fun onAuthErrorRtmp() {
        runOnUiThread { Toast.makeText(this@MainActivity, "Auth error", Toast.LENGTH_SHORT).show() }
    }

    override fun onAuthSuccessRtmp() {
        runOnUiThread { Toast.makeText(this@MainActivity, "Auth success", Toast.LENGTH_SHORT).show() }
    }


    override fun surfaceCreated(surfaceHolder: SurfaceHolder) {

    }

    override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {
        rtmpCamera2!!.startPreview()
    }

    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
        if (rtmpCamera2!!.isStreaming) {
            rtmpCamera2!!.stopStream()
            button_start_stop!!.text = "start"
        }
        rtmpCamera2!!.stopPreview()
    }

    override fun onClick(view: View) {
        startStreamAction()
    }

    private fun startStreamAction() {
        if (!rtmpCamera2!!.isStreaming) {
            rtmpCamera2!!.disableAudio()
            if (rtmpCamera2!!.prepareVideo(640, 480, 30, 1200 * 1024, false, 4, 0)
                && rtmpCamera2!!.prepareAudio()
            ) {
                //rtmpCamera2!!.disableAudio()

                //button_start_stop!!.text = "stop"
                rtmpCamera2!!.startStream(etUrl)
            } else {
                Log.v("err", "streaming opening error")
            }
        }
    }

    private fun stopStreamAction() {

        if (rtmpCamera2!!.isStreaming) {
            rtmpCamera2!!.stopStream()
        }

    }


}
