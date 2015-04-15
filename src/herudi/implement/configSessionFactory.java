/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package herudi.implement;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class configSessionFactory {
    @Autowired
    SessionFactory sf;

    public SessionFactory getSf() {
        return sf;
    }
    
    public void setSf(SessionFactory sf) {
        this.sf = sf;
    }

    public void create(Object o) {
        Session session = this.sf.openSession();
        Transaction tx = session.beginTransaction();
        try {
           session.persist(o);
        } catch (HibernateException e) {
           System.out.println("Gagal Create"); 
        }
        tx.commit();
        session.close();
    }

    public void update(Object o) {
        Session session = this.sf.openSession();
        Transaction tx = session.beginTransaction();
        try {
            session.merge(o);
        } catch (HibernateException e) {
            System.out.println("Gagal Update"); 
        }
        tx.commit();
        session.close();
    }

    public void delete(Object o) {
        Session session = this.sf.openSession();
        Transaction tx = session.beginTransaction();
        try {
            session.delete(o);
        } catch (HibernateException e) {
            System.out.println("Gagal Delete"); 
        }
        
        tx.commit();
        session.close();
    }
    
}
