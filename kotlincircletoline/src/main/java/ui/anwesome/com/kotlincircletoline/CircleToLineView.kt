package ui.anwesome.com.kotlincircletoline

/**
 * Created by anweshmishra on 17/12/17.
 */
import android.content.*
import android.graphics.*
import android.view.*

class CircleToLineView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

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
}