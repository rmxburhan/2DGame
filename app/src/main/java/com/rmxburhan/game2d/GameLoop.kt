package com.rmxburhan.game2d

import android.graphics.Canvas
import android.graphics.RectF
import android.util.Log
import android.view.SurfaceHolder

class GameLoop(val game:  Game, val surfaceHolder: SurfaceHolder?) : Thread() {
    private val MAX_UPS: Double = 120.0
    private val UPS_PERIOD: Double = 1E+3 / MAX_UPS
    private var averageFPS:  Double = 0.0
    private var averageUPS: Double = 0.0
    private var isRunning = false
    fun getAverageUPS(): Double {
        return averageUPS
    }

    fun getAverageFPS(): Double   {
        return averageFPS
    }

    fun startLoop() {
        isRunning = true
        start()
    }

    override fun run() {
        super.run()
        var canvas: Canvas? = null
        var updateCount = 0
        var frameCount = 0


        var startTime : Long = System.currentTimeMillis()
        var elapsedTime : Long = 0
        var sleepTime : Long = 0

        while (isRunning) {
            try {
                canvas = surfaceHolder?.lockCanvas()

                synchronized (surfaceHolder as Any) {
                    game.update()
                    updateCount++
                    canvas?.let { game.draw(it) }
                }

            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            finally {
                try {
                    frameCount++
                    surfaceHolder?.unlockCanvasAndPost(canvas)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }



            elapsedTime?.let {
                sleepTime = (updateCount * UPS_PERIOD - it).toLong()
            }
                if (sleepTime > 0) {
                    sleep(sleepTime) }


            while (sleepTime <= 0) {
                game.update()
                updateCount++
                elapsedTime = System.currentTimeMillis() - startTime
                sleepTime =  (updateCount * UPS_PERIOD - elapsedTime).toLong()
            }


            elapsedTime = System.currentTimeMillis() - startTime!!
            if (elapsedTime >= 1000) {
                averageUPS = updateCount / (1E-3 * elapsedTime)
                averageFPS = frameCount / (1E-3 * elapsedTime)
                updateCount= 0
                frameCount = 0
                startTime = System.currentTimeMillis()
            }
        }
    }

}
