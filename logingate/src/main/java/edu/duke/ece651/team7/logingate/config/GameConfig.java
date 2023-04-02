package edu.duke.ece651.team7.logingate.config;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GameConfig {

    @Bean
    public Registry registry() throws RemoteException {
        return LocateRegistry.createRegistry(8082);
    }
}
