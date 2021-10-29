package exemples

import java.sql.DriverManager
import java.io.BufferedReader
import java.io.InputStreamReader

fun main(args: Array<String>) {
    val con = DriverManager.getConnection("jdbc:postgresql://89.36.214.106:5432/geo_ad", "geo_ad", "geo_ad")

    val dbmd = con.getMetaData()
    println("Llistat de taules:")
    println(String.format("%-6s %-7s %-7s %-10s %-10s", "Número", "Catàleg", "Esquema", "Nom", "Tipus"))
    println("---------------------------------------------")
    val ll = dbmd.getTables(null, "public", null, null)
    var compt = 1
    val taules = ArrayList<String>()
    while (ll.next()) {
        println(String.format("%-6d %-7s %-7s %-10s %-10s",(compt++),ll.getString(1),ll.getString(2),
            ll.getString(3),ll.getString(4)))
        taules.add(ll.getString(3))
    }
    println()
    println("Introdueix un número per veure el contingut de la taula (0 per acabar): ")
    val ent = BufferedReader(InputStreamReader(System.`in`))
    var opcio = Integer.parseInt(ent.readLine())

    while (opcio != 0) {
        if (opcio < compt && opcio > 0) {
            val taula = dbmd.getTables(null, "public", taules.get(opcio - 1), null)
            if (taula.next()) {
                if (taula.getString(4).equals("TABLE")) {
                    val rs = con.createStatement().executeQuery("SELECT * FROM " + taules.get(opcio - 1) + " ORDER BY 1")
                    println("Contingut de la taula " + taules.get(opcio - 1))
                    println("----------------------------")

                    val rsmd = rs.getMetaData()
                    for (i in 1..rsmd.getColumnCount())
                        print(String.format("%-20.20s", rsmd.getColumnName(i)))
                    println()
                    println("------------------------------------------")

                    while (rs.next()) {
                        for (i in 1..rsmd.getColumnCount())
                            print(String.format("%-20.20s ", rs.getString(i)))
                        println()
                    }
                    rs.close()
                }
            }
            taula.close()
        }
        println()
        println("Introdueix un número per veure el contingut de la taula (0 per acabar): ")
        opcio = Integer.parseInt(ent.readLine())
    }
    ll.close()
    con.close()
}

