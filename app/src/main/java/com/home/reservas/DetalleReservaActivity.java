package com.home.reservas;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.home.reservas.api.ICloudReservas;
import com.home.reservas.client.HttpClientReservas;
import com.home.reservas.fragment.DatePickerFragment;
import com.home.reservas.model.Reserva;

import org.w3c.dom.Text;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Reynaldo on 04/03/2016.
 */
public class DetalleReservaActivity extends Activity implements DatePickerFragment.onDateSelectedListener{

    private Reserva reserva;
    private ICloudReservas iCloudReservas;

    private DatePickerDialog fechaDialog;
    SimpleDateFormat dateFormat =  new SimpleDateFormat(ReservasData.dateFormat);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservas_detalle_layout);
        initializeActivity();
    }

    private void initializeActivity()
    {

        final Bundle bundle = new Bundle(1);
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
                bundle.putInt("view",R.id.editTextFInicio);
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.setArguments(bundle);
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });
        //evento de calendarios
        final EditText fechaFinReserva =  (EditText) findViewById(R.id.editTextFFin);
        fechaFinReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putInt("view",R.id.editTextFFin);
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.setArguments(bundle);
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });
    }


    /*método para cargar el detalle de la reserva en IU*/
    private void loadReservaDetail(Reserva reserva)
    {

        EditText numeroReserva =  (EditText) findViewById(R.id.editTextReservaId);
        TextView fechaInicioReserva =  (TextView) findViewById(R.id.editTextFInicio);
        TextView fechaFinReserva =  (TextView) findViewById(R.id.editTextFFin);
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
                if (response.body().booleanValue() == true) {
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
        TextView fechaInicioReserva =  (TextView) findViewById(R.id.editTextFInicio);
        TextView fechaFinReserva =  (TextView) findViewById(R.id.editTextFFin);
        EditText horasReserva =  (EditText) findViewById(R.id.editTextHora);
        EditText lugarReserva =  (EditText) findViewById(R.id.editTextLugar);
        EditText estadoReserva =  (EditText) findViewById(R.id.editTextEstado);


        reserva.setNumero(Integer.valueOf(numeroReserva.getText().toString()));
        reserva.setFecha_inicio(java.sql.Date.valueOf(fechaInicioReserva.getText().toString()));
        reserva.setFecha_fin(java.sql.Date.valueOf(fechaFinReserva.getText().toString()));
        reserva.setHoras(Integer.valueOf(horasReserva.getText().toString()));
        reserva.setEstado(Integer.valueOf(estadoReserva.getText().toString()));
        reserva.setLugar(lugarReserva.getText().toString());

    }

    /*método para cargar la fecha seleccionada en el DatePicker */
    public void onDateSelected(Date fechaSeleccionada, int viewId)
    {
        EditText fechaEditText =  (EditText) findViewById(viewId);
        fechaEditText.setText(dateFormat.format(fechaSeleccionada));
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
