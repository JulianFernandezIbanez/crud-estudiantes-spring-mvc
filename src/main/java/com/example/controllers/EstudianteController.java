package com.example.controllers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.entities.Correo;
import com.example.entities.Estudiante;
import com.example.entities.Telefono;
import com.example.services.EstudianteService;
import com.example.services.FacultadService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
@RequestMapping("/estudiantes")
public class EstudianteController {

    private static final Logger LOG = Logger.getLogger("EstudianteController");

    private final EstudianteService estudianteService;
    private final FacultadService facultadService;

    @GetMapping("/listar")
    public String listarEstudiantes(Model model) {

        model.addAttribute("estudiantes", estudianteService.getAllEstudiantes());

        return "listadoEstudiantes";
    }
    
    @GetMapping("/alta")
    public String mostrarformularioAlta(Model model,
        @ModelAttribute Estudiante estudiante) {
        
        model.addAttribute("facultades", facultadService.getAllFacultades());

        return "formularioAltaModificacion";
    }
    
    @PostMapping("/persistir")
    public String procesarFormularioAltaModificacion(
        @Valid
        @ModelAttribute Estudiante estudiante,
        BindingResult result,
        @RequestParam(name = "numerostlf") String numtlf, 
		@RequestParam(name = "mail") String emails,
        Model model) {
        
        if (result.hasErrors()) {

            model.addAttribute("facultades", facultadService.getAllFacultades());
            
            return "formularioAltaModificacion";

        }

        LOG.info("Estudiante recibido :");
		LOG.info(estudiante.toString());
		LOG.info(numtlf);
		LOG.info(emails);

        Set<Telefono> numerostlf = new HashSet<>();
		Set<Correo> dirCorreos = new HashSet<>();

        if(!numtlf.isEmpty() && !numtlf.isBlank()){
			
			String[] arraynumTlf = numtlf.split(";");
			List<String> listadoNumeros = Arrays.asList(arraynumTlf);
			listadoNumeros.forEach(numero -> {numerostlf.add(Telefono.builder().numero(numero).estudiante(estudiante).build());});
			estudiante.setTelefonos(numerostlf);

		}

		if(!emails.isEmpty() && !emails.isBlank()){
			
			String[] email = emails.split(";");
			List<String> listadoCorreos = Arrays.asList(email);
			listadoCorreos.forEach(correo -> {dirCorreos.add(Correo.builder().direccion(correo).estudiante(estudiante).build());});
			estudiante.setCorreos(dirCorreos);
		}

        estudianteService.saveEstudiante(estudiante);

        return "redirect:/estudiantes/listar";
    }
    
}
