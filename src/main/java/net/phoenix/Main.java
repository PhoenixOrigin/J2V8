package net.phoenix;

import com.eclipsesource.v8.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        V8 v8 = V8.createV8Runtime();


        JavaVoidCallback print = (receiver, parameters) -> {
            if (parameters.length() > 0) {
                Object arg1 = parameters.get(0);
                System.out.print(arg1);
                if (arg1 instanceof Releasable) {
                    ((Releasable) arg1).release();
                }
            }
        };
        v8.registerJavaMethod(print, "print");

        JavaVoidCallback printf = (receiver, parameters) -> {
            if (parameters.length() > 0) {
                Object arg1 = parameters.get(0);
                System.out.printf(arg1.toString(), Arrays.asList(parameters).remove(0));
                if (arg1 instanceof Releasable) {
                    ((Releasable) arg1).release();
                }
            }
        };
        v8.registerJavaMethod(print, "printf");

        JavaVoidCallback println = (receiver, parameters) -> {
            if (parameters.length() > 0) {
                Object arg1 = parameters.get(0);
                System.out.println(arg1.toString());
                if (arg1 instanceof Releasable) {
                    ((Releasable) arg1).release();
                }
            }
        };
        v8.registerJavaMethod(print, "println");

        try(InputStream is = Main.class.getClassLoader().getResourceAsStream("index.js")) {
            String code = isstring(is);
            v8.executeScript(code);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        v8.release();
    }

    public static String isstring(InputStream stream) throws IOException {
        int bufferSize = 1024;
        char[] buffer = new char[bufferSize];
        StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(stream, StandardCharsets.UTF_8);
        for (int numRead; (numRead = in.read(buffer, 0, buffer.length)) > 0; ) {
            out.append(buffer, 0, numRead);
        }
        return out.toString();
    }
}