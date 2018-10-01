package gol.gui.skin

import gol.gui.GameOfLifeGrid
import javafx.css.PseudoClass
import javafx.geometry.HPos
import javafx.geometry.VPos
import javafx.scene.control.SkinBase
import javafx.scene.input.MouseEvent
import javafx.scene.layout.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class GameOfLifeGridSkin(val control: GameOfLifeGrid) : SkinBase<GameOfLifeGrid>(control) {

    private lateinit var grid: GridPane

    private lateinit var cells: Array<Pane>

    private lateinit var colConstraint: ColumnConstraints
    private lateinit var rowConstraint: RowConstraints

    init {
        println("Initing Cells")
        initNodes()
        println("Laying out Cells")
        layoutNodes()
        println("Done")

        println("Starting Simulation")
        val exe = Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate({
            readFromBoard()
            control.gameBoardProperty.get().doTick()
        }, 0, 1, TimeUnit.SECONDS)
    }

    private fun initNodes() {

        colConstraint = ColumnConstraints()
        colConstraint.isFillWidth = true
        colConstraint.hgrow = Priority.ALWAYS
        colConstraint.halignment = HPos.CENTER
        colConstraint.percentWidth = 100.0

        rowConstraint = RowConstraints()
        rowConstraint.isFillHeight = true
        rowConstraint.vgrow = Priority.ALWAYS
        rowConstraint.valignment = VPos.CENTER
        rowConstraint.percentHeight = 100.0

        grid = GridPane()

        cells = Array(control.gameBoardProperty.get().cellCount) { index ->
            val cell = Pane()
            cell.styleClass += "gol-cell"
            cell.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE)
            cell.setOnMouseClicked { handleCellClicked(it) }
            cell.userData = index to false
            cell
        }
    }

    private fun layoutNodes() {
        cells.forEachIndexed { index, cell ->
            val x = index % control.gameBoardProperty.get().width
            val y = index / control.gameBoardProperty.get().width
            grid.add(cell, x, y)
        }

        (0 until control.gameBoardProperty.get().width).forEach {
            grid.columnConstraints += colConstraint
        }

        (0 until control.gameBoardProperty.get().height).forEach {
            grid.rowConstraints += rowConstraint
        }

        children += grid
    }

    private fun handleCellClicked(event: MouseEvent) {
        val cell = event.source as Pane
        val cellData = (cell.userData as Pair<Int, Boolean>)
        val index = cellData.first
        val selected = cellData.second
        cell.pseudoClassStateChanged(PseudoClass.getPseudoClass("dead"), selected)
        cell.pseudoClassStateChanged(PseudoClass.getPseudoClass("alive"), !selected)
        cell.userData = index to !selected
    }

    private fun writeToBoard() {
        cells.forEach {
            val cellData = (it.userData as Pair<Int, Boolean>)
            val index = cellData.first
            val selected = cellData.second
            control.gameBoardProperty.get().setCell(index, selected)
        }
    }

    private fun readFromBoard() {
        cells.forEach {
            val cellData = (it.userData as Pair<Int, Boolean>)
            val index = cellData.first
            val selected = control.gameBoardProperty.get().getCell(index)
            it.pseudoClassStateChanged(PseudoClass.getPseudoClass("dead"), !selected)
            it.pseudoClassStateChanged(PseudoClass.getPseudoClass("alive"), selected)
            it.userData = index to selected
        }
    }
}