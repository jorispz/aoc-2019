package day07

import print

sealed class Parameter(val i: Int) {
    class Immediate(i: Int) : Parameter(i)
    class Position(i: Int) : Parameter(i)
}

data class Instruction(val numParameters: Int, val run: Computer.(parameters: Array<Parameter>) -> Int?)

val plus = Instruction(3) { param -> null.also { this[param[2]] = this[param[0]] + this[param[1]] } }
val times = Instruction(3) { param -> null.also { this[param[2]] = this[param[0]] * this[param[1]] } }
val read = Instruction(1) { param -> null.also { this[param[0]] = readInput() } }
val write = Instruction(1) { param -> null.also { writeOutput(this[param[0]]) } }
val jumpIfTrue = Instruction(2) { param -> if (this[param[0]] != 0) this[param[1]] else null }
val jumpIfFalse = Instruction(2) { param -> if (this[param[0]] == 0) this[param[1]] else null }
val lessThan = Instruction(3) { param -> null.also { this[param[2]] = if (this[param[0]] < this[param[1]]) 1 else 0 } }
val equals = Instruction(3) { param -> null.also { this[param[2]] = if (this[param[0]] == this[param[1]]) 1 else 0 } }
val halt = Instruction(0) { null }

val instructions = mapOf(
    1 to plus,
    2 to times,
    3 to read,
    4 to write,
    5 to jumpIfTrue,
    6 to jumpIfFalse,
    7 to lessThan,
    8 to equals,
    99 to halt
)

typealias State = Triple<ROM, InstructionPointer, Int>
typealias RAM = MutableMap<Int, Int>
typealias ROM = Map<Int, Int>
typealias InstructionPointer = Int

class Computer(program: String) {
    private val memory: RAM =
        program.split(",").withIndex().associateTo(mutableMapOf()) { it.index to it.value.toInt() }
    private var pointer = 0

    private lateinit var inputs: Iterator<Int>
    fun readInput() = inputs.next()

    private val outputs = mutableListOf<State>()
    fun writeOutput(i: Int) = outputs.add(Triple(memory.toMap(), pointer, i))

    fun run(inputs: List<Int>): List<State> {
        this.inputs = inputs.iterator()
        generateSequence(::tick).takeWhile { !it }.count().print { "Executed $it instructions" }
        return outputs
    }

    operator fun get(p: Parameter) = when (p) {
        is Parameter.Position -> memory.getValue(p.i)
        is Parameter.Immediate -> p.i
    }

    operator fun set(p: Parameter, j: Int) = when (p) {
        is Parameter.Position -> memory[p.i] = j
        is Parameter.Immediate -> throw IllegalArgumentException("Cannot write to parameter in Immediate mode")
    }

    private fun tick(): Boolean {
        val instructionDefinition = memory.getValue(pointer)
        val instructionID = instructionDefinition.rem(100)
        val instruction = instructions.getValue(instructionID)
        val modes =
            instructionDefinition.toString().dropLast(2).reversed().padEnd(instruction.numParameters, '0').toList()
        return (instruction == halt) || false.also {
            val args = (1..instruction.numParameters).map { memory.getValue(pointer + it) }
            val params = args.zip(modes).map {
                when (it.second) {
                    '0' -> Parameter.Position(it.first)
                    '1' -> Parameter.Immediate(it.first)
                    else -> throw IllegalArgumentException("Unknown mode ${it.second}")
                }
            }
            val jump = instruction.run(this, params.toTypedArray())
            pointer = jump ?: pointer + 1 + instruction.numParameters
        }

    }


}

