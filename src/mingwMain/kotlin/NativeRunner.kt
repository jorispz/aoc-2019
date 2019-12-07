import kotlinx.coroutines.runBlocking
import kotlin.system.measureNanoTime

actual inline fun measureNanos(block: () -> Unit) = measureNanoTime(block)
fun main(args: Array<String>) {
    // Only single-threaded coroutine support for now
    // See https://github.com/Kotlin/kotlinx.coroutines/issues/462
    runBlocking {
        val repeat = args[0].toInt()
        val day = args[1].toInt()
        val part = if (args.size == 3) args[2].toInt() else null
        Puzzles.run(repeat, day, part)
    }
}