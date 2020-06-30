package net.runelite.client.rsb.walker.dax_api;

import net.runelite.api.Point;
import net.runelite.client.rsb.methods.MethodContext;
import net.runelite.client.rsb.wrappers.RSTile;
import net.runelite.client.rsb.wrappers.common.Clickable07;

public class WalkerTile extends RSTile implements Clickable07 {

    private MethodContext ctx;

    public WalkerTile(MethodContext ctx, RSTile tile) {
        super(tile.getWorldLocation());
        this.ctx = ctx;
    };

    @Override
    public boolean isClickable() {
        return ctx.calc.tileOnScreen(this);
    }


    @Override
    public boolean doAction(String action) {
        return ctx.tiles.doAction(this, action);
    }

    @Override
    public boolean doAction(String action, String option) {
        return ctx.tiles.doAction(this, action, option);
    }

    @Override
    public boolean doClick() {
        return ctx.tiles.doAction(this, "Walk here");
    }

    @Deprecated
    @Override
    public boolean doClick(boolean leftClick) {
        return ctx.tiles.doAction(this, "Walk here");
    }

    @Override
    public boolean doHover() {
        Point p = ctx.calc.tileToScreen(this);
        if (isClickable()) {
            ctx.mouse.move(p);
            return true;
        }
        return false;
    }

    @Override
    public boolean turnTo() {
        if (isClickable()) {
            ctx.camera.turnTo(this);
            return true;
        }
        return false;
    }
}
