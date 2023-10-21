package com.rmxburhan.game2d

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.media.metrics.Event
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintSet.Motion
import androidx.core.content.ContextCompat
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

class Game(context: Context, attrs : AttributeSet) : SurfaceView(context, attrs), SurfaceHolder.Callback{
    private lateinit var myCanvas: Canvas
    private var rectF2: RectF
    private var rectF: RectF
    private var joystick: Joystick
    private var player: Player
    private lateinit var gameLoop: GameLoop
    private var isRunning : Boolean = false
    private var obstacles : ArrayList<RectF>  = arrayListOf()
    private val paintObject : Paint = Paint()
    private lateinit var  rectGoal : RectF
    var onFinished : (() -> Unit)? = null

    fun play() {
        isRunning = true
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isRunning == false)
        {
            return onEditingTouch(event)
        } else {
            return onPlayingTouch(event)
        }

        return super.onTouchEvent(event)
    }


    fun onPlayingTouch(event : MotionEvent)  : Boolean{
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                if (player.isTouched(event.x.toDouble(), event.y.toDouble())) {
                    player.setMoveable(true)
                    player.setPosition(event.getX().toDouble(), event.getY().toDouble())
                }
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                if (player.getMoveable()) {
                    player.setPosition(event.getX().toDouble(), event.getY().toDouble())
                    if (checkIntersectWithObstacles(obstacles, player)) {
                        Log.d("pesan", "Kena coy")
                        reset()
                        player.setMoveable(false)
                    }
                    return true
                }
                else {
                    return false
                }
            }
            MotionEvent.ACTION_UP -> {
                player.setMoveable(false)
                return  false
            }
            else -> {
                return  false
            }
        }
    }

    fun isIntersect(player : Player, rectF: RectF) : Boolean {
        val closestX = max(rectF.left, min(player.positionX.toFloat(), rectF.right))
        val closestY = max(rectF.top, min(player.positionY.toFloat(), rectF.bottom))

        val jarak = sqrt((player.positionX.toFloat() - closestX).pow(2) + (player.positionY.toFloat() - closestY).pow(2))

    return jarak <= player.radius
    }

    fun reset() {
        player.reset()
    }

    fun onEditingTouch(event : MotionEvent) :Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                val rect = RectF(event.getX() - 25, event.getY() - 25, event.getX() + 25, event.getY() + 25)
                obstacles.add(rect)
                return true
            }
            else -> {
                return  false
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        player.initialX = (w /2).toDouble()
        player.initialY = (h - 50).toDouble()
        player.positionY = player.initialY
        player.positionX = player.initialX
    }

    init {
        val surfaceHolder = getHolder()
        surfaceHolder.addCallback(this)
        gameLoop = GameLoop(this, surfaceHolder)
        joystick = Joystick(100, 700, 30, 70)
        player = Player((width / 2).toDouble(), (height - 150).toDouble(),30.0)
        rectF = RectF(200f,200f,300f, 300f)
        rectF2 = RectF(200f,200f,300f, 300f)
        setFocusable(true)
        paintObject.color = Color.GREEN
        paintObject.style = Paint.Style.FILL
    }


    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        gameLoop.startLoop()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        myCanvas = canvas
        drawUPS(canvas)
        drawFPS(canvas)
        player.draw(canvas)
//        joystick.draw(canvas)
        val paintD = Paint()
        paintD.color = Color.GREEN
        val paintb= Paint()
        paintb.color = Color.GREEN
        canvas.drawRect(rectF, paintD )
        canvas.drawRect(rectF2, paintb)
        drawAllObstacles(obstacles, canvas)
    }
    fun checkIntersectWithObstacles(obstacles : List<RectF>, player: Player)  : Boolean{
        for(data in obstacles) {
            if (isIntersect(player, data)) {
                return true
            }
        }
        return false
    }

    fun drawAllObstacles(obstacles : List<RectF>, canvas: Canvas) {
        for (data in obstacles) {
            canvas.drawRect(data, paintObject)
        }
    }

    fun drawUPS(canvas: Canvas) {
        val averageUPS = gameLoop.getAverageUPS().toString()
        val paint = Paint()
        val color = ContextCompat.getColor(context, R.color.white)
        paint.color = color
        paint.textSize = 50f
        canvas.drawText("UPS : " + averageUPS, 100f, 60f, paint)
    }

    fun drawFPS(canvas: Canvas) {
        val averageFPS = gameLoop.getAverageFPS().toString()
        val paint = Paint()
        val color = ContextCompat.getColor(context, R.color.white)
        paint.color = color
        paint.textSize = 50f
        canvas.drawText("FPS : " + averageFPS, 100f, 115f, paint)
    }

    fun update() {
        joystick.update()
        player.update()
    }
}
