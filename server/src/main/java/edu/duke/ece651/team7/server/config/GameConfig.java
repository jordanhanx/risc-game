package edu.duke.ece651.team7.server.config;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GameConfig {

    @Value("${rmi.registry.port}")
    int port;

    @Bean
    public Registry registry() throws RemoteException {
        return LocateRegistry.createRegistry(port);
    }
}
