@file:Suppress("UNUSED_PARAMETER")

package lesson8.task1

import lesson1.task1.sqr
import java.io.File
import java.util.*
import kotlin.math.*
import kotlin.random.Random.Default.nextDouble

const val delta = 1e-10

data class Point(val x: Double, val y: Double) {
    fun distance(other: Point): Double = sqrt(sqr(x - other.x) + sqr(y - other.y))
    override fun equals(other: Any?) = other is Point && this.x == other.x && this.y == other.y
}

data class Segment(val begin: Point, val end: Point) {

    //fun angleFromOtherToX(): Double = acos((end.x - begin.x) / this.length())

    fun length() = begin.distance(end)

    override fun equals(other: Any?) =
        other is Segment && (begin == other.begin && end == other.end || end == other.begin && begin == other.end)

    override fun hashCode() =
        begin.hashCode() + end.hashCode()
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

//просто вращаем на 90 против часовой
fun getRightZeroVector(vector: ZeroVector): ZeroVector = ZeroVector(-vector.y, vector.x)

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

//проверка на правый поворот a -> b -> c
fun isNotRightTurn(a: Point, b: Point, c: Point): Boolean =
    (b.x - a.x) * (c.y - b.y) - (b.y - a.y) * (c.x - b.x) >= 0

fun isLeftTurn(a: Point, b: Point, c: Point): Boolean =
    (b.x - a.x) * (c.y - b.y) - (b.y - a.y) * (c.x - b.x) > 0

fun ZeroVector.angleWithX(): Double = this.angleWith(ZeroVector(1.0, 0.0))

//алгоритм Грэхема
fun getHull(listInput: List<Point>, precision: Double): List<Point> {
    val p = listInput.minByOrNull { it.y }!!
    //в одну строчку на работает?
    val listFirstSort = listInput.filter { it != p }.sortedBy { p.distance(it) }
    val listSecondSort = listFirstSort.map { Point(toZero(it.x, precision), toZero(it.y, precision)) }
    val listThirdSort = listSecondSort.sortedBy { ZeroVector(p, it).angleWithX() }
    //тонкий момент. далее нужно убрать подряд идущие, и если ох угол через р о остью OX будет одинаковым, то взять самое дальнее
    //sortedBy - stable, сохраняет порядок одинаковых элементов

    //удаление идущих под одним углом
    val list = mutableListOf<Point>()
    for (i in 0..listThirdSort.size - 2) {
        if (abs(
                ZeroVector(p, listThirdSort[i]).angleWithX() - ZeroVector(p, listThirdSort[i + 1]).angleWithX()
            ) > delta
        ) {
            list.add(listThirdSort[i])
        }
    }
    list.add(listThirdSort.last())

    val hull = Stack<Point>()
    hull.add(p)
    hull.add(list[0])
    for (index in 1 until list.size) {
        val pi = list[index]
        while (
            !isNotRightTurn(hull.previous(), hull.last(), pi)
        ) hull.pop()
        hull.push(pi)
    }
    //удаление крайне близких
    val hullAns = mutableListOf<Point>()
    var prev = hull[0]
    hullAns.add(prev)
    for (point in hull) {
        if (point.distance(prev) > precision) hullAns.add(point)
        prev = point
    }
    //удаление идущих в ряд
    //println(hullAns)
    var i = 1
    while (i < hullAns.size - 1) {
        if (!isLeftTurn(hullAns[i - 1], hullAns[i], hullAns[i + 1])) hullAns.removeAt(i) else i++
    }
    println(hullAns)
    return hullAns.toList()
    //return hull
}

fun diameter(vararg points: Point): Segment {
    val hull = getHull(points.toList(), delta)
    var pointIndex = 0
    var oppositeIndex = hull.indices.maxByOrNull { i -> hull[i].y }!!

    //var sumAngle = 0.0

    var result = Segment(hull[0], hull[1])
    var max = result.length()
    var calipersAngle = 0.0

    fun checkMax(point: Point, opposite: Point) {
        val athwart = Segment(point, opposite)
        if (athwart.length() > max) {
            max = athwart.length()
            result = athwart
        }
    }

    while (pointIndex < hull.size) { //движемся против часовой стрелки
        val point = hull.getFromPos(pointIndex)
        val opposite = hull.getFromPos(oppositeIndex)
        val nextPoint = hull.getFromPos(pointIndex + 1)
        val nextOpposite = hull.getFromPos(oppositeIndex + 1)
        val pointVectorMoveTo = ZeroVector(point, nextPoint)
        val oppositeVectorMoveTo = ZeroVector(opposite, nextOpposite)
        checkMax(point, opposite)
        println("${pointIndex % hull.size} ${oppositeIndex % hull.size}")
        var pointAngleMoveTo = pointVectorMoveTo.angleWith(ZeroVector(1.0, 0.0))
        if (pointVectorMoveTo.y < 0) pointAngleMoveTo = 2 * PI - pointAngleMoveTo
        var oppositeAngleMoveTo = oppositeVectorMoveTo.angleWith(ZeroVector(1.0, 0.0))
        if (oppositeVectorMoveTo.y < 0) oppositeAngleMoveTo = 2 * PI - oppositeAngleMoveTo
        val pointAngle = (PI + pointAngleMoveTo - calipersAngle) % PI
        val oppositeAngle = (PI + oppositeAngleMoveTo - (calipersAngle + PI) % (PI * 2)) % PI
        when {
            abs(pointAngle - oppositeAngle) < delta * delta -> {
                pointIndex++
                oppositeIndex++
                checkMax(nextPoint, opposite)
                checkMax(point, nextOpposite)
                calipersAngle += pointAngleMoveTo
            }
            pointAngle < oppositeAngle -> {
                pointIndex++
                calipersAngle = pointAngleMoveTo
            }
            pointAngle > oppositeAngle -> {
                oppositeIndex++
                calipersAngle = oppositeAngleMoveTo
            }
        }
    }
    //println(sumAngle / PI * 180)
    return result
}

fun main() {
    //println(toZero(-2.220446049250313e-16, delta))
    /*while (true) {
        val list = List(200) { Point(nextDouble(-1.0, 1.0), nextDouble(-1.0, 1.0)) }
        val diameter = diameter(*list.toTypedArray())
        val diameterOld = diameterOld(*list.toTypedArray())
        println(abs(diameter.length() - diameterOld.length()) < delta)
    }*/
    //val list = List(20000) { Point(nextDouble(-1000.0, 1000.0), nextDouble(-1000.0, 1000.0)) }
    val list = parse("input/inputAnswer4.txt")
    val hull = getHull(list, delta)
    //println(hull)
    println("-----------------")
    val diameter = diameter(*list.toTypedArray())
    println(diameter)
    println(diameter.length())
    println("${hull.indexOf(diameter.begin)} ${hull.indexOf(diameter.end)}")
    val diameterOld = diameterOld(*list.toTypedArray())
    println(diameterOld)
    println(diameterOld.length())
    println(
        "${
            hull.indexOf(
                diameterOld.begin.let { Point(toZero(it.x, delta), toZero(it.y, delta)) })
        } ${
            hull.indexOf(
                diameterOld.end.let { Point(toZero(it.x, delta), toZero(it.y, delta)) })
        }"
    )
    println("------------------------------------------")
}


