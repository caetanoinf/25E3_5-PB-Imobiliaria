package edu.infnet.imovel.exception;

public class ImovelAlreadyFavoritedException extends RuntimeException {

    public ImovelAlreadyFavoritedException(Long imovelId, String userId) {
        super("Imóvel ID " + imovelId + " já está nos favoritos do usuário " + userId);
    }
}
