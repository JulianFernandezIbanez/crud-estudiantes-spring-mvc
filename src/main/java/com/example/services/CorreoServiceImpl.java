package com.example.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.dao.CorreoDao;
import com.example.entities.Correo;
import com.example.entities.Estudiante;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CorreoServiceImpl implements CorreoService {

    private final CorreoDao correoDao;

    @Override
    public List<Correo> getAllCorreos() {

        return correoDao.findAll();

    }

    @Override
    public Correo saveCorreo(Correo correo) {

        return correoDao.save(correo);

    }

    @Override
    public boolean existsByEstudiante(Estudiante estudiante) {

        return correoDao.existsByEstudiante(estudiante);

    }

    @Override
    public void deleteByEstudiante(Estudiante estudiante) {

        correoDao.deleteByEstudiante(estudiante);
    
    }

    @Override
    public List<Correo> findByEstudiante(Estudiante estudiante) {

        return correoDao.findByEstudiante(estudiante);
    
    }

}
