package main

import com.jogamp.opengl.GL2
import com.jogamp.opengl.GLAutoDrawable
import com.jogamp.opengl.GLEventListener
import com.jogamp.opengl.util.FPSAnimator
import java.nio.ByteBuffer

class Listener : GLEventListener {

    lateinit var gl2: GL2
    var textureId = 0

    override fun init(drawable: GLAutoDrawable) {
        gl2 = drawable.gl.gL2
        gl2.glEnable(GL2.GL_TEXTURE)
        gl2.glClearColor(1f, 0f, 0f, 1f)
        textureId = gl2.UGenTexture(540, 960)
        FPSAnimator(drawable,60).start()
    }

    fun setTexture(buf: ByteBuffer) {
        gl2.glTexSubImage2D(GL2.GL_TEXTURE_2D, 0, 0, 0, 540, 960, GL2.GL_RGB, GL2.GL_UNSIGNED_BYTE, buf)
    }

    override fun reshape(drawable: GLAutoDrawable, x: Int, y: Int, width: Int, height: Int) {

    }

    override fun display(drawable: GLAutoDrawable) {
        gl2.glClear(GL2.GL_COLOR_BUFFER_BIT)
        gl2.glBindTexture(GL2.GL_TEXTURE_2D,textureId)
        gl2.glBegin(GL2.GL_QUADS)

        gl2.glTexCoord2d(0.0,0.0)
        gl2.glVertex2d(-1.0,-1.0)

        gl2.glTexCoord2d(0.0,1.0)
        gl2.glVertex2d(-1.0,1.0)

        gl2.glTexCoord2d(1.0,1.0)
        gl2.glVertex2d(1.0,1.0)

        gl2.glTexCoord2d(1.0,0.0)
        gl2.glVertex2d(1.0,-1.0)

        gl2.glEnd()

    }

    override fun dispose(drawable: GLAutoDrawable) {

    }
}