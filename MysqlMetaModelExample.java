package com.bizruntime.metamodel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.metamodel.DataContext;
import org.apache.metamodel.UpdateCallback;
import org.apache.metamodel.UpdateScript;
import org.apache.metamodel.UpdateableDataContext;
import org.apache.metamodel.create.CreateTable;
import org.apache.metamodel.data.DataSet;
import org.apache.metamodel.data.Row;
import org.apache.metamodel.delete.DeleteFrom;
import org.apache.metamodel.insert.InsertInto;
import org.apache.metamodel.jdbc.JdbcDataContext;
import org.apache.metamodel.schema.Schema;
import org.apache.metamodel.schema.Table;
import org.apache.metamodel.update.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bizruntime.metamodel.exceptions.DataBaseConnectionException;
public class MysqlExample {
	static Logger log = LoggerFactory.getLogger(MysqlExample.class);
	public static void select() throws DataBaseConnectionException {
		DataSet dataSet = null;
		DataContext dataContext = null;
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3307/bizruntime", "root", "bizruntime@123");
			dataContext = new JdbcDataContext(con);
			log.debug("execute query");
			dataSet = dataContext.query().from("student1").select("name").execute();
			log.debug("dataset : " + dataSet);
			while (dataSet.next()) {
				log.debug("in dataset");
				Row row = dataSet.getRow();
				System.out.println(row.size());
				String name = (String) dataSet.getRow().getValue(0);
				log.info("name is " + name);
			}

		} catch (Exception e) {
			throw new DataBaseConnectionException("unable to connect to mysql database ",e);
		}
	}

	public static void insert() throws DataBaseConnectionException {
		UpdateableDataContext dataContext = null;
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3307/bizruntime", "root", "bizruntime@123");
			dataContext = new JdbcDataContext(con);
			Table table = dataContext.getTableByQualifiedLabel("student1");
			dataContext.executeUpdate(new InsertInto(table).value("name", "pqr1").value("age", 29));
		} catch (Exception e) {
			throw new DataBaseConnectionException("unable to connect to mysql database ",e);
		}
	}
	public static void update(){
		UpdateableDataContext dataContext = null;
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3307/bizruntime", "root", "bizruntime@123");
			dataContext = new JdbcDataContext(con);
			Table table = dataContext.getTableByQualifiedLabel("student1");
			dataContext.executeUpdate(new Update(table).value("name","dileep").where("age").eq(25));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public static void delete() throws DataBaseConnectionException{
		UpdateableDataContext dataContext = null;
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3307/bizruntime", "root", "bizruntime@123");
			dataContext = new JdbcDataContext(con);
			Table table = dataContext.getTableByQualifiedLabel("student1");
			
			dataContext.executeUpdate(new DeleteFrom(table).where("name").eq("xyz"));
		} catch (Exception e) {
			throw new DataBaseConnectionException("unable to connect to mysql database ",e);
		}
		
	}
	public static void create() throws DataBaseConnectionException{
		UpdateableDataContext dataContext = null;
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3307/bizruntime", "root", "bizruntime@123");
			dataContext = new JdbcDataContext(con);
			Schema schema=dataContext.getDefaultSchema();
			dataContext.executeUpdate(new CreateTable(schema, "person").withColumn("name").ofNativeType("varchar(20)").withColumn("age").ofNativeType("int"));
		} catch (Exception e) {
			throw new DataBaseConnectionException("unable to connect to mysql database ",e);
		}
	}
	public static void updateScripts() throws DataBaseConnectionException{
		final UpdateableDataContext dataContext;
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3307/bizruntime", "root", "bizruntime@123");
			dataContext = new JdbcDataContext(con);
			dataContext.executeUpdate(new UpdateScript()
					{
				public void run(UpdateCallback callback){
					Table table=dataContext.getTableByQualifiedLabel("student1");
					callback.insertInto(table).value("name", "mahesh").value("age", 25).execute();
					callback.insertInto(table).value("name", "raj").value("agee", 28).execute();
				
				}
					});
	}
		catch (Exception e) {
			throw new DataBaseConnectionException("unable to connect to mysql database ",e);
		}
	}
	public static void joins() throws DataBaseConnectionException{
		DataSet dataSet = null;
		DataContext dataContext = null;
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3307/bizruntime", "root", "bizruntime@123");
			dataContext = new JdbcDataContext(con);
			dataSet = dataContext.query().from("employee").leftJoin("address").on("addressId", "addressId").select("employee.ename","address.city").execute();
			while (dataSet.next()) {
				String name = (String) dataSet.getRow().getValue(0);
				log.info("ename is " + name +" employee city is "+(String) dataSet.getRow().getValue(1));
			}
			
		}
		catch (Exception e) {
			throw new DataBaseConnectionException("unable to connect to mysql database ",e);
		}
		
		
	}
	public static void main(String[] args) throws DataBaseConnectionException {
		//select()
		//insert();
		//update();
		//delete();
		//create();
		//updateScripts();
		joins();

	}
}
