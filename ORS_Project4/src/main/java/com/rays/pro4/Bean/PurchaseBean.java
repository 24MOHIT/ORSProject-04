package com.rays.pro4.Bean;

import java.util.Date;

public class PurchaseBean extends BaseBean {
	
	private long quantity;
	private String product;
	private Date orderdate;
	private long totalcost;
	
	public long getQuantity() {
		return quantity;
	}
	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	
	public Date getOrderdate() {
		return orderdate;
	}
	public void setOrderdate(Date orderdate) {
		this.orderdate = orderdate;
	}
	public long getTotalcost() {
		return totalcost;
	}
	public void setTotalcost(long totalcost) {
		this.totalcost = totalcost;
	}
	@Override
	public String getkey() {
		// TODO Auto-generated method stub
		return id + "";
	}
	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return product + "";
	}
	
	
	

}
