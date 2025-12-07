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


    //too low 7980502
    fun part2(input: List<String>, isTest: Boolean = false): Long {
        val maze = parse(input)
        var row = 0
        var columns = mutableMapOf(maze.first().indexOfFirst { it == 'S' } to 1L)
        while((row + 1) < maze.size) {
            val beamsToAdd = mutableMapOf<Int, Long>()
            columns.forEach { (column, count) ->
                val nextPoint = (row + 1) to column
                val char = maze[nextPoint]
                if(char == '.') {
                    beamsToAdd.addToCountLong(column, count)

//                    maze[(row + 1) to column] = if(isTest) (count % 10).digitToChar() else '|'
                } else if(char == '^') {
                    val left = column - 1
                    val right = column + 1
                    beamsToAdd.addToCountLong(left, count)
                    beamsToAdd.addToCountLong(right, count)

                } else if(char == '|' || char?.digitToInt() in 0..9) {
                    beamsToAdd.addToCountLong(column, count)

//                    maze[(row + 1) to column] = if(isTest) (count % 10).digitToChar() else '|'
                } else {
                    error("Unreachable")
                }

//                maze[(row + 1) to left] = if(isTest) (count % 10).digitToChar() else '|'
//                maze[(row + 1) to right] = if(isTest) (count % 10).digitToChar() else '|'
                beamsToAdd.forEach { (column, count) ->
                    maze[(row + 1) to column] = if(isTest) (count % 10L).toInt().digitToChar() else '|'
                }
                columns = beamsToAdd
            }
            println(columns.values.sum())
            maze.forEach { row ->
                row.joinToString("").println()
            }
            println()
            row++
        }

        return columns.values.sum()
    }



    val testInput = readInput("Day7Test")
    checkDebug(part1(testInput), 21)
    checkDebug(part2(testInput, true), 40)

    val input = readInput("Day7")
    "part1: ${part1(input)}".println()
    "part2: ${part2(input)}".println()
}