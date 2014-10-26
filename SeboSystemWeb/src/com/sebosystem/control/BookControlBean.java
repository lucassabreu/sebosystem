package com.sebosystem.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import com.ocpsoft.pretty.faces.annotation.URLQueryParameter;
import com.sebosystem.control.base.AbstractControlBean;
import com.sebosystem.dao.Author;
import com.sebosystem.dao.Book;
import com.sebosystem.dao.Copy;
import com.sebosystem.dao.Excerpt;
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
public class BookControlBean extends AbstractControlBean implements Serializable {

    private static final long serialVersionUID = -6191544788464214418L;

    public static final String BLANK = "";

    @EJB(name = "bookBean", mappedName = "ejb/BookBean")
    private BookBeanLocal bookBean;

    @URLQueryParameter("title")
    protected String filterTitle = "";

    @URLQueryParameter("type")
    private int propertyFilter;

    private Book model;
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

    public void selectAutorAction(Author author) {
        this.setSelectedAuthor(author);
        this.getModel().setAuthor(author);
    }

    public String markAsDuplicated() {
        // TODO Write the content of Mark As Duplicated method
        this.model.setMarkedAsDuplicated(true);
        try {
            this.bookBean.save(this.model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "pretty:book_view";
    }

    public String save() {
        // TODO Write the logic of save book in control
        if (this.getSelectedAuthor() != null)
            this.model.setAuthor(this.getSelectedAuthor());
        try {
            this.bookBean.save(this.model);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage("error", new FacesMessage(e.getLocalizedMessage()));
            return null;
        }
        return "pretty:book_view";
    }

    @RolesAllowed("moderator")
    public String remove() {

        try {
            this.bookBean.remove(this.model);
        } catch (Exception e) {
            addExceptionToFacesMessage("error", FacesMessage.SEVERITY_ERROR, e);
        }

        return "pretty:book_index";
    }

    public String rateBook(Book book, int rating) {

        book = this.bookBean.getBookByOid(book.getOid());

        if (book != null) {
            try {
                this.bookBean.rateBook(book, this.getPrincipalAsUser(), rating);

                if (this.getModel().getOid() != 0)
                    this.model = this.bookBean.getBookByOid(this.model.getOid());
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage("error", new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getLocalizedMessage(), BLANK));
                return null;
            }
        } else
            return "pretty:book_index";

        return null;
    }

    public void toogleOwnedCopy() {
        Copy c = this.bookBean.getCopyByUserAndBook(this.getPrincipalAsUser(), this.getModel());

        try {
            if (c == null || !c.isOwned()) {
                c = this.bookBean.addBookToUser(this.model, this.getPrincipalAsUser());
                FacesContext.getCurrentInstance().addMessage("info",
                        new FacesMessage(I18NFacesUtils.getLocalizedString("copy_added", c.getBook().getTitle())));
            } else {
                c = this.bookBean.removeBookOfUser(this.model, this.getPrincipalAsUser());
                FacesContext.getCurrentInstance().addMessage("info",
                        new FacesMessage(I18NFacesUtils.getLocalizedString("copy_removed", c.getBook().getTitle())));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage("error", new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getLocalizedMessage(), BLANK));
            return;
        }

        return;
    }

    public boolean isUserHasBook() {
        // TODO: Adicionar regras para vender/trocar livros

        if (!this.isAuthenticated())
            return false;

        Copy c = this.bookBean.getCopyByUserAndBook(this.getPrincipalAsUser(), this.getModel());
        return c != null && c.isOwned();
    }

    public Author getSelectedAuthor() {
        if (this.selectedAuthor == null)
            this.selectedAuthor = this.getModel().getAuthor();

        return selectedAuthor;
    }

    public void setSelectedAuthor(Author selectedAuthor) {
        this.selectedAuthor = selectedAuthor;
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

    public List<Excerpt> getExcerpts() {
        if (this.model != null)
            return this.bookBean.getExcerptsOfBook(this.model);
        else
            return new ArrayList<Excerpt>();
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
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
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

    public int getPropertyFilter() {
        return propertyFilter;
    }

    public void setPropertyFilter(int propertyFilter) {
        this.propertyFilter = propertyFilter;
    }
}
