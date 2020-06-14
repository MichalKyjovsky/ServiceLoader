package cz.cuni.mff;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;


public class MyServiceLoader {
    private static final String META_INF = "META-INF/services/";

    public static <T> Iterable<T> load(Class<T> cl) {
        List<T> plugins = new ArrayList<>();
        try  {
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            Enumeration<URL> urls = classLoader.getResources(META_INF + cl.getCanonicalName());

            while (urls.hasMoreElements()) {
                URL service = urls.nextElement();

                try (BufferedReader br = new BufferedReader(new InputStreamReader(service.openStream()))) {
                    String line;

                    while ((line = br.readLine()) != null) {
                        Class<?> cls = Class.forName(line);
                        if (cl.isAssignableFrom(cls)) {
                            plugins.add(cl.cast(cls.getDeclaredConstructor().newInstance()));
                        }
                    }
                }
            }
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return plugins;
    }
}
