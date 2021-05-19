package com.triplet.dao;

import java.math.BigDecimal;
import java.util.List;

import com.triplet.model.Category;
import com.triplet.model.Product;

public interface ProductDAO extends BaseDAO<Integer, Product> {

	List<Product> loadProductsByCategory(int categoryId);

	List<String> getListNamesOfProduct(String term);

	List<Product> findByName(String name);

	List<Product> getActiveProducts();

	List<Product> getDeletedProducts();

	List<Product> search(boolean instock, Category category, BigDecimal priceFrom, BigDecimal priceTo);
	
	List<Product> searchDeleted(Category category, BigDecimal priceFrom, BigDecimal priceTo);
}