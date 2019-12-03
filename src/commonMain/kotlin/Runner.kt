import day1.p01
import day2.p02
import day3.p03
import kotlin.math.sqrt

data class Puzzle(val day: Int, val part: Int? = null)

object Puzzles {

    private val puzzles = mapOf(
        Puzzle(1) to p01,
        Puzzle(2) to p02,
        Puzzle(3) to p03
    )

    fun run(repeat: Int, day: Int, part: Int? = null) {
        val times = (1..repeat).map {
            measureNanos {
                puzzles[Puzzle(day, part)]!!()
            }
        }

        val average = times.average()
        val std = sqrt(times.map { it.toDouble() - average }.map { it * it }.sum())

        println("\nAverage: ${average / 1e3} ± ${std / 1e3}")
        println("Times: ${times.map { (it / 1e3).toInt() }.joinToString()} µs")
        println("\nAverage: ${average / 1e6} ± ${std / 1e6}")
        println("Times: ${times.map { (it / 1e6).toInt() }.joinToString()} ms")

    }

}

expect inline fun measureNanos(block: () -> Unit): Long
