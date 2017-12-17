package ui.anwesome.com.kotlincircletolineview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ui.anwesome.com.kotlincircletoline.CircleToLineView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CircleToLineView.create(this)
    }
}
