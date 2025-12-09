import kotlin.math.absoluteValue
import kotlin.math.sign
import kotlin.system.measureTimeMillis

fun main() {
    fun parse(input: List<String>): List<Pair<Int, Int>> {
        return input.map { line ->
            val (a, b) = line.split(",").map { it.trim().toInt() }
            a to b
        }
    }

    fun calculateArea(a: Point, b: Point): Long {
        val deltaX = (a.first - b.first).absoluteValue + 1L
        val deltaY = (a.second - b.second).absoluteValue + 1L
        val area = deltaX * deltaY
        return area
    }
    fun part1(input: List<String>): Long {
        val pairs = parse(input)

        val areas = mutableListOf<Long>()
        pairs.forEachIndexed { index, pair ->
            ((index + 1) until pairs.size).forEach { secondPairIndex ->
                val secondPair = pairs[secondPairIndex]
                if(pair == secondPair) return@forEach
                val area = calculateArea(pair, secondPair)
                areas.add(area)
            }
        }

        return areas.max()
    }

    data class Ranges(val columns: List<Pair<Int, IntRange>>, val rows: List<Pair<Int, IntRange>>)
    fun List<Pair<Int, Int>>.createRanges(): Ranges {
        var firstTile = this.last()
        val columns = mutableListOf<Pair<Int, IntRange>>()
        val rows = mutableListOf<Pair<Int, IntRange>>()
        for(secondTile in this) {
            if(firstTile.first == secondTile.first) {
                val range = listOf(firstTile.second, secondTile.second).sorted().let { (a, b) -> a..b }
                val column = secondTile.first to range
                columns.add(column)
            } else {
                val range = listOf(firstTile.first, secondTile.first).sorted().let { (a, b) -> a..b }
                val row = secondTile.second to range
                rows.add(row)
            }

            firstTile = secondTile
        }

        columns.sortBy { it.first }
        rows.sortBy { it.first }
        return Ranges(columns, rows)
    }


    fun Tuple<Point>.toUnitVector(): Point {
        val a = this.first
        val b = this.second
        //if x coords are the same => we travel vertically
        if(a.first == b.first) return 0 to (b.second - a.second).sign
        //if y coords are the same => we travel horizontally
        if(a.second == b.second) return (b.first - a.first).sign to 0
        error("Unreachable, points are assumed to share at least one coordinate")
    }

    fun Tuple<Point>.getTurnTo(other: Tuple<Point>): Turn {
        val a = this.toUnitVector()
        val b = other.toUnitVector()
        //Ax * By - Ay * Bx
        val crossProduct = a.first * b.second - a.second * b.first
        return when {
            crossProduct > 0 -> Turn.CounterClockWise
            crossProduct < 0 -> Turn.ClockWise
            else -> Turn.Straight
        }
    }

    //1  0        0   1    1  0  -1  0
    //0  1       -1   0    0 -1   0  1
    @Suppress("KotlinConstantConditions")
    fun Point.turn(turn: Turn): Point {
        val a =  0; var b: Int
        var c: Int; val d = 0
        when (turn) {
            Turn.CounterClockWise -> { c =  1; b = -1 }
            Turn.ClockWise -> { c = -1; b =  1 }
            Turn.Straight -> return this
        }

        val x = first
        val y = second
        val xCoord = a * x + b * y
        val yCoord = c * x + d * y
        return Point(xCoord, yCoord)
    }


    class Edge(edge: Tuple<Point>, turn: Turn) {
        val inside: Point
        init {
            val unitVector = edge.toUnitVector()
            inside = unitVector.turn(turn)
        }
    }

    class Vertex(val location: Point, from: Edge, to: Edge) {
        val inside = location + (from.inside + to.inside)
    }
    fun List<Pair<Int, Int>>.findInside(): List<Vertex> {
        var previousTile = this.last()
        val edges = this.map { tile ->
            val edge = previousTile to tile
            previousTile = tile
            edge
        }

        val loop = edges + edges.first()
        val turns = loop.windowed(2).map { (a, b) ->
            val turn = a.getTurnTo(b)
            turn
        }

        val turnValue = turns.sumOf { it.value }
        val turnDirection = when(turnValue) {
            4 -> Turn.CounterClockWise
            -4 -> Turn.ClockWise
            else -> error("To form a loop there must be 4 more CCW than CW or vice versa")
        }

        val vertices = loop.windowed(2).map { (edgeA, edgeB) ->
            if(edgeA.second != edgeB.first) error("edges don't start/end on same vertex")
            val a = Edge(edgeA, turnDirection)
            val b = Edge(edgeB, turnDirection)
            Vertex(edgeA.second, a, b)
        }
        return vertices
    }

    fun Ranges.hasLinesThrough(x: IntRange, y: IntRange): Boolean {
        val firstY = this.rows.indexOfFirst { it.first >= y.first }
        val secondY = this.rows.indexOfLast { it.first <= y.last }
        val hasLinesThroughHorizontal = (firstY..secondY).any { index ->
            val (_, range) = this.rows[index]
            val isOutside = range.endInclusive < x.start || x.endInclusive < range.start
            !isOutside
        }

        if(hasLinesThroughHorizontal) return true

        val firstX = this.columns.indexOfFirst { it.first >= x.first }
        val secondX = this.columns.indexOfLast { it.first <= x.last }
        val hasLinesThroughVertical = (firstX..secondX).any { index ->
            val (_, range) = this.columns[index]
            val isOutside = range.endInclusive < y.start || y.endInclusive < range.start
            !isOutside
        }

        return hasLinesThroughVertical
    }

    data class Area(val value: Long, val a: Vertex, val b: Vertex)
    fun Point.isInRanges(xRange: IntRange, yRange: IntRange) = this.first in xRange && this.second in yRange
    fun part2(input: List<String>): Long {
        val pairs = parse(input)
        val vertices = pairs.findInside()
        val ranges = pairs.createRanges()
        val validAreas = mutableListOf<Area>()
        vertices.forEachIndexed { index, firstVertex ->
            ((index + 1) until pairs.size).forEach { secondVertexIndex ->
                val secondVertex = vertices[secondVertexIndex]

                val controlPoints = listOf(firstVertex.inside, secondVertex.inside)
                val isLine = firstVertex.location.first == secondVertex.location.first
                    || firstVertex.location.second == secondVertex.location.second

                val xRange = listOf(firstVertex.location.first, secondVertex.location.first)
                    .sorted().let { (a, b) -> (a + 1)..(b - 1) }
                val yRange = listOf(firstVertex.location.second, secondVertex.location.second)
                    .sorted().let { (a, b) -> (a + 1)..(b - 1) }

                if(isLine) return@forEach
                //all control points are in the bigger shape, but not all must be inside the rectangle
                if(!controlPoints.any { it.isInRanges(xRange, yRange) }) return@forEach
                if(vertices.any { it.location.isInRanges(xRange, yRange) }) return@forEach

                if(ranges.hasLinesThrough(xRange, yRange)) return@forEach

                val area = calculateArea(firstVertex.location, secondVertex.location)
                validAreas.add(Area(area, firstVertex, secondVertex))
            }
        }

        return validAreas.maxBy { it.value }.value
    }



    val testInput = readInput("Day9Test")
    checkDebug(part1(testInput), 50)
    checkDebug(part2(testInput), 24)

    val input = readInput("Day9")
    val time = measureTimeMillis {
        "part1: ${part1(input)}".println()
        "part2: ${part2(input)}".println()
    }
    println("${time}ms")
}

enum class Turn(val value: Int) {
    Straight(0),
    CounterClockWise(1),
    ClockWise(-1)
}