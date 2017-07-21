package com.adjika.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.adjika.dao.ProductDao;
import com.adjika.entity.Category;
import com.adjika.entity.Product;

public class ProductDaoImpl implements ProductDao {

	private SessionFactory sessionFactory;

	public Product getProductDetails(int productId) {

		Session session = this.sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();

		String query = "from Product where productId=:productId";
		Query query2 = session.createQuery(query);
		query2.setParameter("productId", productId);

		Object object = query2.uniqueResult();

		transaction.commit();
		session.close();

		return (Product) object;
	}

	public List<Category> getCategories() {

		Session session = this.sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();

		String query = "from Category";
		Query query2 = session.createQuery(query);

		Object object = query2.list();

		List<Category> list = (List<Category>) object;

		transaction.commit();
		session.close();
		return list;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
