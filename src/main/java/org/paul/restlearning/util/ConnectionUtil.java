package org.paul.restlearning.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;
//import org.h2.tools.RunScript;


 // The ConnectionUtil class will be utilized to create an active connection to our database.
// This class utilizes the singleton design pattern. We will be utilizing an in-memory called h2database for the sql demos.

public class ConnectionUtil {


    private static final String URL = "jdbc:postgresql://localhost:5432/pep_social_media";
    private static final String USERNAME = System.getenv("DB_USER_PSQL");
    private static final String PASSWORD = System.getenv("DB_PASSWORD_PSQL");

    // DataSource for pooling. Pooling enables the creation of multiple connections when connections are closed.
    private static final BasicDataSource POOL = new BasicDataSource();

    //  static initialization block to establish credentials for DataSource Pool
    static {
        POOL.setUrl(URL);
        POOL.setUsername(USERNAME);
        POOL.setPassword(PASSWORD);


        POOL.setMinIdle(5);
        POOL.setMaxIdle(5);
        POOL.setMaxTotal(10);
    }

    // @return an active connection to the database
    public static Connection getConnection() {
        try {
            return POOL.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    // For the purpose of testing, we will need to drop and recreate our database tables to keep it consistent across all tests.
    // The method will read the sql file in resources. This will be performed before every test.
    /*public static void resetTestDatabase() {
        try {
            FileReader sqlReader = new FileReader("src/main/resources/ResourceMedia.sql");
            RunScript.execute(getConnection(), sqlReader);
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }*/
}
