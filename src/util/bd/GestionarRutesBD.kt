package util.bd

import exercicis.Coordenades
import exercicis.PuntGeo
import exercicis.Ruta
import java.sql.DriverManager


class GestionarRutesBD {
    val conexion = DriverManager.getConnection("jdbc:sqlite:Rutes.sqlite")

    init {
        val conexion = DriverManager.getConnection("jdbc:sqlite:Empleats.sqlite")
    }

    /**
     * tancarà la connexió.
     */
    fun close(){
        conexion.close()
    }

    /**
     * Inserirà en la BD les dades corresponents a la ruta passada per paràmetre
     * (inicialment s'aconsella únicament "imprimir" les sentències, per veure si són correctes).
     */
    fun inserir(ruta: Ruta){
        /*
        El num_r ha de ser el posterior a l'última existent,
        per exemple amb la consulta SELECT MAX(num_r) FROM RUTES
         */

        //Los statements son conexiones, se han de cerrar
        val statement0 = conexion.createStatement()
        val consulta = "SELECT MAX(num_r) FROM Rutes"
        val rsNumRuta = statement0.executeQuery(consulta)

        //Inserir
        val statement = conexion.createStatement()
        val inserir = "INSERT INTO Rutes VALUES (${rsNumRuta.getInt(1)+1},'${ruta.nom}',${ruta.desnivell},${ruta.desnivellAcumulat})"

        statement.executeUpdate(inserir)
        statement.close()
    }

    /**
     * torna la ruta amb el número passat com a paràmetre.
     */
    fun buscar(i: Int): Ruta{
        val statement0 = conexion.createStatement()
        val consultaRuta = "SELECT * FROM Rutes WHERE num_r = $i"
        val rsRuta = statement0.executeQuery(consultaRuta)
        //statement0.close()

        val statement1 = conexion.createStatement()
        val consultaPunts = "SELECT * FROM Punts WHERE num_r = $i"
        val rsPuntos = statement1.executeQuery(consultaPunts)
       // statement1.close()

        val llistaDePunts = ArrayList<PuntGeo>()
        /*
        Bucle para recorrer el conjunto de resultados de la consulta,
        los bucles como for no funcionan, debemos usar .next(), que
        cambiar el puntero de la consulta a una siguiente posicion.
        Cada posicion es un punto de la ruta seleccionada, estos puntos
        se deben añadir a llistaPunts: ArrayList<PuntGeo>()
         */
        while (rsPuntos.next()){
            val nom = rsPuntos.getString(3)
            val latitud = rsPuntos.getDouble(4)
            val longitud = rsPuntos.getDouble(5)
            val coordenades = Coordenades(latitud, longitud)

            val punto = PuntGeo(nom,coordenades)
            llistaDePunts.add(punto)
        }

        val nom = rsRuta.getString(2)
        val desnivell = rsRuta.getInt(3)
        val desnivellAcumulat = rsRuta.getInt(4)

        /*
        Los statements son conexiones y se deben cerrar, pero se
        deben cerrar despues de recoger los datos. Si no Excepcion ResultSet closed
         */
        statement0.close()
        statement1.close()

        return Ruta(nom, desnivell, desnivellAcumulat, llistaDePunts)
    }

    /**
     * torna un ArrayList de Ruta amb totes les rutes de la Base de Dades.
     */
    fun llistat(): ArrayList<Ruta>{
        val statement0 = conexion.createStatement()
        val consultaRuta = "SELECT * FROM Rutes"
        val rsRuta = statement0.executeQuery(consultaRuta)
        statement0.close()

        val llistaDeRutes = ArrayList<Ruta>()
        val llistaDePunts = ArrayList<PuntGeo>()

        while (rsRuta.next()){
            val nom = rsRuta.getString(2)
            val desnivell = rsRuta.getInt(3)
            val desnivellAcumulat = rsRuta.getInt(4)

            val statement1 = conexion.prepareStatement("SELECT * FROM Punts WHERE num_r = ?")
            statement1.setInt(1,rsRuta.getInt(1))
            val rsPuntos = statement1.executeQuery()

            while (rsPuntos.next()){
                val nomPunt = rsPuntos.getString(3)
                val latitud = rsPuntos.getDouble(4)
                val longitud = rsPuntos.getDouble(5)
                val coordenades = Coordenades(latitud, longitud)

                val punto = PuntGeo(nomPunt,coordenades)
                llistaDePunts.add(punto)
            }
            llistaDeRutes.add(Ruta(nom, desnivell, desnivellAcumulat, llistaDePunts))
            statement1.close()
        }

        return llistaDeRutes
    }

    fun esborrar(i: Int){

    }


}