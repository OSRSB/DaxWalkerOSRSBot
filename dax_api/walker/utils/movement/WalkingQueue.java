package net.runelite.client.rsb.walker.dax_api.walker.utils.movement;

import net.runelite.client.rsb.methods.Web;
import net.runelite.client.rsb.wrappers.RSCharacter;
import net.runelite.client.rsb.wrappers.RSNPC;
import net.runelite.client.rsb.wrappers.RSTile;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSCharacter;
import org.tribot.api2007.types.RSTile;

import java.util.ArrayList;


public class WalkingQueue {

    public static boolean isWalkingTowards(RSTile tile){
        RSTile tile1 = getWalkingTowards();
        return tile1 != null && tile1.equals(tile);
    }

    public static RSTile getWalkingTowards(){
        ArrayList<RSTile> tiles = getWalkingQueue();
        return tiles.size() > 0 && !tiles.get(0).equals(Web.methods.players.getMyPlayer().getLocation()) ? tiles.get(0) : null;
    }

    public static ArrayList<RSTile> getWalkingQueue(){
        return getWalkingQueue(Web.methods.players.getMyPlayer());
    }

    public static RSTile getWalkingTowards(RSCharacter rsCharacter){
        ArrayList<RSTile> tiles = getWalkingQueue(rsCharacter);
        return tiles.size() > 0 && !tiles.get(0).equals(rsCharacter.getLocation()) ? tiles.get(0) : null;
    }

    public static ArrayList<RSTile> getWalkingQueue(RSCharacter rsCharacter){
        ArrayList<RSTile> walkingQueue = new ArrayList<>();
        if (rsCharacter == null){
            return walkingQueue;
        }
        int[] xIndex = ((RSNPC) rsCharacter)
                //..getWalkingQueueX(), yIndex = rsCharacter.getWalkingQueueY();
        int plane = rsCharacter.getPosition().getPlane();

        for (int i = 0; i < xIndex.length && i < yIndex.length; i++) {
            walkingQueue.add(new RSTile(xIndex[i], yIndex[i], plane, RSTile.TYPES.LOCAL).toWorldTile());
        }
        return walkingQueue;
    }

}
