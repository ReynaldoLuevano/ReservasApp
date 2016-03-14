package com.home.reservas.api;

import com.home.reservas.model.Reserva;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

public interface ICloudReservas {

    @POST("insertReserva")
	Call<Boolean> insertReserva(@Body Reserva reserva);

	@GET("misreservas")
	Call<List<Reserva>> getMisReservas(@Query("usuario") String usuario);

	@POST("updateReserva")
	Call<Boolean> updateReserva(@Body Reserva reserva);

	@POST("deleteReserva")
	Call<Boolean> deleteReserva(@Body int reservaId);

}
