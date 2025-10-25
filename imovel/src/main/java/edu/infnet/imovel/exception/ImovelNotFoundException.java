package edu.infnet.imovel.exception;

public class ImovelNotFoundException extends RuntimeException {

    public ImovelNotFoundException(Long id) {
        super("Imóvel não encontrado com id: " + id);
    }
}
