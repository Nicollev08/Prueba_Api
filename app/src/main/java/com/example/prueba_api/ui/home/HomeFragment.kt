package com.example.prueba_api.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.prueba_api.ApiService
import com.example.prueba_api.Tip
import com.example.prueba_api.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome

        // Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.18.5:8000/api/")  // Cambia la URL según tu configuración
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.getTips()

        call.enqueue(object : Callback<List<Tip>> {
            override fun onResponse(call: Call<List<Tip>>, response: Response<List<Tip>>) {
                if (response.isSuccessful) {
                    val tips = response.body()
                    if (tips != null && tips.isNotEmpty()) {
                        val firstTip = tips[0]
                        textView.text = "ID: ${firstTip.id}\nName: ${firstTip.name}\nDescription: ${firstTip.description}\nImage: ${firstTip.image}"
                    } else {
                        textView.text = "No tips available."
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("Retrofit", "Failed to fetch tips. HTTP Code: ${response.code()}, Error body: $errorBody")
                    textView.text = "Failed to fetch tips."
                }
            }

            override fun onFailure(call: Call<List<Tip>>, t: Throwable) {
                Log.e("Retrofit", "Error: ${t.message}", t)
                textView.text = "Error: ${t.message}"
            }

        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
