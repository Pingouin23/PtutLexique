/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexique;

import java.sql.SQLException;

/**
 *
 * @author eyepop
 */
public class Connection {
    String userid;
    String password;
    String URL;
    java.sql.Connection conn;

    public Connection() throws SQLException {
        userid = "p1105653";
        password = "154996";
        URL = "jdbc:oracle:thin:@iuta.univ-lyon1.fr:1521:orcl";
        conn = java.sql.DriverManager.getConnection(URL, userid, password);
        java.sql.DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        /*if (conn != null) {
            System.out.println("Connexion établie");
        } else {
            System.out.println("Connexion échouée");
        }*/
    }

}
