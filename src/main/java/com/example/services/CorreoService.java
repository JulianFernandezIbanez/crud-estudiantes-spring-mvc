package com.example.services;

import java.util.List;

import com.example.entities.Correo;
import com.example.entities.Estudiante;

public interface CorreoService {

    List<Correo> getAllCorreos();
    Correo saveCorreo(Correo correo);
    boolean existsByEstudiante(Estudiante estudiante);
    void deleteByEstudiante(Estudiante estudiante);
    List<Correo> findByEstudiante(Estudiante estudiante);

}
