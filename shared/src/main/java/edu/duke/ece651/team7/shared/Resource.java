package edu.duke.ece651.team7.shared;

import java.io.Serializable;


public abstract class Resource implements Serializable, Comparable<Resource>{
    private int amount;

    public Resource(int a){
        amount = a;
    }


    public int getAmount(){
        return amount;
    }

    /**
     * increase the amount of resouce of a player
     * @param v amount of resource to add
     */
    public void addResource(int value){ 
        amount += value;
    }

    /**
     * 
     * @param v the amount of resource to consume
     * @return null if success, else error message
     */
    public String comsumeResoure(int value){
        if(amount - value< 0){
            return "Resource Error: no enough resource";
        }
        else{
            amount -= value;
            return null;
        }
    }

    @Override
    public int compareTo(Resource re) {
        if(amount > re.amount){
            return 1;
        }else if(amount == re.amount){
            return 0;
        }else{
            return -1;
        }
    }

}
