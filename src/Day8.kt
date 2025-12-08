import kotlin.math.sqrt

fun main() {
    data class Node(val x: Int, val y: Int, val z :Int) {
        fun distanceTo(other: Node): Float {
            val xDelta = (x.toLong() - other.x) * (x.toLong() - other.x)
            val yDelta = (y.toLong() - other.y) * (y.toLong() - other.y)
            val zDelta = (z.toLong() - other.z) * (z.toLong() - other.z)
            val radicand = xDelta + yDelta + zDelta
            return sqrt(radicand.toFloat())
        }
    }

    data class DistancePair(val first: Node, val second: Node, val distance: Float)
    fun parse(input: List<String>): List<Node> {
        return input.map { line ->
            val (x, y, z) = line.split(",").map { it.trim().toInt() }
            Node(x, y, z)
        }
    }

    fun List<DistancePair>.expandNetwork(start: Node): Set<Node> {
        val nodesToVisit = mutableListOf(start)
        //prevent double counting of same node in network
        val visitedNodes = mutableSetOf<Node>()
        while (nodesToVisit.isNotEmpty()) {
            val nodeToVisit = nodesToVisit.removeFirst()
            val connectedLinks = this.filter { it.first == nodeToVisit || it.second == nodeToVisit }
            val connectedNodes = connectedLinks.map { if(it.first == nodeToVisit) it.second else it.first }
                .filter { !visitedNodes.contains(it) }
            nodesToVisit.addAll(connectedNodes)
            visitedNodes.add(nodeToVisit)
        }
        return visitedNodes
    }
    fun List<DistancePair>.findAllNetworks(): List<Set<Node>> {
        val nodesToExplore = this.flatMap { listOf(it.first, it.second) }.toMutableSet()
        val networks = mutableListOf<Set<Node>>()
        while(nodesToExplore.isNotEmpty()) {
            val nodeToExplore = nodesToExplore.first()
            val network = expandNetwork(nodeToExplore)
            nodesToExplore.removeAll(network)
            networks.add(network)
        }
        return networks
    }


    //14814 too low
    fun part1(input: List<String>, numberOfConnections: Int = 1000): Int {
        val nodes = parse(input)

        val distances = mutableListOf<DistancePair>()
        nodes.forEachIndexed { index, node ->
            ((index + 1) until nodes.size).forEach { secondNodeIndex ->
                val secondNode = nodes[secondNodeIndex]
                if(node == secondNode) return@forEach
                val distance = node.distanceTo(secondNode)
                distances.add(DistancePair(node, secondNode, distance))
            }
        }

        distances.sortBy { it.distance }
        val connections = distances.take(numberOfConnections)
        val networks = connections.findAllNetworks()

        val biggestNetworks = networks.sortedByDescending { it.size }.take(3)
        val sizes = biggestNetworks.map { it.size }
        return sizes.reduce(Int::times)
    }

    fun part2(input: List<String>): Int {
        val x = parse(input)
        return input.size
    }



    val testInput = readInput("Day8Test")
    checkDebug(part1(testInput, 10), 40)
//    checkDebug(part2(testInput), 1)

    val input = readInput("Day8")
    "part1: ${part1(input)}".println()
    "part2: ${part2(input)}".println()
}