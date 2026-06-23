package com.example.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.models.Genero;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name ="estudiantes")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Estudiante implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "El campo nombre no puede estar vacio")
    @NotBlank(message = "El campo nombre no puede contener unicamente espacios en blanco")
    @Size(min = 4, max = 30, message = "El nombre no cumple los requisitos (minimo 4 y maximo 30 caracteres)")
    @Pattern(regexp = "^([A-ZÁÉÍÓÚÑ][a-záéíóúüñ]+(\s)?)+$", message = "El nombre solo puede contener los caracteres de la A a la Z y su primer caracter a de ser una letra mayuscula (A-Z)")
    private String nombre;

    @NotNull(message = "El campo Primer Apellido no puede estar vacio")
    @NotBlank(message = "El campo Primer Apellido no puede contener unicamente espacios en blanco")
    @Size(min = 4, max = 30, message = "El Primer Apellido no cumple los requisitos (minimo 4 y maximo 30 caracteres)")
    @Pattern(regexp = "^([A-ZÁÉÍÓÚÑ][a-záéíóúüñ]+(\s)?)+$", message = "El Primer Apellido solo puede contener los caracteres de la A a la Z y su primer caracter a de ser una letra mayuscula (A-Z)")
    private String primerApellido;

    @NotNull(message = "El campo Segundo Apellido no puede contener unicamente espacios en blanco")
    @Size(max = 30, message = "El Segundo Apellido no cumple los requisitos (minimo 4 y maximo 30 caracteres)")
    @Pattern(regexp = "^(|[A-ZÁÉÍÓÚÑ][a-záéíóúüñ]{2,}+(\s)?)+$", message = "El Segundo Apellido solo puede contener los caracteres de la A a la Z y su primer caracter a de ser una letra mayuscula (A-Z)")
    private String segundoApellido;


    @Enumerated(EnumType.STRING)
    private Genero genero;


    @DateTimeFormat(pattern ="yyyy-MM-dd")
    @FutureOrPresent(message = "La fecha de Matriculacion no puede ser inferior a la fecha actual")
    private LocalDate fechaMatriculacion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Facultad facultad;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "estudiante")
    @Builder.Default
    private Set<Telefono> telefonos = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "estudiante")
    @Builder.Default
    private Set<Correo> correos = new HashSet<>();

}
