@file:Suppress("UNUSED_PARAMETER", "ConvertCallChainIntoSequence")

package lesson7.task1

import java.io.File
import java.io.PrintStream
import java.util.*
import kotlin.math.min
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
    val printStream = PrintStream(File(outputName))
    File(inputName).forEachLine { line ->
        if (line.isEmpty() || line[0] != '_') printStream.println(line)
    }
    printStream.close()
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
    val printStream = PrintStream(File(outputName))
    //var s = ""
    File(inputName).forEachLine { line ->
        //s += regex.replace(line, ::transform) + '\n'
        printStream.println(regex.replace(line, ::transform))
    }
    //println(s)
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
    var max = 0
    val printStream = PrintStream(File(outputName))
    File(inputName).forEachLine { line ->
        if (line.length > max) max = line.trim().length
    }
    File(inputName).forEachLine { line ->//repeat
        val len = line.trim().length
        printStream.println(" ".repeat((max - len) / 2) + line.trim())
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
    val printStream = PrintStream(File(outputName))
    File(inputName).forEachLine { line ->
        val len = regex.replace(line, " ").trim().length
        //println(regex.replace(line, " ").trim())
        if (len > max) max = len
    }
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
    val printStream = PrintStream(File(outputName))
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
    File(inputName).forEachLine { line ->
        for (char in line) stringBuilder.append(if (char in replacement) replacement[char] else char)
        printStream.println(stringBuilder.toString())
        stringBuilder.clear()
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
    val printStream = PrintStream(File(outputName))
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
    printStream.println(mutableList.joinToString())
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
    var line = string
    var open = true
    while (line.contains("~~")) {
        line = line.replaceFirst("~~", if (open) "<s>" else "</s>")
        open = !open
    }
    val nearestRegex = Regex("""\*{1,3}""")
    val stack = Stack<String>()
    var matchResult = nearestRegex.find(line)
    while (matchResult != null) {
        val near = matchResult.value
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
        line = line.replaceFirst(near, replacement)
        matchResult = nearestRegex.find(line)
    }
    return line
}

fun main() {
    println(
        toHtml(
            "***Lorem ipsum* ***dolor sit amet*, consectetur **adipiscing** elit.\n" +
                    "Vestibulum lobortis. ~~Est vehicula rutrum *suscipit*~~, ipsum ~~lib~~ero *placerat **tortor***.\n" +
                    "\n\t\n" +
                    "Suspendisse ~~et elit in enim tempus iaculis~~."
        )
    )
}

fun markdownToHtmlSimple(inputName: String, outputName: String) {
    val printStream = PrintStream(File(outputName))
    val regexParagraph = Regex("""\s*""")
    val stringBuilder = StringBuilder()
    stringBuilder.append("<html><body><p>")
    File(inputName).forEachLine { line ->
        if (regexParagraph.matches(line)) stringBuilder.append("</p><p>") else stringBuilder.append(
            regexParagraph.replace(
                line,
                ""
            )
        )
    }
    stringBuilder.append("</p></body></html>")
    printStream.println(toHtml(stringBuilder.toString().replace("<p></p>", "")))
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
    TODO()
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
    TODO()
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
    TODO()
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
    TODO()
}

