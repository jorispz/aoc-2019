import kotlin.math.roundToLong

external val process: dynamic

actual inline fun measureNanos(block: () -> Unit): Long {
    val start = process.hrtime()
    block()
    val end = process.hrtime(start)
    val nanos = (end[0] * 1e9 + end[1]) as Double
    return nanos.roundToLong()
}

suspend fun main() {
    val arguments = (process["argv"] as Array<String>).drop(2)
    val repeat = arguments[0].toInt()
    val day = arguments[1].toInt()
    val part = if (arguments.size == 3) arguments[2].toInt() else null
    Puzzles.run(repeat, day, part)
}