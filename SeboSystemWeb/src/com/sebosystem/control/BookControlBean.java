package com.sebosystem.control;

import java.io.Serializable;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import com.sebosystem.dao.Author;
import com.sebosystem.dao.Book;
import com.sebosystem.ejb.BookBeanLocal;

@ManagedBean(name = "bookControlBean")
@ViewScoped
@URLMappings(mappings = {
        @URLMapping(id = "book_index", parentId = "index", viewId = "/faces/book/index.xhtml",
                pattern = "book"),
        @URLMapping(id = "book_index_paged", parentId = "book_index", viewId = "/faces/book/index.xhtml",
                pattern = "/page/#{ /[0-9]+/ page : BookRequestBean.currentPage}"),
        @URLMapping(id = "book_add", parentId = "book_index", viewId = "/faces/book/edit.xhtml",
                pattern = "/add"),
        @URLMapping(id = "book_view", parentId = "book_index", viewId = "/faces/book/view.xhtml",
                pattern = "/#{ /[0-9]+/ oid : BookRequestBean.bookOid }"),
        @URLMapping(id = "book_edit", parentId = "book_view", viewId = "/faces/book/edit.xhtml",
                pattern = "/edit")
})
public class BookControlBean implements Serializable {

    private static final long serialVersionUID = -6191544788464214418L;

    @Inject
    private BookBeanLocal bookBean;

    private Book model;
    private String authorName;
    private Author selectedAuthor;
    private int currentPage;

    public List<Book> getBooks() {
        return this.bookBean.getAllBooks();
    }

    public Book getModel() {
        if (this.model == null) {
            this.model = new Book();
            this.model.setYear(GregorianCalendar.getInstance().get(GregorianCalendar.YEAR));
        }

        return model;
    }

    public String save() throws Exception {
        // TODO Write the logic of save book in control
        if (this.getSelectedAuthor() != null)
            this.model.setAuthor(this.getSelectedAuthor());

        this.bookBean.save(this.model);
        return null;
    }

    public void setModel(Book model) {
        this.model = model;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public long getBookOid() {
        return this.model.getOid();
    }

    public void setBookOid(long oid) {
        this.setModel(this.bookBean.getBookByOid(oid));
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Author getSelectedAuthor() {
        return selectedAuthor;
    }

    public void setSelectedAuthor(Author selectedAuthor) {
        this.authorName = selectedAuthor.getName();
        this.selectedAuthor = selectedAuthor;
    }
}
