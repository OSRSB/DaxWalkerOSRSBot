package net.runelite.client.rsb.walker.dax_api;

import net.runelite.client.rsb.internal.wrappers.Filter;
import net.runelite.client.rsb.wrappers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

public interface Filters {

    Filter<?> actionsContains(String... actions);
    Filter<?> actionsEquals(String... actions);
    Filter<?> actionsNotContains(String... actions);
    Filter<?> actionsNotEquals(String... actions);
    Filter<?> idEquals(int... ids);
    Filter<?> idNotEquals(int... ids);
    Filter<?> nameContains(String... names);
    Filter<?> nameEquals(String... names);
    Filter<?> nameNotContains(String... names);
    Filter<?> nameNotEquals(String... names);


    /**
     * A class used to compare an object against an array with a passable comparing operation
     * @param <T> The type of the objects being compared
     */
    class Comparator<T> {
        /**
         * Iterates through the array and uses the function to perform the passed comparing operation on the passed
         * <var>obj</var> amd each entry in the array to find a result that returns a true statement from the
         * comparing function.
         * @param obj The object derived from a instance of an in-game entity
         * @param array The array of options to compare against
         * @param operation The comparing operation being passed to perform on each obj and option in the array
         * @return <tt>True</tt> if any option in the array fulfills the comparing operation; otherwise <tt>false</tt>
         */
        boolean iterateAndCompare(T obj, T[] array, Function<ArrayList<T>, Boolean> operation) {
            for (T t : array) {
                ArrayList<T> opArray = new ArrayList<>(Arrays.asList(t, obj));
                if (operation.apply(opArray)) {
                    return true;
                }
            }
            return false;
        }
    }


    class Items implements Filters {

        @Override
        public Filter<RSItem> actionsContains(String... actions) {
            return (RSItem item) -> {
                String[] itemActions = item.getInventoryActions();
                Function<ArrayList<String>, Boolean> operation = e -> e.get(0).contains(e.get(1));
                boolean actionCheck = false;
                for (int i = 0; i<itemActions.length; i++)
                    actionCheck = actionCheck  || (new Comparator<String>()).iterateAndCompare(itemActions[i], Arrays.stream(actions).toArray(String[]::new), operation);
                return actionCheck;
            };
        }

        @Override
        public Filter<RSItem> actionsEquals(String... actions) {
            return (RSItem item) -> {
                String[] itemActions = item.getInventoryActions();
                Function<ArrayList<String>, Boolean> operation = e -> e.get(0).equals(e.get(1));
                boolean actionCheck = false;
                for (int i = 0; i<itemActions.length; i++)
                    actionCheck = actionCheck  || (new Comparator<String>()).iterateAndCompare(itemActions[i], Arrays.stream(actions).toArray(String[]::new), operation);
                return actionCheck;
            };
        }

        @Override
        public Filter<RSItem> actionsNotContains(String... actions) {
            return (RSItem item) -> {
                String[] itemActions = item.getInventoryActions();
                Function<ArrayList<String>, Boolean> operation = e -> !e.get(0).contains(e.get(1));
                boolean actionCheck = false;
                for (int i = 0; i<itemActions.length; i++)
                    actionCheck = actionCheck  || (new Comparator<String>()).iterateAndCompare(itemActions[i], Arrays.stream(actions).toArray(String[]::new), operation);
                return actionCheck;
            };
        }

        @Override
        public Filter<RSItem> actionsNotEquals(String... actions) {
            return (RSItem item) -> {
                String[] itemActions = item.getInventoryActions();
                Function<ArrayList<String>, Boolean> operation = e -> !e.get(0).equals(e.get(1));
                boolean actionCheck = false;
                for (int i = 0; i<itemActions.length; i++)
                    actionCheck = actionCheck  || (new Comparator<String>()).iterateAndCompare(itemActions[i], Arrays.stream(actions).toArray(String[]::new), operation);
                return actionCheck;
            };
        }

        @Override
        public Filter<RSItem> idEquals(int... ids) {
            return (RSItem item) -> {
                int iid = item.getItem().getItemId();
                Function<ArrayList<Integer>, Boolean> operation = e -> e.get(0).equals(e.get(1));
                return (new Comparator<Integer>()).iterateAndCompare(iid, Arrays.stream(ids).boxed().toArray(Integer[]::new), operation);
            };
        }

        @Override
        public Filter<RSItem> idNotEquals(int... ids) {
            return (RSItem item) -> {
                int iid = item.getItem().getItemId();
                Function<ArrayList<Integer>, Boolean> operation = e -> !e.get(0).equals(e.get(1));
                return (new Comparator<Integer>()).iterateAndCompare(iid, Arrays.stream(ids).boxed().toArray(Integer[]::new), operation);
            };
        }

        @Override
        public Filter<RSItem> nameContains(String... names) {
            return (RSItem item) -> {
                String iName = item.getName();
                Function<ArrayList<String>, Boolean> operation = e -> e.get(0).contains(e.get(1));
                return (new Comparator<String>()).iterateAndCompare(iName, Arrays.stream(names).toArray(String[]::new), operation);
            };
        }

        @Override
        public Filter<RSItem> nameEquals(String... names) {
            return (RSItem item) -> {
                String iName = item.getName();
                Function<ArrayList<String>, Boolean> operation = e -> e.get(0).equals(e.get(1));
                return (new Comparator<String>()).iterateAndCompare(iName, Arrays.stream(names).toArray(String[]::new), operation);
            };
        }

        @Override
        public Filter<RSItem> nameNotContains(String... names) {
            return (RSItem item) -> {
                String iName = item.getName();
                Function<ArrayList<String>, Boolean> operation = e -> !e.get(0).contains(e.get(1));
                return (new Comparator<String>()).iterateAndCompare(iName, Arrays.stream(names).toArray(String[]::new), operation);
            };
        }

        @Override
        public Filter<RSItem> nameNotEquals(String... names) {
            return (RSItem item) -> {
                String iName = item.getName();
                Function<ArrayList<String>, Boolean> operation = e -> !e.get(0).equals(e.get(1));
                return (new Comparator<String>()).iterateAndCompare(iName, Arrays.stream(names).toArray(String[]::new), operation);
            };
        }
    }

    class Objects implements Filters {

        @Override
        public Filter<RSObject> actionsContains(String... actions) {
            return (RSObject object) -> {
                String[] objectActions = object.getDef().getActions();
                Function<ArrayList<String>, Boolean> operation = e -> e.get(0).contains(e.get(1));
                boolean actionCheck = false;
                for (int i = 0; i<objectActions.length; i++)
                    actionCheck = actionCheck  || (new Comparator<String>()).iterateAndCompare(objectActions[i], Arrays.stream(actions).toArray(String[]::new), operation);
                return actionCheck;
            };
        }

        @Override
        public Filter<RSObject> actionsEquals(String... actions) {
            return (RSObject object) -> {
                String[] objectActions = object.getDef().getActions();
                Function<ArrayList<String>, Boolean> operation = e -> e.get(0).equals(e.get(1));
                boolean actionCheck = false;
                for (int i = 0; i<objectActions.length; i++)
                    actionCheck = actionCheck  || (new Comparator<String>()).iterateAndCompare(objectActions[i], Arrays.stream(actions).toArray(String[]::new), operation);
                return actionCheck;
            };
        }

        @Override
        public Filter<RSObject> actionsNotContains(String... actions) {
            return (RSObject object) -> {
                String[] objectActions = object.getDef().getActions();
                Function<ArrayList<String>, Boolean> operation = e -> !e.get(0).contains(e.get(1));
                boolean actionCheck = false;
                for (int i = 0; i<objectActions.length; i++)
                    actionCheck = actionCheck  || (new Comparator<String>()).iterateAndCompare(objectActions[i], Arrays.stream(actions).toArray(String[]::new), operation);
                return actionCheck;
            };
        }

        @Override
        public Filter<RSObject> actionsNotEquals(String... actions) {
            return (RSObject object) -> {
                String[] objectActions = object.getDef().getActions();
                Function<ArrayList<String>, Boolean> operation = e -> !e.get(0).equals(e.get(1));
                boolean actionCheck = false;
                for (int i = 0; i<objectActions.length; i++)
                    actionCheck = actionCheck  || (new Comparator<String>()).iterateAndCompare(objectActions[i], Arrays.stream(actions).toArray(String[]::new), operation);
                return actionCheck;
            };
        }

        @Override
        public Filter<RSObject> idEquals(int... ids) {
            return (RSObject object) -> {
                int oid = object.getID();
                Function<ArrayList<Integer>, Boolean> operation = e -> e.get(0).equals(e.get(1));
                return (new Comparator<Integer>()).iterateAndCompare(oid, Arrays.stream(ids).boxed().toArray(Integer[]::new), operation);
            };
        }

        @Override
        public Filter<RSObject> idNotEquals(int... ids) {
            return (RSObject object) -> {
                int oid = object.getID();
                Function<ArrayList<Integer>, Boolean> operation = e -> !e.get(0).equals(e.get(1));
                return (new Comparator<Integer>()).iterateAndCompare(oid, Arrays.stream(ids).boxed().toArray(Integer[]::new), operation);
            };
        }

        @Override
        public Filter<RSObject> nameContains(String... names) {
            return (RSObject object) -> {
                String oName = object.getName();
                Function<ArrayList<String>, Boolean> operation = e -> e.get(0).contains(e.get(1));
                return (new Comparator<String>()).iterateAndCompare(oName, Arrays.stream(names).toArray(String[]::new), operation);
            };
        }

        @Override
        public Filter<RSObject> nameEquals(String... names) {
            return (RSObject object) -> {
                String oName = object.getName();
                Function<ArrayList<String>, Boolean> operation = e -> e.get(0).equals(e.get(1));
                return (new Comparator<String>()).iterateAndCompare(oName, Arrays.stream(names).toArray(String[]::new), operation);
            };
        }

        @Override
        public Filter<RSObject> nameNotContains(String... names) {
            return (RSObject object) -> {
                String oName = object.getName();
                Function<ArrayList<String>, Boolean> operation = e -> !e.get(0).contains(e.get(1));
                return (new Comparator<String>()).iterateAndCompare(oName, Arrays.stream(names).toArray(String[]::new), operation);
            };
        }

        @Override
        public Filter<RSObject> nameNotEquals(String... names) {
            return (RSObject object) -> {
                String oName = object.getName();
                Function<ArrayList<String>, Boolean> operation = e -> !e.get(0).equals(e.get(1));
                return (new Comparator<String>()).iterateAndCompare(oName, Arrays.stream(names).toArray(String[]::new), operation);
            };
        }

        public Filter<RSObject> modelIndexCount(int... counts) {
            return (RSObject object) -> {
                int oIndexCount = object.getModel().getIndexCount();
                Function<ArrayList<Integer>, Boolean> operation = e -> e.get(0).equals(e.get(1));
                return (new Comparator<Integer>()).iterateAndCompare(oIndexCount, Arrays.stream(counts).boxed().toArray(Integer[]::new), operation);
            };
        }

        public Filter<RSObject> modelVertexCount(int... counts) {
            return (RSObject object) -> {
                int oVertexCount = object.getModel().getVertexCount();
                Function<ArrayList<Integer>, Boolean> operation = e -> e.get(0).equals(e.get(1));
                return (new Comparator<Integer>()).iterateAndCompare(oVertexCount, Arrays.stream(counts).boxed().toArray(Integer[]::new), operation);
            };
        }

        public Filter<RSObject> inArea(RSArea area) {
            return (RSObject object) -> {
                RSArea oArea = object.getArea();
                Function<ArrayList<RSArea>, Boolean> operation = e -> e.get(0).contains(e.get(1).getTileArray());
                return (new Comparator<RSArea>()).iterateAndCompare(oArea, new RSArea[]{area}, operation);
            };
        }

        public Filter<RSObject> notInArea(RSArea area) {
            return (RSObject object) -> {
                RSArea oArea = object.getArea();
                Function<ArrayList<RSArea>, Boolean> operation = e -> !e.get(0).contains(e.get(1).getTileArray());
                return (new Comparator<RSArea>()).iterateAndCompare(oArea, new RSArea[]{area}, operation);
            };
        }

        public Filter<RSObject> tileEquals(Positionable pos) {
            return (RSObject object) -> {
                RSTile oTile = object.getLocation();
                RSTile pTile = pos.getPosition();
                Function<ArrayList<RSTile>, Boolean> operation = e -> e.get(0).equals(e.get(1));
                return (new Comparator<RSTile>()).iterateAndCompare(oTile, new RSTile[]{pTile}, operation);
            };
        }

        public Filter<RSObject> tileNotEquals(Positionable pos) {
            return (RSObject object) -> {
                RSTile oTile = object.getLocation();
                RSTile pTile = pos.getPosition();
                Function<ArrayList<RSTile>, Boolean> operation = e -> !e.get(0).equals(e.get(1));
                return (new Comparator<RSTile>()).iterateAndCompare(oTile, new RSTile[]{pTile}, operation);
            };
        }


    }

    class NPCs implements Filters {

        @Override
        public Filter<RSNPC> actionsContains(String... actions) {
            return (RSNPC npc) -> {
                String[] npcActions = npc.getActions();
                Function<ArrayList<String>, Boolean> operation = e -> e.get(0).contains(e.get(1));
                boolean actionCheck = false;
                for (int i = 0; i<npcActions.length; i++)
                    actionCheck = actionCheck  || (new Comparator<String>()).iterateAndCompare(npcActions[i], Arrays.stream(actions).toArray(String[]::new), operation);
                return actionCheck;
            };
        }

        @Override
        public Filter<RSNPC> actionsEquals(String... actions) {
            return (RSNPC npc) -> {
                String[] npcActions = npc.getActions();
                Function<ArrayList<String>, Boolean> operation = e -> e.get(0).equals(e.get(1));
                boolean actionCheck = false;
                for (int i = 0; i<npcActions.length; i++)
                    actionCheck = actionCheck  || (new Comparator<String>()).iterateAndCompare(npcActions[i], Arrays.stream(actions).toArray(String[]::new), operation);
                return actionCheck;
            };
        }

        @Override
        public Filter<RSNPC> actionsNotContains(String... actions) {
            return (RSNPC npc) -> {
                String[] npcActions = npc.getActions();
                Function<ArrayList<String>, Boolean> operation = e -> !e.get(0).contains(e.get(1));
                boolean actionCheck = false;
                for (int i = 0; i<npcActions.length; i++)
                    actionCheck = actionCheck  || (new Comparator<String>()).iterateAndCompare(npcActions[i], Arrays.stream(actions).toArray(String[]::new), operation);
                return actionCheck;
            };
        }

        @Override
        public Filter<RSNPC> actionsNotEquals(String... actions) {
            return (RSNPC npc) -> {
                String[] npcActions = npc.getActions();
                Function<ArrayList<String>, Boolean> operation = e -> !e.get(0).equals(e.get(1));
                boolean actionCheck = false;
                for (int i = 0; i<npcActions.length; i++)
                    actionCheck = actionCheck  || (new Comparator<String>()).iterateAndCompare(npcActions[i], Arrays.stream(actions).toArray(String[]::new), operation);
                return actionCheck;
            };
        }

        @Override
        public Filter<RSNPC> idEquals(int... ids) {
            return (RSNPC npc) -> {
                int nid = npc.getID();
                Function<ArrayList<Integer>, Boolean> operation = e -> e.get(0).equals(e.get(1));
                return (new Comparator<Integer>()).iterateAndCompare(nid, Arrays.stream(ids).boxed().toArray(Integer[]::new), operation);
            };
        }

        @Override
        public Filter<RSNPC> idNotEquals(int... ids) {
            return (RSNPC npc) -> {
                int nid = npc.getID();
                Function<ArrayList<Integer>, Boolean> operation = e -> !e.get(0).equals(e.get(1));
                return (new Comparator<Integer>()).iterateAndCompare(nid, Arrays.stream(ids).boxed().toArray(Integer[]::new), operation);
            };
        }

        @Override
        public Filter<RSNPC> nameContains(String... names) {
            return (RSNPC npc) -> {
                String nName = npc.getName();
                Function<ArrayList<String>, Boolean> operation = e -> e.get(0).contains(e.get(1));
                return (new Comparator<String>()).iterateAndCompare(nName, Arrays.stream(names).toArray(String[]::new), operation);
            };
        }

        @Override
        public Filter<RSNPC> nameEquals(String... names) {
            return (RSNPC npc) -> {
                String nName = npc.getName();
                Function<ArrayList<String>, Boolean> operation = e -> e.get(0).equals(e.get(1));
                return (new Comparator<String>()).iterateAndCompare(nName, Arrays.stream(names).toArray(String[]::new), operation);
            };
        }

        @Override
        public Filter<RSNPC> nameNotContains(String... names) {
            return (RSNPC npc) -> {
                String nName = npc.getName();
                Function<ArrayList<String>, Boolean> operation = e -> !e.get(0).contains(e.get(1));
                return (new Comparator<String>()).iterateAndCompare(nName, Arrays.stream(names).toArray(String[]::new), operation);
            };
        }

        @Override
        public Filter<RSNPC> nameNotEquals(String... names) {
            return (RSNPC npc) -> {
                String nName = npc.getName();
                Function<ArrayList<String>, Boolean> operation = e -> !e.get(0).equals(e.get(1));
                return (new Comparator<String>()).iterateAndCompare(nName, Arrays.stream(names).toArray(String[]::new), operation);
            };
        }

        public Filter<RSNPC> modelIndexCount(int... counts) {
            return (RSNPC npc) -> {
                int nIndexCount = npc.getModel().getIndexCount();
                Function<ArrayList<Integer>, Boolean> operation = e -> e.get(0).equals(e.get(1));
                return (new Comparator<Integer>()).iterateAndCompare(nIndexCount, Arrays.stream(counts).boxed().toArray(Integer[]::new), operation);
            };
        }

        public Filter<RSNPC> modelVertexCount(int... counts) {
            return (RSNPC npc) -> {
                int nVertexCount = npc.getModel().getVertexCount();
                Function<ArrayList<Integer>, Boolean> operation = e -> e.get(0).equals(e.get(1));
                return (new Comparator<Integer>()).iterateAndCompare(nVertexCount, Arrays.stream(counts).boxed().toArray(Integer[]::new), operation);
            };
        }

        public Filter<RSNPC> inArea(RSArea area) {
            return (RSNPC npc) -> {
                RSArea nArea = new RSArea(new RSTile[]{npc.getLocation()});
                Function<ArrayList<RSArea>, Boolean> operation = e -> e.get(0).contains(e.get(1).getTileArray());
                return (new Comparator<RSArea>()).iterateAndCompare(nArea, new RSArea[]{area}, operation);
            };
        }

        public Filter<RSNPC> notInArea(RSArea area) {
            return (RSNPC npc) -> {
                RSArea nArea = new RSArea(new RSTile[]{npc.getLocation()});
                Function<ArrayList<RSArea>, Boolean> operation = e -> e.get(0).contains(e.get(1).getTileArray());
                return (new Comparator<RSArea>()).iterateAndCompare(nArea, new RSArea[]{area}, operation);
            };
        }

        public Filter<RSNPC> tileEquals(Positionable pos) {
            return (RSNPC npc) -> {
                RSTile nTile = npc.getLocation();
                RSTile pTile = pos.getPosition();
                Function<ArrayList<RSTile>, Boolean> operation = e -> e.get(0).equals(e.get(1));
                return (new Comparator<RSTile>()).iterateAndCompare(nTile, new RSTile[]{pTile}, operation);
            };
        }

        public Filter<RSNPC> tileNotEquals(Positionable pos) {
            return (RSNPC npc) -> {
                RSTile nTile = npc.getLocation();
                RSTile pTile = pos.getPosition();
                Function<ArrayList<RSTile>, Boolean> operation = e -> !e.get(0).equals(e.get(1));
                return (new Comparator<RSTile>()).iterateAndCompare(nTile, new RSTile[]{pTile}, operation);
            };
        }
    }

    class Players implements Filters {

        //Actions might be add-able later
        @Override
        public Filter<RSPlayer> actionsContains(String... actions) {
            return null;
        }

        @Override
        public Filter<RSPlayer> actionsEquals(String... actions) {
            return null;
        }

        @Override
        public Filter<RSPlayer> actionsNotContains(String... actions) {
            return null;
        }

        @Override
        public Filter<RSPlayer> actionsNotEquals(String... actions) {
            return null;
        }

        //Players lack ID
        @Override
        public Filter<RSPlayer> idEquals(int... ids) {
            return null;
        }

        @Override
        public Filter<RSPlayer> idNotEquals(int... ids) {
            return null;
        }

        @Override
        public Filter<RSPlayer> nameContains(String... names) {
            return (RSPlayer player) -> {
                String pName = player.getName();
                Function<ArrayList<String>, Boolean> operation = e -> e.get(0).contains(e.get(1));
                return (new Comparator<String>()).iterateAndCompare(pName, Arrays.stream(names).toArray(String[]::new), operation);
            };
        }

        @Override
        public Filter<RSPlayer> nameEquals(String... names) {
            return (RSPlayer player) -> {
                String pName = player.getName();
                Function<ArrayList<String>, Boolean> operation = e -> e.get(0).equals(e.get(1));
                return (new Comparator<String>()).iterateAndCompare(pName, Arrays.stream(names).toArray(String[]::new), operation);
            };
        }

        @Override
        public Filter<RSPlayer> nameNotContains(String... names) {
            return (RSPlayer player) -> {
                String nName = player.getName();
                Function<ArrayList<String>, Boolean> operation = e -> !e.get(0).contains(e.get(1));
                return (new Comparator<String>()).iterateAndCompare(nName, Arrays.stream(names).toArray(String[]::new), operation);
            };
        }

        @Override
        public Filter<RSPlayer> nameNotEquals(String... names) {
            return (RSPlayer player) -> {
                String pName = player.getName();
                Function<ArrayList<String>, Boolean> operation = e -> !e.get(0).equals(e.get(1));
                return (new Comparator<String>()).iterateAndCompare(pName, Arrays.stream(names).toArray(String[]::new), operation);
            };
        }

        public Filter<RSPlayer> modelIndexCount(int... counts) {
            return (RSPlayer player) -> {
                int pIndexCount = player.getModel().getIndexCount();
                Function<ArrayList<Integer>, Boolean> operation = e -> e.get(0).equals(e.get(1));
                return (new Comparator<Integer>()).iterateAndCompare(pIndexCount, Arrays.stream(counts).boxed().toArray(Integer[]::new), operation);
            };
        }

        public Filter<RSPlayer> modelVertexCount(int... counts) {
            return (RSPlayer player) -> {
                int pVertexCount = player.getModel().getVertexCount();
                Function<ArrayList<Integer>, Boolean> operation = e -> e.get(0).equals(e.get(1));
                return (new Comparator<Integer>()).iterateAndCompare(pVertexCount, Arrays.stream(counts).boxed().toArray(Integer[]::new), operation);
            };
        }

        public Filter<RSPlayer> inArea(RSArea area) {
            return (RSPlayer player) -> {
                RSArea pArea = new RSArea(new RSTile[]{player.getLocation()});
                Function<ArrayList<RSArea>, Boolean> operation = e -> e.get(0).contains(e.get(1).getTileArray());
                return (new Comparator<RSArea>()).iterateAndCompare(pArea, new RSArea[]{area}, operation);
            };
        }

        public Filter<RSPlayer> notInArea(RSArea area) {
            return (RSPlayer player) -> {
                RSArea pArea = new RSArea(new RSTile[]{player.getLocation()});
                Function<ArrayList<RSArea>, Boolean> operation = e -> e.get(0).contains(e.get(1).getTileArray());
                return (new Comparator<RSArea>()).iterateAndCompare(pArea, new RSArea[]{area}, operation);
            };
        }

        public Filter<RSPlayer> tileEquals(Positionable pos) {
            return (RSPlayer player) -> {
                RSTile playerTile = player.getLocation();
                RSTile pTile = pos.getPosition();
                Function<ArrayList<RSTile>, Boolean> operation = e -> e.get(0).equals(e.get(1));
                return (new Comparator<RSTile>()).iterateAndCompare(playerTile, new RSTile[]{pTile}, operation);
            };
        }

        public Filter<RSPlayer> tileNotEquals(Positionable pos) {
            return (RSPlayer player) -> {
                RSTile playerTile = player.getLocation();
                RSTile pTile = pos.getPosition();
                Function<ArrayList<RSTile>, Boolean> operation = e -> !e.get(0).equals(e.get(1));
                return (new Comparator<RSTile>()).iterateAndCompare(playerTile, new RSTile[]{pTile}, operation);
            };
        }
    }

    class GroundItems implements Filters {
        @Override
        public Filter<RSGroundItem> actionsContains(String... actions) {
            return (RSGroundItem item) -> {
                String[] itemActions = item.getItem().getGroundActions();
                Function<ArrayList<String>, Boolean> operation = e -> e.get(0).contains(e.get(1));
                boolean actionCheck = false;
                for (int i = 0; i<itemActions.length; i++)
                    actionCheck = actionCheck  || (new Comparator<String>()).iterateAndCompare(itemActions[i], Arrays.stream(actions).toArray(String[]::new), operation);
                return actionCheck;
            };
        }

        @Override
        public Filter<RSGroundItem> actionsEquals(String... actions) {
            return (RSGroundItem item) -> {
                String[] itemActions = item.getItem().getGroundActions();
                Function<ArrayList<String>, Boolean> operation = e -> e.get(0).equals(e.get(1));
                boolean actionCheck = false;
                for (int i = 0; i<itemActions.length; i++)
                    actionCheck = actionCheck  || (new Comparator<String>()).iterateAndCompare(itemActions[i], Arrays.stream(actions).toArray(String[]::new), operation);
                return actionCheck;
            };
        }

        @Override
        public Filter<RSGroundItem> actionsNotContains(String... actions) {
            return (RSGroundItem item) -> {
                String[] itemActions = item.getItem().getGroundActions();
                Function<ArrayList<String>, Boolean> operation = e -> !e.get(0).contains(e.get(1));
                boolean actionCheck = false;
                for (int i = 0; i<itemActions.length; i++)
                    actionCheck = actionCheck  || (new Comparator<String>()).iterateAndCompare(itemActions[i], Arrays.stream(actions).toArray(String[]::new), operation);
                return actionCheck;
            };
        }

        @Override
        public Filter<RSGroundItem> actionsNotEquals(String... actions) {
            return (RSGroundItem item) -> {
                String[] itemActions = item.getItem().getGroundActions();
                Function<ArrayList<String>, Boolean> operation = e -> !e.get(0).equals(e.get(1));
                boolean actionCheck = false;
                for (int i = 0; i<itemActions.length; i++)
                    actionCheck = actionCheck  || (new Comparator<String>()).iterateAndCompare(itemActions[i], Arrays.stream(actions).toArray(String[]::new), operation);
                return actionCheck;
            };
        }

        @Override
        public Filter<RSGroundItem> idEquals(int... ids) {
            return (RSGroundItem item) -> {
                int iid = item.getItem().getItem().getItemId();
                Function<ArrayList<Integer>, Boolean> operation = e -> e.get(0).equals(e.get(1));
                return (new Comparator<Integer>()).iterateAndCompare(iid, Arrays.stream(ids).boxed().toArray(Integer[]::new), operation);
            };
        }

        @Override
        public Filter<RSGroundItem> idNotEquals(int... ids) {
            return (RSGroundItem item) -> {
                int iid = item.getItem().getItem().getItemId();
                Function<ArrayList<Integer>, Boolean> operation = e -> !e.get(0).equals(e.get(1));
                return (new Comparator<Integer>()).iterateAndCompare(iid, Arrays.stream(ids).boxed().toArray(Integer[]::new), operation);
            };
        }

        @Override
        public Filter<RSGroundItem> nameContains(String... names) {
            return (RSGroundItem item) -> {
                String iName = item.getItem().getName();
                Function<ArrayList<String>, Boolean> operation = e -> e.get(0).contains(e.get(1));
                return (new Comparator<String>()).iterateAndCompare(iName, Arrays.stream(names).toArray(String[]::new), operation);
            };
        }

        @Override
        public Filter<RSGroundItem> nameEquals(String... names) {
            return (RSGroundItem item) -> {
                String iName = item.getItem().getName();
                Function<ArrayList<String>, Boolean> operation = e -> e.get(0).equals(e.get(1));
                return (new Comparator<String>()).iterateAndCompare(iName, Arrays.stream(names).toArray(String[]::new), operation);
            };
        }

        @Override
        public Filter<RSGroundItem> nameNotContains(String... names) {
            return (RSGroundItem item) -> {
                String iName = item.getItem().getName();
                Function<ArrayList<String>, Boolean> operation = e -> !e.get(0).contains(e.get(1));
                return (new Comparator<String>()).iterateAndCompare(iName, Arrays.stream(names).toArray(String[]::new), operation);
            };
        }

        @Override
        public Filter<RSGroundItem> nameNotEquals(String... names) {
            return (RSGroundItem item) -> {
                String iName = item.getItem().getName();
                Function<ArrayList<String>, Boolean> operation = e -> !e.get(0).equals(e.get(1));
                return (new Comparator<String>()).iterateAndCompare(iName, Arrays.stream(names).toArray(String[]::new), operation);
            };
        }
    }
}
