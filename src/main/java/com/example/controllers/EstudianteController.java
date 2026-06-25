package com.example.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.entities.Correo;
import com.example.entities.Estudiante;
import com.example.entities.Telefono;
import com.example.services.CorreoService;
import com.example.services.EstudianteService;
import com.example.services.FacultadService;
import com.example.services.TelefonoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequiredArgsConstructor
@RequestMapping("/estudiantes")
public class EstudianteController {

    private static final Logger LOG = Logger.getLogger("EstudianteController");

    private final EstudianteService estudianteService;
    private final FacultadService facultadService;
    private final TelefonoService telefonoService;
	private final CorreoService correoService;

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
        Model model,
        @RequestParam(name = "file", required = false) MultipartFile file) {
        
        if (result.hasErrors()) {

            model.addAttribute("facultades", facultadService.getAllFacultades());
            
            return "formularioAltaModificacion";

        }

        if (file != null && !file.isEmpty()) {
			
			Path relativePath = Paths.get("src/main/resources/static/imagenes/");
			String absolutePath = relativePath.toFile().getAbsolutePath();
			Path completePath = Paths.get(absolutePath + "/" + file.getOriginalFilename());

			try {

				byte[] bytesImagenRecibida = file.getBytes();
				Files.write(completePath, bytesImagenRecibida);
				estudiante.setFoto(file.getOriginalFilename());

			} catch (Exception e) {
				e.printStackTrace();
			}

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

        if (estudiante.getId() != 0) {
            if (telefonoService.existsByEstudiante(estudiante)) {
                telefonoService.deleteByEstudiante(estudiante);
            }

            if (correoService.existsByEstudiante(estudiante)) {
                correoService.deleteByEstudiante(estudiante);
            }
        }
        

        estudianteService.saveEstudiante(estudiante);

        return "redirect:/estudiantes/listar";
    }
    
    @GetMapping("/detalles/{id}")
	public String detallesEmpleado(Model model,
		@PathVariable(name = "id", required = true) int estudiante_id) {

		model.addAttribute("estudiante", estudianteService.getEstudianteById(estudiante_id));

		return "detalles";

	}

    @GetMapping("/update/{id}")
	public String actualizarEmpleado(Model model,
		@PathVariable(name = "id", required = true) int estudiante_id) {

		Estudiante estudiante = estudianteService.getEstudianteById(estudiante_id);

		model.addAttribute("estudiante", estudiante);
		model.addAttribute("facultades",facultadService.getAllFacultades());

		//Procesar los telefonos y correos en el controller 
		// ya que no es aconsejable hacerlo en la vista

		Set<Telefono> telefonos = estudiante.getTelefonos();
		Set<Correo> correos = estudiante.getCorreos();

		if (telefonos.size() > 0) {
			
			String numTlf = telefonos.stream().map(telefono -> telefono.getNumero()).collect(Collectors.joining(";"));
			model.addAttribute("telefonos", numTlf);

		}

		if (correos.size() > 0) {
			
			String emails = correos.stream().map(correo -> correo.getDireccion()).collect(Collectors.joining(";"));
			model.addAttribute("emails", emails);

		}

		//Comprobar si el empleado tiene una foto
		String foto = estudiante.getFoto();

		if (foto != null) {
			
			model.addAttribute("foto", foto);

		}

		return "formularioAltaModificacion";
	}

    @GetMapping("/delete/{id}")
	public String deleteEstudiante(Model model,
		@PathVariable(name = "id", required = true) int estudiante_id) {

        Estudiante estudiante = estudianteService.getEstudianteById(estudiante_id);

		String foto = estudiante.getFoto();

		Path relativePath = Paths.get("src/main/resources/static/imagenes/");
		String absolutePath = relativePath.toFile().getAbsolutePath();
		Path completePath = Paths.get(absolutePath + "/" + foto);

		if (foto != null) {
			try {
				Files.delete(completePath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		estudianteService.deleteEstudianteById(estudiante_id);

		return "redirect:/estudiantes/listar";

	}

}
