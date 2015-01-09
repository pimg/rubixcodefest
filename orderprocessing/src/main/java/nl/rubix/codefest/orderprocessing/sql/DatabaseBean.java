package nl.rubix.codefest.orderprocessing.sql;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Bean that creates the database table
 */
public class DatabaseBean {
	private static final Logger LOG = LoggerFactory
			.getLogger(DatabaseBean.class);
	private DataSource dataSource;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void create() throws Exception {
		JdbcTemplate jdbc = new JdbcTemplate(dataSource);
		String sql = "create table orders (\n" 
				+ " id varchar(100) primary key,\n"
				+ " totalAmount integer,\n" 
				+ " totalPrice varchar(255),\n"
				+ " countryCode varchar(2))";
		LOG.info("Creating table orders ...");
		try {
			jdbc.execute("drop table orders");
		} catch (Throwable e) {
			// ignore
		}
		jdbc.execute(sql);
		LOG.info("... created table orders");
	}

	public void destroy() throws Exception {
		JdbcTemplate jdbc = new JdbcTemplate(dataSource);
		try {
			jdbc.execute("drop table orders");
		} catch (Throwable e) {
			// ignore
		}
	}
}