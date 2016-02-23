package ejemplo.com.reservas.api;

import java.util.List;

import ejemplo.com.reservas.bean.Reserva;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Reynaldo on 17/02/2016.
 */
public interface ICloudReservas {


    @GET("misreservas")
    Call<List<Reserva>> getMisReservas(@Query("usuario") String usuario);

}
