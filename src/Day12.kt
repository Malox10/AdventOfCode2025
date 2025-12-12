fun main() {
    data class Board(val width: Int, val height: Int, val counts: List<Int>)
    data class Puzzle(val shapes: List<List<List<Boolean>>>, val board: List<Board>)
    fun parse(input: List<String>): Puzzle {
        val parts = input.joinToString("\n").split("\n\n")
        val boardString = parts.last().lines()
        val boards = boardString.map { board ->
            val (dimensions, counts) = board.split(":").map { it.trim() }
            val (width, height) = dimensions.split("x").map { it.trim().toInt() }

            Board(width, height, counts.split(" ").map { it.trim().toInt() })
        }

        val pieces = parts.dropLast(1).map { piece ->
            val parsedPiece = piece.lines().drop(1).map { line ->
                line.map { it == '#' }
            }
            parsedPiece
        }

        return Puzzle(pieces, boards)
    }

    fun part1(input: List<String>): Int {
        val puzzle = parse(input)
        val puzzleCounts = puzzle.shapes.map { shape ->
            shape.flatten().count { it }
        }

        val possible = mutableListOf<Board>()
        val impossible = mutableListOf<Board>()
        val undecided = puzzle.board.filter { board ->
            val squares = (board.width / 3) * (board.height / 3)
            val threeByThreeSquares = board.counts.sum()
            if(threeByThreeSquares <= squares) {
                possible.add(board)
                return@filter false
            }
            val squareCounts = board.counts.zip(puzzleCounts).map { (a, b) -> a * b }
            if(squareCounts.sum() > (board.width * board.height)) {
                impossible.add(board)
                return@filter false
            }


            true
        }
        if(undecided.isNotEmpty()) error("Tough luck buddy")
        return possible.size
    }




//    val testInput = readInput("Day12Test")
//    checkDebug(part1(testInput), 2) //xdd

    val input = readInput("Day12")
    "part1: ${part1(input)}".println()
}