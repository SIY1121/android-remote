package main

import javafx.application.Application
import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import org.bytedeco.javacpp.Loader

class Main : Application() {
    override fun start(primaryStage: Stage) {
        primaryStage.scene = Scene(FXMLLoader.load<Parent>(ClassLoader.getSystemResource("layout/main.fxml")))
        primaryStage.setOnCloseRequest { Platform.exit() }
        primaryStage.show()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Loader.load(org.bytedeco.javacpp.opencv_java::class.java)
            Loader.load(org.bytedeco.javacpp.opencv_core::class.java)
            launch(Main::class.java, *args)
        }
    }
}