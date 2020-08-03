package net.runelite.client.rsb.walker.dax_api.walker_engine.navigation_utils;

import net.runelite.client.rsb.methods.Interfaces;
import net.runelite.client.rsb.walker.dax_api.Filters;
import net.runelite.client.rsb.walker.dax_api.WalkerTile;
import net.runelite.client.rsb.walker.dax_api.shared.helpers.InterfaceHelper;
import net.runelite.client.rsb.walker.dax_api.walker_engine.WaitFor;
import net.runelite.client.rsb.walker.dax_api.walker_engine.interaction_handling.InteractionHelper;


import java.util.Arrays;


public class GnomeGlider {

    private static final int GNOME_GLIDER_MASTER_INTERFACE = 138;

    public enum Location {
        TA_QUIR_PRIW ("Ta Quir Priw", 2465, 3501, 3),
        GANDIUS ("Gandius", 2970, 2972, 0),
        LEMANTO_ANDRA ("Lemanto Andra", 3321, 3430, 0),
        KAR_HEWO ("Kar-Hewo", 3284, 3211, 0),
        SINDARPOS ("Sindarpos", 2850, 3498, 0)
        ;

        private int x, y, z;
        private String name;
        Location(String name, int x, int y, int z){
            this.x = x;
            this.y = y;
            this.z = z;
            this.name = name;
        }
        public String getName() {
            return name;
        }
        public WalkerTile getWalkerTile(){
            return new WalkerTile(x, y, z);
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getZ() {
            return z;
        }
    }

    public static boolean to(Location location) {
        if (!Interfaces.isInterfaceValid(GNOME_GLIDER_MASTER_INTERFACE)
                && !InteractionHelper.click(InteractionHelper.getRSNPC(Filters.NPCs.actionsContains("Glider")), "Glider", () -> Interfaces.isInterfaceValid(GNOME_GLIDER_MASTER_INTERFACE) ? WaitFor.Return.SUCCESS : WaitFor.Return.IGNORE)) {
            return false;
        }

        RSInterface option = InterfaceHelper.getAllInterfaces(GNOME_GLIDER_MASTER_INTERFACE).stream().filter(rsInterface -> {
            String[] actions = rsInterface.getActions();
            return actions != null && Arrays.stream(actions).anyMatch(s -> s.contains(location.getName()));
        }).findAny().orElse(null);

        if (option == null){
            return false;
        }

        if (!option.click()){
            return false;
        }

        if (WaitFor.condition(General.random(5400, 6500), () -> location.getWalkerTile().distanceTo(new WalkerTile(Web.methods.players.getMyPlayer().getLocation())) < 10 ? WaitFor.Return.SUCCESS : WaitFor.Return.IGNORE) == WaitFor.Return.SUCCESS){
            WaitFor.milliseconds(250, 500);
            return true;
        }
        return false;
    }

}
