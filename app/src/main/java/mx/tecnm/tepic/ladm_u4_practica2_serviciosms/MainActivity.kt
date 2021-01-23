package mx.tecnm.tepic.ladm_u4_practica2_serviciosms

import android.content.pm.PackageManager
import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val siPermisoReceiver = 1
    val siPermisoEnviar = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS) !=
            PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.RECEIVE_SMS), siPermisoReceiver)
        }

        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.SEND_SMS), siPermisoEnviar)
        }

        button.setOnClickListener {
            try {
                var baseDatos = BaseDatos(this,"servicioKD",null,1)
                var insertar = baseDatos.writableDatabase
                var id = editTextID.text.toString()
                var kdmw = editTextKDMW.text.toString().toFloat()
                var kdwz = editTextKDWZ.text.toString().toFloat()
                var kdcw = editTextKDCW.text.toString().toFloat()
                var kdcm = editTextKDCM.text.toString().toFloat()
                var SQL = "INSERT INTO DATOS VALUES ('${id}', ${kdmw}, ${kdwz}, ${kdcw}, ${kdcm})"
                insertar.execSQL(SQL)
                baseDatos.close()

                Toast.makeText(this, "SE INSERTARON CORRECTAMENTE STATS DE: \n${id}", Toast.LENGTH_LONG)
                    .show()

                limpiarCampos()

            } catch (err: SQLiteException) {
                Toast.makeText(this, err.message, Toast.LENGTH_LONG)
                    .show()
            }
        }

    }

    private fun limpiarCampos() {
        editTextID.setText("")
        editTextKDMW.setText("")
        editTextKDWZ.setText("")
        editTextKDCW.setText("")
        editTextKDCM.setText("")
    }

}