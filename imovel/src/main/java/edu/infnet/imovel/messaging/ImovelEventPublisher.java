package edu.infnet.imovel.messaging;

import edu.infnet.imovel.config.RabbitMQConfig;
import edu.infnet.imovel.dto.ImovelCriadoEvent;
import edu.infnet.imovel.model.Imovel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImovelEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publicarImovelCriado(Imovel imovel) {
        ImovelCriadoEvent event = new ImovelCriadoEvent(
                imovel.getId(),
                imovel.getTipo(),
                imovel.getEndereco().getCidade(),
                imovel.getEndereco().getEstado(),
                imovel.getAreaM2(),
                imovel.getValor(),
                imovel.getStatus().toString()
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY,
                event
        );

        log.info("Evento de imóvel criado publicado: ID={}, Tipo={}, Cidade={}",
                event.getId(), event.getTipo(), event.getCidade());
    }
}
