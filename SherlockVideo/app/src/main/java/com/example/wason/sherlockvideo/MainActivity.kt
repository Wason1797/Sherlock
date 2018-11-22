package com.example.wason.sherlockvideo

import android.app.Activity
import android.hardware.camera2.CameraMetadata
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.pedro.encoder.input.video.CameraOpenException

import com.pedro.rtplibrary.rtmp.RtmpCamera2
import net.ossrs.rtmp.ConnectCheckerRtmp
import java.io.File

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
        View.OnClickListener, SurfaceHolder.Callback
{

    private var rtmpCamera2: RtmpCamera2? = null
    private var button_start_stop: Button? = null
    private var button_camera: Button? = null
    private var etUrl: String = "your rtp server url"

    private var currentDateAndTime = ""
    private val folder =
            File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/rtmp-rtsp-stream-client-java")

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val surfaceView = findViewById<SurfaceView>(R.id.surfaceView)
        button_start_stop = findViewById<Button>(R.id.button_play)
        button_start_stop!!.setOnClickListener(this)
        button_camera = findViewById(R.id.camera_on_off)
        button_camera!!.setOnClickListener(this)
        rtmpCamera2 = RtmpCamera2(surfaceView, this)
        surfaceView.holder.addCallback(this)
    }

    override fun onConnectionSuccessRtmp()
    {
        runOnUiThread {
            Toast.makeText(this, "Connection success", Toast.LENGTH_SHORT)
                    .show()
        }
    }

    override fun onConnectionFailedRtmp(reason: String)
    {
        runOnUiThread {
            Toast.makeText(
                    this, "Connection failed. $reason",
                    Toast.LENGTH_SHORT
            ).show()
            rtmpCamera2!!.stopStream()
            button_start_stop!!.text = "Start"
        }
    }

    override fun onDisconnectRtmp()
    {
        runOnUiThread { Toast.makeText(this@MainActivity, "Disconnected", Toast.LENGTH_SHORT).show() }
    }

    override fun onAuthErrorRtmp()
    {
        runOnUiThread { Toast.makeText(this@MainActivity, "Auth error", Toast.LENGTH_SHORT).show() }
    }

    override fun onAuthSuccessRtmp()
    {
        runOnUiThread { Toast.makeText(this@MainActivity, "Auth success", Toast.LENGTH_SHORT).show() }
    }


    override fun surfaceCreated(surfaceHolder: SurfaceHolder)
    {

    }

    override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int)
    {
        rtmpCamera2!!.startPreview()
    }

    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder)
    {
        if (rtmpCamera2!!.isStreaming())
        {
            rtmpCamera2!!.stopStream()
            button_start_stop!!.text = "start"
        }
        rtmpCamera2!!.stopPreview()
    }

    override fun onClick(view: View)
    {
        if (!rtmpCamera2!!.isStreaming()) {
            if (rtmpCamera2!!.prepareVideo()&& rtmpCamera2!!.prepareAudio()) {
                Toast.makeText(
                    this, "Video prepared",
                    Toast.LENGTH_SHORT
                ).show()
                button_start_stop!!.text = "stop"
                rtmpCamera2!!.startStream(etUrl)
            } else {
                Toast.makeText(
                    this, "Error preparing stream, This device cant do it",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun onBtnStreamClick()
    {

    }


}
