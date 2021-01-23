package mx.tecnm.tepic.ladm_u4_practica2_serviciosms

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BaseDatos(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE DATOS(ID_ACT VARCHAR(50), KD_MW REAL, KD_WZ REAL, KD_CW REAL, KD_CM REAL)")
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {

    }

}