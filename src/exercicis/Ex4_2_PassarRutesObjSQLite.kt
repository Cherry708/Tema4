package exercicis

import java.io.EOFException
import java.io.FileInputStream
import java.io.ObjectInputStream
import java.sql.Connection
import java.sql.Driver
import java.sql.DriverManager

fun main(){
    /*
    En la string de la URL se debe definir el driver a usar
     */
    val url = "jdbc:sqlite:Rutes.sqlite"
    val conexion = DriverManager.getConnection(url)
    System.out.println("Connexió completada")

    val sentencia = conexion.createStatement()

    val fileIn = ObjectInputStream(FileInputStream("Rutes.obj"))
    try {
        var numRuta = 1
        while (true){
            val ruta = fileIn.readObject() as Ruta
            val nomRuta = ruta.nom
            val desnivellRuta = ruta.desnivell
            val desnivellAcumulat = ruta.desnivellAcumulat
            sentencia.executeUpdate("INSERT INTO Rutes VALUES($numRuta,'$nomRuta',$desnivellRuta,$desnivellAcumulat)")

            var numPunt = 1
            for (punt in ruta.llistaDePunts){
                val nomPunt = punt.nom
                val latitud = punt.coord.latitud
                val longitud = punt.coord.longitud
                sentencia.executeUpdate("INSERT INTO Punts VALUES($numRuta,$numPunt,'$nomPunt',$latitud,$longitud)")
                numPunt++
            }
            numRuta++
        }
    } catch (e:EOFException){

    }

    finalizarConexion(conexion)
}
fun finalizarConexion(conexion:Connection){
    conexion.close()
    println("Conexion finalizada")
}