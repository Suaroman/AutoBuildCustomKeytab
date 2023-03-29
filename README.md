# AutoBuildCustomKeytab
Automatically creates a keytab derived from an interactive kerberos login.
Salt value is fetched from the interactive session and used in the construction of the keytab.



<b>AutoBuildCustomKeytab</b> is a lightweight console application which automatically creates a user keytab based on encyption
and salt values fetched from an interactive kerberos session.<br>

The goal is to reduce complexity of creating a user keytab in an ESP HDInsight cluster where RC4 encryption is disabled at the domain controller and the user doesn't know what salt value to provide in the ktutil addent. .<br>

The tool build and construct the keytab automatically.

**Note**: 

Currently, HDInsight cluster with Ubuntu 18.04 LTS come with MIT Kerberos version 1.16 which does _not_ support fetch (-f ) operation in Ktuils addent.  
Future versions of HDInsight with Ubuntu 20 (or higher) will support fetch and this tool will no longer be needed. 





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
