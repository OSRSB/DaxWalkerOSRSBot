package net.runelite.client.rsb.walker.dax_api;

import net.runelite.client.rsb.wrappers.RSCharacter;
import net.runelite.client.rsb.wrappers.RSObject;
import net.runelite.client.rsb.wrappers.RSTile;

public class Positionable {

    RSObject object;
    RSCharacter character;
    RSTile tile;

    public Positionable(RSCharacter character) {
        this.character = character;
    }

    public Positionable(RSObject object) {
        this.object = object;
    }

    public Positionable(RSTile tile) {this.tile = tile;}

    public RSTile getPosition() {
        return (object == null) ? character.getLocation() : object.getLocation();
    }

}
