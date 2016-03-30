package com.home.reservas.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import com.home.reservas.R;
import com.home.reservas.model.Reserva;

/**
 * Created by Reynaldo on 25/02/2016.
 */
public class ItemReservaAdapter extends BaseAdapter implements View.OnClickListener{

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
        Holder holder = null;

        if(convertView == null)
        {
            holder = new Holder();
            convertView = layoutInflater.inflate(R.layout.reserva_item, null);
            holder.setCheckBoxDelete((CheckBox) convertView.findViewById(R.id.checkBoxDelete));
            holder.setTextViewNumeroReserva((TextView) convertView.findViewById(R.id.textViewNumeroReserva));
            holder.setTextViewNombreReserva((TextView) convertView.findViewById(R.id.textViewNombreReserva));
            holder.setTextViewFechaInicio((TextView) convertView.findViewById(R.id.textViewFechaInicio));
        }else{
            holder = (Holder) convertView.getTag();
        }
        //TableRow tableRowReservaItem = (TableRow) convertView.findViewById(R.id.tableRowReservaItem);
        //CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBoxDelete);
        //TextView numeroReserva =  (TextView) convertView.findViewById(R.id.textViewNumeroReserva);
        //TextView nombreReserva =  (TextView) convertView.findViewById(R.id.textViewNombreReserva);
        //TextView fechaInicioReserva =  (TextView) convertView.findViewById(R.id.textViewFechaInicio);

        //numeroReserva.setText(String.valueOf(reserva.getNumero()));
        //nombreReserva.setText(reserva.getLugar());

        //fechaInicioReserva.setText(dateFormat.format(reserva.getFecha_inicio()));

        holder.getTextViewNumeroReserva().setText(String.valueOf(reserva.getNumero()));
        holder.getTextViewNombreReserva().setText(reserva.getId_persona());
        SimpleDateFormat dateFormat =  new SimpleDateFormat("MM-dd-yyyy");
        holder.getTextViewFechaInicio().setText(dateFormat.format(reserva.getFecha_inicio()));
        holder.getCheckBoxDelete().setTag(position);
        holder.getCheckBoxDelete().setChecked(reservas.get(position).isChecked());
        holder.getCheckBoxDelete().setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        CheckBox checkBox = (CheckBox) v;
        int position = (Integer) v.getTag();
        reservas.get(position).setIsChecked(checkBox.isChecked());
    }

    static class Holder{

        CheckBox checkBoxDelete;
        TextView textViewNumeroReserva;
        TextView textViewNombreReserva;
        TextView textViewFechaInicio;

        public CheckBox getCheckBoxDelete() {
            return checkBoxDelete;
        }

        public void setCheckBoxDelete(CheckBox checkBoxDelete) {
            this.checkBoxDelete = checkBoxDelete;
        }

        public TextView getTextViewNumeroReserva() {
            return textViewNumeroReserva;
        }

        public void setTextViewNumeroReserva(TextView textViewNumeroReserva) {
            this.textViewNumeroReserva = textViewNumeroReserva;
        }

        public TextView getTextViewNombreReserva() {
            return textViewNombreReserva;
        }

        public void setTextViewNombreReserva(TextView textViewNombreReserva) {
            this.textViewNombreReserva = textViewNombreReserva;
        }

        public TextView getTextViewFechaInicio() {
            return textViewFechaInicio;
        }

        public void setTextViewFechaInicio(TextView textViewFechaInicio) {
            this.textViewFechaInicio = textViewFechaInicio;
        }

    }
}
