package per.ervin.hotel.service.impl;

import per.ervin.hotel.mapper.HotelMapper;
import per.ervin.hotel.pojo.Hotel;
import per.ervin.hotel.service.IHotelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class HotelService extends ServiceImpl<HotelMapper, Hotel> implements IHotelService {
}
