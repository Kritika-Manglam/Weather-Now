package com.example.weathernow

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.weathernow.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//3c3a21f84f0364b4ff761aa50dd67620
class MainActivity : AppCompatActivity() {
    private val binding:ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        fetchWeatherData()
    }

    private fun fetchWeatherData() {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)
        val response = retrofit.getWeatherData("jaipur" , "3c3a21f84f0364b4ff761aa50dd67620" , "metric" )
         response.enqueue(object : Callback<WeatherNow> {
             override fun onResponse(call: Call<WeatherNow>, response: Response<WeatherNow>) {
                 val responseBody= response.body()
                 if(response.isSuccessful && responseBody!=null){
                     val temperature =responseBody.main.temp.toString()
                     binding.temp.text="$temperature  °C"
                    // Log.d("TAG" , "onResponse: $temperature")
                 }
             }

             override fun onFailure(call: Call<WeatherNow>, t: Throwable) {
                 TODO("Not yet implemented")
             }

         })

    }
}