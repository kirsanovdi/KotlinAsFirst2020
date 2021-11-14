@file:Suppress("UNUSED_PARAMETER", "ConvertCallChainIntoSequence")

package lesson7.task1

import java.io.File
import java.io.PrintStream
import java.lang.Exception
import java.util.*
import javax.swing.plaf.basic.BasicSplitPaneDivider
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random.Default.nextInt
//import java.lang.StringBuilder
import kotlin.text.StringBuilder

// Урок 7: работа с файлами
// Урок интегральный, поэтому его задачи имеют сильно увеличенную стоимость
// Максимальное количество баллов = 55
// Рекомендуемое количество баллов = 20
// Вместе с предыдущими уроками (пять лучших, 3-7) = 55/103

/**
 * Пример
 *
 * Во входном файле с именем inputName содержится некоторый текст.
 * Вывести его в выходной файл с именем outputName, выровняв по левому краю,
 * чтобы длина каждой строки не превосходила lineLength.
 * Слова в слишком длинных строках следует переносить на следующую строку.
 * Слишком короткие строки следует дополнять словами из следующей строки.
 * Пустые строки во входном файле обозначают конец абзаца,
 * их следует сохранить и в выходном файле
 */
fun alignFile(inputName: String, lineLength: Int, outputName: String) {
    val writer = File(outputName).bufferedWriter()
    var currentLineLength = 0
    fun append(word: String) {
        if (currentLineLength > 0) {
            if (word.length + currentLineLength >= lineLength) {
                writer.newLine()
                currentLineLength = 0
            } else {
                writer.write(" ")
                currentLineLength++
            }
        }
        writer.write(word)
        currentLineLength += word.length
    }
    for (line in File(inputName).readLines()) {
        if (line.isEmpty()) {
            writer.newLine()
            if (currentLineLength > 0) {
                writer.newLine()
                currentLineLength = 0
            }
            continue
        }
        for (word in line.split(Regex("\\s+"))) {
            append(word)
        }
    }
    writer.close()
}

/**
 * Простая (8 баллов)
 *
 * Во входном файле с именем inputName содержится некоторый текст.
 * Некоторые его строки помечены на удаление первым символом _ (подчёркивание).
 * Перенести в выходной файл с именем outputName все строки входного файла, убрав при этом помеченные на удаление.
 * Все остальные строки должны быть перенесены без изменений, включая пустые строки.
 * Подчёркивание в середине и/или в конце строк значения не имеет.
 */
fun deleteMarked(inputName: String, outputName: String) {
    PrintStream(File(outputName)).use { printStream ->
        File(inputName).forEachLine { line ->
            if (line.isEmpty() || line[0] != '_') printStream.println(line)
        }
    }
}

/**
 * Средняя (14 баллов)
 *
 * Во входном файле с именем inputName содержится некоторый текст.
 * На вход подаётся список строк substrings.
 * Вернуть ассоциативный массив с числом вхождений каждой из строк в текст.
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 *
 */
fun countSubstrings(inputName: String, substrings: List<String>): Map<String, Int> {
    val mutableMap = mutableMapOf<String, Int>()
    val regexPair = substrings.filter { it.length > 1 }.toSet() //не одинаковые символы
        .map { keyword -> Pair(keyword, keyword.lowercase().toRegex(RegexOption.LITERAL)) }
    val singlePair = substrings.filter { it.length == 1 }.toSet()
        .map { simLine -> Pair(simLine, simLine.lowercase()[0]) }

    fun goThroughLine(line: String, pair: Pair<String, Regex>) {
        var i = 0
        var sumUp = 0
        while (i < line.length) {
            val resultFind = pair.second.find(line, i)
            if (resultFind != null) {
                i = resultFind.range.first + 1
                sumUp++
            } else break
        }
        mutableMap[pair.first] = (mutableMap[pair.first] ?: 0) + sumUp
    }

    fun forSingleSubstrings(line: String, pair: Pair<String, Char>) {
        mutableMap[pair.first] = (mutableMap[pair.first] ?: 0) + line.count { it == pair.second }
    }

    File(inputName).forEachLine { line ->
        val lineLower = line.lowercase()
        for (pair in regexPair) goThroughLine(lineLower, pair)
        for (pair in singlePair) forSingleSubstrings(lineLower, pair)
    }
    return mutableMap
}


fun transform(matchResult: MatchResult): CharSequence {
    val mistake = matchResult.value
    return listOf(
        mistake[0],
        when (mistake[1]) {
            'ы' -> 'и'
            'Ы' -> 'И'
            'ю' -> 'у'
            'Ю' -> 'У'
            'я' -> 'а'
            'Я' -> 'А'
            else -> '-'
        }
    ).joinToString("")
}

/**
 * Средняя (12 баллов)
 *
 * В русском языке, как правило, после букв Ж, Ч, Ш, Щ пишется И, А, У, а не Ы, Я, Ю.
 * Во входном файле с именем inputName содержится некоторый текст на русском языке.
 * Проверить текст во входном файле на соблюдение данного правила и вывести в выходной
 * файл outputName текст с исправленными ошибками.
 *
 * Регистр заменённых букв следует сохранять.
 *
 * Исключения (жюри, брошюра, парашют) в рамках данного задания обрабатывать не нужно
 *
 */
fun sibilants(inputName: String, outputName: String) {
    val regex = Regex("""[жчшщ][ыяю]""", RegexOption.IGNORE_CASE)
    PrintStream(File(outputName)).use { printStream ->
        File(inputName).forEachLine { line ->
            printStream.println(regex.replace(line, ::transform))
        }
    }
}

/**
 * Средняя (15 баллов)
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 * Вывести его в выходной файл с именем outputName, выровняв по центру
 * относительно самой длинной строки.
 *
 * Выравнивание следует производить путём добавления пробелов в начало строки.
 *
 *
 * Следующие правила должны быть выполнены:
 * 1) Пробелы в начале и в конце всех строк не следует сохранять.
 * 2) В случае невозможности выравнивания строго по центру, строка должна быть сдвинута в ЛЕВУЮ сторону
 * 3) Пустые строки не являются особым случаем, их тоже следует выравнивать
 * 4) Число строк в выходном файле должно быть равно числу строк во входном (в т. ч. пустых)
 *
 */
fun centerFile(inputName: String, outputName: String) {
    val max = File(inputName).readLines().let {
        if (it.isEmpty()) 0 else it.maxOf { line -> line.trim().length }
    }
    PrintStream(File(outputName)).use { printStream ->
        File(inputName).forEachLine { line ->//repeat
            val len = line.trim().length
            printStream.println(" ".repeat((max - len) / 2) + line.trim())
        }
    }
}

/**
 * Сложная (20 баллов)
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 * Вывести его в выходной файл с именем outputName, выровняв по левому и правому краю относительно
 * самой длинной строки.
 * Выравнивание производить, вставляя дополнительные пробелы между словами: равномерно по всей строке
 *
 * Слова внутри строки отделяются друг от друга одним или более пробелом.
 *
 * Следующие правила должны быть выполнены:
 * 1) Каждая строка входного и выходного файла не должна начинаться или заканчиваться пробелом.
 * 2) Пустые строки или строки из пробелов трансформируются в пустые строки без пробелов.
 * 3) Строки из одного слова выводятся без пробелов.
 * 4) Число строк в выходном файле должно быть равно числу строк во входном (в т. ч. пустых).
 *
 * Равномерность определяется следующими формальными правилами:
 * 5) Число пробелов между каждыми двумя парами соседних слов не должно отличаться более, чем на 1.
 * 6) Число пробелов между более левой парой соседних слов должно быть больше или равно числу пробелов
 *    между более правой парой соседних слов.
 *
 * Следует учесть, что входной файл может содержать последовательности из нескольких пробелов  между слвоами. Такие
 * последовательности следует учитывать при выравнивании и при необходимости избавляться от лишних пробелов.
 * Из этого следуют следующие правила:
 * 7) В самой длинной строке каждая пара соседних слов должна быть отделена В ТОЧНОСТИ одним пробелом
 * 8) Если входной файл удовлетворяет требованиям 1-7, то он должен быть в точности идентичен выходному файлу
 */
fun alignFileByWidth(inputName: String, outputName: String) {
    var max = 0
    val regex = Regex("""[ ]+""")
    File(inputName).forEachLine { line ->
        val len = regex.replace(line, " ").trim().length
        //println(regex.replace(line, " ").trim())
        if (len > max) max = len
    }
    PrintStream(File(outputName)).use { printStream ->
        File(inputName).forEachLine { lineNotTrim ->//repeat
            val line = lineNotTrim.trim()
            if (line == "" || line.all { it == ' ' }) printStream.println("") else {
                val len = line.count { it != ' ' }
                val words = line.split(regex)
                if (words.count() == 1) printStream.println(words[0]) else {
                    val countPossibleSpaces = words.count() - 1
                    val pSum = (max - len) / countPossibleSpaces
                    var delta = (max - len) - pSum * countPossibleSpaces
                    val stringBuilder = StringBuilder()
                    for (word in words) {
                        stringBuilder.append(word)
                        stringBuilder.append(" ".repeat(pSum + (if (delta > 0) 1 else 0)))
                        //println(pSum + (if (delta >= 0) 1 else 0))
                        delta--
                    }
                    //printStream.println(stringBuilder.toString())
                    printStream.println(stringBuilder.trim().toString())
                }
            }
        }
    }
}

/**
 * Средняя (14 баллов)
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 *
 * Вернуть ассоциативный массив, содержащий 20 наиболее часто встречающихся слов с их количеством.
 * Если в тексте менее 20 различных слов, вернуть все слова.
 * Вернуть ассоциативный массив с числом слов больше 20, если 20-е, 21-е, ..., последнее слова
 * имеют одинаковое количество вхождений (см. также тест файла input/onegin.txt).
 *
 * Словом считается непрерывная последовательность из букв (кириллических,
 * либо латинских, без знаков препинания и цифр).
 * Цифры, пробелы, знаки препинания считаются разделителями слов:
 * Привет, привет42, привет!!! -привет?!
 * ^ В этой строчке слово привет встречается 4 раза.
 *
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 * Ключи в ассоциативном массиве должны быть в нижнем регистре.
 *
 */
fun top20Words(inputName: String): Map<String, Int> {
    val mutableMap = mutableMapOf<String, Int>()
    val regex = Regex("""[^a-zA-Zа-яА-ЯёЁ]+""")
    File(inputName).forEachLine { line ->
        val words = line.trim().lowercase().split(regex)
        for (word in words) {
            mutableMap[word] = (mutableMap[word] ?: 0) + 1
        }
    }
    mutableMap.remove("")
    val topList = mutableMap.toList().sortedByDescending { (_, value) -> value }
    mutableMap.clear()
    for (i in 0 until min(20, topList.size)) {
        mutableMap[topList[i].first] = topList[i].second
    }
    if (topList.size > 20) {
        val minMax = topList[19].second
        for ((key, value) in topList) {
            if (value == minMax) {
                mutableMap[key] = value
            }
        }
    }
    return mutableMap
}

/**
 * Средняя (14 баллов)
 *
 * Реализовать транслитерацию текста из входного файла в выходной файл посредством динамически задаваемых правил.

 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 *
 * В ассоциативном массиве dictionary содержится словарь, в котором некоторым символам
 * ставится в соответствие строчка из символов, например
 * mapOf('з' to "zz", 'р' to "r", 'д' to "d", 'й' to "y", 'М' to "m", 'и' to "yy", '!' to "!!!")
 *
 * Необходимо вывести в итоговый файл с именем outputName
 * содержимое текста с заменой всех символов из словаря на соответствующие им строки.
 *
 * При этом регистр символов в словаре должен игнорироваться,
 * но при выводе символ в верхнем регистре отображается в строку, начинающуюся с символа в верхнем регистре.
 *
 * Пример.
 * Входной текст: Здравствуй, мир!
 *
 * заменяется на
 *
 * Выходной текст: Zzdrавствуy, mир!!!
 *
 * Пример 2.
 *
 * Входной текст: Здравствуй, мир!
 * Словарь: mapOf('з' to "zZ", 'р' to "r", 'д' to "d", 'й' to "y", 'М' to "m", 'и' to "YY", '!' to "!!!")
 *
 * заменяется на
 *
 * Выходной текст: Zzdrавствуy, mир!!!
 *
 * Обратите внимание: данная функция не имеет возвращаемого значения
 */
fun transliterate(inputName: String, dictionary: Map<Char, String>, outputName: String) {
    /*val regexes = dictionary.map { (key, value) ->
        fun(nextWrapper: (String) -> String) =
            fun(line: String) = nextWrapper(Regex(key.lowercase()).replace(line, value.lowercase()))
    }
    val great = regexes.fold(fun(line: String) = line) { prev, curr -> curr(prev) }
    File(inputName).forEachLine { line -> printStream.println(great(line)) }
    */
    val regexLetters = Regex("""[a-zа-яё]""")
    val replacement = mutableMapOf<Char, String>()
    for ((key, value) in dictionary) {
        replacement[key.lowercaseChar()] = value.lowercase()
        if (regexLetters.matches(key.lowercase()))
            replacement[key.uppercaseChar()] = value.lowercase()//далее capitalize
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }
    val stringBuilder = StringBuilder()
    PrintStream(File(outputName)).use { printStream ->
        File(inputName).forEachLine { line ->
            for (char in line) stringBuilder.append(if (char in replacement) replacement[char] else char)
            printStream.println(stringBuilder.toString())
            stringBuilder.clear()
        }
    }
}

/**
 * Средняя (12 баллов)
 *
 * Во входном файле с именем inputName имеется словарь с одним словом в каждой строчке.
 * Выбрать из данного словаря наиболее длинное слово,
 * в котором все буквы разные, например: Неряшливость, Четырёхдюймовка.
 * Вывести его в выходной файл с именем outputName.
 * Если во входном файле имеется несколько слов с одинаковой длиной, в которых все буквы разные,
 * в выходной файл следует вывести их все через запятую.
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 *
 * Пример входного файла:
 * Карминовый
 * Боязливый
 * Некрасивый
 * Остроумный
 * БелогЛазый
 * ФиолетОвый

 * Соответствующий выходной файл:
 * Карминовый, Некрасивый
 *
 * Обратите внимание: данная функция не имеет возвращаемого значения
 */
fun different(string: String): Boolean = string.length == string.toSet().size

fun chooseLongestChaoticWord(inputName: String, outputName: String) {
    val mutableList = mutableListOf<String>()
    var max = 0
    File(inputName).forEachLine { line ->
        if (different(line.lowercase())) when (val len = line.length) {
            !in 0..max -> {
                mutableList.clear()
                max = len
                mutableList.add(line)
            }
            max -> {
                mutableList.add(line)
            }
        }
    }
    PrintStream(File(outputName)).use { printStream -> printStream.println(mutableList.joinToString()) }
}

/**
 * Сложная (22 балла)
 *
 * Реализовать транслитерацию текста в заданном формате разметки в формат разметки HTML.
 *
 * Во входном файле с именем inputName содержится текст, содержащий в себе элементы текстовой разметки следующих типов:
 * - *текст в курсивном начертании* -- курсив
 * - **текст в полужирном начертании** -- полужирный
 * - ~~зачёркнутый текст~~ -- зачёркивание
 *
 * Следует вывести в выходной файл этот же текст в формате HTML:
 * - <i>текст в курсивном начертании</i>
 * - <b>текст в полужирном начертании</b>
 * - <s>зачёркнутый текст</s>
 *
 * Кроме того, все абзацы исходного текста, отделённые друг от друга пустыми строками, следует обернуть в теги <p>...</p>,
 * а весь текст целиком в теги <html><body>...</body></html>.
 *
 * Все остальные части исходного текста должны остаться неизменными с точностью до наборов пробелов и переносов строк.
 * Отдельно следует заметить, что открывающая последовательность из трёх звёздочек (***) должна трактоваться как "<b><i>"
 * и никак иначе.
 *
 * При решении этой и двух следующих задач полезно прочитать статью Википедии "Стек".
 *
 * Пример входного файла:
Lorem ipsum *dolor sit amet*, consectetur **adipiscing** elit.
Vestibulum lobortis, ~~Est vehicula rutrum *suscipit*~~, ipsum ~~lib~~ero *placerat **tortor***,

Suspendisse ~~et elit in enim tempus iaculis~~.
 *
 * Соответствующий выходной файл:
<html>
<body>
<p>
Lorem ipsum <i>dolor sit amet</i>, consectetur <b>adipiscing</b> elit.
Vestibulum lobortis. <s>Est vehicula rutrum <i>suscipit</i></s>, ipsum <s>lib</s>ero <i>placerat <b>tortor</b></i>.
</p>
<p>
Suspendisse <s>et elit in enim tempus iaculis</s>.
</p>
</body>
</html>
 *
 * (Отступы и переносы строк в примере добавлены для наглядности, при решении задачи их реализовывать не обязательно)
 */
fun toHtml(string: String): String {
    val stringBuilder = StringBuilder()
    var lastIndex = 0 //optimization
    var prevIndex = 0
    var open = true
    val sickRegex = Regex("""~~""")
    var matchResult = sickRegex.find(string, lastIndex)
    while (matchResult != null) {
        lastIndex = matchResult.range.first
        stringBuilder.append(string.substring(prevIndex, lastIndex)).append(if (open) "<s>" else "</s>")
        prevIndex = lastIndex + 2
        matchResult = sickRegex.find(string, lastIndex + 2)
        open = !open
    }
    stringBuilder.append(string.substring(prevIndex))

    val line = stringBuilder.toString()
    val nearestRegex = Regex("""\*{1,3}""")
    val stack = Stack<String>()

    stringBuilder.clear()
    lastIndex = 0 //optimization
    prevIndex = 0
    matchResult = nearestRegex.find(line, lastIndex)

    while (matchResult != null) {
        val near = matchResult.value
        lastIndex = matchResult.range.first
        val replacement = when (near.length) {
            1 -> if (!stack.empty() && stack.peek() == "i") "</${stack.pop()}>" else {
                stack.add("i")
                "<i>"
            }
            2 -> if (!stack.empty() && stack.peek() == "b") "</${stack.pop()}>" else {
                stack.add("b")
                "<b>"
            }
            3 -> {
                if (stack.empty()) {
                    stack.push("b")
                    stack.push("i")
                    "<b><i>"
                } else {
                    val ret = stack.pop()
                    if (!stack.empty()) "</$ret></${stack.pop()}>" else {
                        if (ret == "b") {
                            stack.add("i")
                            //println("</$ret><i>")
                            "</$ret><i>"
                        } else {
                            stack.add("b")
                            //println("</$ret><b>")
                            "</$ret><b>"
                        }
                    }
                }
            }
            else -> "!!!"
        }
        stringBuilder.append(line.substring(prevIndex, lastIndex)).append(replacement)
        prevIndex = lastIndex + near.length
        matchResult = nearestRegex.find(line, lastIndex + near.length)
    }
    stringBuilder.append(line.substring(prevIndex))
    return stringBuilder.toString()
}

//fun main() {
//printDivisionProcess(123269, 8000, "input/test.txt")
//756269 784214
//190654 337921
//615914 631180
/*for (i in 0..100) {
        val a = nextInt(1, 1000000)
        val b = nextInt(1, 1000000)
        try {
            printDivisionProcess(a, b, "input/test.txt")
        } catch (e: Exception) {
            println("$a $b")
        }
    }*/
//}

fun markdownToHtmlSimple(inputName: String, outputName: String) {
    val regexParagraph = Regex("""\s*""")
    val stringBuilder = StringBuilder()
    stringBuilder.append("<html><body><p>")
    File(inputName).forEachLine { line ->
        if (regexParagraph.matches(line)) stringBuilder.append("</p><p>") else stringBuilder.append(line)
    }
    stringBuilder.append("</p></body></html>")
    val toWorkAt = stringBuilder.toString().replace("<p></p>", "")
    PrintStream(File(outputName)).use { printStream ->
        if (toWorkAt == "<html><body></body></html>")
            printStream.println("<html><body><p></p></body></html>") else printStream.println(toHtml(toWorkAt))
    }
}

/**
 * Сложная (23 балла)
 *
 * Реализовать транслитерацию текста в заданном формате разметки в формат разметки HTML.
 *
 * Во входном файле с именем inputName содержится текст, содержащий в себе набор вложенных друг в друга списков.
 * Списки бывают двух типов: нумерованные и ненумерованные.
 *
 * Каждый элемент ненумерованного списка начинается с новой строки и символа '*', каждый элемент нумерованного списка --
 * с новой строки, числа и точки. Каждый элемент вложенного списка начинается с отступа из пробелов, на 4 пробела большего,
 * чем список-родитель. Максимально глубина вложенности списков может достигать 6. "Верхние" списки файла начинются
 * прямо с начала строки.
 *
 * Следует вывести этот же текст в выходной файл в формате HTML:
 * Нумерованный список:
 * <ol>
 *     <li>Раз</li>
 *     <li>Два</li>
 *     <li>Три</li>
 * </ol>
 *
 * Ненумерованный список:
 * <ul>
 *     <li>Раз</li>
 *     <li>Два</li>
 *     <li>Три</li>
 * </ul>
 *
 * Кроме того, весь текст целиком следует обернуть в теги <html><body><p>...</p></body></html>
 *
 * Все остальные части исходного текста должны остаться неизменными с точностью до наборов пробелов и переносов строк.
 *
 * Пример входного файла:
///////////////////////////////начало файла/////////////////////////////////////////////////////////////////////////////
 * Утка по-пекински
 * Утка
 * Соус
 * Салат Оливье
1. Мясо
 * Или колбаса
2. Майонез
3. Картофель
4. Что-то там ещё
 * Помидоры
 * Фрукты
1. Бананы
23. Яблоки
1. Красные
2. Зелёные
///////////////////////////////конец файла//////////////////////////////////////////////////////////////////////////////
 *
 *
 * Соответствующий выходной файл:
///////////////////////////////начало файла/////////////////////////////////////////////////////////////////////////////
<html>
<body>
<p>
<ul>
<li>
Утка по-пекински
<ul>
<li>Утка</li>
<li>Соус</li>
</ul>
</li>
<li>
Салат Оливье
<ol>
<li>Мясо
<ul>
<li>Или колбаса</li>
</ul>
</li>
<li>Майонез</li>
<li>Картофель</li>
<li>Что-то там ещё</li>
</ol>
</li>
<li>Помидоры</li>
<li>Фрукты
<ol>
<li>Бананы</li>
<li>Яблоки
<ol>
<li>Красные</li>
<li>Зелёные</li>
</ol>
</li>
</ol>
</li>
</ul>
</p>
</body>
</html>
///////////////////////////////конец файла//////////////////////////////////////////////////////////////////////////////
 * (Отступы и переносы строк в примере добавлены для наглядности, при решении задачи их реализовывать не обязательно)
 */
fun markdownToHtmlLists(inputName: String, outputName: String) {
    var nestingLevel = -1
    val stack = Stack<String>()
    val regexSpace = Regex(""" *""")
    val regexStar = Regex("""\*""")
    val regexNumber = Regex("""\d+\.""")
    val regexAll = Regex("""\d+\.|\*""")
    val stringBuilder = StringBuilder()
    stringBuilder.append("<html><body><p>")
    File(inputName).forEachLine { line ->
        val currentNestingLevel = (regexSpace.find(line)?.value?.length ?: 0) / 4
        if (currentNestingLevel == nestingLevel) {
            stringBuilder.append("</li>").append(regexAll.replace(line, "<li>"))
        }
        if (currentNestingLevel < nestingLevel) {
            stringBuilder.append("</li></${stack.pop()}></li>")
            stringBuilder.append(regexAll.replace(line, "<li>"))
        }
        if (currentNestingLevel > nestingLevel) {
            if (regexStar.find(line) != null) {
                stack.add("ul")
                stringBuilder.append("<ul>")
                stringBuilder.append(regexAll.replace(line, "<li>"))
            }
            if (regexNumber.find(line) != null) {
                stack.add("ol")
                stringBuilder.append("<ol>")
                stringBuilder.append(regexAll.replace(line, "<li>"))
            }
        }
        nestingLevel = currentNestingLevel
    }
    while (!stack.empty()) {
        stringBuilder.append("</li></${stack.pop()}>")
    }
    stringBuilder.append("</p></body></html>")
    PrintStream(File(outputName)).use { printStream -> printStream.println(stringBuilder.toString()) }
}

/**
 * Очень сложная (30 баллов)
 *
 * Реализовать преобразования из двух предыдущих задач одновременно над одним и тем же файлом.
 * Следует помнить, что:
 * - Списки, отделённые друг от друга пустой строкой, являются разными и должны оказаться в разных параграфах выходного файла.
 *
 */
fun markdownToHtml(inputName: String, outputName: String) {
    var nestingLevel = -1
    val stack = Stack<String>()
    val regexSpace = Regex(""" *""")
    val regexStar = Regex("""\*""")
    val regexNumber = Regex("""\d+\.""")
    val regexAll = Regex("""\d+\.|\*""")
    val regexParagraph = Regex("""\s*""")
    val regexSpacePlus = Regex(""" +""")
    val stringBuilder = StringBuilder()
    stringBuilder.append("<html><body><p>")
    File(inputName).forEachLine { line ->
        val currentNestingLevel = (regexSpace.find(line)?.value?.length ?: 0) / 4
        if (regexParagraph.matches(line)) {
            nestingLevel = 0
            while (!stack.empty()) {
                stringBuilder.append("</li></${stack.pop()}>")
            }
            stringBuilder.append("</p><p>")
        } else {
            val workingPart = line.trim().split(regexSpacePlus)[0]
            if (!regexAll.matches(workingPart)) {
                stringBuilder.append(line)
            } else {
                if (currentNestingLevel == nestingLevel) {
                    stringBuilder.append("</li>").append(regexAll.replaceFirst(line, "<li>"))
                }
                if (currentNestingLevel < nestingLevel) {
                    stringBuilder.append("</li></${stack.pop()}></li>")
                    stringBuilder.append(regexAll.replaceFirst(line, "<li>"))
                }
                if (currentNestingLevel > nestingLevel) {
                    if (regexStar.find(line) != null) {
                        stack.add("ul")
                        stringBuilder.append("<ul>")
                        stringBuilder.append(regexAll.replaceFirst(line, "<li>"))
                    }
                    if (regexNumber.find(line) != null) {
                        stack.add("ol")
                        stringBuilder.append("<ol>")
                        stringBuilder.append(regexAll.replaceFirst(line, "<li>"))
                    }
                }
            }
            nestingLevel = currentNestingLevel
        }
    }
    while (!stack.empty()) {
        stringBuilder.append("</li></${stack.pop()}>")
    }
    stringBuilder.append("</p></body></html>")
    PrintStream(File(outputName)).use { printStream -> printStream.println(toHtml(stringBuilder.toString())) }
}

/**
 * Средняя (12 баллов)
 *
 * Вывести в выходной файл процесс умножения столбиком числа lhv (> 0) на число rhv (> 0).
 *
 * Пример (для lhv == 19935, rhv == 111):
19935
 *    111
--------
19935
+ 19935
+19935
--------
2212785
 * Используемые пробелы, отступы и дефисы должны в точности соответствовать примеру.
 * Нули в множителе обрабатывать так же, как и остальные цифры:
235
 *  10
-----
0
+235
-----
2350
 *
 */
fun printMultiplicationProcess(lhv: Int, rhv: Int, outputName: String) {
    val lines = mutableListOf<String>()
    val trueMultiplication = lhv * rhv
    var secondValue = rhv
    var spacesBehind = -1
    while (secondValue != 0) {
        val digit = secondValue % 10
        secondValue /= 10
        spacesBehind++
        lines.add((lhv * digit).toString())
    }
    val absLength = max(spacesBehind + lines.last().length + 1, trueMultiplication.toString().length + 1)
    PrintStream(File(outputName)).use { printStream ->
        printStream.println(" ".repeat(absLength - lhv.toString().length) + lhv.toString())
        printStream.println("*" + " ".repeat(absLength - rhv.toString().length - 1) + rhv.toString())
        printStream.println("-".repeat(absLength))
        printStream.println(" ".repeat(absLength - lines[0].length) + lines[0])
        for (i in 1 until lines.size) {
            printStream.println("+" + " ".repeat(absLength - lines[i].length - 1 - i) + lines[i])
        }
        printStream.println("-".repeat(absLength))
        printStream.println(" ".repeat(absLength - trueMultiplication.toString().length) + trueMultiplication.toString())
    }
}


/**
 * Сложная (25 баллов)
 *
 * Вывести в выходной файл процесс деления столбиком числа lhv (> 0) на число rhv (> 0).
 *
 * Пример (для lhv == 19935, rhv == 22):
19935 | 22
-198     906
----
13
-0
--
135
-132
----
3

 * Используемые пробелы, отступы и дефисы должны в точности соответствовать примеру.
 *
 */
fun printDivisionProcess(lhv: Int, rhv: Int, outputName: String) {
    PrintStream(File(outputName)).use { printStream ->
        val trueDivision = lhv / rhv
        val lhvString = lhv.toString()
        val stringBuilder = StringBuilder()
        fun getSubtrahendResult(endAt: Int, remain: Int, nextChar: Char): Int {
            val number = remain * 10 + (nextChar - '0')
            val subtrahend = number - number % rhv
            val result = number % rhv
            val numberString = "$remain$nextChar"
            val subtrahendString = subtrahend.toString()
            val dashLength = max(numberString.length, subtrahendString.length + 1)
            stringBuilder.appendLine(" ".repeat(endAt - numberString.length) + numberString)
            stringBuilder.appendLine(" ".repeat(endAt - subtrahendString.length - 1) + "-" + subtrahendString)
            stringBuilder.appendLine(" ".repeat(endAt - dashLength) + "-".repeat(dashLength))
            return result
        }

        var delta = 0
        var symbolIndex = 0
        while (delta < rhv && symbolIndex < lhvString.length) {
            delta = delta * 10 + (lhvString[symbolIndex] - '0')
            symbolIndex++
        }
        var result = delta % rhv
        val subtrahend = delta - result
        val deltaString = delta.toString()
        val subtrahendString = subtrahend.toString()
        val needSpace = subtrahendString.length == deltaString.length
        val needSpaceOne = if (needSpace) 1 else 0
        stringBuilder.appendLine("${if (needSpace) " " else ""}$lhv | $rhv")
        val firstSpacesCount = max(deltaString.length - subtrahendString.length - 1, 0)
        stringBuilder.appendLine(
            " ".repeat(firstSpacesCount) +
                    "-" + subtrahendString +
                    " ".repeat(lhvString.length + 2 + needSpaceOne - firstSpacesCount - subtrahendString.length) +
                    trueDivision.toString()
        )
        stringBuilder.appendLine("-".repeat(deltaString.length + needSpaceOne))
        var endAt = deltaString.length + needSpaceOne
        for (i in deltaString.length until lhvString.length) {
            val char = lhvString[i]
            result = getSubtrahendResult(++endAt, result, char)
        }
        stringBuilder.append(" ".repeat(endAt - result.toString().length) + result.toString())
        printStream.println(stringBuilder.toString())
    }
}

fun main() {
    PrintStream("input/markdown_simple_custom.md").use { printStream ->
        printStream.println("   \nasdfhbd\nfgdfg\n   \nfddf")
    }
    markdownToHtmlSimple("input/markdown_simple_custom.md", "temp.html")
    printDivisionProcess(4987, 7396, "input/test.txt")
}

