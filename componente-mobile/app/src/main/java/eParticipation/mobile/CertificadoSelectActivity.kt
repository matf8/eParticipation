package eParticipation.mobile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.ByteArrayInputStream
import java.io.InputStream

class CertificadoSelectActivity : AppCompatActivity() {
    private var nombre: TextView? = null
    private var fecha: TextView? = null
    private var insignia: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_certificado_select)

        nombre = findViewById(R.id.nombre)
        fecha = findViewById(R.id.fecha)
        insignia = findViewById(R.id.insignia)

        //obtengo las variables que me manda el intent
        nombre?.text = intent.getStringExtra("nombre")
        fecha?.text = intent.getStringExtra("fecha")

        //if (intent.getStringExtra("insignia") != null) {
            //val imagenis: InputStream;
            //imagenis = ByteArrayInputStream(intent.getStringExtra("insignia")?.toByteArray())

            //val bytes: ByteArray = Base64.decode(intent.getStringExtra("insignia"), Base64.DEFAULT);
            //val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

            //insignia?.setImageBitmap(bitmap)

            //insignia?.setImageBitmap(BitmapFactory.decodeStream(imagenis));
        //}
    }
}