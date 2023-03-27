# AutoBuildCustomKeytab
Automatically creates a keytab derived from an interactive kerberos login.
Salt value is fetched from the interactive session and used in the construction of the keytab.



<b>AutoBuildCustomKeytab</b> is a lightweight console application which automatically creates a user keytab based on encyption

and salt values fetched from an interactive kerberos session.<br>

The goal is to reduce complexity and error associated wtih creating custom keytabs where RC4 is disabled at the domain controller. .<br>


Please note it's currently in an alpha state -<br>



<h3>Installation/Usage</h3>
Click on the <a href="https://github.com/Suaroman/AutoBuildCustomKeytab/releases"> releases tab</a>, download the
most recent jar and copy it to the machine where you would like to generate the keytab.


Usage scenario below

```
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

Enter principal name: alt-gregorys
Enter password for alt-gregorys:
Using default cache: /tmp/krb5cc_2020
Using principal: alt-gregorys@HDINSIGHTCSS.COM
[14342] 1679521837.666157: Getting initial credentials for alt-gregorys@HDINSIGHTCSS.COM
[14342] 1679521837.666159: Sending unauthenticated request
[14342] 1679521837.666160: Sending request (207 bytes) to HDINSIGHTCSS.COM
[14342] 1679521837.666161: Sending DNS URI query for _kerberos.HDINSIGHTCSS.COM.
[14342] 1679521837.666162: No URI records found
[14342] 1679521837.666163: Sending DNS SRV query for _kerberos._udp.HDINSIGHTCSS.COM.
[14342] 1679521837.666164: SRV answer: 0 100 88 "nxzncnqms4bz1e9.hdinsightcss.com."
[14342] 1679521837.666165: SRV answer: 0 100 88 "kpgqapkkp-hwiyb.hdinsightcss.com."
[14342] 1679521837.666166: Sending DNS SRV query for _kerberos._tcp.HDINSIGHTCSS.COM.
[14342] 1679521837.666167: SRV answer: 0 100 88 "kpgqapkkp-hwiyb.hdinsightcss.com."
[14342] 1679521837.666168: SRV answer: 0 100 88 "nxzncnqms4bz1e9.hdinsightcss.com."
[14342] 1679521837.666169: Resolving hostname nxzncnqms4bz1e9.hdinsightcss.com.
[14342] 1679521837.666170: Resolving hostname kpgqapkkp-hwiyb.hdinsightcss.com.
[14342] 1679521837.666171: Resolving hostname kpgqapkkp-hwiyb.hdinsightcss.com.
[14342] 1679521837.666172: Initiating TCP connection to stream 10.0.0.5:88
[14342] 1679521837.666173: Sending TCP request to stream 10.0.0.5:88
[14342] 1679521837.666174: Received answer (216 bytes) from stream 10.0.0.5:88
[14342] 1679521837.666175: Terminating TCP connection to stream 10.0.0.5:88
[14342] 1679521837.666176: Sending DNS URI query for _kerberos.HDINSIGHTCSS.COM.
[14342] 1679521837.666177: No URI records found
[14342] 1679521837.666178: Sending DNS SRV query for _kerberos-master._tcp.HDINSIGHTCSS.COM.
[14342] 1679521837.666179: No SRV records found
[14342] 1679521837.666180: Response was not from master KDC
[14342] 1679521837.666181: Received error from KDC: -1765328359/Additional pre-authentication required
[14342] 1679521837.666184: Preauthenticating using KDC method data
[14342] 1679521837.666185: Processing preauth types: 16, 15, 19, 2
[14342] 1679521837.666186: Selected etype info: etype aes256-cts, salt "HDINSIGHTCSS.ONMICROSOFT.COMalt-gregorys", params ""
Salt: HDINSIGHTCSS.ONMICROSOFT.COMalt-gregorys
Enter password for alt-gregorys:
ktutil:  addent -password -p alt-gregorys@HDINSIGHTCSS.COM -k 1 -e aes256-cts-hma
ac-sha1-96 -s HDINSIGHTCSS.ONMICROSOFT.COMalt-gregorys
Password for alt-gregorys@HDINSIGHTCSS.COM:
ktutil:  wkt alt-gregorys.keytab
ktutil:  quit


Keytab created: alt-gregorys.keytab