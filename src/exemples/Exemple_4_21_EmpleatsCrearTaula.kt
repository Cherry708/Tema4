package exemples

import java.sql.DriverManager

fun main(args: Array<String>) {
    val url = "jdbc:sqlite:Empleats.sqlite"
    val connection = DriverManager.getConnection(url)
    val statement = connection.createStatement()

    val sentenciaSql = "CREATE TABLE EMPLEAT(" +
            "num INTEGER CONSTRAINT cp_emp PRIMARY KEY, " +
            "nom TEXT, " +
            "depart INTEGER, " +
            "edat INTEGER, " +
            "sou REAL " +
            ")"

    statement.executeUpdate(sentenciaSql)
    statement.close()
    connection.close()
}

