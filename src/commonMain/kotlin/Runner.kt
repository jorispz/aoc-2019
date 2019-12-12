import day06.p06
import day07.p07
import day08.p08
import day09.p09
import day1.p01
import day10.p10
import day11.p011
import day12.p12
import day2.p02
import day3.p03
import day4.p04
import day5.p05
import p13.p13
import kotlin.math.sqrt

data class Puzzle(val day: Int, val part: Int? = null)

object Puzzles {

    private val puzzles = mapOf(
        Puzzle(1) to p01,
        Puzzle(2) to p02,
        Puzzle(3) to p03,
        Puzzle(4) to p04,
        Puzzle(5) to p05,
        Puzzle(6) to p06,
        Puzzle(7) to p07,
        Puzzle(8) to p08,
        Puzzle(9) to p09,
        Puzzle(10) to p10,
        Puzzle(11) to p011,
        Puzzle(12) to p12,
        Puzzle(13) to p13
    )

    suspend fun run(repeat: Int, day: Int, part: Int? = null) {
        val times = (1..repeat).map {
            measureNanos {
                val p = puzzles.getValue(Puzzle(day, part))
                p()
            }
        }

        val average = times.average()
        val std = sqrt(times.map { it.toDouble() - average }.map { it * it }.sum() / times.size)

        println("\nAverage: ${average / 1e3} ± ${std / 1e3}")
        println("Times: ${times.map { (it / 1e3).toInt() }.joinToString()} µs")
        println("\nAverage: ${average / 1e6} ± ${std / 1e6}")
        println("Times: ${times.map { (it / 1e6).toInt() }.joinToString()} ms")

    }

}

expect inline fun measureNanos(block: () -> Unit): Long
