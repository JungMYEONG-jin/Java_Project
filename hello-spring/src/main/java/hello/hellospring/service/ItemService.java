package hello.hellospring.service;

import hello.hellospring.domain.item.Item;
import hello.hellospring.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item)
    {
        itemRepository.save(item);
    }

    public List<Item> findItems ()
    {
        return itemRepository.findAll();
    }

    public Item findOne(Long id)
    {
        return itemRepository.findOne(id);
    }
}
