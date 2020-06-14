package cz.cuni.mff;

import javax.naming.MalformedLinkException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

public class MyServiceLoader {

    private static final String PROJECT_PATH = System.getProperty("user.dir") + "\\" + "src\\META-INF\\services\\";

    public static <T> Iterable<T> load(Class<T> cl) {
        try (BufferedReader br = new BufferedReader(new FileReader(PROJECT_PATH + cl.getName()))) {
            List<T> clazzes = new ArrayList<>();
            String line = "";
            URLClassLoader urlClassLoader = getJarsFromClassPath();
            while ((line = br.readLine()) != null) {
                Class<?> cls = Class.forName(line, true, urlClassLoader);
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

    private static URLClassLoader getJarsFromClassPath() {
        String[] classPathSplit = System.getProperty("java.class.path").split(";");
        List<String> jars = Arrays
                .stream(classPathSplit)
                .filter(p -> p.endsWith(".jar"))
                .collect(Collectors.toList());
        List<URL> urls = new ArrayList<>();
        for (String jar : jars) {
            try {
                urls.add(Path.of(jar).toUri().toURL());
            } catch (MalformedURLException e) {
                System.out.println("Pickle Tickle!");
            }
        }
        return URLClassLoader.newInstance(urls.toArray(new URL [0]));
    }
}
