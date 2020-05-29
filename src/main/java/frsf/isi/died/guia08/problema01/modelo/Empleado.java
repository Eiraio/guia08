package frsf.isi.died.guia08.problema01.modelo;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Empleado {

	public enum Tipo { CONTRATADO,EFECTIVO};
	
	private Integer cuil;
	private String nombre;
	private Tipo tipo;
	private Double costoHora;
	private List<Tarea> tareasAsignadas;
	
	private Function<Tarea, Double> calculoPagoPorTarea;		
	private Predicate<Tarea> puedeAsignarTarea;

	private List<Tarea> auxlist;
	private double auxdouble;
	private Duration lapso;


	private DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

	public Empleado(Integer cuil, String nombre, Tipo tipo, Double costoHora) {
		this.cuil = cuil;
		this.nombre = nombre;
		this.tipo = tipo;
		this.costoHora = costoHora;
	}

	public String getNombre() {
		return nombre;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public Double salario() {

		auxlist = tareasAsignadas.stream().filter(g -> g.getFacturada()==false && g.getFechaFin() != null).collect((Collectors.toList()));
		auxdouble=0;

		for(int i=0; i <= auxlist.size(); i++){
			lapso = Duration.between(auxlist.get(i).getFechaInicio(),auxlist.get(i).getFechaFin());
			if(lapso.toDays()*4 <= auxlist.get(i).getDuracionEstimada()){
				if(this.getTipo() == Tipo.EFECTIVO){
					auxdouble += auxlist.get(i).getDuracionEstimada() * this.costoHora * 1.20;
				} else {
					auxdouble += auxlist.get(i).getDuracionEstimada() * this.costoHora * 1.30;
				}
			} else if(this.getTipo() == Tipo.CONTRATADO && (lapso.toDays()*4) >= (auxlist.get(i).getDuracionEstimada()+8)){
				auxdouble += auxlist.get(i).getDuracionEstimada() * this.costoHora * 0.75;
			} else{
				auxdouble += auxlist.get(i).getDuracionEstimada();
			}

			auxlist.get(i).setFacturada(true);
		}

		return auxdouble;
	}




	/**
	 * Si la tarea ya fue terminada nos indica cuaal es el monto según el algoritmo de calculoPagoPorTarea
	 * Si la tarea no fue terminada simplemente calcula el costo en base a lo estimado.
	 * @param t
	 * @return
	 */
	public Double costoTarea(Tarea t) {
		return 0.0;
	}

	public Integer getCuil() {
		return cuil;
	}

	public List<Tarea> getTareasAsignadas() {
		return tareasAsignadas;
	}

	public Boolean asignarTarea(Tarea t) throws errorTareaException{

		if(t.getFechaFin() != null || t.getEmpleadoAsignado() != null){ throw new errorTareaException();}


		if(this.getTipo() == Tipo.CONTRATADO){
			if (tareasAsignadas.stream().filter(y -> y.getFechaFin() == null).count() >= 5){
				return false;
			}
		} else{
			if(tareasAsignadas.stream().filter(y -> y.getFechaFin() == null).map(g -> g.getDuracionEstimada()).collect(Collectors.summingInt(Integer::intValue)) + t.getDuracionEstimada() >= 15){
				return false;
			}

		}

		tareasAsignadas.add(t);
		return true;
	}
	
	public void comenzar(Integer idTarea) throws tareaNoAsignadaException{

		if(tareasAsignadas.stream().anyMatch(t -> t.getId() == idTarea)) {
			tareasAsignadas.stream().filter(t -> t.getId() == idTarea).collect(Collectors.toList()).get(0).setFechaInicio(LocalDateTime.now());
		}else{
			throw new tareaNoAsignadaException();
		}

	}
	
	public void finalizar(Integer idTarea) throws tareaNoAsignadaException {
		if(tareasAsignadas.stream().anyMatch(t -> t.getId() == idTarea)) {
			tareasAsignadas.stream().filter(t -> t.getId() == idTarea).collect(Collectors.toList()).get(0).setFechaFin(LocalDateTime.now());
		}else{
			throw new tareaNoAsignadaException();
		}
	}

	public void comenzar(Integer idTarea,String fecha) throws tareaNoAsignadaException{
		if(tareasAsignadas.stream().anyMatch(t -> t.getId() == idTarea)) {
			tareasAsignadas.stream().filter(t -> t.getId() == idTarea).collect(Collectors.toList()).get(0).setFechaInicio(LocalDateTime.parse(fecha,formater));
		}else{
			throw new tareaNoAsignadaException();
		}
	}
	
	public void finalizar(Integer idTarea,String fecha) throws tareaNoAsignadaException{
		if(tareasAsignadas.stream().anyMatch(t -> t.getId() == idTarea)) {
			tareasAsignadas.stream().filter(t -> t.getId() == idTarea).collect(Collectors.toList()).get(0).setFechaFin(LocalDateTime.parse(fecha,formater));
		}else{
			throw new tareaNoAsignadaException();
		}
	}
}
