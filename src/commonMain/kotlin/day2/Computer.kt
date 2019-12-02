package day2


enum class Instruction(val id: Int, val length: Int) {

    PLUS(1, 4) {
        override operator fun invoke(memory: RAM, vararg args: Int) {
            val (i, j, k) = args
            memory[k] = memory[i] + memory[j]
        }
    },
    TIMES(2, 4) {
        override operator fun invoke(memory: RAM, vararg args: Int) {
            val (i, j, k) = args
            memory[k] = memory[i] * memory[j]
        }
    },
    HALT(99, 0) {
        override operator fun invoke(memory: RAM, vararg args: Int) {}
    };

    abstract operator fun invoke(memory: RAM, vararg args: Int)
}

class RAM(initial: String) {
    private val contents: MutableMap<Int, Int> = mutableMapOf()

    init {
        initial.split(",").withIndex().associateTo(contents) { it.index to it.value.toInt() }
    }

    operator fun set(i: Int, j: Int) = contents.set(i, j)
    operator fun get(i: Int) = contents.getValue(i)

    operator fun get(i: IntRange): IntArray = i.map { get(it) }.toIntArray()

    override fun toString() = contents.toString()


}

class Computer(initialMemory: RAM, initialPointer: Int = 0) {
    val memory = initialMemory
    private var instructionPointer = initialPointer
    private val instructions = Instruction.values().associateBy { it.id }

    fun tick(): Boolean {
        val instructionID = memory[instructionPointer];
        val instruction = instructions.getValue(instructionID)
        if (instruction == Instruction.HALT) {
            return true
        }
        val args = memory[instructionPointer + 1 until instructionPointer + instruction.length]
        instruction(memory, *args)

        instructionPointer += instruction.length
        return false
    }

    fun runToCompletion(): Computer {
        while (!tick()) {
        }
        return this
    }

}

