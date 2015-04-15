/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package herudi.interfaces;

import herudi.model.MicroMarket;
import java.util.List;

/**
 *
 * @author Herudi
 */
public interface interMicro {
    List<MicroMarket> selectData();
    void saveOrUpdate(MicroMarket m);
    void delete(MicroMarket m);
    Integer chekZip(String a);
}
