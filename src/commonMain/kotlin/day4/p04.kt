package day4

import print

val p04 = fun() {

    fun Int.isValidPassword(): Boolean {
        val pairs = this.toString().asSequence().windowed(2)
        val hasDoubleDigit = pairs.any { it[0] == it[1] }
        val neverDecreasing = pairs.none { it[1] < it[0] }
        return hasDoubleDigit && neverDecreasing
    }

    (356261..846303).count { it.isValidPassword() }.print()

    fun Int.groupedDigits(): List<List<Char>> {
        val result = mutableListOf(mutableListOf<Char>())
        this.toString().forEach {
            if (result.last().contains(it)) {
                result.last().add(it)
            } else {
                result.add(mutableListOf(it))
            }
        }
        return result.drop(1)
    }

    fun Int.isValidPassword2(): Boolean {
        val groups = this.groupedDigits()
        val hasIsolatedPair = groups.any { it.size == 2 }
        val everIncreasing = groups.windowed(2).all { it[0].first() < it[1].first() }
        return hasIsolatedPair && everIncreasing
    }

    (356261..846303).count { it.isValidPassword2() }.print()

}