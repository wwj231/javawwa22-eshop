package pl.sda.javawwa22project.controller;

import org.dom4j.rule.Mode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.sda.javawwa22project.converter.ItemConverter;
import pl.sda.javawwa22project.dto.ItemDto;
import pl.sda.javawwa22project.entity.Item;
import pl.sda.javawwa22project.exception.ItemNotFoundException;
import pl.sda.javawwa22project.service.ItemsService;

import javax.validation.Valid;
import java.util.Optional;
import java.util.stream.Collectors;

// 4). define endpoint in controller
@Controller
public class ItemController {

    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);
    private static final String ONE_ITEM_KEY = "itemsToShow";
    private static final String MANY_ITEMS_KEY = "items";
    private static final String CURRENT_OPERATION = "current_operation";
    private static final String ITEM_DTO = "itemDto";
    private static final String EXCEPTION_MESSAGE = "exc_message";

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

    @GetMapping("/item-delete/{id}")
    public String deleteItemById(Model model, @PathVariable Long id){
        logger.info("deleteItemById(): [{}]", id);
        itemsService.deleteItemById(id);
        return "redirect:/all-items";
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

    @GetMapping("/edit-item/{id}")
    public String editItem(Model model, @PathVariable("id") Long inputId){
        logger.info("edtItem() : [{}] ", inputId);

        Optional<Item> foundItem = itemsService.findItemById(inputId);
        var itemDto = foundItem.map(itemConverter::fromItem)
                .orElseThrow(() -> new ItemNotFoundException(String.format("Item with id [%d] not exist!", inputId)));

        model.addAttribute(ITEM_DTO, itemDto);
        model.addAttribute(CURRENT_OPERATION, "Editing item with id: " + inputId);
        return "items/add-edit";
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public String errorPage(ItemNotFoundException exc, Model model){
        logger.warn("Something is wrong...", exc);

        model.addAttribute(EXCEPTION_MESSAGE, exc.getMessage());

        return "exception/error-item-not found";
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
