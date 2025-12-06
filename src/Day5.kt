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

    fun combineRanges(ranges: List<LongRange>): List<LongRange> {
        val remainingRanges = ranges.toMutableList()
        val currentRanges = emptyList<LongRange>().toMutableList()
        while(remainingRanges.isNotEmpty()) {
            val currentRange = remainingRanges.removeFirst()
            if(currentRanges.isEmpty()) {
                currentRanges.add(currentRange)
                continue
            }
            val iterator = currentRanges.iterator()

            var didCombine = false
            while(iterator.hasNext()) {
                val nextRangeToCheck = iterator.next()
                //if they are the same
                if(currentRange.start == nextRangeToCheck.start && currentRange.endInclusive == nextRangeToCheck.endInclusive) {
                    println(currentRange)
                    println(nextRangeToCheck)
                    didCombine = true
                    break
                }

                //if new range is completely inside the other range
                if(currentRange.start in nextRangeToCheck && currentRange.endInclusive in nextRangeToCheck) {
                    didCombine = true
                    break
                }

                //if new range is bigger than other range
                if(currentRange.start < nextRangeToCheck.start && nextRangeToCheck.endInclusive < currentRange.endInclusive) {
                    didCombine = true
                    iterator.remove()
                    currentRanges.add(currentRange)
                    break
                }
                //if new range is outside other range
                if(currentRange.start > nextRangeToCheck.endInclusive || currentRange.endInclusive < nextRangeToCheck.start) {
                    continue
                }
                if(currentRange.start in nextRangeToCheck && currentRange.endInclusive > nextRangeToCheck.endInclusive) {
                    didCombine = true
                    val newRange = nextRangeToCheck.start..currentRange.endInclusive
                    iterator.remove()
                    currentRanges.add(newRange)
                    break
                }
                if(currentRange.endInclusive in nextRangeToCheck && currentRange.start < nextRangeToCheck.start) {
                    didCombine = true
                    val newRange = currentRange.start..nextRangeToCheck.endInclusive
                    iterator.remove()
                    currentRanges.add(newRange)
                    break
                }
                error("Unreachable")
            }
            if(!didCombine) currentRanges.add(currentRange)

        }
        return currentRanges
    }

    fun part2(input: List<String>): Long {
        val (ranges, _) = parse(input)
        var previousSize: Int
        var rangesToCombine = ranges
        do {
            previousSize = rangesToCombine.size
            val combinedRanges = combineRanges(rangesToCombine)
            rangesToCombine = combinedRanges
        } while(previousSize != combinedRanges.size)

        return rangesToCombine.sumOf {  it.endInclusive - it.start + 1 }
    }

    val testInput = readInput("Day5Test")
    checkDebug(part1(testInput), 3)
    checkDebug(part2(testInput), 14)

    val input = readInput("Day5")
    "part1: ${part1(input)}".println()
    "part2: ${part2(input)}".println()
}