import java.awt.Color
import java.awt.Dimension
import java.net.Socket
import javax.swing.*


class Gui(private val server: Server): JFrame() {
    //текстовое поле, куда выводится результат
    private var mainTextArea: JTextArea = JTextArea()
    init {
        val scrollPane = JScrollPane(mainTextArea)
        val frame = JFrame("Matrix")
        mainTextArea.isEnabled = false
        mainTextArea.disabledTextColor= Color.BLACK
        frame.contentPane.add(scrollPane)
        frame.defaultCloseOperation = EXIT_ON_CLOSE
        frame.size = Dimension(600, 400)
        frame.isVisible = true
    }
    /*
    * synchronized означает, что если два или более потока не могут использовать этот метод одновременно
    * вместо этого они вызовут этот метод по очереди.*/
    @Synchronized
    fun returnResult(socket: Socket?, result: String?) {
        /* получили от подключения результат вычисления - вывели его в поле,
        * а само подключение вернули серверу как "освободившееся"*/
        mainTextArea.append(result)
        server.returnSocket(socket!!)
    }

    fun append(s: String?) {
        mainTextArea.append(s)
    }
}
