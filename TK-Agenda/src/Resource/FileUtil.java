/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Resource;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dave van Rijn, Student 500714558, Klas IS202
 */
public class FileUtil {

    public static final int COLOR = 0, LOGGED_USER = 1, USERS = 2;
    private static final String DIR = System.getProperty("user.home") + "\\TimeKeeper";
    private static final String FILE = DIR + "\\props.tk";
    private static final Map<Integer, Object> PROPS = new TreeMap<>();

    public static void add(int title, Object object) {
        PROPS.put(title, object);
        write();
    }

    public static Object get(int title) {
        return PROPS.get(title);
    }

    public static Object remove(int title) {
        return PROPS.remove(title);
    }

    public static Object remove(Object object) {
        for (Entry<Integer, Object> e : PROPS.entrySet()) {
            if (e.getValue() == object) {
                return remove(e.getKey());
            }
        }
        return null;
    }

    private static void write() {
        if (!PROPS.isEmpty()) {
            ObjectOutputStream output = null;
            File file = new File(FILE);

            try {
                init();
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

    public static void read() {
        PROPS.clear();
        ObjectInputStream input = null;
        File file = new File(FILE);

        try {
            init();
            input = new ObjectInputStream(new FileInputStream(file));
            while (true) {
                int title = (Integer) input.readObject();
                Object object = input.readObject();
                addPrivate(title, object);
            }
        } catch (IOException | ClassNotFoundException ex) {
            if (ex instanceof EOFException) {
                System.out.println("End of file reached.");
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

    private static void init() throws IOException {
        File directory = new File(DIR);
        if (!directory.isDirectory()) {
            if (!directory.mkdirs()) {
                throw new IOException("Unable to create directories.");
            } else {
                init();
            }
        } else {
            File file = new File(FILE);
            file.createNewFile();
        }
    }

    private static void addPrivate(int title, Object object) {
        PROPS.put(title, object);
    }
}
