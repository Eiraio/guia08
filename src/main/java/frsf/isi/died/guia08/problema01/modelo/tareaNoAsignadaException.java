package frsf.isi.died.guia08.problema01.modelo;

public class tareaNoAsignadaException extends Exception {

    public tareaNoAsignadaException(){
        super("Esta tarea no existe entre las tareas asignadas a este empleado");
    }
}
