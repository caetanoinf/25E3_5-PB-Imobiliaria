package edu.infnet.user.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImovelCriadoEvent implements Serializable {
    private Long id;
    private String tipo;
    private String cidade;
    private String estado;
    private Double areaM2;
    private BigDecimal valor;
    private String status;
}
