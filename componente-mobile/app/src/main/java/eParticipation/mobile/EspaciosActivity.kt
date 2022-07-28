package eParticipation.mobile

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.json.JSONException
import org.json.JSONObject
import kotlin.math.*

class EspaciosActivity : AppCompatActivity() {
    private var espacios = ArrayList<JSONObject>()
    private var gps: Location? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var esp1: TextView? = null
    private var esp2: TextView? = null
    private var esp3: TextView? = null
    private var sharedPref: SharedPreferences? = null
    private var endpoint: String? = null
    private var token: String? = null
    private var usuario: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_espacios)
        esp1 = findViewById(R.id.esp1)
        esp2 = findViewById(R.id.esp2)
        esp3 = findViewById(R.id.esp3)
        sharedPref = getSharedPreferences("myPref", MODE_PRIVATE)
        token = sharedPref?.getString("token", null) //obtengo el token, si no existe retorna null
        usuario = sharedPref?.getString("user", null)
        endpoint = sharedPref?.getString("endpoint", "")

        //permisos de ubicacion
        if (ActivityCompat.checkSelfPermission(
                this@EspaciosActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            //Cuando tenemos el permiso
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            getEspacios()
            //location
            //onclick del primero
        } else {
            ActivityCompat.requestPermissions(
                this@EspaciosActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                44
            )
            finish()
        }
    }

    // Obtengo la ultima ubicacion conocida. En algunas situaciones raras puede ser null.
    @get:SuppressLint("MissingPermission")
    val location: Unit
        get() {
            fusedLocationClient?.lastLocation
                ?.addOnSuccessListener(this
                ) { location ->
                    if (location != null) {
                        println(location.latitude.toString())
                        println(location.longitude.toString())
                        gps = location
                        getEspacios()
                    }
                }
        }

    private fun getEspacios() {
        println("entro al getEspacios")
        // hago el pedido de los espacios participativos
        // val url = "$endpoint/backend-web/rest/espacios/listar"
        val url = "$endpoint/backend-web/eParticipation/iniciativa/listar"
        //{"id":"1","nombre":"iniciativa1","descripcion":"desc1","fecha":"2022-07-05","estado":"En_espera","creador":"rubio@gmail","adheridos":["colo@gmail","morocho@gmail"],"seguidores":["colo@gmail","morocho@gmail","rubio@gmail"],"comentarios":[],"recurso":null,"pushNotify":true}
        val rq = Volley.newRequestQueue(this)
        val or: JsonArrayRequest = object : JsonArrayRequest(
            Method.GET, url, null,
            Response.Listener { response ->
                for (i in 0 until response.length()) {
                    try {
                        espacios.add(response.getJSONObject(i))
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
                continuaFlujo()
            },
            Response.ErrorListener { error ->
                Log.e("Response: ", error.toString())
                Toast.makeText(
                    this@EspaciosActivity,
                    "Todavia no se asignaron los espacios",
                    Toast.LENGTH_SHORT
                ).show()
            }) {
            //esta es la parte que agrega el header al request
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                //params["Cookie"] = cookie!!
                params["authorization"] = token!!
                return params
            }
        }
        rq.add(or)
        //        OkHttpClient client = new OkHttpClient();
//        System.out.println(cookie);
//        Request request = new Request.Builder()
//                .addHeader("Cookie", cookie)
//                .url(url)
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) { //si falla
//                Log.e("Response: ", e.toString());
//                Toast.makeText(EspaciosActivity.this,"Todavia no se asignaron los espacios", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException { //si obtengo respuesta
//                try {
//                    System.out.println("##################################");
//                    System.out.println(response.code());
//                    System.out.println("##################################");
//                    JSONArray respuesta = new JSONArray(response.body());
//                    System.out.println(respuesta);
//                    for(int i=0; i<respuesta.length(); i++) {
//                        espacios.add(respuesta.getJSONObject(i));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                continuaFlujo();
//            }
//        });
    }

    fun continuaFlujo() {
        println("entro al continuarFlujo")
        //println(gps!!.latitude)
        println(espacios.size)
        try {
            //espacios = bubbleSort(espacios)
            //{"id":"1","nombre":"iniciativa1","descripcion":"desc1","fecha":"2022-07-05","estado":"En_espera","creador":"rubio@gmail","adheridos":["colo@gmail","morocho@gmail"],"seguidores":["colo@gmail","morocho@gmail","rubio@gmail"],"comentarios":[],"recurso":null,"pushNotify":true}
            esp1!!.text = espacios[0].getString("nombre")
            esp2!!.text = espacios[0].getString("descripcion")

            //seteo el onclick listener de esp1
            esp1!!.setOnClickListener {
                try {
                    //val c = iniciativas.getJSONObject(position) //puede que position sea -1
                    //println(c.toString())
                    //val fud = c.getJSONObject("fecha")
                    //val fechaCreacionJSON =
                        //fud.getString("dayOfMonth") + "/" + fud.getString("monthValue") + "/" + fud.getString(
                        //    "year"
                        //)
                    //val fechaCreacion = c.getString("fecha")
                    val i = Intent(this@EspaciosActivity, IniciativaActivity::class.java)
                    //i.putExtra("id", c.getString("idCertificado"))

                    i.putExtra("nombre", espacios[0].getString("nombre"))
                    i.putExtra("descripcion", espacios[0].getString("descripcion"))
                    i.putExtra("fecha", espacios[0].getString("fecha"))
                    i.putExtra("creador", espacios[0].getString("creador"))
                    //i.putExtra("iniciativas", c.getString("iniciativa"))
                    i.putExtra("recurso", espacios[0].getString("recurso"))
                    startActivity(i)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }


                // Ejemplo intent
                // Intent espacios = new Intent(EspaciosActivity.this, EspaciosActivity.class);
                // startActivity(espacios);

                //var gmmIntentUri: Uri? = null
                //try {
                //    gmmIntentUri = Uri.parse(
                //        "google.navigation:q=" + espacios[0].getString("latitud") + "," + espacios[0].getString(
                //            "longitud"
                //        )
                //    )
                //} catch (e: JSONException) {
                //    e.printStackTrace()
                //}
                //val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                //mapIntent.setPackage("com.google.android.apps.maps")
                //startActivity(mapIntent)
                //                    if (mapIntent.resolveActivity(getPackageManager()) != null) {
                //                        startActivity(mapIntent);
                //                    }
            }
            //seteo el onclick listener de esp2
            if (espacios.size >= 2) {
                esp2!!.text = espacios[1].getString("nombre")
                esp2!!.setOnClickListener {

                    try {
                        //val c = iniciativas.getJSONObject(position) //puede que position sea -1
                        //println(c.toString())
                        //val fud = c.getJSONObject("fecha")
                        //val fechaCreacionJSON =
                        //fud.getString("dayOfMonth") + "/" + fud.getString("monthValue") + "/" + fud.getString(
                        //    "year"
                        //)
                        //val fechaCreacion = c.getString("fecha")
                        val i = Intent(this@EspaciosActivity, IniciativaActivity::class.java)
                        //i.putExtra("id", c.getString("idCertificado"))

                        i.putExtra("nombre", espacios[1].getString("nombre"))
                        i.putExtra("descripcion", espacios[1].getString("descripcion"))
                        i.putExtra("fecha", espacios[1].getString("fecha"))
                        i.putExtra("creador", espacios[1].getString("creador"))
                        //i.putExtra("iniciativas", c.getString("iniciativa"))
                        i.putExtra("recurso", espacios[1].getString("recurso"))
                        startActivity(i)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }





                    //var gmmIntentUri: Uri? = null
                    //try {
                    //    gmmIntentUri = Uri.parse(
                    //        "google.navigation:q=" + espacios[1].getString("latitud") + "," + espacios[1].getString(
                    //            "longitud"
                    //        )
                    //    )
                    //} catch (e: JSONException) {
                    //    e.printStackTrace()
                    //}
                    //val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    //mapIntent.setPackage("com.google.android.apps.maps")
                    //startActivity(mapIntent)
                }
                //seteo el onclick listener de esp3
                if (espacios.size >= 3) {
                    esp3!!.text = espacios[2].getString("nombre")
                    esp3!!.setOnClickListener {

                        try {
                            //val c = iniciativas.getJSONObject(position) //puede que position sea -1
                            //println(c.toString())
                            //val fud = c.getJSONObject("fecha")
                            //val fechaCreacionJSON =
                            //fud.getString("dayOfMonth") + "/" + fud.getString("monthValue") + "/" + fud.getString(
                            //    "year"
                            //)
                            //val fechaCreacion = c.getString("fecha")
                            val i = Intent(this@EspaciosActivity, IniciativaActivity::class.java)
                            //i.putExtra("id", c.getString("idCertificado"))

                            i.putExtra("nombre", espacios[2].getString("nombre"))
                            i.putExtra("descripcion", espacios[2].getString("descripcion"))
                            i.putExtra("fecha", espacios[2].getString("fecha"))
                            i.putExtra("creador", espacios[2].getString("creador"))
                            //i.putExtra("iniciativas", c.getString("iniciativa"))
                            i.putExtra("recurso", espacios[2].getString("recurso"))
                            startActivity(i)
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                        //var gmmIntentUri: Uri? = null
                        //try {
                        //    gmmIntentUri = Uri.parse(
                        //        "google.navigation:q=" + espacios[2].getString("latitud") + "," + espacios[2].getString(
                        //            "longitud"
                        //        )
                        //    )
                        //} catch (e: JSONException) {
                        //    e.printStackTrace()
                        //}
                        //val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                        //mapIntent.setPackage("com.google.android.apps.maps")
                        //startActivity(mapIntent)
                    }
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    //acomoda el arreglo con los mas cercanos arriba
    @Throws(JSONException::class)
    fun bubbleSort(v: ArrayList<JSONObject>): ArrayList<JSONObject> {
        var sorted = false
        var temp: JSONObject
        while (!sorted) {
            sorted = true
            for (i in 0 until v.size - 1) {
                val d1 = getDistancia(
                    v[i].getString("latitud").toDouble(),
                    v[i].getString("longitud").toDouble()
                )
                val d2 = getDistancia(
                    v[i + 1].getString("latitud").toDouble(),
                    v[i + 1].getString("longitud").toDouble()
                )
                if (d1 > d2) {
                    temp = v[i]
                    v[i] = v[i + 1]
                    v[i + 1] = temp
                    sorted = false
                }
                //                if (v[i] > v[i+1]) {
//                    temp = v[i];
//                    v[i] = v[i+1];
//                    v[i+1] = temp;
//                    sorted = false;
//                }
            }
        }
        return v
    }

    private fun getDistancia(lat2: Double, lng2: Double): Double {
        val radioTierra = 6371.0 //en kilómetros
        val dLat = Math.toRadians(lat2 - gps!!.latitude)
        val dLng = Math.toRadians(lng2 - gps!!.longitude)
        val sindLat = sin(dLat / 2)
        val sindLng = sin(dLng / 2)
        val va1 =
            sindLat.pow(2.0) + (sindLng.pow(2.0)
                    * cos(Math.toRadians(gps!!.latitude)) * cos(
                Math.toRadians(lat2)
            ))
        val va2 =
            2 * atan2(sqrt(va1), sqrt(1 - va1))
        return radioTierra * va2
    }
}