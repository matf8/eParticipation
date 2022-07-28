package eParticipation.mobile

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import java.io.IOException


class ComeBackActivity : AppCompatActivity() {
    private var sharedPref: SharedPreferences? = null
    private var endpoint: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_come_back)

        // get or create SharedPreferences
        sharedPref = getSharedPreferences("myPref", MODE_PRIVATE)
        endpoint = sharedPref?.getString("endpoint", "")
        val resultado = intent.dataString
        val uriData = intent.data

        if (uriData != null) {
            var uriString = uriData.toString()
            val code = uriData.getQueryParameter("code")
            val state = uriData.getQueryParameter("state")
            uriString = "https://eparticipation.web.elasticloud.uy/backend-web/tokenMobile"
            //val url = uriString.replace(
            //    "http://localhost/".toRegex(),
            //    "$endpoint/backend-web/callback"
            //) //cambio el link para que se direccione a donde quiero
            println(uriString)

            Log.i("onCreate", "Resultado:$resultado")

            //defino el cliente para hacer el http request
            val client = OkHttpClient().newBuilder()
                .followRedirects(false)
                .build()

            val urlRequest: HttpUrl = HttpUrl.Builder()
                .addQueryParameter("code", code)
                .addQueryParameter("state", state)
                .scheme("https")
                .host("eparticipation.web.elasticloud.uy")
                .addPathSegments("backend-web/tokenMobile")
                .build()

            //defino el request
            val request: Request = Request.Builder()
                .url(urlRequest)
                .build()
            //hago el http request
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) { //si falla
                    e.printStackTrace()
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) { //si obtengo respuesta
                    //val ci = response.message
                    //println(response.body.toString())
                    //println(response.networkResponse?.body.toString())

                    //val cookie = response.headers("authorization")
                    val token = response.headers("tokenSesion")
                    val correo = response.headers("usuarioC")
                    val tokenID = response.headers("tokenID")
                    println(response.headers)
                    println(response.headers("tokenSesion"))
                    println(response.headers("usuarioC"))
                    println(response.headers("tokenID"))
                    println(response.code)
                    if (response.code == 302) {
                        println("#########################################")
                        // guardar token en SharedPreferences
                        sharedPref?.edit()?.putString("token", token[0])?.apply()

                        sharedPref?.edit()?.putString("user", correo[0])?.apply()

                        sharedPref?.edit()?.putString("tokenID", tokenID[0])?.apply()
                        println("#########################################")
                        goMenu()
                        //getNombre();
                    } else {
                        Log.i("InicioSesion", "No se inicio correctamente desde el back LoginServletTokenMobile")
                    }
                }
            })
        } else {
            finish()
        }
    }

    fun goMenu() {
        val menu = Intent(this@ComeBackActivity, SecondActivity::class.java)
        startActivity(menu)
    }
}