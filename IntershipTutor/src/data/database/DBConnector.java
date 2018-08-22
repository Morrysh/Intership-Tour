package data.database;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DBConnector {
	
	private static DataSource dataSource;
    private static InitialContext ctx;

    private static void init() {
        try {
        	ctx = new InitialContext();
        	Context envContext  = (Context)ctx.lookup("java:comp/env");
        	dataSource = (DataSource)envContext.lookup("jdbc/intershiptutor_database");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public static DataSource getDatasource() {
        if (dataSource == null)
            init();
        return dataSource;
    }

}
