package net.runelite.client.rsb.walker.dax_api.walker_engine.local_pathfinding;


import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.rsb.methods.Web;
import net.runelite.client.rsb.walker.dax_api.Positionable;
import net.runelite.client.rsb.walker.dax_api.shared.helpers.BankHelper;
import net.runelite.client.rsb.wrappers.RSTile;

import java.util.*;

public class Reachable {

    private RSTile[][] map;

    /**
     * Generates reachable map from player position
     */
    public Reachable() {
        this(null);
    }

    private Reachable(RSTile homeTile) {
        map = generateMap(homeTile != null ? homeTile : Web.methods.players.getMyPlayer().getLocation());
    }

    public boolean canReach(RSTile position) {
        RSTile playerPosition = Web.methods.players.getMyPlayer().getLocation();
        if (playerPosition.getWorldLocation().getX() == position.getWorldLocation().getX() && playerPosition.getWorldLocation().getY() == position.getWorldLocation().getY()) {
            return true;
        }
        return getParent(new Positionable(position)) != null;
    }

    public boolean canReach(int x, int y) {
        RSTile playerPosition = Web.methods.players.getMyPlayer().getLocation();
        if (playerPosition.getWorldLocation().getX() == x && playerPosition.getWorldLocation().getY() == y) {
            return true;
        }
        RSTile position = new RSTile(x, y);
        return getParent(new Positionable(position)) != null;
    }

    public RSTile closestTile(Collection<RSTile> tiles) {
        RSTile closest = null;
        double closestDistance = Integer.MAX_VALUE;
        RSTile playerPosition = Web.methods.players.getMyPlayer().getLocation();
        for (RSTile positionable : tiles) {
            double distance = Web.methods.calc.distanceBetween(playerPosition, positionable);
            if (distance < closestDistance) {
                closestDistance = distance;
                closest = positionable;
            }
        }
        return closest;
    }

    /**
     * @param x
     * @param y
     * @return parent tile of x and y through BFS.
     */
    public RSTile getParent(int x, int y) {
        RSTile position = new RSTile(x, y);
        return getParent(new Positionable(position));
    }

    public RSTile getParent(Positionable positionable) {
        RSTile tile = positionable.getPosition();
        int x = tile.getLocalLocation(Web.methods).getX(), y = tile.getLocalLocation(Web.methods).getY();
        if (x < 0 || y < 0) {
            return null;
        }
        if (x >= 104 || y >= 104 || x >= map.length || y >= map[x].length){
            return null;
        }
        return map[x][y];
    }

    /**
     * @param x
     * @param y
     * @return Distance to tile. Max integer value if unreachable. Does not account for positionable behind doors
     */
    public int getDistance(int x, int y) {
        RSTile position = new RSTile(x, y);
        return getDistance(new Positionable(position));
    }

    /**
     * @param positionable
     * @return path to tile. Does not account for positionable behind doors
     */
    public ArrayList<RSTile> getPath(Positionable positionable) {
        int x = positionable.getPosition().getLocalLocation(Web.methods).getX(), y = positionable.getPosition().getLocalLocation(Web.methods).getY();
        return getPath(x, y);
    }

    /**
     * @param x
     * @param y
     * @return null if no path.
     */
    public ArrayList<RSTile> getPath(int x, int y) {
        ArrayList<RSTile> path = new ArrayList<>();
        LocalPoint playerPos = Web.methods.players.getMyPlayer().getLocation().getLocalLocation(Web.methods);
        if (x == playerPos.getX() && y == playerPos.getY()) {
            return path;
        }
        if (x < 0 || y < 0) {
            return null;
        }
        if (x >= 104 || y >= 104) {
            return null;
        }
        if (map[x][y] == null) {
            return null;
        }
        RSTile tile = new RSTile(WorldPoint.fromScene(Web.methods.client, x, y, Web.methods.players.getMyPlayer().getLocation().getWorldLocation().getPlane()));
        while ((tile = map[tile.getLocalLocation(Web.methods).getX()][tile.getLocalLocation(Web.methods).getY()]) != null) {
            path.add(tile);
        }
        Collections.reverse(path);
        return path;
    }

    public int getDistance(Positionable positionable) {
        int x = positionable.getPosition().getLocalLocation(Web.methods).getX(), y = positionable.getPosition().getLocalLocation(Web.methods).getY();
        LocalPoint playerPos = Web.methods.players.getMyPlayer().getLocation().getLocalLocation(Web.methods);
        if (x == playerPos.getX() && y == playerPos.getY()) {
            return 0;
        }
        if (x < 0 || y < 0) {
            return Integer.MAX_VALUE;
        }
        if (x >= 104 || y >= 104) {
            return Integer.MAX_VALUE;
        }
        if (map[x][y] == null) {
            return Integer.MAX_VALUE;
        }
        int length = 0;
        RSTile tile = new RSTile(WorldPoint.fromScene(Web.methods.client, x, y, Web.methods.players.getMyPlayer().getLocation().getWorldLocation().getPlane()));
        while ((tile = map[tile.getLocalLocation(Web.methods).getX()][tile.getLocalLocation(Web.methods).getY()]) != null) {
            length++;
        }
        return length;
    }

    /*
    private static RSTile convertToLocal(int x, int y) {
        RSTile position = new RSTile(x, y, Player.getPosition().getPlane(), x >= 104 || y >= 104 ? RSTile.TYPES.WORLD : RSTile.TYPES.LOCAL);
        if (position.getType() != RSTile.TYPES.LOCAL) {
            position = position.toLocalTile();
        }
        return position;
    }

     */

    public static RSTile getBestWalkableTile(Positionable positionable, Reachable reachable) {
        RSTile localPosition = positionable.getPosition();
        HashSet<RSTile> building = BankHelper.getBuilding(positionable);
        boolean[][] traversed = new boolean[104][104];
        RSTile[][] parentMap = new RSTile[104][104];
        Queue<RSTile> queue = new LinkedList<>();
        int[][] collisionData = Web.methods.walking.getCollisionFlags(positionable.getPosition().getWorldLocation().getPlane());
        if(collisionData == null)
            return null;

        queue.add(localPosition);
        try {
            traversed[localPosition.getLocalLocation(Web.methods).getX()][localPosition.getLocalLocation(Web.methods).getY()] = true;
            parentMap[localPosition.getLocalLocation(Web.methods).getX()][localPosition.getLocalLocation(Web.methods).getY()] = null;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }

        while (!queue.isEmpty()) {
            RSTile currentLocal = queue.poll();
            int x = currentLocal.getLocalLocation(Web.methods).getX(), y = currentLocal.getLocalLocation(Web.methods).getY();

            int currentCollisionFlags = collisionData[x][y];
            if (AStarNode.isWalkable(currentCollisionFlags)) {
                if (reachable != null && !reachable.canReach(currentLocal.getWorldLocation().getX(), currentLocal.getWorldLocation().getY())) {
                    continue;
                }
                if (building != null && building.size() > 0) {
                    if (building.contains(currentLocal)) {
                        return currentLocal;
                    }
                    continue; //Next tile because we are now outside of building.
                } else {
                    return currentLocal;
                }
            }

            for (Direction direction : Direction.values()) {
                if (!direction.isValidDirection(x, y, collisionData)) {
                    continue; //Cannot traverse to tile from current.
                }

                RSTile neighbor = direction.getPointingTile(currentLocal);
                int destinationX = neighbor.getLocalLocation(Web.methods).getX(), destinationY = neighbor.getLocalLocation(Web.methods).getY();
                if (traversed[destinationX][destinationY]) {
                    continue; //Traversed already
                }
                traversed[destinationX][destinationY] = true;
                parentMap[destinationX][destinationY] = currentLocal;
                queue.add(neighbor);
            }

        }
        return null;
    }

    /**
     * @return gets collision map.
     */
    public static Reachable getMap() {
        return new Reachable(Web.methods.players.getMyPlayer().getLocation());
    }

    public static Reachable getMap(RSTile homeTile) {
        return new Reachable(homeTile);
    }

    /**
     * @return local reachable tiles
     */
    private static RSTile[][] generateMap(RSTile homeTile) {
        RSTile localPlayerPosition = homeTile;
        boolean[][] traversed = new boolean[104][104];
        RSTile[][] parentMap = new RSTile[104][104];
        Queue<RSTile> queue = new LinkedList<>();
        int[][] collisionData = Web.methods.walking.getCollisionFlags(homeTile.getWorldLocation().getPlane());

        if(collisionData == null)
            return new RSTile[][]{};

        queue.add(homeTile);
        try {
            traversed[homeTile.getLocalLocation(Web.methods).getX()][homeTile.getLocalLocation(Web.methods).getY()] = true;
            parentMap[homeTile.getLocalLocation(Web.methods).getX()][homeTile.getLocalLocation(Web.methods).getY()] = null;
        } catch (Exception e) {
            return parentMap;
        }

        while (!queue.isEmpty()) {
            RSTile currentLocal = queue.poll();
            int x = currentLocal.getLocalLocation(Web.methods).getX(), y = currentLocal.getLocalLocation(Web.methods).getY();

            int currentCollisionFlags = collisionData[x][y];
            if (!AStarNode.isWalkable(currentCollisionFlags)) {
                continue;
            }

            for (Direction direction : Direction.values()) {
                if (!direction.isValidDirection(x, y, collisionData)) {
                    continue; //Cannot traverse to tile from current.
                }

                RSTile neighbor = direction.getPointingTile(currentLocal);
                int destinationX = neighbor.getLocalLocation(Web.methods).getX(), destinationY = neighbor.getLocalLocation(Web.methods).getY();
                if (traversed[destinationX][destinationY]) {
                    continue; //Traversed already
                }
                traversed[destinationX][destinationY] = true;
                parentMap[destinationX][destinationY] = currentLocal;
                queue.add(neighbor);
            }

        }
        return parentMap;
    }

    public enum Direction {
        EAST(1, 0),
        NORTH(0, 1),
        WEST(-1, 0),
        SOUTH(0, -1),
        NORTH_EAST(1, 1),
        NORTH_WEST(-1, 1),
        SOUTH_EAST(1, -1),
        SOUTH_WEST(-1, -1),
        ;

        int x, y;

        Direction(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public RSTile getPointingTile(RSTile tile) {
            return new RSTile(WorldPoint.fromLocal(Web.methods.client,
                    tile.getLocalLocation(Web.methods).getX() + x, tile.getLocalLocation(Web.methods).getY() + y, tile.getWorldLocation().getPlane()));
        }

        public boolean isValidDirection(int x, int y, int[][] collisionData) {
            try {
                switch (this) {
                    case NORTH:
                        return !AStarNode.blockedNorth(collisionData[x][y]);
                    case EAST:
                        return !AStarNode.blockedEast(collisionData[x][y]);
                    case SOUTH:
                        return !AStarNode.blockedSouth(collisionData[x][y]);
                    case WEST:
                        return !AStarNode.blockedWest(collisionData[x][y]);
                    case NORTH_EAST:
                        if (AStarNode.blockedNorth(collisionData[x][y]) || AStarNode.blockedEast(collisionData[x][y])) {
                            return false;
                        }
                        if (!AStarNode.isWalkable(collisionData[x + 1][y])) {
                            return false;
                        }
                        if (!AStarNode.isWalkable(collisionData[x][y + 1])) {
                            return false;
                        }
                        if (AStarNode.blockedNorth(collisionData[x + 1][y])) {
                            return false;
                        }
                        if (AStarNode.blockedEast(collisionData[x][y + 1])) {
                            return false;
                        }
                        return true;
                    case NORTH_WEST:
                        if (AStarNode.blockedNorth(collisionData[x][y]) || AStarNode.blockedWest(collisionData[x][y])) {
                            return false;
                        }
                        if (!AStarNode.isWalkable(collisionData[x - 1][y])) {
                            return false;
                        }
                        if (!AStarNode.isWalkable(collisionData[x][y + 1])) {
                            return false;
                        }
                        if (AStarNode.blockedNorth(collisionData[x - 1][y])) {
                            return false;
                        }
                        if (AStarNode.blockedWest(collisionData[x][y + 1])) {
                            return false;
                        }
                        return true;
                    case SOUTH_EAST:
                        if (AStarNode.blockedSouth(collisionData[x][y]) || AStarNode.blockedEast(collisionData[x][y])) {
                            return false;
                        }
                        if (!AStarNode.isWalkable(collisionData[x + 1][y])) {
                            return false;
                        }
                        if (!AStarNode.isWalkable(collisionData[x][y - 1])) {
                            return false;
                        }
                        if (AStarNode.blockedSouth(collisionData[x + 1][y])) {
                            return false;
                        }
                        if (AStarNode.blockedEast(collisionData[x][y - 1])) {
                            return false;
                        }
                        return true;
                    case SOUTH_WEST:
                        if (AStarNode.blockedSouth(collisionData[x][y]) || AStarNode.blockedWest(collisionData[x][y])) {
                            return false;
                        }
                        if (!AStarNode.isWalkable(collisionData[x - 1][y])) {
                            return false;
                        }
                        if (!AStarNode.isWalkable(collisionData[x][y - 1])) {
                            return false;
                        }
                        if (AStarNode.blockedSouth(collisionData[x - 1][y])) {
                            return false;
                        }
                        if (AStarNode.blockedWest(collisionData[x][y - 1])) {
                            return false;
                        }
                        return true;
                    default:
                        return false;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                return false;
            }
        }
    }

}
