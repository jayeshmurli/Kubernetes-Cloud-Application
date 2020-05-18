package com.neu.backend.dao;

import com.neu.backend.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.neu.backend.model.User;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDao {

    @Autowired
    MeterRegistry meterRegistry;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void saveUser(User user) {
        Timer timer = meterRegistry.timer("database.user.save");
        long startTime = System.nanoTime();
        this.entityManager.merge(user);
        timer.record(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
        timer.close();
    }

    @Transactional
    public User updateUser(User user, String Id){
        Timer timer = meterRegistry.timer("database.user.update");
        long startTime = System.nanoTime();
        User userToBeUpdated = this.entityManager.find(User.class, Id);
        userToBeUpdated.setFirst_name(user.getFirst_name());
        userToBeUpdated.setLast_name(user.getLast_name());
        userToBeUpdated.setPassword(user.getPassword());
        userToBeUpdated.setAccount_updated(new Date());
        timer.record(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
        flushAndClear();
        timer.close();
        return userToBeUpdated;
    }

    @Transactional
    public User getUser(String username) {
        Timer timer = meterRegistry.timer("database.user.get");
        long startTime = System.nanoTime();
        TypedQuery<User> query = this.entityManager.createQuery("SELECT u from User u WHERE u.email_address = ?1",User.class);
        query.setParameter(1,username);
        timer.record(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
        timer.close();
        return query.getSingleResult();
    }

    public String getStoredPasswordFromUser(String email) {
        String hashed_pw = "";
        try {
            Query query = this.entityManager.createQuery("SELECT u FROM User u WHERE u.email_address = ?1");
            query.setParameter(1, email);
            List<User> resultList = query.getResultList();
            hashed_pw = resultList.get(0).getPassword();

        } catch (Exception e) {

            hashed_pw = null;

        }


        return hashed_pw;
    }
    @Transactional
    public int checkIfUserExists(String email) {

        int result = 0;
        try {
            Query query = this.entityManager.createQuery("SELECT COUNT(u) FROM User u WHERE u.email_address = ?1");
            query.setParameter(1, email);
            Long resultInLong = (Long) query.getSingleResult();

            result = Math.toIntExact(resultInLong);
        } catch (Exception e) {

            result = 0;
        }

        return result;
    }

    void flushAndClear(){
        entityManager.flush();
        entityManager.clear();
    }
}
