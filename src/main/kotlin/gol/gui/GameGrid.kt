package gol.gui

//import gol.gui.skin.GameGridSkin
import gol.gui.skin.GameGridSkin
import gol.model.GameBoard
import javafx.beans.binding.Bindings
import javafx.beans.binding.NumberBinding
import javafx.beans.property.IntegerProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.control.Control
import javafx.scene.control.Skin

class GameGrid(boardWidth: Int, boardHeight: Int) : Control(), GameBoard {

    val boardWidthProperty: IntegerProperty = SimpleIntegerProperty(boardWidth)
    override var boardWidth: Int
        set(value) = boardWidthProperty.set(value)
        get() = boardWidthProperty.get()

    val boardHeightProperty: IntegerProperty = SimpleIntegerProperty(boardHeight)
    override var boardHeight: Int
        set(value) = boardHeightProperty.set(value)
        get() = boardHeightProperty.get()

    val totalCellsProperty: NumberBinding = Bindings.multiply(boardHeightProperty, boardWidthProperty)

    val cellsProperty: ObjectProperty<ObservableList<GameCell>> = SimpleObjectProperty()

    val selectionModel: GameGridSelectionModel = GameGridSelectionModel(this)

    init {
        styleClass += "game-grid"

        prefWidth = 300.0
        prefHeight = 300.0

        val cells = Array(boardWidth * boardHeight) { GameCell(it) }
        cellsProperty.set(FXCollections.observableArrayList(*cells))
    }

    override fun getUserAgentStylesheet(): String {
        return GameGrid::class.java.getResource("gamegrid.css").toExternalForm()
    }

    public override fun createDefaultSkin(): Skin<*> {
        return GameGridSkin(this)
    }

    override fun isAlive(x: Int, y: Int): Boolean {
        return inBounds(x, y) && selectionModel.isSelected(cellIndex(x, y))
    }

    override fun isAlive(index: Int): Boolean {
        return inBounds(index) && selectionModel.isSelected(index)
    }

    override fun setCell(x: Int, y: Int, alive: Boolean) {
        if (alive) {
            selectionModel.select(cellIndex(x, y))
        } else {
            selectionModel.clearSelection(cellIndex(x, y))
        }
    }

    override fun setCell(index: Int, alive: Boolean) {
        if (alive) {
            selectionModel.select(index)
        } else {
            selectionModel.clearSelection(index)
        }
    }

    override fun clear() {
        selectionModel.clearSelection()
    }

    override fun doTick() {
        val changes = getTickChanges()
        selectionModel.selectIndices(*changes.filter { it.second }.map { it.first }.toIntArray())
        selectionModel.clearIndices(*changes.filter { !it.second }.map { it.first }.toIntArray())
    }
}