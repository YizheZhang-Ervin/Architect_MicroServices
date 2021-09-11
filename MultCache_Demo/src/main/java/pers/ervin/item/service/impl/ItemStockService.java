package pers.ervin.item.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import pers.ervin.item.service.IItemStockService;
import pers.ervin.item.mapper.ItemStockMapper;
import pers.ervin.item.pojo.ItemStock;
import org.springframework.stereotype.Service;

@Service
public class ItemStockService extends ServiceImpl<ItemStockMapper, ItemStock> implements IItemStockService {
}
