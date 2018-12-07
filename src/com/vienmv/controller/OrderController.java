package com.vienmv.controller;

import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.vienmv.controller.client.ClientBaseController;
import com.vienmv.model.Cart;
import com.vienmv.model.User;
import com.vienmv.model.CartItem;
import com.vienmv.service.CartItemService;
import com.vienmv.service.CartService;
import com.vienmv.service.UserService;
import com.vienmv.service.impl.CartServiceImpl;
import com.vienmv.service.impl.CartServiceItemImpl;
import com.vienmv.service.impl.UserServiceImpl;
@WebServlet(urlPatterns= {"/member/order"})
public class OrderController extends ClientBaseController {
	UserService userService = new UserServiceImpl();
	CartService cartService = new CartServiceImpl();
	CartItemService cartItemService = new CartServiceItemImpl();
	long time = System.currentTimeMillis();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		Object obj = session.getAttribute("account");
		User buyer = (User) obj;
		Cart cart = new Cart();
		cart.setBuyer(buyer);
		cart.setBuyDate(new java.sql.Date(time));
		cartService.insert(cart);

		
		Object objCart = session.getAttribute("cart");
		if (objCart != null) {
		//ep ve dung kieu cua no khi them vao o phan them vao gio hang controller
		Map<Integer, CartItem> map = (Map<Integer, CartItem>) objCart;

		for (CartItem cartItem: map.values()) {
		cartItem.setCart(cart);
		cartItemService.insert(cartItem);
		}

		}
		session.removeAttribute("cart");
		resp.sendRedirect(req.getContextPath()+"/home");

	
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}