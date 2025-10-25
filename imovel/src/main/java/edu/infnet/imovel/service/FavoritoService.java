package edu.infnet.imovel.service;

import edu.infnet.imovel.exception.ImovelAlreadyFavoritedException;
import edu.infnet.imovel.exception.ImovelNotFoundException;
import edu.infnet.imovel.model.Favorito;
import edu.infnet.imovel.repository.FavoritoRepository;
import edu.infnet.imovel.model.Imovel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FavoritoService {

    private final FavoritoRepository favoritoRepository;
    private final ImovelService imovelService;

    @Transactional(readOnly = true)
    public List<Imovel> findFavoritesByUserId(String userId) {
        return favoritoRepository.findByUserId(userId)
                .stream()
                .map(Favorito::getImovel)
                .toList();
    }

    public void addToFavorites(String userId, Long imovelId) {
        // Verifica se o imóvel já está favoritado
        boolean alreadyFavorited = favoritoRepository.findByUserId(userId)
                .stream()
                .anyMatch(fav -> fav.getImovel().getId().equals(imovelId));

        if (alreadyFavorited) {
            throw new ImovelAlreadyFavoritedException(imovelId, userId);
        }

        // Busca o imóvel e valida se existe
        Imovel imovel = imovelService.findById(imovelId)
                .orElseThrow(() -> new ImovelNotFoundException(imovelId));

        // Cria e salva o favorito
        Favorito favorito = new Favorito();
        favorito.setUserId(userId);
        favorito.setImovel(imovel);

        favoritoRepository.save(favorito);
    }
}
