package net.runelite.client.rsb.walker.dax_api.walker_engine.navigation_utils;

import net.runelite.client.rsb.methods.Web;
import net.runelite.client.rsb.util.StdRandom;
import net.runelite.client.rsb.walker.dax_api.Filters;
import net.runelite.client.rsb.wrappers.subwrap.WalkerTile;
import net.runelite.client.rsb.walker.dax_api.walker_engine.WaitFor;
import net.runelite.client.rsb.walker.dax_api.walker_engine.interaction_handling.InteractionHelper;
import net.runelite.client.rsb.wrappers.RSArea;
import net.runelite.client.rsb.wrappers.RSObject;



public class ShipUtils {

    private static final WalkerTile[] SPECIAL_CASES = new WalkerTile[]{new WalkerTile(2663, 2676, 1)};

    public static boolean isOnShip() {
        WalkerTile playerPos = new WalkerTile(new WalkerTile(Web.methods.players.getMyPlayer().getLocation()));
        for (WalkerTile specialCase : SPECIAL_CASES){
            if (new RSArea(specialCase, 5).contains(playerPos)){
                return true;
            }
        }
        return getGangplank() != null
                && new WalkerTile(Web.methods.players.getMyPlayer().getLocation()).getWorldLocation().getPlane() == 1
                && Web.methods.objects.getAll(Filters.Objects.nameEquals("Ship's wheel", "Ship's ladder", "Anchor")).length > 0;
    }

    public static boolean crossGangplank() {
        RSObject gangplank = getGangplank();
        if (gangplank == null){
            return false;
        }
        if (!gangplank.doAction("Cross")){
            return false;
        }
        if (WaitFor.condition(1000, () -> Web.methods.game.getCrosshairState() == 2 ? WaitFor.Return.SUCCESS : WaitFor.Return.IGNORE) != WaitFor.Return.SUCCESS){
            return false;
        }
        return WaitFor.condition(StdRandom.uniform(2500, 3000), () -> !ShipUtils.isOnShip() ? WaitFor.Return.SUCCESS : WaitFor.Return.IGNORE) == WaitFor.Return.SUCCESS;
    }

    private static RSObject getGangplank(){
        return InteractionHelper.getRSObject(Filters.Objects.nameEquals("Gangplank").combine(Filters.Objects.actionsContains("Cross"), true));
    }

}
