package ru.dargr

import java.net.Socket
import java.util.*
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JTextArea


class Gui(private val server: Server) {
    //текстовое поле, куда выводится результат
    private val mainTextArea: JTextArea? = null
    private val scroll: JPanel? = null

    init {
        val frame = JFrame("Result area")
        frame.contentPane = scroll
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.pack()
        frame.isVisible = true
        frame.setSize(640, 720)
        frame.isAlwaysOnTop = true
    }
    /*
    * synchronized означает, что если два или более потока не могут использовать этот метод одновременно
    * вместо этого они вызовут этот метод по очереди.*/
    @Synchronized
    fun returnResult(socket: Socket?, result: String?) {
        /* получили от подключения результат вычисления - вывели его в поле,
        * а само подключение вернули серверу как "освободившееся"*/
        mainTextArea!!.append(result)
        server.returnSocket(socket!!)
    }
}
