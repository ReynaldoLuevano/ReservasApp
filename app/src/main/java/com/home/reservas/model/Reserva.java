package com.home.reservas.model;

public class Reserva {

	private int numero;
	private String idPersona;
	private String fechaInicio;
	private String fechaFin;
	private int horas;
	private String lugar;
	private int estado;

	public Reserva() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Reserva(int numero, String idPersona, String fechaInicio, String fechaFin, int horas, String lugar,
			int estado) {
		super();
		this.numero = numero;
		this.idPersona = idPersona;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.horas = horas;
		this.lugar = lugar;
		this.estado = estado;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(String idPersona) {
		this.idPersona = idPersona;
	}

	public String getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public String getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
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
