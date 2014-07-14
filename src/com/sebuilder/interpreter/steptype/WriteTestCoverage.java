package com.sebuilder.interpreter.steptype;

import com.sebuilder.interpreter.Script;
import com.sebuilder.interpreter.Step;
import com.sebuilder.interpreter.StepType;
import com.sebuilder.interpreter.TestRun;
import java.util.ArrayList;

public class WriteTestCoverage implements StepType {
    /**
     * @param ctx - Current test run.
     * @return Whether the step succeeded. This should be true except for failed verify steps, which should return false. Other failures should throw a RuntimeException.
    */
    public boolean run(TestRun ctx) {
        Script script = ctx.getScript();
        ArrayList<Step> steps = script.steps;
        for (Step step : steps) {
            System.out.println(step);
        }
        return true;
    }

}
