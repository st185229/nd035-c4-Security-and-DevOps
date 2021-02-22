package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.service.ItemService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/item")
@Log4j2
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public ResponseEntity<List<Item>> getItems() {
        log.info("Get Items");
        var items =itemService.findAllInventoryItems();
        log.debug("Number of items returned={}", items.size());
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        log.debug("Get Item id={}",id);
        var item = itemService.findInventoryItemById(id);
        log.debug("Get Item id={},item={}",id,item);
        return ResponseEntity.of(item);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) {
        log.debug("Get Item by name={}",name);
        List<Item> items = itemService.findInventoryItemByName(name);
        log.debug("Number of items returned={}", items.size());
        if( items.isEmpty()){
            log.debug("No items found with name={}",name);
            return ResponseEntity.notFound().build();
        }else{
            log.debug("No items of count={}",items.size());
            return  ResponseEntity.ok(items);
        }

    }

}
