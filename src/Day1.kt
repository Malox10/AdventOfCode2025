fun main() {
    fun parse(input: List<String>) {

    }

    fun part1(input: List<String>): Int {
        val x = parse(input)
        return input.size
    }

    fun part2(input: List<String>): Int {
        val x = parse(input)
        return input.size
    }



    val testInput = readInput("Day1Test")
    checkDebug(part1(testInput), 3)
//    checkDebug(part2(testInput), 1)

    val input = readInput("Day1")
    "part1: ${part1(input)}".println()
    "part2: ${part2(input)}".println()
}