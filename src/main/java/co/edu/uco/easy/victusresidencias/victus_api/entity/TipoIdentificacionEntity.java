package co.edu.uco.easy.victusresidencias.victus_api.entity;

import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.*;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "tipo_identificacion")
public class TipoIdentificacionEntity extends DomainEntity {

    @Column(nullable = false, name = "nombre", length = 50)
    private String nombre;

    public TipoIdentificacionEntity() {
        super(UUIDHelper.getDefault());
        setNombre(TextHelper.EMPTY);
    }

    public static final TipoIdentificacionEntity create() {
        return new TipoIdentificacionEntity();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = TextHelper.applyTrim(nombre);
    }

    @Override
    public void setId(final UUID id) {
        super.setId(id);
    }

    @Override
    public UUID getId() {
        return super.getId();
    }
}