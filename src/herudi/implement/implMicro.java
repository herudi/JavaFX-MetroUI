/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package herudi.implement;

import herudi.config.config2;
import herudi.interfaces.interMicro;
import herudi.model.MicroMarket;
import herudi.model.Product;
import java.util.List;
import javafx.scene.control.Alert;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class implMicro implements interMicro {
    @Autowired
    configSessionFactory csf;
    
    @Override
    public List<MicroMarket> selectData() {
        return csf.getSf().openSession().createCriteria(MicroMarket.class).list();
    }

    @Override
    public void saveOrUpdate(MicroMarket m) {
        if (m.getZipCode()==null) {
            csf.create(m);
        }else{
            csf.update(m);
        }
    }

    @Override
    public void delete(MicroMarket m) {
        try {
            Query q = csf.getSf().openSession().createQuery("delete from MicroMarket where zipCode = :zipCode");
            q.setParameter("zipCode", m.getZipCode());
            q.executeUpdate();
            config2.dialog(Alert.AlertType.INFORMATION, "Success Delete");
        } catch (Exception e) {
            config2.dialog(Alert.AlertType.INFORMATION, "Sorry, Can't Delete This Record");
        }
    }

    @Override
    public Integer chekZip(String a) {
        Integer ret = null;
        try {
            Query q = csf.getSf().openSession().createQuery("select count(*) from MicroMarket where zipCode = '"+a+"'");
            Object o = q.uniqueResult();
            ret = Integer.parseInt(o.toString());
        } catch (HibernateException | NumberFormatException e) {
        }
        return ret;
    }
    
}
