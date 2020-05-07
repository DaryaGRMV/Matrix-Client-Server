package ru.dargr

import java.net.Socket
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JTextArea


class Gui(server: Server?){
    //текстовое поле, куда выводится результат
    private val mainTextArea: JTextArea? = null
    private val scroll: JPanel? = null
    private var server: Server? = null

    init{
        this.server = server
        val frame = JFrame("Result area")
        frame.contentPane = scroll
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.pack()
        frame.isVisible = true
        frame.setSize(640, 720)
        frame.isAlwaysOnTop = true

    }

    /*fun Gui(server: Server?) {
        this.server = server
        val frame = JFrame("Result area")
        frame.contentPane = scroll
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.pack()
        frame.isVisible = true
        frame.setSize(640, 720)
        frame.isAlwaysOnTop = true
    }*/

    /*
    * synchronized означает, что если два или более потока не могут использовать этот метод одновременно
    * вместо этого они вызовут этот метод по очереди.
    * это нужно на случай, если несколько клиентов завершат вычисление почти одновременно -
    * без synchronized в поле вывода несколько результатов бы перемешались между собой
    * */
    @Synchronized
    fun returnResult(socket: Socket, result: String?) { /*
        * получили от подключения результат вычисления - вывели его в поле,
        * а само подключение вернули серверу как "освободившееся",
        * то есть готовое взять на себя очередную вычислительную задачу
        * */
        mainTextArea!!.append(result)
        server?.returnSocket(socket)
    }

    fun append(s: String?) {
        mainTextArea!!.append(s)
    }
}