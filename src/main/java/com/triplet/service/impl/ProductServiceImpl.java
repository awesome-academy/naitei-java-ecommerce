package com.triplet.service.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.triplet.bean.SearchProduct;
import com.triplet.model.Category;
import com.triplet.model.Product;
import com.triplet.service.ProductService;

public class ProductServiceImpl extends BaseServiceImpl implements ProductService {

	private static final Logger logger = Logger.getLogger(ProductServiceImpl.class);

	@Override
	public List<Product> loadFullProductsByCategory(int categoryId) {
		try {
			List<Product> products = new ArrayList<>();
			Category category = getCategoryDAO().findById(categoryId);
			List<Category> leafNodes = category.getAllLeafNodes();
			for (Category node : leafNodes) {
				products.addAll(getProductDAO().loadProductsByCategory(node.getId()));
			}
			return products;
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}

	@Override
	public Product findById(Serializable key) {
		try {
			return getProductDAO().findById(key);
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}

	@Override
	public Product saveOrUpdate(Product entity) {
		try {
			return getProductDAO().saveOrUpdate(entity);
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
	}

	@Override
	public boolean delete(Product entity) {
		try {
			getProductDAO().delete(entity);
			return true;
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
	}

	@Override
	public Page<Product> findPagination(Pageable pageable, List<Product> products) {
		Page<Product> productPage = findPaginationBase(pageable, products);
		return productPage;
	}

	@Override
	public List<String> getListNamesOfProduct(String term) {
		try {
			return getProductDAO().getListNamesOfProduct(term);
		} catch (Exception e) {
			logger.error(e);
		}
		return Collections.emptyList();
	}

	@Override
	public List<Product> findByName(String name) {
		try {
			return getProductDAO().findByName(name);
		} catch (Exception e) {
			logger.error(e);
		}
		return Collections.emptyList();
	}

	@Override
	public List<Product> getDeletedProducts() {
		try {
			return getProductDAO().getDeletedProducts();
		} catch (Exception e) {
			logger.error(e);
			return Collections.emptyList();
		}
	}

	@Override
	public List<Product> getActiveProducts() {
		try {
			return getProductDAO().getActiveProducts();
		} catch (Exception e) {
			logger.error(e);
			return Collections.emptyList();
		}
	}

	@Override
	public List<Product> search(SearchProduct searchProduct) {
		String status = searchProduct.getStatus();
		BigDecimal priceFrom = searchProduct.getPriceFrom();
		BigDecimal priceTo = searchProduct.getPriceTo();
		int categoryId = searchProduct.getCategoryId();
		Category category = getCategoryDAO().findById(categoryId);
		List<Product> products = new ArrayList<Product>();
		try {
			switch (status) {
			case "instock":
				products = getProductDAO().search(true, category, priceFrom, priceTo);
				break;
			case "outstock":
				products = getProductDAO().search(false, category, priceFrom, priceTo);
				break;
			default:
				products = getProductDAO().searchDeleted(category, priceFrom, priceTo);
				break;
			}
			return products;
		} catch (Exception e) {
			logger.error(e);
		}
		return Collections.emptyList();
	}

}
