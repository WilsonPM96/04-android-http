package com.example.usrdel.aplicationmov

import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import com.google.android.gms.maps.*

import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_google_maps.*
import java.util.*
import kotlin.collections.ArrayList

class GoogleMapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnCameraIdleListener,
        GoogleMap.OnPolylineClickListener,
        GoogleMap.OnPolygonClickListener {

    var usuarioTienePermiososLocalizacion = false

    var mMarkerArray = ArrayList<Marker>()

    val epnLatLang = LatLng(-0.2102535,-78.4892594)
    val zoom = 17f
    val casaCulturaLatLang = LatLng(-0.210066, -78.495162)
    val superMaxiLatLang = LatLng(-0.206692, -78.486990)
    private val casaCultura: CameraPosition = CameraPosition.Builder()
            .target(casaCulturaLatLang)
            .zoom(zoom)
//            .bearing(300f)
//            .tilt(50f)
            .build()

    private val superMaxi: CameraPosition = CameraPosition.Builder()
            .target(superMaxiLatLang)
            .zoom(zoom)
//            .bearing(300f)
//            .tilt(50f)
            .build()


    override fun onCameraMoveStarted(p0: Int) {

    }

    override fun onCameraMove() {

    }

    override fun onCameraMoveCanceled() {

    }

    override fun onCameraIdle() {

    }

    override fun onPolylineClick(p0: Polyline?) {
        Log.i("google-wilson",  "Dio click en la ruta $p0")

    }

    override fun onPolygonClick(p0: Polygon?) {
        Log.i("google-wilson",  "Dio click en la ruta $p0")

    }

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_maps)

        solicitarPermisosLocalizacion()
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    private val COLOR_BLACK_ARGB = -0x1000000
    private val COLOR_WHITE_ARGB = -0x1
    private val COLOR_GREEN_ARGB = -0xc771c4
    private val COLOR_PURPLE_ARGB = -0x7e387c
    private val COLOR_ORANGE_ARGB = -0xa80e9
    private val COLOR_BLUE_ARGB = -0x657db

    private val POLYGON_STROKE_WIDTH_PX: Float = 8.toFloat()
    private val PATTERN_DASH_LENGTH_PX = 20
    private val PATTERN_GAP_LENGTH_PX = 20
    private val DOT = Dot()
    private val DASH = Dash(PATTERN_DASH_LENGTH_PX.toFloat())
    private val GAP = Gap(PATTERN_GAP_LENGTH_PX.toFloat())

    private val PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DASH)

    // Create a stroke pattern of a dot followed by a gap, a dash, and another gap.
    private val PATTERN_POLYGON_BETA = Arrays.asList(DOT, GAP, DASH, GAP)
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        with(googleMap) {
            var polyline1 = googleMap.addPolyline(PolylineOptions()
                    .clickable(true)
                    .add(
                            LatLng(-0.210373, -78.493893),
                            LatLng(-0.206618, -78.487978),
                            LatLng(-0.207401, -78.487141),
                            LatLng(-0.209488, -78.490285)
                    )
            )

            var polygon1 = googleMap.addPolygon(PolygonOptions()
                    .clickable(true)
                    .add(
                            LatLng(-0.209484, -78.490270),
                            LatLng(-0.208583, -78.488963),
                            LatLng(-0.209436, -78.488089),
                            LatLng(-0.210276, -78.489784)

                    )

            )

            polygon1.tag = "alpha"

            formatearEstiloPoligono(polygon1)


            establecerListeners(googleMap)
            establecerSettings(googleMap)
            anadirMarcador(epnLatLang, "Ciudad de quito")
            moverCamaraPorLatLongZoom(epnLatLang, zoom)


            button_quito_julio_andrade.setOnClickListener { v ->
                anadirMarcador(casaCulturaLatLang, "Marcador en Quito Casa Cultura")


                moverCamaraPorPosicion(casaCultura)
            }

            button_quito.setOnClickListener { v ->
                anadirMarcador(superMaxiLatLang, "Marcador en Quito Supermaxi")


                moverCamaraPorPosicion(superMaxi)
            }
        }
        // Add a marker in Sydney and move the camera
    }

    private fun formatearEstiloPoligono(polygon: Polygon) {
        var type = ""
        // Get the data object stored with the polygon.
        if (polygon.tag != null) {
            type = polygon.tag.toString()
        }

        var pattern: List<PatternItem>? = null
        var strokeColor = COLOR_BLACK_ARGB
        var fillColor = COLOR_WHITE_ARGB

        when (type) {
        // If no type is given, allow the API to use the default.
            "alpha" -> {
                // Apply a stroke pattern to render a dashed line, and define colors.
                pattern = PATTERN_POLYGON_ALPHA
                strokeColor = COLOR_GREEN_ARGB
                fillColor = COLOR_PURPLE_ARGB
            }
            "beta" -> {
                // Apply a stroke pattern to render a line of dots and dashes, and define colors.
                pattern = PATTERN_POLYGON_BETA
                strokeColor = COLOR_ORANGE_ARGB
                fillColor = COLOR_BLUE_ARGB
            }
        }

        polygon.strokePattern = pattern
        polygon.setStrokeWidth(POLYGON_STROKE_WIDTH_PX)
        polygon.strokeColor = strokeColor
        polygon.fillColor = fillColor
    }

    fun solicitarPermisosLocalizacion(){
        if (ContextCompat.checkSelfPermission(this.applicationContext,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            usuarioTienePermiososLocalizacion = true
        } else {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
    }

    private fun establecerSettings(googleMap: GoogleMap) {
        with(googleMap) {
            uiSettings.isZoomControlsEnabled = false
            uiSettings.isMyLocationButtonEnabled = true
        }
    }

    private fun establecerListeners(googleMap: GoogleMap) {
        with(googleMap) {
            setOnCameraIdleListener(this@GoogleMapsActivity)
            setOnCameraMoveStartedListener(this@GoogleMapsActivity)
            setOnCameraMoveListener(this@GoogleMapsActivity)
            setOnCameraMoveCanceledListener(this@GoogleMapsActivity)


            setOnPolylineClickListener(this@GoogleMapsActivity)
            setOnPolygonClickListener(this@GoogleMapsActivity)
        }
    }

    private fun anadirMarcador(latitudLongitud: LatLng, titulo: String) {
        mMarkerArray.forEach { marker: Marker ->
            marker.remove()
        }
        mMarkerArray = ArrayList<Marker>()
        val marker = mMap.addMarker(MarkerOptions().position(latitudLongitud).title(titulo))
        mMarkerArray.add(marker)
        Log.i("map-wilson", "$mMarkerArray")
    }

    private fun moverCamaraPorLatLongZoom(latitudLongitud: LatLng, zoom: Float) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latitudLongitud, zoom))
    }

    private fun moverCamaraPorPosicion(posicionCamara: CameraPosition) {
        changeCamera(CameraUpdateFactory.newCameraPosition(posicionCamara))
    }

    private fun changeCamera(update: CameraUpdate, callback: GoogleMap.CancelableCallback? = null) {
//        if (animateToggle.isChecked) {
//            if (customDurationToggle.isChecked) {
//                // The duration must be strictly positive so we make it at least 1.
//                map.animateCamera(update, Math.max(customDurationBar.progress, 1), callback)
//            } else {
//                map.animateCamera(update, callback)
//            }
//        } else {
        mMap.moveCamera(update)
//        }
    }
}
