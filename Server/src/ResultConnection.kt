package ru.dargr

import java.io.IOException
import java.io.InputStream
import java.net.Socket


class ResultConnection(resultSocket: Socket, gui: Gui): Runnable {
    /*
* Этот класс используется для приемы результата вычислений от клиента
* одна вычислительная задача - один поток, ждущий ответ
* */
    private var socket: Socket? = null
    private var gui: Gui? = null

    fun ResultConnection(socket: Socket?, gui: Gui?) {
        this.socket = socket
        this.gui = gui
    }

    override fun run() {
        try {
            val inputStream: InputStream = socket!!.getInputStream()

            while (!Character.isAlphabetic(inputStream.read())) {
                Thread.sleep(100)
            }

            var c: Char = inputStream.read().toChar()

            val resultBuilder = StringBuilder()
            while (c != '$') {
                resultBuilder.append(c)
                c = inputStream.read().toChar()
            }
            gui!!.returnResult(socket!!, resultBuilder.toString())
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}