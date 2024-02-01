package pl.umk.mat.mappic.opengl

import android.graphics.PointF
import kotlin.math.cos
import kotlin.math.sin

/**
 * Creates circles.
 * @param radius - radius of circle to draw
 */
class CircleMaker(
    private val radius: Float = 0f
) {
    private var verticCount: Int = 0
    private var step: Float = 0f

    /**
     * Creates meshed circles.
     * Mesh of circle is pizza-like.
     * @param verticCount - number of vertices on the circle. More vertices, means more circular circle.
     * @return array of triangles (polygons) to be drawn to draw circle.
     */
    fun createCircleCoords(verticCount: Int): Array<Triangle> {
        this.verticCount = verticCount
        step = (2 * Math.PI).toFloat() / (verticCount * 1f)
        return Array(verticCount) { polygonMaker(it) }
    }

    /**
     * Creates triangles that will be part of Circle.
     */
    private fun polygonMaker(i: Int): Triangle {
        //Log.d("CircleMaker", ">>> ${i} triangle")

        val point1 = pointOnCircle(i)
        val point2 = pointOnCircle((i+1) % verticCount) // when i = (size - 1) this must be 0, thus modulo operator is used
        // third point is '0, 0'

        // 3 coordinates in vertex, 3 vertices
        val vertics = floatArrayOf(
            point1.x, point1.y, 0f,
            point2.x, point2.y, 0f,
            0f, 0f, 0f
        )

        return Triangle().setVertices(vertics)
    }

    /**
     * Outputs coordinates of a point on a circle
     */
    private fun pointOnCircle(i: Int): PointF {
        return PointF(
            cos(i * step) * radius + 0f, // x
            sin(i * step) * radius + 0f  // y
        )
    }
}