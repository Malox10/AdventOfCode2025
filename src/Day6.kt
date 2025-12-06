fun main() {
    data class Homework(val numberGrid: List<List<Long>>, val operators: List<Operator>)

    fun parse(input: List<String>): Homework {
        val operators = input.last().split(" ").filter { it.isNotEmpty() }.map { it.trim() }.map {
            if(it == "+") Operator.Add else Operator.Multiply
        }

        val numberInput = input.dropLast(1)
        val grid = numberInput.map { it.split(" ").filter { it.isNotEmpty() }.map { num -> num.trim().toLong() } }.transpose()
        return Homework(grid, operators)
    }

    fun part1(input: List<String>): Long {
        val homework = parse(input)
        val answers = homework.numberGrid.mapIndexed { index, numbers ->
            val operator = homework.operators[index]
            val result = numbers.reduce(if(operator == Operator.Add) Long::plus else Long::times)
            result
        }
        return answers.sum()
    }

    fun part2(input: List<String>): Int {
        val x = parse(input)
        return input.size
    }



    val testInput = readInput("Day6Test")
    checkDebug(part1(testInput), 4277556)
//    checkDebug(part2(testInput), 1)

    val input = readInput("Day6")
    "part1: ${part1(input)}".println()
    "part2: ${part2(input)}".println()
}

enum class Operator {
    Add,
    Multiply
}