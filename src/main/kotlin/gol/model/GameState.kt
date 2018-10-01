package gol.model

data class GameState(
        val generation: Int,
        val board: BooleanArray,
        val width: Int,
        val height: Int,
        val aliveCells: Int,
        val deadCells: Int
) {
    companion object {
        fun fromBoard(board: GameBoard): GameState {
            return GameState(board.tick, board.board, board.width, board.height, board.aliveCells, board.deadCells)
        }
    }
}