package exemples

import java.sql.DriverManager

fun main(args: Array<String>) {
    val url = "jdbc:sqlite:Empleats.sqlite"
    val connection = DriverManager.getConnection(url)
    val statement = connection.createStatement()

    statement.executeUpdate("INSERT INTO EMPLEAT VALUES (1,'Andreu',10,32,1000.0)")

    statement.executeUpdate("INSERT INTO EMPLEAT VALUES (2,'Bernat',20,28,1200.0)")

    statement.executeUpdate("INSERT INTO EMPLEAT VALUES (3,'Clàudia',10,26,1100.0)")

    statement.executeUpdate("INSERT INTO EMPLEAT VALUES (4,'Damià',10,40,1500.0)")

    statement.close()
    connection.close()
}

