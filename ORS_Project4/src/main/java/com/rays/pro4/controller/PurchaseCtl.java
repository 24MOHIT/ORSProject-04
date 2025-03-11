package com.rays.pro4.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.PurchaseBean;
import com.rays.pro4.Model.PurchaseModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;


@WebServlet(name = "PurchaseCtl" , urlPatterns = {"/ctl/PurchaseCtl"})
public class PurchaseCtl extends BaseCtl{

	@Override
	protected boolean validate(HttpServletRequest request) {
		System.out.println("uctl Validate");

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("quantity"))) {
			request.setAttribute("quantity", PropertyReader.getValue("error.require", "quantity"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("product"))) {
			request.setAttribute("product", PropertyReader.getValue("error.require", "product"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("orderdate"))) {
			request.setAttribute("orderdate", PropertyReader.getValue("error.require", "orderdate"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("totalcost"))) {
			request.setAttribute("totalcost", PropertyReader.getValue("error.require", "totalcost"));
			pass = false;
		}

		return pass;

	}

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {

		PurchaseBean bean = new PurchaseBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));

		bean.setQuantity(DataUtility.getLong(request.getParameter("quantity")));

		bean.setProduct(DataUtility.getString(request.getParameter("product")));

		bean.setOrderdate(DataUtility.getDate(request.getParameter("orderdate")));

		bean.setTotalcost(DataUtility.getLong(request.getParameter("totalcost")));

		return bean;

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String op = DataUtility.getString(request.getParameter("operation"));

		PurchaseModel model = new PurchaseModel();

		long id = DataUtility.getLong(request.getParameter("id"));

//		System.out.println("product Edit Id >= " + id);

		if (id != 0 && id > 0) {

			System.out.println("in id > 0  condition " + id);
			PurchaseBean bean;

			try {
				bean = model.findByPK(id);
				ServletUtility.setBean(bean, request);

			} catch (Exception e) {

				e.printStackTrace();
			}
		}

		ServletUtility.forward(getView(), request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("uctl Do Post");

		String op = DataUtility.getString(request.getParameter("operation"));

		long id = DataUtility.getLong(request.getParameter("id"));

		System.out.println(">>>><<<<>><<><<><<><>**********" + id + op);

		PurchaseModel model = new PurchaseModel();

		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {

			PurchaseBean bean = (PurchaseBean) populateBean(request);

			if (id > 0) {

				try {
					model.update(bean);
					ServletUtility.setBean(bean, request);
					ServletUtility.setSuccessMessage("purchase is successfully Updated", request);
				} catch (Exception e) {
					System.out.println("purchase not update");
					e.printStackTrace();
				}

			} else {

				try {
					long pk = model.add(bean);
					ServletUtility.setBean(bean, request);
					ServletUtility.setSuccessMessage("purchase is successfully Added", request);
					bean.setId(pk);
				} catch (Exception e) {
					System.out.println("purchase not added");
					e.printStackTrace();
				}

			}

		}
		ServletUtility.forward(getView(), request, response);

	}

	@Override
	protected String getView() {
		// TODO Auto-generated method stub
		return ORSView.PURCHASE_VIEW;
	}

	
	
}
