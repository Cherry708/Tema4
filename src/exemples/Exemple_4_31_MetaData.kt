package exemples

import java.sql.DriverManager
import java.io.BufferedReader
import java.io.InputStreamReader

fun main(args: Array<String>) {
    val con = DriverManager.getConnection ("jdbc:postgresql://89.36.214.106:5432/geo_ad", "geo_ad", "geo_ad")

    val dbmd = con.getMetaData ()
    println("Informació general")
    println("------------------")
    println("SGBD " + dbmd.getDatabaseProductName())
    println("Driver " + dbmd.getDriverName())
    println("URL " + dbmd.getURL())
    println("Usuari " + dbmd.getUserName())
    println()
    println("Llistat de taules")
    println(String.format("%-6s %-7s %-7s %-10s %-10s", "Número", "Catàleg", "Esquema", "Nom", "Tipus"))
    println("---------------------------------------------")
    val ll = dbmd.getTables(null, "public", null, null)
    var compt = 1
    val taules = ArrayList<String>()
    while (ll.next()) {
        println(String.format("%-6d %-7s %-7s %-10s %-10s",(compt++),ll.getString(1),ll.getString(2),ll.getString(3),ll.getString(4)))
        taules.add(ll.getString(3))
    }
    println()
    println("Introdueix un número per veure l'estructura de la taula (0 per acabar) ")
    val ent = BufferedReader(InputStreamReader (System.`in`))
    var opcio = Integer.parseInt(ent.readLine())

    while (opcio != 0) {
        if (opcio < compt && opcio > 0) {
            val taula = dbmd.getTables (null, "public", taules.get(opcio-1), null)
            if (taula.next()) {
                if (taula.getString(4).equals("TABLE")) {
                    val rs = dbmd.getColumns (null, "public", taules.get(opcio-1), null)
                    println("Estructura de la taula " + taules.get(opcio - 1))
                    println("----------------------------")
                    while (rs.next())
                        println(rs.getString(4) + " (" + rs.getString(6) + ")")
                    println("----------------------------")

                    val rs2 = dbmd.getPrimaryKeys(null, "public", taules.get(opcio - 1))
                    print("Clau principal ")
                    while (rs2.next())
                        print(rs2.getString(4) + " ")
                    println()

                    val rs3 = dbmd.getImportedKeys(null, "public", taules.get(opcio - 1))
                    println("Claus externes ")
                    while (rs3.next()) {
                        println(rs3.getString(8) + " apunta a " + rs3.getString(3))
                    }
                    rs.close()
                    rs2.close()
                    rs3.close()
                }
            }
            taula.close()

        }
        println()
        println("Introdueix un número per veure l'estructura de la taula (0 per acabar) ")
        opcio = Integer.parseInt(ent.readLine())
    }
    ll.close()
    con.close()
}