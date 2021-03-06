package com.home.reservas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.home.reservas.api.ICloudReservas;
import com.home.reservas.client.HttpClientReservas;
import com.home.reservas.fragment.DatePickerFragment;
import com.home.reservas.model.Reserva;

import java.sql.Date;
import java.text.SimpleDateFormat;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by root on 24/02/16.
 */
public class InsertarReservaActivity extends Activity implements DatePickerFragment.onDateSelectedListener{

    private TextView editTextReservaFInicio;
    private TextView editTextReservaFFin;
    private EditText editTextReservaHora;
    private EditText editTextReservaLugar;
    private EditText editTextReservaEstado;

    private Button btnReservaInsert;
    private Button btnReservaCancel;
    private Button btnReservaClean;

    private ICloudReservas iCloudReservas;
    private Reserva uReserva;
    SimpleDateFormat dateFormat =  new SimpleDateFormat(ReservasData.dateFormat);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservas_insert_layout);

        initializeActivity();
    }

    private void initializeActivity(){

        final Bundle bundle = new Bundle(1);
        iCloudReservas  = HttpClientReservas.createClient(ICloudReservas.class);

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

        //evento de calendarios
        editTextReservaFInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putInt("view",R.id.editTextFInicio);
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.setArguments(bundle);
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });
        //evento de calendarios
        editTextReservaFFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putInt("view",R.id.editTextFFin);
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.setArguments(bundle);
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });
    }

    private void insertReservaCloudService(ICloudReservas cloudReservas, Reserva uDataReserva){

        Call<Boolean> callReserva = iCloudReservas.insertReserva(uDataReserva);
        callReserva.enqueue(new Callback<Boolean>() {

            @Override
            public void onFailure(Throwable t) {
                Log.e("insertReservaCloud", " Exception: " + t.getMessage());
                t.printStackTrace();
                popUpAppError();
            }

            @Override
            public void onResponse(Response<Boolean> response, Retrofit retrofit) {
                Log.d("insertReservaCloud", "Status Code = " + response.code());
                if (response.isSuccess()) {

                    if (response.body().booleanValue() == true) {
                        popUpAppUpate();
                    } else {
                        popUpAppError();
                    }
                } else {
                    popUpAppError();
                }
            }
        });
    }

    private Reserva readReservaEditText(){
        Reserva reserva = new Reserva();

        try {
            //TODO modificar el ID de persona cuando se conecte con Google o Linkedin
            reserva.setId_persona("1");
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
            editTextReservaFInicio.setText("");
            editTextReservaFFin.setText("");
            editTextReservaHora.setText("");
            editTextReservaLugar.setText("");
            editTextReservaEstado.setText("");

        } catch (Exception e) {
            Log.e("cleanReservaEditText", "Exception: " + e.getMessage());
        }
    }

    /*método para cargar la fecha seleccionada en el DatePicker */
    public void onDateSelected(java.util.Date fechaSeleccionada, int viewId)
    {
        EditText fechaEditText =  (EditText) findViewById(viewId);
        fechaEditText.setText(dateFormat.format(fechaSeleccionada));
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
                finish();
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