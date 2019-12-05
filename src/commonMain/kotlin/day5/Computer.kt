package day5

enum class Instruction(val id: Int, val numParameters: Int) {

    PLUS(1, 3) {
        override operator fun invoke(computer: Computer, vararg parameters: Parameter): Int {
            val (i, j, k) = parameters
            computer.memory[k] = computer.memory[i] + computer.memory[j]
            return 0
        }
    },

    TIMES(2, 3) {
        override operator fun invoke(computer: Computer, vararg parameters: Parameter): Int {
            val (i, j, k) = parameters
            computer.memory[k] = computer.memory[i] * computer.memory[j]
            return 0
        }
    },

    READ(3, 1) {
        override operator fun invoke(computer: Computer, vararg parameters: Parameter): Int {
            computer.memory[parameters[0]] = computer.readSingle()
            return 0
        }
    },

    WRITE(4, 1) {
        override operator fun invoke(computer: Computer, vararg parameters: Parameter): Int {
            computer.outputSingle(computer.memory[parameters[0]])
            return 0;
        }
    },

    JUMP_IF_TRUE(5, 2) {
        override operator fun invoke(computer: Computer, vararg parameters: Parameter): Int =
            if (computer.memory[parameters[0]] != 0) {
                computer.memory[parameters[1]]
            } else {
                0
            }
    },

    JUMP_IF_FALSE(6, 2) {
        override operator fun invoke(computer: Computer, vararg parameters: Parameter): Int =
            if (computer.memory[parameters[0]] == 0) {
                computer.memory[parameters[1]]
            } else {
                0
            }
    },

    LESS_THAN(7, 3) {
        override operator fun invoke(computer: Computer, vararg parameters: Parameter): Int {
            val (a, b) = parameters.take(2).map { computer.memory[it] }
            computer.memory[parameters[2]] = if (a < b) 1 else 0
            return 0
        }

    },

    EQUALS(8, 3) {
        override operator fun invoke(computer: Computer, vararg parameters: Parameter): Int {
            val (a, b) = parameters.take(2).map { computer.memory[it] }
            computer.memory[parameters[2]] = if (a == b) 1 else 0
            return 0
        }

    },

    HALT(99, 0) {
        override operator fun invoke(computer: Computer, vararg parameters: Parameter) = 0
    };

    abstract operator fun invoke(computer: Computer, vararg parameters: Parameter): Int
}

sealed class Parameter(val i: Int) {
    abstract fun read(ram: RAM): Int
    abstract fun write(ram: RAM, value: Int)

    class Immediate(i: Int) : Parameter(i) {
        override fun read(ram: RAM) = i
        override fun write(ram: RAM, j: Int) = throw RuntimeException("Can't write to an Immediate parameter")
    }

    class Position(i: Int) : Parameter(i) {
        override fun read(ram: RAM) = ram[i]
        override fun write(ram: RAM, j: Int) {
            ram[this.i] = j
        }
    }
}


class RAM(initial: String) {
    private val contents: MutableMap<Int, Int> = mutableMapOf()

    init {
        initial.split(",").withIndex().associateTo(contents) { it.index to it.value.toInt() }
    }

    operator fun get(p: Parameter) = p.read(this)
    operator fun set(p: Parameter, j: Int) = p.write(this, j)

    operator fun set(i: Int, j: Int) = contents.set(i, j)
    operator fun get(i: Int) = contents.getValue(i)

    operator fun get(i: IntRange): IntArray = i.map { get(it) }.toIntArray()
    override fun toString() = contents.toString()
}

class Computer(initialMemory: RAM, inputSequence: Sequence<Int>, val outputSink: (Int) -> Unit) {
    val memory = initialMemory
    private var pointer = 0
    private val instructions = Instruction.values().associateBy { it.id }
    private val inputs = inputSequence.iterator()

    fun tick(): Boolean {
        val instructionDefinition = memory[pointer]
        val instructionID = instructionDefinition.rem(100)
        val instruction = instructions.getValue(instructionID)
        val modes =
            instructionDefinition.toString().dropLast(2).reversed().padEnd(instruction.numParameters, '0').toList()
        return (instruction == Instruction.HALT) || false.also {
            val args = memory[(pointer + 1)..(pointer + instruction.numParameters)]
            val params = args.zip(modes).map {
                when (it.second) {
                    '0' -> Parameter.Position(it.first)
                    '1' -> Parameter.Immediate(it.first)
                    else -> throw IllegalArgumentException("Unknown mode ${it.second}")
                }
            }
            val jump = instruction(this, *params.toTypedArray())
            pointer = if (jump != 0) jump else pointer + 1 + instruction.numParameters
        }

    }


    @Suppress("ControlFlowWithEmptyBody")
    fun runToCompletion(): Computer {
        generateSequence(::tick).takeWhile { !it }.last()
        return this
    }

    fun readSingle() = inputs.next()
    fun outputSingle(i: Int) = this.outputSink(i)

}

