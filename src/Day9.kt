import kotlin.math.absoluteValue

fun main() {
    fun parse(input: List<String>): List<Pair<Int, Int>> {
        return input.map { line ->
            val (a, b) = line.split(",").map { it.trim().toInt() }
            a to b
        }
    }

    fun part1(input: List<String>): Long {
        val pairs = parse(input)

        val areas = mutableListOf<Long>()
        pairs.forEachIndexed { index, pair ->
            ((index + 1) until pairs.size).forEach { secondPairIndex ->
                val secondPair = pairs[secondPairIndex]
                if(pair == secondPair) return@forEach
                val deltaX = (pair.first - secondPair.first).absoluteValue + 1L
                val deltaY = (pair.second - secondPair.second).absoluteValue + 1L
                val area = deltaX * deltaY
                areas.add(area)
            }
        }

        return areas.max()
    }

    fun part2(input: List<String>): Int {
        val x = parse(input)
        return input.size
    }



    val testInput = readInput("Day9Test")
    checkDebug(part1(testInput), 50)
//    checkDebug(part2(testInput), 1)

    val input = readInput("Day9")
    "part1: ${part1(input)}".println()
    "part2: ${part2(input)}".println()
}