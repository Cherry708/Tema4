package exemples

import java.sql.DriverManager

fun main(args: Array<String>) {
    val url = "jdbc:mysql://89.36.214.106:3306/factura"
    val usuari = "factura"
    val password = "factura"

    val connection = DriverManager.getConnection(url, usuari, password)

    val statement = connection.createStatement()
    val resultSet = statement.executeQuery("SELECT * FROM poble")
    while (resultSet.next()) {
        print("" + resultSet.getInt(1) + "\t")
        println(resultSet.getString(2))
    }
    statement.close()
    connection.close()
}

