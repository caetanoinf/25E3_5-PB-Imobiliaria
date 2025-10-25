package edu.infnet.imovel.repository;

import edu.infnet.imovel.model.Imovel;
import edu.infnet.imovel.model.StatusImovel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ImovelRepository extends JpaRepository<Imovel, Long>, JpaSpecificationExecutor<Imovel> {

    List<Imovel> findByStatus(StatusImovel status);

    List<Imovel> findByValorBetween(BigDecimal valorMinimo, BigDecimal valorMaximo);

    List<Imovel> findByEnderecoCidade(String cidade);

    List<Imovel> findByEnderecoEstado(String estado);

    @Query("SELECT i FROM Imovel i WHERE i.areaM2 >= :areaMinima")
    List<Imovel> findByAreaMinima(@Param("areaMinima") Double areaMinima);

    @Query("SELECT i FROM Imovel i WHERE i.status = :status AND i.endereco.cidade = :cidade")
    List<Imovel> findByStatusAndCidade(@Param("status") StatusImovel status, @Param("cidade") String cidade);
}
