package com.example.wason.sherlockbot

class BoardController  (){

    private var motor: MotorController?=null
    init {

        motor = MotorController()
    }
    fun setDirectionMotor(direction:String){
        when (direction) {
            "still" -> motor!!.stop()
            "forward" -> motor!!.forward()
            "backward" -> motor!!.backward()
            "left" -> motor!!.turnLeft()
            "right" -> motor!!.turnRight()
        }
    }
}