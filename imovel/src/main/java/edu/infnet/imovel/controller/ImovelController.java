package edu.infnet.imovel.controller;

import edu.infnet.imovel.exception.ImovelAlreadyFavoritedException;
import edu.infnet.imovel.dto.ImovelCreateDto;
import edu.infnet.imovel.exception.ImovelNotFoundException;
import edu.infnet.imovel.service.FavoritoService;
import edu.infnet.imovel.dto.ImovelFilter;
import edu.infnet.imovel.model.Imovel;
import edu.infnet.imovel.model.StatusImovel;
import edu.infnet.imovel.repository.ImovelRepository;
import edu.infnet.imovel.service.ImovelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/imoveis")
@RequiredArgsConstructor
public class ImovelController {

    private final ImovelService imovelService;
    private final ImovelRepository imovelRepository;
    private final FavoritoService favoritoService;

    @GetMapping
    public Page<Imovel> findAll(@PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return imovelService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Imovel> findById(@PathVariable Long id) {
        return imovelService.findById(id)
            .map(imovel -> ResponseEntity.ok(imovel))
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CORRETOR')")
    public ResponseEntity<Imovel> create(@RequestBody @Valid ImovelCreateDto imovelDto) {
        Imovel savedImovel = imovelService.create(imovelDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedImovel);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CORRETOR')")
    public ResponseEntity<Imovel> update(@PathVariable Long id, @RequestBody Imovel imovel) {
        try {
            Imovel updatedImovel = imovelService.update(id, imovel);
            return ResponseEntity.ok(updatedImovel);
        } catch (ImovelNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            imovelService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (ImovelNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/status/{status}")
    public List<Imovel> findByStatus(@PathVariable StatusImovel status) {
        return imovelRepository.findByStatus(status);
    }

    @GetMapping("/favoritados")
    @PreAuthorize("isAuthenticated()")
    public List<Imovel> findFavorites(@RequestHeader("User-ID") String userId) {
        return favoritoService.findFavoritesByUserId(userId);
    }

    @PostMapping("/{id}/favoritar")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> addToFavorites(@PathVariable Long id, @RequestHeader("User-ID") String userId) {
        try {
            favoritoService.addToFavorites(userId, id);
            return ResponseEntity.ok("Imóvel favoritado com sucesso");
        } catch (ImovelAlreadyFavoritedException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (ImovelNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/buscar")
    public List<Imovel> searchWithFilters(@ModelAttribute ImovelFilter filter) {
        return imovelService.search(filter);
    }

    @GetMapping("/{id}/historico")
    @PreAuthorize("hasAnyRole('ADMIN', 'CORRETOR')")
    public ResponseEntity<List<Object[]>> getHistorico(@PathVariable Long id) {
        List<Object[]> historico = imovelService.findHistorico(id);
        return ResponseEntity.ok(historico);
    }
}
