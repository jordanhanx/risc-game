package edu.duke.ece651.team7.server.model;

public interface Order {
    public <T> T accept(OrderVisitor<T> visitor);
}
