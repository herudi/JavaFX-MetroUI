/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package herudi.interfaces;

import herudi.model.Manufacturer;
import herudi.model.Product;
import herudi.model.ProductCode;
import java.util.List;

/**
 *
 * @author Herudi
 */
public interface interProduct {
    List<Product> selectData();
    void saveOrUpdate(Product p);
    void delete(Product p);
    Integer auto();
    List<Manufacturer> selectManufacturerID();
    List<ProductCode> selectProductCode();
}
