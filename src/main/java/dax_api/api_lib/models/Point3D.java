package dax_api.api_lib.models;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.runelite.api.coords.WorldPoint;
import net.runelite.rsb.wrappers.common.Positionable;
import net.runelite.rsb.wrappers.RSTile;

public class Point3D {


    private int x, y, z;

    public Point3D(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
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

    public JsonElement toJson() {
        return new Gson().toJsonTree(this);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }

    public Positionable toPositionable() {
        return new Positionable() {
            public RSTile getAnimablePosition() {
                return new RSTile(x, y, z);
            }

            public boolean adjustCameraTo() {
                return false;
            }

            @Override
            public RSTile getLocation() {
                return new RSTile(new WorldPoint(x, y, z));
            }

            @Override
            public boolean turnTo() {
                return false;
            }
        };
    }

    public static Point3D fromPositionable(Positionable positionable) {
        RSTile RSTile = positionable.getLocation();
        return new Point3D(RSTile.getX(), RSTile.getY(), RSTile.getPlane());
    }

}
