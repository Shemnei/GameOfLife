package gol.model

class GameBoard(val width: Int, val height: Int) {

    val cellCount: Int = width * height

    var tick: Int = 0
        private set

    private var _board: BooleanArray = BooleanArray(cellCount) { false }
    val board: BooleanArray
        get() = _board.copyOf()

    var aliveCells: Int = 0
        private set

    var deadCells: Int = cellCount
        private set


    fun setCell(x: Int, y: Int, value: Boolean) {
        checkBounds(x, y)
        _board[cellIndex(x, y)] = value
    }

    fun setCell(index: Int, value: Boolean) {
        if (index < 0 || index >= cellCount)
            throw IllegalArgumentException("Index must be in range [0-$cellCount]")
        _board[index] = value
    }

    fun getCell(x: Int, y: Int): Boolean {
        checkBounds(x, y)
        return _board[cellIndex(x, y)]
    }

    fun getCell(index: Int): Boolean {
        if (index < 0 || index >= cellCount)
            throw IllegalArgumentException("Index must be in range [0-$cellCount]")
        return _board[index]
    }

    fun toggleCell(x: Int, y: Int): Boolean {
        val value = getCell(x, y)
        setCell(x, y, !value)
        return !value
    }

    fun clear() {
        _board = BooleanArray(cellCount)
    }

    fun loadState(state: GameState) {
        if (state.width != width) {
            throw IllegalArgumentException("Width of state and board doesn't match")
        }
        if (state.height != height) {
            throw IllegalArgumentException("Height of state and board doesn't match")
        }
        if (state.deadCells + state.aliveCells != cellCount) {
            throw IllegalArgumentException("State is not valid (deadCell + aliveCells != width * height)")
        }

        this.tick = state.generation
        this._board = state.board
        this.aliveCells = state.aliveCells
        this.deadCells = state.deadCells
    }

    private fun cellIndex(x: Int, y: Int): Int {
        return y * width + x
    }

    private fun checkBounds(x: Int, y: Int) {
        if (x < 0 || x >= width)
            throw IllegalArgumentException("x must be in range [0-$width]")
        if (y < 0 || y >= height)
            throw IllegalArgumentException("y must be in range [0-$height]")
    }

    fun doTick() {
        val tmpBoard = _board.copyOf()
        (0 until height).forEach { y ->
            (0 until width).forEach { x ->
                val cellValue = getCellNoCheck(x, y)
                val neighbors = getNeighbors(x, y)
                val liveNeighbors = neighbors.count { it }

                if (!cellValue) {
                    if (liveNeighbors == 3) {
                        tmpBoard[cellIndex(x, y)] = true
                    }
                } else {
                    when (liveNeighbors) {
                        0, 1 -> tmpBoard[cellIndex(x, y)] = false
                        in (4 until 9) -> tmpBoard[cellIndex(x, y)] = false
                    }
                }
            }
        }

        tick++
        _board = tmpBoard
    }

    private fun getCellNoCheck(x: Int, y: Int): Boolean {
        if (x < 0 || x >= width)
            return false
        if (y < 0 || y >= height)
            return false
        return _board[cellIndex(x, y)]
    }

    private fun getNeighbors(x: Int, y: Int): BooleanArray {
        val neighbors: BooleanArray = BooleanArray(8) { false }

        // first row
        neighbors[0] = getCellNoCheck(x - 1, y - 1)
        neighbors[1] = getCellNoCheck(x, y - 1)
        neighbors[2] = getCellNoCheck(x + 1, y - 1)

        // second row
        neighbors[3] = getCellNoCheck(x - 1, y)
        // ignore own cell
        neighbors[4] = getCellNoCheck(x + 1, y)

        // third row
        neighbors[5] = getCellNoCheck(x - 1, y + 1)
        neighbors[6] = getCellNoCheck(x, y + 1)
        neighbors[7] = getCellNoCheck(x + 1, y + 1)

        return neighbors
    }
}
