package mx.tecnm.tepic.ladm_u4_practica2_serviciosms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteException
import android.os.Build
import android.telephony.SmsManager
import android.telephony.SmsMessage
import android.widget.Toast

class SmsReceiver : BroadcastReceiver() {

    var telefono = ""
    var mensaje : List<String> ?= null

    override fun onReceive(p0: Context, p1: Intent) {
        val extras = p1.extras

        if (extras != null) {
            var sms = extras.get("pdus") as Array<Any>

            for (indice in sms.indices) {
                val formato = extras.getString("format")

                var smsMensaje = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    SmsMessage.createFromPdu(sms[indice] as ByteArray, formato)
                } else {
                    SmsMessage.createFromPdu(sms[indice] as ByteArray)
                }

                var celularOrigen = smsMensaje.originatingAddress
                telefono = celularOrigen.toString()
                var contenidoSMS = smsMensaje.messageBody.toString()


                Toast.makeText(p0, "ENTRO CONTENIDO DE: ${celularOrigen}", Toast.LENGTH_LONG)
                    .show()

                verificarProceso(contenidoSMS, p0)
            }

        }
    }

    fun verificarProceso( m : String, context: Context){
        var arreglo = m.split(" ")
        mensaje = arreglo

        if (arreglo[0].equals("KD")) {
            if (arreglo.size == 3) {
                enviarMensaje(context)
            } else {
                errorSintaxis()
            }
        }

    }

    private fun errorSintaxis() {
        SmsManager.getDefault().sendTextMessage(telefono,null,
            "ERROR EN SU MENSAJE!!\nLA SINTAXIS CORRECTA ES: KD [IDACTIVISION] [SIGLASJUEGO:MW|WZ|CW|CM]\nEjemplo: KD Prueba08 WZ",null,null)
    }

    private fun enviarMensaje(context: Context) {
        var baseDatos = BaseDatos(context,"servicioKD",null,1)
        try {
            var trans = baseDatos.readableDatabase

            var resultados = trans.query("DATOS", arrayOf("KD_${mensaje?.get(2)}"), "ID_ACT=?", arrayOf(mensaje?.get(1)), null, null, null)

            if (resultados.moveToFirst()) {
                var kd = resultados.getFloat(0).toString()

                SmsManager.getDefault().sendTextMessage(telefono,null,
                        "SU KD RATIO EN ${mensaje?.get(2)} ES DE: ${kd}",null,null)
            } else {
                SmsManager.getDefault().sendTextMessage(telefono,null,
                        "ATENCION!!\nNO HAY STATS REGISTRADAS DEL USUARIO SOLICITADO",null,null)
            }
            trans.close()

        } catch (e: SQLiteException) {

        }
    }
}