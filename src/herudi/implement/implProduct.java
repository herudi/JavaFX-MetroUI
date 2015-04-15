/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package herudi.implement;

import herudi.config.config2;
import herudi.interfaces.interProduct;
import herudi.model.Manufacturer;
import herudi.model.Product;
import herudi.model.ProductCode;
import java.util.List;
import javafx.scene.control.Alert;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class implProduct implements interProduct {
    @Autowired
    configSessionFactory csf;

    @Override
    public List<Product> selectData() {
        return csf.getSf().openSession().createCriteria(Product.class).list();
    }

    @Override
    public void saveOrUpdate(Product p) {
        if (p.getProductId()==null) {
            csf.create(p);
        }else{
            csf.update(p);
        }
    }

    @Override
    public void delete(Product p) {
        try {
            Query q = csf.getSf().openSession().createQuery("delete from Product where productId = :productId");
            q.setParameter("productId", p.getProductId());
            q.executeUpdate();
            config2.dialog(Alert.AlertType.INFORMATION, "Success Delete");
        } catch (Exception e) {
            config2.dialog(Alert.AlertType.INFORMATION, "Sorry, Can't Delete This Record");
        }
    }

    @Override
    public Integer auto() {
        Integer ret = null;
        try {
            Query q = csf.getSf().openSession().createQuery("select max(t.productId) from Product t");
            Object o = q.uniqueResult();
            ret = Integer.parseInt(o.toString())+1;
        } catch (HibernateException | NumberFormatException e) {
        }
        return ret;
    }

    @Override
    public List<Manufacturer> selectManufacturerID() {
        Criteria cr = csf.getSf().openSession().createCriteria(Manufacturer.class);
        cr.setProjection(Projections.property("manufacturerId"));
        return cr.list();
    }

    @Override
    public List<ProductCode> selectProductCode() {
        Criteria cr = csf.getSf().openSession().createCriteria(ProductCode.class);
        cr.setProjection(Projections.property("prodCode"));
        return cr.list();
    }
    
}
