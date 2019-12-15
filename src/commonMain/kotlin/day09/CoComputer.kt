package day09

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.launch

enum class Instruction(val id: Int, val numParameters: Int) {
    PLUS(1, 3),
    TIMES(2, 3),
    READ(3, 1),
    WRITE(4, 1),
    JMP_IF_TRUE(5, 2),
    JMP_IF_EQ(6, 2),
    LESS_THAN(7, 3),
    EQUALS(8, 3),
    SHIFT_BASE(9, 1),
    HALT(99, 0)
}

val instructions = Instruction.values().associateBy { it.id }


fun CoroutineScope.launchComputer(program: String, input: ReceiveChannel<Long>, output: SendChannel<Long>): Job {
    val memory =
        program.split(",").withIndex().associateTo(mutableMapOf()) { it.index to it.value.toLong() }.withDefault { 0 }
    var pointer = 0
    var baseAddress = 0

    abstract class Parameter(val i: Long) {
        abstract fun read(): Long
        abstract fun write(j: Long)
    }

    class Immediate(i: Long) : Parameter(i) {
        override fun read() = i
        override fun write(j: Long) = throw IllegalArgumentException("Cannot write to parameter in Immediate mode")
    }

    class Position(i: Long) : Parameter(i) {
        override fun read() = memory.getValue(i.toInt())
        override fun write(j: Long) = memory.set(i.toInt(), j)
    }

    class Relative(i: Long) : Parameter(i) {
        override fun read() = memory.getValue(baseAddress + i.toInt())
        override fun write(j: Long) = memory.set(baseAddress + i.toInt(), j)
    }

    return launch {
        while (true) {
            val next = memory.getValue(pointer)
            val instructionID = next.rem(100).toInt()
            val instruction = instructions.getValue(instructionID)
            if (instruction == Instruction.HALT) {
                break
            } else {
                val modes = next.toString().dropLast(2).reversed().padEnd(instruction.numParameters, '0').toList()
                val args = (1..instruction.numParameters).map { memory.getValue(pointer + it) }
                val params = args.zip(modes).map {
                    when (it.second) {
                        '0' -> Position(it.first)
                        '1' -> Immediate(it.first)
                        '2' -> Relative(it.first)
                        else -> throw IllegalArgumentException("Unknown mode ${it.second}")
                    }
                }

                val jump = when (instruction) {
                    Instruction.PLUS -> null.also { params[2].write(params[0].read() + params[1].read()) }
                    Instruction.TIMES -> null.also { params[2].write(params[0].read() * params[1].read()) }
                    Instruction.READ -> null.also { params[0].write(input.receive()) }
                    Instruction.WRITE -> null.also { output.send(params[0].read()) }
                    Instruction.JMP_IF_TRUE -> if (params[0].read() != 0L) params[1].read() else null
                    Instruction.JMP_IF_EQ -> if (params[0].read() == 0L) params[1].read() else null
                    Instruction.LESS_THAN -> null.also { params[2].write(if (params[0].read() < params[1].read()) 1 else 0) }
                    Instruction.EQUALS -> null.also { params[2].write(if (params[0].read() == params[1].read()) 1 else 0) }
                    Instruction.SHIFT_BASE -> null.also { baseAddress += params[0].read().toInt() }
                    else -> throw IllegalArgumentException("Unknown instruction $next")
                }
                pointer = jump?.toInt() ?: pointer + 1 + instruction.numParameters
            }
        }
        output.close()
    }

}

