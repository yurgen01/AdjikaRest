package com.adjika.dao;

import java.util.List;

import com.adjika.entity.Category;
import com.adjika.entity.Product;

public interface ProductDao {

	public Product getProductDetails(int itemId);
	
	public List<Category> getCategories();
	
}
