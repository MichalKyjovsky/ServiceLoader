package cz.cuni.mff;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class MyServiceLoader {

    private static final String PROJECT_PATH = System.getProperty("user.dir") + "\\" + "src\\META-INF\\services\\";

    public static <T> Iterable<T> load(Class<T> cl) {
        try (BufferedReader br = new BufferedReader(new FileReader(PROJECT_PATH + cl.getName()))) {
            List<T> clazzes = new ArrayList<>();
            String line = "";
            while ((line = br.readLine()) != null) {
                Class<?> cls = Class.forName(line);
                if (cl.isAssignableFrom(cls)) {
                    clazzes.add(cl.cast(cls.getDeclaredConstructor().newInstance()));
                }
            }
            return clazzes;
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
