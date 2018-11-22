package com.example.wason.sherlockgps

import android.app.Activity
import android.os.Bundle


private val TAG = MainActivity::class.java.simpleName

class MainActivity : Activity()
{

    private var gpsTaskBuilder: GpsTaskBuilder? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        gpsTaskBuilder = GpsTaskBuilder(
            context = this,
            port = "UART0",
            baud_rate = 9600,
            accuracy = 2.5f,
            _TAG = TAG
        )
        gpsTaskBuilder!!.registerGPSDriver()

    }

    override fun onDestroy()
    {
        super.onDestroy()

        gpsTaskBuilder!!.closeDriver()
    }

}
