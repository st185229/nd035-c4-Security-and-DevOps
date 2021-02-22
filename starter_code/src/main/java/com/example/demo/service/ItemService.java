package com.example.demo.service;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@Service
@Log4j2
public class ItemService {
    private final ItemRepository itemRepository;
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }
    public Optional<Item> findInventoryItemById(Long id) {
        log.debug("Item queried by id={}",id);
        return itemRepository.findById(id);
    }
    public List<Item> findInventoryItemByName(String itemName) {
        log.debug("Return all items with name={}",itemName);
        return itemRepository.findByName(itemName);
    }
    public List<Item> findAllInventoryItems() {
        return itemRepository.findAll();
    }
    public void saveAllInventoryItems(List<Item> items) {
        log.debug("Save all inventory Items count={}",items.size());
        itemRepository.saveAll(items);
    }
}
