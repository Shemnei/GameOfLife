package gol

import gol.gui.GameGrid
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.stage.Stage

class GameOfLife : Application() {
    override fun start(primaryStage: Stage) {
        val root = VBox()

        val board = GameGrid(100, 100)
        VBox.setVgrow(board, Priority.ALWAYS)
        val doTick = Button("Do Tick")

        doTick.setOnAction {
            board.doTick()
        }

        root.children += doTick
        root.children += board
        primaryStage.scene = Scene(root)
        primaryStage.show()
    }
}

fun main(args: Array<String>) {
    Application.launch(GameOfLife::class.java)
}