package com.example.weathernow

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import com.example.weathernow.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

//3c3a21f84f0364b4ff761aa50dd67620
class MainActivity : AppCompatActivity() {
    private val binding:ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        fetchWeatherData("Jaipur")//by default it shows the weather of jaipur
        searchCity()
    }

    private fun searchCity(){
       val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query!=null) {
                    fetchWeatherData(query)
                }
                    return true

            }

            override fun onQueryTextChange(p0: String?): Boolean {
              return true
            }

        })

    }

    private fun fetchWeatherData(cityName : String) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)
        val response = retrofit.getWeatherData(cityName , "3c3a21f84f0364b4ff761aa50dd67620" , "metric" )
         response.enqueue(object : Callback<WeatherNow> {
             override fun onResponse(call: Call<WeatherNow>, response: Response<WeatherNow>) {
                 val responseBody= response.body()
                 if(response.isSuccessful && responseBody!=null){
                     val temperature =responseBody.main.temp.toString()
                     val humidity = responseBody.main.humidity
                     val windSpeed= responseBody.wind.speed
                     val sunRise= responseBody.sys.sunrise.toLong()
                     val sunSet=responseBody.sys.sunset.toLong()
                     val seaLevel=responseBody.main.pressure
                     val condition=responseBody.weather.firstOrNull()?.main?:"unknown"
//                     val maxTemp=responseBody.main.temp_max
//                     val minTemp=responseBody.main.temp_min

                     binding.temp.text="$temperature  °C"
                     binding.textView6.text=condition
//                     binding.maxTemp.text="Max Temp: $maxTemp °C"
//                     binding.minTemp.text="Min Temp: $minTemp °C"
                     binding.humidity.text="$humidity %"
                     binding.windSpeed.text="$windSpeed m/s"
                     binding.sunRise.text="${time(sunRise)}"
                     binding.sunset.text="${time(sunSet)}"
                     binding.sea.text="$seaLevel hPa"
                     binding.condition.text=condition
                     binding.day.text=dayName(System.currentTimeMillis())
                     binding.date.text=date()
                     binding.cityName.text="$cityName"

                    // Log.d("TAG" , "onResponse: $temperature")
                     changeImageAccordingToWeatherCondition(condition)
                 }
             }

             override fun onFailure(call: Call<WeatherNow>, t: Throwable) {
                 TODO("Not yet implemented")
             }

         })


    }

    private fun changeImageAccordingToWeatherCondition(conditions : String) {
        when(conditions){
            "Clear Sky","Clear"->{
                binding.root.setBackgroundResource(R.drawable.clear_sky)


            }
            "Sunny"->{
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)

            }
            "Partly Clouds","Clouds"->{
                binding.root.setBackgroundResource(R.drawable.cloud2)
                binding.lottieAnimationView.setAnimation(R.raw.cloud)

            }
            "Overcast","Mist","Foggy","Haze"-> {
                binding.root.setBackgroundResource(R.drawable.colud_background)
                binding.lottieAnimationView.setAnimation(R.raw.cloud)
            }
            "Heavy Rain","Light Rain","Drizzle","Moderate Rain","showers","Rain"->{
                binding.root.setBackgroundResource(R.drawable.green_rain)
                binding.lottieAnimationView.setAnimation(R.raw.rain)

            }
            "Light Snow","Moderate Snow","Heavy Snow","Blizzard"->{
                binding.root.setBackgroundResource(R.drawable.snow_background)
                binding.lottieAnimationView.setAnimation(R.raw.snow)

            }
            else->{
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }
        }
        binding.lottieAnimationView.playAnimation()
    }

    private fun date():String{
        val sdf=SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format((Date()))
    }
    private fun time(timestamp: Long):String{
        val sdf=SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format((Date(timestamp*1000)))
    }
    fun dayName(timestamp : Long):String{
        val sdf=SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format((Date()))
    }
}