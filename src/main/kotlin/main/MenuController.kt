package main

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.ListView
import javafx.stage.Stage
import java.net.URL
import java.util.*

class MenuController : Initializable {
    @FXML
    lateinit var deviceList: ListView<String>

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        val devices = exec("adb", "devices").split("\n")
        for (i in 1 until devices.size - 2)
            deviceList.items.add(devices[i])

    }

    fun onScriptingStart(actionEvent: ActionEvent) {
        Stage().apply {
            scene = Scene(FXMLLoader.load<Parent>(ClassLoader.getSystemResource("layout/editor.fxml")))
        }.show()
    }

    fun onMirrorModeStart(actionEvent: ActionEvent) {
        Stage().apply {
            scene = Scene(FXMLLoader.load<Parent>(ClassLoader.getSystemResource("layout/mirrorViewer.fxml")))
        }.show()
    }
}