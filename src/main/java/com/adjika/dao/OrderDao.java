package com.adjika.dao;

import java.util.List;

import com.adjika.entity.Order;
import com.adjika.model.ShoppingCart;

public interface OrderDao {

	public void saveOrder(ShoppingCart cart);
	
	public List<Order> showOrdersForToday();
	
	public Order getOrderDetails(int orderId);
}
