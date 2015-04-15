/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package herudi.implement;

import herudi.config.config2;
import herudi.interfaces.interCustomer;
import herudi.model.Customer;
import herudi.model.DiscountCode;
import herudi.model.MicroMarket;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class implCustomer implements interCustomer {
    @Autowired
    configSessionFactory csf;

    @Override
    public void saveOrUpdate(Customer c) {
        if (c.getCustomerId()==null) {
            csf.create(c);
        }else{
            csf.update(c);
        }
    }

    @Override
    public Integer auto() {
        Integer ret = null;
        try {
            Query q = csf.getSf().openSession().createQuery("select max(t.customerId) from Customer t");
            Object o = q.uniqueResult();
            ret = Integer.parseInt(o.toString())+1;
        } catch (HibernateException | NumberFormatException e) {
        }
        return ret;
    }

    @Override
    public List<DiscountCode> selectCode() {
        Criteria cr = csf.getSf().openSession().createCriteria(DiscountCode.class);
        cr.setProjection(Projections.property("discountCode"));
        return cr.list();
    }

    @Override
    public List<MicroMarket> selectZip() {
        Criteria cr = csf.getSf().openSession().createCriteria(MicroMarket.class);
        cr.setProjection(Projections.property("zipCode"));
        return cr.list();
    }

    @Override
    public void delete(Customer c) {
        try {
            Query q = csf.getSf().openSession().createQuery("delete from Customer where customerId = :customerId");
            q.setParameter("customerId", c.getCustomerId());
            q.executeUpdate();
            config2.dialog(Alert.AlertType.INFORMATION, "Success Delete");
        } catch (Exception e) {
            config2.dialog(Alert.AlertType.INFORMATION, "Sorry, Can't Delete This Record");
        }
    }

    @Override
    public List<Customer> select() {
        return csf.getSf().openSession().createCriteria(Customer.class).list();
    }
}
