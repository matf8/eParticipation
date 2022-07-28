package eParticipation.mobile

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.sql.Blob

class IniciativaActivity : AppCompatActivity() {
    private var nombre: TextView? = null
    private var descripcion: TextView? = null
    private var fecha: TextView? = null
    private var creador: TextView? = null
    private var insignia: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iniciativa)

        nombre = findViewById(R.id.nombre)
        descripcion = findViewById(R.id.descripcion)
        fecha = findViewById(R.id.fecha)
        creador = findViewById(R.id.creador)
        insignia = findViewById(R.id.insignia)

        //obtengo las variables que me manda el intent
        nombre?.text = intent.getStringExtra("nombre")
        descripcion?.text = intent.getStringExtra("descripcion")
        fecha?.text = intent.getStringExtra("fecha")
        creador?.text = intent.getStringExtra("creador")

        if (intent.getStringExtra("recurso") != null) {
            val imagenis: InputStream;
            imagenis = ByteArrayInputStream(intent.getStringExtra("recurso")?.toByteArray())

            insignia?.setImageBitmap(BitmapFactory.decodeStream(imagenis));
        }
    }
}