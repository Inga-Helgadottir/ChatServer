package Database;

import Database.DBBasicMethods;

import java.sql.*;

public class DBConnector implements Database.DBBasicMethods
{

    private Connection connection;
    private String driverName = "com.mysql.cj.jdbc.Driver";
    private PreparedStatement pstmt = null;

    public DBConnector() throws SQLException
    {
        try
        {
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatserver?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "JonasIngstrupSQL");
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }

    @Override
    public String whoIsOnline()
    {
        return null;
    }
}