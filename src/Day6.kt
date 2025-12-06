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

    fun part2(input: List<String>): Long {
        val numbers = input.dropLast(1)
        val numberLength = numbers.size

        val operators = input.last()

        val operatorIndices = mutableListOf<Pair<Operator, Int>>()
        operators.forEachIndexed { index, operator ->
            if(operator == '*') operatorIndices.add(Operator.Multiply to index)
            if(operator == '+') operatorIndices.add(Operator.Add to index)
        }

        val longestLine = numbers.maxOf { it.length }
        operatorIndices.add(Operator.Multiply to longestLine + 1)
        val answers = operatorIndices.windowed(2).map { (start, end) ->
            val parsedNumbers = (start.second..(end.second - 2)).reversed().map { column ->
                val number = (0 until numberLength).joinToString("") { row ->
                    val char = numbers.getOrNull(row)?.getOrNull(column) ?: ""
                    char.toString()
                }.trim().toLong()
                number
            }
            parsedNumbers.reduce(if(start.first == Operator.Add) Long::plus else Long::times)
        }
        return answers.sum()
    }



    val testInput = readInput("Day6Test")
    checkDebug(part1(testInput), 4277556)
    checkDebug(part2(testInput), 3263827)

    val input = readInput("Day6")
    "part1: ${part1(input)}".println()
    "part2: ${part2(input)}".println()
}

enum class Operator {
    Add,
    Multiply
}