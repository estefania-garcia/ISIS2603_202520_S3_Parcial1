package co.edu.uniandes.dse.parcial1.entities;

import jakarta.persistence.Entity;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;
import jakarta.persistence.ManyToMany;
import java.util.*;

@Data
@Entity
public class RutaEntity extends BaseEntity {

    private String nombre;
    private String color;
    private String tipo;

    @PodamExclude
    @ManyToMany
    private Collection<EstacionEntity> estaciones = new ArrayList<>(); 
}