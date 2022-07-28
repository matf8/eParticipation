package eParticipation.mobile

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private var sharedPref: SharedPreferences? = null
    var loggeado = false
    private var endpoint: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = getSharedPreferences("myPref", MODE_PRIVATE) //obtengo el sharedPreference
        val token = sharedPref?.getString("token", null) //obtengo el token, si no existe retorna null
        //https://eparticipation.web.elasticloud.uy/backend-web/
        sharedPref?.edit()
            ?.putString("endpoint", "https://eparticipation.web.elasticloud.uy")
            ?.apply() //seteo el endpoint a donde quiero que vaya
        //https://eparticipation.web.elasticloud.uy
        endpoint = sharedPref?.getString("endpoint", "")
        println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$")
        println(token)
        println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$")
        if (token != null) {
            //val url = "$endpoint/backend-web/eParticipation/usuario/checkToken"
            println("#####################")
            //println(url)
            println("#####################")
            val client = OkHttpClient();
            //defino el request

            loggeado = true
            continuoFlujo()

            //Request para checkear la cookie la cookie
            //val request: Request = Request.Builder()
            //    .addHeader("Cookie", cookie)
            //    .url(url)
            //    .build()

            //hago el http request
//            client.newCall(request).enqueue(object : Callback {
//                override fun onFailure(call: Call, e: IOException) { //si falla
//                    println("Error en la conexion")
//                    println(e.message)
//                }
//
//                @Throws(IOException::class)
//                override fun onResponse(call: Call, response: Response) { //si obtengo respuesta
//                    println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$")
//                    println(response.code)
//                    if (response.code == 200) loggeado = true
//                    println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$")
//                    println(loggeado)
//                    println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$")
//                    continuoFlujo()
//                }
//            })
        } else continuoFlujo()
    }

    private fun continuoFlujo() {
        if (!loggeado) {
            setContentView(R.layout.activity_main) //seteo el xml
            val loginButton = findViewById<Button>(R.id.login)
            loginButton.setOnClickListener {
                //println("#####################")
                //println("$endpoint/backend-web/login?tipoUsuario=ciudadano")
                //println("#####################")

                //val url = "https://auth-testing.iduruguay.gub.uy/oidc/v1/authorize?response_type=code&scope=openid%20personal%20email&client_id=890192&state=asdf934087&redirect_uri=https%3A%2F%2Fopenidconnect.net%2Fcallback"
                //val i = Intent(Intent.ACTION_VIEW)
                //i.data = Uri.parse(url)
                //startActivity(i)

                val browserIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://auth-testing.iduruguay.gub.uy/oidc/v1/authorize?response_type=code&scope=openid%20document&client_id=890192&state=asdf934087&redirect_uri=http%3A%2F%2Flocalhost")
                    //Uri.parse("https://eparticipation.web.elasticloud.uy/backend-web/auth")
                    //Uri.parse("https://auth-testing.iduruguay.gub.uy/oidc/v1/authorize?response_type=code&scope=openid%20personal%20email&client_id=890192&state=asdf934087&redirect_uri=https%3A%2F%2Fopenidconnect.net%2Fcallback")
                )
                startActivity(browserIntent)
                println("funciona")
            }
        } else {
            val menu = Intent(this@MainActivity, SecondActivity::class.java)
            startActivity(menu)
        }
    }
}
