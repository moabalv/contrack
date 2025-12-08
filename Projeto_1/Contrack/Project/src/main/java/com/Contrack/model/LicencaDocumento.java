package com.Contrack.model;

import com.Contrack.enums.TipoDocumento;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("LICENCA")
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class LicencaDocumento extends Documento {

    @Override
    public TipoDocumento getTipoDocumento() {
        return TipoDocumento.LICENCA;
    }
}
