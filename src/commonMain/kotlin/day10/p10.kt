package day10

import Position
import gcd
import print
import without
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan


class LineOfSight(val origin: Position, other: Position) {
    private val dX: Int
    private val dY: Int
    // Angle of line of sight wrt positive Y-axis.
    // Straight up is zero, clockwise is positive angle up to 2*PI
    val phi: Double
        get() = atan(
            if (dY == 0) {
                if (dX > 0) Double.POSITIVE_INFINITY else Double.NEGATIVE_INFINITY
            } else {
                dX.toDouble() / dY
            }
        ) + if (dX >= 0 && dY > 0) {
            // Quadrant upper right
            0 * PI
        } else if (dY < 0) {
            // Lower quadrant
            PI
        } else {
            // Upper left
            2 * PI
        }


    init {
        val gcd = abs(gcd(other.x - origin.x, other.y - origin.y))
        dX = (other.x - origin.x) / gcd
        dY = (other.y - origin.y) / gcd
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as LineOfSight

        if (origin != other.origin) return false
        if (dX != other.dX) return false
        if (dY != other.dY) return false

        return true
    }

    override fun hashCode(): Int {
        var result = origin.hashCode()
        result = 31 * result + dX
        result = 31 * result + dY
        return result
    }


}

// 1: 286
// 2: 504
val p10 = suspend {
    val asteroids = input.lines().withIndex().flatMap { l ->
        val y = -l.index
        l.value.withIndex().filter { it.value == '#' }.map { Position(it.index, y) }
    }

    val station = asteroids.associateWith { origin ->
        asteroids.without(origin).groupBy { asteroid -> LineOfSight(origin, asteroid) }
    }.maxBy { it.value.count() }!!

    station.value.count().print { "Part 1: $it" }

    val zapped = sequence {
        val origin = station.key
        val asteroidsLeft = mutableMapOf<LineOfSight, List<Position>>().also { al ->
            station.value.forEach {
                al[it.key] = it.value.sortedBy { it.distanceTo(origin) }
            }
        }
        while (asteroidsLeft.isNotEmpty()) {
            asteroidsLeft.keys.sortedBy { it.phi }.forEach { l ->
                val asts = asteroidsLeft.getValue(l)
                val left = asts.drop(1)
                if (left.isEmpty()) {
                    asteroidsLeft.remove(l)
                } else {
                    asteroidsLeft.put(l, left)
                }
                yield(asts.first())
            }
        }
    }
    zapped.take(199).last().print { it.x * 100 - it.y }

}

val test = """.#..##.###...#######
##.############..##.
.#.######.########.#
.###.#######.####.#.
#####.##.#.##.###.##
..#####..#.#########
####################
#.####....###.#.#.##
##.#################
#####.##.###..####..
..######..##.#######
####.##.####...##..#
.#####..#.######.###
##...#.##########...
#.##########.#######
.####.#.###.###.#.##
....##.##.###..#####
.#.#.###########.###
#.#.#.#####.####.###
###.##.####.##.#..##""".trimMargin()

val input = """.#.####..#.#...#...##..#.#.##.
..#####.##..#..##....#..#...#.
......#.......##.##.#....##..#
..#..##..#.###.....#.#..###.#.
..#..#..##..#.#.##..###.......
...##....#.##.#.#..##.##.#...#
.##...#.#.##..#.#........#.#..
.##...##.##..#.#.##.#.#.#.##.#
#..##....#...###.#..##.#...##.
.###.###..##......#..#...###.#
.#..#.####.#..#....#.##..#.#.#
..#...#..#.#######....###.....
####..#.#.#...##...##....#..##
##..#.##.#.#..##.###.#.##.##..
..#.........#.#.#.#.......#..#
...##.#.....#.#.##........#..#
##..###.....#.............#.##
.#...#....#..####.#.#......##.
..#..##..###...#.....#...##..#
...####..#.#.##..#....#.#.....
####.#####.#.#....#.#....##.#.
#.#..#......#.........##..#.#.
#....##.....#........#..##.##.
.###.##...##..#.##.#.#...#.#.#
##.###....##....#.#.....#.###.
..#...#......#........####..#.
#....#.###.##.#...#.#.#.#.....
.........##....#...#.....#..##
###....#.........#..#..#.#.#..
##...#...###.#..#.###....#.##."""