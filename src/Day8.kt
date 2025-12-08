import kotlin.math.sqrt
import kotlin.system.measureTimeMillis

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

    fun Map<Node, List<Node>>.expandNetwork(start: Node): Set<Node> {
        //prevent double counting of same node in network
        val nodesToVisit = mutableSetOf(start)
        val visitedNodes = mutableSetOf<Node>()
        while (nodesToVisit.isNotEmpty()) {
            val nodeToVisit = nodesToVisit.first()
            nodesToVisit.remove(nodeToVisit)
            val connectedNodes = this[nodeToVisit] ?: emptyList()
            nodesToVisit.addAll(connectedNodes.filter { !visitedNodes.contains(it) })
            visitedNodes.add(nodeToVisit)
        }
        return visitedNodes
    }
    fun List<DistancePair>.findAllNetworks(nodesToExplore: Set<Node>? = null): List<Set<Node>> {
        val nodesToExplore = nodesToExplore?.toMutableSet() ?: this.flatMap { listOf(it.first, it.second) }.toMutableSet()
        val nodeMap = mutableMapOf<Node, MutableList<Node>>()
        this.forEach { pair ->
            nodeMap.addToList(pair.first, pair.second)
            nodeMap.addToList(pair.second, pair.first)
        }
        val networks = mutableListOf<Set<Node>>()
        while(nodesToExplore.isNotEmpty()) {
            val nodeToExplore = nodesToExplore.first()
            val network = nodeMap.expandNetwork(nodeToExplore)
            nodesToExplore.removeAll(network)
            networks.add(network)
        }
        return networks
    }

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

    //479575008 too low
    fun part2(input: List<String>, startNumberOfConnections: Int = 10): Long {
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

        var lowestWorkingSolution = distances.size
        var highestFail = 0
        var currentNumberOfLinks = startNumberOfConnections

        val mutableNodes =  nodes.toSet()
        while(true) {
            val currentLinks = distances.take(currentNumberOfLinks)
            val networks = currentLinks.findAllNetworks(mutableNodes)
            val isOneNetwork = networks.size == 1
            if(isOneNetwork) {
                lowestWorkingSolution = currentNumberOfLinks
                currentNumberOfLinks = (lowestWorkingSolution + highestFail) / 2
            } else {
                highestFail = currentNumberOfLinks
                if(highestFail < (lowestWorkingSolution / 2)) {
                    currentNumberOfLinks *= 2
                } else {
                    currentNumberOfLinks = (lowestWorkingSolution + highestFail) / 2
                }
            }

//            println("lowestWorkingSolution: $lowestWorkingSolution")
//            println("currentNumberOfLinks: $currentNumberOfLinks")
//            println("highestFail: $highestFail")
            if(lowestWorkingSolution - highestFail == 1) {
                val winningLink = distances[lowestWorkingSolution - 1]
                return winningLink.first.x.toLong() * winningLink.second.x
            }
        }
    }



    val testInput = readInput("Day8Test")
    checkDebug(part1(testInput, 10), 40)
    checkDebug(part2(testInput), 25272)

    val input = readInput("Day8")
    val time = measureTimeMillis {
        "part1: ${part1(input)}".println()
        "part2: ${part2(input)}".println()
    }
    kotlin.io.println(time)
}