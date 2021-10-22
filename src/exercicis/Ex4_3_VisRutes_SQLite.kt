package exercicis

import javax.swing.JFrame
import java.awt.EventQueue
import java.awt.BorderLayout
import javax.swing.JPanel
import java.awt.FlowLayout
import java.sql.DriverManager
import javax.swing.JComboBox
import javax.swing.JButton
import javax.swing.JTextArea
import javax.swing.JLabel

class Finestra : JFrame() {

    init {
        // Sentències per a fer la connexió
        val url = "jdbc:sqlite:Rutes.sqlite"
        val conexion = DriverManager.getConnection(url)
        val sentencia = conexion.createStatement()


        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        setTitle("JDBC: Visualitzar Rutes")
        setSize(450, 450)
        setLayout(BorderLayout())

        val panell1 = JPanel(FlowLayout())
        val panell2 = JPanel(BorderLayout())
        add(panell1, BorderLayout.NORTH)
        add(panell2, BorderLayout.CENTER)

        val llistaRutes = arrayListOf<String>()
        // Sentències per a omplir l'ArrayList amb el nom de les rutes
        val consulta = "SELECT nom_r FROM Rutes"
        val resultadoNombres = sentencia.executeQuery(consulta)
        while (resultadoNombres.next()){
            llistaRutes.add(resultadoNombres.getString(1))
        }

        val combo = JComboBox<String>(llistaRutes.toTypedArray())
        panell1.add(combo)
        val eixir = JButton("Eixir")
        panell1.add(eixir)
        val area = JTextArea()
        panell2.add(JLabel("Llista de punts de la ruta:"),BorderLayout.NORTH)
        panell2.add(area,BorderLayout.CENTER)


        combo.addActionListener() {
            // Sentèncis quan s'ha seleccionat un element del JComboBox
            // Han de consistir en omplir el JTextArea
            for (i in 0 until llistaRutes.size)     {
                if (combo.selectedIndex == i){
                    area.text = ""
                    val sentenciaPuntos = conexion.createStatement()
                    val consultaPuntos = sentenciaPuntos.executeQuery("SELECT 'nom_p',latitud,longitud FROM Punts")
                    val resultadoConsulta = consultaPuntos
                    //hay que saltar una fila
                    area.text = resultadoConsulta.getString(1)
                }
            }

        }

        eixir.addActionListener(){
            // Sentències per a tancar la connexió i eixir
            conexion.close()
            println("Conexion finalizada.")
        }
    }
}

fun main(args: Array<String>) {
    EventQueue.invokeLater {
        Finestra().isVisible = true
    }
}

