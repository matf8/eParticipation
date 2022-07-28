package eParticipation.mobile

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*

//import com.google.firebase.messaging.FirebaseMessaging;
class SecondActivity : AppCompatActivity() {
    //esta clase se va a crearse cuando se llegue a un http://localhost
    private var token: String? = null
    //private var tokenFirebase: Unit
    private var nombre: TextView? = null
    private var certificado: CardView? = null
    private var espacio: CardView? = null
    private var toolbar: Toolbar? = null
    private var sharedPref: SharedPreferences? = null
    private var endpoint: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        sharedPref = getSharedPreferences("myPref", MODE_PRIVATE)
        endpoint = sharedPref?.getString("endpoint", "")
        nombre = findViewById<View>(R.id.nombre) as TextView
        certificado = findViewById(R.id.certificado)
        espacio = findViewById(R.id.espacio)
        toolbar = findViewById<View>(R.id.navbar) as Toolbar
        sharedPref = getSharedPreferences("myPref", MODE_PRIVATE) //obtengo el sharedPreference
        token = sharedPref?.getString("token", null) //obtengo el token, si no existe retorna null
        //sendToken();
        //getNombre()
        //tokenFirebase
        //seteo el onclick del icono en toolbar
        toolbar!!.setNavigationOnClickListener {
            //sharedPref?.edit()?.remove("token")?.apply() //elimino el token y cierro la sesion
            val tokenID = sharedPref?.getString("tokenID", null)
            sharedPref?.edit()?.clear()?.apply() // Elimino todos los datos guardados
            val main = Intent(applicationContext, MainActivity::class.java)
            startActivity(main)

            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://auth-testing.iduruguay.gub.uy/oidc/v1/logout?id_token_hint=${tokenID}&post_logout_redirect_uri=http://localhost&state=logout")
            )
            startActivity(browserIntent)

            finish()
        }

        //onClick del certificado
        certificado?.setOnClickListener {
            val certificado = Intent(this@SecondActivity, CertificadoActivity::class.java)
            certificado.putExtra("token", token)
            startActivity(certificado)
        }

        //onclick del espacio
        espacio?.setOnClickListener {
            val espacios = Intent(this@SecondActivity, EspaciosActivity::class.java)
            startActivity(espacios)
        }
    }

    private fun getNombre() {
        val url = "$endpoint/backend-web/rest/mobile/id"
        val client = OkHttpClient()
        //defino el request
        val request: Request = Request.Builder()
            .addHeader("authorization", token.toString())
            .url(url)
            .build()

        //hago el http request
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) { //si falla
                e.printStackTrace()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) { //si obtengo respuesta
                val name = Objects.requireNonNull(response.body).string()
                println(name)
                nombre!!.text = name
            }
        })
    }

    // Get new FCM registration token
    private val tokenFirebase: Unit
        // Log and toast
        get() {
            FirebaseMessaging.getInstance().token
                .addOnCompleteListener(object : OnCompleteListener<String?> {
                    override fun onComplete(task: Task<String?>) {
                        if (!task.isSuccessful) {
                            Log.w(
                                "TOKEN_FAILED",
                                "Fetching FCM registration token failed",
                                task.exception
                            )
                            return
                        }

                        // Get new FCM registration token
                        val token: String? = task.result

                        // Log and toast
                        if (token != null) {
                            Log.d("TAG", token)
                            sendToken(token)
                        }
                    }
                })
        }

    private fun sendToken(token: String) { //envio el token al central
        val url = "$endpoint/backend-web/eParticipation/registrarApp"
        val client = OkHttpClient()

        //creo el jsonobject
        val firebaseJson = JSONObject()
        try {
            firebaseJson.put("mobileToken", token)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val firebaseBody: RequestBody =
            firebaseJson.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
        //defino el request
        val request: Request = Request.Builder()
            .addHeader("authorization", token.toString())
            .post(firebaseBody)
            .url(url)
            .build()

        //hago el http request
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) { //si falla
                e.printStackTrace()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) { //si obtengo respuesta
                Log.i("MOBILE_TOKEN", "token enviado con exito")
            }
        })
    }
}
