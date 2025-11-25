package edu.infnet.user.messaging;

import edu.infnet.user.dto.event.ImovelCriadoEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ImovelEventListener {

    @RabbitListener(queues = "imovel.criado.queue")
    public void handleImovelCriado(ImovelCriadoEvent event) {
        log.info(String.format("Evento recebido: Imóvel %d cadastrado - %s em %s/%s por R$ %.2f",
                event.getId(), event.getTipo(), event.getCidade(), event.getEstado(), event.getValor()));
        log.info("Notificando usuários interessados...");
    }
}
