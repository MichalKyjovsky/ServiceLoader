package cz.cuni.mff;

import java.sql.Driver;

public class Main {
    public static void main(String[] args) {
        Iterable<Driver> it = MyServiceLoader.load(Driver.class);
        for (Driver d : it) {
            if (d instanceof com.mysql.cj.jdbc.Driver) {
                System.out.println("PASS");
            }
        }
    }
}
