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


        // Sentències per a omplir l'ArrayList amb el nom de les rutes
        // Añadimos nombres de rutas al combo
        val llistaRutes = arrayListOf<String>()
        val consultaNomRutes = "SELECT nom_r FROM Rutes"
        val rsNomRutes = sentencia.executeQuery(consultaNomRutes)
        while (rsNomRutes.next()) {
            llistaRutes.add(rsNomRutes.getString(1))
        }
        rsNomRutes.close()
        val combo = JComboBox<String>(llistaRutes.toTypedArray())

        panell1.add(combo)
        val eixir = JButton("Eixir")
        panell1.add(eixir)
        val area = JTextArea()
        panell2.add(JLabel("Llista de punts de la ruta:"), BorderLayout.NORTH)
        panell2.add(area, BorderLayout.CENTER)


        combo.addActionListener() {
            // Sentèncis quan s'ha seleccionat un element del JComboBox
            // Han de consistir en omplir el JTextArea

            /*
            Obtenemos el numero de la ruta seleccionada,
            la emplearemos como parametro para la consulta
             */
            val ruta = combo.selectedIndex+1

            area.text = ""

            val sentenciaPuntos = conexion.createStatement()
            println("SELECT nom_p,latitud,longitud FROM Punts WHERE num_ruta = $ruta ")

            //Seleccionamos * de la tabla puntos cuya ruta sea combo.selecteIndex+1 (ruta)
            val rsConsultaPuntos = sentenciaPuntos.executeQuery("SELECT * FROM Punts WHERE num_r = $ruta ")

            //la primera columna es 1
            while (rsConsultaPuntos.next()) {
                area.append(rsConsultaPuntos.getString(3))
                area.append(" (${rsConsultaPuntos.getDouble(4)}, ${rsConsultaPuntos.getDouble(5)})\n")
            }
            rsConsultaPuntos.close()
        }

        eixir.addActionListener() {
            // Sentències per a tancar la connexió i eixir
            conexion.close()
            println("Conexion finalizada.")
            System.exit(0)
        }
    }
}

fun main(args: Array<String>) {
    EventQueue.invokeLater {
        Finestra().isVisible = true
    }
}

