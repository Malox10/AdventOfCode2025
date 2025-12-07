fun main() {
    fun parse(input: List<String>): List<MutableList<Char>> {
        val result = input.map { it.toCharArray().toMutableList() }
        return result
    }

    fun part1(input: List<String>): Int {
        val maze = parse(input)
        var row = 0
        var columns = mutableSetOf(maze.first().indexOfFirst { it == 'S' })
        var score = 0
        while((row + 1) < maze.size) {
            val beamsToAdd = mutableSetOf<Int>()
            columns.forEach { column ->
                val nextPoint = (row + 1) to column
                val char = maze[nextPoint]
                if(char == '.') {
                    beamsToAdd.add(column)

                    maze[(row + 1) to column] = '|'
                } else if(char == '^') {
                    score++
                    val left = column - 1
                    val right = column + 1
                    beamsToAdd.add(left)
                    beamsToAdd.add(right)
                    maze[(row + 1) to left] = '|'
                    maze[(row + 1) to right] = '|'
                } else if(char == '|') {
                    println("beam overwrite")
                } else {
                    error("Unreachable")
                }
                columns = beamsToAdd
            }
            maze.forEach { row ->
                row.joinToString("").println()
            }
            println()
            row++
        }

        return score
    }

    fun part2(input: List<String>): Int {
        val x = parse(input)
        return input.size
    }



    val testInput = readInput("Day7Test")
    checkDebug(part1(testInput), 21)
//    checkDebug(part2(testInput), 1)

    val input = readInput("Day7")
    "part1: ${part1(input)}".println()
    "part2: ${part2(input)}".println()
}