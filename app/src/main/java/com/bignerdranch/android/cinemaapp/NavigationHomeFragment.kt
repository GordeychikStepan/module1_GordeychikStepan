package com.bignerdranch.android.cinemaapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class NavigationHomeFragment : Fragment() {

    private lateinit var apiService: ApiService
    private lateinit var eventsRecyclerView: RecyclerView
    private lateinit var eventsAdapter: EventsAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private val sharedPrefFile = "com.example.sharedPrefFile"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_navigation_home, container, false)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://virtserver.swaggerhub.com/focus.lvlup2021/LEVEL_UP_MOBILE/1.0.0/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)


        // Инициализация RecyclerView
        eventsRecyclerView = view.findViewById(R.id.eventsRecyclerView)
        eventsAdapter = EventsAdapter(listOf()) { event ->
            // Обработка нажатия на элемент (переход на Chat Screen)
        }
        eventsRecyclerView.adapter = eventsAdapter
        eventsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        eventsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                updateTitleBasedOnScrollPosition()
            }
        })

        sharedPreferences = requireActivity().getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("accessToken", null)

        loadEvents(accessToken)

        return view
    }

    private fun loadEvents(userId: String?) {
        if (userId != null) {
            apiService.getEvents(userId, true, true, true).enqueue(object : Callback<EventsResponse> {
                override fun onResponse(call: Call<EventsResponse>, response: Response<EventsResponse>) {
                    if (response.isSuccessful) {
                        // Обновите ваш RecyclerView адаптер здесь
                        eventsAdapter.updateEvents(response.body()?.events ?: emptyList())
                    } else {
                        showToast("Ошибка: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<EventsResponse>, t: Throwable) {
                    handleApiFailure(t)
                }
            })
        } else {
            showToast("Пустой Id")
        }
    }

    private fun handleApiFailure(t: Throwable) {
        if (t is IOException) {
            showToast("Отсутствует подключение к интернету")
        } else {
            showToast("Не удалось выполнить запрос: ${t.localizedMessage}")
        }
        Log.e("PremieresResponse", "Failed to execute the request", t)
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


    private fun updateTitleBasedOnScrollPosition() {
        val layoutManager = eventsRecyclerView.layoutManager as LinearLayoutManager
        val firstVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition()
        if (firstVisibleItemPosition != RecyclerView.NO_POSITION) {
            // Получаем активный элемент и обновляем titleTextView
            val activeEvent = eventsAdapter.getItem(firstVisibleItemPosition)
            view?.findViewById<TextView>(R.id.titleTextView)?.text = activeEvent.name
        }
    }
}

