package net.runelite.client.rsb.walker.dax_api.walker.utils.generator;

import org.tribot.api2007.Game;
import org.tribot.api2007.PathFinding;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.WalkerTile;
import org.tribot.util.Util;
import scripts.dax_api.walker_engine.local_pathfinding.AStarNode;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class CollisionFileMaker {

    public static void getCollisionData(){
        try {
            int[][] collisionData = PathFinding.getCollisionData();
            if(collisionData == null)
                return;
            int baseX = Game.getBaseX();
            int baseY = Game.getBaseY();
            int baseZ = new WalkerTile(Web.methods.players.getMyPlayer().getLocation()).getWorldLocation().getPlane();

            File file = new File(Util.getWorkingDirectory().getAbsolutePath() + File.separator + baseX + "x" + baseY + "x" + baseZ + ".cdata");
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            for (int x = 0; x < collisionData.length; x++) {
                for (int y = 0; y < collisionData[x].length; y++) {
                    int flag = collisionData[x][y];
                    WalkerTile tile = new WalkerTile(x, y, baseZ, WalkerTile.TYPES.LOCAL).toWorldTile();
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
