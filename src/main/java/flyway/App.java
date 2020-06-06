package flyway;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

public class App {

    @Autowired
    private static Environment env;

    private static String db_location = env.getProperty("spring.datasource.url");
    private static String db_user = env.getProperty("spring.datasource.username");
    private static String db_login = env.getProperty("spring.datasource.password");

    public static void main(String[] args) {

        // point Flyway instance to db
        Flyway flyway = Flyway.configure().dataSource(db_location, db_user, db_login).load();

        // remove any failed migrations, realign checksums
        flyway.repair();

        // start migration
        flyway.migrate();
    }
}
