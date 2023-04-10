package edu.duke.ece651.team7.server.model;

public interface Order {
    /**
     * execute different functions o forderVisitors
     * 
     * @param <T> 
     * @param visitor Differenet orderVisitor
     * @return
     */
    public <T> T accept(OrderVisitor<T> visitor);
}
