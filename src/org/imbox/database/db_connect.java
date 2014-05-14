package org.imbox.database;
import java.sql.*;
/*author: cindyboy*/
public class db_connect {
    private static String driver   = "com.mysql.jdbc.Driver";
    private static String url      = "jdbc:mysql://127.0.0.1:3306/";
    protected static String dbname   = "group2_db";  // real : group2_db
    private static String user     = "root";
    private static String password = "imbox";
    protected static Connection connect;
    static{
	try{
	Class.forName(driver);
	connect = DriverManager.getConnection(url+dbname,user,password);
	}catch(ClassNotFoundException | SQLException e){
	    throw new ExceptionInInitializerError(e);
	}
    };
    public db_connect(){ };
    
}
