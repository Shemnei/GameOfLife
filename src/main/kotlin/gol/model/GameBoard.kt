package gol.model

interface GameBoard {

    val boardWidth: Int
    val boardHeight: Int

    fun isAlive(x: Int, y: Int): Boolean
    fun isAlive(index: Int): Boolean

    fun setCell(x: Int, y: Int, alive: Boolean)
    fun setCell(index: Int, alive: Boolean)

    fun clear()

    fun doTick()

    fun cellIndex(x: Int, y: Int): Int {
        return y * boardWidth + x
    }

    fun inBounds(x: Int, y: Int): Boolean {
        if (x < 0 || x >= boardWidth)
            return false
        if (y < 0 || y >= boardHeight)
            return false
        return true
    }

    fun inBounds(index: Int): Boolean {
        if (index < 0 || index >= (boardWidth * boardHeight))
            return false
        return true
    }

    fun getNeighbors(x: Int, y: Int): BooleanArray {
        val neighbors = BooleanArray(8) { false }

        // first row
        neighbors[0] = isAlive(x - 1, y - 1)
        neighbors[1] = isAlive(x, y - 1)
        neighbors[2] = isAlive(x + 1, y - 1)

        // second row
        neighbors[3] = isAlive(x - 1, y)
        // ignore own cell
        neighbors[4] = isAlive(x + 1, y)

        // third row
        neighbors[5] = isAlive(x - 1, y + 1)
        neighbors[6] = isAlive(x, y + 1)
        neighbors[7] = isAlive(x + 1, y + 1)

        return neighbors
    }

    fun getTickChanges(): Array<Pair<Int, Boolean>> {
        val changes = mutableListOf<Pair<Int, Boolean>>()

        (0 until boardHeight).forEach { y ->
            (0 until boardWidth).forEach { x ->
                val cellValue = isAlive(cellIndex(x, y))
                val neighbors = getNeighbors(x, y)
                val liveNeighbors = neighbors.count { it }

                if (!cellValue) {
                    if (liveNeighbors == 3) {
                        changes.add(cellIndex(x, y) to true)
                    }
                } else {
                    when (liveNeighbors) {
                        0, 1 -> changes.add(cellIndex(x, y) to false)
                        in (4 until 9) -> changes.add(cellIndex(x, y) to false)
                    }
                }
            }
        }

        return changes.toTypedArray()
    }
}