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
    override fun hashCode(): Int = x.hashCode() + y.hashCode()
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

    override fun equals(other: Any?) = other is Line && angle == other.angle && b == other.b

    override fun hashCode(): Int {
        var result = b.hashCode()
        result = 31 * result + angle.hashCode()
        return result
    }

    override fun toString() = "Line(${cos(angle)} * y = ${sin(angle)} * x + $b)"
}

data class Circle(val center: Point, val radius: Double) {
    /**
     * Простая (2 балла)
     *
     * Рассчитать расстояние между двумя окружностями.
     * Расстояние между непересекающимися окружностями рассчитывается как
     * расстояние между их центрами минус сумма их радиусов.
     * Расстояние между пересекающимися окружностями считать равным 0.0.
     */
    fun distance(other: Circle): Double = (this.center.distance(other.center) - this.radius - other.radius).let {
        if (it > 0.0) it else 0.0
    }

    /**
     * Тривиальная (1 балл)
     *
     * Вернуть true, если и только если окружность содержит данную точку НА себе или ВНУТРИ себя
     */
    fun contains(p: Point): Boolean = this.center.distance(p) <= radius
}

@Suppress("MemberVisibilityCanBePrivate")
class Triangle private constructor(private val points: Set<Point>) {

    private val pointList = points.toList()

    val a: Point get() = pointList[0]

    val b: Point get() = pointList[1]

    val c: Point get() = pointList[2]

    constructor(a: Point, b: Point, c: Point) : this(linkedSetOf(a, b, c))

    /**
     * Пример: полупериметр
     */
    fun halfPerimeter() = (a.distance(b) + b.distance(c) + c.distance(a)) / 2.0

    /**
     * Пример: площадь
     */
    fun area(): Double {
        val p = halfPerimeter()
        return sqrt(p * (p - a.distance(b)) * (p - b.distance(c)) * (p - c.distance(a)))
    }

    /**
     * Пример: треугольник содержит точку
     */
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

//вектор в двумерном пространстве
data class ZeroVector(val x: Double, val y: Double) {
    constructor(begin: Point, end: Point) : this(end.x - begin.x, end.y - begin.y)

    private fun length(): Double = sqrt(x * x + y * y)
    fun angleWith(other: ZeroVector): Double =
        acos((this.x * other.x + this.y * other.y) / (this.length() * other.length()))
}

//индексация как в +, так и в -
fun List<Point>.getFromPos(index: Int): Point = this[(this.size + index) % this.size]

fun Stack<Point>.previous(): Point = this[this.size - 2]

//аппросимация к 0
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

//проверка на правый поворот a -> b -> c (без аппроксимации)
fun isNotRightTurn(a: Point, b: Point, c: Point): Boolean =
    (b.x - a.x) * (c.y - b.y) - (b.y - a.y) * (c.x - b.x) >= 0

//проверка на правый поворот a -> b -> c (исключающая)
fun isLeftTurn(a: Point, b: Point, c: Point): Boolean =
    (b.x - a.x) * (c.y - b.y) - (b.y - a.y) * (c.x - b.x) > 0

//угол с осью OX
fun ZeroVector.angleWithX(): Double = this.angleWith(ZeroVector(1.0, 0.0))

//алгоритм Грэхема
fun getHull(listInput: List<Point>, precision: Double): List<Point> {
    val p = listInput.minByOrNull { it.y }!!.let { Point(toZero(it.x, precision), toZero(it.y, precision)) }
    //в одну строчку на работает?
    val listFirstSort = listInput.map {
        Point(toZero(it.x, precision), toZero(it.y, precision))
    }
    val listSecondSort = listFirstSort.filter { abs(p.distance(it)) > delta }.sortedBy { p.distance(it) }

    val listThirdSort = listSecondSort.sortedBy { ZeroVector(p, it).angleWithX() }
    //тонкий момент. далее нужно убрать подряд идущие, и если ох угол через р о остью OX будет одинаковым,
    // то взять самое дальнее
    //sortedBy - stable, сохраняет порядок одинаковых элементов

    //удаление идущих под одним углом
    val list = mutableListOf<Point>()
    list.add(listThirdSort.first())
    for (i in 1..listThirdSort.size - 2) {
        if (abs(
                ZeroVector(p, listThirdSort[i]).angleWithX() - ZeroVector(p, listThirdSort[i + 1]).angleWithX()
            ) > delta
        ) {
            list.add(listThirdSort[i])
        }
    }
    list.add(listThirdSort.last())
    //сердце алгоритма
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
    if (hull.first().distance(hull.last()) < delta) hull.pop()//для проверки перехода из последней к первой
    hullAns.add(hull[0])
    for (point in hull) {
        if (point.distance(prev) > precision) hullAns.add(point)
        prev = point
    }
    //удаление идущих в ряд
    var i = 1
    while (i < hullAns.size - 1) {
        if (!isLeftTurn(hullAns[i - 1], hullAns[i], hullAns[i + 1])) hullAns.removeAt(i) else i++
    }
    return hullAns.toList()
}

/**
 * Средняя (3 балла)
 *
 * Дано множество точек. Вернуть отрезок, соединяющий две наиболее удалённые из них.
 * Если в множестве менее двух точек, бросить IllegalArgumentException
 */
fun diameter(vararg points: Point): Segment { //diameterCustomTests() <- тесты тут
    if (points.size < 2) throw IllegalArgumentException()
    if (points.size == 2) return Segment(points[0], points[1])
    var hull: List<Point> = listOf()
    try {
        hull = getHull(points.toList(), delta)
    } catch (e: Exception) {
        throw Exception(points.toList().toString())
    }
    if (hull.size < 2) throw Exception(points.toList().toString())
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

    while (pointIndex < hull.size + 1) { //движемся против часовой стрелки
        val point = hull.getFromPos(pointIndex)
        val opposite = hull.getFromPos(oppositeIndex)
        val nextPoint = hull.getFromPos(pointIndex + 1)
        val nextOpposite = hull.getFromPos(oppositeIndex + 1)
        val pointVectorMoveTo = ZeroVector(point, nextPoint)
        val oppositeVectorMoveTo = ZeroVector(opposite, nextOpposite)
        checkMax(point, opposite)
        var pointAngleMoveTo = pointVectorMoveTo.angleWith(ZeroVector(1.0, 0.0))
        if (pointVectorMoveTo.y < 0) pointAngleMoveTo = 2 * PI - pointAngleMoveTo
        var oppositeAngleMoveTo = oppositeVectorMoveTo.angleWith(ZeroVector(1.0, 0.0))
        if (oppositeVectorMoveTo.y < 0) oppositeAngleMoveTo = 2 * PI - oppositeAngleMoveTo
        val pointAngle = (PI * 2 + pointAngleMoveTo - calipersAngle) % PI
        val oppositeAngle = (PI * 2 + oppositeAngleMoveTo - (calipersAngle + PI) % (PI * 2)) % PI
        //println("${pointIndex % hull.size}\t${oppositeIndex % hull.size}\t${hull.size}")
        if (pointIndex % hull.size == oppositeIndex % hull.size) throw Exception(points.toList().toString())
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
            else -> throw Exception(points.toList().toString())
        }
    }
    return result
}

fun main() {
    val list = listOf(
        Point(x = 4.9E-324, y = -632.0), Point(x = 4.9E-324, y = 0.330244380589769), Point(x = 0.0, y = -4.9E-324)
    )
    val diameter = diameter(*list.toTypedArray())
    val diameterOld = diameterOld(*list.toTypedArray())
    val hull = getHull(list, delta)
    println(diameter)
    println(hull.indexOf(diameter.begin))
    println(hull.indexOf(diameter.end))
    println(diameterOld)
    println(hull.indexOf(diameterOld.begin))
    println(hull.indexOf(diameterOld.end))
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
 * Средняя (3 балла)
 *
 * Построить прямую по отрезку
 */
fun lineBySegment(s: Segment): Line = Line(
    s.begin,
    if (abs(s.begin.x - s.end.x) < delta) PI / 2 else (PI + atan((s.begin.y - s.end.y) / (s.begin.x - s.end.x))) % PI
)

/**
 * Средняя (3 балла)
 *
 * Построить прямую по двум точкам
 */
fun lineByPoints(a: Point, b: Point): Line = lineBySegment(Segment(a, b))

/**
 * Сложная (5 баллов)
 *
 * Построить серединный перпендикуляр по отрезку или по двум точкам
 */
fun bisectorByPoints(a: Point, b: Point): Line = Line(
    Point((a.x + b.x) / 2, (a.y + b.y) / 2),
    (lineByPoints(a, b).angle + PI / 2) % PI
)

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
