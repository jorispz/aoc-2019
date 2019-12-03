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

This year's first puzzle shows a very comparable performance between JVM and JS, with native being much slower. 


| Platform | Average (µs)           | Measurements (µs) |
| ---------| ----------------------:|------------------:|
| JVM      | 2333&nbsp;±&nbsp;35341 | `36888, 1539, 1665, 2358, 1270, 1392, 1104, 1138, 1018,  779,  785,  790,  931,  649,  612,  709,  532,  502,  497,  521,  361,  517,  557,  543,  647` |
| Node JS  | 1846 ± 9899            | ` 5310, 3450, 6375, 8137, 1849, 1429, 1131,  897,  857,  972, 2664, 2122,  845,  679,  686,  476,  489,  642,  572,  413, 1507,  611, 3033,  598,  401` |
| MinGW64  | 2746 ± 6130            | ` 3742, 4797, 5268, 3800, 4935, 2203, 2150, 1828, 3374, 1577, 2342, 1932, 3117, 1734, 5184, 1413, 2375, 1377, 3232, 1327, 2872, 1948, 2450, 1335, 2339` | 


# Day 02
The solution for day 2 is very overdesigned, mostly because part 2 strongly hints that this is the first of several puzzles involving emulating this computer. So the code isn't the most performant, but hopefully it
will help later in the month!

JS and native perform dramatically worse today than the JVM:


| Platform | Average (ms)         | Measurements (ms) |
| ---------| --------------------:|------------------:|
| JVM      |  184&nbsp;±&nbsp;234 | ` 403,  228,  201,  174,  175,  173,  168,  171,  165,  168,  170,  176,  173,  171,  169,  166,  169,  170,  165,  169,  164,  166,  174,  192,  177` |
| Node JS  | 8375 ± 2512          | `8132, 8235, 8443, 8046, 8058, 7936, 8541, 8578, 8688, 8469, 8981, 8159, 8620, 8453, 7882, 7711, 7706, 8452, 9847, 9585, 8349, 8357, 8203, 8114, 7825` |
| MinGW64  | 5746 ± 2479          | `5308, 5253, 5163, 5059, 5389, 6387, 5259, 6118, 5426, 6653, 5217, 5787, 6156, 6182, 6006, 5699, 6813, 5716, 6608, 5436, 5476, 5983, 5760, 5380, 5417` | 

# Day 03

| Platform | Average (ms)         | Measurements (ms) |
| ---------| --------------------:|------------------:|
| JVM      |  210&nbsp;±&nbsp;185 | ` 373,  228,  203,  202,  210,  189,  183,  219,  192,  189,  192,  195,  204,  190,  190,  182,  195,  193,  252,  218,  213,  193,  216,  226,  193` |
| Node JS  | 1220 ± 547           | `1722, 1218, 1213, 1183, 1190, 1193, 1183, 1195, 1324, 1175, 1173, 1179, 1198, 1230, 1204, 1183, 1188, 1312, 1171, 1170, 1175, 1192, 1180, 1168, 1181` |
| MinGW64  | 1941 ± 6489          | `8297, 1669, 1792, 1797, 1665, 1663, 1671, 1677, 1655, 1653, 1665, 1663, 1671, 1703, 1660, 1671, 1663, 1657, 1660, 1674, 1651, 1654, 1666, 1660, 1673` | 

