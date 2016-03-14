package com.home.reservas;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import com.home.reservas.api.ICloudReservas;
import com.home.reservas.client.HttpClientReservas;
import com.home.reservas.model.Reserva;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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

    private DatePickerDialog fechaDialog;
    SimpleDateFormat dateFormat =  new SimpleDateFormat("dd-MM-yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservas_detalle_layout);
        initializeActivity();

    }


    private void initializeActivity()
    {

        reserva = ReservasData.getInstace().getReserva();
        iCloudReservas  = HttpClientReservas.createClient(ICloudReservas.class);

        //carga detalle de Reserva
        loadReservaDetail(reserva);

        //evento de botón para actualizar Reserva
        Button btnReservaUpdate = (Button) findViewById(R.id.buttonReservaGuardar);
        btnReservaUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gatherReservaDetail();
                updateReservaCloudService(iCloudReservas, reserva);

            }
        });
        //evento de calendarios
        final EditText fechaInicioReserva =  (EditText) findViewById(R.id.editTextFInicio);
        fechaInicioReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker(fechaInicioReserva);
            }
        });
        //evento de calendarios
        final EditText fechaFinReserva =  (EditText) findViewById(R.id.editTextFFin);
        fechaInicioReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker(fechaFinReserva);
            }
        });


    }


    /*método para cargar el detalle de la reserva en IU*/
    private void loadReservaDetail(Reserva reserva)
    {

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

    /*método para llamar al servicio Rest de actualizar la reserva*/
    private void updateReservaCloudService(ICloudReservas cloudReservas, Reserva uDataReserva){

        Call<Boolean> callReserva = iCloudReservas.updateReserva(uDataReserva);
        callReserva.enqueue(new Callback<Boolean>() {

            @Override
            public void onFailure(Throwable t) {
                popUpAppError();
            }

            @Override
            public void onResponse(Response<Boolean> response, Retrofit retrofit) {
                if (response.body().booleanValue()==true) {
                    popUpAppUpate();
                } else {
                    popUpAppError();
                }
            }
        });
    }


    /*método para verificar los cambios realizados y enviarlos al objeto reserva*/
    private void gatherReservaDetail()
    {
        EditText numeroReserva =  (EditText) findViewById(R.id.editTextReservaId);
        EditText fechaInicioReserva =  (EditText) findViewById(R.id.editTextFInicio);
        EditText fechaFinReserva =  (EditText) findViewById(R.id.editTextFFin);
        EditText horasReserva =  (EditText) findViewById(R.id.editTextHora);
        EditText lugarReserva =  (EditText) findViewById(R.id.editTextLugar);
        EditText estadoReserva =  (EditText) findViewById(R.id.editTextEstado);

        reserva.setNumero(Integer.valueOf(numeroReserva.getText().toString()));
        reserva.setFecha_inicio(Date.valueOf(fechaInicioReserva.getText().toString()));
        reserva.setFecha_fin(Date.valueOf(fechaFinReserva.getText().toString()));
        reserva.setHoras(Integer.valueOf(horasReserva.getText().toString()));
        reserva.setEstado(Integer.valueOf(estadoReserva.getText().toString()));
        reserva.setLugar(lugarReserva.getText().toString());

    }

    /*mostrar la fecha*/
    private void datePicker(final EditText fechaEditText)   {

        Calendar newCalendar = Calendar.getInstance();
        fechaDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Date fechaDate = new Date(year,monthOfYear,dayOfMonth);
                fechaEditText.setText(dateFormat.format(fechaDate));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
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
}
