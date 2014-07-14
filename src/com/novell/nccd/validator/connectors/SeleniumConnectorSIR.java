package com.novell.nccd.validator.connectors;

/*
import com.sebuilder.interpreter.SeInterpreter;
import jp.vmi.selenium.selenese.Runner;
import jp.vmi.selenium.selenese.Selenese;
import jp.vmi.selenium.selenese.TestSuite;
import jp.vmi.selenium.selenese.log.CookieFilter;
import jp.vmi.selenium.selenese.result.Result;
import jp.vmi.selenium.selenese.result.Unexecuted;
import jp.vmi.selenium.webdriver.DriverOptions;
import jp.vmi.selenium.webdriver.WebDriverManager;
import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
  */
@SuppressWarnings("javadoc")
@Deprecated
public class SeleniumConnectorSIR {
/*
    public SeleniumConnectorSIR(String[] args) throws IOException {
        useSRMain(args);
        //useSeleneseRunner(args);
//        useSeInterpreter(args);
    }

    private static class SROptions extends Options {
        private static final long serialVersionUID = 1L;

        private int i = 0;

        public final Map<Option, Integer> optionOrder = new HashMap<Option, Integer>();

        @Override
        public Options addOption(Option opt) {
            optionOrder.put(opt, ++i);
            return super.addOption(opt);
        }

        public Comparator<Option> getOptionComparator() {
            return new Comparator<Option>() {
                @Override
                public int compare(Option o1, Option o2) {
                    return optionOrder.get(o1) - optionOrder.get(o2);
                }
            };
        }
    }

    private final SROptions options = new SROptions();
    public CommandLine parseCommandLine(String... args) throws IllegalArgumentException {
        CommandLine cli;
        try {
            cli = new PosixParser().parse(options, args);
            System.out.println("Specified options:");
            for (Option opt : cli.getOptions()) {
                System.out.println("[{" + opt.getLongOpt() + "}]=[{" + opt.getValue() + "}]");
            }
        } catch (ParseException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
        return cli;
    }

    public static void main(String[] args) throws IOException {
        new SeleniumConnectorSIR(args);
    }

    public void useSRMain(String[] args) {
        options.addOption(OptionBuilder
                .withLongOpt("driver")
                .hasArg()
                .withArgName("driver")
                .withDescription(
                        "firefox (default) | chrome | ie | safari | htmlunit | phantomjs | remote | appium | FQCN-of-WebDriverFactory")
                .create('d'));
        options.addOption(OptionBuilder.withLongOpt("profile")
                .hasArg().withArgName("name")
                .withDescription("profile name (Firefox only)")
                .create('p'));
        options.addOption(OptionBuilder.withLongOpt("profile-dir")
                .hasArg().withArgName("dir")
                .withDescription("profile directory (Firefox only)")
                .create('P'));
        options.addOption(OptionBuilder.withLongOpt("proxy")
                .hasArg().withArgName("proxy")
                .withDescription("proxy host and port (HOST:PORT) (excepting IE)")
                .create());
        options.addOption(OptionBuilder.withLongOpt("proxy-user")
                .hasArg().withArgName("user")
                .withDescription("proxy username (HtmlUnit only *)")
                .create());
        options.addOption(OptionBuilder.withLongOpt("proxy-password")
                .hasArg().withArgName("password")
                .withDescription("proxy password (HtmlUnit only *)")
                .create());
        options.addOption(OptionBuilder.withLongOpt("no-proxy")
                .hasArg().withArgName("no-proxy")
                .withDescription("no-proxy hosts")
                .create());
        options.addOption(OptionBuilder.withLongOpt("remote-url")
                .hasArg().withArgName("url")
                .withDescription("Remote test runner URL (Remote only)")
                .create());
        options.addOption(OptionBuilder.withLongOpt("remote-platform")
                .hasArg().withArgName("platform")
                .withDescription("Desired remote platform (Remote only)")
                .create());
        options.addOption(OptionBuilder.withLongOpt("remote-browser")
                .hasArg().withArgName("browser")
                .withDescription("Desired remote browser (Remote only)")
                .create());
        options.addOption(OptionBuilder.withLongOpt("remote-version")
                .hasArg().withArgName("browser-version")
                .withDescription("Desired remote browser version (Remote only)")
                .create());
        options.addOption(OptionBuilder.withLongOpt("highlight")
                .withDescription("highlight locator always.")
                .create('H'));
        options.addOption(OptionBuilder.withLongOpt("screenshot-dir")
                .hasArg().withArgName("dir")
                .withDescription("override captureEntirePageScreenshot directory.")
                .create('s'));
        options.addOption(OptionBuilder.withLongOpt("screenshot-all")
                .hasArg().withArgName("dir")
                .withDescription("take screenshot at all commands to specified directory.")
                .create('S'));
        options.addOption(OptionBuilder.withLongOpt("screenshot-on-fail")
                .hasArg().withArgName("dir")
                .withDescription("take screenshot on fail commands to specified directory.")
                .create());
        options.addOption(OptionBuilder.withLongOpt("ignore-screenshot-command")
                .withDescription("ignore captureEntirePageScreenshot command.")
                .create());
        options.addOption(OptionBuilder.withLongOpt("baseurl")
                .hasArg().withArgName("baseURL")
                .withDescription("override base URL set in selenese.")
                .create('b'));
        options.addOption(OptionBuilder.withLongOpt("chromedriver")
                .hasArg().withArgName("path")
                .withDescription("path to 'chromedriver' binary. (implies '--driver chrome')")
                .create());
        options.addOption(OptionBuilder.withLongOpt("iedriver")
                .hasArg().withArgName("path")
                .withDescription("path to 'IEDriverServer' binary. (implies '--driver ie')")
                .create());
        options.addOption(OptionBuilder.withLongOpt("phantomjs")
                .hasArg().withArgName("path")
                .withDescription("path to 'phantomjs' binary. (implies '--driver phantomjs')")
                .create());
        options.addOption(OptionBuilder.withLongOpt("xml-result")
                .hasArg().withArgName("dir")
                .withDescription("output XML JUnit results to specified directory.")
                .create());
        options.addOption(OptionBuilder.withLongOpt("html-result")
                .hasArg().withArgName("dir")
                .withDescription("output HTML results to specified directory.")
                .create());
        options.addOption(OptionBuilder.withLongOpt("timeout")
                .hasArg().withArgName("timeout")
                .withDescription("set timeout (ms) for waiting. (default: " + 3000 + " ms)")
                .create('t'));
        options.addOption(OptionBuilder.withLongOpt("set-speed")
                .hasArg().withArgName("speed")
                .withDescription("same as executing setSpeed(ms) command first.")
                .create());
        options.addOption(OptionBuilder.withLongOpt("height")
                .hasArg().withArgName("height")
                .withDescription("set initial height. (excluding mobile)")
                .create());
        options.addOption(OptionBuilder.withLongOpt("width")
                .hasArg().withArgName("width")
                .withDescription("set initial width. (excluding mobile)")
                .create());
        options.addOption(OptionBuilder.withLongOpt("define")
                .hasArg().withArgName("key=value or key+=value")
                .withDescription("define parameters for capabilities. (multiple)")
                .create('D'));
        options.addOption(OptionBuilder.withLongOpt("rollup")
                .hasArg().withArgName("file")
                .withDescription("define rollup rule by JavaScript. (multiple)")
                .create());
        options.addOption(OptionBuilder.withLongOpt("cookie-filter")
                .hasArg().withArgName("+RE|-RE")
                .withDescription("filter cookies to log by RE matching the name. (\"+\" is passing, \"-\" is ignoring)")
                .create());
        options.addOption(OptionBuilder.withLongOpt("help")
                .withDescription("show this message.")
                .create('h'));
        int exitCode = 1;
        try {
            CommandLine cli = parseCommandLine(args);
            String[] filenames = cli.getArgs();
            if (filenames.length == 0) {
                return;
            }
            System.out.println("Start: " + "PROG_TITLE" + " {getVersion()}");
            String driverName = cli.getOptionValue("driver");
            DriverOptions driverOptions = new DriverOptions(cli);
            if (driverName == null) {
                if (driverOptions.has(DriverOptions.DriverOption.CHROMEDRIVER))
                    driverName = WebDriverManager.CHROME;
                else if (driverOptions.has(DriverOptions.DriverOption.IEDRIVER))
                    driverName = WebDriverManager.IE;
                else if (driverOptions.has(DriverOptions.DriverOption.PHANTOMJS))
                    driverName = WebDriverManager.PHANTOMJS;
            }
            WebDriverManager manager = WebDriverManager.getInstance();
            manager.setWebDriverFactory(driverName);
            manager.setDriverOptions(driverOptions);
            Runner runner = new Runner();
            runner.setDriver(manager.get());
            runner.setHighlight(cli.hasOption("highlight"));
            runner.setScreenshotDir(cli.getOptionValue("screenshot-dir"));
            runner.setScreenshotAllDir(cli.getOptionValue("screenshot-all"));
            runner.setScreenshotOnFailDir(cli.getOptionValue("screenshot-on-fail"));
            runner.setOverridingBaseURL(cli.getOptionValue("baseurl"));
            runner.setIgnoredScreenshotCommand(cli.hasOption("ignore-screenshot-command"));
            if (cli.hasOption("rollup")) {
                String[] rollups = cli.getOptionValues("rollup");
                for (String rollup : rollups)
                    runner.getRollupRules().load(rollup);
            }
            if (cli.hasOption("cookie-filter")) {
                String cookieFilter = cli.getOptionValue("cookie-filter");
                if (cookieFilter.length() < 2)
                    throw new IllegalArgumentException("invalid cookie filter format: " + cookieFilter);
                CookieFilter.FilterType filterType;
                switch (cookieFilter.charAt(0)) {
                    case '+':
                        filterType = CookieFilter.FilterType.PASS;
                        break;
                    case '-':
                        filterType = CookieFilter.FilterType.SKIP;
                        break;
                    default:
                        throw new IllegalArgumentException("invalid cookie filter format: " + cookieFilter);
                }
                String pattern = cookieFilter.substring(1);
                runner.setCookieFilter(new CookieFilter(filterType, pattern));
            }
            runner.setJUnitResultDir(cli.getOptionValue("xml-result"));
            runner.setHtmlResultDir(cli.getOptionValue("html-result"));
            int timeout = NumberUtils.toInt(cli.getOptionValue("timeout", "30000"));
            if (timeout <= 0)
                throw new IllegalArgumentException("Invalid timeout value. (" + cli.getOptionValue("timeout") + ")");
            runner.setTimeout(timeout);
            int speed = NumberUtils.toInt(cli.getOptionValue("set-speed", "0"));
            if (speed < 0)
                throw new IllegalArgumentException("Invalid speed value. (" + cli.getOptionValue("set-speed") + ")");
            runner.setInitialSpeed(speed);
            runner.setPrintStream(System.out);
            Result totalResult = runner.run(filenames);
            runner.finish();
            exitCode = totalResult.getLevel().exitCode;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            runner.finish();
        } catch (Throwable t) {
            t.printStackTrace();
            runner.finish();
        }
    }
    @Rule
    public static final WebServerResource wsr = new WebServerResource();
    @Rule
    public final TemporaryFolder screenshotDir = new TemporaryFolder();
    @Rule
    public final TemporaryFolder screenshotOnFailDir = new TemporaryFolder();
    @Rule
    public final TemporaryFolder xmlResultDir = new TemporaryFolder();
    public Runner runner;
    public List<TestSuite> testSuites;
    public Result result;
    public String xmlResult;

    @Test
    public void useSeleneseRunner(String[] args) {
        String scriptName = "c:\\\\screenshots\\test.json";
        result = Unexecuted.UNEXECUTED;
        xmlResult = null;
        try {
            String scriptFile = getScriptFile(scriptName);
            WebDriverManager manager = WebDriverManager.getInstance();
            manager.setWebDriverFactory(WebDriverManager.HTMLUNIT);
            manager.setDriverOptions(new DriverOptions());
            Runner runner = new Runner();
            runner.setDriver(manager.get());
            //runner.setOverridingBaseURL(wsr.getBaseURL());
            runner.setScreenshotDir(screenshotDir.getRoot().getPath());
            runner.setScreenshotOnFailDir(screenshotOnFailDir.getRoot().getPath());
            runner.setJUnitResultDir(xmlResultDir.getRoot().getPath());
            result = runner.run(scriptFile);
            if (testSuites.isEmpty()) {
                xmlResult = null;
            } else {
                String xmlFile = String.format("TEST-%s.xml", testSuites.get(0).getName());
                xmlResult = FileUtils.readFileToString(new File(xmlResultDir.getRoot(), xmlFile), "UTF-8");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            runner.setJUnitResultDir(null);
        }
    }


/*
    protected static interface Filter {
        String filter(String line);
    }
    protected List<String> getSystemOut(Filter filter) {
        List<String> list = new ArrayList<String>();
        Pattern re = Pattern.compile("<system-out>(.*?)</system-out>", Pattern.DOTALL);
        Matcher m = re.matcher(xmlResult);
        if (!m.find())
            return list;
        String systemOut = StringEscapeUtils.unescapeXml(m.group(1));
        for (String line : systemOut.split("\r?\n|\r")) {
            String filtered = filter.filter(line);
            if (filtered != null)
                list.add(filtered);
        }
        return list;
    }
    protected List<String> listFilter(Filter filter, List<String> src) {
        List<String> dst = new ArrayList<String>();
        for (String line : src) {
            String filtered = filter.filter(line);
            if (filtered != null)
                dst.add(filtered);
        }
        return dst;
    }
*/

  /*
    public static String getScriptFile(String name) {
        String html = name;
        URL resource = Class.class.getResource(html);
        if (resource == null)
            throw new RuntimeException(new FileNotFoundException(html));
        try {
            return new File(resource.toURI()).getCanonicalPath();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }





    public void useSeInterpreter(String[] args) throws IOException {
        // Pros:
        //  * less code
        //  * easier to understand
        // Cons:
        //  * no apparent hook to getting the logging, results, or anything.
        //      We may be able to get exceptions, if any exist.
        //      We may be able to configure this, since it uses LoggerUtils. But, we'd likely need to configure the environment, since it doesn't pass any of the parameters to the LoggerUtils class.
        //          The default output sends to the console: "INFO: c:\screenshots\test.json succeeded"
        String[] ourArgs = args;  // We can set the values pragmatically
        SeInterpreter.main(ourArgs);


        // Pros:
        //  * Get access to the running process, so we may have more flexibility
        // Cons:
        //  * Much harder/more brittle to pass configuration
        //  * Harder to read
        //  * Need to figure out how to interpret the error codes
        Process proc = Runtime.getRuntime().exec("java -cp lib\\* com.sebuilder.interpreter.SeInterpreter c:\\screenshots\\test.json");
        InputStream in = proc.getInputStream();
        InputStream err = proc.getErrorStream();
        System.out.print("In: " + in.read());
        System.out.print("Err: " + err.read());
    }
  */
}
