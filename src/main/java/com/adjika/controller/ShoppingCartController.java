package com.adjika.controller;

import java.util.ArrayList;
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

import com.adjika.dao.ProductDao;
import com.adjika.entity.Product;
import com.adjika.model.HotnessInfo;
import com.adjika.model.ItemInfo;
import com.adjika.model.ProductInfo;
import com.adjika.model.ShoppingCart;
import com.adjika.utils.AdjikaUtils;
import com.adjika.validator.HotnessValidator;

@Controller
public class ShoppingCartController {

	@Autowired
	private ProductDao productDao;

	@Autowired
	private HotnessValidator hotnessValidator;

	public ProductDao getProductDao() {
		return productDao;
	}

	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}

	public HotnessValidator getHotnessValidator() {
		return hotnessValidator;
	}

	public void setHotnessValidator(HotnessValidator hotnessValidator) {
		this.hotnessValidator = hotnessValidator;
	}

	@RequestMapping(value = "/addToCart", method = RequestMethod.GET)
	public String addToCart(@RequestParam(value = "productId", required = true) int productId,
			HttpServletRequest httpServletRequest) {

		Product product = this.productDao.getProductDetails(productId);

		if (product == null) {
			return "redirect:/403";
		}

		ProductInfo productInfo = new ProductInfo(product);

		ShoppingCart cart = AdjikaUtils.getOrderFromSession(httpServletRequest);
		cart.addItemToCart(productInfo, 1);

		return "redirect:/shoppingCart";
	}

	@RequestMapping(value = "/shoppingCart", method = RequestMethod.GET)
	public String shoppingCart(HttpServletRequest httpServletRequest, ModelMap map) {
		ShoppingCart cart = AdjikaUtils.getOrderFromSession(httpServletRequest);

		map.addAttribute("cart", cart);
		return "cart";
	}

	public String updateCart(HttpServletRequest httpServletRequest, ModelMap map) {
		return "";
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String delete(@RequestParam(value = "code", required = true) String code,
			HttpServletRequest httpServletRequest) {

		ShoppingCart cart = AdjikaUtils.getOrderFromSession(httpServletRequest);
		cart.removeItemFromCart(code);
		return "redirect:/shoppingCart";
	}

	@RequestMapping(value = "/hotness", method = RequestMethod.GET)
	public String hotnessLevel(HttpServletRequest httpServletRequest, Model map) {

		ShoppingCart cart = AdjikaUtils.getOrderFromSession(httpServletRequest);

		if (cart.isEmpty()) {
			return "redirect:shoppingCart";
		}

		HotnessInfo hotnessInfo = new HotnessInfo();

		List<ItemInfo> infos = new ArrayList<ItemInfo>();

		for (ItemInfo info : cart.getCartItem()) {
			if (AdjikaUtils.categoriesForHotnessLevel().contains(info.getProductInfo().getCategoryId())) {
				infos.add(info);
			}
		}

		if (infos.isEmpty()) {
			return "redirect:checkout";
		}

		map.addAttribute("orderItems", infos);
		map.addAttribute("hotnessInfo", hotnessInfo);

		return "hotness";
	}

	@RequestMapping(value = "/hotness", method = RequestMethod.POST)
	public String hotnessLevel(HttpServletRequest httpServletRequest, Model map,
			@ModelAttribute("hotnessInfo") HotnessInfo hotnessInfo, BindingResult bindingResult) {

		hotnessValidator.validate(hotnessInfo, bindingResult);

		if (bindingResult.hasErrors()) {
			return "redirect:hotness";
		}

		ShoppingCart cart = AdjikaUtils.getOrderFromSession(httpServletRequest);

		if (cart.isEmpty()) {
			return "redirect:shoppingCart";
		}

		cart.updateHotnessLevel(hotnessInfo.getHotnessLevel());

		return "redirect:checkout";
	}

}
