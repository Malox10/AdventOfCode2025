fun main() {
    fun parse(input: List<String>): Map<String, List<String>> {
        val connections = input.associate { line ->
            val (start, rest) = line.split(":").map { it.trim() }
            val connections = rest.split(" ").map { it.trim() }
            start to connections
        }
        return connections
    }

//    fun Map<String, List<String>>.subRoutes(current: String): List<List<String>> {
//        val nextNodes = this[current]!!
//        if(nextNodes ) {}
//        val x = nextNodes.map {
//             subRoutes(it).map { listOf(current) + it  }
//        }
//    }

    fun part1(input: List<String>): Int {
        val connections = parse(input)
        val pathsToExplore = ArrayDeque<String>()
        pathsToExplore.add("you")
        var score = 0
        while(pathsToExplore.isNotEmpty()) {
            val nodeToExplore = pathsToExplore.removeFirst()
            if(nodeToExplore == "out") {
                score++
                continue
            }

            val nextNodes = connections[nodeToExplore]!!
            pathsToExplore.addAll(nextNodes)
        }

        return score
    }

    data class Key(val current: String, val fft: Boolean, val dac: Boolean)
    val cache = mutableMapOf<Key, Long>()
    fun Map<String, List<String>>.findOutRecursive(current: String = "svr", fft: Boolean = false, dac: Boolean = false): Long {
        if(current == "out") {
            return if(fft && dac) 1L else 0L
        }
        val key = Key(current, fft, dac)
        if(cache.containsKey(key)) {
            return cache[key]!!
        }

        val nextNodes = this[current]!!
        val value =  nextNodes.sumOf { node ->
            val newFft = fft || node == "fft"
            val newDac = dac || node == "dac"
            findOutRecursive(node, newFft, newDac)
        }

        cache[key] = value
        return value
    }

    fun part2(input: List<String>): Long {
        val connections = parse(input)
        cache.clear()
        return connections.findOutRecursive()
    }

    val testInput = readInput("Day11Test")
    checkDebug(part1(testInput), 5)

    val testInputPart2 = readInput("Day11TestPart2")
    checkDebug(part2(testInputPart2), 2)

    val input = readInput("Day11")
    "part1: ${part1(input)}".println()
    "part2: ${part2(input)}".println()
}