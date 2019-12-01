import day1.p01

data class Puzzle(val day: Int, val part: Int? = null)

object Puzzles {

    private val puzzles = mapOf(
        Puzzle(1) to p01
    )

    fun run(repeat: Int, day: Int, part: Int? = null) {
        val times = (1..repeat).map {
            measureNanos {
                puzzles[Puzzle(day, part)]!!()
            }
        }

        println("\nAverage: ${times.average() / 1e6}")
        println("Times: ${times.map { (it / 1e6).toInt() }.joinToString("|")} ms")

    }

}

expect inline fun measureNanos(block: () -> Unit): Long
