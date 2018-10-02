package gol.gui

import javafx.scene.layout.Pane

class GameCell(
        val index: Int
) : Pane() {
    var selected: Boolean = false
}