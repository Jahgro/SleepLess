import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.PopupMenu;
import java.awt.Robot;
import  java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SleepLess implements Runnable, ActionListener { 
  private Robot r;
  private PointerInfo pi; 
  static SystemTray tray = null;
  static TrayIcon trayIcon = null;
  static BufferedImage imageOn = null;
  static BufferedImage imageOff = null;
  static ActionListener alDoubleClickOn  = null;
  static ActionListener alDoubleClickOff = null;

  public SleepLess() {
    try{r = new Robot();}catch(Exception e) {System.exit(0);}
  }

  public static void main(String[] args) {
    tray = SystemTray.getSystemTray();
    SleepLess b = new SleepLess();
    Thread t = new Thread(b);
    t.start();                 // Start running

    if (SystemTray.isSupported()) {  // Create system icon
      // Get icon image
      try {
        imageOn  = ImageIO.read(SleepLess.class.getResource("/icon/zzz.gif"));
        imageOff = ImageIO.read(SleepLess.class.getResource("/icon/aaa.gif"));
      } catch (IOException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }

      // Create icon menu
      PopupMenu popup = new PopupMenu();
      MenuItem exit = new MenuItem("Exit");
      popup.add(exit);

      trayIcon = new TrayIcon(imageOn, "SleepLess", popup);

      // Create ActionListeners
      ActionListener alExit = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          System.out.println("Exiting...");
          System.exit(0);
        }
      };

      /*ActionListener*/ alDoubleClickOn = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          trayIcon.setImage(imageOff);
          trayIcon.removeActionListener(alDoubleClickOn);
          trayIcon.addActionListener(alDoubleClickOff);
          System.out.println("Click On...");
          //System.exit(0);
        }
      };

      /*ActionListener*/ alDoubleClickOff = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          trayIcon.setImage(imageOn);
          trayIcon.removeActionListener(alDoubleClickOff);
          trayIcon.addActionListener(alDoubleClickOn);
          System.out.println("Click Off...");
          //System.exit(0);
        }
      };

    trayIcon.setImageAutoSize(true);
    trayIcon.addActionListener(alDoubleClickOn);
    exit.addActionListener(alExit);

    try {
      tray.add(trayIcon);
    }
    catch (AWTException e) {
        System.err.println("TrayIcon could not be added.");
      }
    }   
    else {
      //  System Tray is not supported
    }
  }

  public void run() {
    // We want to run forever now
    //int cycles = 1;
    //try {
    //  cycles = Integer.parseInt(JOptionPane.showInputDialog(null, "How many minutes?"));
    //}
    //catch(Exception e) {
    //  cycles = 60; 
    //}
    
    int ctr = 1;
    while( true /* ctr <= cycles */) {
      r.delay(60000);
      ctr++;

      pi = MouseInfo.getPointerInfo();
      int x = pi.getLocation().x;
      int y = pi.getLocation().y;

      r.mouseMove(x,y);
      if(ctr%5==0) System.gc();
    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    // TODO Auto-generated method stub
  }
}
