package edu.ucsd.cse110.cse110group8_compass.model;

import android.util.Log;

import androidx.annotation.AnyThread;
import androidx.annotation.WorkerThread;

import com.google.gson.Gson;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UUIDAPI {

    private volatile static UUIDAPI instance = null;
    public static final MediaType JSON
            = MediaType.parse("application/json");

    private OkHttpClient client;
    public String url = "https://socialcompass.goto.ucsd.edu/";

    public UUIDAPI() {
        this.client = new OkHttpClient();
    }

    public static UUIDAPI provide() {
        if (instance == null) {
            instance = new UUIDAPI();
        }
        return instance;
    }

    /**
     * An example of sending a GET request to the server.
     *
     * The /echo/{msg} endpoint always just returns {"message": msg}.
     *
     * This method should can be called on a background thread (Android
     * disallows network requests on the main thread).
     */
    @WorkerThread
    public String echo(String msg) {
        // URLs cannot contain spaces, so we replace them with %20.
        String encodedMsg = msg.replace(" ", "%20");

        var request = new Request.Builder()
                .url(url + "echo/" + encodedMsg)
                .method("GET", null)
                .build();

        try (var response = client.newCall(request).execute()) {
            assert response.body() != null;
            var body = response.body().string();
            Log.i("ECHO", body);
            return body;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public UUID get(String public_code){

        Log.i("get", "calling get");

        public_code = public_code.replace(" ", "%20");
        var request = new Request.Builder()
                .url("https://socialcompass.goto.ucsd.edu/location/" + public_code)
                .method("GET", null)
                .build();

        try (var response = client.newCall(request).execute()) {
            String jsonObject = response.body().string();
            UUID pin = UUID.fromJSON(jsonObject);
            Log.i("get", "note content " + pin.label);
            return pin;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String put(UUID pin){

        String title = pin.getPublic_code().replace(" ", "%20");
        String jsonNote = pin.toJSON();
        Gson gson = new Gson();
        RequestBody body = RequestBody.create(JSON, gson.toJson(Map.of(
                "private_code", pin.public_code,
                "label", pin.label,
                "latitude", pin.latitude,
                "longitude", pin.longitude
                )));
        Log.i("put", "requestbody "+body);
        Request request = new Request.Builder()
                .url(url + "location/" + pin.public_code)
                .put(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            Log.i("put", "success "+pin.public_code);
            // Log.i("put", gson.toJson(Map.of( "content", note.content, "version", note.version)));
            return response.body().string();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public Future<String> putAsync(UUID pin) {
        var executor = Executors.newSingleThreadExecutor();
        var future = executor.submit(() -> put(pin));

        // We can use future.get(1, SECONDS) to wait for the result.
        return future;
    }



    @AnyThread
    public Future<UUID> getAsync(String msg) {
        var executor = Executors.newSingleThreadExecutor();
        var future = executor.submit(() -> get(msg));

        // We can use future.get(1, SECONDS) to wait for the result.
        return future;
    }

    @AnyThread
    public Future<String> echoAsync(String msg) {
        var executor = Executors.newSingleThreadExecutor();
        var future = executor.submit(() -> echo(msg));

        // We can use future.get(1, SECONDS) to wait for the result.
        return future;
    }

}
