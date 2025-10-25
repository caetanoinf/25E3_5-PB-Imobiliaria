package edu.infnet.imovel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ImovelServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ImovelServiceApplication.class, args);
    }
}
