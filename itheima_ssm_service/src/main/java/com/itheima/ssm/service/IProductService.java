package com.itheima.ssm.service;

import com.itheima.ssm.domain.Product;

import java.util.List;

/**
 * 商品信息业务层接口
 * @auther itheima
 * @create 2018-08-31 12:25
 */
public interface IProductService {

    public List<Product> findAll(int page,int size) throws Exception;

    void save(Product product) throws Exception;

}
