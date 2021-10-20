package exemples

import java.sql.DriverManager

fun main(args: Array<String>) {
    val url = "jdbc:sqlite:proveta.sqlite"

    val	connection = DriverManager.getConnection(url)

    val statement = connection.createStatement ()
    statement.executeUpdate("CREATE TABLE T2 (c1 TEXT)")
    statement.close()

    connection.close()
}