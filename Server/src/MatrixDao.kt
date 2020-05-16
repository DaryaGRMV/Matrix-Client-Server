package ru.dargr

import java.io.OutputStream
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement

//Класс, отвечающий за доступ к данным из базы данных
class MatrixDao {
    private val instance = MatrixDao()

    fun getInstance(): MatrixDao? {
        return instance
    }

    //@Throws(IOException::class)
    fun writeMatrix(tableName: String, outputStream: OutputStream) {
        try {
            val c: Connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/matrix?serverTimezone=UTC",
                "dargr",
                "14011"
            )
            val selectAllStatement: Statement = c.createStatement()
            val s: Statement = c.createStatement()
            var countRes =
                s.executeQuery("SELECT MAX(column_index) FROM $tableName") //максимальный индекс столбца
            countRes.next()
            val columnCount = countRes.getInt(1) //количество столбцов
            countRes = s.executeQuery("SELECT MAX(record_index) FROM $tableName") //максимальный индекс строки
            countRes.next()
            val recordCount = countRes.getInt(1) //количество строк
            s.close()
            outputStream.write(" $tableName $columnCount $recordCount\n".toByteArray())
            val resultSet =
                selectAllStatement.executeQuery("SELECT * FROM $tableName ORDER BY record_index")
            while (resultSet.next()) {
                outputStream.write(
                    (resultSet.getInt("value").toString() + " " +
                            resultSet.getInt("column_index") + " " +
                            resultSet.getInt("record_index") + "\n")
                        .toByteArray()
                )
            }
            countRes.close()
            outputStream.write(" $ ".toByteArray())
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun isExistsTable(table1: String?): Boolean {
        try {
            val c: Connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/matrix?serverTimezone=UTC",
                "dargr",
                "14011"
            )
            val metaData = c.metaData
            if (metaData.getTables(null, null, table1, null).next()) {
                return true
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return false
    }

}
