@file:Suppress("UNUSED_PARAMETER")

package lesson8.task1

import kotlin.math.*

/**
 * Точка (гекс) на шестиугольной сетке.
 * Координаты заданы как в примере (первая цифра - y, вторая цифра - x)
 *
 *       60  61  62  63  64  65
 *     50  51  52  53  54  55  56
 *   40  41  42  43  44  45  46  47
 * 30  31  32  33  34  35  36  37  38
 *   21  22  23  24  25  26  27  28
 *     12  13  14  15  16  17  18
 *       03  04  05  06  07  08
 *
 * В примерах к задачам используются те же обозначения точек,
 * к примеру, 16 соответствует HexPoint(x = 6, y = 1), а 41 -- HexPoint(x = 1, y = 4).
 *
 * В задачах, работающих с шестиугольниками на сетке, считать, что они имеют
 * _плоскую_ ориентацию:
 *  __
 * /  \
 * \__/
 *
 * со сторонами, параллельными координатным осям сетки.
 *
 * Более подробно про шестиугольные системы координат можно почитать по следующей ссылке:
 *   https://www.redblobgames.com/grids/hexagons/
 */
data class HexPoint(val x: Int, val y: Int) {
    /**
     * Средняя (3 балла)
     *
     * Найти целочисленное расстояние между двумя гексами сетки.
     * Расстояние вычисляется как число единичных отрезков в пути между двумя гексами.
     * Например, путь межу гексами 16 и 41 (см. выше) может проходить через 25, 34, 43 и 42 и имеет длину 5.
     */
    fun distance(other: HexPoint): Int =
        if (
            (this.x - other.x < 0) && (this.y - other.y < 0)
            || (this.x - other.x > 0) && (this.y - other.y > 0)
        ) abs(this.x - other.x) + abs(this.y - other.y) else max(
            abs(this.x - other.x),
            abs(this.y - other.y)
        )

    override fun toString(): String = "$y.$x"
}

/**
 * Правильный шестиугольник на гексагональной сетке.
 * Как окружность на плоскости, задаётся центральным гексом и радиусом.
 * Например, шестиугольник с центром в 33 и радиусом 1 состоит из гексов 42, 43, 34, 24, 23, 32.
 */
data class Hexagon(val center: HexPoint, val radius: Int) {

    /**
     * Средняя (3 балла)
     *
     * Рассчитать расстояние между двумя шестиугольниками.
     * Оно равно расстоянию между ближайшими точками этих шестиугольников,
     * или 0, если шестиугольники имеют общую точку.
     *
     * Например, расстояние между шестиугольником A с центром в 31 и радиусом 1
     * и другим шестиугольником B с центром в 26 и радиуоом 2 равно 2
     * (расстояние между точками 32 и 24)
     */
    fun distance(other: Hexagon): Int =
        (this.center.distance(other.center) - this.radius - other.radius).let { if (it < 0) 0 else it }

    /**
     * Тривиальная (1 балл)
     *
     * Вернуть true, если заданная точка находится внутри или на границе шестиугольника
     */
    fun contains(point: HexPoint): Boolean = point.distance(this.center) - radius <= 0
}

/**
 * Прямолинейный отрезок между двумя гексами
 */
class HexSegment(val begin: HexPoint, val end: HexPoint) {
    /**
     * Простая (2 балла)
     *
     * Определить "правильность" отрезка.
     * "Правильным" считается только отрезок, проходящий параллельно одной из трёх осей шестиугольника.
     * Такими являются, например, отрезок 30-34 (горизонталь), 13-63 (прямая диагональ) или 51-24 (косая диагональ).
     * А, например, 13-26 не является "правильным" отрезком.
     */
    fun isValid(): Boolean =
        (begin != end) && (begin.x == end.x || begin.y == end.y || (begin.x - end.x == -begin.y + end.y))

    /**
     * Средняя (3 балла)
     *
     * Вернуть направление отрезка (см. описание класса Direction ниже).
     * Для "правильного" отрезка выбирается одно из первых шести направлений,
     * для "неправильного" -- INCORRECT.
     */
    fun direction(): Direction = if (!this.isValid()) Direction.INCORRECT else {
        val deltaX = end.x - begin.x
        val deltaY = end.y - begin.y
        when {
            deltaX > 0 && deltaY < 0 -> Direction.DOWN_RIGHT
            deltaX < 0 && deltaY > 0 -> Direction.UP_LEFT
            deltaX > 0 -> Direction.RIGHT
            deltaY > 0 -> Direction.UP_RIGHT
            deltaX < 0 -> Direction.LEFT
            deltaY < 0 -> Direction.DOWN_LEFT
            else -> Direction.INCORRECT
        }
    }

    override fun equals(other: Any?) =
        other is HexSegment && (begin == other.begin && end == other.end || end == other.begin && begin == other.end)

    override fun hashCode() =
        begin.hashCode() + end.hashCode()
}

/**
 * Направление отрезка на гексагональной сетке.
 * Если отрезок "правильный", то он проходит вдоль одной из трёх осей шестугольника.
 * Если нет, его направление считается INCORRECT
 */
enum class Direction {
    RIGHT,      // слева направо, например 30 -> 34
    UP_RIGHT,   // вверх-вправо, например 32 -> 62
    UP_LEFT,    // вверх-влево, например 25 -> 61
    LEFT,       // справа налево, например 34 -> 30
    DOWN_LEFT,  // вниз-влево, например 62 -> 32
    DOWN_RIGHT, // вниз-вправо, например 61 -> 25
    INCORRECT;  // отрезок имеет изгиб, например 30 -> 55 (изгиб в точке 35)

    /**
     * Простая (2 балла)
     *
     * Вернуть направление, противоположное данному.
     * Для INCORRECT вернуть INCORRECT
     */
    fun opposite(): Direction = when (this) {
        RIGHT -> LEFT
        LEFT -> RIGHT
        UP_RIGHT -> DOWN_LEFT
        UP_LEFT -> DOWN_RIGHT
        DOWN_LEFT -> UP_RIGHT
        DOWN_RIGHT -> UP_LEFT
        INCORRECT -> INCORRECT
    }

    /**
     * Средняя (3 балла)
     *
     * Вернуть направление, повёрнутое относительно
     * заданного на 60 градусов против часовой стрелки.
     *
     * Например, для RIGHT это UP_RIGHT, для UP_LEFT это LEFT, для LEFT это DOWN_LEFT.
     * Для направления INCORRECT бросить исключение IllegalArgumentException.
     * При решении этой задачи попробуйте обойтись без перечисления всех семи вариантов.
     */
    fun next(): Direction = if (this == INCORRECT) throw IllegalArgumentException() else
        values()[(this.ordinal + if (this == DOWN_RIGHT) 2 else 1) % 7]

    /**
     * Простая (2 балла)
     *
     * Вернуть true, если данное направление совпадает с other или противоположно ему.
     * INCORRECT не параллельно никакому направлению, в том числе другому INCORRECT.
     */
    fun isParallel(other: Direction): Boolean = if (this == INCORRECT || other == INCORRECT) false else
        this == other || values().toList().dropLast(1)[(this.ordinal + 3) % 6] == other
}

/**
 * Средняя (3 балла)
 *
 * Сдвинуть точку в направлении direction на расстояние distance.
 * Бросить IllegalArgumentException(), если задано направление INCORRECT.
 * Для расстояния 0 и направления не INCORRECT вернуть ту же точку.
 * Для отрицательного расстояния сдвинуть точку в противоположном направлении на -distance.
 *
 * Примеры:
 * 30, direction = RIGHT, distance = 3 --> 33
 * 35, direction = UP_LEFT, distance = 2 --> 53
 * 45, direction = DOWN_LEFT, distance = 4 --> 05
 */
fun HexPoint.move(direction: Direction, distance: Int): HexPoint =
    if (direction == Direction.INCORRECT) throw IllegalArgumentException() else {
        if (distance == 0) this else
            when (direction) {
                Direction.RIGHT -> HexPoint(this.x + distance, this.y)
                Direction.LEFT -> HexPoint(this.x - distance, this.y)
                Direction.UP_RIGHT -> HexPoint(this.x, this.y + distance)
                Direction.DOWN_LEFT -> HexPoint(this.x, this.y - distance)
                Direction.DOWN_RIGHT -> HexPoint(this.x + distance, this.y - distance)
                else -> HexPoint(this.x - distance, this.y + distance)//Direction.UP_LEFT
            }
    }

/**
 * Сложная (5 баллов)
 *
 * Найти кратчайший путь между двумя заданными гексами, представленный в виде списка всех гексов,
 * которые входят в этот путь.
 * Начальный и конечный гекс также входят в данный список.
 * Если кратчайших путей существует несколько, вернуть любой из них.
 *
 * Пример (для координатной сетки из примера в начале файла):
 *   pathBetweenHexes(HexPoint(y = 2, x = 2), HexPoint(y = 5, x = 3)) ->
 *     listOf(
 *       HexPoint(y = 2, x = 2),
 *       HexPoint(y = 2, x = 3),
 *       HexPoint(y = 3, x = 3),
 *       HexPoint(y = 4, x = 3),
 *       HexPoint(y = 5, x = 3)
 *     )
 *
 *       60  61  62  63  64  65
 *     50  51  52  53  54  55  56
 *   40  41  42  43  44  45  46  47
 * 30  31  32  33  34  35  36  37  38
 *   21  22  23  24  25  26  27  28
 *     12  13  14  15  16  17  18
 *       03  04  05  06  07  08
 *
 */
fun pathBetweenHexes(from: HexPoint, to: HexPoint): List<HexPoint> {
    if (from == to) return listOf(from)
    var x = from.x
    var y = from.y
    val mutableList = mutableListOf(from)
    while (!HexSegment(HexPoint(x, y), to).isValid()) {
        val deltaX = to.x - x
        val deltaY = to.y - y
        if (deltaY > 0) {
            y++
            if (deltaX < 0) x--
            mutableList.add(HexPoint(x, y))
        } else {
            y--
            if (deltaX > 0) x++
            mutableList.add(HexPoint(x, y))
        }
    }
    var dX = 0
    var dY = 0
    when (HexSegment(HexPoint(x, y), to).direction()) {
        Direction.RIGHT -> dX = 1
        Direction.LEFT -> dX = -1
        Direction.UP_RIGHT -> dY = 1
        Direction.DOWN_LEFT -> dY = -1
        Direction.UP_LEFT -> {
            dY = 1
            dX = -1
        }
        Direction.DOWN_RIGHT -> {
            dY = -1
            dX = 1
        }
    }
    while (HexPoint(x, y) != to) {
        x += dX
        y += dY
        mutableList.add(HexPoint(x, y))
    }
    println(mutableList)
    return mutableList
}

/**
 * Очень сложная (20 баллов)
 *
 * Дано три точки (гекса). Построить правильный шестиугольник, проходящий через них
 * (все три точки должны лежать НА ГРАНИЦЕ, а не ВНУТРИ, шестиугольника).
 * Все стороны шестиугольника должны являться "правильными" отрезками.
 * Вернуть null, если такой шестиугольник построить невозможно.
 * Если шестиугольников существует более одного, выбрать имеющий минимальный радиус.
 *
 * Пример: через точки 13, 32 и 44 проходит правильный шестиугольник с центром в 24 и радиусом 2.
 * Для точек 13, 32 и 45 такого шестиугольника не существует.
 * Для точек 32, 33 и 35 следует вернуть шестиугольник радиусом 3 (с центром в 62 или 05).
 *
 * Если все три точки совпадают, вернуть шестиугольник нулевого радиуса с центром в данной точке.
 */
fun checkHex(set: Set<Hexagon>, queue: ArrayDeque<Hexagon>, list: List<HexPoint>, hexagon: Hexagon, dx: Int, dy: Int) {
    if (hexagon !in set) {
        var delta = (Int.MAX_VALUE - max(abs(hexagon.center.x), abs(hexagon.center.y))) / 4
        var newHex = hexagon
        var absDelta = 0
        while (delta > 0) {
            var prom = newHex
            while (list.all { prom.contains(it) }) {
                newHex = prom
                absDelta += delta
                prom = hexagon.center.let {
                    Hexagon(
                        HexPoint(it.x + absDelta * dx, it.y + absDelta * dy),
                        hexagon.radius - absDelta
                    )
                }
            }
            absDelta -= delta
            delta /= 2
        }

        if (newHex != hexagon) {
            queue.add(newHex)
        }
    }
}

fun checkHexes(set: Set<Hexagon>, queue: ArrayDeque<Hexagon>, list: List<HexPoint>, hexagon: Hexagon) {
    checkHex(set, queue, list, hexagon, -1, -1)
    checkHex(set, queue, list, hexagon, 1, 1)
    checkHex(set, queue, list, hexagon, -1, 1)
    checkHex(set, queue, list, hexagon, 1, -1)
    checkHex(set, queue, list, hexagon, 0, 1)
    checkHex(set, queue, list, hexagon, 0, -1)
    checkHex(set, queue, list, hexagon, 1, 0)
    checkHex(set, queue, list, hexagon, -1, 0)
}

fun hexagonByThreePoints(a: HexPoint, b: HexPoint, c: HexPoint): Hexagon? {
    val queue = ArrayDeque<Hexagon>()
    var smallest: Hexagon? = null
    val set = mutableSetOf<Hexagon>()
    val list = listOf(a, b, c)
    fun lastCheck(hexagon: Hexagon, hexPoint: HexPoint) = hexagon.center.distance(hexPoint) == hexagon.radius

    fun calc() {
        while (queue.isNotEmpty()) {
            val hexagon = queue.removeFirst()
            if (lastCheck(hexagon, a) && lastCheck(hexagon, b) && lastCheck(hexagon, c)
                && (smallest == null || hexagon.radius < smallest!!.radius)//тут смарткаст решил не работать
            ) smallest = hexagon
            checkHexes(set, queue, list, hexagon)
            set.add(hexagon)
        }
    }
    queue.add(Hexagon(a, max(a.distance(b), a.distance(c)) * 6))
    calc()
    queue.add(Hexagon(b, max(b.distance(a), b.distance(c)) * 6))
    calc()
    queue.add(Hexagon(c, max(c.distance(a), c.distance(b)) * 6))
    calc()
    return smallest
}

fun main() {
    val p1 = HexPoint(-558, -557)
    val p2 = HexPoint(-558, -558)
    val p3 = HexPoint(565, -999)
    for (i in -1000..1000) {
        for (j in -1000..1000) {
            val g = HexPoint(i, j)
            if (g.distance(p1) == g.distance(p2) && g.distance(p1) == g.distance(p3)) println("$i\t$j\t${g.distance(p3)}")
        }
    }
    val h = hexagonByThreePoints(p1, p2, p3)!!
    println(h)
    println(h.center.distance(p1))
    println(h.center.distance(p2))
    println(h.center.distance(p3))
}

/**
 * Очень сложная (20 баллов)
 *
 * Дано множество точек (гексов). Найти правильный шестиугольник минимального радиуса,
 * содержащий все эти точки (безразлично, внутри или на границе).
 * Если множество пустое, бросить IllegalArgumentException.
 * Если множество содержит один гекс, вернуть шестиугольник нулевого радиуса с центром в данной точке.
 *
 * Пример: 13, 32, 45, 18 -- шестиугольник радиусом 3 (с центром, например, в 15)
 */
fun minContainingHexagon(vararg points: HexPoint): Hexagon {
    val list = points.toList()
    when (list.size) {
        0 -> throw IllegalArgumentException()
        1 -> return Hexagon(list[0], 0)
    }
    val queue = ArrayDeque<Hexagon>()
    var smallest = Hexagon(list[0], list.maxOf { it.distance(list[0]) } * 6)
    val set = mutableSetOf<Hexagon>()

    fun lastCheck(hexagon: Hexagon, hexPoint: HexPoint) = hexagon.center.distance(hexPoint) <= hexagon.radius

    fun calc() {
        while (queue.isNotEmpty()) {
            val hexagon = queue.removeFirst()
            if (hexagon.radius < smallest.radius) smallest = hexagon
            checkHexes(set, queue, list, hexagon)
            set.add(hexagon)
        }
    }
    queue.add(smallest)
    calc()
    return smallest
}