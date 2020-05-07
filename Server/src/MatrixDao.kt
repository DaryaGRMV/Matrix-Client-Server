package ru.dargr

import java.io.IOException
import java.io.OutputStream
import java.sql.*


class MatrixDao {
    private val instance = MatrixDao()

    fun getInstance(): MatrixDao? {
        return instance
    }

    fun isExistsTable(table1: String?): Boolean {
        try {
            DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/matrix?serverTimezone=UTC",
                "dargr",
                "14011"
            ).use { connection ->
                val metaData: DatabaseMetaData = connection.metaData
                if (metaData.getTables(null, null, table1, null).next()) {
                    return true
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return false
    }

    @Throws(IOException::class)
    fun writeMatrix(tableName: String, outputStream: OutputStream) { /*
        * этот try блок сам закроет все,
        * что открывается в его круглых скобках
        * (Connection, PreparedStatement, Statement)
        * */
        try {
            val c: Connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/matrix?serverTimezone=UTC",
                "dargr",
                "14011"
            )
            val selectAllStatement: Statement = c.createStatement()
            val s: Statement = c.createStatement()

            var countRes: ResultSet = s.executeQuery("SELECT MAX(column_index) FROM $tableName")
            countRes.next()
            val columnCount: Int = countRes.getInt(1)

            countRes = s.executeQuery("SELECT MAX(record_index) FROM $tableName")
            countRes.next()
            val recordCount = countRes.getInt(1)

            s.close()

            outputStream.write(
                (" " + tableName +
                        " " + columnCount + " " + recordCount + "\n").toByteArray()
            )

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

}