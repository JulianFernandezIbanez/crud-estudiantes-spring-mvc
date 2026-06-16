package com.example.services;

import java.util.List;

import com.example.entities.Estudiante;

public interface EstudianteService {

    List<Estudiante> getAllEstudiantes();
    Estudiante getEstudianteById(int id);
    Estudiante saveEstudiante(Estudiante estudiante);
    void deleteEstudianteById(int id);
    void deleteEstudiante(Estudiante estudiante);
    Estudiante updateEstudiante(Estudiante estudiante);

}
