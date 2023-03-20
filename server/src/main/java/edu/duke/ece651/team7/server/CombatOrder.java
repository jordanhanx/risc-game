// package edu.duke.ece651.team7.server;

// import edu.duke.ece651.team7.shared.Player;
// import edu.duke.ece651.team7.shared.Territory;

// public class CombatOrder extends Order{

//     public CombatOrder(Player p, Territory d, int u) {
//         super(p, d, u);
//     }

//     public CombatOrder(AttackOrder o){
//         super(o.getPlayer(), o.getDest(), o.getUnits());
//     }

//     @Override
//     public boolean equals(Object o){
//         if(o != null && o.getClass().equals(getClass())){
//             CombatOrder other = (CombatOrder) o;
//             return issuer.equals(other.getPlayer()) && dest.equals(other.getDest()) && units == other.getUnits();
//         }else{
//             return false;
//         }
//     }
    
// }
