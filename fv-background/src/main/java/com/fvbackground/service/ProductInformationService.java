package com.fvbackground.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fvbackground.entity.ProductInformation;
import com.fvbackground.mapper.ProductInformationMapper;
import org.springframework.stereotype.Service;

@Service
public class ProductInformationService extends ServiceImpl<ProductInformationMapper, ProductInformation> {

}
