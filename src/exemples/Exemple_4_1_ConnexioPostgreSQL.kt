package exemples

import java.sql.DriverManager

fun main(args: Array<String>) {
    val url = "jdbc:postgresql://89.36.214.106:5432/geo_ad"
    val usuari = "geo_ad"
    val password = "geo_ad"

    val con = DriverManager.getConnection(url, usuari, password)
    System.out.println("Connexió completada")
    con.close()
}

