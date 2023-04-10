package edu.duke.ece651.team7.client.Model;

public class UserSession {

    private static UserSession obj = new UserSession();

    private String username;
    private String session;

    private UserSession() {
        this.username = null;
        this.session = null;
    }

    public static UserSession getInstance() {
        return obj;
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
