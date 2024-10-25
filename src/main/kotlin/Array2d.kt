package net.riedel

import net.riedel.ArrayType.Companion.DEFAULT

enum class ArrayType(val string: String) {
    EMPTY("."),
    TABLE("X"),
    ;

    override fun toString(): String = string

    companion object {
        val DEFAULT = EMPTY
    }
}

class Array2d(val array: List<MutableList<ArrayType>>) {

    constructor(width: Int, height: Int) : this(List(height) { MutableList(width) { DEFAULT } })
    constructor(lines: Iterable<String>) : this(lines.map { line -> line.mapTo(mutableListOf()) { char -> ArrayType.entries.first { it.string == char.toString() } } })

    fun getOrNull(x: Int, y: Int) = array.getOrNull(y)?.getOrNull(x)
    fun get(x: Int, y: Int) = array[y][x]
    fun set(x: Int, y: Int, value: ArrayType) = array[y].set(x, value)

    fun getSubArray(x0: Int, y0: Int, x1Excl: Int, y1Excl: Int) =
        Array2d(List(y1Excl - y0) { y ->
            array[y0 + y].subList(x0, x1Excl)
        })

    fun clone() =
        Array2d(array.map { it.toMutableList() })

    fun count(value: ArrayType): Int =
        array.sumOf { row -> row.count { it == value } }

    fun transpose(): Array2d {
        val newWidth = array.size
        val newHeight = array.first().size
        val transposed = Array2d(newWidth, newHeight)
        repeat(newWidth) { x1 ->
            repeat(newHeight) { y1 ->
                transposed.set(x1, y1, get(y1, x1))
            }
        }
        return transposed
    }

    override fun toString() =
        array.joinToString("\n") { it.joinToString("") }
}

val IntProgression.size: Int
    get() = this.last - this.first + 1

val IntProgression.endExcl: Int
    get() = this.last + 1