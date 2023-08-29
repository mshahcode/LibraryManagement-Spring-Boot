package az.library.management.dao.entity;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.*;

public class Book_Log {
    private static final Log log = LogFactory.getLog(Book_Log.class);

    @PrePersist
    @PreUpdate
    @PreRemove
    public void beforeAnyUpdate(Book book) {
        if (book.getId() == null) log.info("About to add a book");
        else log.info("About to update/remove a book with id: " + book.getId());
    }


    @PostLoad
    public void postLoad(Book book) {
        log.info("A book loaded with id: " + book.getId());
    }

    @PostPersist
    @PostUpdate
    public void afterAnyUpdate(Book book) {
        log.info("A book was saved/updated with id: " + book.getId());
    }

    @PostRemove
    public void afterDelete(Book book){
        log.info("A book was removed with id: " + book.getId());
    }
}
