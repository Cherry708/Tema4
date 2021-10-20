package exemples

import java.sql.DriverManager

fun main(args: Array<String>) {
    val url = "jdbc:sqlite:proveta.sqlite"

    val con = DriverManager.getConnection(url)
    System.out.println("Connexió completada")
    con.close()
}

