package exemples

import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JTextField
import javax.swing.JButton
import javax.swing.JPanel
import java.awt.FlowLayout
import java.awt.GridLayout
import java.awt.BorderLayout
import java.awt.EventQueue
import java.sql.DriverManager
import java.sql.ResultSet

class Exemple_4_41_Scroll : JFrame() {

    val nomComarca = JTextField()
    val nomProvincia = JTextField()

    val primer = JButton("<<")
    val anterior = JButton("<")
    val seguent = JButton(">")
    val ultim = JButton(">>")
    val tancar = JButton("Tancar")


    val pDalt = JPanel(FlowLayout())
    val pCentre = JPanel(GridLayout(8, 0))
    val pDades = JPanel(GridLayout(2, 2))
    val pBotonsMov = JPanel(FlowLayout())
    val pTancar = JPanel(FlowLayout())

    val con = DriverManager.getConnection("jdbc:postgresql://89.36.214.106:5432/geo_ad", "geo_ad", "geo_ad")
    val st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)
    val rs = st.executeQuery("SELECT * FROM COMARCA ORDER BY 1")

    init {
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        setBounds(100, 100, 350, 400)
        setLayout(BorderLayout())

        getContentPane().add(pCentre, BorderLayout.CENTER)
        getContentPane().add(JPanel(FlowLayout()), BorderLayout.WEST)
        getContentPane().add(JPanel(FlowLayout()), BorderLayout.EAST)
        getContentPane().add(pTancar, BorderLayout.SOUTH)

        pDalt.add(JLabel("Manteniment de COMARQUES"))
        pCentre.add(pDalt)

        pDades.add(JLabel("Nom comarca"))
        pDades.add(nomComarca)
        pDades.add(JLabel("Nom província"))
        pDades.add(nomProvincia)
        pCentre.add(pDades)

        nomComarca.setEditable(false)
        nomProvincia.setEditable(false)

        pCentre.add(JPanel(FlowLayout()))

        pBotonsMov.add(primer)
        pBotonsMov.add(anterior)
        pBotonsMov.add(seguent)
        pBotonsMov.add(ultim)
        pCentre.add(pBotonsMov)

        pTancar.add(tancar)

        rs.first()
        visComarca()

        primer.addActionListener {
            rs.first()
            visComarca()
        }

        anterior.addActionListener {
            if (!rs.isFirst())
                rs.previous()
            visComarca()
        }

        seguent.addActionListener {
            if (!rs.isLast())
                rs.next()
            visComarca()
        }

        ultim.addActionListener {
            rs.last()
            visComarca()
        }

        tancar.addActionListener{
            rs.close()
            st.close()
            con.close()
            System.exit(0)
        }
    }

    fun visComarca() {
        nomComarca.setText(rs.getString(1))
        nomProvincia.setText(rs.getString(2))
    }
}

fun main(args: Array<String>) {
    EventQueue.invokeLater({ Exemple_4_41_Scroll().isVisible = true })
}