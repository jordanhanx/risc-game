package edu.duke.ece651.team7.shared;

public class ResourceStorage {
    private int amount;

    public ResourceStorage(){
        amount = 0;
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
    public String comsumeResoure(int  value){
        if(amount - value< 0){
            return "Resource Error: no enough resource";
        }
        else{
            amount -= value;
            return null;
        }
    }

}
