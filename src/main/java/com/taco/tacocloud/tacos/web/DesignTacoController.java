package com.taco.tacocloud.tacos.web;

import com.taco.tacocloud.tacos.Ingredient;
import com.taco.tacocloud.tacos.Order;
import com.taco.tacocloud.tacos.Taco;
import com.taco.tacocloud.tacos.data.IngredientRepository;
import com.taco.tacocloud.tacos.data.TacoRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("order")
public class DesignTacoController {
    private final IngredientRepository ingredientRepo;
    private final TacoRepository designRepo;

    @Autowired
    public DesignTacoController(IngredientRepository ingredientRepo, TacoRepository designRepo) {
        this.ingredientRepo = ingredientRepo;
        this.designRepo = designRepo;
    }

    @ModelAttribute(name = "design")
    public Taco design() {
        return new Taco();
    }

    @ModelAttribute(name = "order")
    public Order order() {
        return new Order();
    }

    @ModelAttribute
    public void addIngredientToModel(Model model) {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredientRepo.findAll().forEach(i -> ingredients.add(i));

        Ingredient.Type[] types = Ingredient.Type.values();
        for (Ingredient.Type type : types) {
            model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
        }
    }

    @GetMapping
    public String showDesignForm() {
        return "design";
    }

    @PostMapping
    public String processDesign(@Valid @ModelAttribute("design") Taco design, Errors errors, @ModelAttribute Order order) {
        if (errors.hasErrors()) {
            log.error(errors.toString());
            return "/design";
        }

        Taco saved = designRepo.save(design);
        order.addDesign(saved);
        log.info(saved.toString());

        return "redirect:/orders/current";
    }


    private List<Ingredient> filterByType(List<Ingredient> ingredients, Ingredient.Type type) {
        return ingredients
                .stream()
                .filter(x -> x.getType().equals(type))
                .collect(Collectors.toList());
    }
}
