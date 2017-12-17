package ui.anwesome.com.kotlincircletoline

/**
 * Created by anweshmishra on 17/12/17.
 */
import android.app.Activity
import android.content.*
import android.graphics.*
import android.view.*

class CircleToLineView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = CircleToLineRenderer(this)
    var circleToLineListener:CircleToLineListener?=null
    override fun onDraw(canvas:Canvas) {
        renderer.render(canvas,paint)
    }
    fun addCircleToLineListener(onLine:()->Unit,onCircle:()->Unit) {
        circleToLineListener = CircleToLineListener(onLine,onCircle)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }
    data class CircleToLine(var x:Float,var y:Float,var r:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = r/7
            paint.color = Color.parseColor("#1A237E")
            paint.strokeCap = Paint.Cap.ROUND
            canvas.drawArc(RectF(x-r,y-r,x+r,y+r),360*scale,360*(1-scale),false,paint)
            canvas.drawLine(x+r,y,x+r+(2*Math.PI*r).toFloat()*scale,y,paint)
        }
    }
    data class CircleToLineState(var scale:Float = 0f,var dir:Float = 0f,var prevScale:Float = 0f) {
        fun update(stopcb:(Float)->Unit) {
            scale += 0.1f*dir
            if(Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                stopcb(scale)
            }
        }
        fun startUpdating(startcb:()->Unit) {
            dir = 1f-2*scale
            startcb()
        }
    }
    data class CircleToLineContainer(var w:Float,var h:Float) {
        val r = Math.min(w,h)/10
        var circleToLine = CircleToLine(w/60+r,h/2,r)
        var lineIndicator = LineIndicator(w/60+2*r,4*h/5,(2*Math.PI*r).toFloat())
        val state = CircleToLineState()
        fun draw(canvas:Canvas,paint:Paint) {
            circleToLine.draw(canvas,paint,state.scale)
            lineIndicator.draw(canvas,paint,state.scale)
        }
        fun update(stopcb:(Float)->Unit) {
            state.update(stopcb)
        }
        fun startUpdating(startcb:()->Unit) {
            state.startUpdating(startcb)
        }
    }
    data class CircleToLineAnimator(var container:CircleToLineContainer,var view:CircleToLineView) {
        var animated = false
        fun update() {
            if(animated) {
                container.update{ scale ->
                    animated = false
                    when(scale) {
                        0f -> view.circleToLineListener?.onCircle?.invoke()
                        1f -> view.circleToLineListener?.onLine?.invoke()
                    }
                }
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            container.draw(canvas,paint)
        }
        fun startUpdating() {
            if(!animated) {
                container.startUpdating{
                    animated = true
                    view.postInvalidate()
                }
            }
        }
    }
    data class LineIndicator(var x:Float,var y:Float,var size:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            paint.strokeWidth = size/30
            paint.strokeCap = Paint.Cap.ROUND
            paint.color = Color.parseColor("#00E676")
            canvas.drawLine(x,y,x+size*scale,y,paint)
        }
    }
    data class CircleToLineRenderer(var view:CircleToLineView,var time:Int = 0) {
        var animator:CircleToLineAnimator?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = CircleToLineAnimator(CircleToLineContainer(w,h),view)
            }
            canvas.drawColor(Color.parseColor("#212121"))
            animator?.draw(canvas,paint)
            animator?.update()
            time++
        }
        fun handleTap() {
            animator?.startUpdating()
        }
    }
    data class CircleToLineListener(var onLine:()->Unit,var onCircle:()->Unit)
    companion object {
        fun create(activity:Activity):CircleToLineView {
            val view = CircleToLineView(activity)
            activity.setContentView(view)
            return view
        }
    }
}