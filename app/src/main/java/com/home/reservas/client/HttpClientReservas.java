package com.home.reservas.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.home.reservas.ReservasData;
import com.squareup.okhttp.OkHttpClient;

import java.text.DateFormat;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Reynaldo on 17/02/2016.
 */
public class HttpClientReservas {

	public static final String CLOUD_IMPATIENT_URL = "http://192.168.1.34:8080/reservas/";

	private static OkHttpClient httpClient = new OkHttpClient();
	private static Retrofit.Builder builder = new Retrofit.Builder();

	public static <S> S createClient(Class<S> serviceClass) {

		Gson gson = new GsonBuilder()
				.setDateFormat(ReservasData.dateFormat)
				.create();

		builder.baseUrl(CLOUD_IMPATIENT_URL);
		builder.addConverterFactory(GsonConverterFactory.create(gson));
		Retrofit retrofit = builder.client(httpClient).build();


		return retrofit.create(serviceClass);
	}
}
