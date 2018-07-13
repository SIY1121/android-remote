package main

import com.jogamp.opengl.GL2
import com.jogamp.opengl.GLAutoDrawable
import com.jogamp.opengl.GLEventListener
import com.jogamp.opengl.util.FPSAnimator
import com.jogamp.opengl.util.texture.Texture
import com.jogamp.opengl.util.texture.TextureIO
import java.io.File
import java.nio.ByteBuffer

class Listener(val width: Int, val height: Int) : GLEventListener {

    lateinit var gl2: GL2
    var textureId = 0

    var imageBuf: ByteBuffer? = null


    override fun init(drawable: GLAutoDrawable) {
        gl2 = drawable.gl.gL2
        gl2.glEnable(GL2.GL_TEXTURE_2D)
        gl2.glClearColor(1f, 0f, 0f, 1f)
        textureId = gl2.UGenTexture(width, height)
        FPSAnimator(drawable, 60).start()
    }

    override fun reshape(drawable: GLAutoDrawable, x: Int, y: Int, width: Int, height: Int) {

    }

    override fun display(drawable: GLAutoDrawable) {
        gl2.glClear(GL2.GL_COLOR_BUFFER_BIT)
        gl2.glBindTexture(GL2.GL_TEXTURE_2D, textureId)
        gl2.glBegin(GL2.GL_QUADS)

        gl2.glTexCoord2d(0.0, 1.0)
        gl2.glVertex2d(-1.0, -1.0)

        gl2.glTexCoord2d(0.0, 0.0)
        gl2.glVertex2d(-1.0, 1.0)

        gl2.glTexCoord2d(1.0, 0.0)
        gl2.glVertex2d(1.0, 1.0)

        gl2.glTexCoord2d(1.0, 1.0)
        gl2.glVertex2d(1.0, -1.0)

        gl2.glEnd()
        gl2.glBindTexture(GL2.GL_TEXTURE_2D, 0)
    }

    override fun dispose(drawable: GLAutoDrawable) {

    }
}