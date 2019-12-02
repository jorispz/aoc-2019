package day2

val p02 = fun() {
    val testInput = "1,9,10,3,2,3,11,0,99,30,40,50"
    val program = input.split(",").withIndex().associate { it.index to it.value.toInt() }
    //19690720
    println(run(program, 77, 49))
    println(100 * 77 + 49)
}

fun run(program: Map<Int, Int>, first: Int, second: Int): Int {
    val p = program.toMutableMap()
    p[1] = first;
    p[2] = second
    var current = 0;
    do {
        val instr = p[current]
        when (instr) {
            1 -> p[p[current + 3]!!] = p[p[current + 1]!!]!! + p[p[current + 2]!!]!!
            2 -> p[p[current + 3]!!] = p[p[current + 1]!!]!! * p[p[current + 2]!!]!!
            99 -> null
            else -> throw IllegalArgumentException()
        }
        current += 4
    } while (instr != 99)
    return p[0]!!
}

val input =
    "1,0,0,3,1,1,2,3,1,3,4,3,1,5,0,3,2,9,1,19,1,19,5,23,1,23,6,27,2,9,27,31,1,5,31,35,1,35,10,39,1,39,10,43,2,43,9,47,1,6,47,51,2,51,6,55,1,5,55,59,2,59,10,63,1,9,63,67,1,9,67,71,2,71,6,75,1,5,75,79,1,5,79,83,1,9,83,87,2,87,10,91,2,10,91,95,1,95,9,99,2,99,9,103,2,10,103,107,2,9,107,111,1,111,5,115,1,115,2,119,1,119,6,0,99,2,0,14,0"