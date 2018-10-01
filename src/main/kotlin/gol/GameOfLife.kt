package gol

import gol.gui.GameOfLifeGrid
import gol.model.GameBoard
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.stage.Stage

class GameOfLife : Application() {
    override fun start(primaryStage: Stage) {

        val board = GameBoard(20, 10)
        board.setCell(2, 3, true)
        board.setCell(3, 3, true)
        board.setCell(4, 3, true)

        val grid = GameOfLifeGrid(board)
        VBox.setVgrow(grid, Priority.ALWAYS)

        primaryStage.scene = Scene(grid)
        primaryStage.show()
    }
}

fun main(args: Array<String>) {
    Application.launch(GameOfLife::class.java)
}