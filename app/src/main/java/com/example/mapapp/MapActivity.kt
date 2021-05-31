package com.example.mapapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mapapp.databinding.ActivityMapBinding
import com.example.mapapp.model.SearchResultEntitiy
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding : ActivityMapBinding
    private lateinit var map : GoogleMap
    private var currentMarker : Marker? = null

    private lateinit var searchResult : SearchResultEntitiy

    companion object{
       val SEARCH_RESULT_EXTRA_KEY ="SEARCH_RESULT_EXTRA_KEY"
        val CAMERA_ZOOM_LEVEL = 17f
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(::searchResult.isInitialized.not()){
            intent?.let {
                searchResult = it.getParcelableExtra<SearchResultEntitiy>(SEARCH_RESULT_EXTRA_KEY) ?: throw Exception("데이터가 존재하지 않습니다")
                setupGoogleMap()
            }
        }

    }

    private fun setupGoogleMap(){
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {

        this.map = map
        currentMarker =setupMarker(searchResult)

        currentMarker?.showInfoWindow()

    }

    private fun setupMarker(searchResultEntitiy: SearchResultEntitiy) : Marker{
        val positionLatLng = LatLng(
            searchResultEntitiy.locationLatLng.latitude.toDouble(), searchResultEntitiy.locationLatLng.longitude.toDouble()
        )
        val markerOptions = MarkerOptions().apply {
            position(positionLatLng)
            title(searchResultEntitiy.name)
            snippet(searchResultEntitiy.fullAddress)
        }
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(positionLatLng, CAMERA_ZOOM_LEVEL))

        return map.addMarker(markerOptions)
    }


}