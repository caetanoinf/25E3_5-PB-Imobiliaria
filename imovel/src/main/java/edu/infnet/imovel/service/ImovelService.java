package edu.infnet.imovel.service;

import edu.infnet.imovel.dto.ImovelCreateDto;
import edu.infnet.imovel.dto.ImovelFilter;
import edu.infnet.imovel.mapper.ImovelMapper;
import edu.infnet.imovel.exception.ImovelNotFoundException;
import edu.infnet.imovel.model.Imovel;
import edu.infnet.imovel.repository.ImovelRepository;
import edu.infnet.imovel.repository.ImovelSpecifications;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ImovelService {

    private final ImovelRepository imovelRepository;
    private final EntityManager entityManager;
    private final ImovelMapper imovelMapper;

    public Page<Imovel> findAll(Pageable pageable) {
        return imovelRepository.findAll(pageable);
    }

    public Optional<Imovel> findById(Long id) {
        return imovelRepository.findById(id);
    }

    public Imovel create(ImovelCreateDto imovelDto) {
        Imovel imovel = imovelMapper.toEntity(imovelDto);
        return imovelRepository.save(imovel);
    }

    public List<Imovel> search(ImovelFilter filter) {
        Specification<Imovel> spec = Specification.where(null);

        if (filter.getStatus() != null) {
            spec = spec.and(ImovelSpecifications.comStatus(filter.getStatus()));
        }
        if (filter.getCidade() != null && !filter.getCidade().trim().isEmpty()) {
            spec = spec.and(ImovelSpecifications.comCidade(filter.getCidade()));
        }
        if (filter.getValorMaximo() != null) {
            spec = spec.and(ImovelSpecifications.comValorMaximo(filter.getValorMaximo()));
        }
        if (filter.getAreaMinima() != null) {
            spec = spec.and(ImovelSpecifications.comAreaMinima(filter.getAreaMinima()));
        }
        if (filter.getTipo() != null && !filter.getTipo().trim().isEmpty()) {
            spec = spec.and(ImovelSpecifications.comTipo(filter.getTipo()));
        }

        return imovelRepository.findAll(spec);
    }

    public Imovel update(Long id, Imovel imovelAtualizado) {
        return imovelRepository.findById(id)
            .map(imovel -> {
                imovel.setTipo(imovelAtualizado.getTipo());
                imovel.setEndereco(imovelAtualizado.getEndereco());
                imovel.setAreaM2(imovelAtualizado.getAreaM2());
                imovel.setValor(imovelAtualizado.getValor());
                imovel.setStatus(imovelAtualizado.getStatus());
                imovel.setDescricao(imovelAtualizado.getDescricao());

                return imovelRepository.save(imovel);
            })
            .orElseThrow(() -> new ImovelNotFoundException(id));
    }

    public void deleteById(Long id) {
        if (!imovelRepository.existsById(id)) {
            throw new ImovelNotFoundException(id);
        }
        imovelRepository.deleteById(id);
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> findHistorico(Long imovelId) {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        return auditReader.createQuery()
                .forRevisionsOfEntity(Imovel.class, false, true)
                .add(AuditEntity.id().eq(imovelId))
                .getResultList();
    }
}
