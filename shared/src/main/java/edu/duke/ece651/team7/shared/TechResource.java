package edu.duke.ece651.team7.shared;

public class TechResource extends Resource implements Comparable<TechResource>{
    public TechResource(int a){
        super(a);
    } 

    public void addResource(TechResource re){
        addResource(re.getAmount());
    }

    public void consumeResource(TechResource re){
        consumeResource(re.getAmount());
    }


    @Override
    public int compareTo(TechResource re) {
        if(amount > re.amount){
            return 1;
        }else if(amount == re.amount){
            return 0;
        }else{
            return -1;
        }
    }
}
