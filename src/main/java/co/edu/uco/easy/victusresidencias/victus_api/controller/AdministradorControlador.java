package co.edu.uco.easy.victusresidencias.victus_api.controller;

import co.edu.uco.easy.victusresidencias.victus_api.controller.response.concrete.AdministratorResponse;
import co.edu.uco.easy.victusresidencias.victus_api.controller.response.concrete.GenericResponse;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions.BusinessLogicVictusResidenciasException;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions.UcoApplicationException;
import co.edu.uco.easy.victusresidencias.victus_api.entity.AdministratorEntity;
import co.edu.uco.easy.victusresidencias.victus_api.service.AdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//@CrossOrigin(origins = "https://tangerine-profiterole-824fd8.netlify.app")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/administradores")
public class AdministradorControlador {
    private static final String NAMEclassSingular = "Administrador";
    private static final String NAMEclassPlural = "Administradores";

    @Autowired
    private AdministratorService adminService;

    @GetMapping("/dummy")
    public AdministratorEntity getDummy() {
        return new AdministratorEntity();
    }

    @GetMapping("/todos")
    public ResponseEntity<AdministratorResponse> retrieveAll() {
        var response = new AdministratorResponse();
        var messages = new ArrayList<String>();

        try {
            var entities = adminService.findAll();
            response.setData(entities);
            messages.add(String.format("Los %s fueron consultados satisfactoriamente.", NAMEclassPlural));
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            messages.add(String.format("Error al consultar los %s. Por favor intente nuevamente.", NAMEclassPlural));
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdministratorResponse> retrieveById(@PathVariable UUID id) {
        var response = new AdministratorResponse();
        var messages = new ArrayList<String>();

        try {
            var entity = adminService.findById(id);
            if (entity.isEmpty()) {
                messages.add(String.format("No se encontró un %s con el ID especificado.", NAMEclassSingular));
                response.setMessages(messages);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            
            response.setData(List.of(entity.get()));
            messages.add(String.format("El %s fue consultado satisfactoriamente.", NAMEclassSingular));
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            messages.add(String.format("Error al consultar el %s. Por favor intente nuevamente.", NAMEclassSingular));
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<GenericResponse> create(@RequestBody AdministratorEntity administrador) {
        var messages = new ArrayList<String>();

        try {
            if (administrador.getName() == null || administrador.getName().isEmpty()) {
                String userMessage = String.format("El %s es requerido para poder registrar la información.", NAMEclassSingular);
                String technicalMessage = String.format("El %s en la clase %s llegó nulo o vacío.", NAMEclassSingular, AdministratorEntity.class.getSimpleName());
                throw BusinessLogicVictusResidenciasException.crear(userMessage, technicalMessage);
            }
            
            if (adminService.existsByName(administrador.getName())) {
                String userMessage = String.format("El %s ya existe", NAMEclassSingular);
                String technicalMessage = String.format("El %s con el nombre ", NAMEclassSingular) + administrador.getName() + "' ya existe en la base de datos.";
                throw BusinessLogicVictusResidenciasException.crear(userMessage, technicalMessage);
            }

            adminService.save(administrador);
            messages.add(String.format("El %s se registró de forma satisfactoria.", NAMEclassSingular));
            var response = new GenericResponse();
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (UcoApplicationException exception) {
            messages.add(exception.getUserMessage());
            exception.printStackTrace();
            var response = new GenericResponse();
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception exception) {
            messages.add(String.format("Se ha presentado un problema inesperado al registrar el %s.", NAMEclassSingular));
            exception.printStackTrace();
            var response = new GenericResponse();
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdministratorResponse> update(@PathVariable UUID id, @RequestBody AdministratorEntity administrador) {
        var responseWithData = new AdministratorResponse();
        var messages = new ArrayList<String>();

        try {
            var existingAdmin = adminService.findById(id);
            if (existingAdmin.isEmpty()) {
                messages.add(String.format("No se encontró un %s con el ID especificado.", NAMEclassSingular));
                responseWithData.setMessages(messages);
                return new ResponseEntity<>(responseWithData, HttpStatus.NOT_FOUND);
            }

            var existingAdministradorEntity = existingAdmin.get();
            
            // Actualizar campos si no están vacíos
            if (administrador.getName() != null && !administrador.getName().isEmpty()) {
                existingAdministradorEntity.setName(administrador.getName());
            }
            if (administrador.getLastName() != null && !administrador.getLastName().isEmpty()) {
                existingAdministradorEntity.setLastName(administrador.getLastName());
            }
            if (administrador.getIdType() != null && !administrador.getIdType().isEmpty()) {
                existingAdministradorEntity.setIdType(administrador.getIdType());
            }
            if (administrador.getIdNumber() != null && !administrador.getIdNumber().isEmpty()) {
                existingAdministradorEntity.setIdNumber(administrador.getIdNumber());
            }
            if (administrador.getContactNumber() != null && !administrador.getContactNumber().isEmpty()) {
                existingAdministradorEntity.setContactNumber(administrador.getContactNumber());
            }
            if (administrador.getEmail() != null && !administrador.getEmail().isEmpty()) {
                existingAdministradorEntity.setEmail(administrador.getEmail());
            }
            if (administrador.getPassword() != null && !administrador.getPassword().isEmpty()) {
                existingAdministradorEntity.setPassword(administrador.getPassword());
            }

            adminService.save(existingAdministradorEntity);
            
            messages.add(String.format("El %s ", NAMEclassSingular) + existingAdministradorEntity.getName() + " se actualizó correctamente.");
            responseWithData.setData(List.of(existingAdministradorEntity));
            responseWithData.setMessages(messages);
            return new ResponseEntity<>(responseWithData, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            messages.add(String.format("Error al actualizar el %s. Por favor intente nuevamente.", NAMEclassSingular));
            responseWithData.setMessages(messages);
            return new ResponseEntity<>(responseWithData, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse> delete(@PathVariable UUID id) {
        var messages = new ArrayList<String>();
        var response = new GenericResponse();

        try {
            if (id == null) {
                var userMessage = String.format("El ID del %S es requerido para poder eliminar la información.", NAMEclassSingular);
                var technicalMessage = String.format("El ID del %s en la clase AdministratorEntity llegó nulo.", NAMEclassSingular);
                throw BusinessLogicVictusResidenciasException.crear(userMessage, technicalMessage);
            }

            var existingAdmin = adminService.findById(id);
            if (existingAdmin.isEmpty()) {
                messages.add(String.format("No se encontró un %s con el ID especificado.", NAMEclassSingular));
                response.setMessages(messages);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            adminService.deleteById(id);
            messages.add(String.format("El %s se eliminó de manera satisfactoria.", NAMEclassSingular));
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UcoApplicationException e) {
            messages.add(e.getUserMessage());
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            messages.add(String.format("Error al eliminar el %s. Por favor intente nuevamente.", NAMEclassSingular));
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
