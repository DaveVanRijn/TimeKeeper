/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TimeKeeper;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;

/**
 *
 * @author Dave van Rijn, Student 500714558, Klas IS202
 */
public class TrayManager {

    private static final String CAPTION = "TimeKeeper";
    private static final Color ORANGE = new Color(255, 102, 0), BLUE = new Color(51, 102, 255);

    private static TrayIcon icon;
    private static SystemTray tray;
    private static Image image;
    private static PopupMenu popup;
    private static boolean initialized;

    public TrayManager() throws AWTException {
        icon = null;
        tray = null;
        image = null;
        popup = null;
        initialized = false;
        init();
    }

    public static void showMessage(String message, MessageType type) throws AWTException {
        if (!initialized) {
            init();
        }
        icon.displayMessage(CAPTION, message, type);
    }

    private static void remove() {
        tray.remove(icon);
    }

    private static void init() throws AWTException {
        tray = SystemTray.getSystemTray();
        image = new ImageIcon(TrayManager.class.getResource("/Resource/clock.png")).getImage();
        popup = new PopupMenu();

        //MenuItems
        //Standard actions
        MenuItem exit = new MenuItem("Sluiten");
        MenuItem hide = new MenuItem("Verberg");
        MenuItem show = new MenuItem("Toon");
        MenuItem open = new MenuItem("Open App");

        //Color options
        MenuItem orange = new MenuItem("  Oranje");
        MenuItem blue = new MenuItem("  Blauw");
        MenuItem black = new MenuItem("  Zwart");
        MenuItem white = new MenuItem("  Wit");
        MenuItem otherColor = new MenuItem("  Anders..");

        //Blink options
        MenuItem blinkOn = new MenuItem("Aan");
        MenuItem blinkOff = new MenuItem("Uit");

        //Menu's 
        Menu color = new Menu("Kleur");
        Menu blink = new Menu("Knipperen");

        //ActionListeners
        orange.addActionListener(getColorListener(ORANGE, orange, color));
        blue.addActionListener(getColorListener(BLUE, blue, color));
        black.addActionListener(getColorListener(Color.BLACK, black, color));
        white.addActionListener(getColorListener(Color.WHITE, white, color));
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remove();
                System.exit(0);
            }
        });
        hide.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hide.setEnabled(false);
                show.setEnabled(true);
                Keeper.showTime(false);
            }
        });
        show.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                show.setEnabled(false);
                hide.setEnabled(true);
                Keeper.showTime(true);
            }
        });
        otherColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color newColor = JColorChooser.showDialog(null, "Kies een kleur", Color.BLACK);
                if (newColor != null) {
                    Keeper.changeColor(newColor);
                    setColorLabels(color, otherColor);
                }
            }
        });
        blinkOn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                blinkOn.setEnabled(false);
                blinkOff.setEnabled(true);
                Keeper.setBlink(true);
            }
        });
        blinkOff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                blinkOff.setEnabled(false);
                blinkOn.setEnabled(true);
                Keeper.setBlink(false);
            }
        });

        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread th = new Thread() {
                    @Override
                    public void run() {
                        openAgenda();
                    }
                };
                th.start();
            }
        });

        //Build up
        color.add(orange);
        color.add(blue);
        color.add(black);
        color.add(white);
        color.add(otherColor);

        blink.add(blinkOn);
        blink.add(blinkOff);

        popup.add(open);
        popup.addSeparator();
        popup.add(color);
        popup.add(blink);
        popup.add(show);
        popup.add(hide);
        popup.addSeparator();
        popup.add(exit);

        open.setFont(new Font("Tahoma", Font.BOLD, 12));

        //Enablings
        show.setEnabled(false);
        blinkOn.setEnabled(false);

        //Make icon
        icon = new TrayIcon(image, CAPTION, popup);
        icon.setImageAutoSize(true);
        icon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    Thread th = new Thread() {
                        @Override
                        public void run() {
                            openAgenda();
                        }
                    };
                    th.start();
                }
            }
        });
        tray.add(icon);
        initialized = true;
    }

    private static ActionListener getColorListener(Color color, MenuItem item, Menu menu) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Keeper.changeColor(color);
                setColorLabels(menu, item);
            }
        };
    }

    private static void setColorLabels(Menu menu, MenuItem item) {
        int menuItems = menu.getItemCount();
        for (int i = 0; i < menuItems; i++) {
            MenuItem m = menu.getItem(i);
            String prefix;
            if (m == item) {
                prefix = "* ";
            } else {
                prefix = "  ";
            }
            m.setLabel(prefix + m.getLabel().substring(2));
        }
    }

    private static void openAgenda() {
        String app = System.getProperty("user.dir") + "\\TK-Agenda.jar";
        try {
            Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", "java -jar " + app});
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(TrayManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
