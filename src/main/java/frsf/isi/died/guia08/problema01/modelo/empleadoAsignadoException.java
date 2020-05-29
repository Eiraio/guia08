package frsf.isi.died.guia08.problema01.modelo;

public class empleadoAsignadoException extends Exception {
    public empleadoAsignadoException(){
        super("Esta tarea ya fue finalizada con un empleado asignado");
    }
}
