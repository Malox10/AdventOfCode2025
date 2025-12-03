fun main() {
    fun parse(input: List<String>): List<List<Int>> {
        return input.map { line -> line.map { it.digitToInt() } }
    }

    fun List<Int>.maximumJoltage(): Int {
        var highestDigit = 9
        while(true) {
            val index = this.indexOfFirst { it == highestDigit }
            if(index == (this.size - 1) || index == -1) {
                highestDigit--
                continue
            }
            val secondDigit = this.drop(index + 1).max()
            return "$highestDigit$secondDigit".toInt()
        }
    }

    fun part1(input: List<String>): Int {
        val banks = parse(input)
        val joltages = banks.map {
            it.maximumJoltage()
        }
        return joltages.sum()
    }

    fun part2(input: List<String>): Int {
        val x = parse(input)
        return input.size
    }



    val testInput = readInput("Day3Test")
    checkDebug(part1(testInput), 357)
//    checkDebug(part2(testInput), 1)

    val input = readInput("Day3")
    "part1: ${part1(input)}".println()
    "part2: ${part2(input)}".println()
}