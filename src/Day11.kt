import java.util.Queue

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

    fun part2(input: List<String>): Int {
        val x = parse(input)
        return input.size
    }



    val testInput = readInput("Day11Test")
    checkDebug(part1(testInput), 5)
//    checkDebug(part2(testInput), 1)

    val input = readInput("Day11")
    "part1: ${part1(input)}".println()
    "part2: ${part2(input)}".println()
}