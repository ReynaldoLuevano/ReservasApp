package com.home.reservas;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.List;

import com.home.reservas.adapter.ItemReservaAdapter;
import com.home.reservas.api.ICloudReservas;
import com.home.reservas.model.Reserva;
import com.home.reservas.client.HttpClientReservas;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Retrofit;

public class ListaActivity extends AppCompatActivity{

    private ICloudReservas iCloudReservas;
    private ListView misReservasListView;
    private List<Reserva> misReservas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //boton flotante para dar de alta nuevas reservas
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_anadeReserva);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListaActivity.this, InsertarReservaActivity.class);
                startActivity(intent);
            }
        });
        //fin de boton flotante para dar de alta nuevas reservas

        initializeActivity();
    }

    private void initializeActivity()
    {
        iCloudReservas = HttpClientReservas.createClient(ICloudReservas.class);
        misReservasListView = (ListView) findViewById(R.id.listViewReservas);
        misReservasListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        //clikcs en la lista de Reservas
        misReservasListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ReservasData.getInstace().setReserva((Reserva) misReservasListView.getAdapter().getItem(position));
                Intent intent = new Intent(ListaActivity.this, DetalleReservaActivity.class);
                startActivity(intent);
            }
        });
        //clikcs en la lista de Reservas
        //TODO modificar el 1 por el ID del usuario que se autentica
        Reserva r = (Reserva) getIntent().getExtras().getSerializable("reserva");
        loadDataFromReservasCloudService(r.getId_persona());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lista, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()){
            case R.id.action_settings:
                Log.i("Action Bar", "Settings Menu");
                return  true;
            case R.id.deleteIconBar:
                for(int i = 0; i < misReservas.size() ; i++){
                    if(misReservas.get(i).isChecked()){
                        deleteDataFromReservasCloudService(misReservas.get(i).getNumero(), misReservas.get(i).getId_persona());
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    private void loadDataFromReservasCloudService(String usuario)
    {
        Call<List<Reserva>> callReservasList = iCloudReservas.getMisReservas(usuario);
        callReservasList.enqueue(new Callback<List<Reserva>>() {

            @Override
            public void onResponse(retrofit.Response<List<Reserva>> response, Retrofit retrofit) {
                Log.d("ListActivity", "Status Code = " + response.code());
                if(response.isSuccess()){
                    misReservas = response.body();
                    if(misReservas != null){
                        laodMisReservasIU(misReservas);
                        //barra de progreso desaparece al cargar la lista
                        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
                        progressBar.setVisibility(View.INVISIBLE);

                    }else{
                        System.out.print("error");
                    }
                }else{
                    System.out.print("error");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("ListActivity", " Exception: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }

    private void deleteDataFromReservasCloudService(int numeroReserva, final String usuario)
    {
        Call<Boolean> callReservasList = iCloudReservas.deleteReserva(numeroReserva);
        callReservasList.enqueue(new Callback<Boolean>() {

            @Override
            public void onResponse(retrofit.Response<Boolean> response, Retrofit retrofit) {
                Log.d("ListActivity", "Status Code = " + response.code());
                if(response.isSuccess()){
                    Boolean isDeleted = response.body();
                    if(isDeleted != null){
                        loadDataFromReservasCloudService(usuario);
                        //barra de progreso desaparece al cargar la lista
                        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
                        progressBar.setVisibility(View.INVISIBLE);

                    }else{
                        System.out.print("error");
                    }
                }else{
                    System.out.print("error");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("ListActivity", " Exception: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }

    private void laodMisReservasIU(List<Reserva> reservas){

        try {
            if(reservas.size() > 0){
                ItemReservaAdapter adapter = new ItemReservaAdapter (this, reservas);
                misReservasListView.setAdapter(adapter);
                for (Reserva r: reservas) {
                    Log.i("Reserva", "w = " + r.toString());
                }
            }
        } catch (Exception e) {
            Log.e("Reserva", "Exception: " + e.getMessage());
        }
    }
}
