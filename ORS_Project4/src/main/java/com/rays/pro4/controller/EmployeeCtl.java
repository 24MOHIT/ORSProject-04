package com.rays.pro4.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.DocterBean;
import com.rays.pro4.Bean.EmployeeBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.DocterModel;
import com.rays.pro4.Model.EmployeeModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;

@WebServlet(name = "EmployeeCtl" , urlPatterns = {"/ctl/EmployeeCtl"})
public class EmployeeCtl extends BaseCtl {

	@Override
	protected boolean validate(HttpServletRequest request) {
		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("fullName"))) {
			request.setAttribute("fullName", PropertyReader.getValue("error.require", "Full Name"));
			pass = false;
		}else if (!DataValidator.isName(request.getParameter("fullName"))) {
			request.setAttribute("fullName", "Invalid Full Name");
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("userName"))) {
			request.setAttribute("userName", PropertyReader.getValue("error.require", "User Name"));
			pass = false;
		}else if (!DataValidator.isEmail(request.getParameter("userName"))) {
			request.setAttribute("userName", "Invalid EmailId");
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("password"))) {
			request.setAttribute("password", PropertyReader.getValue("error.require", "Password"));
			pass = false;
		
		}else if (!DataValidator.isPasswordLength(request.getParameter("password"))) {
			request.setAttribute("password", "Invalid Password Length");
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("birthDate"))) {
			request.setAttribute("birthDate", PropertyReader.getValue("error.require", "Birth Date"));
			pass = false;
		}else if (!DataValidator.isDate(request.getParameter("birthDate"))) {
			request.setAttribute("birthDate", "Invalid Birth Date");
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("contactNumber"))) {
			request.setAttribute("contactNumber", PropertyReader.getValue("error.require", "Contact Number"));
			pass = false;
		}else if (!DataValidator.isPhoneLength(request.getParameter("contactNumber"))) {
			request.setAttribute("contactNumber", "Invalid contactNumber");
			pass = false;
		}
		return pass;
	}
	
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		
		EmployeeBean bean = new EmployeeBean();
		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setFullName(DataUtility.getString(request.getParameter("fullName")));
		System.out.println("date => " + request.getParameter("fullName"));
		bean.setUserName(DataUtility.getString(request.getParameter("userName")));
		bean.setPassword(DataUtility.getString(request.getParameter("password")));
		bean.setBirthDate(DataUtility.getDate(request.getParameter("birthDate")));
		bean.setContactNumber(DataUtility.getString(request.getParameter("contactNumber")));
		populateDTO(bean, request);
		return bean;

	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String op = DataUtility.getString(request.getParameter("operation"));
		Long id = DataUtility.getLong(request.getParameter("id"));

		if (id > 0) {

			EmployeeModel model = new EmployeeModel();

			try {
				EmployeeBean bean = model.findByPk(id)
;
				ServletUtility.setBean(bean, request);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		ServletUtility.forward(getView(), request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("uctl Do Post");

		String op = DataUtility.getString(request.getParameter("operation"));

		long id = DataUtility.getLong(request.getParameter("id"));

		System.out.println(">>>><<<<>><<><<><<><>**********" + id + op);

		EmployeeModel model = new EmployeeModel();

		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {

			EmployeeBean bean = (EmployeeBean) populateBean(request);

			if (id > 0) {

				try {
					model.update(bean);
					ServletUtility.setBean(bean, request);
					ServletUtility.setSuccessMessage("Data is successfully Updated", request);
				} catch (Exception e) {
					System.out.println("Data not update");
					e.printStackTrace();
				}

			} else {

				try {
					long pk = model.add(bean);
					ServletUtility.setBean(bean, request);
					ServletUtility.setSuccessMessage("Data is successfully Added", request);
					bean.setId(pk);
				} catch (ApplicationException e) {
					System.out.println("Data not added");
					e.printStackTrace();
				} catch (DuplicateRecordException e) {
					System.out.println(" U ctl D post 4444444");
					ServletUtility.setBean(bean, request);
					ServletUtility.setErrorMessage("UserName already exists", request);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
		ServletUtility.forward(getView(), request, response);

	}

	@Override
	protected String getView() {
		return ORSView.EMPLOYEE_VIEW;
	}

}
