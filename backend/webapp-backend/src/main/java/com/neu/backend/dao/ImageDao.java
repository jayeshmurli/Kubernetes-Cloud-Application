package com.neu.backend.dao;

import com.neu.backend.model.Image;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Repository
@Transactional
public class ImageDao {

    @Autowired
    MeterRegistry meterRegistry;

    @PersistenceContext
    private EntityManager entityManager;

    public Image findById(String id){
        Timer timer = meterRegistry.timer("database.image.get");
        long startTime = System.nanoTime();
        UUID uuid = UUID.fromString(id);
        Session currentSession = entityManager.unwrap(Session.class);
        Image image = currentSession.get(Image.class, uuid);
        timer.record(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
        timer.close();
        return image;
    }

    public Image save(Image image){
        Timer timer = meterRegistry.timer("database.image.save");
        long startTime = System.nanoTime();
        Session currentSession = entityManager.unwrap(Session.class);
        currentSession.saveOrUpdate(image);
        timer.record(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
        timer.close();
        return image;
    }

    public int delete(String id){
        Timer timer = meterRegistry.timer("database.image.delete");
        long startTime = System.nanoTime();
        UUID uuid = UUID.fromString(id);
        Session currentSession = entityManager.unwrap(Session.class);
        Query<Image> query = currentSession.createQuery("delete from Image where id=:imageid");
        query.setParameter("imageid", uuid);
        int result = query.executeUpdate();
        timer.record(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
        timer.close();
        return result;
    }

    public Image update(String id, Image image){
        Timer timer = meterRegistry.timer("database.image.delete");
        long startTime = System.nanoTime();
        UUID uuid = UUID.fromString(id);
        Session currentSession = entityManager.unwrap(Session.class);

        Image existingImage = currentSession.get(Image.class, uuid);
        if(existingImage != null){
            existingImage.setUrl(image.getUrl());
            entityManager.merge(existingImage);
            timer.record(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
            timer.close();
            return existingImage;
        }
        timer.record(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
        timer.close();
        return null;
    }

}
