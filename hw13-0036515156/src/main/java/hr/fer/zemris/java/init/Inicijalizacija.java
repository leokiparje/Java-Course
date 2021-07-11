package hr.fer.zemris.java.init;

import java.beans.PropertyVetoException;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

import hr.fer.zemris.java.DB.TableName;

@WebListener
public class Inicijalizacija implements ServletContextListener {
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		ResourceBundle config = ResourceBundle.getBundle("database");
		validateArguments(config);
		
		String dbName = config.getString("name");
		String host = config.getString("host");
		String port = config.getString("port");
		String username = config.getString("user");
		String password = config.getString("password");
		String connectionURL = "jdbc:derby://"+host+":"+port+"/"+dbName;

		ComboPooledDataSource cpds = new ComboPooledDataSource();
		try {
			cpds.setDriverClass("org.apache.derby.client.ClientAutoloadedDriver");
		} catch (PropertyVetoException e1) {
			throw new RuntimeException("Pogreška prilikom inicijalizacije poola.", e1);
		}
		configConnectionPool(cpds,connectionURL,username,password);

		sce.getServletContext().setAttribute("hr.fer.zemris.dbpool", cpds); // U Filteru koristim ovaj database pool.
		DataSource ds = (DataSource)sce.getServletContext().getAttribute("hr.fer.zemris.dbpool");
		
		createTablesIfNotExists(ds);
		populateTablesIfEmpty(ds);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ComboPooledDataSource cpds = (ComboPooledDataSource)sce.getServletContext().getAttribute("hr.fer.zemris.dbpool");
		if(cpds!=null) {
			try {
				DataSources.destroy(cpds);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void populateTablesIfEmpty(DataSource ds) {
		
		List<Long> ids = new ArrayList<>();
		
		if (tableIsEmpty(ds,TableName.Polls)) {
			populateTable(ds,TableName.Polls,ids);
		}
		if (tableIsEmpty(ds,TableName.PollOptions)) {
			populateTable(ds,TableName.PollOptions,ids);
		}
	}
	
	private void populateTable(DataSource ds, TableName tableName, List<Long> ids) {
		
		String insertQuery = null;
		switch(tableName) {
		case Polls:
			insertQuery = "INSERT INTO Polls (title,message) VALUES (?,?)";
			
			try(Connection con = ds.getConnection()) {
				try(PreparedStatement statement = con.prepareStatement(insertQuery,Statement.RETURN_GENERATED_KEYS)){
					
					statement.setString(1,"Glasanje za omiljeni bend");
					statement.setString(2,"Od sljedećih bendova, koji Vam je bend najdraži?");
					statement.executeUpdate();
					ResultSet rset = statement.getGeneratedKeys();
					if (rset!=null && rset.next()) {
						ids.add(rset.getLong(1));
					}
					
					statement.setString(1,"Glasanje za najdraži fakultet Zagreba");
					statement.setString(2,"Od sljedećih fakulteta, koji vam je najdraži?");
					statement.executeUpdate();
					rset = statement.getGeneratedKeys();
					if (rset!=null && rset.next()) {
						ids.add(rset.getLong(1));
					}
					
				}catch(Exception e) {
					e.printStackTrace();
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			break;
		case PollOptions:
			insertQuery = "INSERT INTO PollOptions (optionTitle,optionLink,pollID,votesCount)"+
							"VALUES ('The Beatles','https://www.youtube.com/watch?v=z9ypq6_5bsg',"+ids.get(0)+",23),"+
							"('The Platters', 'https://www.youtube.com/watch?v=H2di83WAOhU',"+ids.get(0)+",44),"+
							"('The Beach Boys', 'https://www.youtube.com/watch?v=2s4slliAtQU',"+ids.get(0)+",78),"+
							"('FER','https://www.youtube.com/watch?v=z9ypq6_5bsg',"+ids.get(1)+",89),"+
							"('FSB','https://www.youtube.com/watch?v=z9ypq6_5bsg',"+ids.get(1)+",11)";
			
			try(Connection con = ds.getConnection()) {
				try(PreparedStatement statement = con.prepareStatement(insertQuery,Statement.RETURN_GENERATED_KEYS)){
					
					statement.executeUpdate();
					
				}catch(Exception e) {
					throw new RuntimeException(e);
				}
			}catch(Exception e) {
				throw new RuntimeException(e);
			}
			break;
		default: throw new UnsupportedOperationException();
		}
	}
	
	private boolean tableIsEmpty(DataSource ds, TableName tableName) {
		
		try(Connection con = ds.getConnection()) {
			
			String query = "SELECT * FROM "+tableName.toString();
			try(PreparedStatement statement = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS)) {				
				ResultSet rs = statement.executeQuery();
				if (rs.next()) {
					return false;
				}
			}catch(Exception e) {
				throw new RuntimeException(e);
			}
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
		return true;
	}
	
	private void createTablesIfNotExists(DataSource ds) {
		
		if (!tableExists(TableName.Polls,ds)) {
			createTable(TableName.Polls,ds);
		}
		if (!tableExists(TableName.PollOptions,ds)) {
			createTable(TableName.PollOptions,ds);
		}
	}
	
	private void createTable(TableName tableName, DataSource ds) {
		
		String statementQuery = null;
		
		switch(tableName) {
		case Polls:
			statementQuery = """
							CREATE TABLE Polls
							(id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
							title VARCHAR(150) NOT NULL,
							message CLOB(2048) NOT NULL)""";
			break;
		case PollOptions:
			statementQuery = """
							CREATE TABLE PollOptions
							(id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
							optionTitle VARCHAR(100) NOT NULL,
							optionLink VARCHAR(150) NOT NULL,
							pollID BIGINT,
							votesCount BIGINT,
							FOREIGN KEY (pollID) REFERENCES Polls(id)
							)""";
			break;
		default: throw new UnsupportedOperationException();
		}
		
		try(Connection con = ds.getConnection()) {
			
			try(PreparedStatement statement = con.prepareStatement(statementQuery)) {
				
				statement.executeUpdate();

			}catch(Exception e) {
				throw new RuntimeException(e);
			}
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private void configConnectionPool(ComboPooledDataSource cpds, String connectionURL, String username, String password) {
		cpds.setJdbcUrl(connectionURL);
		cpds.setUser(username);
		cpds.setPassword(password);
		cpds.setInitialPoolSize(5);
		cpds.setMinPoolSize(5);
		cpds.setAcquireIncrement(5);
		cpds.setMaxPoolSize(20);
	}
	
	private void validateArguments(ResourceBundle config) {
		if (missingArguments(config)) throw new RuntimeException("Missing arguments in database properties file.");
	}
	
	private boolean missingArguments(ResourceBundle config) {
		if (!config.containsKey("host") ||
				!config.containsKey("port") ||
				!config.containsKey("name") ||
				!config.containsKey("user") ||
				!config.containsKey("password")) return true;
		return false;
	}
	
	private boolean tableExists(TableName tableName, DataSource ds) throws RuntimeException {
		
		try(Connection con = ds.getConnection()) {

			if (con!=null) {
				try {
					DatabaseMetaData dbmd = con.getMetaData();
					ResultSet rs = dbmd.getTables(null,null,tableName.toString().toUpperCase(),null);
					if (rs.next()) {
						return true;
					}
				}catch(Exception e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException("Unable to establish connection while checking if tables already exists.");
		}
		return false;
	}
}









































