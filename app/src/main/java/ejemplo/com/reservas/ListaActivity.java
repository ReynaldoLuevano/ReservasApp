package ejemplo.com.reservas;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.SimpleCursorAdapter;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

import ejemplo.com.reservas.api.ICloudReservas;
import ejemplo.com.reservas.bean.Reserva;
import ejemplo.com.reservas.client.HttpClientReservas;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Retrofit;

public class ListaActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    SimpleCursorAdapter myAdapter;
    private ICloudReservas iCloudReservas;
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    private List<Reserva> misReservas;
    static final String[] PROJECTION = new String[] {"Reserva1","Reserva2", "Reserva3","Reserva4","Reserva5", "Reserva6","Reserva7","Reserva8", "Reserva9",};

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



        //Creo barra de progreso mientras la lista carga
        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER));

        progressBar.setIndeterminate(true);
        ListView lista = (ListView) findViewById(R.id.listView);
        lista.setEmptyView(progressBar);

        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        root.addView(progressBar);

        initializeActivity();
    }

    private void initializeActivity()
    {

        iCloudReservas = HttpClientReservas.createClient(ICloudReservas.class);
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, ContactsContract.Data.CONTENT_URI,
                PROJECTION,"SELECTION" , null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void loadDataFromReservasCloudService(String usuario)
    {
       Call<List<Reserva>> callReservasList = iCloudReservas.getMisReservas(usuario);
        callReservasList.enqueue(new Callback() {

            @Override
            public void onResponse(retrofit.Response response, Retrofit retrofit) {
                Log.d("ListActivity", "Status Code = " + response.code());
                if(response.isSuccess()){
                    Gson gson = new Gson();
                    String json = response.body().toString();


                    response.body();
                    if(misReservas != null){
                        Log.d("ListActivity", "wList = " + misReservas.toString());

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
}
