package com.student.mappic.addmap.common.myviews

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.student.mappic.clist

class MyView(context: Context): View(context) {

    /**
     * Interface for passing MotionEvent to Activity/Fragment.
     *
     * Remember to set it:
     *     myView.passMotEv = { /* some code here */ }
     * And unset it later:
     *     myView.passMotEv = null
     */
    var passMotEv: PassMotionEvent? = null // public

    init {
        Log.d(clist.MyView, ">>> default init, 1 arg")
    }
    constructor(context: Context, attrs: AttributeSet?): this(context) {
        Log.d(clist.MyView, ">>> init, 2 args")
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): this(context) {
        Log.d(clist.MyView, ">>> init, 3 args")
    }
    /*
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int): this(context) {
        Log.d(clist.MyView, ">>> init, 4 args (never used)")
    }
    */
    fun setPassMotionEvent(passMotEv: PassMotionEvent) {
        this.passMotEv = passMotEv
        Log.d(clist.MyView, ">>> passMotionEvent is set")
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // here my code
        if (event != null) {
            Log.d(clist.MyView, ">>> W myView wykryto klik w " + "x: " + event.x + "; y: " + event.y)
            if(passMotEv != null) {
                passMotionEvent(event)
            }
        }
        else {
            Log.e(clist.MyView, ">>> Eeee, w myView MotionEvent jest null")
        }
        return super.performClick()
    }
    private fun passMotionEvent(event: MotionEvent) {
        if(passMotEv != null) {
            passMotEv!!.receiveMotionEvent(event)
            Log.d(clist.MyView, ">>> Wysłano MotionEvent")
        }
    }
}