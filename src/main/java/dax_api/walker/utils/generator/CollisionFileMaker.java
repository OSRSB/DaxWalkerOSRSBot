package dax_api.walker.utils.generator;

import dax_api.walker_engine.local_pathfinding.AStarNode;
import net.runelite.rsb.internal.globval.GlobalConfiguration;
import net.runelite.rsb.methods.Web;
import net.runelite.rsb.wrappers.subwrap.WalkerTile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class CollisionFileMaker {

    public static void getCollisionData(){
        try {
            int[][] collisionData = Web.methods.client.getCollisionMaps()[Web.methods.game.getPlane()].getFlags();
            if(collisionData == null)
                return;
            int baseX = Web.methods.game.getBaseX();
            int baseY = Web.methods.game.getBaseY();
            int baseZ = new WalkerTile(Web.methods.players.getMyPlayer().getLocation()).getWorldLocation().getPlane();

            File file = new File(GlobalConfiguration.Paths.getSettingsDirectory() + File.separator + baseX + "x" + baseY + "x" + baseZ + ".cdata");
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            for (int x = 0; x < collisionData.length; x++) {
                for (int y = 0; y < collisionData[x].length; y++) {
                    int flag = collisionData[x][y];
                    WalkerTile tile = new WalkerTile(x, y, baseZ, WalkerTile.TYPES.SCENE).toWorldTile();
                    CollisionTile collisionTile = new CollisionTile(
                            tile.getX(), tile.getY(), tile.getPlane(),
                            AStarNode.blockedNorth(flag),
                            AStarNode.blockedEast(flag),
                            AStarNode.blockedSouth(flag),
                            AStarNode.blockedWest(flag),
                            !AStarNode.isWalkable(flag),
                            false,
                            !AStarNode.isInitialized(flag));
                    bufferedWriter.write(collisionTile.toString());
                    bufferedWriter.newLine();
                }
            }
            bufferedWriter.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}
