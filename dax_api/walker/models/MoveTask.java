package net.runelite.client.rsb.walker.dax_api.walker.models;

import net.runelite.client.rsb.walker.dax_api.walker.models.enums.Situation;
import net.runelite.client.rsb.wrappers.RSTile;

public class MoveTask {

    private Situation situation;
    private RSTile destination, next;

    public MoveTask(Situation situation, RSTile destination, RSTile next) {
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
    public RSTile getDestination() {
        return destination;
    }

    /**
     *
     * @return Tile we want to walk to after reaching destination.
     */
    public RSTile getNext() {
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