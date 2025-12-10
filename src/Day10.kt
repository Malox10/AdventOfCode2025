import kotlin.math.pow
import kotlin.math.roundToInt

fun main() {
    data class Schematic(val lights: Int, val buttons: List<Int>, val joltages: List<Int>)
    fun parse(input: List<String>): List<Schematic> {
        val schematics = input.map { line ->
            val parts = line.split(" ").map { it.trim() }.toMutableList()
            val lightsString = parts.removeFirst()
            val joltagesString = parts.removeLast()

            val bracketsRegex = Regex("[\\[\\]]")
            val lights = lightsString
                .replace(bracketsRegex,"")
                .trim()
                .map { if(it == '.') "0" else "1" }
                .joinToString("")
                .reversed()
                .toInt(2)

            val bracesRegex = Regex("[{}]")
            val joltages = joltagesString
                .replace(bracesRegex, "")
                .trim()
                .split(",")
                .map { it.trim().toInt() }

            val parensRegex = Regex("[()]")
            val buttons = parts.map { button ->
                button
                    .replace(parensRegex, "")
                    .trim()
                    .split(",")
                    .map { it.trim().toInt() }
                    .sumOf { exponent ->
                        2.0.pow(exponent).roundToInt()
                    }
            }

            Schematic(lights, buttons, joltages)
        }

        return schematics
    }

    fun Schematic.findShortestSequence(): Int {
        if(this.lights == 0) return 0
        val stateQueue = ArrayDeque<Pair<Int, Int>>()
        stateQueue.add(0 to 0)

        while(stateQueue.isNotEmpty()) {
            val currentState = stateQueue.removeFirst()
            this.buttons.forEach { button ->
                val changedState = currentState.first xor button
                val newDepth = currentState.second + 1
                if(changedState == this.lights) return newDepth
                stateQueue.add(changedState to newDepth)
            }
        }
        error("no solution found")
    }

    fun part1(input: List<String>): Int {
        val schematics = parse(input)
        val solutions = schematics.map { it.findShortestSequence() }
        return solutions.sum()
    }

    fun part2(input: List<String>): Int {
        val x = parse(input)
        return input.size
    }



    val testInput = readInput("Day10Test")
    checkDebug(part1(testInput), 7)
//    checkDebug(part2(testInput), 1)

    val input = readInput("Day10")
    "part1: ${part1(input)}".println()
    "part2: ${part2(input)}".println()
}