@file:Suppress("UNUSED_PARAMETER")

package lesson8.task2

import lesson8.task1.delta
import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.math.*

/**
 * Клетка шахматной доски. Шахматная доска квадратная и имеет 8 х 8 клеток.
 * Поэтому, обе координаты клетки (горизонталь row, вертикаль column) могут находиться в пределах от 1 до 8.
 * Горизонтали нумеруются снизу вверх, вертикали слева направо.
 */
data class Square(val column: Int, val row: Int) {
    /**
     * Пример
     *
     * Возвращает true, если клетка находится в пределах доски
     */
    fun inside(): Boolean = column in 1..8 && row in 1..8

    /**
     * Простая (2 балла)
     *
     * Возвращает строковую нотацию для клетки.
     * В нотации, колонки обозначаются латинскими буквами от a до h, а ряды -- цифрами от 1 до 8.
     * Для клетки не в пределах доски вернуть пустую строку
     */
    fun notation(): String = if (inside()) "${'a' + column - 1}$row" else ""
}

/**
 * Простая (2 балла)
 *
 * Создаёт клетку по строковой нотации.
 * В нотации, колонки обозначаются латинскими буквами от a до h, а ряды -- цифрами от 1 до 8.
 * Если нотация некорректна, бросить IllegalArgumentException
 */
fun square(notation: String): Square = if (Regex("""[a-h][1-8]""").matches(notation)) Square(
    notation[0] - 'a' + 1,
    notation[1] - '0'
) else throw IllegalArgumentException()

/**
 * Простая (2 балла)
 *
 * Определить число ходов, за которое шахматная ладья пройдёт из клетки start в клетку end.
 * Шахматная ладья может за один ход переместиться на любую другую клетку
 * по вертикали или горизонтали.
 * Ниже точками выделены возможные ходы ладьи, а крестиками -- невозможные:
 *
 * xx.xxххх
 * xх.хxххх
 * ..Л.....
 * xх.хxххх
 * xx.xxххх
 * xx.xxххх
 * xx.xxххх
 * xx.xxххх
 *
 * Если клетки start и end совпадают, вернуть 0.
 * Если любая из клеток некорректна, бросить IllegalArgumentException().
 *
 * Пример: rookMoveNumber(Square(3, 1), Square(6, 3)) = 2
 * Ладья может пройти через клетку (3, 3) или через клетку (6, 1) к клетке (6, 3).
 */
fun rookMoveNumber(start: Square, end: Square): Int = if (start.inside() && end.inside()) {
    2 - toOne(start.column == end.column) - toOne(start.row == end.row)
} else throw IllegalArgumentException()

fun toOne(boolean: Boolean): Int = if (boolean) 1 else 0

/**
 * Средняя (3 балла)
 *
 * Вернуть список из клеток, по которым шахматная ладья может быстрее всего попасть из клетки start в клетку end.
 * Описание ходов ладьи см. предыдущую задачу.
 * Список всегда включает в себя клетку start. Клетка end включается, если она не совпадает со start.
 * Между ними должны находиться промежуточные клетки, по порядку от start до end.
 * Примеры: rookTrajectory(Square(3, 3), Square(3, 3)) = listOf(Square(3, 3))
 *          (здесь возможен ещё один вариант)
 *          rookTrajectory(Square(3, 1), Square(6, 3)) = listOf(Square(3, 1), Square(3, 3), Square(6, 3))
 *          (здесь возможен единственный вариант)
 *          rookTrajectory(Square(3, 5), Square(8, 5)) = listOf(Square(3, 5), Square(8, 5))
 * Если возможно несколько вариантов самой быстрой траектории, вернуть любой из них.
 */
fun rookTrajectory(start: Square, end: Square): List<Square> = when (rookMoveNumber(start, end)) {
    1 -> listOf(start, end)
    2 -> listOf(start, Square(start.column, end.row), end)
    else -> listOf(start)
}

/**
 * Простая (2 балла)
 *
 * Определить число ходов, за которое шахматный слон пройдёт из клетки start в клетку end.
 * Шахматный слон может за один ход переместиться на любую другую клетку по диагонали.
 * Ниже точками выделены возможные ходы слона, а крестиками -- невозможные:
 *
 * .xxx.ххх
 * x.x.xххх
 * xxСxxxxx
 * x.x.xххх
 * .xxx.ххх
 * xxxxx.хх
 * xxxxxх.х
 * xxxxxхх.
 *
 * Если клетки start и end совпадают, вернуть 0.
 * Если клетка end недостижима для слона, вернуть -1.
 * Если любая из клеток некорректна, бросить IllegalArgumentException().
 *
 * Примеры: bishopMoveNumber(Square(3, 1), Square(6, 3)) = -1; bishopMoveNumber(Square(3, 1), Square(3, 7)) = 2.
 * Слон может пройти через клетку (6, 4) к клетке (3, 7).
 */
fun bishopMoveNumber(start: Square, end: Square): Int = when {
    !(start.inside() && end.inside()) -> throw IllegalArgumentException()
    start == end -> 0
    abs(start.row - end.row) % 2 != abs(start.column - end.column) % 2 -> -1
    else -> if (abs(start.row - end.row) == abs(start.column - end.column)) 1 else 2
}

/**
 * Сложная (5 баллов)
 *
 * Вернуть список из клеток, по которым шахматный слон может быстрее всего попасть из клетки start в клетку end.
 * Описание ходов слона см. предыдущую задачу.
 *
 * Если клетка end недостижима для слона, вернуть пустой список.
 *
 * Если клетка достижима:
 * - список всегда включает в себя клетку start
 * - клетка end включается, если она не совпадает со start.
 * - между ними должны находиться промежуточные клетки, по порядку от start до end.
 *
 * Примеры: bishopTrajectory(Square(3, 3), Square(3, 3)) = listOf(Square(3, 3))
 *          bishopTrajectory(Square(3, 1), Square(3, 7)) = listOf(Square(3, 1), Square(6, 4), Square(3, 7))
 *          bishopTrajectory(Square(1, 3), Square(6, 8)) = listOf(Square(1, 3), Square(6, 8))
 * Если возможно несколько вариантов самой быстрой траектории, вернуть любой из них.
 */
fun bishopTrajectory(start: Square, end: Square): List<Square> = when (bishopMoveNumber(start, end)) {
    -1 -> listOf()
    1 -> listOf(start, end)
    2 -> listOf(start, getMiddle(start, end), end)
    else -> listOf(start)
}

fun getMiddle(start: Square, end: Square): Square { //строим 4 прямые и ищем 2 точки пересечения
    val b1 = (start.column - start.row + 1)
    val b2 = (start.column + start.row - 1)
    val b3 = (end.column - end.row + 1)
    val b4 = (end.column + end.row - 1)
    val yP1 = (b1 + b4) / 2
    val yP2 = (b2 + b3) / 2
    val square1 = Square(yP1, yP1 - b1 + 1)
    return if (square1.inside()) square1 else Square(yP2, yP2 - b3 + 1)
}

/**
 * Средняя (3 балла)
 *
 * Определить число ходов, за которое шахматный король пройдёт из клетки start в клетку end.
 * Шахматный король одним ходом может переместиться из клетки, в которой стоит,
 * на любую соседнюю по вертикали, горизонтали или диагонали.
 * Ниже точками выделены возможные ходы короля, а крестиками -- невозможные:
 *
 * xxxxx
 * x...x
 * x.K.x
 * x...x
 * xxxxx
 *
 * Если клетки start и end совпадают, вернуть 0.
 * Если любая из клеток некорректна, бросить IllegalArgumentException().
 *
 * Пример: kingMoveNumber(Square(3, 1), Square(6, 3)) = 3.
 * Король может последовательно пройти через клетки (4, 2) и (5, 2) к клетке (6, 3).
 */
fun kingMoveNumber(start: Square, end: Square): Int {
    if (!start.inside() || !end.inside()) throw IllegalArgumentException()
    val deltaR = abs(start.row - end.row)
    val deltaC = abs(start.column - end.column)
    return deltaR + deltaC - min(deltaR, deltaC)
}

/**
 * Сложная (5 баллов)
 *
 * Вернуть список из клеток, по которым шахматный король может быстрее всего попасть из клетки start в клетку end.
 * Описание ходов короля см. предыдущую задачу.
 * Список всегда включает в себя клетку start. Клетка end включается, если она не совпадает со start.
 * Между ними должны находиться промежуточные клетки, по порядку от start до end.
 * Примеры: kingTrajectory(Square(3, 3), Square(3, 3)) = listOf(Square(3, 3))
 *          (здесь возможны другие варианты)
 *          kingTrajectory(Square(3, 1), Square(6, 3)) = listOf(Square(3, 1), Square(4, 2), Square(5, 2), Square(6, 3))
 *          (здесь возможен единственный вариант)
 *          kingTrajectory(Square(3, 5), Square(6, 2)) = listOf(Square(3, 5), Square(4, 4), Square(5, 3), Square(6, 2))
 * Если возможно несколько вариантов самой быстрой траектории, вернуть любой из них.
 */
fun kingTrajectory(start: Square, end: Square): List<Square> {
    if (start == end) return mutableListOf(start)
    val result = mutableListOf<Square>()
    val deltaR = end.row - start.row
    val deltaC = end.column - start.column
    val diagonal = min(abs(deltaC), abs(deltaR))
    val signR = if (deltaR > 0) start.row..start.row + diagonal else start.row downTo start.row - diagonal
    val signC = if (deltaC > 0) start.column..start.column + diagonal else start.column downTo start.column - diagonal
    result.addAll(signC.zip(signR).map { (column, row) -> Square(column, row) })
    val last = result.last()
    if (last == end) return result
    if (last.row == end.row) {
        for (column in if (last.column > end.column) last.column - 1 downTo end.column else last.column + 1..end.column) {
            result.add(Square(column, last.row))
        }
    } else {
        for (row in if (last.row > end.row) last.row - 1 downTo end.row else last.row + 1..end.row) {
            result.add(Square(last.column, row))
        }
    }
    return result
}

fun <T> knightUniversal(start: Square, end: Square, p: T, mapCheck: (T, T) -> Boolean, sum: (T, Square) -> T): T =
    if (!start.inside() || !end.inside()) throw IllegalArgumentException() else
        mutableMapOf(start to p).let { map ->
            val queue = ArrayDeque<Square>()
            queue.add(start)
            fun putNext(column: Int, row: Int, value: T) {
                val square = Square(column, row)
                if (square.inside() && (map[square]?.let { mapCheck(it, value) } != false)) {
                    map[square] = sum(value, square)
                    if (square !in queue) queue.add(square)
                }
            }
            while (queue.isNotEmpty()) {
                val curr = queue.removeFirst()
                val column = curr.column
                val row = curr.row
                val value = map[curr]!!
                putNext(column + 1, row + 2, value)
                putNext(column + 1, row - 2, value)
                putNext(column - 1, row + 2, value)
                putNext(column - 1, row - 2, value)
                putNext(column + 2, row + 1, value)
                putNext(column + 2, row - 1, value)
                putNext(column - 2, row + 1, value)
                putNext(column - 2, row - 1, value)
            }
            map[end]!!
        }

/**
 * Сложная (6 баллов)
 *
 * Определить число ходов, за которое шахматный конь пройдёт из клетки start в клетку end.
 * Шахматный конь одним ходом вначале передвигается ровно на 2 клетки по горизонтали или вертикали,
 * а затем ещё на 1 клетку под прямым углом, образуя букву "Г".
 * Ниже точками выделены возможные ходы коня, а крестиками -- невозможные:
 *
 * .xxx.xxx
 * xxKxxxxx
 * .xxx.xxx
 * x.x.xxxx
 * xxxxxxxx
 * xxxxxxxx
 * xxxxxxxx
 * xxxxxxxx
 *
 * Если клетки start и end совпадают, вернуть 0.
 * Если любая из клеток некорректна, бросить IllegalArgumentException().
 *
 * Пример: knightMoveNumber(Square(3, 1), Square(6, 3)) = 3.
 * Конь может последовательно пройти через клетки (5, 2) и (4, 4) к клетке (6, 3).
 */
fun knightMoveNumber(start: Square, end: Square): Int =
    knightUniversal(start, end, 0, { a: Int, b: Int -> a > b + 1 }, { a: Int, _: Square -> a + 1 })

/**
 * Очень сложная (10 баллов)
 *
 * Вернуть список из клеток, по которым шахматный конь может быстрее всего попасть из клетки start в клетку end.
 * Описание ходов коня см. предыдущую задачу.
 * Список всегда включает в себя клетку start. Клетка end включается, если она не совпадает со start.
 * Между ними должны находиться промежуточные клетки, по порядку от start до end.
 * Примеры:
 *
 * knightTrajectory(Square(3, 3), Square(3, 3)) = listOf(Square(3, 3))
 * здесь возможны другие варианты)
 * knightTrajectory(Square(3, 1), Square(6, 3)) = listOf(Square(3, 1), Square(5, 2), Square(4, 4), Square(6, 3))
 * (здесь возможен единственный вариант)
 * knightTrajectory(Square(3, 5), Square(5, 6)) = listOf(Square(3, 5), Square(5, 6))
 * (здесь опять возможны другие варианты)
 * knightTrajectory(Square(7, 7), Square(8, 8)) =
 *     listOf(Square(7, 7), Square(5, 8), Square(4, 6), Square(6, 7), Square(8, 8))
 *
 * Если возможно несколько вариантов самой быстрой траектории, вернуть любой из них.
 */
fun knightTrajectory(start: Square, end: Square): List<Square> = if (start == end) listOf(start) else
    knightUniversal(
        start,
        end,
        Pair(0, listOf(start)),
        { (current), (checked) -> current > checked + 1 },
        { (first, second), square -> Pair(first + 1, second + square) }).second
