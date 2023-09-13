package com.student.mappic.opengl

import android.opengl.GLES20
import android.util.Log
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

/*
    By default, OpenGL ES assumes a coordinate system where [0,0,0] (X,Y,Z) specifies the
    center of the GLSurfaceView frame, [1,1,0] is the top right corner of the frame and
    [-1,-1,0] is bottom left corner of the frame.

    [ x (-), y (|), z (depth)] OpenGL coordinate system:

    x=-1                  x= 0                  x= 1
    [-1, 1, 0] -------- -------- -------- [ 1, 1, 0]  y= 1
     |                                            |
     |                 [ 0, 0, 0]                 |   y= 0
     |                                            |
    [-1,-1, 0] -------- -------- -------- [ 1,-1, 0]  y=-1
*/

class Triangle {

    private val fragmentShaderCode = ShaderCode.TriangleShaderCode.fragmentShaderCode
    private val vertexShaderCode = ShaderCode.TriangleShaderCode.vertexShaderCode
    private var mProgram: Int

    // default values
    var color = floatArrayOf(0.63671875f, 0.76953125f, 0.22265625f, 1.0f) // range [0f, 1f] for R,G,B,a

    // default values
    var triangleCoords = floatArrayOf(     // in counterclockwise order:
        0.0f, -0.622008459f, 0.0f,      // top
        -0.5f, 0.311004243f, 0.0f,    // bottom left
        0.5f, 0.311004243f, 0.0f      // bottom right
    )
    private var trianglePosition = ObjPosition(0f, 0f, 0f)

    // those methods return Triangle to create chains like this: Triangle().setVertices(...).setColor(...).setPosition(...)
    fun setVertices(coords: FloatArray): Triangle {
        createVertexBuffer(coords)
        return this
    }
    fun setColor(R: Float, G: Float, B: Float, a: Float): Triangle {
        color = floatArrayOf(R, G, B, a)
        return this
    }
    fun setPosition(triPos: ObjPosition): Triangle {
        trianglePosition = triPos
        return this
    }

    init {
        val vertexShader: Int = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader: Int = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram().also {

            // add the vertex shader to program
            GLES20.glAttachShader(it, vertexShader)

            // add the fragment shader to program
            GLES20.glAttachShader(it, fragmentShader)

            // creates OpenGL ES program executables
            GLES20.glLinkProgram(it)
        }
        createVertexBuffer(triangleCoords)
    }

    private lateinit var vertexBuffer: FloatBuffer
    private fun createVertexBuffer(coords: FloatArray) {
        vertexBuffer =
        // (number of coordinate values * 4 bytes per float)
        ByteBuffer.allocateDirect(coords.size * 4).run {
            // use the device hardware's native byte order
            order(ByteOrder.nativeOrder())

            // create a floating point buffer from the ByteBuffer
            asFloatBuffer().apply {
                // add the coordinates to the FloatBuffer
                put(coords)
                // set the buffer to read the first coordinate
                position(0)
            }
        }
    }

    /**
     * this compiles GLSL shader program
     */
    fun loadShader(type: Int, shaderCode: String): Int {

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        return GLES20.glCreateShader(type).also { shader ->

            // add the source code to the shader and compile it
            GLES20.glShaderSource(shader, shaderCode)
            GLES20.glCompileShader(shader)
        }
    }

    private var positionHandle: Int = 0
    private var mColorHandle: Int = 0
    private val vertexCount: Int = triangleCoords.size / COORDS_PER_VERTEX
    private val vertexStride: Int = COORDS_PER_VERTEX * 4 // 4 bytes per vertex
    private var vPMatrixHandle: Int = 0 // Use to access and set the view transformation

    /**
     * If you set trianglePosition earlier, then you can use draw() without specifying Matrix.
     * Can be used without setting trianglePos, then it uses default position (angle: 0deg, x: 0.0, y: 0.0)
     * Calls draw(FloatArray)
     * Draws Triangle.
     */
    fun draw() {
        //Log.d("draw", "draw alphacolor ${color.get(3)}")
        draw(MVPCreator().applyAllMatrices(trianglePosition))
    }

    /**
     * Actual drawing to the screen.
     * @param mvpMatrix matrix specifying position and view transformations.
     * Draws Triangle.
     */
    private fun draw(mvpMatrix: FloatArray) {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram)

        // get handle to vertex shader's vPosition member
        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition").also {

            // Enable a handle to the triangle vertices
            GLES20.glEnableVertexAttribArray(it)

            // Prepare the triangle coordinate data
            GLES20.glVertexAttribPointer(
                it,
                COORDS_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                vertexStride,
                vertexBuffer
            )

            // get handle to fragment shader's vColor member
            mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor").also { colorHandle ->

                // Set color for drawing the triangle
                GLES20.glUniform4fv(colorHandle, 1, color, 0)
            }

            // Hope placed it in right spot.
            // get handle to shape's transformation matrix
            vPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix")

            // Pass the projection and view transformation to the shader
            GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0)

            // Draw the triangle
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)

            // Disable vertex array
            GLES20.glDisableVertexAttribArray(positionHandle)
        }
    }
    companion object {
        // number of coordinates per vertex in this array
        const val COORDS_PER_VERTEX = 3
    }

}
