package com.home.reservas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.home.reservas.api.ICloudReservas;
import com.home.reservas.client.HttpClientReservas;
import com.home.reservas.model.Reserva;

import java.text.SimpleDateFormat;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Reynaldo on 04/03/2016.
 */
public class DetalleReservaActivity extends Activity {

    private Reserva reserva;
    private ICloudReservas iCloudReservas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservas_detalle_layout);
        initializeActivity();

    }

    private void initializeActivity()
    {

        iCloudReservas  = HttpClientReservas.createClient(ICloudReservas.class);
        //carga detalle de Reserva
        loadReservaDetail(reserva);

        //evento de botón para actualizar Reserva
        Button btnReservaUpdate = (Button) findViewById(R.id.buttonReservaGuardar);
        btnReservaUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateReservaCloudService(iCloudReservas, reserva);

            }
        });
    }


    private void loadReservaDetail(Reserva reserva)
    {
        SimpleDateFormat dateFormat =  new SimpleDateFormat("MM-dd-yyyy");
        reserva = ReservasData.getInstace().getReserva();

        EditText numeroReserva =  (EditText) findViewById(R.id.editTextReservaId);
        EditText fechaInicioReserva =  (EditText) findViewById(R.id.editTextFInicio);
        EditText fechaFinReserva =  (EditText) findViewById(R.id.editTextFFin);
        EditText horasReserva =  (EditText) findViewById(R.id.editTextHora);
        EditText lugarReserva =  (EditText) findViewById(R.id.editTextLugar);
        EditText estadoReserva =  (EditText) findViewById(R.id.editTextEstado);


        numeroReserva.setText(String.valueOf(reserva.getNumero()));
        fechaInicioReserva.setText(dateFormat.format(reserva.getFecha_inicio()));
        fechaFinReserva.setText(dateFormat.format(reserva.getFecha_fin()));
        horasReserva.setText(String.valueOf(reserva.getHoras()));
        lugarReserva.setText(reserva.getLugar());
        estadoReserva.setText(String.valueOf(reserva.getEstado()));


    }

    private void updateReservaCloudService(ICloudReservas cloudReservas, Reserva uDataReserva){

        Call<Reserva> callReserva = iCloudReservas.updateReserva(uDataReserva);
        callReserva.enqueue(new Callback<Reserva>() {

            @Override
            public void onFailure(Throwable t) {
                popUpAppError();
            }

            @Override
            public void onResponse(Response<Reserva> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                        popUpAppUpate();
                } else {
                    popUpAppError();
                }
            }
        });
    }

    private void popUpAppUpate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.app_update_message).setTitle(R.string.info_message)
                .setCancelable(false).setNegativeButton(R.string.button_close, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void popUpAppError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.app_error_message).setTitle(R.string.alert_message)
                .setCancelable(false).setNegativeButton(R.string.button_close, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                setResult(RESULT_CANCELED, null);
                finish();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
