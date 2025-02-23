@file:Suppress("UNUSED_PARAMETER", "unused")

package lesson9.task1

import java.io.StringReader

// Урок 9: проектирование классов
// Максимальное количество баллов = 40 (без очень трудных задач = 15)

/**
 * Ячейка матрицы: row = ряд, column = колонка
 */
data class Cell(val row: Int, val column: Int)

/**
 * Интерфейс, описывающий возможности матрицы. E = тип элемента матрицы
 */
interface Matrix<E> {
    /** Высота */
    val height: Int

    /** Ширина */
    val width: Int

    /**
     * Доступ к ячейке.
     * Методы могут бросить исключение, если ячейка не существует или пуста
     */
    operator fun get(row: Int, column: Int): E

    operator fun get(cell: Cell): E

    /**
     * Запись в ячейку.
     * Методы могут бросить исключение, если ячейка не существует
     */
    operator fun set(row: Int, column: Int, value: E)

    operator fun set(cell: Cell, value: E)
}

/**
 * Простая (2 балла)
 *
 * Метод для создания матрицы, должен вернуть РЕАЛИЗАЦИЮ Matrix<E>.
 * height = высота, width = ширина, e = чем заполнить элементы.
 * Бросить исключение IllegalArgumentException, если height или width <= 0.
 */
fun <E> createMatrix(height: Int, width: Int, e: E): Matrix<E> = MatrixImpl(height, width, e)

/**
 * Средняя сложность (считается двумя задачами в 3 балла каждая)
 *
 * Реализация интерфейса "матрица"
 */
class MatrixImpl<E>(override val height: Int, override val width: Int, e: E) : Matrix<E> {
    private val map: MutableMap<Cell, E> = mutableMapOf()

    init {
        for (i in 0 until height) {
            for (j in 0 until width) {
                map[Cell(i, j)] = e
            }
        }
    }

    override fun get(row: Int, column: Int): E = get(Cell(row, column))

    override fun get(cell: Cell): E = map[cell] ?: throw IllegalArgumentException(cell.toString())

    override fun set(row: Int, column: Int, value: E) {
        set(Cell(row, column), value)
    }

    override fun set(cell: Cell, value: E) {
        if (cell in map) map[cell] = value else throw IllegalArgumentException(cell.toString())
    }

    override fun equals(other: Any?) =
        other is MatrixImpl<*> && this.width == other.width && this.height == other.height
                && this.map.all { it.value == other.map[it.key] }

    override fun toString(): String = StringBuilder().let { stringBuilder ->
        for (i in 0 until height) {
            for (j in 0 until width) {
                stringBuilder.append("\t${map[Cell(i, j)]}")
            }
            stringBuilder.append("\n")
        }
        stringBuilder.toString()
    }
}

