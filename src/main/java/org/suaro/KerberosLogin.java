package org.suaro;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Arrays;

public class KerberosLogin {

    public static void main(String[] args) throws IOException, InterruptedException {
        boolean verbose = Arrays.asList(args).contains("-v");

        Console console = System.console();
        if (console == null) {
            throw new IllegalStateException("No console available");
        }

        String principal = console.readLine("Enter principal name: ");
        String salt = getSalt(principal, verbose);
        if (verbose) {
            System.out.println("Salt: " + salt);
        }
        createKeytab(principal, salt, verbose);
    }

    private static String getSalt(String principal, boolean verbose) throws IOException, InterruptedException {
        Console console = System.console();
        if (console == null) {
            throw new IllegalStateException("No console available");
        }

        char[] password = console.readPassword("Enter password for %s: ", principal);

        String[] kinitCmd = {
                "/bin/sh",
                "-c",
                "echo '" + new String(password) + "' | KRB5_TRACE=/dev/stdout kinit -V "
                        + principal
        };


        ProcessBuilder kinitProcessBuilder = new ProcessBuilder(kinitCmd);
        kinitProcessBuilder.redirectErrorStream(true);
        Process kinitProcess = kinitProcessBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(kinitProcess.getInputStream()));
        String line;
        String salt = null;

        // We need to check for single and double quotes
        //Pattern saltPattern = Pattern.compile(".*Selected etype info:.*salt \"(.*?)\".*");
        Pattern saltPattern = Pattern.compile(".*Selected etype info:.*salt [\"'](.*?)[\"'].*");

        while ((line = reader.readLine()) != null) {
            if (verbose) {
                System.out.println(line);
            }
            Matcher matcher = saltPattern.matcher(line);
            if (matcher.find()) {
                salt = matcher.group(1);
                break;
            }
        }

        kinitProcess.waitFor();
        if (salt == null) {
            throw new IllegalStateException("Salt not found in kinit output");
        }

        return salt;
    }

    private static void createKeytab(String principal, String salt, boolean verbose) throws IOException, InterruptedException {
        Console console = System.console();
        if (console == null) {
            throw new IllegalStateException("No console available");
        }

        char[] password = console.readPassword("Enter password for %s: ", principal);
        String keytabName = principal + ".keytab";

        ProcessBuilder ktutilProcessBuilder = new ProcessBuilder("ktutil");
        ktutilProcessBuilder.redirectErrorStream(true);
        Process ktutilProcess = ktutilProcessBuilder.start();

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ktutilProcess.getOutputStream()));
        BufferedReader reader = new BufferedReader(new InputStreamReader(ktutilProcess.getInputStream()));

        writer.write("addent -password -p " + principal + " -k 1 -e aes256-cts-hmac-sha1-96 -s " + salt + "\n");
        writer.flush();
        writer.write(new String(password) + "\n");
        writer.flush();
        writer.write("wkt " + keytabName + "\n");
        writer.flush();
        writer.write("quit\n");
        writer.flush();

        String line;
        while ((line = reader.readLine()) != null) {
            if (verbose) {
                System.out.println(line);
            }
        }

        ktutilProcess.waitFor();
        System.out.println("Keytab created: " + keytabName);
    }
}