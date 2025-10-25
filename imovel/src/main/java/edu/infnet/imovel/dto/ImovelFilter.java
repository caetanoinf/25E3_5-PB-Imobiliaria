package edu.infnet.imovel.dto;

import edu.infnet.imovel.model.StatusImovel;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ImovelFilter {
    private StatusImovel status;
    private String cidade;
    private BigDecimal valorMaximo;
    private Double areaMinima;
    private String tipo;

    public boolean isEmpty() {
        return status == null &&
               (cidade == null || cidade.trim().isEmpty()) &&
               valorMaximo == null &&
               areaMinima == null &&
               (tipo == null || tipo.trim().isEmpty());
    }

    public static ImovelFilter defaultFilter() {
        ImovelFilter filter = new ImovelFilter();
        filter.setStatus(StatusImovel.DISPONIVEL);
        filter.setValorMaximo(new BigDecimal("1000000"));
        filter.setAreaMinima(0.0);
        return filter;
    }
}
