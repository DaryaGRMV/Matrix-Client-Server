package ru.dargr

import java.io.IOException
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket
import java.util.*


class Server(port: Int, maxConnections: Int, address: String?) : Runnable{

    private var freeSockets: Queue<Socket>? = null
    private var serverSocket: ServerSocket? = null
    private val dao: MatrixDao? = MatrixDao().getInstance()
    private var running = false

    init {
        this.serverSocket = ServerSocket(port, maxConnections, InetAddress.getByName(address))
        this.freeSockets = LinkedList()
        this.running = true
    }

    /*fun Server(port: Int, maxConnections: Int, address: String?) {
        try {
            this.serverSocket = ServerSocket(port, maxConnections, InetAddress.getByName(address))
            this.freeSockets = LinkedList()
            this.running = true
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }*/

    override fun run() {
        while (running) {
            try {
                freeSockets!!.add(serverSocket!!.accept())
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    @Throws(InterruptedException::class, IllegalArgumentException::class, IOException::class)
    fun multiply(table1: String, table2: String): Socket? { /*
        * если таблицы с заданным именем нет - выбрасывается исключение
        * */
        require(!(!MatrixDao().isExistsTable(table1) || !MatrixDao().isExistsTable(table2))) { "table not found" }
        // require - аналог if с исключениями
        /*
        * Ждем, пока появится свободный клиент
        * */while (freeSockets!!.isEmpty()) {
            Thread.sleep(100)
        }
        val socket = freeSockets!!.remove()
        /*
        * забираем сокет свободного клиента, отправляем ему запрос в виде двух таблиц
        * и возвращаем этот сокет - по нему клиент передаст результат вычислений, когда посчитает его.
        * между отправкой таблиц ждем от клиента сигнала, что первая таблица сохранена - знак $
        * */
        val outputStream = socket.getOutputStream()
        outputStream.flush()
        if (dao != null) {
            dao.writeMatrix(table1, outputStream)
        }
        val inputStream = socket.getInputStream()
        while (inputStream.read() != '$'.toInt()) {
            Thread.sleep(10)
        }
        if (dao != null) {
            dao.writeMatrix(table2, outputStream)
        }
        return socket
    }

    fun stop() {
        running = false
    }

    fun returnSocket(socket: Socket) {
        freeSockets!!.add(socket)
    }
}