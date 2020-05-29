package frsf.isi.died.guia08.problema01.modelo;

public class errorTareaException extends Exception {

    public errorTareaException(){
        super("La tarea que se quiere asignar es incorrecta, por favor asigne otra tarea");
    }
}
