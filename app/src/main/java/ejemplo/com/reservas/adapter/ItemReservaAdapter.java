package ejemplo.com.reservas.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import ejemplo.com.reservas.R;
import ejemplo.com.reservas.bean.Reserva;

/**
 * Created by Reynaldo on 25/02/2016.
 */
public class ItemReservaAdapter extends BaseAdapter{

    private LayoutInflater layoutInflater;
    private List<Reserva> reservas;

    public ItemReservaAdapter(Context context, List<Reserva> reservas)
    {
        layoutInflater = LayoutInflater.from(context);
        this.reservas = reservas;

    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Reserva reserva = reservas.get(position);

        if(convertView==null)
        {
            convertView = layoutInflater.inflate(R.layout.reserva_item, null);
        }
            TextView numeroReserva =  (TextView) convertView.findViewById(R.id.textViewNumeroReserva);
            TextView nombreReserva =  (TextView) convertView.findViewById(R.id.textViewNombreReserva);
            TextView fechaInicioReserva =  (TextView) convertView.findViewById(R.id.textViewFechaInicio);

            numeroReserva.setText(String.valueOf(reserva.getNumero()));
            nombreReserva.setText(reserva.getLugar());
            SimpleDateFormat dateFormat =  new SimpleDateFormat("MM-dd-yyyy");
            fechaInicioReserva.setText(dateFormat.format(reserva.getFecha_inicio()));
            int x=0;
        return convertView;

    }

    @Override
    public int getCount() {
        return reservas.size();
    }

    @Override
    public Object getItem(int position) {
        return reservas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return reservas.get(position).getNumero();
    }
}
