package net.runelite.client.rsb.walker.dax_api.walker_engine.local_pathfinding;

import net.runelite.client.rsb.methods.Web;
import net.runelite.client.rsb.walker.dax_api.shared.PathFindingNode;
import net.runelite.client.rsb.walker.dax_api.walker_engine.bfs.BFS;
import net.runelite.client.rsb.walker.dax_api.walker_engine.real_time_collision.CollisionDataCollector;
import net.runelite.client.rsb.walker.dax_api.walker_engine.real_time_collision.RealTimeCollisionTile;
import net.runelite.client.rsb.wrappers.RSTile;

import java.util.ArrayList;


public class PathAnalyzer {

    public static RealTimeCollisionTile closestToPlayer = null, furthestReachable = null;

    public static RealTimeCollisionTile closestTileInPathToPlayer(ArrayList<RSTile> path) {
        CollisionDataCollector.generateRealTimeCollision();
        final RSTile playerPosition = Web.methods.players.getMyPlayer().getLocation();
        closestToPlayer = (RealTimeCollisionTile) BFS.bfsClosestToPath(path, RealTimeCollisionTile.get(playerPosition.getWorldLocation().getX(), playerPosition.getWorldLocation().getY(),
                playerPosition.getWorldLocation().getPlane()));
        return closestToPlayer;
    }


    public static DestinationDetails furthestReachableTile(ArrayList<RSTile> path){
        return furthestReachableTile(path, closestTileInPathToPlayer(path));
    }


    public static DestinationDetails furthestReachableTile(ArrayList<RSTile> path, PathFindingNode currentPosition){
        if (path == null || currentPosition == null){
            System.out.println("PathAnalyzer attempt to find closest tile in path: " + currentPosition + " " + path);
            return null;
        }
        outside:
        for (int i = path.indexOf(currentPosition.getRSTile()); i < path.size() && i >= 0; i++) {
            RSTile currentNode = path.get(i);
            RealTimeCollisionTile current = RealTimeCollisionTile.get(currentNode.getWorldLocation().getX(), currentNode.getWorldLocation().getY(), currentNode.getWorldLocation().getPlane());
            if (current == null){
                return null;
            }
            if (i + 1 >= path.size()){
                return new DestinationDetails(PathState.END_OF_PATH, current);
            }
            RSTile nextNode = path.get(i + 1);
            if(!isLoaded(nextNode) && Web.methods.calc.tileOnScreen(nextNode)){
                return new DestinationDetails(PathState.FURTHEST_CLICKABLE_TILE, current);
            }
            RealTimeCollisionTile next = RealTimeCollisionTile.get(nextNode.getWorldLocation().getX(), nextNode.getWorldLocation().getY(), nextNode.getWorldLocation().getPlane());
            Direction direction = directionTo(current.getRSTile(), nextNode);
            if (direction == Direction.UNKNOWN){
                furthestReachable = current;
                return new DestinationDetails(PathState.DISCONNECTED_PATH, current, nextNode.getWorldLocation().getX(), nextNode.getWorldLocation().getY(), nextNode.getWorldLocation().getPlane());
            }
            if (!direction.confirmTileMovable(RealTimeCollisionTile.get(current.getX(), current.getY(), current.getZ()))){

                for (int j = 2; j < 6 && j + i < path.size(); j++) {
                    RSTile nextInPath = path.get(i + j);
                    RealTimeCollisionTile nextInPathCollision = RealTimeCollisionTile.get(nextInPath.getWorldLocation().getX(), nextInPath.getWorldLocation().getY(), nextInPath.getWorldLocation().getPlane());
                    if (nextInPathCollision != null && nextInPathCollision.isWalkable()){
                        if (BFS.isReachable(current, nextInPathCollision, 360)) {
                            i += j-1;
                            continue outside;
                        }
                    }
                }

                furthestReachable = current;
                if (next != null) {
                    return new DestinationDetails(PathState.OBJECT_BLOCKING, current, next);
                }
                return new DestinationDetails(PathState.OBJECT_BLOCKING, current, nextNode.getWorldLocation().getX(), nextNode.getWorldLocation().getY(), nextNode.getWorldLocation().getPlane());
            }
            if (Web.methods.calc.tileOnMap(new RSTile(nextNode.getWorldLocation().getX(), nextNode.getWorldLocation().getY(), nextNode.getWorldLocation().getPlane()))){
                furthestReachable = current;
                if (next != null) {
                    return new DestinationDetails(PathState.FURTHEST_CLICKABLE_TILE, current, next);
                }
                return new DestinationDetails(
		                PathState.FURTHEST_CLICKABLE_TILE, current, nextNode.getWorldLocation().getX(), nextNode.getWorldLocation().getY(), nextNode.getWorldLocation().getPlane());
            }
        }
        return null;
    }

    public static Direction directionTo(RSTile fromNode, RSTile toNode){
        if (fromNode.getWorldLocation().getPlane() != toNode.getWorldLocation().getPlane()){
            return Direction.UNKNOWN;
        }
        for (Direction direction : Direction.values()){
            if (fromNode.getWorldLocation().getX() + direction.x == toNode.getWorldLocation().getX() && fromNode.getWorldLocation().getY() + direction.y == toNode.getWorldLocation().getY()){
                return direction;
            }
        }
        return Direction.UNKNOWN;
    }

    public static class DestinationDetails {
        private PathState state;
        private RealTimeCollisionTile destination, nextTile;
        private int assumedX, assumedY, assumedZ;

        private DestinationDetails(PathState state, RealTimeCollisionTile destination){
            this.state = state;
            this.destination = destination;
            this.assumedX = -1;
            this.assumedY = -1;
            this.assumedZ = -1;
        }
        private DestinationDetails(PathState state, RealTimeCollisionTile destination, RealTimeCollisionTile nextTile){
            this.state = state;
            this.destination = destination;
            this.nextTile = nextTile;
            this.assumedX = nextTile.getX();
            this.assumedY = nextTile.getY();
            this.assumedZ = nextTile.getZ();

        }
        private DestinationDetails(PathState state, RealTimeCollisionTile destination, int x, int y, int z){
            this.state = state;
            this.destination = destination;
            this.assumedX = x;
            this.assumedY = y;
            this.assumedZ = z;
        }

        public PathState getState() {
            return state;
        }

        public RSTile getAssumed(){
            return new RSTile(assumedX, assumedY, assumedZ);
        }


        public RealTimeCollisionTile getDestination() {
            return destination;
        }

        public RealTimeCollisionTile getNextTile() {
            return nextTile;
        }

        public int getAssumedX() {
            return assumedX;
        }

        public int getAssumedY() {
            return assumedY;
        }

        public int getAssumedZ() {
            return assumedZ;
        }

        @Override
        public String toString(){
            String debug = "PATH_DEBUG[ ";
            if (state == PathState.END_OF_PATH){
                debug += state;
            } else {
                if (destination != null){
                    debug += (destination.getX() + ", " + destination.getY() + ", " + destination.getZ());
                } else {
                    debug += null;
                }
                debug += ") -> " + state + " -> (";
                if (nextTile != null){
                    debug += (nextTile.getX() + ", " + nextTile.getY() + ", " + nextTile.getZ());
                } else {
                    debug += null + " [" + assumedX + ", " + assumedY + ", " + assumedZ + "] ";
                }
                debug += ")";
            }
            debug += " ]";
            return debug;
        }
    }

    public enum PathState {
        FURTHEST_CLICKABLE_TILE,
        DISCONNECTED_PATH,
        OBJECT_BLOCKING,
        END_OF_PATH
    }

    private enum Direction {
        NORTH (0, 1),
        EAST (1, 0),
        SOUTH (0, -1),
        WEST (-1, 0),
        NORTH_EAST (1, 1),
        SOUTH_EAST (1, -1),
        NORTH_WEST (-1, 1),
        SOUTH_WEST (-1, -1),
        SAME_TILE (0, 0),
        UNKNOWN (104, 104);

        int x, y;

        Direction(int x, int y){
            this.x = x;
            this.y = y;
        }

        boolean confirmTileMovable(RealTimeCollisionTile realTimeCollisionTile){
            if (this == SAME_TILE){
                return true;
            }
            RealTimeCollisionTile destination = RealTimeCollisionTile.get(realTimeCollisionTile.getX() + this.x, realTimeCollisionTile.getY() + this.y, realTimeCollisionTile.getZ());
            if (destination == null){
                return false;
            }
            if (realTimeCollisionTile.getNeighbors().contains(destination)){
                return true;
            }
            return BFS.isReachable(realTimeCollisionTile, destination, 225);
        }
    }

    private static boolean isLoaded(RSTile tile){
        return tile.getLocalLocation(Web.methods).getX() >= 0 && tile.getLocalLocation(Web.methods).getX() < 104 && tile.getLocalLocation(Web.methods).getY() >= 0 && tile.getLocalLocation(Web.methods).getY() < 104;
    }

}
