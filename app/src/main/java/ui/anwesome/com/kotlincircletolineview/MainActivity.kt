package ui.anwesome.com.kotlincircletolineview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import ui.anwesome.com.kotlincircletoline.CircleToLineView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var view = CircleToLineView.create(this)
        view.addCircleToLineListener({
            Toast.makeText(this,"converted to line",Toast.LENGTH_SHORT).show()
        },{
            Toast.makeText(this,"converted back to circle",Toast.LENGTH_SHORT).show()
        })
        fullScreen()
    }
}
fun AppCompatActivity.fullScreen() {
    supportActionBar?.hide()
    window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
}
