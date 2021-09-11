package pers.ervin.item.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.ervin.item.pojo.Item;

public interface IItemService extends IService<Item> {
    void saveItem(Item item);
}
