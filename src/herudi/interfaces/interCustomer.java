/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package herudi.interfaces;

import herudi.model.Customer;
import herudi.model.DiscountCode;
import herudi.model.MicroMarket;
import java.util.List;
import javafx.collections.ObservableList;

/**
 *
 * @author Herudi
 */
public interface interCustomer {
    List<Customer> select();
    void saveOrUpdate(Customer c);
    void delete(Customer c);
    Integer auto();
    List<DiscountCode> selectCode();
    List<MicroMarket> selectZip();
}
