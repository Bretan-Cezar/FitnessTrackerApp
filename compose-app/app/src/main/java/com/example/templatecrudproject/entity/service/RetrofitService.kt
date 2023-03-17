package com.example.templatecrudproject.entity.service

import android.util.Log
import com.example.templatecrudproject.entity.domain.Entity
import com.example.templatecrudproject.entity.dto.EntityDTO
import com.example.templatecrudproject.entity.repository.EntityRepositoryNetwork
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import org.java_websocket.client.WebSocketClient
import org.java_websocket.drafts.Draft_6455
import org.java_websocket.handshake.ServerHandshake
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.InterruptedIOException
import java.net.SocketTimeoutException
import java.net.URI
import java.util.concurrent.TimeUnit

interface RetrofitService {

    // TODO fill in the dots
    @GET("dates")
    fun getDates(): Call<List<String>>

    @GET("entries/{date}")
    fun getItemsByDates(@Path("date") date: String): Call<List<EntityDTO>>

    @POST("entry")
    fun addItem(@Body dto: EntityDTO): Call<EntityDTO>

    @DELETE("entry/{id}")
    fun deleteItem(@Path("id") id: Long): Call<Unit>

    @GET("all")
    fun getAll(): Call<List<EntityDTO>>


    companion object {

        var webSocket: WebSocketClient? = null
        var retrofitService: RetrofitService? = null

        // TODO replace with server IP
        private const val baseURLString = "192.168.149.49:2305/"
        private const val serverTimeoutInterval: Long = 3

        fun getInstance(repo: EntityRepositoryNetwork, scope: CoroutineScope): RetrofitService? {

            val http = OkHttpClient.Builder()
                .connectTimeout(serverTimeoutInterval, TimeUnit.SECONDS)
                .callTimeout(serverTimeoutInterval, TimeUnit.SECONDS)
                .readTimeout(serverTimeoutInterval, TimeUnit.SECONDS)
                .writeTimeout(serverTimeoutInterval, TimeUnit.SECONDS)
                .build()


            val retrofit = Retrofit.Builder()
                .baseUrl("http://$baseURLString")
                .client(http)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            retrofitService = retrofit.create(RetrofitService::class.java)

            try {

                Log.d("NETWORK", "Opening socket via ws://$baseURLString...")

                val uri = URI("ws://$baseURLString")

                webSocket = object : WebSocketClient(uri, Draft_6455(), null, 1000) {

                    override fun onOpen(handshakedata: ServerHandshake?) {

                        Log.i("WS", "Successfully opened WebSocket connection.")
                    }

                    override fun onMessage(message: String?) {

                        Log.i("WS", "Received message from WebSocket: $message")

                        scope.launch(Dispatchers.IO) {

                            if (message != null) {

                                val dto = Json.decodeFromString<EntityDTO>(message)

                                if (repo.findById(dto.id) == null) {

                                    repo.saveLocally(Entity.fromDTO(dto))
                                } else {

                                    Log.i(
                                        "WS",
                                        "Entity received through WS message already present; no op performed."
                                    )
                                }
                            }
                        }
                    }

                    override fun onClose(code: Int, reason: String?, remote: Boolean) {

                        Log.i("WS", "WebSocket connection closed.")
                    }

                    override fun onError(ex: Exception?) {

                        Log.e("WS", "WebSocket connection error: $ex")
                    }
                }

                if(!(webSocket as WebSocketClient).connectBlocking()) {
                    retrofitService = null
                }
            }
            catch (e: SocketTimeoutException) {
                retrofitService = null
            }
            catch (e: InterruptedIOException) {
                retrofitService = null
            }
            catch (e: InterruptedException) {
                retrofitService = null
            }

            return retrofitService
        }
    }
}