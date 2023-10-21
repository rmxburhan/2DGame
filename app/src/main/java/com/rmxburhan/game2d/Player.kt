package com.rmxburhan.game2d

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import androidx.core.content.ContextCompat
import kotlin.math.pow
import kotlin.math.sqrt

class Player(var positionX : Double, var positionY : Double, val radius : Double) {
     var initialY: Double
     var initialX: Double
    private lateinit var paint : Paint
    private var isMoveable : Boolean = false

    fun isTouched(x : Double, y : Double) : Boolean {
        val distance = sqrt((x - positionX).pow(2) + (y - positionY).pow(2))
        return distance <= radius
    }

    fun getMoveable() : Boolean {
        return isMoveable
    }
    fun setMoveable(input : Boolean) {
        isMoveable =  input
    }

    init {
        paint = Paint()
        paint.color = Color.RED
        initialX = positionX
        initialY = positionY
    }

    fun draw(canvas: Canvas) {
        canvas.drawCircle(positionX.toFloat(), positionY.toFloat(), radius.toFloat(), paint)
    }

    fun reset() {
        positionX = initialX
        positionY = initialY
    }

    fun update() {
    }

    fun setPosition(x: Double, y: Double) {
        if (isMoveable) {
            positionX = x
            positionY = y
        }
    }

}
