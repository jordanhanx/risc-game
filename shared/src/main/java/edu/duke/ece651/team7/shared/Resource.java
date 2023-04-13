package edu.duke.ece651.team7.shared;

import java.io.Serializable;

/**
 * Abstract class that represents a resource, which can be added or consumed by
 * a player
 */
public abstract class Resource implements Serializable {
    protected int amount;

    public Resource(int a) {
        if (a < 0) {
            throw new IllegalArgumentException("Resource Error: cannot initialize negative amount of resource");
        }
        amount = a;
    }

    public int getAmount() {
        return amount;
    }

    /**
     * increase the amount of resouce of a player
     * 
     * @param v amount of resource to add
     */
    public void addResource(int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Resource Error: cannnot add negative amount of resource");
        }
        amount += value;
    }

    /**
     * 
     * @param v the amount of resource to consume
     * @return null if success, else error message
     */
    public void consumeResource(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Resource Error: cannot consume negative amount of resource");
        } else if (amount - value < 0) {
            throw new IllegalArgumentException("Resource Error: no enough resource");
        } else {
            amount -= value;
        }
    }
}
