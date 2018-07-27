package main

import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.coroutines.experimental.suspendCoroutine

class JavascriptInterface(val controller: Controller) {

    fun print(msg: String) {
        println(msg)
    }

    fun alert(msg: String) {
        Alert(Alert.AlertType.NONE, msg, ButtonType.OK).show()
    }

    fun sleep(time: Long) {
        Thread.sleep(time)
    }

    fun execAdb(cmd: String): String {
        val list = cmd.split(',').toMutableList()
        list.add(0, "adb")
        return exec(list)
    }

    fun execAdbShell(cmd: String): String {
        val list = cmd.split(',').toMutableList()
        list.add(0, "shell")
        list.add(0, "adb")
        return exec(list)
    }

    private fun exec(vararg cmd: String): String {
        return exec(cmd.toList())
    }

    private fun exec(cmd: List<String>): String {
        val p = ProcessBuilder(cmd.toList()).apply {
            redirectErrorStream(true)
        }.start()
        val s = BufferedReader(InputStreamReader(p.inputStream))
        val res = s.readText()
        s.close()
        return res
    }

    fun tap(x: Int, y: Int) {
        exec("adb", "shell", "input touchscreen tap $x $y")
    }

    fun swipe(x1: Int, y1: Int, x2: Int, y2: Int) {
        exec("adb", "shell", "input touchscreen swipe $x1 $y1 $x2 $y2")
    }

    fun pressKey(key : String){
        execAdbShell("input keyevent $key")
    }

    fun inputText(text : String){
        execAdbShell("input text $text")
    }

    fun screenshot() {
        val img = Mat(controller.listener.height, controller.listener.width, CvType.CV_8UC3, controller.listener.imageBuf)
        Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2BGRA)
        Imgcodecs.imwrite("out.png", img)
    }

    fun startActivity(packageName : String){
        val dump = execAdbShell("pm dump $packageName")
        val activity = Regex("android\\.intent\\.action\\.MAIN:[\\s\\S]*?$packageName\\/(.*?)[\\s]").find(dump)?.groupValues?.get(1)
        execAdbShell("am start -a android.intent.action.MAIN -n $packageName/$activity")
    }
}