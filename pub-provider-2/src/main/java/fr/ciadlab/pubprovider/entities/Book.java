/*
 * $Id$
 * 
 * Copyright (c) 2019-22, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Systems and Transportation Laboratory ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.pubprovider.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "books")
@PrimaryKeyJoinColumn(name = "pubId")
public class Book extends Publication {

    /**
     *
     */
    private static final long serialVersionUID = 1656720692321416627L;

    @Column
    private String bookEditor;

    @Column
    private String bookPublisher;

    @Column
    private String bookVolume;

    @Column
    private String bookSeries;

    @Column
    private String bookAddress;

    @Column
    private String bookEdition;

    @Column
    private String bookPages;

    public Book(Publication p, Book b) {
        super(p);
        this.bookEditor = b.bookEditor;
        this.bookPublisher = b.bookPublisher;
        this.bookVolume = b.bookVolume;
        this.bookSeries = b.bookSeries;
        this.bookAddress = b.bookAddress;
        this.bookEdition = b.bookEdition;
        this.bookPages = b.bookPages;
    }

    public Book(Publication p, String bookEditor, String bookPublisher, String bookVolume, String bookSeries, String bookAddress,
                String bookEdition, String bookPages) {
        super(p);
        this.bookEditor = bookEditor;
        this.bookPublisher = bookPublisher;
        this.bookVolume = bookVolume;
        this.bookSeries = bookSeries;
        this.bookAddress = bookAddress;
        this.bookEdition = bookEdition;
        this.bookPages = bookPages;
    }

    public Book() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String getBookEditor() {
        return bookEditor;
    }

    public void setBookEditor(String bookEditor) {
        this.bookEditor = bookEditor;
    }

    public String getBookPublisher() {
        return bookPublisher;
    }

    public void setBookPublisher(String bookPublisher) {
        this.bookPublisher = bookPublisher;
    }

    public String getBookVolume() {
        return bookVolume;
    }

    public void setBookVolume(String bookVolume) {
        this.bookVolume = bookVolume;
    }

    public String getBookSeries() {
        return bookSeries;
    }

    public void setBookSeries(String bookSeries) {
        this.bookSeries = bookSeries;
    }

    public String getBookAddress() {
        return bookAddress;
    }

    public void setBookAddress(String bookAddress) {
        this.bookAddress = bookAddress;
    }

    public String getBookEdition() {
        return bookEdition;
    }

    public void setBookEdition(String bookEdition) {
        this.bookEdition = bookEdition;
    }

    public String getBookPages() {
        return bookPages;
    }

    public void setBookPages(String bookPages) {
        this.bookPages = bookPages;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((bookAddress == null) ? 0 : bookAddress.hashCode());
        result = prime * result + ((bookEdition == null) ? 0 : bookEdition.hashCode());
        result = prime * result + ((bookEditor == null) ? 0 : bookEditor.hashCode());
        result = prime * result + ((bookPages == null) ? 0 : bookPages.hashCode());
        result = prime * result + ((bookPublisher == null) ? 0 : bookPublisher.hashCode());
        result = prime * result + ((bookSeries == null) ? 0 : bookSeries.hashCode());
        result = prime * result + ((bookVolume == null) ? 0 : bookVolume.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Book other = (Book) obj;
        if (bookAddress == null) {
            if (other.bookAddress != null)
                return false;
        } else if (!bookAddress.equals(other.bookAddress))
            return false;
        if (bookEdition == null) {
            if (other.bookEdition != null)
                return false;
        } else if (!bookEdition.equals(other.bookEdition))
            return false;
        if (bookEditor == null) {
            if (other.bookEditor != null)
                return false;
        } else if (!bookEditor.equals(other.bookEditor))
            return false;
        if (bookPages == null) {
            if (other.bookPages != null)
                return false;
        } else if (!bookPages.equals(other.bookPages))
            return false;
        if (bookPublisher == null) {
            if (other.bookPublisher != null)
                return false;
        } else if (!bookPublisher.equals(other.bookPublisher))
            return false;
        if (bookSeries == null) {
            if (other.bookSeries != null)
                return false;
        } else if (!bookSeries.equals(other.bookSeries))
            return false;
        if (bookVolume == null) {
            if (other.bookVolume != null)
                return false;
        } else if (!bookVolume.equals(other.bookVolume))
            return false;
        return true;
    }


}


