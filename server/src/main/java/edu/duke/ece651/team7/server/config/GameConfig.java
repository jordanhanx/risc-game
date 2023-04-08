package edu.duke.ece651.team7.server.config;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This class provides a configuration for a RMI registry.
 */
@Configuration
public class GameConfig {
    /**
     * The port number for the RMI registry.
     */
    @Value("${rmi.registry.port}")
    int port;

    /**
     * Creates a RMI registry Bean.
     *
     * @return the RMI registry.
     * @throws RemoteException if there is an error creating the RMI registry.
     */
    @Bean
    public Registry registry() throws RemoteException {
        return LocateRegistry.createRegistry(port);
    }
}
