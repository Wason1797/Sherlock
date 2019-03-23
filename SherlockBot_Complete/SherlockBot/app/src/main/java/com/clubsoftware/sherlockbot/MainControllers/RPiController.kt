package com.clubsoftware.sherlockbot.MainControllers


import android.content.Context
import android.location.LocationListener
import com.clubsoftware.sherlockbot.HardwareControllers.GPSController
import com.clubsoftware.sherlockbot.HardwareControllers.MotorController

class RPiController {
    companion object {

        private lateinit var gps:GPSController
        private var motor:MotorController?=null


        fun setDirectionMotor(direction:String){
            if (motor == null) motor = MotorController()
            when (direction) {
                "still" -> motor!!.stop()
                "forward" -> motor!!.forward()
                "backward" -> motor!!.backward()
                "left" -> motor!!.turnLeft()
                "right" -> motor!!.turnRight()
            }
        }

        fun startGPS(context:Context, locationListener: LocationListener){
            gps= GPSController(
                context = context,
                port = "UART0",
                baud_rate = 9600,
                accuracy = 2.5f,
                _TAG = "GPS"
            )
            gps.registerGPSDriver(locationListener)
        }

    }
}