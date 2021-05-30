package com.example.mapapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchResultEntitiy(

    val fullAddress : String,
    val name : String,
    val locationLatLng : LocationLatLngEntity

    ) : Parcelable