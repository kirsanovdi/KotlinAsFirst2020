@file:Suppress("UNUSED_PARAMETER", "ConvertCallChainIntoSequence")

package lesson6.task1

import java.lang.Exception
import java.util.*
import lesson2.task2.daysInMonth
import lesson4.task1.roman

// Урок 6: разбор строк, исключения
// Максимальное количество баллов = 13
// Рекомендуемое количество баллов = 11
// Вместе с предыдущими уроками (пять лучших, 2-6) = 40/54
val months = listOf(
    "января",
    "февраля",
    "марта",
    "апреля",
    "мая",
    "июня",
    "июля",
    "августа",
    "сентября",
    "октября",
    "ноября",
    "декабря"
)

/**
 * Пример
 *
 * Время представлено строкой вида "11:34:45", содержащей часы, минуты и секунды, разделённые двоеточием.
 * Разобрать эту строку и рассчитать количество секунд, прошедшее с начала дня.
 */
fun timeStrToSeconds(str: String): Int {
    val parts = str.split(":")
    var result = 0
    for (part in parts) {
        val number = part.toInt()
        result = result * 60 + number
    }
    return result
}

/**
 * Пример
 *
 * Дано число n от 0 до 99.
 * Вернуть его же в виде двухсимвольной строки, от "00" до "99"
 */
fun twoDigitStr(n: Int) = if (n in 0..9) "0$n" else "$n"

/**
 * Пример
 *
 * Дано seconds -- время в секундах, прошедшее с начала дня.
 * Вернуть текущее время в виде строки в формате "ЧЧ:ММ:СС".
 */
fun timeSecondsToStr(seconds: Int): String {
    val hour = seconds / 3600
    val minute = (seconds % 3600) / 60
    val second = seconds % 60
    return String.format("%02d:%02d:%02d", hour, minute, second)
}

/**
 * Пример: консольный ввод
 */
fun main() {
    println("Введите время в формате ЧЧ:ММ:СС")
    val line = readLine()
    if (line != null) {
        val seconds = timeStrToSeconds(line)
        if (seconds == -1) {
            println("Введённая строка $line не соответствует формату ЧЧ:ММ:СС")
        } else {
            println("Прошло секунд с начала суток: $seconds")
        }
    } else {
        println("Достигнут <конец файла> в процессе чтения строки. Программа прервана")
    }
}


/**
 * Средняя (4 балла)
 *
 * Дата представлена строкой вида "15 июля 2016".
 * Перевести её в цифровой формат "15.07.2016".
 * День и месяц всегда представлять двумя цифрами, например: 03.04.2011.
 * При неверном формате входной строки вернуть пустую строку.
 *
 * Обратите внимание: некорректная с точки зрения календаря дата (например, 30.02.2009) считается неверными
 * входными данными.
 */
fun dateStrToDigit(str: String): String = str.split(" ").let { date ->
    if (date.size != 3 || date[1] !in months) return@let ""
    try {
        val day = date[0].toInt()
        val month = 1 + months.indexOf(date[1])
        val year = date[2].toInt()
        if (day > daysInMonth(month, year)) return@let ""
        String.format("%02d.%02d.%d", day, month, year)
    } catch (e: Exception) {
        return@let ""
    }
}

/**
 * Средняя (4 балла)
 *
 * Дата представлена строкой вида "15.07.2016".
 * Перевести её в строковый формат вида "15 июля 2016".
 * При неверном формате входной строки вернуть пустую строку
 *
 * Обратите внимание: некорректная с точки зрения календаря дата (например, 30 февраля 2009) считается неверными
 * входными данными.
 */
fun dateDigitToStr(digital: String): String = digital.split(".").let { date ->
    if (date.size != 3) return@let ""
    try {
        val day = date[0].toInt()
        val month = date[1].toInt()
        val year = date[2].toInt()
        if (date[0].toInt() > daysInMonth(month, year)) return@let ""
        "$day " + months[month - 1] + " $year"
    } catch (e: Exception) {
        return@let ""
    }
}

/**
 * Средняя (4 балла)
 *
 * Номер телефона задан строкой вида "+7 (921) 123-45-67".
 * Префикс (+7) может отсутствовать, код города (в скобках) также может отсутствовать.
 * Может присутствовать неограниченное количество пробелов и чёрточек,
 * например, номер 12 --  34- 5 -- 67 -89 тоже следует считать легальным.
 * Перевести номер в формат без скобок, пробелов и чёрточек (но с +), например,
 * "+79211234567" или "123456789" для приведённых примеров.
 * Все символы в номере, кроме цифр, пробелов и +-(), считать недопустимыми.
 * При неверном формате вернуть пустую строку.
 *
 * PS: Дополнительные примеры работы функции можно посмотреть в соответствующих тестах.
 */
fun flattenPhoneNumber(phone: String): String {
    val answer = Regex("""[^0-9+]""").replace(phone, "")
    if (Regex("""[()]""").containsMatchIn(phone) &&
        (Regex("""\).*\(""").containsMatchIn(phone)
                || !Regex("""\(.*\)""").containsMatchIn(phone))
    ) return ""
    if (Regex("""[^0-9+\- ()]""").containsMatchIn(phone)
        || Regex("""\+.*\+""").containsMatchIn(phone)
    ) return ""
    return answer
}

/**
 * Средняя (5 баллов)
 *
 * Результаты спортсмена на соревнованиях в прыжках в длину представлены строкой вида
 * "706 - % 717 % 703".
 * В строке могут присутствовать числа, черточки - и знаки процента %, разделённые пробелами;
 * число соответствует удачному прыжку, - пропущенной попытке, % заступу.
 * Прочитать строку и вернуть максимальное присутствующее в ней число (717 в примере).
 * При нарушении формата входной строки или при отсутствии в ней чисел, вернуть -1.
 */
fun bestLongJump(jumps: String): Int = try {
    jumps.split(" ").filter { it !in setOf("-", "%") }.map { it.toInt() }.maxOf { it }
} catch (e: Exception) {
    -1
}

/**
 * Сложная (6 баллов)
 *
 * Результаты спортсмена на соревнованиях в прыжках в высоту представлены строкой вида
 * "220 + 224 %+ 228 %- 230 + 232 %%- 234 %".
 * Здесь + соответствует удачной попытке, % неудачной, - пропущенной.
 * Высота и соответствующие ей попытки разделяются пробелом.
 * Прочитать строку и вернуть максимальную взятую высоту (230 в примере).
 * При нарушении формата входной строки, а также в случае отсутствия удачных попыток,
 * вернуть -1.
 */
fun bestHighJump(jumps: String): Int = try {
    val data = jumps.split(" ")
    var max = -1
    for (i in data.indices step 2) {
        if (data[i + 1].contains('+') && data[i].toInt() > max) max = data[i].toInt()
    }
    max
} catch (e: Exception) {
    -1
}

/**
 * Сложная (6 баллов)
 *
 * В строке представлено выражение вида "2 + 31 - 40 + 13",
 * использующее целые положительные числа, плюсы и минусы, разделённые пробелами.
 * Наличие двух знаков подряд "13 + + 10" или двух чисел подряд "1 2" не допускается.
 * Вернуть значение выражения (6 для примера).
 * Про нарушении формата входной строки бросить исключение IllegalArgumentException
 */
fun plusMinus(expression: String): Int = try {
    val data = expression.split(" ")
    if (data[0].contains('+') || data[0].contains('-')
        || data.size % 2 == 0
    ) throw IllegalArgumentException()
    var sum = data[0].toInt()
    for (i in 1 until data.size step 2) {
        if (data[i + 1].contains('+') || data[i + 1].contains('-')) throw IllegalArgumentException()
        if (data[i] == "+") sum += data[i + 1].toInt() else sum -= data[i + 1].toInt()
    }
    sum
} catch (e: Exception) {
    throw IllegalArgumentException()
}

/**
 * Сложная (6 баллов)
 *
 * Строка состоит из набора слов, отделённых друг от друга одним пробелом.
 * Определить, имеются ли в строке повторяющиеся слова, идущие друг за другом.
 * Слова, отличающиеся только регистром, считать совпадающими.
 * Вернуть индекс начала первого повторяющегося слова, или -1, если повторов нет.
 * Пример: "Он пошёл в в школу" => результат 9 (индекс первого 'в')
 */
fun firstDuplicateIndex(str: String): Int = str.split(" ").map { it.lowercase() }.let { words ->
    for (i in 0 until words.size - 1) {
        if (words[i] == words[i + 1]) return@let str.map { it.lowercase() }.joinToString("")
            .indexOf(words[i] + " " + words[i])
    }
    -1
}

/**
 * Сложная (6 баллов)
 *
 * Строка содержит названия товаров и цены на них в формате вида
 * "Хлеб 39.9; Молоко 62; Курица 184.0; Конфеты 89.9".
 * То есть, название товара отделено от цены пробелом,
 * а цена отделена от названия следующего товара точкой с запятой и пробелом.
 * Вернуть название самого дорогого товара в списке (в примере это Курица),
 * или пустую строку при нарушении формата строки.
 * Все цены должны быть больше нуля либо равны нулю.
 */
fun mostExpensive(description: String): String = try {
    description.split("; ").map { it.split(" ") }.maxByOrNull { it[1].toDouble() }!![0]
} catch (e: Exception) {
    ""
}

/**
 * Сложная (6 баллов)
 *
 * Перевести число roman, заданное в римской системе счисления,
 * в десятичную систему и вернуть как результат.
 * Римские цифры: 1 = I, 4 = IV, 5 = V, 9 = IX, 10 = X, 40 = XL, 50 = L,
 * 90 = XC, 100 = C, 400 = CD, 500 = D, 900 = CM, 1000 = M.
 * Например: XXIII = 23, XLIV = 44, C = 100
 *
 * Вернуть -1, если roman не является корректным римским числом
 */
fun fromRoman(roman: String): Int {//1678 MDCLXXVIII
    if (roman == "") return -1
    val listSymbol = setOf('I', 'V', 'X', 'L', 'C', 'D', 'M')
    val listValue = listOf(1, 5, 10, 50, 100, 500, 1000)
    for (char in roman) if (char !in listSymbol) return -1
    var max = 0
    var sum = 0
    for (i in roman.length - 1 downTo 0) {
        val idValue = listSymbol.indexOf(roman[i])
        if (idValue < max) sum -= listValue[idValue] else sum += listValue[idValue]
        if (idValue > max) max = idValue
    }
    if (roman(sum) != roman) return -1
    return sum
}

/**
 * Очень сложная (7 баллов)
 *
 * Имеется специальное устройство, представляющее собой
 * конвейер из cells ячеек (нумеруются от 0 до cells - 1 слева направо) и датчик, двигающийся над этим конвейером.
 * Строка commands содержит последовательность команд, выполняемых данным устройством, например +>+>+>+>+
 * Каждая команда кодируется одним специальным символом:
 *	> - сдвиг датчика вправо на 1 ячейку;
 *  < - сдвиг датчика влево на 1 ячейку;
 *	+ - увеличение значения в ячейке под датчиком на 1 ед.;
 *	- - уменьшение значения в ячейке под датчиком на 1 ед.;
 *	[ - если значение под датчиком равно 0, в качестве следующей команды следует воспринимать
 *  	не следующую по порядку, а идущую за соответствующей следующей командой ']' (с учётом вложенности);
 *	] - если значение под датчиком не равно 0, в качестве следующей команды следует воспринимать
 *  	не следующую по порядку, а идущую за соответствующей предыдущей командой '[' (с учётом вложенности);
 *      (комбинация [] имитирует цикл)
 *  пробел - пустая команда
 *
 * Изначально все ячейки заполнены значением 0 и датчик стоит на ячейке с номером N/2 (округлять вниз)
 *
 * После выполнения limit команд или всех команд из commands следует прекратить выполнение последовательности команд.
 * Учитываются все команды, в том числе несостоявшиеся переходы ("[" при значении под датчиком не равном 0 и "]" при
 * значении под датчиком равном 0) и пробелы.
 *
 * Вернуть список размера cells, содержащий элементы ячеек устройства после завершения выполнения последовательности.
 * Например, для 10 ячеек и командной строки +>+>+>+>+ результат должен быть 0,0,0,0,0,1,1,1,1,1
 *
 * Все прочие символы следует считать ошибочными и формировать исключение IllegalArgumentException.
 * То же исключение формируется, если у символов [ ] не оказывается пары.
 * Выход за границу конвейера также следует считать ошибкой и формировать исключение IllegalStateException.
 * Считать, что ошибочные символы и непарные скобки являются более приоритетной ошибкой чем выход за границу ленты,
 * то есть если в программе присутствует некорректный символ или непарная скобка, то должно быть выброшено
 * IllegalArgumentException.
 * IllegalArgumentException должен бросаться даже если ошибочная команда не была достигнута в ходе выполнения.
 *
 */
fun getPair(str: String): Int {
    var insidePower = -1
    for (i in str.indices) {
        when (str[i]) {
            '[' -> insidePower++
            ']' -> if (insidePower == 0) return i else insidePower--
        }
    }
    return 0//невозможный случай, т.к. проверка на loopPair
}

fun computeDeviceCells(cells: Int, commands: String, limit: Int): List<Int> {
    val commandsCheck = commands.filter { it != ' ' }
    var loopPair = 0
    val goodChars = setOf('+', '-', '>', '<', '[', ']')
    for (char in commandsCheck) when (char) {
        !in goodChars -> throw IllegalArgumentException()
        '[' -> loopPair++
        ']' -> loopPair--
    }
    if (loopPair != 0) throw IllegalArgumentException()
    val result = Array<Int>(cells) { 0 }
    var commandCount = 1
    val commandLimit = commands.length
    var carriage = cells / 2
    var command = 0
    val getBack = Stack<Int>()
    while (commandCount <= limit && command < commandLimit) {
        when (commands[command]) {
            '[' -> if (result[carriage] == 0) command += getPair(commands.substring(command))
            else getBack.push(command)
            ']' -> if (result[carriage] != 0) command = getBack.peek() else getBack.pop()
            '+' -> result[carriage]++
            '-' -> result[carriage]--
            '>' -> carriage++
            '<' -> carriage--
        }
        command++
        commandCount++
        if (carriage !in 0 until cells) throw IllegalStateException()
    }
    return result.toList()
}
