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

    val arrayLength = 12
    fun List<Int>.maximumJoltagePart2(prefix: String = "", depth: Int = 1): Long {
        val remainingLength = arrayLength - depth
        if(remainingLength < 0) return prefix.toLong()

        val leadDigits = this.take(this.size - remainingLength)
        val highestDigit = leadDigits.max()
        val index = leadDigits.indexOfFirst { it == highestDigit }

        val remainingDigits = this.drop(index + 1)
        return remainingDigits.maximumJoltagePart2("$prefix$highestDigit", depth + 1)
    }

    fun part2(input: List<String>): Long {
        val banks = parse(input)
        val joltages = banks.map {
            it.maximumJoltagePart2()
        }
        return joltages.sum()
    }



    val testInput = readInput("Day3Test")
    checkDebug(part1(testInput), 357)
    checkDebug(part2(testInput), 3121910778619)

    val input = readInput("Day3")
    "part1: ${part1(input)}".println()
    "part2: ${part2(input)}".println()
}