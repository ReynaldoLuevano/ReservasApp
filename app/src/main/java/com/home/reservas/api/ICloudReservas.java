package com.home.reservas.api;

import com.home.reservas.model.Reserva;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;

public interface ICloudReservas {

	    @POST("insertReserva")
	    Call<Reserva> insertReserva(@Body Reserva reserva);
}
