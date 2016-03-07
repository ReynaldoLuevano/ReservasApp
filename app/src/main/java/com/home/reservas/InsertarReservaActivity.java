package com.home.reservas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.home.reservas.api.ICloudReservas;
import com.home.reservas.client.HttpClientReservas;
import com.home.reservas.model.Reserva;

import java.sql.Date;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by root on 24/02/16.
 */
public class InsertarReservaActivity extends Activity{

    private EditText editTextReservaId;
    private EditText editTextReservaNombre;
    private EditText editTextReservaFInicio;
    private EditText editTextReservaFFin;
    private EditText editTextReservaHora;
    private EditText editTextReservaLugar;
    private EditText editTextReservaEstado;

    private Button btnReservaInsert;
    private Button btnReservaCancel;
    private Button btnReservaClean;

    private ICloudReservas iCloudReservas;
    private Reserva uReserva;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservas_insert_layout);

        initializeActivity();
    }

    private void initializeActivity(){

        iCloudReservas  = HttpClientReservas.createClient(ICloudReservas.class);

        editTextReservaId = (EditText) findViewById(R.id.editTextReservaId);
        editTextReservaNombre = (EditText) findViewById(R.id.editTextReservaNombre);
        editTextReservaFInicio = (EditText) findViewById(R.id.editTextFInicio);
        editTextReservaFFin = (EditText) findViewById(R.id.editTextFFin);
        editTextReservaHora = (EditText) findViewById(R.id.editTextHora);
        editTextReservaLugar = (EditText) findViewById(R.id.editTextLugar);
        editTextReservaEstado = (EditText) findViewById(R.id.editTextEstado);

        btnReservaCancel = (Button) findViewById(R.id.buttonReservaCancel);
        btnReservaInsert = (Button) findViewById(R.id.buttonReservaInsert);
        btnReservaClean = (Button) findViewById(R.id.buttonReservaClean);

        btnReservaCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpCancel(v);
            }
        });

        btnReservaInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reserva reserva = readReservaEditText();
                insertReservaCloudService(iCloudReservas, reserva);
            }
        });

        btnReservaClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanReservaEditText();
            }
        });
    }

    private void insertReservaCloudService(ICloudReservas cloudReservas, Reserva uDataReserva){

        Call<Reserva> callReserva = iCloudReservas.insertReserva(uDataReserva);
        callReserva.enqueue(new Callback<Reserva>() {

            @Override
            public void onFailure(Throwable t) {
                Log.e("insertReservaCloud", " Exception: " + t.getMessage());
                t.printStackTrace();
                popUpAppError();
            }

            @Override
            public void onResponse(Response<Reserva> response, Retrofit retrofit) {
                Log.d("insertReservaCloud", "Status Code = " + response.code());
                if(response.isSuccess()){
                    uReserva = response.body();
                    if(uReserva != null){
                        Log.d("insertReservaCloud", "reserva = " + uReserva.toString());
                        loadReservaEditText(uReserva);
                        popUpAppUpate();
                    }else{
                        popUpAppError();
                    }
                }else{
                    popUpAppError();
                }
            }
        });
    }

    private Reserva readReservaEditText(){
        Reserva reserva = new Reserva();

        try {
            reserva.setNumero(0);
            reserva.setId_persona(editTextReservaNombre.getText().toString());
            reserva.setFecha_inicio(Date.valueOf(editTextReservaFInicio.getText().toString()));
            reserva.setFecha_fin(Date.valueOf(editTextReservaFFin.getText().toString()));
            reserva.setHoras(Integer.parseInt(editTextReservaHora.getText().toString()));
            reserva.setLugar(editTextReservaLugar.getText().toString());
            reserva.setEstado(Integer.parseInt(editTextReservaEstado.getText().toString()));

        } catch (Exception e) {
            Log.e("readReservaEditText", "Exception: " + e.getMessage());
        }
        return reserva;
    }

    private void loadReservaEditText(Reserva reserva){
        try {
            editTextReservaId.setText(reserva.getNumero());
            editTextReservaNombre.setText(reserva.getId_persona());
            editTextReservaFInicio.setText(reserva.getFecha_inicio().toString());
            editTextReservaFFin.setText(reserva.getFecha_fin().toString());
            editTextReservaHora.setText(reserva.getHoras());
            editTextReservaLugar.setText(reserva.getLugar());
            editTextReservaEstado.setText(reserva.getEstado());

        } catch (Exception e) {
            Log.e("loadCheckInEditText", "Exception: " + e.getMessage());
        }
    }

    private void cleanReservaEditText(){

        try {
            editTextReservaId.setText("");
            editTextReservaNombre.setText("");
            editTextReservaFInicio.setText("");
            editTextReservaFFin.setText("");
            editTextReservaHora.setText("");
            editTextReservaLugar.setText("");
            editTextReservaEstado.setText("");

        } catch (Exception e) {
            Log.e("cleanReservaEditText", "Exception: " + e.getMessage());
        }
    }

    private void popUpCancel(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.question_cancel).setTitle(R.string.alert_message)
                .setCancelable(false).setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        }).setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
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
        builder.setMessage(R.string.app_insert_message).setTitle(R.string.info_message)
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