package fr.ciadlab.pubprovider.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "bookChapters")
@PrimaryKeyJoinColumn(name = "pubId")
public class BookChapter extends Book {


    /**
     *
     */
    private static final long serialVersionUID = -443806489080823344L;

    @Column
    private String bookChapBookNameProceedings;

    @Column
    private String bookChapNumberOrName;

    public BookChapter(Publication p, Book b, String bookChapBookNameProceedings, String bookChapNumberOrName) {
        super(p, b);
        this.bookChapBookNameProceedings = bookChapBookNameProceedings;
        this.bookChapNumberOrName = bookChapNumberOrName;
    }

    public BookChapter() {
        super();
        // TODO Auto-generated constructor stub
    }


    public String getBookChapBookNameProceedings() {
        return bookChapBookNameProceedings;
    }

    public void setBookChapBookNameProceedings(String bookChapBookNameProceedings) {
        this.bookChapBookNameProceedings = bookChapBookNameProceedings;
    }

    public String getBookChapNumberOrName() {
        return bookChapNumberOrName;
    }

    public void setBookChapNumberOrName(String bookChapNumberOrName) {
        this.bookChapNumberOrName = bookChapNumberOrName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((bookChapBookNameProceedings == null) ? 0 : bookChapBookNameProceedings.hashCode());
        result = prime * result + ((bookChapNumberOrName == null) ? 0 : bookChapNumberOrName.hashCode());
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
        BookChapter other = (BookChapter) obj;
        if (bookChapBookNameProceedings == null) {
            if (other.bookChapBookNameProceedings != null)
                return false;
        } else if (!bookChapBookNameProceedings.equals(other.bookChapBookNameProceedings))
            return false;
        if (bookChapNumberOrName == null) {
            if (other.bookChapNumberOrName != null)
                return false;
        } else if (!bookChapNumberOrName.equals(other.bookChapNumberOrName))
            return false;
        return true;
    }


}


