package util.bd

import exercicis.Coordenades
import exercicis.PuntGeo
import exercicis.Ruta
import java.sql.DriverManager


class GestionarRutesBD {
    var conexion = DriverManager.getConnection("jdbc:sqlite:Rutes.sqlite")
    val statement0 = conexion.createStatement()

    init {
        val sentenciaRutas = "CREATE TABLE IF NOT EXISTS Rutes(" +
                "num_r INTEGER PRIMARY KEY," +
                "nom_r TEXT, " +
                "desn INTEGER , " +
                "desn_ac INTEGER " +
                ")"
        val sentenciaPunts = "CREATE TABLE IF NOT EXISTS Punts("+
                "num_r INTEGER,"+
                "num_p INTEGER,"+
                "nom_p TEXT,"+
                "latitud REAL,"+
                "longitud REAL,"+
                "PRIMARY KEY (num_r,num_p),"+
                "FOREIGN KEY (num_r) REFERENCES Rutes(num_r))"

        statement0.executeUpdate(sentenciaRutas)
        statement0.executeUpdate(sentenciaPunts)
        statement0.close()
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

        rsNumRuta.close()
        statement0.close()
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
        deben cerrar despues de recoger los datos. Si no, Excepcion ResultSet closed
         */
        rsRuta.close()
        rsPuntos.close()

        statement0.close()
        statement1.close()

        return Ruta(nom, desnivell, desnivellAcumulat, llistaDePunts)
    }

    /**
     * Torna un ArrayList de Ruta amb totes les rutes de la Base de Dades.
     */
    fun llistat(): ArrayList<Ruta>{
        val statement0 = conexion.createStatement()
        val consultaRuta = "SELECT * FROM Rutes"
        val rsRuta = statement0.executeQuery(consultaRuta)

        val llistaDeRutes = ArrayList<Ruta>()
        val llistaDePunts = ArrayList<PuntGeo>()

        while (rsRuta.next()){
            val nom = rsRuta.getString(2)
            val desnivell = rsRuta.getInt(3)
            val desnivellAcumulat = rsRuta.getInt(4)

            /*
            Declaramos un prepareStatement con una incognita, esta incognita se
            resuelve con un setter que recoge el numero de la ruta sobre la que
            queremos trabajar.
             */
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

            rsPuntos.close()
            statement1.close()
        }
        rsRuta.close()
        statement0.close()

        return llistaDeRutes
    }

    /**
     * Esborra la ruta amb el número passat com a paràmetre
     * (recordeu que els punts de la ruta també s'han d'esborrar)
     */
    fun esborrar(i: Int){
        val statement0 = conexion.createStatement()
        val statement1 = conexion.createStatement()
        statement0.executeUpdate("DELETE FROM Rutes WHERE num_r = $i")
        statement0.close()

        statement1.executeUpdate("DELETE FROM Punts WHERE num_r = $i")
        statement1.close()
    }

    fun guardar(ruta: Ruta){

        val statement0 = conexion.createStatement()
        val consulta0 = "SELECT * FROM Rutes"
        val rsRutes= statement0.executeQuery(consulta0)

        //Si el nombre de la ruta introducida existe
        if (rsRutes.getString(2).equals(ruta.nom)){

            val statement1 = conexion.createStatement()
            val consulta1 = "UPDATE Rutes " +
                    "SET desn = ${ruta.desnivell}, desn_ac = ${ruta.desnivellAcumulat} " +
                    "WHERE nom_r = ${ruta.nom}"
            statement1.executeUpdate(consulta1)
            statement1.close()

            /*
            Eliminamos los puntos cuya ruta coincida con la introducida
             */
            val statement2 = conexion.prepareStatement("DELETE FROM Punts WHERE num_r = ?")
            statement2.setInt(1,rsRutes.getInt(1))
            statement2.close()

            /*
            Introducimos los puntos de la ruta
             */
            val statement3 = conexion.createStatement()
            for (punt in ruta.llistaDePunts) {
                val consulta2 = "INSERT INTO Punts " +
                        "VALUES (${rsRutes.getInt(1)},'${punt.nom}', ${punt.coord.latitud}, ${punt.coord.longitud})"
                statement3.executeUpdate(consulta2)
            }
            rsRutes.close()
            statement3.close()

        } else {

            val statement4 = conexion.createStatement()

        }
    }


}