import kotlin.math.min

private data class Vertex<T>(val node: T, var distance: Int = Int.MAX_VALUE)

/**
 * Implementation of Dijkstra's algorithm to find all shortest distances between
 *
 *
 */
fun <T> shortestPathLengths(origin: T, nodes: Collection<T>, adjacentTo: T.(t: T) -> Boolean): List<Pair<T, Int>> {

    val shortest = mutableListOf<Vertex<T>>()
    val source = nodes.mapTo(mutableListOf()) { Vertex(it) }
    source.find { it.node == origin }?.distance = 0

    while (source.isNotEmpty()) {
        source.sortBy { it.distance }
        val next = source.removeAt(0)
        shortest.add(next)
        source.filter { it.node.adjacentTo(next.node) }.forEach {
            it.distance = min(next.distance + 1, it.distance)
        }
    }
    return shortest.map { Pair(it.node, it.distance) }
}
