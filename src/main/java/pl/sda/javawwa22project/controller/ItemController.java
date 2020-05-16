package pl.sda.javawwa22project.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pl.sda.javawwa22project.converter.ItemConverter;
import pl.sda.javawwa22project.entity.Item;
import pl.sda.javawwa22project.service.ItemsService;

import java.util.ArrayList;
import java.util.List;

// 4). define endpoint in controller
@Controller
public class ItemController {

    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

    private final ItemsService itemsService;
    private final ItemConverter itemConverter;

    public ItemController(final ItemsService itemsService, final ItemConverter itemConverter) {
        this.itemsService = itemsService;
        this.itemConverter = itemConverter;
    }

    // /items/1
    // /items/1024
    @GetMapping("/items/{id}")
    String displayItemById(@PathVariable Long id, Model model) {
        var itemDto = itemsService.findItemById(id)
            .map(item -> itemConverter.fromItem(item))
            .orElse(null);

        model.addAttribute("item", itemDto);
        return "";
    }

    // TODO
    // metoda wyświetla 3 przykładowe przedmioty
    @GetMapping("all-items")
    String getAllItems(Model model) {
        List<Item> itemLis = new ArrayList<>();
        // dodaj 3 przedmioty
        model.addAttribute("items", itemLis);
        return "items/all-items";
    }
}
