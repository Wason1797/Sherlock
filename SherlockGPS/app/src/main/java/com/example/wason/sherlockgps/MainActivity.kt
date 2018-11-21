package com.example.wason.sherlockgps

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.location.*
import com.google.android.things.contrib.driver.gps.NmeaGpsDriver
import java.io.IOException
import android.location.LocationListener
import android.location.LocationManager
import java.util.concurrent.TimeUnit
import com.google.android.things.contrib.driver.gps.NmeaGpsModule;
import com.google.android.things.contrib.driver.gps.GpsModuleCallback
import java.text.ParseException


private val TAG = MainActivity::class.java.simpleName

class MainActivity : Activity()
{

    var mGpsDriver: NmeaGpsDriver? = null

    var mGpsModule: NmeaGpsModule? = null
    var prevlocation: Location? = null
    private var mLocationManager: LocationManager? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        /*try
        {
            prevlocation = Location("gps")
            mGpsModule = NmeaGpsModule(
                "UART0",
                9600,
                2.5f
            )
            mGpsModule!!.setGpsModuleCallback(object : GpsModuleCallback()
            {
                override fun onGpsLocationUpdate(location: Location?)
                {
                    try
                    {
                        Log.v(TAG, "Location update: $location")

                    } catch (e: ParseException)
                    {
                        Log.v(TAG, e.message)
                    }

                }

                override fun onGpsSatelliteStatus(status: GnssStatus?){}

                override fun onGpsTimeUpdate(timestamp: Long){}

                override fun onNmeaMessage(nmeaMessage: String?){}

            })

        } catch (e: Exception)
        {
            // couldn't configure the gps module...
        }*/


        mLocationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        try
        {
            mGpsDriver = NmeaGpsDriver(
                this,
                "UART0",
                9600,
                2.5f
            )

            mGpsDriver!!.register()


            // Register for location updates*
            mLocationManager!!.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0f,
                mLocationListener
            )
        } catch (e: IOException)
        {
            Log.v(TAG, "Could not configure gps driver")
        }

    }

    override fun onDestroy()
    {
        super.onDestroy()
        //mGpsModule!!.close()
        mGpsDriver!!.unregister()
        try
        {
            mGpsDriver!!.close()
        } catch (e: IOException)
        {
            // error closing gps driver
        }


    }



    /** Report location updates  */
    private val mLocationListener = object : LocationListener
    {
        override fun onLocationChanged(location: Location)
        {
            try
            {
                Log.v(TAG, "Location update: $location")
                //TimeUnit.MILLISECONDS.sleep(500L)
            }
            catch (e : ParseException)
            {
               Log.v(TAG, e.message)
            }

        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle)
        {
            Log.v(TAG, "Status changed, provider: $provider , status: $status")
        }

        override fun onProviderEnabled(provider: String)
        {
            Log.v(TAG, "Provider enabled: $provider")
        }

        override fun onProviderDisabled(provider: String)
        {
            Log.v(TAG, "Provider disabled: $provider")
        }
    }

}
