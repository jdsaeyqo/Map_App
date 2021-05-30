package com.example.mapapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mapapp.databinding.ActivityMainBinding
import com.example.mapapp.model.LocationLatLngEntity
import com.example.mapapp.model.SearchResultEntitiy
import com.example.mapapp.model.search.Poi
import com.example.mapapp.model.search.Pois
import com.example.mapapp.utility.RetrofitUtil
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var job : Job

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var binding : ActivityMainBinding
    private lateinit var adapter : SearchRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        job = Job()

        initAdapter()
        initViews()
        bindViews()
        initData()

    }



    private fun initAdapter() {
        adapter = SearchRecyclerAdapter()

    }

    private fun initViews() = with(binding){
        searchNothingTextView.isVisible = false
        mainRecyclerView.adapter = adapter
    }

    private fun bindViews() = with(binding) {
        mainSearchButton.setOnClickListener {
            searchKeyWord(mainEditTextView.text.toString())
           
        }
    }

    private fun searchKeyWord(keyWordString: String) {

        launch(coroutineContext) {
            try {
                withContext(Dispatchers.IO){
                    val response = RetrofitUtil.apiService.getSearchLocation(
                        keyword = keyWordString
                    )
                    if(response.isSuccessful){
                        val body = response.body()
                        withContext(Dispatchers.Main){
                            Log.e("response",body.toString())
                            body?.let {searchResponse ->
                                setData(searchResponse.searchPoiInfo.pois)
                            }
                        }
                    }
                }
            }catch (e : Exception){

            }
        }
    }

    private fun initData(){
        adapter.notifyDataSetChanged()
    }

    private fun setData(pois : Pois){
        val dataList = pois.poi.map {
            SearchResultEntitiy(
                name = it.name?:"빌딩명 없음",
                fullAddress = makeMainAddress(it),
                locationLatLng = LocationLatLngEntity(
                    it.noorLat,
                    it.noorLon
                )

            )
        }
        adapter.setSearchResultList(dataList){

            Toast.makeText(this,"빌딩 이름 : ${it.name}, 주소 : ${it.fullAddress}, 위도/경도 : ${it.locationLatLng}",Toast.LENGTH_SHORT).show()
        }

    }

    private fun makeMainAddress(poi: Poi): String =
        if (poi.secondNo?.trim().isNullOrEmpty()) {
            (poi.upperAddrName?.trim() ?: "") + " " +
                    (poi.middleAddrName?.trim() ?: "") + " " +
                    (poi.lowerAddrName?.trim() ?: "") + " " +
                    (poi.detailAddrName?.trim() ?: "") + " " +
                    poi.firstNo?.trim()
        } else {
            (poi.upperAddrName?.trim() ?: "") + " " +
                    (poi.middleAddrName?.trim() ?: "") + " " +
                    (poi.lowerAddrName?.trim() ?: "") + " " +
                    (poi.detailAddrName?.trim() ?: "") + " " +
                    (poi.firstNo?.trim() ?: "") + " " +
                    poi.secondNo?.trim()
        }
}