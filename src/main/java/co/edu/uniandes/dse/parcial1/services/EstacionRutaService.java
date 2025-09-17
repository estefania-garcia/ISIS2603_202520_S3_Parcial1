package co.edu.uniandes.dse.parcial1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

import co.edu.uniandes.dse.parcial1.entities.*;
import co.edu.uniandes.dse.parcial1.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcial1.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcial1.repositories.EstacionRepository;
import co.edu.uniandes.dse.parcial1.repositories.RutaRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EstacionRutaService {
    
    @Autowired
    private EstacionRepository estacionRepository;

    @Autowired
    private RutaRepository rutaRepository;

    /**
     * Agregar una ruta a la estación. Validando las siguientes reglas de negoci:
     * La estación proporcionada debe existir *
     * La ruta proporcionada deben existir *
     * La estación dada NO debe ser parte de la ruta dada *

     * @param estacionId
     * @param rutaId
     * @return
     * @throws EntityNotFoundException
     */
    @Transactional
    public RutaEntity addEstacionRuta(Long estacionId, Long rutaId) throws EntityNotFoundException, IllegalOperationException{
        log.info("Inicia proceso de asociarle una ruta a una estación");
        Optional<RutaEntity> rutaEntity = rutaRepository.findById(rutaId);
        if (rutaEntity.isEmpty()) {
            throw new EntityNotFoundException("Ruta no encontrada");
        }
        Optional<EstacionEntity> estacionEntity = estacionRepository.findById(estacionId);
        if (estacionEntity.isEmpty()) {
            throw new EntityNotFoundException("Estacion no encontrada");
        }
        Collection<EstacionEntity> listaEstaciones = rutaEntity.get().getEstaciones();
        for(EstacionEntity estaciones : listaEstaciones){
            if (estaciones.getId().equals(estacionId)) {
                throw new IllegalOperationException("La estación dada no puede ser parte de la lista existente de estaciones ya asociadas a una ruta");
            }
        }
        estacionEntity.get().getRutas().add(rutaEntity.get());
        log.info("Termina proceso de asociarle una ruta a una estacion");
        return rutaEntity.get();
    }

    /**
     * Elimina la asociación entre la ruta y la estación. Validando las siguientes reglas de negocio:
     * La estación proporcionada debe existir*
     * La ruta proporcionada debe existir*
     * La estación dada debe ser parte de la ruta dada

     * @param estacionId
     * @param rutaId
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @Transactional
    public void removeEstacionRuta(Long estacionId, Long rutaId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de borrar una ruta de la estacion dada");
        Optional<RutaEntity> rutaEntity = rutaRepository.findById(rutaId);
        Optional<EstacionEntity> estacionEntity = estacionRepository.findById(estacionId);

        if (rutaEntity.isEmpty()) {
            throw new EntityNotFoundException("La ruta no existe");
        }
        if (estacionEntity.isEmpty()) {
            throw new EntityNotFoundException("La estación no existe");
        }
        Collection<EstacionEntity> listaEstaciones = rutaEntity.get().getEstaciones();
        for(EstacionEntity estaciones : listaEstaciones){
            if (estaciones.getId().equals(estacionId)) {
                throw new IllegalOperationException("La estación dada debe ser parte de la lista existente de estaciones ya asociadas a una ruta");
            }
        }
        estacionEntity.get().getRutas().remove(rutaEntity.get());
        log.info("Finaliza proceso");
    }

    @Transactional
    public RutaEntity getRuta(Long estacionId, Long rutaId)  throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de consultar una ruta de la estacion con id = {0}", estacionId);
		Optional<RutaEntity> rutaEntity = rutaRepository.findById(rutaId);
		Optional<EstacionEntity> estacionEntity = estacionRepository.findById(estacionId);

		if (rutaEntity.isEmpty()) {
			throw new EntityNotFoundException("Ruta no encontrada");
		}
		if (estacionEntity.isEmpty()) { 
			throw new EntityNotFoundException("Estación no encontrada");
		}
		log.info("Termina proceso de consultar una ruta de la estacion con id = {0}", estacionId);
		if (!estacionEntity.get().getRutas().contains(rutaEntity.get())) {
			throw new IllegalOperationException("La ruta no esta asociada a una estación");
		}
		return rutaEntity.get();
    }

    @Transactional
	public Collection<RutaEntity> getRutas(Long estacionId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar todas las rutas de la estación con id = {0}", estacionId);
		Optional<EstacionEntity> estacionEntity = estacionRepository.findById(estacionId);
		if (estacionEntity.isEmpty()) {
			throw new EntityNotFoundException("Estacion no encontrada");
		}
		log.info("Finaliza proceso de consultar todas las rutas de la estacion con id = {0}", estacionId);
		return estacionEntity.get().getRutas();
	}
}