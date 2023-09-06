package com.student.mappic.addmap.common.myviews.opengl

import android.graphics.PointF
import android.util.Log
import kotlin.math.cos
import kotlin.math.sin

/**
 * Creates circels.
 * @param radius
 * @param transl - translation. Move circle by transl.x and transl.y
 */
class CircleMaker(
    private val radius: Float = 0f,
    private val transl: PointF
) {
    private var verticCount: Int = 0
    private var step: Float = 0f

    /**
     * Creates meshed circles.
     * Mesh of circle is pizza-like.
     * @param verticCount - number of vertices on the circle. More vertices, means more circular circle.
     * @return array of triangles (polygons) to be drawn to draw circle.
     */
    fun circleCoords(verticCount: Int): Array<Triangle> {
        this.verticCount = verticCount
        step = (2 * Math.PI).toFloat() / (verticCount * 1f)
        return Array(verticCount) { polygonMaker(it) }
    }
    private fun polygonMaker(i: Int): Triangle {
        Log.d("CircleMaker", ">>> ${i} triangle")
        val verticList = ArrayList<Float>(9)

        val point1 = pointOnCircle(i)
        val point2 = pointOnCircle((i+1) % verticCount) // when i = (size - 1) this must be 0, thus modulo operator is used
        // third point is 'transl'

        // 3 coordinates in vertex, 3 vertices
        verticList.add(point1.x)
        verticList.add(point1.y)
        verticList.add(0f)
        verticList.add(point2.x)
        verticList.add(point2.y)
        verticList.add(0f)
        verticList.add(transl.x)
        verticList.add(transl.y)
        verticList.add(0f)

        return Triangle().customVertices(verticList.toFloatArray())
    }

    /**
     * Outputs coordinates of a point on a circle
     */
    private fun pointOnCircle(i: Int): PointF {
        return PointF(
            cos(i * step) * radius + transl.x, // x
            sin(i * step) * radius + transl.y  // y
        )
    }
    companion object {}
}