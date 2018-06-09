package com.example.usrdel.aplicationmov

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.beust.klaxon.Klaxon
import com.beust.klaxon.json
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        "http://172.29.64.40:1337/Entrenador/2".httpGet().responseString { request, response, result ->
            //do something with response
            when (result) {
                is Result.Failure -> {
                    val ex = result.getException()
                    Log.i("http-ejemplo", "${ex.response}")
                }
                is Result.Success -> {
                    val jsonStringEntrenador = result.get()
                    Log.i("http-ejemplo", "${jsonStringEntrenador}")

                    val entrenador:Entrenador? = Klaxon().
                            parse<Entrenador>(jsonStringEntrenador)

                    if(entrenador!=null){
                        Log.i("http-ejemplo","Nombre:${entrenador!!.nombre}")
                        Log.i("http-ejemplo","Apellido:${entrenador!!.apellido}")
                        Log.i("http-ejemplo","Medallas:${entrenador!!.medallas}")
                        Log.i("http-ejemplo","Edad:${entrenador!!.edad}")
                        Log.i("http-ejemplo","Creado:${entrenador!!.createdAtDate}")
                        Log.i("http-ejemplo","Actualizado:${entrenador!!.updatedAtDate}")
                        Log.i("http-ejemplo","ID:${entrenador!!.id}")
                    }  else {
                        Log.i("http-ejemplo","Entrenado Nulo")
                    }

                }
            }
        }
    }

    class Entrenador(var nombre:String,
                     var apellido: String,
                     var edad:String,
                     var medallas:String,
                     var createdAt: Long,
                     var updatedAt:Long,
                     var id:Int,
                     var pokemons){

      var createdAtDate = Date(createdAt)
        var updatedAtDate = Date(updatedAt)
        }

    class Pokemon(var nombre:String,
                     var numero: Int,
                     var tipo:String,
                     var createdAt: Long,
                     var updatedAt:Long,
                     var id:Int,
                  var entrenadorId: Entrenador){

        var createdAtDate = Date(createdAt)
        var updatedAtDate = Date(updatedAt)
    }

}

