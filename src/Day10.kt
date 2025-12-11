import kotlin.math.pow
import kotlin.math.roundToInt

fun main() {
    (0..0).map { it.println() }

    data class Schematic(val lights: Int, val buttons: List<List<Int>>, val joltages: List<Int>) {
        val buttonMasks: List<Int> = buttons.map { it.sumOf { exponent ->
            2.0.pow(exponent).roundToInt() }
        }
    }
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
            this.buttonMasks.forEach { button ->
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


//    data class JoltageState(val state: List<Int>, val depth: Int, val priority: Int)
//    //assumes a state that can still reach the goal
//    fun calculateNewJoltageState(maxStep: Int, maxScore: Int, state: List<Int>, depth: Int): JoltageState {
//        val score = state.sum()
//        val delta = maxScore - score
//        val minimumSteps = ceil(delta.toFloat() / maxStep).roundToInt()
//        val minimumFinalDepth = minimumSteps + depth
//        return JoltageState(state, depth, minimumFinalDepth)
//    }
//
//    val cache = mutableMapOf<List<Int>, Int>()
//    fun Schematic.findShortestJoltageSequence(): Int {
//        val maxStep = this.buttons.maxOfOrNull { it.size }!!
//        val maxScore = this.joltages.sum()
//
//        val stateQueue = PriorityQueue<JoltageState>(compareBy { it.priority })
//        val startState = List(this.joltages.size) { 0 }
//        val startJoltage = calculateNewJoltageState(maxStep, maxScore, startState, 0)
//        stateQueue.add(startJoltage)
//
//        while(stateQueue.isNotEmpty()) {
//            val currentState = stateQueue.remove()
//            if(stateQueue.size > 10) {
//                println(1)
//            }
//
//            this.buttons.forEach { button ->
//                val mutableState = currentState.state.toMutableList()
//                button.forEach { index -> mutableState[index]++ }
//
//                val newDepth = currentState.depth + 1
//                if(mutableState == this.joltages) return newDepth
//
//                val isOverTarget = this.joltages.zip(mutableState).any { (target, current) -> current > target }
//                if(isOverTarget) return@forEach
//
//                val cachedDepth = cache[mutableState]
//                if(cachedDepth != null && cachedDepth <= newDepth) return@forEach
//                cache[mutableState] = newDepth
//
//                val newJoltage = calculateNewJoltageState(maxStep, maxScore, mutableState, newDepth)
//                stateQueue.add(newJoltage)
//            }
//        }
//        error("no solution found")
//    }

    val endCache = mutableMapOf<ButtonsAndJoltages, Int?>()
    fun List<Int>.findShortestJoltageSequence(availableButtons: List<List<Int>>, joltages: List<Int>, depth: Int = 0): Int? {
        if(this == joltages) return depth
        val satisfiedIndices = this.zip(joltages).mapIndexedNotNull { index, (target, current) ->
            if(target != current) return@mapIndexedNotNull null
            index
        }

        val newAvailableButtons = availableButtons
            .filter { button ->
                button.none { it in satisfiedIndices }
            }
        if(newAvailableButtons.isEmpty()) return null

        val key = newAvailableButtons to joltages
        if(endCache.containsKey(key)) {
            return endCache[key]
        }

        val buttonCounts = newAvailableButtons.flatten().counts()
        val uniqueKeys = buttonCounts
            .filter { it.value == 1 }.keys

        //if there's a best next choice
        if(uniqueKeys.isNotEmpty()) {
            val uniqueButtons = newAvailableButtons.filter { button -> uniqueKeys.any { button.contains(it) } }
            val nextButton = uniqueButtons.maxBy { it.size }

            val uniqueKeysOnButton = uniqueKeys.filter { it in nextButton }
            val amountPressed = uniqueKeysOnButton.maxOfOrNull { key -> this[key] - joltages[key] }!!

            val mutableJoltages = joltages.toMutableList()
            nextButton.forEach { mutableJoltages[it] += amountPressed }
            val isOverTarget = this.zip(mutableJoltages).any { (target, current) -> current > target }
            if(isOverTarget) return null

            val finalButtons = newAvailableButtons.toMutableList()
            finalButtons.remove(nextButton)
            return findShortestJoltageSequence(finalButtons, mutableJoltages, depth + amountPressed)
        } else {
            val pairedKeys = buttonCounts.filter { it.value == 2 }.keys
            //special optimization if there are only two buttons for a certain index
            val results = if(pairedKeys.isNotEmpty()) {
                val pairedButtons = pairedKeys.map { key -> key to newAvailableButtons.filter { button -> key in button } }
                val buttonPairAndKey = pairedButtons.maxBy { (_, pair) -> pair[0].size + pair[1].size }
                val (key, buttonPair) = buttonPairAndKey
                assert(buttonPair.size == 2)
                val maxAmountPressed = this[key] - joltages[key]
                val (a, b) = buttonPair
                val results = (0..maxAmountPressed).reversed().mapNotNull { amountPressed ->
                    val mutableJoltages = joltages.toMutableList()
                    a.forEach { mutableJoltages[it] += amountPressed }
                    b.forEach { mutableJoltages[it] += (maxAmountPressed - amountPressed) }

                    val isOverTarget = this.zip(mutableJoltages).any { (target, current) -> current > target }
                    if(isOverTarget) return@mapNotNull null

                    val finalButtons = newAvailableButtons.toMutableList()
                    finalButtons.remove(a)
                    finalButtons.remove(b)
                    findShortestJoltageSequence(finalButtons, mutableJoltages, depth + maxAmountPressed)
                }
                results
            } else {
                val nextButton = newAvailableButtons.maxBy { it.size }
                //what is the index for which it overflows first?
                val maxAmountPressed = nextButton.minOfOrNull { key -> this[key] - joltages[key] } ?: 0
                val results = (0..maxAmountPressed).reversed().mapNotNull { amountPressed ->
                    val mutableJoltages = joltages.toMutableList()
                    nextButton.forEach { mutableJoltages[it] += amountPressed }

                    val finalButtons = newAvailableButtons.toMutableList()
                    finalButtons.remove(nextButton)
                    findShortestJoltageSequence(finalButtons, mutableJoltages, depth + amountPressed)
                }
                results
            }
            val minResult = results.minOrNull()
//            endCache[newAvailableButtons to joltages] = minResult
            return minResult
        }
    }

    fun part2(input: List<String>): Int {
        val schematics = parse(input)
        val solutions = schematics.map { schematic ->
            val result = schematic.joltages.findShortestJoltageSequence(schematic.buttons, List(schematic.joltages.size) { 0 })
            endCache.clear()
            println("$schematic solution: $result")
            result!!
        }
        return solutions.sum()
    }



    val testInput = readInput("Day10Test")
    checkDebug(part1(testInput), 7)
    checkDebug(part2(testInput), 33)

    val input = readInput("Day10")
    "part1: ${part1(input)}".println()
    "part2: ${part2(input)}".println()
}

typealias ButtonsAndJoltages = Pair<List<List<Int>>, List<Int>>