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
import java.util.stream.Stream;

// 4). define endpoint in controller
@Controller
public class ItemController {

    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);
    private static final String ITEM_KEY = "itemsToShow";

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
        logger.info("displayItemById with id: [{}]", id);
        var itemDto = itemsService.findItemById(id)
            .map(itemConverter::fromItem)
            .orElse(null);

        model.addAttribute(ITEM_KEY, itemDto);
        return "items/show-item-page";
    }

    // method reference example
//    static class Sorter {
//        static int orderItems(Item one, Item two) {
//            return -1;
//        }
//    }
//    private void sortItems() {
//        Stream.of(
//            new Item(1L, null, null, null, null, 5, null),
//            new Item(1L, null, null, null, null, 5, null)
//            )
//            .sorted(Sorter::orderItems);
//    }
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
