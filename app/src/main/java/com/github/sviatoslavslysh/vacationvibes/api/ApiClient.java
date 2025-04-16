package com.github.sviatoslavslysh.vacationvibes.api;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.net.CronetProviderInstaller;
import com.google.net.cronet.okhttptransport.CronetInterceptor;

import org.chromium.net.CronetEngine;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Connection;
import okhttp3.ConnectionPool;
import okhttp3.EventListener;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public class ApiClient {
    private static final String BASE_URL = "https://nescol.uk";
//    private static final String BASE_URL = "http://172.20.10.3:080";
    private static Retrofit retrofit;
    private static String authToken;

    public static Retrofit getRetrofitInstance(Context context) {
        if (retrofit == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY); // Логирование всех запросов и ответов

            CronetProviderInstaller.installProvider(context);
            CronetEngine engine = new CronetEngine.Builder(context).build();

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectionPool(new ConnectionPool(5, 5, TimeUnit.MINUTES))
                    .protocols(List.of(Protocol.QUIC, Protocol.HTTP_2, Protocol.HTTP_1_1))
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(new Interceptor() {
                        @NonNull
                        @Override
                        public Response intercept(@NonNull Chain chain) throws IOException {
                            Request originalRequest = chain.request();
                            Request.Builder requestBuilder = originalRequest.newBuilder();

                            if (authToken != null) {
                                requestBuilder.addHeader("Authorization", "Bearer " + authToken);
                            }

//                            Request modifiedRequest = requestBuilder.build();
//                            return chain.proceed(modifiedRequest);
                            Request modifiedRequest = requestBuilder.build();
                            Response response = chain.proceed(modifiedRequest);
                            Log.d("HttpProtocol", "Protocol used: " + response.protocol());

                            long endTime = System.nanoTime();
                            Log.d("Raw response", "Raw response receive time " + String.valueOf(endTime));

                            return response;
                        }
                    })
                    .addInterceptor(CronetInterceptor.newBuilder(engine).build())  // cronnet (http3 + quic)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
//                    .client(okHttpClient)
                    .callFactory((Call.Factory) okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static void setAuthToken(String token) {
        authToken = token;
    }

    public static <T> T createService(Class<T> serviceClass, Context context) {
        return getRetrofitInstance(context).create(serviceClass);
    }
}
