fun main() {
    fun parse(input: List<String>): List<Pair<Long, Long>> {
        val line = input[0]
        return line.split(",").map { range ->
            val (start, end) = range.trim().split("-").map { it.trim().toLong() }
            start to end
        }
    }

    //divides by 11, 101, 1001 and checks when
    fun Long.isRepeat(repetition: Int = 2): Boolean {
        val string = this.toString()
        if(string.length % repetition != 0) return false
        if(string.length < 2) return false

        val segmentLength = string.length / repetition
        val segments = (0 until repetition).map {
            val start = it * segmentLength
            string.substring(start, start + segmentLength)
        }

        return segments.distinct().size == 1
//        var exponent = 1L;
//        while (true) {
//            val smallerExponent = exponent / 2
//            val base = 10.0.pow(exponent.toDouble())
//            val divisor = base + 10.0.pow(smallerExponent.toDouble())
//
//            if(base > this) return false
//            val multiple = ceil(this / (divisor))
//            if(this % divisor.toLong() == 0L && multiple == this % base && floor(log10(this.toDouble())).roundToInt() % 2 == 1) return true
//
//            exponent += 2
//        }
    }

    fun part1(input: List<String>): Long {
        val ranges = parse(input)
        val invalidIDs = ranges.flatMap { (a, b) ->
            val repeatNumbers = (a..b).filter { it.isRepeat() }
            repeatNumbers
        }

        return invalidIDs.sum()
    }

    fun part2(input: List<String>): Long {
        val ranges = parse(input)
        val invalidIDs = ranges.flatMap { (a, b) ->
            val repeatNumbers = (a..b).filter { number ->
                (2..number.toString().length).any {
                    number.isRepeat(it)
                }
            }
            repeatNumbers
        }

        return invalidIDs.sum()
    }



    val testInput = readInput("Day2Test")
    checkDebug(part1(testInput), 1227775554)
    checkDebug(part2(testInput), 4174379265)

    val input = readInput("Day2")
    "part1: ${part1(input)}".println()
    "part2: ${part2(input)}".println()
}