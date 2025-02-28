@file:Suppress("UNUSED_PARAMETER")

package lesson2.task1

import lesson1.task1.discriminant
import lesson1.task1.numberRevert
import kotlin.math.*

// Урок 2: ветвления (здесь), логический тип (см. 2.2).
// Максимальное количество баллов = 6
// Рекомендуемое количество баллов = 5
// Вместе с предыдущими уроками = 9/12
fun main() {
    //println("Root product: ${timeForHalfWay(1.0, 5.0, 2.0, 4.0, 3.0, 3.0)}")
    //println(triangleKind(4.0, 6.0, 8.0))
}

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
fun ageDescription(age: Int): String = when {
    age % 100 in 11..14 || age % 10 !in 1..4 -> "$age лет"
    age % 10 == 1 -> "$age год"
    else -> "$age года"
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
    val halfWay = (t1 * v1 + t2 * v2 + t3 * v3) / 2.0
    val firstPiece = t1 * v1
    return when {
        halfWay <= firstPiece -> halfWay / v1
        halfWay <= firstPiece + t2 * v2 -> t1 + (halfWay - firstPiece) / v2
        else -> t1 + t2 + t3 - halfWay / v3
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
): Int = (if (kingX == rookX1 || kingY == rookY1) 1 else 0) + (if (kingX == rookX2 || kingY == rookY2) 2 else 0)

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
): Int = (if (kingX == rookX || kingY == rookY) 1 else 0) + (if (abs(kingX - bishopX) == abs(kingY - bishopY)) 2 else 0)

/**
 * Простая (2 балла)
 *
 * Треугольник задан длинами своих сторон a, b, c.
 * Проверить, является ли данный треугольник остроугольным (вернуть 0),
 * прямоугольным (вернуть 1) или тупоугольным (вернуть 2).
 * Если такой треугольник не существует, вернуть -1.
 */
fun triangleKind(a: Double, b: Double, c: Double): Int {
    val maxNum = maxOf(a, b, c)
    val minNum = minOf(a, b, c)
    val avgNum = a + b + c - maxNum - minNum
    val angle = acos((avgNum.pow(2.0) + minNum.pow(2.0) - maxNum.pow(2.0)) / (2.0 * avgNum * minNum))
    return if (maxNum >= minNum + avgNum) -1 else when {
        angle < PI / 2.0 -> 0
        abs(angle - PI / 2.0) < 1e-9 -> 1
        else -> 2
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
    val deltaLen = min(d, b) - max(a, c)
    return if (deltaLen >= 0) deltaLen else -1
}
