package dax_api.walker.utils.movement;

import net.runelite.rsb.methods.Web;
import net.runelite.rsb.wrappers.RSCharacter;
import net.runelite.rsb.wrappers.RSTile;

import java.util.ArrayList;


public class WalkingQueue {

    public static boolean isWalkingTowards(RSTile tile){
        RSTile tile1 = getWalkingTowards();
        return tile1 != null && tile1.equals(tile);
    }

    public static RSTile getWalkingTowards(){
        ArrayList<RSTile> tiles = getWalkingQueue();
        return tiles.size() > 0 && !tiles.get(0).equals(new RSTile(Web.methods.players.getMyPlayer().getLocation())) ? tiles.get(0) : null;
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
        int[] xIndex = rsCharacter.getPathX(), yIndex = rsCharacter.getPathY();
        int plane = rsCharacter.getLocation().getWorldLocation().getPlane();

        for (int i = 0; i < xIndex.length && i < yIndex.length; i++) {
            walkingQueue.add(new RSTile(xIndex[i], yIndex[i], plane, RSTile.TYPES.SCENE).toWorldTile());
        }
        return walkingQueue;
    }

}
