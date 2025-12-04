fun main() {
    fun parse(input: List<String>): List<List<Boolean>> {
        return input.map { line -> line.map { it == '@' } }
    }


    val directions = listOf(
        -1 to -1, -1 to 0, -1 to 1,
         0 to -1,           0 to 1,
         1 to -1,  1 to 0,  1 to 1
    )
    fun part1(input: List<String>): Int {
        val paperGrid = parse(input)

        var accessible = 0
        val accessibleSpots = mutableListOf<Pair<Int, Int>>()
        paperGrid.forEachIndexed { rowIndex, row ->
            row.forEachIndexed inner@ { colIndex, isPaper ->
                if(!isPaper) return@inner

                val currentPosition = rowIndex to colIndex
                val neighbourCount = directions.count {
                    val neighbor = currentPosition + it
                    val isOccupied = paperGrid[neighbor] ?: false
                    isOccupied
                }
                if(neighbourCount < 4) {
                    accessible++
                    accessibleSpots.add(currentPosition)
                }
            }
        }
        return accessible
    }

    fun part2(input: List<String>): Int {
        val x = parse(input)
        return input.size
    }



    val testInput = readInput("Day4Test")
    checkDebug(part1(testInput), 13)
//    checkDebug(part2(testInput), 1)

    val input = readInput("Day4")
    "part1: ${part1(input)}".println()
    "part2: ${part2(input)}".println()
}