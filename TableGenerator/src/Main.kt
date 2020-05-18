package ru.dargr

import java.sql.*
import java.util.*


fun main() {
    val columnCount: Int
    val recordCount: Int

    val random = Random()
    val sc = Scanner(System.`in`)

    println("Введите название таблицы")
    val name: String = sc.nextLine()

    println("Введите количество столбцов")
    columnCount = sc.nextInt()
    //columnCount = random.nextInt(20000) + 100

    println("Введите количество строк")
    recordCount = sc.nextInt()
   //recordCount =  random.nextInt(20000) + 100
    println("Стобцов: $columnCount")
    println("Строк: $recordCount")


    val dt1 = "drop table if exists `$name`"
    val ins1 = "INSERT INTO `$name` VALUES (?, ?, ?)"
    val ct1 = "CREATE TABLE `$name` " +
            "(value int," +
            "record_index int," +
            "column_index int)"

    val c: Connection = DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/matrix?serverTimezone=UTC",
        "dargr",
        "14011"
    )
    val ins: PreparedStatement = c.prepareStatement(ins1)
    val s: Statement = c.createStatement()
    s.execute(dt1)
    s.execute(ct1)
    c.autoCommit = false
    val total = columnCount*recordCount
    var progress: Long = 0

    for (recordIndex in 1..recordCount) {
        ins.clearBatch()
        ins.setInt(2, recordIndex)
        for (columnIndex in 1..columnCount) {
            ins.setInt(3, columnIndex)
            ins.setInt(1, random.nextInt(1000) - 500)
            ins.addBatch()
        }
        ins.executeBatch()
        c.commit()
        //вывод прогресса генерации
        val currentProgress =
            Math.round((recordIndex * columnCount).toDouble() / total.toDouble() * 100)
        if (currentProgress > progress) {
            progress = currentProgress
            print("$progress%")
        }
    }
    val result1: ResultSet = s.executeQuery("SELECT COUNT(*) FROM `$name`")
    result1.next()
    println("\nВставлено элементов: " + result1.getInt(1))



}


