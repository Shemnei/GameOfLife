package gol.gui

import javafx.beans.binding.Bindings
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.control.MultipleSelectionModel
import java.util.concurrent.Callable

class GameGridSelectionModel(val control: GameGrid) : MultipleSelectionModel<GameCell>() {

    private val selectedCellsProperty: ObservableList<GameCell> = FXCollections.observableArrayList()
    private val selectedIndicesProperty: ObservableList<Int> = FXCollections.observableArrayList()
    val selectedCountProperty = Bindings.createIntegerBinding(Callable { selectedCellsProperty.size }, selectedCellsProperty)

    override fun clearSelection(index: Int) {
        val cell = control.cellsProperty.get()[index]
        if (cell.selected) {
            selectedCellsProperty.remove(cell)
            selectedIndicesProperty.remove(index)
            cell.selected = false
        }
    }

    override fun clearSelection() {
        selectedCellsProperty.toList().forEach {
            it.selected = false
        }
        selectedCellsProperty.clear()
        selectedIndicesProperty.clear()
    }

    override fun selectLast() {
        select(control.cellsProperty.get().size)
    }

    override fun isSelected(index: Int): Boolean {
        return control.cellsProperty.get()[index].selected
    }

    override fun getSelectedIndices(): ObservableList<Int> {
        return selectedIndicesProperty
    }

    override fun selectAll() {
        selectedCellsProperty.addAll(control.cellsProperty.get())
        selectedIndicesProperty.addAll((0 until selectedCellsProperty.size))
        control.cellsProperty.get().toList().forEach { it.selected = true }
    }

    override fun getSelectedItems(): ObservableList<GameCell> {
        return selectedCellsProperty
    }

    override fun select(index: Int) {
        val cell = control.cellsProperty.get()[index]
        if (!cell.selected) {
            selectedCellsProperty.add(cell)
            selectedIndicesProperty.add(index)
            cell.selected = true
        }
    }

    override fun select(obj: GameCell) {
        if (!obj.selected) {
            selectedCellsProperty.add(obj)
            selectedIndicesProperty.add(obj.index)
            obj.selected = true
        }
    }

    override fun isEmpty(): Boolean {
        return selectedCellsProperty.isEmpty()
    }

    override fun selectNext() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun selectPrevious() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun selectIndices(index: Int, vararg indices: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun selectIndices(vararg indices: Int) {
        val cells = indices.map { val c = control.cellsProperty.get()[it]; c.selected = true; c }
        selectedCellsProperty.addAll(cells)
        selectedIndicesProperty.addAll(indices.toTypedArray())
    }

    fun clearIndices(vararg indices: Int) {
        val cells = indices.map { val c = control.cellsProperty.get()[it]; c.selected = false; c }
        selectedCellsProperty.removeAll(cells)
        selectedIndicesProperty.removeAll(indices.toTypedArray())
    }

    override fun selectFirst() {
        select(0)
    }

    override fun clearAndSelect(index: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun toggle(index: Int) {
        if (isSelected(index)) {
            clearSelection(index)
        } else {
            select(index)
        }
    }
}