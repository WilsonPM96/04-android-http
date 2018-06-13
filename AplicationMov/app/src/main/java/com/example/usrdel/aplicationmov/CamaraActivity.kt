package com.example.usrdel.aplicationmov

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.util.Log
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import kotlinx.android.synthetic.main.activity_camara.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class CamaraActivity : AppCompatActivity() {
var directorioActualImagen = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camara)
        val fotoActual = File(Environment.getExternalStorageDirectory().path + "/Android/data/com.example.usrdel.aplicationmov/files/Pictures/JPEG_20180613_144429_341583551977592652.jpg")
        val fotoActualBitmap = BitmapFactory.decodeFile(fotoActual.absolutePath)
        imageView_camara.setImageBitmap(fotoActualBitmap)
        boton_tomarfoto.setOnClickListener{view ->
            tomarFoto()
        }
    }

    private fun tomarFoto() {
        val archivoImagen = crearArchivo("JPEG_", Environment.DIRECTORY_PICTURES, ".jpg")
        directorioActualImagen = archivoImagen.absolutePath
        enviarIntentFoto(archivoImagen)
    }
    private fun crearArchivo(prefijo: String, directorio: String, extension: String): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = prefijo + timeStamp + "_"
        val storageDir = getExternalFilesDir(directorio)
        directorioActualImagen = imageFileName + extension
        return File.createTempFile(
                imageFileName, /* prefix */
                extension, /* suffix */
                storageDir      /* directory */
        )
    }

    private fun enviarIntentFoto(archivo: File) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoURI: Uri = FileProvider.getUriForFile(
                this,
                "com.example.usrdel.aplicationmov.fileprovider",
                archivo)
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI) //donde se guarda la foto
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, TOMAR_FOTO_REQUEST)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {

        when(requestCode){
            TOMAR_FOTO_REQUEST ->
                if(resultCode == Activity.RESULT_OK){
                    val fotoActualBitmap = BitmapFactory.decodeFile(directorioActualImagen)
                    image_view_foto.setImageBitmap(fotoActualBitmap)
                    obtenerInfoCodigoBarras(fotoActualBitmap)
            }
        }
        }
    fun obtenerInfoCodigoBarras(bitmap: Bitmap) {
        val imagencodigo = FirebaseVisionImage.fromBitmap(bitmap)
        val detector = FirebaseVision.getInstance()
                .visionBarcodeDetector

        val result = detector.detectInImage(imagencodigo)
                .addOnSuccessListener { barCodes ->
                    Log.i("info", "------- tamano del barcode ${barCodes.size}")

                    for (barcode in barCodes) {
                        val bounds = barcode.getBoundingBox()
                        val corners = barcode.getCornerPoints()
                        val rawValue = barcode.getRawValue()

                        Log.i("info", "------- $bounds")
                        Log.i("info", "------- $corners")
                        Log.i("info", "------- $rawValue")

                    }
                }
                .addOnFailureListener {
                    Log.i("info", "------- No reconocio nada")
                }
    }

    companion object {
        val TOMAR_FOTO_REQUEST = 1
    }
}
class GenericFileProvider : FileProvider () {

}