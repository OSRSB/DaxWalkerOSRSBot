package dax_api.walker.handlers.special_cases.impl;

import dax_api.walker.handlers.move_task.MoveTaskHandler;
import dax_api.walker.handlers.passive_action.PassiveAction;
import dax_api.walker.handlers.special_cases.SpecialCaseHandler;
import dax_api.walker.models.DaxLogger;
import dax_api.walker.models.MoveTask;
import dax_api.walker.models.enums.MoveActionResult;

import java.util.List;

public class ShantyPassHandler implements MoveTaskHandler, DaxLogger, SpecialCaseHandler {

    @Override
    public boolean shouldHandle(MoveTask moveTask) {
        return false;
    }

    @Override
    public MoveActionResult handle(MoveTask moveTask, List<PassiveAction> passiveActionList) {
        return null;
    }

    
}
