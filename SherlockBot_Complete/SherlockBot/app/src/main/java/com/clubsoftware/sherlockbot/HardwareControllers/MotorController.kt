package com.clubsoftware.sherlockbot.HardwareControllers

import com.google.android.things.pio.Gpio
import com.google.android.things.pio.PeripheralManager

class MotorController {
    //Motor 1
    private var pin1Motor1: Gpio
    private var pin2Motor1: Gpio
    //Motor 2
    private var pin1Motor2: Gpio
    private var pin2Motor2: Gpio

    init {
        pin1Motor1 = PeripheralManager.getInstance().openGpio("BCM12")
        pin2Motor1 = PeripheralManager.getInstance().openGpio("BCM16")
        pin1Motor2 = PeripheralManager.getInstance().openGpio("BCM20")
        pin2Motor2 = PeripheralManager.getInstance().openGpio("BCM21")
        pin1Motor1.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
        pin2Motor1.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
        pin1Motor2.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
        pin2Motor2.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
    }


    fun forward() {
        setPinValues(true, false, true, false)
    }

    fun backward() {
        setPinValues(false, true, false, true)
    }

    fun stop() {
        setPinValues(false, false, false, false)
    }

    fun turnLeft() {
        setPinValues(false, false, true, false)
    }

    fun turnRight() {
        setPinValues(true, false, false, false)
    }

    private fun setPinValues(p11: Boolean, p12: Boolean, p21: Boolean, p22: Boolean) {


        pin1Motor1.value = p11
        pin2Motor1.value = p12
        pin1Motor2.value = p21
        pin2Motor2.value = p22

    }
}