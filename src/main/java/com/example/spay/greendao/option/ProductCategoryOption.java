package com.example.spay.greendao.option;

import com.example.spay.greendao.entity.ProductCategoryInfo;
import com.example.spay.greendao.gen.ProductCategoryInfoDao;
import com.example.spay.ui.base.GodCodeApplication;
import com.example.spay.utils.LogUtil;

import java.util.List;

/**
 * Created by Administrator on 2018/7/27.
 */

public class ProductCategoryOption {
    private ProductCategoryOption() {
    }

    private static ProductCategoryOption defaultInstance;

    public static ProductCategoryOption getInstance() {
        ProductCategoryOption productCategoryOption = defaultInstance;
        if (defaultInstance == null) {
            synchronized (ProductCategoryOption.class) {
                if (defaultInstance == null) {
                    productCategoryOption = new ProductCategoryOption();
                    defaultInstance = productCategoryOption;
                }
            }
        }
        return productCategoryOption;
    }


    public int querry(String productType) {
        ProductCategoryInfoDao productCategoryInfoDao = GodCodeApplication.getInstance().getDaoSession().getProductCategoryInfoDao();

        List<ProductCategoryInfo> list = productCategoryInfoDao.queryBuilder()
                .where(ProductCategoryInfoDao.Properties.ProductType.eq(productType)).list();
        LogUtil.log("-----qqqqqqqq----"+list);
        if (list.size() > 0) {
            int productId = list.get(0).getProductId();
            return productId;
        } else {
            return 0;
        }
    }

    public void add(String productType,int productId){
        ProductCategoryInfoDao productCategoryInfoDao = GodCodeApplication.getInstance().getDaoSession().getProductCategoryInfoDao();
        LogUtil.log("-----queryType---------"+querry(productType));
        if(querry(productType)==0){
            productCategoryInfoDao.insert(new ProductCategoryInfo(productType,productId));
        }
    }

}
