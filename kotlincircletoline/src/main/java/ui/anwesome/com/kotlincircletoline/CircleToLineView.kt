package ui.anwesome.com.kotlincircletoline

/**
 * Created by anweshmishra on 17/12/17.
 */
import android.content.*
import android.graphics.*
import android.view.*

class CircleToLineView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = CircleToLineRenderer(this)
    override fun onDraw(canvas:Canvas) {
        renderer.render(canvas,paint)
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
            paint.strokeWidth = r/15
            paint.strokeCap = Paint.Cap.ROUND
            canvas.drawArc(RectF(x-r,y-r,x+r,y+r),360*scale,360*(1-scale),false,paint)
            canvas.drawLine(x+r,y,x+r+(2*Math.PI*r).toFloat(),y,paint)
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
        fun startUpdating(stopcb:()->Unit) {
            dir = 1f-2*scale
        }
    }
    data class CircleToLineContainer(var w:Float,var h:Float) {
        var circleToLine = CircleToLine(w/2,h/2,Math.min(w,h)/3)
        val state = CircleToLineState()
        fun draw(canvas:Canvas,paint:Paint) {
            circleToLine.draw(canvas,paint,state.scale)
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
                container.update{
                    animated = false
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
}