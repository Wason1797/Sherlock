package sherlockbot.clubespe.espe.edu.ec.sherlockbot.controller

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import javax.inject.Inject

class BoardController @Inject constructor():LifecycleObserver{
    private lateinit var motor: MotorController

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate(){
        motor = MotorController()
    }
    fun setDirectionMotor(direction:String){
        if(direction=="still") motor.stop()
        else if(direction=="forward") motor.forward()
        else if(direction=="backward") motor.backward()
        else if(direction=="left") motor.turnLeft()
        else if(direction=="right") motor.turnRight()
    }
}