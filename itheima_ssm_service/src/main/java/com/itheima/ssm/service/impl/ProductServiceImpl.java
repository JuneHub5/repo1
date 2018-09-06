package com.itheima.ssm.service.impl;

import com.github.pagehelper.PageHelper;
import com.itheima.ssm.dao.IProductDao;
import com.itheima.ssm.domain.Product;
import com.itheima.ssm.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品业务层实现类
 * @auther itheima
 * @create 2018-08-31 12:25
 */
@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private IProductDao productDao;

    /**
     * 新建产品信息
     * @param product
     */
    @Override
    public void save(Product product) {
        productDao.save(product);
    }

    /**
     * 查询所有产品信息
     * @return
     * @throws Exception
     */
    @Override
    public List<Product> findAll(int page,int size) throws Exception{
        //参数pageNum 是页码值   参数pageSize 代表是每页显示条数
        PageHelper.startPage(page, size);
        return productDao.findAll();
    }
}
