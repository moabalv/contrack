package com.Contrack.dto.Notificacao;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificacaoPostResponseDTO {

    @JsonProperty("id")
    @Id
    private Long id;

}
