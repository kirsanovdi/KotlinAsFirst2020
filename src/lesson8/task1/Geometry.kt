@file:Suppress("UNUSED_PARAMETER")

package lesson8.task1

import lesson1.task1.sqr
import java.io.File
import java.util.*
import kotlin.math.*

const val delta = 1e-9

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

    fun angleBetweenLines(other: Line): Double = abs(PI + this.angle - other.angle) % (PI / 2)

    override fun equals(other: Any?) = other is Line && angle == other.angle && b == other.b

    override fun hashCode(): Int {
        var result = b.hashCode()
        result = 31 * result + angle.hashCode()
        return result
    }

    override fun toString() = "Line(${cos(angle)} * y = ${sin(angle)} * x + $b)"
}

fun List<Point>.getFromPos(index: Int): Point = this[(this.size + index) % this.size]

fun Stack<Point>.previous(): Point = this[this.size - 2]

//проверка на правый поворот a -> b -> c
fun isLeftTurn(a: Point, b: Point, c: Point): Boolean =
    (b.x - a.x) * (c.y - b.y) - (b.y - a.y) * (c.x - b.x) >= 0

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

fun circleByDiameter(diameter: Segment): Circle = Circle(
    Point((diameter.begin.x + diameter.end.x) / 2, (diameter.begin.y + diameter.end.y) / 2),
    diameter.begin.distance(diameter.end) / 2
)

fun lineBySegment(s: Segment): Line = Line(
    s.begin,
    if (abs(s.begin.x - s.end.x) < delta) PI / 2 else (PI + atan((s.begin.y - s.end.y) / (s.begin.x - s.end.x))) % PI
)

fun lineByPoints(a: Point, b: Point): Line = lineBySegment(Segment(a, b))

fun bisectorByPoints(a: Point, b: Point): Line = Line(
    Point((a.x + b.x) / 2, (a.y + b.y) / 2),
    (lineByPoints(a, b).angle + PI / 2) % PI
)

fun athwartByPoints(a: Point, b: Point): Line = Line(
    a,
    (lineByPoints(a, b).angle + PI / 2) % PI
)

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
    var pointIndex = 0
    val hull = getHull(points.toList())
    var oppositeIndex = hull.indices.maxByOrNull { i -> hull[i].distance(hull[0]) }!!

    var result = Segment(hull[0], hull[1])
    var max = result.length()
    while (pointIndex < hull.size) { //движемся против часовой стрелки
        val point = hull.getFromPos(pointIndex)
        val opposite = hull.getFromPos(oppositeIndex)
        val nextPoint = hull.getFromPos(pointIndex + 1)
        val nextOpposite = hull.getFromPos(oppositeIndex + 1)
        val athwart = Segment(point, opposite)
        val pointLine = athwartByPoints(point, opposite)
        val oppositeLine = athwartByPoints(opposite, point)
        val pointLineMoveTo = lineByPoints(point, nextPoint)
        val oppositeLineMoveTo = lineByPoints(opposite, nextOpposite)
        if (athwart.length() > max) {
            max = athwart.length()
            result = athwart
        }
        println("$pointIndex $oppositeIndex")
        if (pointLineMoveTo.angleBetweenLines(pointLine) < oppositeLineMoveTo.angleBetweenLines(oppositeLine)) {
            pointIndex++
        } else oppositeIndex++
    }
    return result
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
    //display(list)
    //println(diameter(*list.toTypedArray()))
    val listParse = parse("input/inputAnswer.txt")
    //println(getHull(listParse))
    //println(getHull(listParse).size)
    val diameter = diameter(*listParse.toTypedArray())
    val diameterOld = diameterOld(*listParse.toTypedArray())
    println(diameter)
    println(diameter.length())
    println(diameterOld)
    println(diameterOld.length())
    /*println(getHull(listParse))
    println(segment)
    println(segment.length())
    println(segmentOld)
    println(segmentOld.length())*/
    //display(list)
    //println("")
    //display(getHull(list))
}

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

