package gol.gui.skin

import gol.gui.GameCell
import gol.gui.GameGrid
import javafx.collections.ListChangeListener
import javafx.css.PseudoClass
import javafx.geometry.HPos
import javafx.geometry.VPos
import javafx.scene.control.SkinBase
import javafx.scene.input.MouseEvent
import javafx.scene.layout.*

class GameGridSkin(val control: GameGrid) : SkinBase<GameGrid>(control) {

    private lateinit var grid: GridPane

    private lateinit var cells: Array<Pane>

    private lateinit var colConstraint: ColumnConstraints
    private lateinit var rowConstraint: RowConstraints

    init {
        initNodes()
        layoutNodes()

        control.selectionModel.selectedItems.addListener(ListChangeListener {
            processCellChange(it)
        })
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

        cells = Array(control.totalCellsProperty.intValue()) { index ->
            val cell = Pane()
            cell.styleClass += "game-cell"
            cell.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE)
            cell.setOnMouseClicked { handleCellClicked(it) }
            cell.userData = index
            cell
        }
    }

    private fun layoutNodes() {
        cells.forEachIndexed { index, cell ->
            val x = index % control.boardWidthProperty.get()
            val y = index / control.boardWidthProperty.get()
            grid.add(cell, x, y)
        }

        (0 until control.boardWidthProperty.get()).forEach {
            grid.columnConstraints += colConstraint
        }

        (0 until control.boardWidthProperty.get()).forEach {
            grid.rowConstraints += rowConstraint
        }

        children += grid
    }

    private fun handleCellClicked(event: MouseEvent) {
        val cell = event.source as Pane
        control.selectionModel.toggle(cell.userData as Int)
    }

    private fun processCellChange(list: ListChangeListener.Change<out GameCell>) {
        while (list.next()) {
            list.removed.forEach {
                val index = it.index
                val cell = cells[index]
                cell.pseudoClassStateChanged(PseudoClass.getPseudoClass("dead"), true)
                cell.pseudoClassStateChanged(PseudoClass.getPseudoClass("alive"), false)
            }
            list.addedSubList.forEach {
                val index = it.index
                val cell = cells[index]
                cell.pseudoClassStateChanged(PseudoClass.getPseudoClass("dead"), false)
                cell.pseudoClassStateChanged(PseudoClass.getPseudoClass("alive"), true)
            }
        }
    }
}