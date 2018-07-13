package main

import com.jogamp.opengl.GL2
import com.jogamp.opengl.awt.GLJPanel
import com.sun.javafx.webkit.WebConsoleListener
import javafx.application.Platform
import javafx.embed.swing.SwingNode
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.web.WebView
import kotlinx.coroutines.experimental.launch
import netscape.javascript.JSException
import netscape.javascript.JSObject
import org.bytedeco.javacv.FFmpegFrameGrabber
import org.bytedeco.javacv.FrameGrabber
import java.net.Socket
import java.net.URL
import java.nio.ByteBuffer
import java.util.*

class Controller : Initializable {
    @FXML
    lateinit var swingNode: SwingNode

    lateinit var glJPanel: GLJPanel

    lateinit var listener: Listener

    lateinit var socket: Socket

    lateinit var grabber: FFmpegFrameGrabber

    @FXML
    lateinit var editor: WebView

    override fun initialize(location: URL?, resources: ResourceBundle?) {

        editor.engine.load(ClassLoader.getSystemResource("editor/editor.html").toString())

        WebConsoleListener.setDefaultListener { webView, message, lineNumber, sourceId ->
            println("Console: [$sourceId:$lineNumber] $message")
        }

        socket = Socket("127.0.0.1", 8080)
        println("connected")

        grabber = FFmpegFrameGrabber(socket.getInputStream())
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

    fun onRun(actionEvent: ActionEvent) {

        try {
            val win = editor.engine.executeScript("window") as JSObject
            win.setMember("app", JavascriptInterface(this@Controller))
            editor.engine.executeScript("eval(editor.getValue())")
        } catch (ex: JSException) {
            Platform.runLater {
                Alert(Alert.AlertType.ERROR, ex.message, ButtonType.OK).show()
            }
        }


    }
}