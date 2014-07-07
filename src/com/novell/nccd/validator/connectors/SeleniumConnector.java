package com.novell.nccd.validator.connectors;

import com.sebuilder.interpreter.SeInterpreter;

import java.io.IOException;
import java.io.InputStream;

public class SeleniumConnector {
   public SeleniumConnector() throws IOException {
    }

    public static void main(String[] args) throws IOException {
        SeInterpreter.main(args);


        Process proc = Runtime.getRuntime().exec("java -cp lib\\* com.sebuilder.interpreter.SeInterpreter c:\\screenshots\\test.json");
        InputStream in = proc.getInputStream();
        InputStream err = proc.getErrorStream();
        System.out.print("In: " + in.read());
        System.out.print("Err: " + err.read());
    }


}
