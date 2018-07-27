package main

import javafx.application.Application
import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.stage.Stage
import javafx.stage.StageStyle
import kotlinx.coroutines.experimental.launch
import org.bytedeco.javacpp.Loader
import org.bytedeco.javacv.FFmpegFrameGrabber

class Main : Application() {
    override fun start(primaryStage: Stage) {
        val splash = Stage()
        splash.initStyle(StageStyle.UNDECORATED)
        splash.scene = Scene(FXMLLoader.load<Parent>(ClassLoader.getSystemResource("layout/splash.fxml")))
        val messageLabel = splash.scene.lookup("#messageLabel") as Label
        splash.show()

        launch {
            Platform.runLater { messageLabel.text = "Loading OpenCV Module(1/2)" }
            Loader.load(org.bytedeco.javacpp.opencv_java::class.java)
            Platform.runLater { messageLabel.text = "Loading OpenCV Module(2/2)" }
            Loader.load(org.bytedeco.javacpp.opencv_core::class.java)
            Platform.runLater { messageLabel.text = "Loading FFmpeg Library" }
            FFmpegFrameGrabber.tryLoad()
            Platform.runLater { messageLabel.text = "Starting ADB Server" }
            exec("adb","start-server")

            Platform.runLater {
                primaryStage.scene = Scene(FXMLLoader.load<Parent>(ClassLoader.getSystemResource("layout/menu.fxml")))
                primaryStage.setOnCloseRequest { Platform.exit() }

                splash.close()

                primaryStage.show()
            }

        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(Main::class.java, *args)
        }
    }
}