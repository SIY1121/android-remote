package main

import javafx.embed.swing.SwingFXUtils
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.image.PixelFormat
import javafx.scene.image.WritableImage
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.nio.ByteBuffer
import javax.imageio.ImageIO

class JavascriptInterface(val controller: Controller) {

    fun print(msg: String) {
        println(msg)
    }

    fun alert(msg: String) {
        Alert(Alert.AlertType.NONE, msg, ButtonType.OK).show()
    }

    fun sleep(time : Long){
        Thread.sleep(time)
    }

    fun exec(cmd: String): String {
        val process = Runtime.getRuntime().exec(cmd)
        process.waitFor()
        return BufferedReader(InputStreamReader(process.inputStream)).readText()
    }

    fun tap(x: Int, y: Int) {
        exec("adb shell input touchscreen tap $x $y")
    }

    fun swipe(x1: Int, y1: Int, x2: Int, y2: Int) {
        exec("adb shell input touchscreen swipe $x1 $y1 $x2 $y2")
    }

    fun screenshot() {
        val img = Mat(controller.listener.height, controller.listener.width, CvType.CV_8UC3, controller.listener.imageBuf)
        Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2BGRA)
        Imgcodecs.imwrite("out.png", img)
    }
}