package com.example.simpleyelp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG ="MainActivity"
private const val BASE_URL = "https://api.yelp.com/v3/"
private const val API_KEY="D3vH265obwXOslTt4CoP5T2ivWnASDMmqcqaMGTG8EoHXehmVG2szg3-UIkwgDr7Iwl-29-R_utsJhzgDrwIePjKgoMNw9vGjQbl7_hZGFOUb5aYfVJEa73f9a7FXnYx"
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val restaurants = mutableListOf<YelpRestaurant>()
        val adapter=RestaurantsAdapter(this, restaurants)
        rvRestaurants.adapter=adapter
        rvRestaurants.layoutManager= LinearLayoutManager(this)

        val retrofit =
            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .build()
        val yelpService=retrofit.create(YelpService::class.java)
        yelpService.searchRestaurant("Bearer $API_KEY","Biryani", "Sunnyvale").enqueue(object:Callback<YelpSearchResult> {
            override fun onResponse(call: Call<YelpSearchResult>, response: Response<YelpSearchResult>) {
                Log.i(TAG, "onResponse $response")
                val body = response.body()
                if (body==null){
                    Log.w(TAG, "Did not receive valid response from Yelp API..exiting")
                    return
                }
                restaurants.addAll(body.restaurants)
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<YelpSearchResult>, t: Throwable) {
                Log.i(TAG, "onFailure $t")
            }

        })
        //
    }
}
