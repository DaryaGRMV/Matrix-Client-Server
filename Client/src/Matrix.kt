package ru.dargr

import java.io.InputStream
import java.util.*


class Matrix(
    private val tableName: String,
    val recordCount: Int, //сроки
    val columnCount: Int) //столбцы
{
    private val body: Array<IntArray> = Array(recordCount) { IntArray(columnCount) } //массив матрицы

    @Throws(IllegalArgumentException::class)
    fun multiply(other: Matrix): Matrix {
        val aRows = recordCount //строки
        val aColumns = columnCount //столбцы
        val bRows = other.recordCount
        val bColumns = other.columnCount
        require(aColumns == bRows) { "ERROR! " + tableName + ": столбцы: " + aColumns + " е совпадение с " + other.tableName + ":строк " + bRows + ".\n" }
        /*
        * алгоритм перемножения матриц, сворован с интернетов
        * */
        val result = Matrix(tableName + "*" + other.tableName, aRows, bColumns)
        for (i in 0 until aRows) {
            for (j in 0 until bColumns) {
                for (k in 0 until aColumns) {
                    result.body[i][j] += body[i][k] * other.body[k][j]
                }
            }
        }
        return result
    }

    override fun toString(): String { //преобразование матрицы к строке
        val maxResolution = 20
        val builder = StringBuilder(" $tableName $columnCount $recordCount\n")
        var j = 0
        for (record in body) { //по строкам
            var i = 0
            while (i < columnCount && i < maxResolution) {
                builder.append(record[i])
                builder.append(" ")
                i++
            }
            builder.append("\n")
            if (j >= maxResolution) break
            j++
        }
        return builder.toString()
    }

    companion object {
        @Throws(InterruptedException::class)
        fun readMatrix(inputStream: InputStream?): Matrix { /*
        * ждем пока начнут приходить данные,
        * затем первыми считываем название и размерность матрицы
        * */
            val sc = Scanner(inputStream)
            while (!sc.hasNext()) {
                Thread.sleep(100)
            }
            val name: String = sc.next()
            val columnCount: Int = sc.nextInt()
            val recordCount: Int = sc.nextInt()
            val result = Matrix(name, recordCount, columnCount)
            /*
        * считываем по три значение - число и его позицию (строка, столбец).
        * сервер присылает данные в отсортированном по номеру строки виде
        * символ $ означает конец таблицы
        * */
            var stringValue: String = sc.next()
            var columnIndex: Int = sc.nextInt()  //индекс столбца
            var recordIndex: Int = sc.nextInt() //индекс записи
            while (stringValue != "$") {
                val record = IntArray(columnCount) //массив равный количеству столбцов
                val currentRecordIndex = recordIndex //текущий индекс
                while (recordIndex == currentRecordIndex) {
                    record[columnIndex - 1] = stringValue.toInt() //записываем матрицу из строк
                    stringValue = sc.next()
                    if (stringValue == "$") { //после каждой строки переходим на новую и так до конца
                        recordIndex++
                        break
                    }
                    columnIndex = sc.nextInt()
                    recordIndex = sc.nextInt()
                }
                result.body[recordIndex - 2] = record
            }
            return result
            /* записывает каждое значение строковой матрицы
            * в матрицу body*/

        }
    }
}