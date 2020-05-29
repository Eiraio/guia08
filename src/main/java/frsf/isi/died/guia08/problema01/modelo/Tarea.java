package frsf.isi.died.guia08.problema01.modelo;

import java.time.LocalDateTime;

public class Tarea {

	private Integer id;
	private String descripcion;
	private Integer duracionEstimada;
	private Empleado empleadoAsignado;
	private LocalDateTime fechaInicio;
	private LocalDateTime fechaFin;
	private Boolean facturada;

	public Tarea(Integer id, String descripcion, Integer duracionEstimada, Empleado emp) {
		this.id = id;
		this.descripcion = descripcion;
		this.duracionEstimada = duracionEstimada;
		this.empleadoAsignado = emp;
	}

	public void asignarEmpleado(Empleado e) throws empleadoAsignadoException{
		if(empleadoAsignado != null && fechaFin != null){
			throw new empleadoAsignadoException();
		}else{
			empleadoAsignado=e;
		}

		// si la tarea ya tiene un empleado asignado
		// y tiene fecha de finalizado debe lanzar una excepcion
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getDuracionEstimada() {
		return duracionEstimada;
	}

	public void setDuracionEstimada(Integer duracionEstimada) {
		this.duracionEstimada = duracionEstimada;
	}

	public LocalDateTime getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDateTime fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDateTime getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDateTime fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Boolean getFacturada() {
		return facturada;
	}

	public void setFacturada(Boolean facturada) {
		this.facturada = facturada;
	}

	public Empleado getEmpleadoAsignado() {
		return empleadoAsignado;
	}
	
	public String asCsv(){
		return this.id + "," + this.descripcion + ";" + this.duracionEstimada + "," + this.empleadoAsignado.getCuil() + "," + this.empleadoAsignado.getNombre()  + "," + this.fechaInicio + "," + this.fechaFin;
	}
}
