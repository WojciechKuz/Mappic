package com.student.mappic.addmap.common.myview

import android.view.MotionEvent

fun interface PassMotionEvent {
    /**
     * Implementation - handle receiving MotionEvent.
     * Calling - pass MotionEvent as result to some method.
     */
    fun receiveMotionEvent(motionEvent: MotionEvent)
}