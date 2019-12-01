package p01

import print

// 3305301
// 4955106
val p01 = fun() {
    val weights = input_01.splitToSequence("\n").map { it.toInt() }

    weights.sumBy { calculateFuel(it) }.print { "Part 1 - fuel required $it" }

    weights.sumBy { keepApplying(it, ::calculateFuel).takeWhile { it > 0 }.sum() }
        .print { "Parr 2 - fuel required $it" }

}

fun calculateFuel(w: Int) = w / 3 - 2

fun <T> keepApplying(start: T, block: (T) -> T) = sequence {
    var current = start;
    while (true) {
        current = block(current).also { yield(it) }
    }
}


const val input_01 = """90014
136811
76785
52456
100165
145455
139492
147364
132728
148120
125346
70660
93908
65560
117553
82640
102895
52255
92105
131486
108400
50206
143776
125140
85110
99560
132357
114882
54894
97524
92970
96947
90800
77099
105103
66349
88495
105036
141694
125727
84853
138364
65577
148012
79944
96503
119701
66221
72469
93647
78767
56419
53435
77682
122753
144944
54835
57744
131886
101510
113730
94631
132978
132739
64250
125158
89069
112371
95739
93349
78558
135082
132015
144682
62515
59722
70175
82703
65827
78405
125701
94987
70914
62543
130058
83997
133749
62224
116328
120760
118160
76755
64521
109956
113248
141473
100546
74991
53223
147635"""