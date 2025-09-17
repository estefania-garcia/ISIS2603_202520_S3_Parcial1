package co.edu.uniandes.dse.parcial1.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;
import co.edu.uniandes.dse.parcial1.entities.*;
import co.edu.uniandes.dse.parcial1.exceptions.*;
import co.edu.uniandes.dse.parcial1.repositories.*;

@DataJpaTest
@Transactional
@Import(EstacionRutaService.class)
public class EstacionRutaServiceTest {
    
    @Autowired
    private EstacionRutaService estacionRutaService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private EstacionEntity estacion = new EstacionEntity();
    private List<RutaEntity> rutaList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from EstacionEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from RutaEntity").executeUpdate();
    }

    private void insertData() {
		estacion = factory.manufacturePojo(EstacionEntity.class);
		entityManager.persist(estacion);

		for (int i = 0; i < 3; i++) {
			RutaEntity entity = factory.manufacturePojo(RutaEntity.class);
			entityManager.persist(entity);
			entity.getEstaciones().add(estacion);
			rutaList.add(entity);
			estacion.getRutas().add(entity);	
		}
	}

    @Test
	void testAddEstacionRuta() throws EntityNotFoundException, IllegalOperationException {
		EstacionEntity nuevaEstacion = factory.manufacturePojo(EstacionEntity.class);
		entityManager.persist(nuevaEstacion);
		
		RutaEntity ruta = factory.manufacturePojo(RutaEntity.class);
		entityManager.persist(ruta);
		
		estacionRutaService.addEstacionRuta(nuevaEstacion.getId(), ruta.getId());
		
		RutaEntity lastRuta = estacionRutaService.getRuta(nuevaEstacion.getId(), ruta.getId());
		assertEquals(ruta.getId(), lastRuta.getId());
		assertEquals(ruta.getNombre(), lastRuta.getNombre());
		assertEquals(ruta.getTipo(), lastRuta.getTipo());
		assertEquals(ruta.getColor(), lastRuta.getColor());
	}

    @Test
	void testAddInvalidRuta() {
		assertThrows(EntityNotFoundException.class, ()->{
			EstacionEntity nuevaEstacion = factory.manufacturePojo(EstacionEntity.class);
			entityManager.persist(nuevaEstacion);
			estacionRutaService.addEstacionRuta(nuevaEstacion.getId(), 0L);
		});
	}

    @Test
	void testAddInvalidEstacion() {
		assertThrows(EntityNotFoundException.class, ()->{
			RutaEntity nuevaRuta = factory.manufacturePojo(RutaEntity.class);
			entityManager.persist(nuevaRuta);
			estacionRutaService.addEstacionRuta(nuevaRuta.getId(), 0L);
		});
	}

    /*
    @Test
    void testAddExistedRuta(){
        assertThrows(IllegalOperationException.class, ()->{
            RutaEntity nuevaRuta = factory.manufacturePojo(RutaEntity.class);
            entityManager.persist(nuevaRuta);
            estacionRutaService.addEstacionRuta(nuevaRuta.getId(), 0L);
        });
    }*/

    @Test
	void testRemoveNotExistEstacion() throws EntityNotFoundException, IllegalOperationException {
		assertThrows(IllegalOperationException.class, ()->{
            for (RutaEntity ruta : rutaList) {
			    estacionRutaService.removeEstacionRuta(estacion.getId(), ruta.getId());
		    }
		    assertTrue(estacionRutaService.getRutas(estacion.getId()).isEmpty());
        });
	}

    @Test
	void testRemoveInvalidEstacion() throws EntityNotFoundException, IllegalOperationException {
		assertThrows(EntityNotFoundException.class, ()->{
             estacionRutaService.removeEstacionRuta(0L, rutaList.get(0).getId());
        });
	}

    @Test
	void testRemoveInvalidRuta() throws EntityNotFoundException, IllegalOperationException {
		assertThrows(EntityNotFoundException.class, ()->{
            estacionRutaService.removeEstacionRuta(estacion.getId(), 0L);
        });
	}
}
