@file:Suppress("UNUSED_PARAMETER")

package lesson2.task1

import lesson1.task1.discriminant
import kotlin.math.max
import kotlin.math.sqrt
import kotlin.math.min
import kotlin.math.abs

/**
 * Пример
 *
 * Найти число корней квадратного уравнения ax^2 + bx + c = 0
 */
fun quadraticRootNumber(a: Double, b: Double, c: Double): Int {
    val discriminant = discriminant(a, b, c)
    return when {
        discriminant > 0.0 -> 2
        discriminant == 0.0 -> 1
        else -> 0
    }
}

/**
 * Пример
 *
 * Получить строковую нотацию для оценки по пятибалльной системе
 */
fun gradeNotation(grade: Int): String = when (grade) {
    5 -> "отлично"
    4 -> "хорошо"
    3 -> "удовлетворительно"
    2 -> "неудовлетворительно"
    else -> "несуществующая оценка $grade"
}

/**
 * Пример
 *
 * Найти наименьший корень биквадратного уравнения ax^4 + bx^2 + c = 0
 */
fun minBiRoot(a: Double, b: Double, c: Double): Double {

    if (a == 0.0) {
        if (b == 0.0) return Double.NaN
        val bc = -c / b
        if (bc < 0.0) return Double.NaN
        return -sqrt(bc)

    }
    val d = discriminant(a, b, c)
    if (d < 0.0) return Double.NaN
    // 4
    val y1 = (-b + sqrt(d)) / (2 * a)
    val y2 = (-b - sqrt(d)) / (2 * a)
    val y3 = max(y1, y2)
    if (y3 < 0.0) return Double.NaN
    return -sqrt(y3)
}

/**
 * Простая (2 балла)
 *
 * Мой возраст. Для заданного 0 < n < 200, рассматриваемого как возраст человека,
 * вернуть строку вида: «21 год», «32 года», «12 лет».
 */
fun ageDescription(age: Int): String {

    if (age % 100 >= 10 && age % 100 <= 20) return "$age лет"
    return when (age % 10) {
        1 -> "$age год"
        2, 3, 4 -> "$age года"
        5, 6, 7, 8, 9, 0 -> "$age лет"
        else -> "No age"
    }
}

/**
 * Простая (2 балла)
 *
 * Путник двигался t1 часов со скоростью v1 км/час, затем t2 часов — со скоростью v2 км/час
 * и t3 часов — со скоростью v3 км/час.
 * Определить, за какое время он одолел первую половину пути?
 */
fun timeForHalfWay(
    t1: Double, v1: Double,
    t2: Double, v2: Double,
    t3: Double, v3: Double
): Double {
    var t1v1 = t1 * v1
    var t2v2 = t2 * v2
    var t3v3 = t3 * v3
    var s = t1v1 + t2v2 + t3v3

    var s2 = (t1v1 + t2v2 + t3v3) / 2

    return when {
        t1v1 < s2 && (t1v1 + t2v2) < s2 -> t1 + t2 + (s2 - (t1v1 + t2v2)) / v3
        t1v1 < s2 -> t1 + (s2 - t1v1) / v2
        else -> s2 / v1
    }

}

/**
 * Простая (2 балла)
 *
 * Нa шахматной доске стоят черный король и две белые ладьи (ладья бьет по горизонтали и вертикали).
 * Определить, не находится ли король под боем, а если есть угроза, то от кого именно.
 * Вернуть 0, если угрозы нет, 1, если угроза только от первой ладьи, 2, если только от второй ладьи,
 * и 3, если угроза от обеих ладей.
 * Считать, что ладьи не могут загораживать друг друга
 */
fun whichRookThreatens(
    kingX: Int, kingY: Int,
    rookX1: Int, rookY1: Int,
    rookX2: Int, rookY2: Int
): Int = when {
    (kingX == rookX1 || kingY == rookY1) && (kingX == rookX2 || kingY == rookY2) -> 3
    kingX == rookX1 || kingY == rookY1 -> 1
    kingX == rookX2 || kingY == rookY2 -> 2
    else -> 0
}


/**
 * Простая (2 балла)
 *
 * На шахматной доске стоят черный король и белые ладья и слон
 * (ладья бьет по горизонтали и вертикали, слон — по диагоналям).
 * Проверить, есть ли угроза королю и если есть, то от кого именно.
 * Вернуть 0, если угрозы нет, 1, если угроза только от ладьи, 2, если только от слона,
 * и 3, если угроза есть и от ладьи и от слона.
 * Считать, что ладья и слон не могут загораживать друг друга.
 */
fun rookOrBishopThreatens(
    kingX: Int, kingY: Int,
    rookX: Int, rookY: Int,
    bishopX: Int, bishopY: Int
): Int {
    return if (kingX == rookX || kingY == rookY) {
        if (abs(kingX - bishopX) == abs(kingY - bishopY)) 3
        else 1
    } else if (abs(kingX - bishopX) == abs(kingY - bishopY)) 2
    else 0
}

/**
 * Простая (2 балла)
 *
 * Треугольник задан длинами своих сторон a, b, c.
 * Проверить, является ли данный треугольник остроугольным (вернуть 0),
 * прямоугольным (вернуть 1) или тупоугольным (вернуть 2).
 * Если такой треугольник не существует, вернуть -1.
 */
fun triangleKind(a: Double, b: Double, c: Double): Int {
    val mx = maxOf(a, b, c)
    val mn = minOf(a, b, c)
    val md = (a + b + c) - mx - mn

    return when {
        mx >= a + b + c - mx -> -1
        md * md + mn * mn == mx * mx -> 1
        md * md + mn * mn < mx * mx -> 2
        else -> 0
    }

}

/**
 * Средняя (3 балла)
 *
 * Даны четыре точки на одной прямой: A, B, C и D.
 * Координаты точек a, b, c, d соответственно, b >= a, d >= c.
 * Найти длину пересечения отрезков AB и CD.
 * Если пересечения нет, вернуть -1.
 */
fun segmentLength(a: Int, b: Int, c: Int, d: Int): Int {
    val mx = max(a, c)
    val mn = min(d, b)
    return if (mn - mx >= 0) mn - mx
    else -1


}
