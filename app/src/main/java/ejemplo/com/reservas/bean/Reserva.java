package ejemplo.com.reservas.bean;

import java.util.Date;

/**
 * Created by Reynaldo on 17/02/2016.
 */
public class Reserva {
    private int numero;
    private Date fecha_fin;
    private int horas;
    private String lugar;
    private int estado;
    private String id_persona;

    public int getNumero() {
        return numero;
    }

    private Date fecha_inicio;

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getId_persona() {
        return id_persona;
    }

    public void setId_persona(String id_persona) {
        this.id_persona = id_persona;
    }

    public Date getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(Date fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public Date getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(Date fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public int getHoras() {
        return horas;
    }

    public void setHoras(int horas) {
        this.horas = horas;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }


}
