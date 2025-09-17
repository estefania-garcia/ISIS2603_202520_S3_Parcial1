package co.edu.uniandes.dse.parcial1.entities;

import jakarta.persistence.Entity;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;
import jakarta.persistence.ManyToMany;
import java.util.*;

@Data
@Entity
public class EstacionEntity extends BaseEntity {
    private String name;
    private String direccion;
    private Integer capacidad;

    @PodamExclude
    @ManyToMany(mappedBy = "estaciones")
    private Collection<RutaEntity> rutas = new ArrayList<>();
}
