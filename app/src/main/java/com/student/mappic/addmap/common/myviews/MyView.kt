package com.student.mappic.addmap.common.myviews

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.student.mappic.clist

/**
 * This view is used to provide onClick data such as x, y positions.
 * You can put it over a view, from which you want to capture onClick position (x, y).
 * Used in [Step2Fragment] and [Step3Fragment].
 */
class MyView(context: Context, attrs: AttributeSet): View(context, attrs) {

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
        Log.d(clist.MyView, ">>> default init, 2 arg")
        Log.d(clist.MyView, ">>> MyView is initialized !")
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): this(context, attrs) {
        Log.d(clist.MyView, ">>> init, 3 args")
    }

    /**
     * Set what should be called after onTouch() is performed.
     * MotionEvent (that includes coordinates of click) is provided to called method.
     */
    fun setPassMotionEvent(passMotEv: PassMotionEvent) {
        this.passMotEv = passMotEv
        Log.d(clist.MyView, ">>> passMotionEvent is set")
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // here my code
        if (event != null) {
            //Log.d(clist.MyView, ">>> W myView wykryto klik w " + "x: " + event.x + "; y: " + event.y)
            if(passMotEv != null) {
                passMotionEvent(event)
            }
        }
        /*else {
            Log.e(clist.MyView, ">>> Eeee, w myView MotionEvent jest null")
        }*/
        return super.performClick()
    }
    private fun passMotionEvent(event: MotionEvent) {
        if(passMotEv != null) {
            passMotEv!!.receiveMotionEvent(event)
            //Log.d(clist.MyView, ">>> Wysłano MotionEvent")
        }
    }
}