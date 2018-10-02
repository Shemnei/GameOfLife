package gol.model

class SimpleGameBoard(override val boardWidth: Int, override val boardHeight: Int) : GameBoard {

    val cellCount: Int = boardWidth * boardHeight

    var tick: Int = 0
        private set

    private var _board: BooleanArray = BooleanArray(cellCount) { false }
    val board: BooleanArray
        get() = _board.copyOf()

    var aliveCells: Int = 0
        private set

    var deadCells: Int = cellCount
        private set


    override fun setCell(x: Int, y: Int, alive: Boolean) {
        checkBounds(x, y)
        _board[cellIndex(x, y)] = alive
    }

    override fun setCell(index: Int, alive: Boolean) {
        if (index < 0 || index >= cellCount)
            throw IllegalArgumentException("Index must be in range [0-$cellCount]")
        _board[index] = alive
    }

    override fun isAlive(x: Int, y: Int): Boolean {
        checkBounds(x, y)
        return _board[cellIndex(x, y)]
    }

    override fun isAlive(index: Int): Boolean {
        if (index < 0 || index >= cellCount)
            throw IllegalArgumentException("Index must be in range [0-$cellCount]")
        return _board[index]
    }

    override fun clear() {
        _board = BooleanArray(cellCount)
    }

    private fun checkBounds(x: Int, y: Int) {
        if (x < 0 || x >= boardWidth)
            throw IllegalArgumentException("x must be in range [0-$boardWidth]")
        if (y < 0 || y >= boardHeight)
            throw IllegalArgumentException("y must be in range [0-$boardHeight]")
    }

    override fun doTick() {
        val changes = getTickChanges()
        changes.forEach { _board[it.first] = it.second }
        tick++
    }

    private fun getCellNoCheck(x: Int, y: Int): Boolean {
        return inBounds(x, y) && _board[cellIndex(x, y)]
    }
}
