package ru.dargr

import java.io.InputStream
import java.util.*

class Matrix(private val tableName: String,
             val recordCount: Int,
             val columnCount: Int) {
    private val body: Array<IntArray> = Array(recordCount) { IntArray(columnCount) }

   // @Throws(IllegalArgumentException::class)
    fun multiply(other: Matrix): Matrix {
        val aRows = recordCount //строки
        val aColumns = columnCount //столбцы
        val bRows = other.recordCount
        val bColumns = other.columnCount

       if (aColumns != bRows) { //если матрицы нельзя перемножить
           throw IllegalArgumentException("ERROR! Матрицы не могут быть перемножены");
       }
        //умножение матриц
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

    override fun toString(): String {
        val maxResolution = 40 //максимальное разрешение
        val builder = StringBuilder(" $tableName $columnCount $recordCount\n")
        var j = 0
        for (record in body) {
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
        return builder.toString() //имя, кол-во столбцов, кол-во строк
                                  //матрица 20х20 к строке
    }

    companion object { //привязывает функцию или свойство к классу, а не к его объектам
        @Throws(InterruptedException::class)
        fun readMatrix(inputStream: InputStream): Matrix { /*
        * ждем пока начнут приходить данные,
        * затем считываем название и размерность матрицы
        * */
            val scanner = Scanner(inputStream)
            while (!scanner.hasNext()) {
                Thread.sleep(100)
            }
            val name = scanner.next()
            val columnCount = scanner.nextInt()
            val recordCount = scanner.nextInt()
            val result = Matrix(name, recordCount, columnCount)
            /*
        * считываем по три значение - число и его позицию (строка, столбец)
        * символ $ означает конец таблицы
        * */
            var stringValue = scanner.next()
            var columnIndex = scanner.nextInt()
            var recordIndex = scanner.nextInt()
            while (stringValue != "$") {
                val record = IntArray(columnCount)
                val currentRecordIndex = recordIndex
                while (recordIndex == currentRecordIndex) {
                    record[columnIndex - 1] = stringValue.toInt()
                    stringValue = scanner.next()
                    if (stringValue == "$") {
                        recordIndex++
                        break
                    }
                    columnIndex = scanner.nextInt()
                    recordIndex = scanner.nextInt()
                }
                result.body[recordIndex - 2] = record
            }
            return result //получаем матрицу
        }
    }

}