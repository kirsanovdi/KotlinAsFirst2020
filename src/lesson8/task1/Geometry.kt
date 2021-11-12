@file:Suppress("UNUSED_PARAMETER")

package lesson8.task1

import lesson1.task1.sqr
import java.io.File
import java.util.*
import kotlin.math.*

const val delta = 1e-9
// Урок 8: простые классы
// Максимальное количество баллов = 40 (без очень трудных задач = 11)

fun List<Point>.getFromPos(index: Int): Point = this[(this.size + index) % this.size]

fun Stack<Point>.previous(): Point = this[this.size - 2]

//алгоритм Грэхема
fun getHull(listInput: List<Point>): List<Point> {
    val p = listInput.minByOrNull { it.y }!!
    val list = listInput.filter { it != p }.sortedBy { Segment(p, it).angleFromOtherToX() }

    val hull = Stack<Point>()
    hull.add(p)
    hull.add(list[0])
    for (pi in list) {
        while (hull.size > 1 && !isLeftTurn(hull.previous(), hull.last(), pi)) hull.pop()
        hull.push(pi)
    }
    return hull.toList()
}

//проверка на правый поворот a -> b -> c
fun isLeftTurn(a: Point, b: Point, c: Point): Boolean =
    (b.x - a.x) * (c.y - b.y) - (b.y - a.y) * (c.x - b.x) >= -delta

//будет ли пересекать перпендикулярная прямая оболочку более чем в одной точке
fun goodArrow(down: Point, up: Point, left: Point, right: Point): Boolean {
    //угол между up-down и up-left/right < 90
    //будет немного лишних значений из-за delta, иначе неточность double съест нужные
    if (((down.x - up.x) * (left.x - up.x) + (down.y - up.y) * (left.y - up.y)) <= delta) return false
    if (((down.x - up.x) * (right.x - up.x) + (down.y - up.y) * (right.y - up.y)) <= delta) return false
    return true//ещё делить на длину, но длина + и на знак не влияет
}
//отображение 10x10
fun display(list: List<Point>) {
    val desk: Array<Array<Int>> = Array(10) { Array(10) { 0 } }
    for ((x, y) in list) {
        desk[desk.size - 1 - y.toInt()][x.toInt()] = 1
    }
    for (arrLine in desk) println(arrLine.joinToString())
}
//парсер данных, на которых возникла ошибка
fun parse(inputName: String): MutableList<Point> {
    var i = -2
    var x = 0.0
    var y = 0.0
    val mutableList = mutableListOf<Point>()
    File(inputName).forEachLine { line ->
        if (i == 4) i = 0
        when (i) {
            0 -> x = line.trim().dropLast(1).split(Regex("""\s"""))[1].toDouble()
            1 -> y = line.trim().split(Regex("""\s"""))[1].toDouble()
            2 -> mutableList.add(Point(x, y))
        }
        i++

    }
    return mutableList
}
//старый метод за N(O^2)
fun diameterOld(vararg points: Point): Segment {
    if (points.size < 2) throw IllegalArgumentException()
    if (points.size == 2) return Segment(points[0], points[1])
    val list = points.toMutableList()
    var maxLen = 0.0
    var maxSegment = Segment(list[0], list[1])
    var remain = list.size - 1
    while (remain != 0) {
        for (i in 0..remain) {
            if (list[remain].distance(list[i]) > maxLen) {
                maxLen = list[remain].distance(list[i])
                maxSegment = Segment(list[i], list[remain])
            }
        }
        remain--
    }
    return maxSegment
}

fun main() {
    val list = listOf(
        Point(2.0, 1.0),
        Point(4.0, 0.0),
        Point(5.0, 6.0),
        Point(3.0, 2.0),
        Point(4.0, 4.0),
        Point(6.0, 2.0),
        Point(3.0, 4.0),
        Point(7.0, 7.0),
        Point(2.0, 2.0)
    )
    val listParse = parse("input/inputAnswer.txt")
    val segment = diameter(*listParse.toTypedArray())
    val segmentOld = diameterOld(*listParse.toTypedArray())
    println(segment)
    println(segment.length())
    println(segmentOld)
    println(segmentOld.length())
    //display(list)
    //println("")
    //display(getHull(list))
}

data class Point(val x: Double, val y: Double) {
    fun distance(other: Point): Double = sqrt(sqr(x - other.x) + sqr(y - other.y))
}

@Suppress("MemberVisibilityCanBePrivate")
class Triangle private constructor(private val points: Set<Point>) {
    private val pointList = points.toList()
    val a: Point get() = pointList[0]
    val b: Point get() = pointList[1]
    val c: Point get() = pointList[2]

    constructor(a: Point, b: Point, c: Point) : this(linkedSetOf(a, b, c))

    fun halfPerimeter() = (a.distance(b) + b.distance(c) + c.distance(a)) / 2.0
    fun area(): Double {
        val p = halfPerimeter()
        return sqrt(p * (p - a.distance(b)) * (p - b.distance(c)) * (p - c.distance(a)))
    }

    fun contains(p: Point): Boolean {
        val abp = Triangle(a, b, p)
        val bcp = Triangle(b, c, p)
        val cap = Triangle(c, a, p)
        return abp.area() + bcp.area() + cap.area() <= area()
    }

    override fun equals(other: Any?) = other is Triangle && points == other.points
    override fun hashCode() = points.hashCode()
    override fun toString() = "Triangle(a = $a, b = $b, c = $c)"
}


data class Circle(val center: Point, val radius: Double) {
    fun distance(other: Circle): Double = (this.center.distance(other.center) - this.radius - other.radius).let {
        if (it > 0.0) it else 0.0
    }

    fun contains(p: Point): Boolean = this.center.distance(p) <= radius
}

data class Segment(val begin: Point, val end: Point) {

    fun angleFromOtherToX(): Double = acos((end.x - begin.x) / this.length())

    fun length() = begin.distance(end)

    override fun equals(other: Any?) =
        other is Segment && (begin == other.begin && end == other.end || end == other.begin && begin == other.end)

    override fun hashCode() =
        begin.hashCode() + end.hashCode()
}


/**
 * Средняя (3 балла)
 *
 * Дано множество точек. Вернуть отрезок, соединяющий две наиболее удалённые из них.
 * Если в множестве менее двух точек, бросить IllegalArgumentException
 */
fun diameter(vararg points: Point): Segment {
    //hull
    if (points.size < 2) throw IllegalArgumentException()
    if (points.size == 2) return Segment(points[0], points[1])
    if (points.size == 3) {
        val list3Points = listOf(
            Segment(points[0], points[1]),
            Segment(points[1], points[2]),
            Segment(points[0], points[2])
        )
        return list3Points.maxByOrNull { it.length() }!!
    }
    val hull = getHull(points.toList())
    val hullSize = hull.size
    //вращающиеся калиперы
    var pIndex = 0
    var oppositeIndex = hull.indexOf(hull.maxByOrNull { it.distance(hull[0]) })
    // для поиска большего
    var maxLen = 0.0
    var result = Segment(points[0], points[1])
    //обход в круг с проверкой на непересекаемость противоположной с помощью правильной стрелки
    while (pIndex < hullSize) {
        val len = hull.getFromPos(pIndex)
            .distance(hull.getFromPos(oppositeIndex))//тем не менее соседи могут быть самыми дальними(трапеция)
        if (len > maxLen) {
            maxLen = len
            result = Segment(hull.getFromPos(pIndex), hull.getFromPos(oppositeIndex))
        }
        when (pIndex) {
            (hullSize + oppositeIndex + 1) % hullSize -> pIndex++ //если down совпадает с left
            (hullSize + oppositeIndex - 1) % hullSize -> oppositeIndex++ //если down совпадает с right
            else -> if (goodArrow(
                    hull.getFromPos(pIndex),
                    hull.getFromPos(oppositeIndex),
                    hull.getFromPos(oppositeIndex + 1),
                    hull.getFromPos(oppositeIndex - 1)
                )
            ) pIndex++ else oppositeIndex++
        }
    }
    //println(result)
    return result
}

/**
 * Простая (2 балла)
 *
 * Построить окружность по её диаметру, заданному двумя точками
 * Центр её должен находиться посередине между точками, а радиус составлять половину расстояния между ними
 */
fun circleByDiameter(diameter: Segment): Circle = Circle(
    Point((diameter.begin.x + diameter.end.x) / 2, (diameter.begin.y + diameter.end.y) / 2),
    diameter.begin.distance(diameter.end) / 2
)

/**
 * Прямая, заданная точкой point и углом наклона angle (в радианах) по отношению к оси X.
 * Уравнение прямой: (y - point.y) * cos(angle) = (x - point.x) * sin(angle)
 * или: y * cos(angle) = x * sin(angle) + b, где b = point.y * cos(angle) - point.x * sin(angle).
 * Угол наклона обязан находиться в диапазоне от 0 (включительно) до PI (исключительно).
 */
class Line private constructor(val b: Double, val angle: Double) {
    init {
        require(angle >= 0 && angle < PI) { "Incorrect line angle: $angle" }
    }

    constructor(point: Point, angle: Double) : this(point.y * cos(angle) - point.x * sin(angle), angle)

    /**
     * Средняя (3 балла)
     *
     * Найти точку пересечения с другой линией.
     * Для этого необходимо составить и решить систему из двух уравнений (каждое для своей прямой)
     */
    fun crossPoint(other: Line): Point {
        if (abs(this.angle - PI / 2) <= 1e-5) return Point(
            -this.b,
            (-this.b * sin(other.angle) + other.b) / cos(other.angle)
        )
        if (abs(other.angle - PI / 2) <= 1e-5) return Point(
            -other.b,
            (-other.b * sin(this.angle) + this.b) / cos(this.angle)
        )
        val x = (this.b / cos(this.angle) - other.b / cos(other.angle)) / (tan(other.angle) - tan(this.angle))
        val y = (x * sin(this.angle) + this.b) / cos(this.angle)
        return Point(x, y)
    }

    override fun equals(other: Any?) = other is Line && angle == other.angle && b == other.b

    override fun hashCode(): Int {
        var result = b.hashCode()
        result = 31 * result + angle.hashCode()
        return result
    }

    override fun toString() = "Line(${cos(angle)} * y = ${sin(angle)} * x + $b)"
}

/**
 * Средняя (3 балла)
 *
 * Построить прямую по отрезку
 */
fun lineBySegment(s: Segment): Line = TODO()

/**
 * Средняя (3 балла)
 *
 * Построить прямую по двум точкам
 */
fun lineByPoints(a: Point, b: Point): Line = TODO()

/**
 * Сложная (5 баллов)
 *
 * Построить серединный перпендикуляр по отрезку или по двум точкам
 */
fun bisectorByPoints(a: Point, b: Point): Line = TODO()

/**
 * Средняя (3 балла)
 *
 * Задан список из n окружностей на плоскости.
 * Найти пару наименее удалённых из них; расстояние между окружностями
 * рассчитывать так, как указано в Circle.distance.
 *
 * При наличии нескольких наименее удалённых пар,
 * вернуть первую из них по порядку в списке circles.
 *
 * Если в списке менее двух окружностей, бросить IllegalArgumentException
 */
fun findNearestCirclePair(vararg circles: Circle): Pair<Circle, Circle> = TODO()

/**
 * Сложная (5 баллов)
 *
 * Дано три различные точки. Построить окружность, проходящую через них
 * (все три точки должны лежать НА, а не ВНУТРИ, окружности).
 * Описание алгоритмов см. в Интернете
 * (построить окружность по трём точкам, или
 * построить окружность, описанную вокруг треугольника - эквивалентная задача).
 */
fun circleByThreePoints(a: Point, b: Point, c: Point): Circle = TODO()

/**
 * Очень сложная (10 баллов)
 *
 * Дано множество точек на плоскости. Найти круг минимального радиуса,
 * содержащий все эти точки. Если множество пустое, бросить IllegalArgumentException.
 * Если множество содержит одну точку, вернуть круг нулевого радиуса с центром в данной точке.
 *
 * Примечание: в зависимости от ситуации, такая окружность может либо проходить через какие-либо
 * три точки данного множества, либо иметь своим диаметром отрезок,
 * соединяющий две самые удалённые точки в данном множестве.
 */
fun minContainingCircle(vararg points: Point): Circle = TODO()

