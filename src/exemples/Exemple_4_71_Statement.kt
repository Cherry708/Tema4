package exemples

import java.sql.DriverManager
import java.util.Scanner

fun main(args: Array<String>) {
    val con = DriverManager.getConnection("jdbc:postgresql://89.36.214.106:5432/geo_ad", "geo_ad", "geo_ad")

    println("Introdueix una comarca:")
    val com = Scanner(System.`in`).nextLine()
    println("Introdueix una altura:")
    val alt = Scanner(System.`in`).nextInt()

    val st = con.createStatement()    // La sentència no va en el moment de la creació sinó en el d'execució
    //Reemplazamos la apostrofe para evitar errores en las consultas
    val rs = st.executeQuery("SELECT nom,altura FROM POBLACIO WHERE nom_c='" + com.replace("'", "''") + "' AND altura>" + alt)
    while (rs.next()) {
        println(rs.getString(1) + " (" +rs.getInt(2) + " m.)")
    }
    st.close()
    con.close()
}