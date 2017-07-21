package com.adjika.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.adjika.dao.OrderDao;
import com.adjika.entity.Order;
import com.adjika.model.CustomerInfo;
import com.adjika.model.ShoppingCart;
import com.adjika.utils.AdjikaUtils;
import com.adjika.validator.CustomerInfoValidator;

@Controller
public class OrderController {
	
	@Autowired
	private OrderDao orderDao;

	@Autowired
	private CustomerInfoValidator customerInfoValidator;

	public OrderDao getOrderDao() {
		return orderDao;
	}

	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}

	public CustomerInfoValidator getCustomerInfoValidator() {
		return customerInfoValidator;
	}

	public void setCustomerInfoValidator(CustomerInfoValidator customerInfoValidator) {
		this.customerInfoValidator = customerInfoValidator;
	}


	
	@RequestMapping(value = "/checkout", method = RequestMethod.GET)
	public String checkout(HttpServletRequest httpServletRequest, Model map) {

		ShoppingCart cart = AdjikaUtils.getOrderFromSession(httpServletRequest);

		if (cart.isEmpty()) {
			return "redirect:shoppingCart";
		}

		CustomerInfo customerInfo = new CustomerInfo();

		map.addAttribute("customerForm", customerInfo);

		return "checkout";

	}

	@RequestMapping(value = "/checkout", method = RequestMethod.POST)
	public String checkout(HttpServletRequest httpServletRequest, Model map,
			@ModelAttribute("customerForm") CustomerInfo customerForm, BindingResult bindingResult) {

		customerInfoValidator.validate(customerForm, bindingResult);

		if (bindingResult.hasErrors()) {
			return "checkout";
		}

		ShoppingCart cart = AdjikaUtils.getOrderFromSession(httpServletRequest);

		cart.setCustomerInfo(customerForm);

		return "redirect:/orderConfirmation";

	}

	@RequestMapping(value = "/orderConfirmation", method = RequestMethod.GET)
	public String orderConfirmation(HttpServletRequest httpServletRequest, ModelMap map) {

		ShoppingCart cart = AdjikaUtils.getOrderFromSession(httpServletRequest);
		if (cart.isEmpty()) {
			return "redirect:/shoppingCart";
		}

		map.addAttribute("cart", cart);

		return "orderConfirmation";

	}

	@RequestMapping(value = "placeOrder", method = RequestMethod.GET)
	public String placeOrder(HttpServletRequest httpServletRequest, Model model) {

		ShoppingCart cart = AdjikaUtils.getOrderFromSession(httpServletRequest);

		if (cart.isEmpty()) {
			return "redirect:/shoppingCart";
		}

		this.orderDao.saveOrder(cart);

		AdjikaUtils.removeOrderFromSession(httpServletRequest);

		model.addAttribute("orderId", cart.getOrderNumber());

		return "orderPlaced";
	}

	@RequestMapping(value = "/orders", method = RequestMethod.GET)
	public String findOrder(@RequestParam(value = "v", required = true) String v, Model map) {

		if (!v.equalsIgnoreCase("today")) {
			map.addAttribute("error", "Bad Input");
		} else {

			List<Order> list = this.orderDao.showOrdersForToday();
			map.addAttribute("orders", list);

		}
		return "orderList";
	}

	@RequestMapping(value = "/orderDetail", method = RequestMethod.GET)
	public String getOrderDetails(@RequestParam(value = "o", required = true) String id, Model map) {

		
		
		map.addAttribute("order", this.orderDao.getOrderDetails(Integer.parseInt(id)));
		return "orderDetails";

	}

	
}
