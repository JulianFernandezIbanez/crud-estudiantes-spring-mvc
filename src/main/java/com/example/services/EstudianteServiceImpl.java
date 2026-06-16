package com.example.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.dao.EstudianteDao;
import com.example.entities.Estudiante;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EstudianteServiceImpl implements EstudianteService {

    private final EstudianteDao estudianteDao;

    @Override
    public List<Estudiante> getAllEstudiantes() {
        
        return estudianteDao.findAll();

    }

    @Override
    public Estudiante getEstudianteById(int id) {

        return estudianteDao.findById(id).orElseThrow(() ->
            new RuntimeException("Estudiante no encontrado con id: "+id));

    }

    @Override
    public Estudiante saveEstudiante(Estudiante estudiante) {

        return estudianteDao.save(estudiante);
    
    }

    @Override
    public void deleteEstudianteById(int id) {

        estudianteDao.deleteById(id);
    
    }

    @Override
    public void deleteEstudiante(Estudiante estudiante) {

        estudianteDao.delete(estudiante);
    
    }

    @Override
    public Estudiante updateEstudiante(Estudiante estudiante) {

        return estudianteDao.save(estudiante);
    
    }

}
