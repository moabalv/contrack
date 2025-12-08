package com.Contrack.model.Documento.Factory;

import com.Contrack.enums.TipoDocumento;
import com.Contrack.model.Documento.Documento;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("ALVARA")
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class AlvaraDocumento extends Documento {

    @Override
    public TipoDocumento getTipoDocumento() {
        return TipoDocumento.ALVARA;
    }
}
