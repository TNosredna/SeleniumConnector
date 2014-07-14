package com.novell.nccd.validator.connectors;

import com.sebuilder.interpreter.Script;
import com.sebuilder.interpreter.Step;
import com.sebuilder.interpreter.TestRun;
import com.sebuilder.interpreter.factory.ScriptFactory;
import com.sebuilder.interpreter.factory.StepTypeFactory;
import com.sebuilder.interpreter.factory.TestRunFactory;
import com.sebuilder.interpreter.steptype.WriteTestCoverage;
import com.sebuilder.interpreter.webdriverfactory.Firefox;
import com.sebuilder.interpreter.webdriverfactory.WebDriverFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SeleniumConnector {
    public static WebDriverFactory DEFAULT_DRIVER_FACTORY = new Firefox();
    public static Log log;
    boolean useWriteTestCoverage = false;

    public SeleniumConnector(ArrayList<String> paths, ScriptFactory sf, WebDriverFactory wdf, HashMap<String, String> driverConfig) {
        for (String path : paths) {
            try {
                boolean scriptPassed = true;
                for (Script script : sf.parse(new File(path))) {
                    for (Map<String, String> data : script.dataRows) {
                        if (useWriteTestCoverage) {
                            script.steps.add(new Step(new WriteTestCoverage()));
                        }
                        try {
                            TestRun testRun = script.start(log, wdf, driverConfig, data);
                            while (testRun.hasNext()) {
                                boolean stepPassed = testRun.next();
                                scriptPassed = scriptPassed && stepPassed;
                                log.info("STEP -> " + script.name + ": " + testRun.currentStep() + (stepPassed ? " succeeded" : " failed"));
                            }
                            log.info("SCRIPT -> " + script.name + ": " + (scriptPassed ? " succeeded" : " failed"));
                        } catch (Exception e) {
                            log.info("SCRIPT -> " + script.name + " failed", e);
                        }
                    }
                }
            } catch (Exception e) {
                log.fatal("Run error.", e);
                System.exit(1);
            }
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: [--driver=<drivername] [--driver.<configkey>=<configvalue>...] [--implicitlyWait=<ms>] [--pageLoadTimeout=<ms>] [--stepTypePackage=<package name>] <script path>...");
            System.exit(0);
        }
        log = LogFactory.getFactory().getInstance(SeleniumConnector.class);
        WebDriverFactory wdf = DEFAULT_DRIVER_FACTORY;
        ScriptFactory sf = new ScriptFactory();
        StepTypeFactory stf = new StepTypeFactory();
        sf.setStepTypeFactory(stf);
        TestRunFactory trf = new TestRunFactory();
        sf.setTestRunFactory(trf);

        ArrayList<String> paths = new ArrayList<>();
        HashMap<String, String> driverConfig = new HashMap<>();
        for (String s : args) {
            if (s.startsWith("--")) {
                String[] kv = s.split("=", 2);
                if (kv.length < 2) {
                    log.fatal("Driver configuration option \"" + s + "\" is not of the form \"--driver=<name>\" or \"--driver.<key>=<value\".");
                    System.exit(1);
                }
                if (s.startsWith("--implicitlyWait")) {
                    trf.setImplicitlyWaitDriverTimeout(Integer.parseInt(kv[1]));
                } else if (s.startsWith("--pageLoadTimeout")) {
                    trf.setPageLoadDriverTimeout(Integer.parseInt(kv[1]));
                } else if (s.startsWith("--stepTypePackage")) {
                    stf.setPrimaryPackage(kv[1]);
                } else if (s.startsWith("--driver.")) {
                    driverConfig.put(kv[0].substring("--driver.".length()), kv[1]);
                } else if (s.startsWith("--driver")) {
                    try {
                        wdf = (WebDriverFactory) Class.forName("com.sebuilder.interpreter.webdriverfactory." + kv[1]).newInstance();
                    } catch (ClassNotFoundException e) {
                        log.fatal("Unknown WebDriverFactory: " + "com.sebuilder.interpreter.webdriverfactory." + kv[1], e);
                    } catch (InstantiationException e) {
                        log.fatal("Could not instantiate WebDriverFactory " + "com.sebuilder.interpreter.webdriverfactory." + kv[1], e);
                    } catch (IllegalAccessException e) {
                        log.fatal("Could not instantiate WebDriverFactory " + "com.sebuilder.interpreter.webdriverfactory." + kv[1], e);
                    }
                } else {
                    paths.add(s);
                }
            } else {
                paths.add(s);
            }
        }

        if (paths.isEmpty()) {
            log.info("Configuration successful but no paths to scripts specified. Exiting.");
            System.exit(0);
        }

        new SeleniumConnector(paths, sf, wdf, driverConfig);
    }
}
