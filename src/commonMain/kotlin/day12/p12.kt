package day12

import gcd
import print
import without
import kotlin.math.abs
import kotlin.math.sign

data class Vector(val x: Int, val y: Int, val z: Int) {
    val energy = abs(x) + abs(y) + abs(z)

    operator fun plus(v: Vector) = Vector(x + v.x, y + v.y, z + v.z)
    override fun toString(): String {
        return "($x, $y, $z)"
    }
}

typealias Projection = Vector.() -> Int


// 1: 12490
// 2:
val p12 = suspend {
    val pattern = Regex("""<x=(-?\d.*), y=(-?\d.*), z=(-?\d.*)>""")
    val initial = input.lines().map {
        val (x, y, z) = pattern.matchEntire(it)!!.groupValues.drop(1)
        Pair(Vector(x.toInt(), y.toInt(), z.toInt()), Vector(0, 0, 0))
    }


    fun List<Pair<Vector, Vector>>.update() = this.map { moon ->
        val (pos, vel) = moon
        val newVel = this.without(moon).fold(vel) { v, m ->
            v + Vector(
                (m.first.x - pos.x).sign,
                (m.first.y - pos.y).sign,
                (m.first.z - pos.z).sign
            )
        }
        val newPos = pos + newVel
        Pair(newPos, newVel)
    }

    fun List<Pair<Vector, Vector>>.energy() = this.sumBy { it.first.energy * it.second.energy }

    val timeline = generateSequence(initial) { it.update() }

    timeline.drop(1).take(1000).last().print { "Part 1: ${it.energy()}" }

    fun findPeriod(p: Projection): Long {
        val target = initial.map { Pair(it.first.p(), it.second.p()) }
        return (timeline.drop(1).takeWhile {
            it.map {
                Pair(
                    it.first.p(),
                    it.second.p()
                )
            } != target
        }.count() + 1).toLong()
    }

    val periodX = findPeriod { this.x }
    val periodY = findPeriod { this.y }
    val periodZ = findPeriod { this.z }

    fun leastCommonMultiple(a: Long, b: Long, c: Long) = ((a * b) / gcd(a, b)).let { abs(it) * c / gcd(abs(it), c) }

    leastCommonMultiple(periodX, periodY, periodZ).print { "Part 2: $it" }

    timeline.map { it.map { it.first } }.filter { it.distinct().size != 4 }.first().print()

}

val test = """<x=-1, y=0, z=2>
<x=2, y=-10, z=-7>
<x=4, y=-8, z=8>
<x=3, y=5, z=-1>"""

val input = """<x=8, y=0, z=8>
<x=0, y=-5, z=-10>
<x=16, y=10, z=-5>
<x=19, y=-10, z=-7>"""