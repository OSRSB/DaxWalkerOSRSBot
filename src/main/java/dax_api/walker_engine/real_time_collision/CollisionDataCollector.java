package dax_api.walker_engine.real_time_collision;

import net.runelite.rsb.methods.Web;
import net.runelite.rsb.wrappers.RSTile;


public class CollisionDataCollector {

    public static void generateRealTimeCollision(){
        RealTimeCollisionTile.clearMemory();

        RSTile playerPosition = new RSTile(new RSTile(Web.methods.players.getMyPlayer().getLocation()));
        int[][] collisionData = Web.methods.client.getCollisionMaps()[Web.methods.game.getPlane()].getFlags();

        if (collisionData == null) {
            return;
        }

        for (int i = 0; i < collisionData.length; i++) {
            for (int j = 0; j < collisionData[i].length; j++) {
                RSTile localTile = new RSTile(i, j, playerPosition.getPlane(), RSTile.TYPES.SCENE);
                RSTile worldTile = localTile.toWorldTile();
                RealTimeCollisionTile.create(worldTile.getX(), worldTile.getY(), worldTile.getPlane(), collisionData[i][j]);
            }
        }
    }

    public static void updateRealTimeCollision(){
        RSTile playerPosition = new RSTile(new RSTile(Web.methods.players.getMyPlayer().getLocation()));
        int[][] collisionData = Web.methods.client.getCollisionMaps()[Web.methods.game.getPlane()].getFlags();
        if(collisionData == null)
            return;
        for (int i = 0; i < collisionData.length; i++) {
            for (int j = 0; j < collisionData[i].length; j++) {
                RSTile localTile = new RSTile(i, j, playerPosition.getPlane(), RSTile.TYPES.SCENE);
                RSTile worldTile = localTile.toWorldTile();
                RealTimeCollisionTile realTimeCollisionTile = RealTimeCollisionTile.get(worldTile.getX(), worldTile.getY(), worldTile.getPlane());
                if (realTimeCollisionTile != null){
                    realTimeCollisionTile.setCollisionData(collisionData[i][j]);
                } else {
                    RealTimeCollisionTile.create(worldTile.getX(), worldTile.getY(), worldTile.getPlane(), collisionData[i][j]);
                }
            }
        }
    }

}
