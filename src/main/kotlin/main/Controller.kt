package main

import com.jogamp.opengl.GL2
import com.jogamp.opengl.awt.GLJPanel
import javafx.application.Platform
import javafx.embed.swing.SwingNode
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.web.WebView
import kotlinx.coroutines.experimental.launch
import org.bytedeco.javacv.FFmpegFrameGrabber
import java.net.Socket
import java.net.URL
import java.nio.ByteBuffer
import java.util.*
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager

class Controller : Initializable {
    @FXML
    lateinit var swingNode: SwingNode

    lateinit var glJPanel: GLJPanel

    lateinit var listener: Listener

    lateinit var socket: Socket

    lateinit var grabber: FFmpegFrameGrabber

    @FXML
    lateinit var editor: WebView

    lateinit var engine: ScriptEngine

    override fun initialize(location: URL?, resources: ResourceBundle?) {

        System.setProperty("nashorn.args", "--language=es6")
        engine = ScriptEngineManager().getEngineByName("Nashorn")

        editor.engine.load(ClassLoader.getSystemResource("editor/editor.html").toString())



        println("finding devices")
        exec("adb", "forward", "tcp:8080", "tcp:8080")
        socket = Socket("127.0.0.1", 8080)
        println("connected")

        grabber = FFmpegFrameGrabber(socket.getInputStream())
        grabber.start()
        println("${grabber.imageWidth}x${grabber.imageHeight}")

        listener = Listener(grabber.imageWidth, grabber.imageHeight)
        glJPanel = GLJPanel()

        glJPanel.addGLEventListener(listener)
        swingNode.content = glJPanel


        engine.put("app", JavascriptInterface(this))

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
        val script = editor.engine.executeScript("editor.getValue()").toString()

        launch {
            try {
                engine.eval(script)
            } catch (ex: Exception) {
                Platform.runLater {
                    Alert(Alert.AlertType.ERROR, ex.message, ButtonType.CLOSE).show()
                }
            }
        }
    }

    fun onTest(actionEvent: ActionEvent) {

    }
}