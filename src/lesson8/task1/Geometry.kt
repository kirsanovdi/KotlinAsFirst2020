@file:Suppress("UNUSED_PARAMETER")

package lesson8.task1

import lesson1.task1.sqr
import java.io.File
import java.util.*
import kotlin.math.*

const val delta = 1e-10

data class Point(val x: Double, val y: Double) {
    fun distance(other: Point): Double = sqrt(sqr(x - other.x) + sqr(y - other.y))
    //override fun equals(other: Any?) = other is Point && this.x == other.x && this.y == other.y
}

data class Segment(val begin: Point, val end: Point) {

    //fun angleFromOtherToX(): Double = acos((end.x - begin.x) / this.length())

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

    fun angleBetweenLines(other: Line): Double = abs(other.angle - this.angle)

    override fun equals(other: Any?) = other is Line && angle == other.angle && b == other.b

    override fun hashCode(): Int {
        var result = b.hashCode()
        result = 31 * result + angle.hashCode()
        return result
    }

    override fun toString() = "Line(${cos(angle)} * y = ${sin(angle)} * x + $b)"
}

data class ZeroVector(val x: Double, val y: Double) {
    constructor(begin: Point, end: Point) : this(end.x - begin.x, end.y - begin.y)

    private fun length(): Double = sqrt(x * x + y * y)
    fun angleWith(other: ZeroVector): Double =
        acos((this.x * other.x + this.y * other.y) / (this.length() * other.length()))
}

fun List<Point>.getFromPos(index: Int): Point = this[(this.size + index) % this.size]

fun Stack<Point>.previous(): Point = this[this.size - 2]

fun toZero(a: Double, precision: Double) = if (abs(a) < precision) 0.0 else a

//проверка на правый поворот a -> b -> c
fun isLeftTurn(a: Point, b: Point, c: Point): Boolean =
    //(b.x - a.x) * (c.y - b.y) - (b.y - a.y) * (c.x - b.x) >= 0
    ZeroVector(a, b).angleWith(ZeroVector(b, c)) < PI / 2

//алгоритм Грэхема
fun getHull(listInput: List<Point>, precision: Double): List<Point> {
    val p = listInput.minByOrNull { it.y }!!
    val list =
        listInput.filter { it != p }.sortedBy { ZeroVector(p, it).angleWith(ZeroVector(1.0, 0.0)) }.toMutableList()
    //list.add(list.first())// <-last change
    val hull = Stack<Point>()
    hull.add(p)
    hull.add(list[0])
    for (pi in list) {
        while (hull.size > 1 && !isLeftTurn(hull.previous(), hull.last(), pi)) hull.pop()
        hull.push(pi)
    }

    val hullRaw = hull.map { Point(toZero(it.x, precision), toZero(it.y, precision)) }
    val hullAns = mutableListOf<Point>()
    var prev = Point(-1.1, -1.1)
    for (point in hullRaw) {
        if (point.distance(prev) > precision) hullAns.add(point)
        prev = point
    }

    return hullAns.toList()
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

fun getRightZeroVector(vector: ZeroVector): ZeroVector = //просто вращаем на 90 против часовой
    ZeroVector(-vector.y, vector.x)

fun diameter(vararg points: Point): Segment {
    //hull
    if (points.size < 2) throw IllegalArgumentException()
    if (points.size == 2) return Segment(points[0], points[1])
    val hull = getHull(points.toList(), delta)

    var pointIndex = 0
    var oppositeIndex = hull.indices.maxByOrNull { i -> hull[0].distance(hull[i]) }!!

    //var sumAngle = 0.0

    var result = Segment(hull[0], hull[1])
    var max = result.length()
    while (pointIndex < hull.size) { //движемся против часовой стрелки
        val point = hull.getFromPos(pointIndex)
        val opposite = hull.getFromPos(oppositeIndex)
        val nextPoint = hull.getFromPos(pointIndex + 1)
        val nextOpposite = hull.getFromPos(oppositeIndex + 1)
        val athwart = Segment(point, opposite)
        val pointVector = getRightZeroVector(ZeroVector(opposite, point))// opposite -> point, не наоборот
        val oppositeVector = getRightZeroVector(ZeroVector(point, opposite))
        val pointVectorMoveTo = ZeroVector(point, nextPoint)
        val oppositeVectorMoveTo = ZeroVector(opposite, nextOpposite)
        if (athwart.length() > max) {
            max = athwart.length()
            result = athwart
        }
        //println("${pointIndex % hull.size} ${oppositeIndex % hull.size}")
        val pointAngle = pointVector.angleWith(pointVectorMoveTo)
        val oppositeAngle = oppositeVector.angleWith(oppositeVectorMoveTo)
        when {
            abs(pointAngle - oppositeAngle) < delta -> {
                pointIndex++
                oppositeIndex++
            }
            pointAngle < oppositeAngle -> pointIndex++
            pointAngle > oppositeAngle -> oppositeIndex++
        }
    }
    //println(sumAngle / PI * 180)
    return result
}

fun main() {
    val hull = getHull(parse("input/inputAnswer3.txt"), delta)
    println(hull)
    val diameter = diameter(*parse("input/inputAnswer3.txt").toTypedArray())
    val diameterOld = diameterOld(*parse("input/inputAnswer3.txt").toTypedArray())
    println(diameter.length())
    println("${hull.indexOf(diameter.begin)} ${hull.indexOf(diameter.end)}")
    println(diameterOld.length())
    println("${hull.indexOf(diameterOld.begin)} ${hull.indexOf(diameterOld.end)}")
    println("------------------------------------------")
}


