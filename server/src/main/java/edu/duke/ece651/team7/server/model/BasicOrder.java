package edu.duke.ece651.team7.server.model;

import edu.duke.ece651.team7.shared.Player;
import edu.duke.ece651.team7.shared.Territory;

import java.util.LinkedHashMap;
import java.util.Map;

import edu.duke.ece651.team7.shared.Level;

public abstract class BasicOrder implements Order{
    protected Territory src;
    protected Territory dest;
    protected Player issuer;
    protected Map<Level, Integer> units;
    protected boolean useAircraft;

    public BasicOrder(Player p, boolean ua, Territory s, Territory d, Object... u){
        issuer = p;
        useAircraft = ua;
        src = s;
        dest = d;
        units = new LinkedHashMap<Level, Integer>();
        for (int i = 0; i < u.length; i+=2) {
            Level l = (Level)u[i];
            units.put(l,(Integer)u[i+1]);
        }
    }

    public BasicOrder(Player p,  boolean ua, Territory s, Territory d, Map<Level, Integer> u){
        issuer = p;
        src = s;
        useAircraft = ua;
        dest = d;
        units = u;
    }
}
