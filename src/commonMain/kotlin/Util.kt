import kotlin.math.abs

inline fun <T> T.print(msg: (T) -> Any? = { it }): T = this.apply { println(msg(this)) }


fun String.sorted(): String = this.toList().sorted().joinToString()

fun <T> List<T>.without(element: T): List<T> = this.filter { it != element }
fun <T> List<T>.replaceElementAt(index: Int, newValue: T): List<T> =
    this.toMutableList().apply { this[index] = newValue }

fun <T> Collection<T>.whenNotEmpty(block: (Collection<T>) -> Unit) {
    if (this.isNotEmpty()) {
        block(this)
    }
}

fun <T : Comparable<T>> Iterable<T>.maxWithIndex(): IndexedValue<T>? = this.withIndex().maxBy { it.value }

fun <T> MutableSet<T>.takeFirst(): T = this.first().also { remove(it) }
//fun <T> MutableList<T>.takeFirst(): T = this.first().also { removeAt(0) }

data class Position(val x: Int, val y: Int) {

    companion object {
        val ORIGIN = Position(0, 0)
    }

    val left by lazy {
        Position(x - 1, y)
    }
    val right by lazy {
        Position(x + 1, y)
    }
    val up by lazy {
        Position(x, y - 1)
    }
    val down by lazy {
        Position(x, y + 1)
    }

    fun headingTo(other: Position) = when (other) {
        this.up -> Heading.N
        this.down -> Heading.S
        this.left -> Heading.W
        this.right -> Heading.E
        else -> throw IllegalArgumentException("Not adjacent")
    }

    fun headingFrom(other: Position) = headingTo(other).reverse()

    fun move(h: Heading) = when (h) {
        Heading.N -> up
        Heading.S -> down
        Heading.W -> left
        Heading.E -> right
    }

    fun adjacents(): List<Position> = listOf(left, up, right, down)

    fun distanceTo(other: Position) = abs(x - other.x) + abs((y - other.y))

    fun adjacentTo(other: Position) = distanceTo(other) == 1

    fun closes(a: Position, b: Position): Position {
        val distA = this.distanceTo(a)
        val distB = this.distanceTo(b)
        return if (distB < distA) b else a
    }


}

operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>) = Pair(this.first + other.first, this.second + other.second)
operator fun Triple<Int, Int, Int>.plus(other: Triple<Int, Int, Int>) =
    Triple(this.first + other.first, this.second + other.second, this.third + other.third)

enum class Turn {
    LEFT, STRAIGHT, RIGHT, REVERSE
}

enum class Heading {
    N, S, E, W;

    companion object {
        fun from(s: String) = when (s.toUpperCase()) {
            "N" -> N
            "U" -> N
            "S" -> S
            "D" -> S
            "W" -> W
            "L" -> W
            "E" -> E
            "R" -> E
            else -> throw IllegalArgumentException()
        }
    }

    fun right(): Heading = when (this) {
        N -> E
        E -> S
        S -> W
        W -> N
    }

    fun left(): Heading = when (this) {
        N -> W
        W -> S
        S -> E
        E -> N
    }

    fun reverse(): Heading = when (this) {
        N -> S
        S -> N
        E -> W
        W -> E
    }
}

data class BoundingBox(val topLeft: Position, val bottomRight: Position) {
    fun render(block: (Position) -> Char) {
        println()
        (bottomRight.y..topLeft.y).map { y ->
            (topLeft.x..bottomRight.x).map { x ->
                block(Position(x, y))
            }.joinToString("").print()
        }
        println()
    }
}

fun Collection<Position>.boundingBox(): BoundingBox {
    val minX = this.minBy { it.x }?.x!!
    val maxX = this.maxBy { it.x }?.x!!
    val minY = this.minBy { it.y }?.y!!
    val maxY = this.maxBy { it.y }?.y!!
    return BoundingBox(Position(minX, maxY), Position(maxX, minY))
}


fun <T> Sequence<T>.infinite() = sequence {
    while (true) {
        yieldAll(this@infinite)
    }
}

fun IntRange.permute() = this.toList().permute()

fun <T> List<T>.permute(): List<List<T>> {
    if (this.size == 1) return listOf(this)
    val perms = mutableListOf<List<T>>()
    val toInsert = this[0]
    for (perm in this.drop(1).permute()) {
        for (i in 0..perm.size) {
            val newPerm = perm.toMutableList()
            newPerm.add(i, toInsert)
            perms.add(newPerm)
        }
    }
    return perms
}

tailrec fun gcd(a: Int, b: Int): Int {
    return if (b == 0) {
        a
    } else {
        gcd(b, a % b)
    }
}

tailrec fun gcd(a: Long, b: Long): Long {
    return if (b == 0L) {
        a
    } else {
        gcd(b, a % b)
    }
}

