# Advent of Code 2019 in Multi-Platform Kotlin

## Intro
In 2019, I will be participating in the [Advent of Code](https://adventofcode.com) using [Kotlin multi-platform](https://kotlinlang.org/docs/reference/multiplatform.html) code only. This means I can run my solutions on the JVM, on Node JS and natively on Windows through MinGW.

I will be measuring the performance of each platform by measuring the elapsed time calculating the solution `N` times:
```kotlin
val times = (1..repeat).map {
    measureNanos {
        Puzzles.run(day, part)
    }
}
```
where `repeat` is the number of desired repetitions, and `day` and `part` are used to select the proper challenge.

Note that the `measureNanos` function is the first example of functionality that isn't available in common code, since measuring time is platform-specific. The function is implemented using Kotlin's `expext/actual` mechanism.

`Runner.kt` defines its expectation of a function `measureNanos` for each supported platform with the signature:
```kotlin
expect inline fun measureNanos(block: () -> Unit): Long
```

Each platform provides an actual implementation in files called `Actuals.kt`. On the JVM and native, we can use `measureNanoTime` as provided by the Kotlin standard library on these platforms:

JVM and Native:
```kotlin
actual inline fun measureNanos(block: () -> Unit) = measureNanoTime(block)
```

On Node, we need to write our own implementation:
```kotlin
actual inline fun measureNanos(block: () -> Unit): Long {
    val start = process.hrtime()
    block()
    val end = process.hrtime(start)
    val nanos = (end[0] * 1e9 + end[1]) as Double
    return nanos.roundToLong()
}
```

Usually, I try to finish a puzzle as quickly as possible, then spend some time to cleanup and optimise the code. If and when these optimizations alter the performance, I'll update the measurements below to reflect the new solution.

# Important
There is a very explicit warning in the [FAQ of Kotlin/Native](https://github.com/JetBrains/kotlin-native/blob/master/RELEASE_NOTES.md#performance) that says the current version of Kotlin/Native is not suited for performance analysis, as it has not been optimised in any way for performance and benchmarking. 

Apart from that, all measurements were taken on my laptop, under non-controlled circumstances using non-optimized code and tools. So, **if you use the results below for anything important, you're insane...**



# Day 01
Last year, most days showed a similar pattern, with the JVM beating JS by a factor or two, and native being an order of magnitude slower than either. 

This year's first puzzle is an interesting deviation from that pattern, with native beating JS. 

What's really weird is how the the first run of the native solution is very fast, while later iterations are an order of magnitude slower.

| Platform | Average (µs) | Measurements (µs) |
| ---------| ------------:|------------------:|
| JVM      | 2333 ± 35341 | `36888, 1539, 1665, 2358, 1270, 1392, 1104, 1138, 1018,  779,  785,  790,  931,  649,  612,  709,  532,  502,  497,  521,  361,  517,  557,  543,  647` |
| Node JS  | 1846 ± 9899  | ` 5310, 3450, 6375, 8137, 1849, 1429, 1131,  897,  857,  972, 2664, 2122,  845,  679,  686,  476,  489,  642,  572,  413, 1507,  611, 3033,  598,  401` |
| MinGW64  | 2746 ± 6130  | ` 3742, 4797, 5268, 3800, 4935, 2203, 2150, 1828, 3374, 1577, 2342, 1932, 3117, 1734, 5184, 1413, 2375, 1377, 3232, 1327, 2872, 1948, 2450, 1335, 2339` | 

