package com.rays.pro4.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.rays.pro4.Bean.PurchaseBean;
import com.rays.pro4.Util.JDBCDataSource;

public class PurchaseModel {
	
	public Integer nextPK() throws Exception {

		int pk = 0;

		Connection conn = JDBCDataSource.getConnection();

		PreparedStatement pstmt = conn.prepareStatement("select max(id) from st_purchase");

		ResultSet rs = pstmt.executeQuery();

		while (rs.next()) {
			pk = rs.getInt(1);
		}

		rs.close();

		return pk + 1;
	}

	public long add(PurchaseBean bean) throws Exception {

		int pk = 0;

		Connection conn = JDBCDataSource.getConnection();

		pk = nextPK();

		conn.setAutoCommit(false);

		PreparedStatement pstmt = conn.prepareStatement("insert into st_purchase values(?,?,?,?,?)");

		pstmt.setInt(1, pk);
		pstmt.setLong(2, bean.getQuantity());
		pstmt.setString(3, bean.getProduct());
		pstmt.setDate(4, new java.sql.Date(bean.getOrderdate().getTime()));
		pstmt.setLong(5, bean.getTotalcost());

		int i = pstmt.executeUpdate();
		System.out.println("Purchase Add Successfully " + i);
		conn.commit();
		pstmt.close();

		return pk;
	}

	public void delete(PurchaseBean bean) throws Exception {

		Connection conn = JDBCDataSource.getConnection();

		conn.setAutoCommit(false);

		PreparedStatement pstmt = conn.prepareStatement("delete from st_purcahse where id = ?");

		pstmt.setLong(1, bean.getId());

		int i = pstmt.executeUpdate();
		System.out.println("Purchase delete successfully " + i);
		conn.commit();

		pstmt.close();
	}

	public void update(PurchaseBean bean) throws Exception {

		Connection conn = JDBCDataSource.getConnection();

		conn.setAutoCommit(false); // Begin transaction

		PreparedStatement pstmt = conn.prepareStatement(
				"update st_purchase set quantity = ?, product = ?, order_date = ?, total_cost = ? where id = ?");

		pstmt.setLong(1, bean.getQuantity());
		pstmt.setString(2, bean.getProduct());
		pstmt.setDate(3, new java.sql.Date(bean.getOrderdate().getTime()));
		pstmt.setLong(4, bean.getTotalcost());
		pstmt.setLong(5, bean.getId());

		int i = pstmt.executeUpdate();

		System.out.println("purchase update successfully " + i);

		conn.commit();
		pstmt.close();

	}

	public PurchaseBean findByPK(long pk) throws Exception {

		String sql = "select * from st_purchase where id = ?";
		PurchaseBean bean = null;

		Connection conn = JDBCDataSource.getConnection();
		PreparedStatement pstmt = conn.prepareStatement(sql);

		pstmt.setLong(1, pk);

		ResultSet rs = pstmt.executeQuery();

		while (rs.next()) {

			bean = new PurchaseBean();
			bean.setId(rs.getLong(1));
			bean.setQuantity(rs.getInt(2));
			bean.setProduct(rs.getString(3));
			bean.setOrderdate(rs.getDate(4));
			bean.setTotalcost(rs.getInt(5));

		}

		rs.close();

		return bean;
	}

	public List search(PurchaseBean bean, int pageNo, int pageSize) throws Exception {

		StringBuffer sql = new StringBuffer("select * from st_purchase where 1=1");
		if (bean != null) {

			if (bean.getProduct() != null && bean.getProduct().length() > 0) {
				sql.append(" AND product like '" + bean.getProduct() + "%'");
			}


			if (bean.getOrderdate() != null && bean.getOrderdate().getTime() > 0) {
				Date d = new Date(bean.getOrderdate().getTime());
				sql.append(" AND orderDate = '" + d + "'");
				System.out.println("done");
			}

			if (bean.getId() > 0) {
				sql.append(" AND id = " + bean.getId());
			}

		}

		if (pageSize > 0) {

			pageNo = (pageNo - 1) * pageSize;

			sql.append(" Limit " + pageNo + ", " + pageSize);

		}

		System.out.println("sql query search >>= " + sql.toString());
		List list = new ArrayList();

		Connection conn = JDBCDataSource.getConnection();
		PreparedStatement pstmt = conn.prepareStatement(sql.toString());

		ResultSet rs = pstmt.executeQuery();

		while (rs.next()) {

			bean = new PurchaseBean();
			bean.setId(rs.getLong(1));
			bean.setQuantity(rs.getInt(2));
			bean.setProduct(rs.getString(3));
			bean.setOrderdate(rs.getDate(4));
			bean.setTotalcost(rs.getInt(5));

			list.add(bean);

		}
		rs.close();

		return list;

	}

	public List list() throws Exception {

		ArrayList list = new ArrayList();

		StringBuffer sql = new StringBuffer("select * from st_purchase");

		Connection conn = JDBCDataSource.getConnection();

		PreparedStatement pstmt = conn.prepareStatement(sql.toString());
		ResultSet rs = pstmt.executeQuery();

		while (rs.next()) {

			PurchaseBean bean = new PurchaseBean();

			bean.setId(rs.getLong(1));
			bean.setQuantity(rs.getInt(2));
			bean.setProduct(rs.getString(3));
			bean.setOrderdate(rs.getDate(4));
			bean.setTotalcost(rs.getInt(5));

			list.add(bean);

		}

		rs.close();

		return list;
	}

}
