package com.rays.pro4.Model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.rays.pro4.Bean.EmployeeBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Util.JDBCDataSource;

public class EmployeeModel {
	
	private static Logger log = Logger.getLogger(EmployeeModel.class);

	public Integer nextPk() throws Exception {
		
		log.debug("Model nextPK Started");

		int pk = 0;

		Connection conn = JDBCDataSource.getConnection();

		PreparedStatement pstmt = conn.prepareStatement("select max(id) from st_employee");

		ResultSet rs = pstmt.executeQuery();

		while (rs.next()) {

			pk = rs.getInt(1);
		}
		JDBCDataSource.closeConnection(conn);

		return pk + 1;
	}

	public long add(EmployeeBean bean) throws Exception {
		log.debug("Model add Started");

		EmployeeBean existBean = findByFullName(bean.getFullName());

		if (existBean != null) {
			throw new DuplicateRecordException("FullName already exist..!!");
		}
		
		EmployeeBean existbean1 = findByUserName(bean.getUserName());
		
		if (existbean1 != null) {
			throw new DuplicateRecordException("UserName is allready exist...");
		}
		
		int pk = nextPk();

		Connection conn = JDBCDataSource.getConnection();

		PreparedStatement pstmt = conn.prepareStatement("insert into st_employee values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		pstmt.setLong(1, pk);
		pstmt.setString(2, bean.getFullName());
		pstmt.setString(3, bean.getUserName());
		pstmt.setString(4, bean.getPassword());
		pstmt.setDate(5, new java.sql.Date(bean.getBirthDate().getTime()));
		pstmt.setString(6, bean.getContactNumber());
		pstmt.setString(7, bean.getCreatedBy());
		pstmt.setString(8, bean.getModifiedBy());
		pstmt.setTimestamp(9, bean.getCreatedDatetime());
		pstmt.setTimestamp(10, bean.getModifiedDatetime());

		int i = pstmt.executeUpdate();

		JDBCDataSource.closeConnection(conn);

		System.out.println("data inserted => " + i);
		return pk;
	}

	public void update(EmployeeBean bean) throws Exception {

		Connection conn = JDBCDataSource.getConnection();

		PreparedStatement pstmt = conn.prepareStatement(
				"update st_employee set full_name = ?, user_name = ?, `password` = ?, birth_date = ?, contact_number = ?, created_by = ?, modified_by = ?, created_datetime = ?, modified_datetime = ? where id = ?");

		pstmt.setString(1, bean.getFullName());
		pstmt.setString(2, bean.getUserName());
		pstmt.setString(3, bean.getPassword());
		pstmt.setDate(4, new java.sql.Date(bean.getBirthDate().getTime()));
		pstmt.setString(5, bean.getContactNumber());
		pstmt.setString(6, bean.getCreatedBy());
		pstmt.setString(7, bean.getModifiedBy());
		pstmt.setTimestamp(8, bean.getCreatedDatetime());
		pstmt.setTimestamp(9, bean.getModifiedDatetime());
		pstmt.setLong(10, bean.getId());

		int i = pstmt.executeUpdate();

		JDBCDataSource.closeConnection(conn);

		System.out.println("Data updated" + i);

	}

	public void delete(long id) throws Exception {

		Connection conn = JDBCDataSource.getConnection();

		PreparedStatement pstmt = conn.prepareStatement("delete from st_employee where id =?");

		pstmt.setLong(1, id);

		int i = pstmt.executeUpdate();

		pstmt.executeUpdate();

		JDBCDataSource.closeConnection(conn);

		System.out.println("data deleted => " + i);
	}

	public EmployeeBean findByPk(long id) throws Exception {

		Connection conn = null;
		EmployeeBean bean = null;

		try {
			conn = JDBCDataSource.getConnection();

			PreparedStatement pstmt = conn.prepareStatement("select * from st_employee where id=?");
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new EmployeeBean();
				bean.setId(rs.getLong(1));
				bean.setFullName(rs.getString(2));
				bean.setUserName(rs.getString(3));
				bean.setPassword(rs.getString(4));
				bean.setBirthDate(rs.getDate(5));
				bean.setContactNumber(rs.getString(6));
				bean.setCreatedBy(rs.getString(7));
				bean.setModifiedBy(rs.getString(8));
				bean.setCreatedDatetime(rs.getTimestamp(9));
				bean.setModifiedDatetime(rs.getTimestamp(10));

			}
		} catch (Exception e) {
			throw new ApplicationException("Exception : Exception in getting Employee by PK");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return bean;

	}

	private EmployeeBean findByFullName(String fullName) throws Exception {

		Connection conn = null;

		EmployeeBean bean = null;

		conn = JDBCDataSource.getConnection();

		PreparedStatement pstmt = conn.prepareStatement("select * from st_employee where full_name = ?");
		pstmt.setString(1, fullName);
		ResultSet rs = pstmt.executeQuery();

		while (rs.next()) {
			bean = new EmployeeBean();
			bean.setId(rs.getLong(1));
			bean.setFullName(rs.getString(2));
			bean.setUserName(rs.getString(3));
			bean.setPassword(rs.getString(4));
			bean.setBirthDate(rs.getDate(5));
			bean.setContactNumber(rs.getString(6));
			bean.setCreatedBy(rs.getString(7));
			bean.setModifiedBy(rs.getString(8));
			bean.setCreatedDatetime(rs.getTimestamp(9));
			bean.setModifiedDatetime(rs.getTimestamp(10));

		}
		JDBCDataSource.closeConnection(conn);

		return bean;

	}

	public List list() throws Exception {
		return search(null, 0, 0);
	}

	public List search(EmployeeBean bean, int pageNo, int pageSize) throws Exception {

		StringBuffer sql = new StringBuffer("select * from st_employee where 1=1");

		if (bean != null) {
			if (bean.getFullName() != null && bean.getFullName().length() > 0) {
				sql.append(" and full_name like '" + bean.getFullName() + "%'");
			}
			
			if (bean.getBirthDate() != null && bean.getBirthDate().getTime() > 0) {
				Date d  = new Date(bean.getBirthDate().getTime());
				sql.append(" and birth_date = '" + d + "'");
			}
			
			if (bean.getUserName() != null && bean.getUserName().length() > 0) {
				sql.append(" and user_name like '" + bean.getUserName() + "'");
			}

		}

		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + ", " + pageSize);
		}

		System.out.println("sql ==>> " + sql.toString());

		Connection conn = null;
		List list = new ArrayList();

		try {
			conn = JDBCDataSource.getConnection();

			PreparedStatement pstmt = conn.prepareStatement(sql.toString());

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new EmployeeBean();
				bean.setId(rs.getLong(1));
				bean.setFullName(rs.getString(2));
				bean.setUserName(rs.getString(3));
				bean.setPassword(rs.getString(4));
				bean.setBirthDate(rs.getDate(5));
				bean.setContactNumber(rs.getString(6));
				bean.setCreatedBy(rs.getString(7));
				bean.setModifiedBy(rs.getString(8));
				bean.setCreatedDatetime(rs.getTimestamp(9));
				bean.setModifiedDatetime(rs.getTimestamp(10));
				list.add(bean);
			}
		} catch (Exception e) {
			throw new ApplicationException("Exception : Exception in search task " + e);
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return list;
	}
	
	private EmployeeBean findByUserName(String userName) throws Exception {

		Connection conn = null;

		EmployeeBean bean = null;

		conn = JDBCDataSource.getConnection();

		PreparedStatement pstmt = conn.prepareStatement("select * from st_employee where user_name = ?");
		pstmt.setString(1, userName);
		ResultSet rs = pstmt.executeQuery();

		while (rs.next()) {
			bean = new EmployeeBean();
			bean.setId(rs.getLong(1));
			bean.setFullName(rs.getString(2));
			bean.setUserName(rs.getString(3));
			bean.setPassword(rs.getString(4));
			bean.setBirthDate(rs.getDate(5));
			bean.setContactNumber(rs.getString(6));
			bean.setCreatedBy(rs.getString(7));
			bean.setModifiedBy(rs.getString(8));
			bean.setCreatedDatetime(rs.getTimestamp(9));
			bean.setModifiedDatetime(rs.getTimestamp(10));

		}
		JDBCDataSource.closeConnection(conn);

		return bean;

	}

}
