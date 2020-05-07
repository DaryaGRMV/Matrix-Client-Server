package ru.dargr

import java.io.InputStream
import java.io.OutputStream
import java.net.Socket

fun main() {
    val port: Int = 5555
    val host: String = "192.168.0.70"
    var running: Boolean = true
    println("адрес сервера: 192.168.0.70")
    /* val sc = Scanner(System.`in`)
    val address: String = sc.nextLine()
    val address: String = "192.168.0.70"
    host = address*/

    val socket: Socket = Socket(host, port)
    val inputStream: InputStream = socket.getInputStream()
    while (running) {
        val matrix1: Matrix = Matrix.readMatrix(inputStream)
        socket.getInputStream()
        socket.getOutputStream().write("$".toByteArray())
        val matrix2: Matrix = Matrix.readMatrix(inputStream)
        val outputStream: OutputStream = socket.getOutputStream()

        try {
            val result = matrix1.multiply(matrix2)
            outputStream.flush()
            outputStream.write("$result $ ".toByteArray())
            println("таблицы успешно перемножены, размерность результирующей таблицы " +
                    result.recordCount.toString() +
                    " x " + result.columnCount)
        } catch (ex: IllegalArgumentException) {
            outputStream.write((ex.message + " $ ").toByteArray())
            println(ex.message)
        }
    }
}



