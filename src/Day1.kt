import kotlin.math.absoluteValue
import kotlin.math.sign

fun main() {
    fun parse(input: List<String>): List<Int> {
        return input.map { line ->
            val direction = if(line[0] == 'L') -1 else 1
            val amount = line.substring(1).toInt()
            direction * amount
        }
    }

    fun part1(input: List<String>): Int {
        val turns = parse(input)
        var location = 50
        var score = 0
        turns.forEach {
            location += it
            location = (location + 100) % 100
            if(location == 0) score++
        }
        return score
    }

    fun part2(input: List<String>): Int {
        val turns = parse(input)
        var location = 50
        var score = 0
        turns.forEach { turn ->
            (1..(turn.absoluteValue)).forEach { _ ->
                location += turn.sign
                if(location < 0) location = 99
                if(location > 99) location = 0
                if(location == 0) score++
            }
        }
        return score
    }

    val testInput = readInput("Day1Test")
    checkDebug(part1(testInput), 3)
    checkDebug(part2(testInput), 6)

    val input = readInput("Day1")
    "part1: ${part1(input)}".println()
    "part2: ${part2(input)}".println()
}