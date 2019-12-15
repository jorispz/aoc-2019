package day14

import print
import kotlin.math.sign

data class Ingredient(val type: String, val amount: Long)
data class Reaction(val produces: Ingredient, val consumes: List<Ingredient>)

class Factory(private val reactions: Map<String, Reaction>) {
    val initialORE = 1000000000000L
    val available = mutableMapOf<String, Long>().also { it.put("ORE", initialORE) }

    val oreUsed
        get() = initialORE - available.getValue("ORE")

    fun deliver(type: String, amount: Long) {
        val current = available.getOrPut(type) { 0 }
        if (current < amount) {
            val reaction = reactions.getValue(type)
            val minimumUnitsNeeded = amount - current
            val times =
                minimumUnitsNeeded / reaction.produces.amount + minimumUnitsNeeded.rem(reaction.produces.amount).sign
            reaction.consumes.forEach { deliver(it.type, it.amount * times) }
            available[type] = available.getValue(type) + times * reaction.produces.amount
        }
        available.put(type, available.getValue(type) - amount)
    }

}

// 1:  201324
// 2: 6326857
val p14 = suspend {

    val reactions = input.lines().associate {
        val def = it.replace(",", "").replace("=> ", "").split(" ").chunked(2)
        val produces = Ingredient(def.last().last(), def.last().first().toLong())
        val consumes = def.dropLast(1).map {
            Ingredient(it.last(), it.first().toLong())
        }
        produces.type to Reaction(produces, consumes)
    }
    Factory(reactions).apply {
        deliver("FUEL", 1)
        oreUsed.print()
    }

    // Manuel search....
    Factory(reactions).apply {
        deliver("FUEL", 6326857)
        available["ORE"].print()
    }

//    Factory(reactions).apply {
//        var fuel = 0
//        deliver(Ingredient("FUEL", 1))
//        fuel++
//        while (available.filterKeys { it != "ORE" }.values.sum() != 0L) {
//            deliver(Ingredient("FUEL", 1))
//            fuel++
//        }
//        oreUsed.print()
//        fuel.print()
//        (fuel * initialORE / oreUsed).print()
//    }


}
val test1 = """10 ORE => 10 A
1 ORE => 1 B
7 A, 1 B => 1 C
7 A, 1 C => 1 D
7 A, 1 D => 1 E
7 A, 1 E => 1 FUEL"""

val test2 = """9 ORE => 2 A
8 ORE => 3 B
7 ORE => 5 C
3 A, 4 B => 1 AB
5 B, 7 C => 1 BC
4 C, 1 A => 1 CA
2 AB, 3 BC, 4 CA => 1 FUEL"""

val test3 = """157 ORE => 5 NZVS
165 ORE => 6 DCFZ
44 XJWVT, 5 KHKGT, 1 QDVJ, 29 NZVS, 9 GPVTF, 48 HKGWZ => 1 FUEL
12 HKGWZ, 1 GPVTF, 8 PSHF => 9 QDVJ
179 ORE => 7 PSHF
177 ORE => 5 HKGWZ
7 DCFZ, 7 PSHF => 2 XJWVT
165 ORE => 2 GPVTF
3 DCFZ, 7 NZVS, 5 HKGWZ, 10 PSHF => 8 KHKGT"""

val test4 = """2 VPVL, 7 FWMGM, 2 CXFTF, 11 MNCFX => 1 STKFG
17 NVRVD, 3 JNWZP => 8 VPVL
53 STKFG, 6 MNCFX, 46 VJHF, 81 HVMC, 68 CXFTF, 25 GNMV => 1 FUEL
22 VJHF, 37 MNCFX => 5 FWMGM
139 ORE => 4 NVRVD
144 ORE => 7 JNWZP
5 MNCFX, 7 RFSQX, 2 FWMGM, 2 VPVL, 19 CXFTF => 3 HVMC
5 VJHF, 7 MNCFX, 9 VPVL, 37 CXFTF => 6 GNMV
145 ORE => 6 MNCFX
1 NVRVD => 8 CXFTF
1 VJHF, 6 MNCFX => 4 RFSQX
176 ORE => 6 VJHF"""

val input = """2 WZMS, 3 NPNFD => 5 SLRGD
4 QTFCJ, 1 RFZF => 1 QFQPN
2 LCDPV => 6 DGPND
1 MVSHM, 3 XSDR, 1 RSJD => 6 GNKB
6 XJRML, 1 LCDPV => 7 HTSJ
3 LQBX => 3 GKNTG
2 NZMLP, 5 FTNZQ => 2 QSLTQ
8 WZMS, 4 XSDR, 2 NPNFD => 9 CJVT
16 HFHB, 1 TRVQG => 8 QTBQ
177 ORE => 7 DNWGS
10 ZJFM, 4 MVSHM => 8 LCDPV
1 LTVKM => 5 ZJFM
5 QFJS => 6 LTVKM
4 CZHM, 12 CJVT => 9 PGMS
104 ORE => 8 QCGM
1 JWLZ, 5 QTFCJ => 4 DHNL
20 VKRBJ => 3 FQCKM
1 FTNZQ, 1 QSLTQ => 4 HFHB
1 JLPVD => 2 JGJFQ
12 PTDL => 1 LVPK
31 JGJFQ, 5 PGMS, 38 PTDL, 1 PGCZ, 3 LVPK, 47 JGHWZ, 21 LVPJ, 27 LTVKM, 5 ZDQD, 5 LCDPV => 1 FUEL
6 WFJT, 2 VKRBJ => 8 NZMLP
21 HNJW, 3 NXTL, 8 WZMS, 5 SLRGD, 2 VZJHN, 6 QFQPN, 5 DHNL, 19 RNXQ => 2 PGCZ
1 QTBQ, 3 MVSHM => 1 XSDR
25 ZKZNB => 9 VZJHN
4 WHLT => 9 PHFKW
29 QPVNV => 9 JGHWZ
13 ZJFM => 2 RNXQ
1 DGPND, 12 PHFKW => 9 BXGXT
25 ZJFM => 6 WHLT
3 QPVNV => 9 BTLH
1 KXQG => 8 TRVQG
2 JWLZ => 8 JLPVD
2 GKNTG => 6 NXTL
28 VKRBJ => 2 DXWSH
126 ORE => 7 VKRBJ
11 WHLT => 8 QTFCJ
1 NZMLP, 1 DNWGS, 8 VKRBJ => 5 XJRML
16 XJRML => 6 SKHJL
3 QTFCJ, 6 ZTHWQ, 15 GKNTG, 1 NXRZL, 1 DGBRZ, 1 SKHJL, 1 VZJHN => 7 LVPJ
1 HFHB, 16 QTBQ, 7 XJRML => 3 NPNFD
2 TRVQG => 4 JWLZ
8 GKNTG, 1 NSVG, 23 RNXQ => 9 NXRZL
3 QTFCJ => 6 CZHM
2 NPNFD => 8 JQSTD
1 DXWSH, 1 DGPND => 4 DGBRZ
3 DXWSH, 24 QFJS, 8 FTNZQ => 8 KXQG
6 FXJQX, 14 ZKZNB, 3 QTFCJ => 2 ZTHWQ
31 NSVG, 1 NXRZL, 3 QPVNV, 2 RNXQ, 17 NXTL, 6 BTLH, 1 HNJW, 2 HTSJ => 1 ZDQD
5 RNXQ, 23 BXGXT, 5 JQSTD => 7 QPVNV
8 NPNFD => 7 WZMS
6 KXQG => 7 ZDZM
129 ORE => 9 WFJT
9 NZMLP, 5 FQCKM, 8 QFJS => 1 LQBX
170 ORE => 9 GDBNV
5 RSJD, 3 CZHM, 1 GNKB => 6 HNJW
14 HTSJ => 7 FXJQX
11 NPNFD, 1 LCDPV, 2 FXJQX => 6 RSJD
9 DGBRZ => 6 ZKZNB
7 GDBNV, 1 QCGM => 8 QFJS
2 QFQPN, 5 JWLZ => 4 NSVG
8 QFJS, 1 ZDZM, 4 QSLTQ => 7 MVSHM
1 LTVKM => 8 RFZF
4 DNWGS => 3 FTNZQ
6 VZJHN => 9 PTDL"""