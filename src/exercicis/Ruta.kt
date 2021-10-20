package exercicis

import java.io.Serializable

class Ruta (var nom: String,
            var desnivell: Int,
            var desnivellAcumulat: Int,
            var llistaDePunts: MutableList<PuntGeo>): Serializable {

    companion object {
        private const val serialVersionUID: Long = 1
    }

    fun addPunt(p: PuntGeo){
        llistaDePunts.add(p)
    }

    fun getPunt(i: Int): PuntGeo{
        return llistaDePunts.get(i)
    }

    fun getPuntNom(i: Int): String {
        return llistaDePunts.get(i).nom
    }

    fun getPuntLatitud(i: Int): Double {
        return llistaDePunts.get(i).coord.latitud
    }

    fun getPuntLongitud(i: Int): Double {
        return llistaDePunts.get(i).coord.longitud
    }

    fun size(): Int {
        return llistaDePunts.size
    }

    /**
     * Deuvelve un String con la informacion correspondiente
     * al objeto ruta.
     */
    fun mostrarRuta():String {
        //String de las caracteristicas principales de la ruta
        val ruta = "Ruta: $nom\nDesnivell: $desnivell\nDesnivell acumulat: $desnivellAcumulat\n" +
                "Te ${size()} punts\n"
        //String para cada uno de los puntos de la ruta
        var punts = ""
        //Bucle para recoger cada punto de la ruta
        for (i in 0..llistaDePunts.size-1){
            punts += "Punt ${i+1}: ${llistaDePunts.get(i).nom} (${getPuntLatitud(i)}, ${getPuntLongitud(i)})\n"
        }
        //Concatena ambas strings
        return ruta.plus(punts)
    }
}

class PuntGeo(
    var nom:String,
    var coord:Coordenades
):Serializable

class Coordenades (
    var latitud:Double,
    var longitud:Double
):Serializable

