package com.example.androidtaskmayank.utils


import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities


interface NetworkConnectivity {
    fun isNetworkConnected(): Boolean
}

class NetworkUtil(private val context: Context) : NetworkConnectivity {

    override fun isNetworkConnected(): Boolean {
        val connectivityManager =
            context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val netCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            netCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            netCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }
}


