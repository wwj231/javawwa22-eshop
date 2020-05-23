package pl.sda.javawwa22project.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.sda.javawwa22project.converter.ItemConverter;
import pl.sda.javawwa22project.dto.ItemDto;
import pl.sda.javawwa22project.service.ItemsService;

import javax.validation.Valid;
import java.util.stream.Collectors;

// 4). define endpoint in controller
@Controller
public class ItemController {

    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);
    private static final String ONE_ITEM_KEY = "itemsToShow";
    private static final String MANY_ITEMS_KEY = "items";
    private static final String CURRENT_OPERATION = "current_operation";
    private static final String ITEM_DTO = "itemDto";

    private final ItemsService itemsService;
    private final ItemConverter itemConverter;

    public ItemController(final ItemsService itemsService, final ItemConverter itemConverter) {
        this.itemsService = itemsService;
        this.itemConverter = itemConverter;
    }

    // /items/1
    // /items/1024
    @GetMapping("/items/{id}")
    public String displayItemById(@PathVariable Long id, Model model) {
        logger.info("displayItemById with id: [{}]", id);
        var itemDto = itemsService.findItemById(id)
            .map(itemConverter::fromItem)
            .orElse(ItemDto.builder().build());

        model.addAttribute(ONE_ITEM_KEY, itemDto);
        return "items/show-item-page";
    }

    @GetMapping("/all-items")
    public String getAllItems(Model model) {
        logger.info("getAllItems");
        var itemsToShow = itemsService.findAllItems()
            .stream()
            .map(itemConverter::fromItem)
            .collect(Collectors.toList());

        model.addAttribute(MANY_ITEMS_KEY, itemsToShow);
        return "items/all-items";
    }

    @GetMapping("/add-item")
    public String addItem(Model model){
        logger.info("addItem()");
        model.addAttribute(ITEM_DTO, ItemDto.builder().build());
        model.addAttribute(CURRENT_OPERATION, "Adding new item");
        return "items/add-edit";
    }

    @PostMapping("/item-save")
    public String saveItem(@Valid ItemDto itemToSave){
        logger.info("saveItem(), received param: [{}]", itemToSave);
        var item = itemConverter.fromDto(itemToSave);
        var savedItem = itemsService.saveItem(item);
        return "redirect:/items/" + savedItem.getId();
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
}
