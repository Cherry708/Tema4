package exercicis

import java.sql.DriverManager

fun main(){
    val url = "jdbc:sqlite:Rutes.sqlite"
    val connection = DriverManager.getConnection(url)
    val statement = connection.createStatement()

    val sentenciaRutas = "CREATE TABLE Rutes(" +
            "num_r INTEGER PRIMARY KEY," +
            "nom_r TEXT, " +
            "desn INTEGER , " +
            "desn_ac INTEGER " +
            ")"

    val sentenciaPunts = "CREATE TABLE Punts("+
            "num_r INTEGER,"+
            "num_p INTEGER,"+
            "nom_p TEXT,"+
            "latitud REAL,"+
            "longitud REAL,"+
            "PRIMARY KEY (num_r,num_p),"+
            "FOREIGN KEY (num_r) REFERENCES Rutes(num_r))"

    statement.executeUpdate(sentenciaRutas)
    statement.executeUpdate(sentenciaPunts)
    statement.close()
    connection.close()
}