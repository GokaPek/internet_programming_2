package com.example.demo.core.api;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

//import com.example.demo.core.session.SessionCart;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class GlobalController {
    //private final SessionCart cart;

    // public GlobalController(SessionCart cart) {
    //     this.cart = cart;
    // }

    @ModelAttribute("servletPath")
    String getRequestServletPath(HttpServletRequest request) {
        return request.getServletPath();
    }

    // @ModelAttribute("totalCart")
    // double getTotalCart(HttpSession session) {
    //     return cart.getSum();
    // }
}
