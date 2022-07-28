package eParticipation.mobile

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class CertificadoActivity : AppCompatActivity() {
    private var token: String? = null
    private var correo: String? = null
    private var certificados = ArrayList<JSONObject>()
    private var sharedPref: SharedPreferences? = null
    private var endpoint: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_certificado)

        //obtengo el token
        sharedPref = getSharedPreferences("myPref", MODE_PRIVATE) //obtengo el sharedPreference
        token = sharedPref?.getString("token", null) //obtengo el token, si no existe retorna null
        correo = sharedPref?.getString("user", null)
        endpoint = sharedPref?.getString("endpoint", "")
        getCertificados()
    }//Toast.makeText(EspaciosActivity.this,"Todavia no se agregaron los espacios", Toast.LENGTH_SHORT).show();

    //hago el pedido de los espacios
    private fun getCertificados() {
            println("entro al getCertificados")
            //hago el pedido de los certificados
            val url = "$endpoint/backend-web/eParticipation/usuario/listarCertificados?user=${correo}"
            val rq = Volley.newRequestQueue(this)
            val or: JsonArrayRequest = object : JsonArrayRequest(
                Method.GET, url, null,
                Response.Listener { response ->
                    for (i in 0 until response.length()) {
                        try {
                            certificados.add(response.getJSONObject(i))
                            continuarFlujo()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                },
                Response.ErrorListener { error ->
                    Log.e("Response: ", error.toString())
                    //Toast.makeText(CertificadoActivity.this,"Todavia no se agregaron los certificados", Toast.LENGTH_SHORT).show();
                }
            ) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val header = HashMap<String, String>()
                    header["authorization"] = token!!
                    return header
                }
            }
            rq.add(or)
        }

    @Throws(JSONException::class)
    fun continuarFlujo() {
        val listaIniciativas = findViewById<View>(R.id.listaIniciativas) as ListView
        val iniciativasAux = ArrayList<String>()
        for (i in 0 until certificados.size) {
            iniciativasAux.add(certificados[i].getString("nombre")) //   iniciativas.getJSONObject(i).getString("iniciativa"))
        }
        val adaptador = ArrayAdapter<String?>(this, android.R.layout.simple_list_item_1,
            iniciativasAux as List<String?>)
            //sobreescribo esto para poder cambiarle el color a los textView dentro de la lista
//            fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//                // Get the Item from ListView
//                val view = getView(position, convertView, parent)
//
//                // Initialize a TextView for ListView each Item
//                val tv = view.findViewById<View>(android.R.id.text1) as TextView
//
//                // Set the text color of TextView (ListView Item)
//                tv.setTextColor(Color.WHITE)
//
//                // Generate ListView Item using TextView
//                return view
//            }
        listaIniciativas.adapter = adaptador
        listaIniciativas.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                try {
                    val c = certificados[position] //iniciativas.getJSONObject(position) //puede que position sea -1
                    //println(c.toString())
                    //val fud = c.getJSONObject("fecha")
                    //val fechaCreacionJSON =
                        //fud.getString("dayOfMonth") + "/" + fud.getString("monthValue") + "/" + fud.getString(
                        //    "year"
                        //)
                    val i = Intent(this@CertificadoActivity, CertificadoSelectActivity::class.java)
                    //i.putExtra("id", c.getString("idCertificado"))
                    i.putExtra("nombre", c.getString("nombre"))
                    i.putExtra("fecha", c.getString("fecha"))
                    //i.putExtra("iniciativas", c.getString("iniciativa"))
                    //i.putExtra("insignia", c.getString("insignia"))
                    startActivity(i)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
    }
}