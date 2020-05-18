package ru.dargr

import java.net.Socket
fun main() {
    val running = true
    // @Throws(IOException::class, InterruptedException::class)
    val socket = Socket("localhost", 5703)
    val inputStream = socket.getInputStream()
    while (running) {
        val matrix1 = Matrix.readMatrix(inputStream)//получаем первую матрицу
        socket.getOutputStream().write("$".toByteArray())//говорим серверу что готовы принять следующую матрицу
        val matrix2 = Matrix.readMatrix(inputStream)//получаем вторую матрицу
        val outputStream = socket.getOutputStream()
        try {
            val result = matrix1.multiply(matrix2)
            outputStream.flush()
            outputStream.write("$result $ ".toByteArray())
            println("таблицы успешно перемножены, размерность результирующей таблицы " + result.columnCount + " x " + result.recordCount)
        } catch (e: IllegalArgumentException) {
            outputStream.write((e.message + " $ ").toByteArray())
            println(e.message)
        }
    }
}


