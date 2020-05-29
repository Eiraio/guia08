package frsf.isi.died.guia08.problema01;

import java.io.*;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import frsf.isi.died.guia08.problema01.modelo.*;

public class AppRRHH {

	private List<Empleado> empleados;
	private Optional<Empleado> aux;

	public void agregarEmpleadoContratado(Integer cuil,String nombre,Double costoHora) {

		empleados.add(new Empleado(cuil,nombre, Empleado.Tipo.CONTRATADO,costoHora));

	}
	
	public void agregarEmpleadoEfectivo(Integer cuil,String nombre,Double costoHora) {
		empleados.add(new Empleado(cuil,nombre, Empleado.Tipo.EFECTIVO,costoHora));
	}
	
	public void asignarTarea(Integer cuil,Integer idTarea,String descripcion,Integer duracionEstimada) {
		aux = buscarEmpleado( t -> t.getCuil() == cuil);
		if(aux.isPresent()){
			try{
				aux.get().asignarTarea(new Tarea(idTarea, descripcion, duracionEstimada,aux.get()));
			}catch (errorTareaException r){
				System.out.println(r.getMessage());
			}

		}
	}
	
	public void empezarTarea(Integer cuil,Integer idTarea) {
		aux = buscarEmpleado( t -> t.getCuil() == cuil);
		if(aux.isPresent()){
			try{
				aux.get().comenzar(idTarea);
			}catch (tareaNoAsignadaException e) {
				e.getMessage();
			}
		}
	}
	
	public void terminarTarea(Integer cuil,Integer idTarea) {
		aux = buscarEmpleado( t -> t.getCuil() == cuil);
		if(aux.isPresent()){
			try{
				aux.get().finalizar(idTarea);
			}catch (tareaNoAsignadaException e) {
				e.getMessage();
			}
		}
	}

	public void cargarEmpleadosContratadosCSV(String nombreArchivo) {
		FileInputStream fis;
		try(Reader fileReader = new FileReader(nombreArchivo)) {
			try(BufferedReader in = new BufferedReader(fileReader)){
				String linea = null;
				while((linea = in.readLine())!=null) {
					String[] fila = linea.split(";");
					agregarEmpleadoContratado(Integer.valueOf(fila[0]),fila[1],Double.valueOf(fila[2]));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// leer datos del archivo
		// por cada fila invocar a agregarEmpleadoContratado
	}

	public void cargarEmpleadosEfectivosCSV(String nombreArchivo) {
		FileInputStream fis;
		try(Reader fileReader = new FileReader(nombreArchivo)) {
			try(BufferedReader in = new BufferedReader(fileReader)){
				String linea = null;
				while((linea = in.readLine())!=null) {
					String[] fila = linea.split(";");
					agregarEmpleadoEfectivo(Integer.valueOf(fila[0]),fila[1],Double.valueOf(fila[2]));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// leer datos del archivo
		// por cada fila invocar a agregarEmpleadoContratado		
	}

	public void cargarTareasCSV(String nombreArchivo) {
		FileInputStream fis;
		try(Reader fileReader = new FileReader(nombreArchivo)) {
			try(BufferedReader in = new BufferedReader(fileReader)){
				String linea = null;
				while((linea = in.readLine())!=null) {
					String[] fila = linea.split(";");
					asignarTarea(Integer.valueOf(fila[3]),Integer.valueOf(fila[0]),fila[1],Integer.valueOf(fila[2]));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// leer datos del archivo
		// cada fila del archivo tendrá:
		// cuil del empleado asignado, numero de la taera, descripcion y duración estimada en horas.
	}
	
	private void guardarTareasTerminadasCSV() {

		try (Writer fileWriter = new FileWriter("tareas.csv", true)) {
			try (BufferedWriter out = new BufferedWriter(fileWriter)) {
				empleados.stream().map(t -> t.getTareasAsignadas()).flatMap(List::stream).filter(t -> t.getFechaFin() != null && t.getFacturada() == false).forEach(t -> {
					try {
						out.write(t.asCsv() + System.getProperty("line.separator"));
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private Optional<Empleado> buscarEmpleado(Predicate<Empleado> p){
		return this.empleados.stream().filter(p).findFirst();
	}

	public Double facturar() {
		this.guardarTareasTerminadasCSV();
		return this.empleados.stream()				
				.mapToDouble(e -> e.salario())
				.sum();
	}
}
