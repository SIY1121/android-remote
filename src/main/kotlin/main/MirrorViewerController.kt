package main

import com.jogamp.opengl.GL2
import com.jogamp.opengl.awt.GLJPanel
import javafx.embed.swing.SwingNode
import javafx.fxml.FXML
import javafx.fxml.Initializable
import kotlinx.coroutines.experimental.launch
import org.bytedeco.javacv.FFmpegFrameGrabber
import java.net.Socket
import java.net.URL
import java.nio.ByteBuffer
import java.util.*

class MirrorViewerController :Initializable {
    @FXML
    lateinit var swingNode: SwingNode

    lateinit var glJPanel: GLJPanel

    lateinit var listener: Listener

    lateinit var socket: Socket

    lateinit var grabber: FFmpegFrameGrabber

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        println("finding devices")
        exec("adb", "forward", "tcp:8080", "tcp:8080")
        socket = Socket("127.0.0.1", 8080)
        println("connected")

        grabber = FFmpegFrameGrabber(socket.getInputStream())
        grabber.frameRate = 60.0
        grabber.start()
        println("${grabber.imageWidth}x${grabber.imageHeight}")

        listener = Listener(grabber.imageWidth, grabber.imageHeight)
        glJPanel = GLJPanel()

        glJPanel.addGLEventListener(listener)
        swingNode.content = glJPanel


        launch {
            while (true) {
                try {
                    val img = grabber.grabImage()
                    //listener.setTexture(img.image[0] as ByteBuffer)
                    glJPanel.invoke(true) {
                        val gl2 = it.gl.gL2
                        val buf = img.image[0] as ByteBuffer
                        gl2.glBindTexture(GL2.GL_TEXTURE_2D, listener.textureId)
                        gl2.glTexSubImage2D(GL2.GL_TEXTURE_2D, 0, 0, 0, grabber.imageWidth, grabber.imageHeight, GL2.GL_BGR, GL2.GL_UNSIGNED_BYTE, buf)
                        gl2.glBindTexture(GL2.GL_TEXTURE_2D, 0)
                        listener.imageBuf = buf
                        false
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
    }

}