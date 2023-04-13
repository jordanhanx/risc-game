package edu.duke.ece651.team7.client.model;

/**
 * This class represents a user information and follows the Singleton design
 * pattern.
 * The instance is created and maintained within the class itself and can be
 * accessed through the getInstance() method.
 */
public class UserSession {

    private static UserSession obj = new UserSession();

    private String host;
    private String port;
    private String username;
    private String session;

    private UserSession() {
        this.host = null;
        this.port = null;
        this.username = null;
        this.session = null;
    }

    public static UserSession getInstance() {
        return obj;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

}
