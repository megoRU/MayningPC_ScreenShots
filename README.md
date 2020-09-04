# MayningPC_ScreenShots

## Replace with your data:

private static final String remoteHost = "95.181.157.159"; //IP SFTP <br>
private static final String username = ""; //LOGIN <br>
private static final String password = ""; // PASS <br>
private static final String localFolderName = "C://MayningImagesFolder"; <br>
private static final String folderSFTP = "/var/www/vhosts/megolox.ru/httpdocs/mayningImages/"; //DIRECTORY ON SFTP SERVER <br>

## Remote time change:
Upload on Web server and: <br>
Change [h2 tag](https://github.com/megoRU/MayningPC_ScreenShots/blob/5145292afde4f62e601c3ba4358f898e83b91335/index.html#L9) <br>

## Run app:
Use .bat for run app:

Chcp 1251 <br>
cd C:\ <br>
java -jar MayningPC_ScreenShots.jar <br>

## FAQ & Troubleshooting

• On Windows/MacOS, install Oracle Java from [here](https://www.oracle.com/java/technologies/javase-downloads.html) or OpenJDK from [here](https://adoptopenjdk.net/).
