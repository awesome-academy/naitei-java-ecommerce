package com.triplet.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import com.triplet.dao.GenericDAO;
import com.triplet.dao.ProductDAO;
import com.triplet.model.Category;
import com.triplet.model.Product;

public class ProductDAOImpl extends GenericDAO<Integer, Product> implements ProductDAO {

	public ProductDAOImpl() {
		super(Product.class);
	}

	public ProductDAOImpl(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> loadProductsByCategory(int categoryId) {
		return getSession().createQuery("from Product where category_id=: category_id")
				.setParameter("category_id", categoryId).getResultList();
	}

	@Override
	public List<String> getListNamesOfProduct(String term) {
		Session session = getSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<String> query = builder.createQuery(String.class);

		Root<Product> root = query.from(Product.class);
		query.select(root.get("name"));
		String pattern = '%' + term + '%';
		query.where(builder.like(root.get("name"), pattern));

		return session.createQuery(query).getResultList();
	}

	@Override
	public List<Product> findByName(String name) {
		Session session = getSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Product> query = builder.createQuery(Product.class);

		Root<Product> root = query.from(Product.class);
		query.select(root);
		String pattern = '%' + name + '%';
		query.where(builder.like(root.get("name"), pattern));

		return session.createQuery(query).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> getActiveProducts() {
		return getSession().createQuery("from Product where delete_time is null").getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> getDeletedProducts() {
		return getSession().createQuery("from Product where delete_time is not null").getResultList();
	}

	@Override
	public List<Product> search(boolean instock, Category category, BigDecimal priceFrom, BigDecimal priceTo) {
		Session session = getSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Product> query = builder.createQuery(Product.class);
		Root<Product> root = query.from(Product.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(builder.isNull(root.get("delete_time"))); // active

		if (instock) {
			predicates.add(builder.gt(root.get("quantity"), 0)); // instock
		} else {
			predicates.add(builder.equal(root.get("quantity"), 0)); // outstock
		}

		if (category != null) {
			predicates.add(builder.equal(root.get("category"), category));
		}

		if (priceFrom != null && priceTo != null) {
			predicates.add(builder.between(root.get("price"), priceFrom, priceTo));
		} else if (priceFrom != null) {
			predicates.add(builder.gt(root.get("price"), priceFrom));
		} else if (priceTo != null) {
			predicates.add(builder.lt(root.get("price"), priceTo));
		}

		query.select(root).where(predicates.toArray(new Predicate[0]));
		query.orderBy(builder.desc(root.get("update_time")));
		return getSession().createQuery(query).getResultList();
	}

	@Override
	public List<Product> searchDeleted(Category category, BigDecimal priceFrom, BigDecimal priceTo) {
		Session session = getSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Product> query = builder.createQuery(Product.class);
		Root<Product> root = query.from(Product.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(builder.isNotNull(root.get("delete_time"))); // active

		if (category != null) {
			predicates.add(builder.equal(root.get("category"), category));
		}

		if (priceFrom != null && priceTo != null) {
			predicates.add(builder.between(root.get("price"), priceFrom, priceTo));
		} else if (priceFrom != null) {
			predicates.add(builder.gt(root.get("price"), priceFrom));
		} else if (priceTo != null) {
			predicates.add(builder.lt(root.get("price"), priceTo));
		}

		query.select(root).where(predicates.toArray(new Predicate[0]));
		query.orderBy(builder.desc(root.get("update_time")));
		return getSession().createQuery(query).getResultList();
	}
}
