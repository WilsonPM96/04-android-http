package com.example.usrdel.aplicationmov

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.beust.klaxon.Klaxon
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button_camara.setOnClickListener{view: View ->
            iraActividadCamara()
        }
        "http://172.31.104.12:1337/Entrenador/1"
                .httpGet()
                .responseString { request, response, result ->
                    when (result) {
                        is Result.Failure -> {
                            val ex = result.getException()
                            Log.i("http-ejemplo", "Error ${ex.response}")
                        }
                        is Result.Success -> {
                            val jsonStringEntrenador = result.get()
                            Log.i("http-ejemplo", "Exito ${jsonStringEntrenador}")

                            val entrenador: Entrenador? = Klaxon()
                                    .parse<Entrenador>(jsonStringEntrenador)

                            if (entrenador != null) {
                                Log.i("http-ejemplo", "Nombre: ${entrenador.nombre}")
                                Log.i("http-ejemplo", "Apellido: ${entrenador.apellido}")
                                Log.i("http-ejemplo", "Id: ${entrenador.id}")
                                Log.i("http-ejemplo", "Medallas: ${entrenador.medallas}")
                                Log.i("http-ejemplo", "Edad: ${entrenador.edad}")
                                Log.i("http-ejemplo", "Creado: ${entrenador.createdAtDate}")
                                Log.i("http-ejemplo", "Actualizado: ${entrenador.updatedAtDate}")

                                entrenador.pokemons.forEach { pokemon: Pokemon ->
                                    Log.i("http-ejemplo", "Nombre ${pokemon.nombre}")
                                    Log.i("http-ejemplo", "Tipo ${pokemon.tipo}")
                                    Log.i("http-ejemplo", "Numero ${pokemon.numero}")
                                }


                            } else {
                                Log.i("http-ejemplo", "Entrenador nulo")
                            }


                        }
                    }
                }
    }


    class Entrenador(var nombre: String,
                     var apellido: String,
                     var edad: Int,
                     var medallas: Int,
                     var createdAt: Long,
                     var updatedAt: Long,
                     var id: Int,
                     var pokemons: List<Pokemon>) {
        var createdAtDate = Date(updatedAt)
        var updatedAtDate = Date(createdAt)


    }

    class Pokemon(var nombre: String,
                  var numero: Int,
                  var tipo: String,
                  var createdAt: Long,
                  var updatedAt: Long,
                  var id: Int,
                  var entrenadorId: Int) {
        var createdAtDate = Date(updatedAt)
        var updatedAtDate = Date(createdAt)
    }
    fun iraActividadCamara() {
        val intent = Intent(this, CamaraActivity::class.java)
        startActivity(intent)
    }
}