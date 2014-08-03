package com.sebosystem.control;

import java.io.Serializable;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import com.ocpsoft.pretty.faces.annotation.URLQueryParameter;
import com.sebosystem.dao.Author;
import com.sebosystem.dao.Book;
import com.sebosystem.ejb.BookBeanLocal;
import com.sebosystem.i18n.I18NFacesUtils;

@ManagedBean(name = "bookControlBean")
@ViewScoped
@URLMappings(mappings = {
        @URLMapping(id = "book_index", parentId = "index", viewId = "/faces/book/index.xhtml",
                pattern = "book"),
        @URLMapping(id = "book_index_paged", parentId = "book_index", viewId = "/faces/book/index.xhtml",
                pattern = "/page/#{ /[0-9]+/ page : bookControlBean.currentPage}"),
        @URLMapping(id = "book_add", parentId = "book_index", viewId = "/faces/book/edit.xhtml",
                pattern = "/add"),
        @URLMapping(id = "book_view", parentId = "book_index", viewId = "/faces/book/view.xhtml",
                pattern = "/#{ /[0-9]+/ oid : bookControlBean.bookOid }"),
        @URLMapping(id = "book_edit", parentId = "book_view", viewId = "/faces/book/edit.xhtml",
                pattern = "/edit")
})
public class BookControlBean implements Serializable {

    private static final long serialVersionUID = -6191544788464214418L;

    @Inject
    private BookBeanLocal bookBean;

    @ManagedProperty(value = "#{authorControlBean}")
    private AuthorControlBean authorControlBean;

    @URLQueryParameter("title")
    protected String filterTitle = "";

    private Book model;
    private String authorName;
    private Author selectedAuthor;
    private int currentPage;

    public String filter() {
        if (!this.filterTitle.isEmpty() && this.filterTitle.length() < 3) {
            this.filterTitle = "";
            FacesContext.getCurrentInstance().addMessage("warning", new FacesMessage(I18NFacesUtils.getLocalizedString("min_title_filter_string")));
            return null;
        }

        this.setCurrentPage(1);
        return "pretty:book_index";
    }

    public void openAuthorSelect() {
        System.out.println("openAuthorSelect" + this.getSelectedAuthor());
        this.authorControlBean.setModel(this.getSelectedAuthor());
        this.authorControlBean.setFilterName(this.getAuthorName());
    }

    public String markAsDuplicated() {
        // TODO Write the content of Mark As Duplicated method
        this.model.setMarkedAsDuplicated(true);
        try {
            this.bookBean.save(this.model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "pretty:author_view";
    }

    public String save() throws Exception {
        // TODO Write the logic of save book in control
        if (this.getSelectedAuthor() != null)
            this.model.setAuthor(this.getSelectedAuthor());

        this.bookBean.save(this.model);
        return "pretty:book_view";
    }

    public void setBookOid(String oid) {
        this.setBookOid(Long.parseLong(oid));
    }

    public void setBookOid(long oid) {
        this.setModel(this.bookBean.getBookByOid(oid));
    }

    public long getBookOid() {
        if (this.model == null)
            return 0;
        else
            return this.getModel().getOid();
    }

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

    public void setModel(Book model) {
        this.model = model;

        if (this.model != null && this.model.getAuthor() != null) {
            this.setSelectedAuthor(this.model.getAuthor());
            this.authorName = this.model.getAuthor().getName();
        } else
            this.authorName = "";
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
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
        this.authorControlBean.setModel(selectedAuthor);
        this.selectedAuthor = selectedAuthor;
    }

    public String getFilterTitle() {
        return filterTitle;
    }

    public void setFilterTitle(String filterTitle) {
        this.filterTitle = filterTitle.trim();
    }

    public boolean isFiltered() {
        return this.filterTitle != null && !this.filterTitle.isEmpty();
    }

    public boolean isFirstPage() {
        // TODO Implementar a parte de paginação para a sessão de livros
        return true;
    }

    public boolean isLastPage() {
        // TODO Implementar a parte de paginação para a sessão de livros
        return true;
    }

    public AuthorControlBean getAuthorControlBean() {
        return authorControlBean;
    }

    public void setAuthorControlBean(AuthorControlBean authorControlBean) {
        this.authorControlBean = authorControlBean;
    }
}
