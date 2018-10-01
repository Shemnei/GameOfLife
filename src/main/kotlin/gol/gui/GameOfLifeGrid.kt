package gol.gui

import gol.gui.skin.GameOfLifeGridSkin
import gol.model.GameBoard
import javafx.beans.property.BooleanProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.control.Control
import javafx.scene.control.Skin

class GameOfLifeGrid(board: GameBoard) : Control() {

    val gameBoardProperty: ObjectProperty<GameBoard> = SimpleObjectProperty(board)
    val disableProperty: BooleanProperty = SimpleBooleanProperty(false)

    init {
        styleClass += "game-of-life-grid"

        prefWidth = 300.0
        prefHeight = 300.0
    }

    override fun getUserAgentStylesheet(): String {
        return GameOfLifeGrid::class.java.getResource("gameoflifegrid.css").toExternalForm()
    }

    public override fun createDefaultSkin(): Skin<*> {
        return GameOfLifeGridSkin(this)
    }
}