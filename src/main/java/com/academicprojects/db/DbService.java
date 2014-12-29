package com.academicprojects.db;

import java.sql.*;

public class DbService {

	Connection conn;                                                //our connnection to the db - presist for life of program

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public DbService(String db_file_name_prefix) throws Exception {    // note more general exception

        Class.forName("org.hsqldb.jdbcDriver");


        conn = DriverManager.getConnection("jdbc:hsqldb:"
                                           + db_file_name_prefix,    // filenames
                                           "sa",                     // username
                                           "");                      // password
    }
	
	public void shutdown() throws SQLException {

        Statement st = conn.createStatement();
        st.execute("SHUTDOWN");
        conn.close();    // if there are no other open connection
    }
	
	public synchronized ResultSet query(String expression) throws SQLException {

        Statement st = null;
        ResultSet rs = null;

        st = conn.createStatement();         // statement objects can be reused with

        rs = st.executeQuery(expression);    // run the query


        st.close();    // NOTE!! if you close a statement the associated ResultSet is
        return rs;

    }
}
