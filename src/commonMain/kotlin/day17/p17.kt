package day17

import Position
import day09.launchComputer
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import print

val p17 = suspend {

    val map = coroutineScope {
        val stdin = Channel<Long>()
        val stdout = Channel<Long>()
        launchComputer(input, stdin, stdout)

        stdout.consumeAsFlow().map { it.toChar() }.toList().joinToString("").lines().mapIndexed { y, l ->
            l.mapIndexed { x, c ->
                Pair(Position(x, y), c)
            }
        }.flatten().associate { it }
    }
    map.filter { it.value == '#' && it.key.adjacents().all { p -> map[p] == '#' } }.keys.sumBy { it.x * it.y }
        .print { "Part 1: $it" }

    val instructions = """
        A,B,A,C,B,A,C,A,C,B
        L,12,L,8,L,8
        L,12,R,4,L,12,R,6
        R,4,L,12,L,12,R,6
        n
        
    """.trimIndent().map {
        it.toInt()
    }

    coroutineScope {
        val stdin = Channel<Long>(Channel.UNLIMITED)
        val stdout = Channel<Long>(Channel.UNLIMITED)
        launchComputer("2" + input.drop(1), stdin, stdout)

        launch {
            while (!stdout.isClosedForReceive) {
                stdout.receive().also { print(if (it < 255) it.toChar() else "Part 2: $it") }
            }
        }

        instructions.forEach { stdin.send(it.toLong()) }

    }
}

val input =
    "1,330,331,332,109,4146,1102,1,1182,15,1101,1451,0,24,1002,0,1,570,1006,570,36,102,1,571,0,1001,570,-1,570,1001,24,1,24,1105,1,18,1008,571,0,571,1001,15,1,15,1008,15,1451,570,1006,570,14,21102,58,1,0,1106,0,786,1006,332,62,99,21102,333,1,1,21101,73,0,0,1105,1,579,1101,0,0,572,1102,1,0,573,3,574,101,1,573,573,1007,574,65,570,1005,570,151,107,67,574,570,1005,570,151,1001,574,-64,574,1002,574,-1,574,1001,572,1,572,1007,572,11,570,1006,570,165,101,1182,572,127,1002,574,1,0,3,574,101,1,573,573,1008,574,10,570,1005,570,189,1008,574,44,570,1006,570,158,1105,1,81,21102,1,340,1,1105,1,177,21102,477,1,1,1106,0,177,21102,1,514,1,21101,0,176,0,1105,1,579,99,21101,0,184,0,1106,0,579,4,574,104,10,99,1007,573,22,570,1006,570,165,102,1,572,1182,21102,1,375,1,21102,1,211,0,1106,0,579,21101,1182,11,1,21102,222,1,0,1106,0,979,21102,388,1,1,21101,0,233,0,1106,0,579,21101,1182,22,1,21102,244,1,0,1105,1,979,21102,1,401,1,21101,0,255,0,1106,0,579,21101,1182,33,1,21101,0,266,0,1105,1,979,21102,414,1,1,21101,277,0,0,1105,1,579,3,575,1008,575,89,570,1008,575,121,575,1,575,570,575,3,574,1008,574,10,570,1006,570,291,104,10,21101,1182,0,1,21101,0,313,0,1105,1,622,1005,575,327,1102,1,1,575,21102,327,1,0,1106,0,786,4,438,99,0,1,1,6,77,97,105,110,58,10,33,10,69,120,112,101,99,116,101,100,32,102,117,110,99,116,105,111,110,32,110,97,109,101,32,98,117,116,32,103,111,116,58,32,0,12,70,117,110,99,116,105,111,110,32,65,58,10,12,70,117,110,99,116,105,111,110,32,66,58,10,12,70,117,110,99,116,105,111,110,32,67,58,10,23,67,111,110,116,105,110,117,111,117,115,32,118,105,100,101,111,32,102,101,101,100,63,10,0,37,10,69,120,112,101,99,116,101,100,32,82,44,32,76,44,32,111,114,32,100,105,115,116,97,110,99,101,32,98,117,116,32,103,111,116,58,32,36,10,69,120,112,101,99,116,101,100,32,99,111,109,109,97,32,111,114,32,110,101,119,108,105,110,101,32,98,117,116,32,103,111,116,58,32,43,10,68,101,102,105,110,105,116,105,111,110,115,32,109,97,121,32,98,101,32,97,116,32,109,111,115,116,32,50,48,32,99,104,97,114,97,99,116,101,114,115,33,10,94,62,118,60,0,1,0,-1,-1,0,1,0,0,0,0,0,0,1,12,28,0,109,4,1201,-3,0,587,20101,0,0,-1,22101,1,-3,-3,21101,0,0,-2,2208,-2,-1,570,1005,570,617,2201,-3,-2,609,4,0,21201,-2,1,-2,1106,0,597,109,-4,2105,1,0,109,5,2102,1,-4,630,20101,0,0,-2,22101,1,-4,-4,21101,0,0,-3,2208,-3,-2,570,1005,570,781,2201,-4,-3,652,21002,0,1,-1,1208,-1,-4,570,1005,570,709,1208,-1,-5,570,1005,570,734,1207,-1,0,570,1005,570,759,1206,-1,774,1001,578,562,684,1,0,576,576,1001,578,566,692,1,0,577,577,21102,1,702,0,1105,1,786,21201,-1,-1,-1,1105,1,676,1001,578,1,578,1008,578,4,570,1006,570,724,1001,578,-4,578,21102,731,1,0,1106,0,786,1105,1,774,1001,578,-1,578,1008,578,-1,570,1006,570,749,1001,578,4,578,21101,0,756,0,1106,0,786,1106,0,774,21202,-1,-11,1,22101,1182,1,1,21101,0,774,0,1105,1,622,21201,-3,1,-3,1106,0,640,109,-5,2106,0,0,109,7,1005,575,802,21001,576,0,-6,21001,577,0,-5,1106,0,814,21101,0,0,-1,21101,0,0,-5,21101,0,0,-6,20208,-6,576,-2,208,-5,577,570,22002,570,-2,-2,21202,-5,55,-3,22201,-6,-3,-3,22101,1451,-3,-3,1201,-3,0,843,1005,0,863,21202,-2,42,-4,22101,46,-4,-4,1206,-2,924,21101,1,0,-1,1105,1,924,1205,-2,873,21101,0,35,-4,1105,1,924,1201,-3,0,878,1008,0,1,570,1006,570,916,1001,374,1,374,2102,1,-3,895,1102,1,2,0,2101,0,-3,902,1001,438,0,438,2202,-6,-5,570,1,570,374,570,1,570,438,438,1001,578,558,922,20102,1,0,-4,1006,575,959,204,-4,22101,1,-6,-6,1208,-6,55,570,1006,570,814,104,10,22101,1,-5,-5,1208,-5,49,570,1006,570,810,104,10,1206,-1,974,99,1206,-1,974,1102,1,1,575,21102,1,973,0,1106,0,786,99,109,-7,2106,0,0,109,6,21101,0,0,-4,21101,0,0,-3,203,-2,22101,1,-3,-3,21208,-2,82,-1,1205,-1,1030,21208,-2,76,-1,1205,-1,1037,21207,-2,48,-1,1205,-1,1124,22107,57,-2,-1,1205,-1,1124,21201,-2,-48,-2,1105,1,1041,21101,-4,0,-2,1106,0,1041,21102,1,-5,-2,21201,-4,1,-4,21207,-4,11,-1,1206,-1,1138,2201,-5,-4,1059,2102,1,-2,0,203,-2,22101,1,-3,-3,21207,-2,48,-1,1205,-1,1107,22107,57,-2,-1,1205,-1,1107,21201,-2,-48,-2,2201,-5,-4,1090,20102,10,0,-1,22201,-2,-1,-2,2201,-5,-4,1103,1202,-2,1,0,1105,1,1060,21208,-2,10,-1,1205,-1,1162,21208,-2,44,-1,1206,-1,1131,1106,0,989,21101,439,0,1,1105,1,1150,21102,1,477,1,1106,0,1150,21102,1,514,1,21102,1,1149,0,1106,0,579,99,21102,1,1157,0,1106,0,579,204,-2,104,10,99,21207,-3,22,-1,1206,-1,1138,1202,-5,1,1176,2102,1,-4,0,109,-6,2106,0,0,10,9,46,1,7,1,46,1,7,1,46,1,7,1,46,1,7,1,46,1,7,1,46,1,7,1,46,1,7,1,42,5,7,1,42,1,11,1,42,1,11,1,42,1,11,1,40,1,1,1,5,7,40,1,1,1,5,1,46,1,1,1,5,1,46,1,1,1,5,1,46,1,1,1,5,1,46,1,1,1,5,1,46,13,44,1,5,1,3,1,44,13,48,1,3,1,1,1,48,1,3,13,38,1,5,1,9,1,34,5,5,1,9,1,5,13,16,1,9,1,9,1,5,1,11,1,16,1,9,13,3,1,11,1,16,1,19,1,1,1,3,1,11,1,8,13,15,13,5,10,7,1,21,1,3,1,5,1,13,2,7,1,21,13,11,2,7,1,25,1,5,1,1,1,11,2,7,1,25,1,5,1,1,1,11,2,7,1,25,1,5,1,1,1,11,2,7,1,25,1,5,1,1,1,11,2,7,1,25,1,5,1,1,1,11,10,19,7,5,1,1,13,28,1,11,1,42,1,11,1,42,1,11,1,42,1,7,5,42,1,7,1,46,1,7,1,46,1,7,1,46,1,7,1,46,1,7,1,46,1,7,1,46,1,7,1,46,9,18"