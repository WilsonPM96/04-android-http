package com.example.usrdel.aplicationmov

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.github.kittinunf.fuel.httpGet
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import org.jetbrains.anko.toast

class AnkoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anko)
        Log.i("async", "Empezo")
        async(UI) {
            llamarPosts("1")
            Log.i("async", "Luego de llamar post")
            val datosDeSesionDeferred: Deferred<String> = bg {
                // Runs in background
                obtenerSesion()
            }
            //usamos respuestas de tareas en background
            mostrarSesion(datosDeSesionDeferred.await())

            }
        Log.i("async", "Termino")

        }
        fun llamarPosts(idPost: String){
            val url = "https://jsonplaceholder.typicode.com/posts/$idPost"
            url.httpGet().responseString{
                request, response, result ->
                Log.i("async", "request: $request")
                Log.i("async", "response: $response")
                Log.i("async", "result: $result")
            }
        }
    fun obtenerSesion(): String {
        return "Esta es una sesion"
    }
    fun mostrarSesion(mensaje: String): String {
        toast(mensaje)
        return mensaje
    }
    }


