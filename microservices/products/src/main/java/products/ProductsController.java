package products;

import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class ProductsController {

	protected Logger logger = Logger.getLogger(ProductsController.class
			.getName());
	protected ProductRepository productRepository;
	
	
	/**
	 * Create an instance plugging in the respository of Accounts.
	 * 
	 * @param accountRepository
	 *            An account repository implementation.
	 */
	@Autowired
	public ProductsController(ProductRepository productRepository) {
		this.productRepository = productRepository;
		
		logger.info("ProductRepository: system has "
				+ productRepository.countProducts() + " products");
		
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setAllowedFields("productNumber", "searchText");
	}
	
	/**
	 * Fetch a product with the specified product number.
	 * 
	 * @param productNumber
	 *            A numeric, 9 digit product number.
	 * @return The product if found.
	 * @throws ProductNotFoundException
	 *             If the number is not recognised.
	 */
	@RequestMapping("/products/{productNumber}")
	public String byNumber(Model model, @PathVariable("productNumber") String productNumber, HttpServletRequest req, HttpServletResponse res) {

		logger.info("checking for proxy-routed header: " + req.getHeader("Host"));
		if (req.getHeader("proxy-routed") != null) {
            res.addHeader("proxy-routed", req.getHeader("proxy-routed"));
        }

		logger.info("products-service byNumber() invoked: " + productNumber);
		int size = 0;
		size = productRepository.countProducts();
		Product product = productRepository.findByNumber(productNumber);
		logger.info("products-service .count(): " + size);
		logger.info("products-service byNumber() found: " + product);
        model.addAttribute("product", product);
		if (product == null)
			throw new ProductNotFoundException(productNumber);
		else {
			return "product";
		}
	}
	
	/**
	 * Fetch products with the specified name. A partial case-insensitive match
	 * is supported. So <code>http://.../produts/productName/a</code> will find any
	 * products with upper or lower case 'a' in their name.
	 * 
	 * @param partialName
	 * @return A non-null, non-empty set of products.
	 * @throws ProductNotFoundException
	 *             If there are no matches at all.
	 */
	@RequestMapping("/products/productName/{name}")
	public String byName(Model model, @PathVariable("name") String partialName) {
		logger.info("products-service byName() invoked: "
				+ productRepository.getClass().getName() + " for "
				+ partialName);

		List<Product> products = productRepository
				.findByProductNameContainingIgnoreCase(partialName);
		logger.info("products-service byName() found: " + products);
		logger.info("\n\nmodel before adding list of products: " + model);
		model.addAttribute("products", products);
		model.addAttribute("search", partialName);
		logger.info("\n\nmodel after adding list of products: " + model);
		if (products == null || products.size() == 0)
			throw new ProductNotFoundException(partialName);
		else {
			return "products";
		}
	}
	
	@RequestMapping(value = "/products/search", method = RequestMethod.GET)
	public String searchForm(Model model) {
		model.addAttribute("searchCriteria", new SearchCriteria());
		return "productSearch";
	}
	
	@RequestMapping(value = "/products/dosearch")
	public String doSearch(Model model, SearchCriteria criteria,
			BindingResult result, HttpServletRequest req, HttpServletResponse res) {
		logger.info("web-service search() invoked: " + criteria);

		criteria.validate(result);

		if (result.hasErrors())
			return "productSearch";

		String productNumber = criteria.getProductNumber();
		if (StringUtils.hasText(productNumber)) {
			return byNumber(model, productNumber, req, res);
		} else {
			String searchText = criteria.getSearchText();
			return byName(model, searchText);
		}
	}
}
