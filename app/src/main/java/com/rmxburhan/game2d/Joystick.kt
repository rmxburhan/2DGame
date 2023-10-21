package com.rmxburhan.game2d

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class Joystick(val centerX : Int, val centerY : Int , val innerRadius : Int , val outerRadius : Int) {
    fun draw(canvas: Canvas) {
        canvas.drawCircle(outerCircleCenterX.toFloat(), outerCircleCenterY.toFloat(), outerRadius.toFloat(), paintOuter)
        canvas.drawCircle(innerCircleCenterX.toFloat(), innerCircleCenterY.toFloat(), innerRadius.toFloat(), paintInner)
    }

    fun update() {

    }

    private var paintOuter = Paint()
    private var paintInner = Paint()
    private var outerCircleCenterX: Int
    private var innerCircleCenterX: Int
    private var outerCircleCenterY: Int
    private var innerCircleCenterY: Int

    init {
        outerCircleCenterX = centerX.toInt()
        outerCircleCenterY = centerY.toInt()
        innerCircleCenterX = centerX.toInt()
        innerCircleCenterY = centerY.toInt()
        paintOuter.color = Color.GRAY
        paintInner.color = Color.BLUE

    }



}
