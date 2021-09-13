package com.salazarev.googlemapapiexample

import android.graphics.Color
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class FetchUrlRemake {

    fun getRoad(urlStr: String): PolylineOptions = creatorLine(downloadUrl(urlStr))


    @Throws(IOException::class)
    private fun downloadUrl(strUrl: String): String {

        var iStream: InputStream? = null
        var urlConnection: HttpURLConnection? = null

        val url = URL(strUrl)
        // Creating an http connection to communicate with url
        urlConnection = url.openConnection() as HttpURLConnection
        // Connecting to url
        urlConnection.connect()
        // Reading data from url
        iStream = urlConnection.inputStream
        val br = BufferedReader(InputStreamReader(iStream))
        val sb = StringBuffer()
        var line: String? = ""
        while (br.readLine().also { line = it } != null) {
            sb.append(line)
        }
        val data = sb.toString()
        br.close()

        iStream?.close()
        urlConnection.disconnect()
        return data
    }

    fun creatorLine(jsonData: String): PolylineOptions {
        var routes: List<List<HashMap<String, String>>>? = null
        val jObject = JSONObject(jsonData)
        val parser = DataParser()

        // Starts parsing data
        routes = parser.parse(jObject)

        var points: ArrayList<LatLng?>
        var lineOptions: PolylineOptions? = null
        // Traversing through all the routes
        for (route in routes) {
            points = ArrayList()
            lineOptions = PolylineOptions()
            // Fetching i-th route
            // Fetching all the points in i-th route
            for (point in route) {
                val lat = point["lat"]!!.toDouble()
                val lng = point["lng"]!!.toDouble()
                val position = LatLng(lat, lng)
                points.add(position)
            }
            // Adding all the points in the route to LineOptions
            lineOptions.apply{
                addAll(points)
                width(20f)
                color(Color.RED)
            }
        }

        return lineOptions!!
    }
}