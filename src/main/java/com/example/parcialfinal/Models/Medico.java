package com.example.parcialfinal.Models;

public class Medico extends Persona{
    private String especialidad;

    public Medico() {}

    public Medico(String id, String nombre, String telefono, String email , String especialidad) {
        super(id, nombre, telefono, email);
        this.especialidad = especialidad;
           }

   public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }


}

