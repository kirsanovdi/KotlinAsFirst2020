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
    val listFirstSort = listInput.filter { it != p }.sortedBy { p.distance(it) }
    val listSecondSort = listFirstSort.map {
        Point(toZero(it.x, precision), toZero(it.y, precision))
    }
    val listThirdSort = listSecondSort.sortedBy { ZeroVector(p, it).angleWithX() }
    //тонкий момент. далее нужно убрать подряд идущие, и если ох угол через р о остью OX будет одинаковым,
    // то взять самое дальнее
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
fun diameter(vararg points: Point): Segment {
    if (points.size < 2) throw IllegalArgumentException()
    if (points.size == 2) return Segment(points[0], points[1])
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
        val pointAngle = (PI + pointAngleMoveTo - calipersAngle) % PI
        val oppositeAngle = (PI * 2 + oppositeAngleMoveTo - (calipersAngle + PI) % (PI * 2)) % PI
        //println("$pointIndex $oppositeIndex ${hull.size}")
        if (pointIndex % hull.size == oppositeIndex % hull.size) throw Exception(hull.joinToString())
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
            else -> throw Exception(hull.joinToString())
        }
    }
    return result
}

fun main() {
    val list = listOf(
        Point(x = 2940.4250972118607, y = 82553.11372431956),
        Point(x = -9438.476602139068, y = 36713.628666449746),
        Point(x = 5612.884267914278, y = 20899.053458871185),
        Point(x = -1736.9391221472615, y = 40055.409052606854),
        Point(x = -5758.272463010794, y = 54911.21888312324),
        Point(x = -2529.2006387421325, y = 65430.62306082931),
        Point(x = 7323.733494232489, y = 7153.076195175268),
        Point(x = 7803.587201633338, y = 62065.04042058319),
        Point(x = 3183.9117559637725, y = 8741.200015875846),
        Point(x = -7053.58234132289, y = -9719.87166632303),
        Point(x = -554.8588665749121, y = 61196.121090170185),
        Point(x = 1210.5464667795168, y = 77038.31281114632),
        Point(x = 73.20320829919547, y = 84681.86948705015),
        Point(x = 3542.3559549505444, y = 86534.6470763328),
        Point(x = 7639.431520991406, y = -6644.591678245411),
        Point(x = 7492.329078035014, y = 95984.75753396032),
        Point(x = -8412.293887340273, y = 54513.82689545744),
        Point(x = 4112.499087941664, y = 30694.41097849149),
        Point(x = -7937.919870422116, y = 87631.57599781505),
        Point(x = 8832.8098952059, y = 44869.180369374364),
        Point(x = 3245.837509098181, y = 82221.71151676361),
        Point(x = 7073.814798385596, y = 35433.16264518663),
        Point(x = 182.31934977328092, y = 39734.511290605784),
        Point(x = -9314.792604740074, y = 95373.78950010016),
        Point(x = 8692.389237589752, y = -5576.455627464579),
        Point(x = -7743.319253143642, y = 30815.30525261948),
        Point(x = 9272.93485378505, y = 31263.617908973298),
        Point(x = -9070.33572006497, y = 32146.535380689013),
        Point(x = -7244.661961591947, y = 85596.81943899175),
        Point(x = -4454.499305439961, y = 34089.10055733099),
        Point(x = 8022.374716596969, y = 49212.14602437319),
        Point(x = 8797.675670891302, y = 42590.332609867575),
        Point(x = 226.53626799758968, y = 21031.026866204273),
        Point(x = -487.2751486297493, y = 37860.594543869694),
        Point(x = -8277.699899775102, y = 77475.33575011551),
        Point(x = -733.5130633786212, y = 62747.875258767),
        Point(x = 7319.402685480927, y = 17936.304601296335),
        Point(x = -5750.595629810853, y = 83952.97827106174),
        Point(x = -9516.300473340772, y = 74542.00602673284),
        Point(x = 3626.5856540258337, y = 10336.714900540821),
        Point(x = -2243.2606040445344, y = 42637.92013635343),
        Point(x = 1595.4061669212424, y = 7756.062382472093),
        Point(x = -5065.309968318803, y = -950.6640849117357),
        Point(x = -8074.582055492767, y = 73489.2824705431),
        Point(x = -9558.195014457464, y = 49286.23697683691),
        Point(x = 972.4083593885389, y = 15370.539239044334),
        Point(x = 8853.138401012544, y = 62380.45785665944),
        Point(x = -2009.5539121028241, y = 31252.592865491002),
        Point(x = -9334.242135596844, y = 23011.884762055437),
        Point(x = 22.80473964837438, y = 93351.7496590101),
        Point(x = 3187.2166912773664, y = 94984.59924828554),
        Point(x = -7265.319812732607, y = 19402.219160938585),
        Point(x = 1804.6080483218211, y = 47955.791731305115),
        Point(x = -1742.3574036439495, y = 18357.60445570324),
        Point(x = -9082.976997938547, y = 20599.461553741945),
        Point(x = 6447.805407299238, y = 12625.346657542366),
        Point(x = -541.2084559157211, y = 98103.81239981877),
        Point(x = -3083.281125633468, y = 1505.386773572507),
        Point(x = 7210.39615567432, y = 50055.48068871273),
        Point(x = -4223.9733185850855, y = 51627.49609413687),
        Point(x = -5011.500540225442, y = 46355.5624229946),
        Point(x = -6756.040862771075, y = 49742.09770648552),
        Point(x = 4326.037816540635, y = 3841.6980601550113),
        Point(x = -3906.237009830744, y = 81217.65001866154),
        Point(x = 397.8818166701076, y = 84757.2435064407),
        Point(x = 2381.205064826081, y = 4033.022844349871),
        Point(x = -4282.919309395761, y = 60112.9245161272),
        Point(x = 4970.310572907987, y = 52926.46426567385),
        Point(x = -8478.744945383587, y = 16328.061325103248),
        Point(x = -4916.464601152142, y = 41673.25845158398),
        Point(x = 8788.21559161341, y = 82317.85856327399),
        Point(x = 7434.601065715204, y = 93212.23706419987),
        Point(x = -8535.565153312282, y = 86409.96603736328),
        Point(x = 4459.244429222646, y = 27526.818373556147),
        Point(x = -9240.487412596502, y = 98127.86815364801),
        Point(x = -1258.8178767053578, y = 15809.352965903829),
        Point(x = 1254.3236620500356, y = -1029.5857536575277),
        Point(x = -62.39661876945138, y = 86498.05463757802),
        Point(x = -2909.946367594458, y = 18846.137556690344),
        Point(x = 5340.54368658686, y = 14273.850349975426),
        Point(x = 9649.871265566002, y = 50787.7943009689),
        Point(x = -8332.468670379778, y = 67040.06283796625),
        Point(x = -3085.4942099791097, y = 15976.817315993336),
        Point(x = 9756.91134429081, y = -1719.169485719629),
        Point(x = -932.4586816519495, y = 93949.24739116196),
        Point(x = -6449.700937698195, y = -6995.2348868707195),
        Point(x = -5054.241141410205, y = 29133.918965297504),
        Point(x = -9923.358576013972, y = 14728.380876525156),
        Point(x = 9574.400926694998, y = 94590.55173616721),
        Point(x = -1890.6106093059143, y = -1360.6540979873353),
        Point(x = -5703.248412789221, y = 96969.61790982574),
        Point(x = 9750.330767362073, y = 42202.714723268014),
        Point(x = 7901.966452630048, y = 33307.74904874875),
        Point(x = 1400.643140751199, y = 29033.75869433923),
        Point(x = 9674.614020429017, y = 40301.63156709487),
        Point(x = -1588.2213435279555, y = 4859.189376196684),
        Point(x = -6566.078311582682, y = 170.22728941072273),
        Point(x = 1165.811192503188, y = 55823.94044247597),
        Point(x = 5400.248220899637, y = 24119.415671002578),
        Point(x = 9416.911014296158, y = -726.0857712923134)
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
