/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Shared.Resource;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author Dave van Rijn, Student 500714558, Klas IS202
 */
public class FileUtil {

    public static final int COLOR = 0, LOGGED_USER = 1, USERS = 2, SELECTED_MEETING = 3;
    private static final String DIR = System.getProperty("user.home") + "\\TimeKeeper";
    private static final String FILE = DIR + "\\props.tk";
    private static String DATA_LOCATION = null;
    private static final Map<Integer, Object> PROPS = new TreeMap<>();

    public static void add(int title, Object object) {
        PROPS.put(title, object);
        write();
    }

    public static Object get(int title) {
        return PROPS.get(title);
    }

    public static Object remove(int title) {
        Object o = PROPS.remove(title);
        write();
        return o;
    }

    public static Object remove(Object object) {
        for (Entry<Integer, Object> e : PROPS.entrySet()) {
            if (e.getValue() == object) {
                return remove(e.getKey());
            }
        }
        return null;
    }

    public static void init(boolean read) throws IOException {
        File directory = new File(DIR);
        if (!directory.isDirectory()) { //Dir does not exist
            if (!directory.mkdirs()) {
                throw new IOException("Unable to create directories.");
            } else {
                init(read);
            }
        } else {
            File file = new File(FILE);
            file.createNewFile();
            if (file.length() > 0) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                DATA_LOCATION = reader.readLine();
                reader.close();
            } else {
                int option = JOptionPane.showOptionDialog(null, "The location "
                        + "of the data file is not yet chosen, do you want to "
                        + "choose it now or use the default location?", "Warning",
                        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
                        null, new Object[]{"Select now", "Use default"}, null);
                if (option == JOptionPane.YES_OPTION) {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    chooser.setApproveButtonText("Select");
                    chooser.showOpenDialog(null);
                    DATA_LOCATION = chooser.getSelectedFile().getAbsolutePath().replace("%20", " ") + "\\Data.tk";
                } else {
                    DATA_LOCATION = DIR + "\\Data.tk";
                }
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write(DATA_LOCATION);
                writer.close();
            }
            File data = new File(DATA_LOCATION);
            data.createNewFile();
            //File to store and read data created

            if (data.length() > 0) {
                //Data is present
                if (read) {
                    read();
                }
            }
        }
    }

    public static void setDataLocation(String location) {
        if (location != null && new File(location).isDirectory()) {
            File file;
            if (DATA_LOCATION != null && (file = new File(DATA_LOCATION)).exists()) {
                try {
                    //Copy to new location
                    Files.move(file.toPath(), new File(location).toPath(),
                            StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {
                    Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            DATA_LOCATION = location;
        }
    }
    
    public static String getDataLocation(){
        return DATA_LOCATION;
    }

    private static void write() {
        if (!PROPS.isEmpty()) {
            ObjectOutputStream output = null;
            try {
                init(false);
                File file = new File(DATA_LOCATION);
                output = new ObjectOutputStream(new FileOutputStream(file));
                for (Entry<Integer, Object> e : PROPS.entrySet()) {
                    output.writeObject(e.getKey());
                    output.writeObject(e.getValue());
                }
            } catch (IOException ex) {
                Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (output != null) {
                    try {
                        output.close();
                    } catch (IOException ex) {
                        Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    private static void read() {
        File data = new File(DATA_LOCATION);
        ObjectInputStream input = null;
        try {
            input = new ObjectInputStream(new FileInputStream(data));
            while (true) {
                int title = (Integer) input.readObject();
                Object object = input.readObject();
                addPrivate(title, object);
            }
        } catch (IOException | ClassNotFoundException ex) {
            if (ex instanceof EOFException) {
                System.out.println("End of data reached.");
            } else {
                Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ex) {
                    Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private static void addPrivate(int title, Object object) {
        PROPS.put(title, object);
    }
}
