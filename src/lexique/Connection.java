/*Classe permettant de se connecter à une base de données SQL*/
package lexique;

import java.sql.SQLException;


public class Connection {
    String userid;
    String password;
    String URL;
    java.sql.Connection conn;

    public Connection() throws SQLException {
        userid = "admin"; //votre identifiant
        password = "admin"; //votre mdp
        URL = "jdbc:oracle:thin:@iuta.univ-lyon1.fr:1521:orcl"; //connection serveur iut
        conn = java.sql.DriverManager.getConnection(URL, userid, password);
        java.sql.DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
    }

}
