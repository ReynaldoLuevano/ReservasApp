package ejemplo.com.reservas;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.List;

import ejemplo.com.reservas.adapter.ItemReservaAdapter;
import ejemplo.com.reservas.api.ICloudReservas;
import ejemplo.com.reservas.bean.Reserva;
import ejemplo.com.reservas.client.HttpClientReservas;
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_anadeReserva);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO conectar con la actividad de Alta
            }
        });

        initializeActivity();
    }

    private void initializeActivity()
    {
        misReservasListView = (ListView) findViewById(R.id.listViewReservas);
        iCloudReservas = HttpClientReservas.createClient(ICloudReservas.class);

        //TODO modificar el 1 por el ID del usuario que se autentica
        loadDataFromReservasCloudService("1");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lista, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
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
                        //Creo barra de progreso mientras la lista carga
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
