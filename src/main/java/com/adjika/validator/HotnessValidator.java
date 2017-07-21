package com.adjika.validator;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.adjika.model.HotnessInfo;
import com.adjika.model.ItemInfo;
import com.adjika.model.ShoppingCart;
import com.adjika.utils.AdjikaUtils;

@Component
public class HotnessValidator implements Validator {

	public boolean supports(Class<?> arg0) {
		return arg0 == HotnessInfo.class;
	}

	@Autowired(required = true)
	private HttpServletRequest request;

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void validate(Object arg0, Errors errors) {

		HotnessInfo hotnessInfo = (HotnessInfo) arg0;

		ShoppingCart cart = AdjikaUtils.getOrderFromSession(request);

		/*if (hotnessInfo.getHotnessLevel() == null){// && AdjikaUtils.categoriesForHotnessLevel().contains(cart.getCartItem().get(0).getProductInfo().getCategoryId()) ) {
			errors.rejectValue("hotnessLevel[0]", "NotEmpty.hotness.value");
		} else {
			List<String> a = hotnessInfo.getHotnessLevel();
			
			if (a != null && cart != null) {
				int i=0;
				for (ItemInfo info:cart.getCartItem()) {
					try {
						if (AdjikaUtils.categoriesForHotnessLevel().contains(info.getProductInfo().getCategoryId()) && a.get(i) == null) {
							errors.rejectValue("hotnessLevel[" + i + "]", "NotEmpty.hotness.value");
						}
							
					} catch (IndexOutOfBoundsException e) {
						errors.rejectValue("hotnessLevel[" + i + "]", "NotEmpty.hotness.value");
					}
					i++;

				}
			}

		}*/
		
		
		int count=0;
		List<String> a = hotnessInfo.getHotnessLevel();
		
		for(ItemInfo info:cart.getCartItem()){
			
			if(AdjikaUtils.categoriesForHotnessLevel().contains(info.getProductInfo().getCategoryId())){
				try{
					if(a==null){
						errors.rejectValue("hotnessLevel[" + count + "]", "NotEmpty.hotness.value");
						break;
					}else if(a.get(count)==null){
						errors.rejectValue("hotnessLevel[" + count + "]", "NotEmpty.hotness.value");
						break;
					}else{
						count++;
					}
				}catch (IndexOutOfBoundsException e) {
					errors.rejectValue("hotnessLevel[" + count + "]", "NotEmpty.hotness.value");
				}
				
				
			}
			
		}
		

	}

}
