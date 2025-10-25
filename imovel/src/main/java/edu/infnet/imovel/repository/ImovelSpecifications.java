package edu.infnet.imovel.repository;

import edu.infnet.imovel.model.Imovel;
import edu.infnet.imovel.model.StatusImovel;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public final class ImovelSpecifications {

    public static Specification<Imovel> comStatus(StatusImovel status) {
        return (root, query, builder) -> builder.equal(root.get("status"), status);
    }

    public static Specification<Imovel> comCidade(String cidade) {
        return (root, query, builder) -> builder.like(builder.lower(root.get("endereco").get("cidade")), "%" + cidade.toLowerCase() + "%");
    }

    public static Specification<Imovel> comValorMaximo(BigDecimal valorMaximo) {
        return (root, query, builder) -> builder.lessThanOrEqualTo(root.get("valor"), valorMaximo);
    }

    public static Specification<Imovel> comAreaMinima(Double areaMinima) {
        return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("areaM2"), areaMinima);
    }

    public static Specification<Imovel> comTipo(String tipo) {
        return (root, query, builder) -> builder.like(builder.lower(root.get("tipo")), "%" + tipo.toLowerCase() + "%");
    }
}
