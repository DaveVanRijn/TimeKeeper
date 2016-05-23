/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Keeper.Object;

import Keeper.View.Keeper;
import Shared.Resource.FileUtil;
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
    private static Keeper keeper;

    public TrayManager(Keeper keeper) throws AWTException {
        icon = null;
        tray = null;
        image = null;
        popup = null;
        initialized = false;
        this.keeper = keeper;
        init();
    }

    public static void showMessage(String message, MessageType type) throws AWTException {
        if (!initialized) {
            init();
        }
        icon.displayMessage(CAPTION, message, type);
    }

    public static void remove() {
        tray.remove(icon);
    }

    private static void init() throws AWTException {
        final Font bold = new Font("Tahoma", Font.BOLD, 12),
                plain = new Font("Tahoma", Font.PLAIN, 12);
        tray = SystemTray.getSystemTray();
        image = new ImageIcon(TrayManager.class.getResource("/Keeper/Resource/clock.png")).getImage();
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
                hide.setFont(bold);
                show.setFont(plain);
                Keeper.showTime(false);
            }
        });
        show.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                show.setFont(bold);
                hide.setFont(plain);
                Keeper.showTime(true);
            }
        });
        otherColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color newColor = JColorChooser.showDialog(null, "Kies een kleur", Color.BLACK);
                if (newColor != null) {
                    Keeper.changeColor(newColor, keeper);
                    setColorLabels(color, otherColor);
                }
            }
        });
        blinkOn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                blinkOn.setFont(bold);
                blinkOff.setFont(plain);
                Keeper.setBlink(true);
            }
        });
        blinkOff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                blinkOff.setFont(bold);
                blinkOn.setFont(plain);
                Keeper.setBlink(false);
            }
        });

        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileUtil.add(FileUtil.SELECTED_MEETING, null);
                Keeper.openAgenda();
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
        popup.addSeparator();
        popup.add(show);
        popup.add(hide);
        popup.addSeparator();
        popup.add(exit);

        open.setFont(bold);

        //Enablings
        show.setFont(bold);
        blinkOn.setFont(bold);
        Color current = (Color) FileUtil.get(FileUtil.COLOR);
        if (current != null) {
            if (current.getRGB() == ORANGE.getRGB()) {
                orange.setFont(bold);
            } else if (current.getRGB() == BLUE.getRGB()) {
                blue.setFont(bold);
            } else if (current.getRGB() == Color.WHITE.getRGB()) {
                white.setFont(bold);
            } else if (current.getRGB() == Color.BLACK.getRGB()) {
                black.setFont(bold);
            } else {
                otherColor.setFont(bold);
            }
        } else {
            orange.setFont(bold);
        }

        //Make icon
        icon = new TrayIcon(image, CAPTION, popup);
        icon.setImageAutoSize(true);
        icon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    Keeper.openAgenda();
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
                Keeper.changeColor(color, keeper);
                setColorLabels(menu, item);
            }
        };
    }

    private static void setColorLabels(Menu menu, MenuItem item) {
        final Font bold = new Font("Tahoma", Font.BOLD, 12);
        final Font plain = new Font("Tahoma", Font.PLAIN, 12);
        int menuItems = menu.getItemCount();
        for (int i = 0; i < menuItems; i++) {
            MenuItem m = menu.getItem(i);
            if (m == item) {
                m.setFont(bold);
            } else {
                m.setFont(plain);
            }
        }
    }
}
