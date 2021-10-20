package exemples

import java.sql.DriverManager

fun main(args: Array<String>) {
    val url = "jdbc:postgresql://89.36.214.106:5432/geo_ad"
    val usuari = "geo_ad"
    val password = "geo_ad"

    val connection = DriverManager.getConnection(url, usuari, password)

    val statement = connection.createStatement()
    val resultSet = statement.executeQuery("SELECT * FROM institut")
    while (resultSet.next()) {
        print("" + resultSet.getInt(1) + "\t")
        println(resultSet.getString(2))
    }
    statement.close()
    connection.close()
}