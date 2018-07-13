package main

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

class Controller : Initializable {
    @FXML
    lateinit var swingNode: SwingNode

    lateinit var glJPanel: GLJPanel

    lateinit var listener: Listener

    lateinit var socket : Socket

    lateinit var grabber : FFmpegFrameGrabber

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        glJPanel = GLJPanel()
        listener = Listener()

        glJPanel.addGLEventListener(listener)
        swingNode.content = glJPanel

        socket = Socket("127.0.0.1",8080)
        grabber = FFmpegFrameGrabber(socket.getInputStream())
        grabber.start()

        Thread {
            while (true){
                val img = grabber.grabImage()
                listener.setTexture(img.image[0] as ByteBuffer)
            }
        }.start()
    }
}