package edu.duke.ece651.team7.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This interface defines remote methods that can be invoked remotely by the
 * clients of the game.
 */
public interface RemoteGame extends Remote {

        public enum GamePhase {
                PICK_GROUP, PLACE_UNITS, PLAY_GAME
        }

        public GamePhase getGamePhase(String username) throws RemoteException;

        public int getGameInitUnits() throws RemoteException;

        public GameMap getGameMap() throws RemoteException;

        public Player getSelfStatus(String username) throws RemoteException;

        public String tryPickTerritoryGroupByName(String username, String groupName) throws RemoteException;

        public String tryPlaceUnitsOn(String username, String territory, int units) throws RemoteException;

        public String tryRegisterClient(String username, RemoteClient client) throws RemoteException;

        public String tryMoveOrder(String username, String src, String dest, int level, int units)
                        throws RemoteException;

        public String tryAttackOrder(String username, String src, String dest, int level, int units)
                        throws RemoteException;

        /**
         * Attemps to upgrade the given number of units with specific level to another
         * level from the target territory
         * owned by the user of the given username
         * 
         * @param username  the username of the player attempting to upgrade
         * @param target    the target territory to upgrade units
         * @param fromlevel the target unit level
         * @param tolevel   the upgraded level
         * @param units     number of units to upgrade
         * @return a message indicating success or failure of the upgrade attempt
         * @throws RemoteException if there is an issue with remote invocation
         */
        public String tryUpgradeOrder(String username, String target, int fromlevel, int tolevel, int units)
                        throws RemoteException;

        /**
         * Attempts to upgrade the maximum tech level of the player with name username
         * 
         * @param username the username of the player attempting to fo tech research
         * @return a message indicating success or failure of the research attempt
         * @throws RemoteException if there is an issue with remote invocation
         */
        public String tryResearchOrder(String username) throws RemoteException;

        /**
         * Instructs the server to commit the orders for the specified player.
         * Blocks until all players have submitted their orders or the timeout period
         * expires.
         * 
         * @param username the username of the player to commit the orders for
         * @throws RemoteException      if there is an issue with remote invocation
         * @throws InterruptedException if the current thread is interrupted while
         *                              waiting
         */
        public String doCommitOrder(String username) throws RemoteException, InterruptedException;
}
