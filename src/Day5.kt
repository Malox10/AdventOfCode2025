fun main() {
    fun parse(input: List<String>): Pair<List<LongRange>, List<Long>> {
        val (start, end) = input.joinToString("\n").split("\n\n")
        val ranges = start.lines().map { line ->
            val (a, b) = line.split("-").map { it.trim() }
            a.toLong()..b.toLong()
        }
        return ranges to end.lines().map { it.toLong() }
    }

    fun part1(input: List<String>): Int {
        val (ranges, seeds) = parse(input)
        val unspoiled = seeds.filter { seed ->
            ranges.any {
                it.contains(seed)
            }
        }
        return unspoiled.count()
    }

    fun part2(input: List<String>): Int {
        val x = parse(input)
        return input.size
    }



    val testInput = readInput("Day5Test")
    checkDebug(part1(testInput), 3)
//    checkDebug(part2(testInput), 1)

    val input = readInput("Day5")
    "part1: ${part1(input)}".println()
    "part2: ${part2(input)}".println()
}