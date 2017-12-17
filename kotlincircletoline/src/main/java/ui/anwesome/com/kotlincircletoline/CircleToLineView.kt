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
}