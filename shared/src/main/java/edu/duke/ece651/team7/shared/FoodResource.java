package edu.duke.ece651.team7.shared;

/**
 * Represents a type of Resource that specifically tracks food.
 */
public class FoodResource extends Resource implements Comparable<FoodResource> {
    public FoodResource(int a) {
        super(a);
    }

    public void addResource(FoodResource re) {
        addResource(re.getAmount());
    }

    public void consumeResource(FoodResource re) {
        consumeResource(re.getAmount());
    }

    @Override
    public int compareTo(FoodResource re) {
        if (amount > re.amount) {
            return 1;
        } else if (amount == re.amount) {
            return 0;
        } else {
            return -1;
        }
    }
}
