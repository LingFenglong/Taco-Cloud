package com.taco.tacocloud.tacos.web;

import com.taco.tacocloud.tacos.Order;
import com.taco.tacocloud.tacos.data.JdbcOrderRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("orders")
public class OrderController {
    private JdbcOrderRepository orderRepo;

    @Autowired
    public OrderController(JdbcOrderRepository jdbcOrderRepo) {
        this.orderRepo = jdbcOrderRepo;
    }

    @GetMapping("/current")
    public String orderForm(Model model) {
        return "orderForm";
    }

    @PostMapping
    public String processOrder(@Valid Order order, Errors errors, SessionStatus sessionStatus) {
        if (errors.hasErrors()) {
            log.error(errors.toString());
            return "orderForm";
        }

        orderRepo.save(order);
        sessionStatus.setComplete();
        log.info("Order submitted: " + order);
        return "redirect:/";
    }
}
