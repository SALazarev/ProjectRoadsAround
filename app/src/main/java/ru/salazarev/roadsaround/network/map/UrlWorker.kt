package ru.salazarev.roadsaround.network.map

import android.graphics.Color
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * Класс преобразования URL-маршрута в объект маршрута GoogleMapApi.
 */
class UrlWorker {

    /**
     * Преобразование URL-маршрута в объект маршрута GoogleMapApi.
     * @param urlStr - URL-маршрута.
     * @return объект маршрута GoogleMapApi.
     */
    fun getRoad(urlStr: String): PolylineOptions = creatorLine(downloadUrl(urlStr))

    @Throws(IOException::class)
    private fun downloadUrl(strUrl: String): String {
        val url = URL(strUrl)
        val urlConnection = url.openConnection() as HttpURLConnection
        urlConnection.connect()
        val iStream = urlConnection.inputStream
        val br = BufferedReader(InputStreamReader(iStream))
        val sb = StringBuffer()
        var line: String?
        while (br.readLine().also { line = it } != null) {
            sb.append(line)
        }
        val data = sb.toString()
        br.close()

        iStream?.close()
        urlConnection.disconnect()
        return data
    }

    private fun creatorLine(jsonData: String): PolylineOptions {
        val jObject = JSONObject(jsonData)
        val parser = RouteParser()

        val routes: List<List<HashMap<String, String>>> = parser.parse(jObject)


        var points: ArrayList<LatLng?>
        var lineOptions: PolylineOptions? = null

        for (route in routes) {
            points = ArrayList()
            lineOptions = PolylineOptions()

            for (point in route) {
                val lat = point["lat"]!!.toDouble()
                val lng = point["lng"]!!.toDouble()
                val position = LatLng(lat, lng)
                points.add(position)
            }

            lineOptions.apply {
                addAll(points)
                width(20f)
                color(Color.RED)
            }
        }
        return lineOptions!!
    }
}