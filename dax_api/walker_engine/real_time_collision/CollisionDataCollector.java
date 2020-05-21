package net.runelite.client.rsb.walker.dax_api.walker_engine.real_time_collision;

import net.runelite.api.coords.WorldPoint;
import net.runelite.client.rsb.methods.Web;
import net.runelite.client.rsb.wrappers.RSTile;


public class CollisionDataCollector {

    public static void generateRealTimeCollision(){
        RealTimeCollisionTile.clearMemory();

        RSTile playerPosition = Web.methods.players.getMyPlayer().getLocation();
        int[][] collisionData = Web.methods.walking.getCollisionFlags(playerPosition.getWorldLocation().getPlane());

        if (collisionData == null) {
            return;
        }

        for (int x = 0; x < collisionData.length; x++) {
            for (int y = 0; y < collisionData[x].length; y++) {
                RSTile worldTile = new RSTile(WorldPoint.fromScene(Web.methods.client, x, y, playerPosition.getWorldLocation().getPlane()));
                RealTimeCollisionTile.create(worldTile.getWorldLocation().getX(), worldTile.getWorldLocation().getY(), worldTile.getWorldLocation().getPlane(), collisionData[x][y]);
            }
        }
    }

    public static void updateRealTimeCollision(){
        RSTile playerPosition = Web.methods.players.getMyPlayer().getLocation();
        int[][] collisionData = Web.methods.walking.getCollisionFlags(playerPosition.getWorldLocation().getPlane());
        if(collisionData == null)
            return;
        for (int x = 0; x < collisionData.length; x++) {
            for (int y = 0; y < collisionData[x].length; x++) {
                //RSTile localTile = new RSTile(i, j, playerPosition.getWorldLocation().getPlane(), RSTile.TYPES.LOCAL);
                RSTile worldTile = new RSTile(WorldPoint.fromScene(Web.methods.client, x, y, playerPosition.getWorldLocation().getPlane()));
                RealTimeCollisionTile realTimeCollisionTile = RealTimeCollisionTile.get(worldTile.getWorldLocation().getX(),
                        worldTile.getWorldLocation().getY(), worldTile.getWorldLocation().getPlane());
                if (realTimeCollisionTile != null){
                    realTimeCollisionTile.setCollisionData(collisionData[x][y]);
                } else {
                    RealTimeCollisionTile.create(worldTile.getWorldLocation().getX(), worldTile.getWorldLocation().getY(), worldTile.getWorldLocation().getPlane(),
                            collisionData[x][y]);
                }
            }
        }
    }

}
