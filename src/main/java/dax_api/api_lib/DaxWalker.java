package dax_api.api_lib;

import dax_api.api_lib.models.*;
import dax_api.teleports.Teleport;
import dax_api.walker.DaxWalkerEngine;
import dax_api.walker_engine.Loggable;
import dax_api.walker_engine.WaitFor;
import dax_api.walker_engine.WalkerEngine;
import dax_api.walker_engine.WalkingCondition;
import dax_api.walker_engine.navigation_utils.ShipUtils;
import net.runelite.rsb.methods.Web;
import net.runelite.rsb.wrappers.common.Positionable;
import net.runelite.rsb.wrappers.RSTile;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DaxWalker implements Loggable {

    private static Map<RSTile, Teleport> map;
    private static DaxWalker daxWalker;
    private static DaxWalkerEngine daxWalkerEngine;
    public static DaxWalker getInstance() {
        return daxWalker != null ? daxWalker : (daxWalker = new DaxWalker());
    }

    public boolean useRun = true;

    private WalkingCondition globalWalkingCondition;

    private DaxWalker() {
        globalWalkingCondition = () -> WalkingCondition.State.CONTINUE_WALKER;

        map = new ConcurrentHashMap<>();
        for (Teleport teleport : Teleport.values()) {
            map.put(teleport.getLocation(), teleport);
        }
    }

    public static WalkingCondition getGlobalWalkingCondition() {
        return getInstance().globalWalkingCondition;
    }

    public void useLocalDevelopmentServer(boolean b) {
        WebWalkerServerApi.getInstance().setTestMode(b);
    }

    public static void setGlobalWalkingCondition(WalkingCondition walkingCondition) {
        getInstance().globalWalkingCondition = walkingCondition;
    }

    public static void setCredentials(DaxCredentialsProvider daxCredentialsProvider) {
        WebWalkerServerApi.getInstance().setDaxCredentialsProvider(daxCredentialsProvider);
    }

    public static boolean walkTo(Positionable positionable) {
        return walkTo(positionable, null);
    }

    public static boolean walkTo(Positionable destination, WalkingCondition walkingCondition) {
        if (ShipUtils.isOnShip()) {
            ShipUtils.crossGangplank();
            WaitFor.milliseconds(500, 1200);
        }
        RSTile start = new RSTile(new RSTile(Web.methods.players.getMyPlayer().getLocation()));
        if (start.equals(destination)) {
            return true;
        }

        List<PathRequestPair> pathRequestPairs = getInstance().getPathTeleports(destination.getLocation());

        pathRequestPairs.add(new PathRequestPair(Point3D.fromPositionable(start), Point3D.fromPositionable(destination)));

	    List<PathResult> pathResults = WebWalkerServerApi.getInstance().getPaths(new BulkPathRequest(PlayerDetails.generate(),pathRequestPairs));

	    List<PathResult> validPaths = getInstance().validPaths(pathResults);

	    PathResult pathResult = getInstance().getBestPath(validPaths);
	    if (pathResult == null) {
            getInstance().log(Level.WARNING, "No valid path found");
		    return false;
	    }

	    return WalkerEngine.getInstance().walkPath(pathResult.toRSTilePath(), getGlobalWalkingCondition().combine(walkingCondition));
    }

    public static boolean walkToBank() {
        return walkToBank(null, null);
    }

    public static boolean walkToBank(RunescapeBank bank) {
        return walkToBank(bank, null);
    }

    public static boolean walkToBank(WalkingCondition walkingCondition) {
        return walkToBank(null, walkingCondition);
    }

    public static boolean walkToBank(RunescapeBank bank, WalkingCondition walkingCondition) {
        if (ShipUtils.isOnShip()) {
            ShipUtils.crossGangplank();
            WaitFor.milliseconds(500, 1200);
        }

        if(bank != null)
            return walkTo(bank.getLocation());

        List<BankPathRequestPair> pathRequestPairs = getInstance().getBankPathTeleports();

        pathRequestPairs.add(new BankPathRequestPair(Point3D.fromPositionable(new RSTile(Web.methods.players.getMyPlayer().getLocation())),null));

        List<PathResult> pathResults = WebWalkerServerApi.getInstance().getBankPaths(new BulkBankPathRequest(PlayerDetails.generate(),pathRequestPairs));

        List<PathResult> validPaths = getInstance().validPaths(pathResults);
        PathResult pathResult = getInstance().getBestPath(validPaths);
        if (pathResult == null) {
            getInstance().log(Level.WARNING, "No valid path found");
            return false;
        }
        return WalkerEngine.getInstance().walkPath(pathResult.toRSTilePath(), getGlobalWalkingCondition().combine(walkingCondition));
    }

    private List<PathRequestPair> getPathTeleports(RSTile start) {
        return Teleport.getValidStartingRSTiles().stream()
                .map(t -> new PathRequestPair(Point3D.fromPositionable(t),
                        Point3D.fromPositionable(start)))
                .collect(Collectors.toList());
    }

    private List<BankPathRequestPair> getBankPathTeleports() {
        return Teleport.getValidStartingRSTiles().stream()
                .map(t -> new BankPathRequestPair(Point3D.fromPositionable(t), null))
                .collect(Collectors.toList());
    }

    public List<PathResult> validPaths(List<PathResult> list) {
        List<PathResult> result = list.stream().filter(pathResult -> pathResult.getPathStatus() == PathStatus.SUCCESS).collect(
		        Collectors.toList());
        if (!result.isEmpty()) {
            return result;
        }
        return Collections.emptyList();
    }

    public PathResult getBestPath(List<PathResult> list) {
        return list.stream().min(Comparator.comparingInt(this::getPathMoveCost)).orElse(null);
    }

    private int getPathMoveCost(PathResult pathResult) {
        if (new RSTile(Web.methods.players.getMyPlayer().getLocation()).equals(pathResult.getPath().get(0).toPositionable().getLocation())) return pathResult.getCost();
        Teleport teleport = map.get(pathResult.getPath().get(0).toPositionable().getLocation());
        if (teleport == null) return pathResult.getCost();
        return teleport.getMoveCost() + pathResult.getCost();
    }

    @Override
    public String getName() {
        return "DaxWalker";
    }
}
