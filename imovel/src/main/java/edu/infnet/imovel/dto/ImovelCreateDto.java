package edu.infnet.imovel.dto;

import edu.infnet.imovel.model.Endereco;
import edu.infnet.imovel.model.StatusImovel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ImovelCreateDto(
        @NotBlank(message = "O tipo do imóvel não pode ser vazio")
        String tipo,

        @NotNull(message = "O endereço não pode ser nulo")
        @Valid
        Endereco endereco,

        @NotNull(message = "A área não pode ser nula")
        @Positive(message = "A área deve ser um número positivo")
        Double areaM2,

        @NotNull(message = "O valor não pode ser nulo")
        @DecimalMin(value = "0.0", inclusive = false, message = "O valor deve ser maior que zero")
        BigDecimal valor,

        @NotNull(message = "O status não pode ser nulo")
        StatusImovel status,

        String descricao
) {
}
