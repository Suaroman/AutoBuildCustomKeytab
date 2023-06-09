# AutoBuildCustomKeytab

AutoBuildCustomKeytab is a lightweight console application that automatically generates a keytab derived from an interactive Kerberos login session. The salt value is fetched from the interactive session and utilized for constructing the keytab.

The primary goal of AutoBuildCustomKeytab is to simplify the process of creating a user keytab in an ESP HDInsight cluster where RC4 encryption is disabled at the domain controller, and the user is unaware of the correct salt value to provide in the `ktutil addent` command. This tool automates the keytab creation and construction process, reducing complexity and the potential for errors.

**Note**:

HDInsight clusters with Ubuntu 18.04 LTS currently use MIT Kerberos version 1.16, which does *not* support the fetch (-f) operation in `ktutil addent`. Future versions of HDInsight with Ubuntu 20 (or higher) will include support for the fetch operation, eliminating the need for this tool.

## Tips for Using AutoBuildCustomKeytab

1. Ensure that you have the appropriate permissions and access to the Kerberos environment before attempting to use AutoBuildCustomKeytab.

2. Always verify the keytab generated by AutoBuildCustomKeytab to ensure that it contains the correct encryption and salt values.

3. Make sure to keep the keytab file secure, as it contains sensitive information that could potentially be used to compromise your environment.

4. Regularly update your keytab file to ensure compatibility with any changes to the Kerberos environment or encryption protocols.



<h3>Installation/Usage</h3>
Click on the <a href="https://github.com/Suaroman/AutoBuildCustomKeytab/releases"> releases tab</a>, download the
most recent jar and copy it to the machine where you would like to generate the keytab.


Usage scenario below

```

$ wget https://github.com/Suaroman/AutoBuildCustomKeytab/releases/download/prerelease/AutoBuildCustomKeytab.jar

$ java -jar AutoBuildCustomKeytab.jar

Enter principal name: alt-gregorys
Enter password for alt-gregorys:
Enter password for alt-gregorys:
Keytab created: alt-gregorys.keytab



$ ls -l
total 8
-rw-rw-r-- 1 hdiuser hdiuser 3025 Mar 22 20:32 AutoBuildCustomKeytab.jar
-rw------- 1 hdiuser hdiuser   89 Mar 22 20:33 alt-gregorys.keytab        <<< new keytab created 




# Let's try it out ...


$ kinit -kt alt-gregorys.keytab alt-gregorys



# no prompt for password so it works ...


$ klist

Ticket cache: FILE:/tmp/krb5cc_2020
Default principal: alt-gregorys@HDINSIGHTCSS.COM

Valid starting     Expires            Service principal
03/22/23 20:33:25  03/23/23 06:33:25  krbtgt/HDINSIGHTCSS.COM@HDINSIGHTCSS.COM
        renew until 03/29/23 20:33:25






# The tool also supports verbose mode in case something doesn't work . Just use the -v option.


$ java -jar AutoBuildCustomKeytab.jar -v
```
