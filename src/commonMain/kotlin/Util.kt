import kotlin.math.abs

fun <T> T.print(msg: (T) -> Any? = { it }): T = this.apply { println(msg(this)) }


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

    val left by lazy {
        Position(x - 1, y)
    }
    val right by lazy {
        Position(x, y + 1)
    }
    val up by lazy {
        Position(x, y - 1)
    }
    val down by lazy {
        Position(x, y + 1)
    }

    fun adjacents(): List<Position> = listOf(left, up, right, down)

    private fun distanceTo(other: Position) = abs(x - other.x) + abs((y - other.y))

    fun adjacentTo(other: Position) = distanceTo(other) == 1


}

operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>) = Pair(this.first + other.first, this.second + other.second)
operator fun Triple<Int, Int, Int>.plus(other: Triple<Int, Int, Int>) =
    Triple(this.first + other.first, this.second + other.second, this.third + other.third)

enum class Turn {
    LEFT, STRAIGHT, RIGHT, REVERSE
}

enum class Heading {
    N, S, E, W;

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

fun <T> Sequence<T>.infinite() = sequence {
    while (true) {
        yieldAll(this@infinite)
    }
}
