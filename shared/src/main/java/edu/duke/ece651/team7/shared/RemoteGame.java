package edu.duke.ece651.team7.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The RemoteGame interface provides methods to interact with a game session in
 * a remote server.
 * This interface defines remote methods that can be invoked remotely by the
 * clients of the game.
 */
public interface RemoteGame extends Remote {

        /**
         * An enumeration representing the different phases of the game.
         */
        public enum GamePhase {
                PICK_GROUP, PLACE_UNITS, PLAY_GAME
        }

        /**
         * Returns the current game phase for the specified user.
         * 
         * @param username the name of the user
         * @return the current game phase
         * @throws RemoteException if a remote communication error occurs
         */
        public GamePhase getGamePhase(String username) throws RemoteException;

        /**
         * Returns the number of initial units for the game.
         * 
         * @return the number of initial units
         * @throws RemoteException if a remote communication error occurs
         */
        public int getGameInitUnits() throws RemoteException;

        /**
         * Returns the current state of the game map.
         * 
         * @return the current game map
         * @throws RemoteException if a remote communication error occurs
         */
        public GameMap getGameMap() throws RemoteException;

        /**
         * Returns the current player status of the specified player.
         * 
         * @param username the name of the player
         * @return the current player status
         * @throws RemoteException if a remote communication error occurs
         */
        public Player getSelfStatus(String username) throws RemoteException;

        /**
         * Attempts to pick a territory group with the specified name for the specified
         * user.
         * 
         * @param username  the name of the user
         * @param groupName the name of the territory group to pick
         * @return an error message if the operation fails, null otherwise
         * @throws RemoteException if a remote communication error occurs
         */
        public String tryPickTerritoryGroupByName(String username, String groupName) throws RemoteException;

        /**
         * Attempts to place the specified number of units on the specified territory
         * for the specified user.
         * 
         * @param username  the name of the user
         * @param territory the name of the territory to place units on
         * @param units     the number of units to place
         * @return an error message if the operation fails, null otherwise
         * @throws RemoteException if a remote communication error occurs
         */
        public String tryPlaceUnitsOn(String username, String territory, int units) throws RemoteException;

        /**
         * Attempts to register a remote client with the specified username to receive
         * updates from the server.
         * 
         * @param username the name of the client user
         * @param client   the remote client to register
         * @return an error message if the operation fails, null otherwise
         * @throws RemoteException if a remote communication error occurs
         */
        public String tryRegisterClient(String username, RemoteClient client) throws RemoteException;

        /**
         * Attempts to move the specified number of units from the specified source
         * territory to the specified destination territory with the units of specified
         * level.
         * 
         * @param username the name of the user
         * @param useAP    whether to use Aircraft 
         * @param src      the name of the source territory
         * @param dest     the name of the destination territory
         * @param level    the level of the unit
         * @param units    the number of units to move
         * @return an error message if the operation fails, null otherwise
         * @throws RemoteException if a remote communication error occurs
         */
        public String tryMoveOrder(String username, Boolean useAP, String src, String dest, int level, int units)
                        throws RemoteException;

        /**
         * Tries to execute an attack order from the specified username on the
         * game map, from the source territory to the destination territory with
         * units of specified level.
         * 
         * @param username the username of the player who initiates the attack order
         * @param useAP    whether to use Aircraft 
         * @param numBomb  the number of bombs the attack order use
         * @param src      the name of the source territory
         * @param dest     the name of the destination territory
         * @param level    the attack level
         * @param units    the number of units used for the attack
         * @return null if the attack is successful, or an error message string if the
         *         attack is not valid
         * @throws RemoteException if a remote communication error occurs during the
         *                         method call
         */
        public String tryAttackOrder(String username, Boolean useAP, int numBomb, String src, String dest, int level, int units)
                        throws RemoteException;

        /**
         * Attemps to upgrade the given number of units with specific level to another
         * level from the target territory owned by the user of the given username
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
         * 
         * @param username the username of the player attempting to alliance
         * @param allianceName the player want want alliance with
         * @return a message indicating success or failure of the alliance attempt
         * @throws RemoteException
         */
        public String tryAllianceOrder(String username, String allianceName) throws RemoteException;


        /**
         * 
         * @param username the username of the player attempting to do ManufactureOrder
         * @param isBomb   true if making bomb, false if making aircraft
         * @param amount   number of weapon want to manufacture
         * @return         a message indicating success or failure of the alliance attempt
         * @throws RemoteException
         */
        public String tryManufactureOrder(String username, boolean isBomb, int amount) throws RemoteException;

        /**
         * Instructs the server to commit the orders for the specified player.
         * Blocks until all players have submitted their orders or the timeout period
         * expires.
         * 
         * @param username the username of the player to commit the orders for
         * @throws RemoteException if there is an issue with remote invocation
         */
        public String doCommitOrder(String username) throws RemoteException;

        public String chatToAll(String username, String msg) throws RemoteException;

        public String chatToPlayer(String username, String toPlayer, String msg) throws RemoteException;
}
