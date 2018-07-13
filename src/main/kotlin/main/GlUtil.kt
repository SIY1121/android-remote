package main

import com.jogamp.opengl.GL2
import java.nio.IntBuffer

fun GL2.UGenTexture(width: Int, height: Int): Int {
    val b = IntBuffer.allocate(1)
    this.glGenTextures(1, b)
    val id = b.get()
    this.glBindTexture(GL2.GL_TEXTURE_2D, id)
    this.glTexImage2D(GL2.GL_TEXTURE_2D, 0, GL2.GL_RGB, width, height, 0, GL2.GL_BGR, GL2.GL_UNSIGNED_BYTE, java.nio.ByteBuffer.allocate(width * height * 3))

    this.glTextureParameteriEXT(id, GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR)
    this.glTextureParameteriEXT(id, GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR)
    this.glTextureParameteriEXT(id, GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP_TO_EDGE)
    this.glTextureParameteriEXT(id, GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP_TO_EDGE)

    this.glBindTexture(GL2.GL_TEXTURE_2D, 0)

    return id
}