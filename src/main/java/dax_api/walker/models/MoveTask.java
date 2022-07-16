package dax_api.walker.models;

import dax_api.walker.models.enums.Situation;
import net.runelite.rsb.wrappers.subwrap.WalkerTile;


public class MoveTask {

    private Situation situation;
    private WalkerTile destination, next;

    public MoveTask(Situation situation, WalkerTile destination, WalkerTile next) {
        this.situation = situation;
        this.destination = destination;
        this.next = next;
    }

    public Situation getSituation() {
        return situation;
    }

    /**
     *
     * @return Tile we can walk to.
     */
    public WalkerTile getDestination() {
        return destination;
    }

    /**
     *
     * @return Tile we want to walk to after reaching destination.
     */
    public WalkerTile getNext() {
        return next;
    }

    @Override
    public String toString() {
        return "MoveTask{" +
                "situation=" + situation +
                ", destination=" + destination +
                ", next=" + next +
                '}';
    }
}