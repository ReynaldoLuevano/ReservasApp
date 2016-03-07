package com.home.reservas;

import com.home.reservas.model.Reserva;

/**
 * Created by Reynaldo on 07/03/2016.
 */
public class ReservasData {

    private Reserva reserva;
    private static final ReservasData reservasData = new ReservasData();

    public Reserva getReserva()
    {
        return this.reserva;
    }

    public void setReserva(Reserva r)
    {
        this.reserva = r;

    }

    public static ReservasData getInstace() {return reservasData;}


}
