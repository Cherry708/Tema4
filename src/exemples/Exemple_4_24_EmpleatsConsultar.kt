package exemples

import java.sql.DriverManager

fun main(args: Array<String>) {
    val url = "jdbc:sqlite:Empleats.sqlite"
    val connection = DriverManager.getConnection(url)
    val statement = connection.createStatement()

    val sentenciaSQL = "SELECT * FROM EMPLEAT WHERE sou > 1100"
    val resultSet = statement.executeQuery(sentenciaSQL)

    System.out.println("Núm. \tNom \tDep \tEdat \tSou")
    System.out.println("-----------------------------------------")

    while (resultSet.next()) {
        print("" + resultSet.getInt(1) + "\t")
        print(resultSet.getString(2) + "\t")
        print("" + resultSet.getInt(3) + "\t")
        print("" + resultSet.getInt(4) + "\t")
        println(resultSet.getDouble(5))
    }

    resultSet.close()
    statement.close()
    connection.close()
}