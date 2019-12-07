import kotlin.system.measureNanoTime


suspend fun main(args: Array<String>) {
    val repeat = args[0].toInt()
    val day = args[1].toInt()
    val part = if (args.size == 3) args[2].toInt() else null
    Puzzles.run(repeat, day, part)
}

actual inline fun measureNanos(block: () -> Unit) = measureNanoTime(block)
