fun main() {
    fun parse(input: List<String>): List<List<Boolean>> {
        return input.map { line -> line.map { it == '@' } }
    }


    val directions = listOf(
        -1 to -1, -1 to 0, -1 to 1,
         0 to -1,           0 to 1,
         1 to -1,  1 to 0,  1 to 1
    )
    fun List<List<Boolean>>.findAccessibleRolls(): List<Pair<Int, Int>> {
        val accessibleSpots = mutableListOf<Pair<Int, Int>>()
        this.forEachIndexed { rowIndex, row ->
            row.forEachIndexed inner@{ colIndex, isPaper ->
                if (!isPaper) return@inner

                val currentPosition = rowIndex to colIndex
                val neighbourCount = directions.count {
                    val neighbor = currentPosition + it
                    val isOccupied = this[neighbor] ?: false
                    isOccupied
                }
                if (neighbourCount < 4) {
                    accessibleSpots.add(currentPosition)
                }
            }
        }
        return accessibleSpots
    }

    fun part1(input: List<String>): Int {
        val paperGrid = parse(input)
        return paperGrid.findAccessibleRolls().size
    }

    fun part2(input: List<String>): Int {
        val paperGrid = parse(input).map { it.toMutableList() }
        var removed = 0
        do {
            val removable = paperGrid.findAccessibleRolls()
            removed += removable.size
            removable.forEach { paperGrid[it] = false }
        } while(removable.isNotEmpty())

        return removed
    }



    val testInput = readInput("Day4Test")
    checkDebug(part1(testInput), 13)
    checkDebug(part2(testInput), 43)

    val input = readInput("Day4")
    "part1: ${part1(input)}".println()
    "part2: ${part2(input)}".println()
}