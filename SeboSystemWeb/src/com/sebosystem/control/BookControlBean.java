package com.sebosystem.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import com.ocpsoft.pretty.faces.annotation.URLQueryParameter;
import com.sebosystem.control.base.AbstractControlBean;
import com.sebosystem.control.types.BookFilterType;
import com.sebosystem.dao.Author;
import com.sebosystem.dao.Book;
import com.sebosystem.dao.Copy;
import com.sebosystem.dao.Excerpt;
import com.sebosystem.dao.Request;
import com.sebosystem.ejb.BookBeanLocal;

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
    public static final String PAGED = "pretty:book_index_paged";

    @EJB
    private BookBeanLocal bookBean;

    @URLQueryParameter("value")
    protected String filterString = "";

    @URLQueryParameter("type")
    private BookFilterType bookFilter;

    private Book model;
    private Author selectedAuthor;
    private int currentPage;
    private int itemsPerPage = 18;

    private List<Book> books;

    private static List<BookFilterType> bookSelectFitler = null;

    private int totalPages = -1;

    static {
        bookSelectFitler = Arrays.asList(new BookFilterType[] { BookFilterType.BookTitle, BookFilterType.BookAuthor });
    }

    /**
     * Retrives if the parameter {@link Book} is the same of the {@code model}
     * attribute.
     * 
     * @param book
     * @return
     */
    public boolean isModel(Book book) {
        if (this.model == null)
            return false;

        return this.model.getOid() == book.getOid();
    }

    public Book getModel() {
        if (this.model == null) {
            this.model = new Book();
            this.model.setYear(GregorianCalendar.getInstance().get(GregorianCalendar.YEAR));
        }

        return model;
    }

    public String save() {
        // TODO Write the logic of save book in control
        if (this.getSelectedAuthor() != null)
            this.model.setAuthor(this.getSelectedAuthor());
        try {
            this.bookBean.save(this.model);
        } catch (Exception e) {
            this.addExceptionToFacesMessage("error", FacesMessage.SEVERITY_ERROR, e);
            return null;
        }
        return "pretty:book_view";
    }

    @RolesAllowed("moderator")
    public String remove() {

        try {
            this.bookBean.remove(this.model);
        } catch (Exception e) {
            this.addExceptionToFacesMessage("error", FacesMessage.SEVERITY_ERROR, e);
        }

        return "pretty:book_index";
    }

    public void toogleOwnedCopy() {
        Copy c = this.bookBean.getCopyByUserAndBook(this.getPrincipalAsUser(), this.getModel());

        try {
            if (c == null || !c.isOwned()) {
                c = this.bookBean.addBookToUser(this.model, this.getPrincipalAsUser());
                this.addFacesMessage("info", FacesMessage.SEVERITY_INFO, this.getLocalizedString("copy_added"), c.getBook().getTitle());
            } else {
                c = this.bookBean.removeBookOfUser(this.model, this.getPrincipalAsUser());
                this.addFacesMessage("info", FacesMessage.SEVERITY_INFO, this.getLocalizedString("copy_removed", c.getBook().getTitle()));
            }
        } catch (Exception e) {
            this.addExceptionToFacesMessage("error", FacesMessage.SEVERITY_ERROR, e);
            return;
        }

        return;
    }

    /**
     * Marks a book as duplicated in the system and open a report about it
     * 
     * @return
     */
    public String markAsDuplicated() {
        try {
            Request r = this.bookBean.reportDuplicated(this.model);
            this.addLocalizedFacesMessage("info", FacesMessage.SEVERITY_INFO, "book_report_request_created", r.getOid());
        } catch (Exception e) {
            this.addExceptionToFacesMessage("error", FacesMessage.SEVERITY_ERROR, e);
        }

        return "pretty:book_view";
    }

    public String rateBook(Book book, int rating) {

        book = this.bookBean.getBookByOid(book.getOid());

        if (book != null) {
            try {
                this.bookBean.rateBook(book, this.getPrincipalAsUser(), rating);

                if (this.getModel().getOid() != 0)
                    this.model = this.bookBean.getBookByOid(this.model.getOid());
            } catch (Exception e) {
                this.addExceptionToFacesMessage("error", FacesMessage.SEVERITY_ERROR, e);
                return null;
            }
        } else
            return "pretty:book_index";

        return null;
    }

    public void filterSelect() {

        if (!this.filterString.isEmpty() && this.filterString.length() < 3)
            return;

        this.filter();
    }

    /**
     * Set the parameters for the filter
     * 
     * @return
     */
    public String filter() {

        if (this.getBookFilter() == null) {
            this.addLocalizedFacesMessage("error", FacesMessage.SEVERITY_ERROR, "book_wrong_filter_type");
            return null;
        }

        if (!this.filterString.isEmpty() && this.filterString.length() < 3) {
            this.filterString = "";
            this.addLocalizedFacesMessage("warning", FacesMessage.SEVERITY_WARN, "min_title_filter_string");
            return null;
        }

        this.setCurrentPage(1);
        this.books = null;
        this.totalPages = -1;

        return "pretty:book_index";
    }

    /**
     * Refresh the values for the next page
     * 
     * @return
     */
    public String nextPage() {
        this.currentPage++;
        this.books = null;
        return PAGED;
    }

    /**
     * Refresh the values for the previews page
     * 
     * @return
     */
    public String previewsPage() {
        this.currentPage--;
        this.books = null;
        return PAGED;
    }

    public boolean isFiltered() {
        return this.filterString != null && !this.filterString.isEmpty();
    }

    /**
     * List of the books based on the parameters
     * 
     * @return
     */
    public List<Book> getBooks() {
        if (this.books != null)
            return this.books;

        int offset = (this.getCurrentPage() - 1) * this.getItemsByPage();

        if (this.isFiltered()) {

            switch (this.getBookFilter()) {
                case BookTitle:
                    this.books = this.bookBean.getBooksByTitle(this.filterString, offset, this.getItemsByPage());
                    break;
                case BookAuthor:
                    this.books = this.bookBean.getBooksByAuthorName(this.filterString, offset, this.getItemsByPage());
                    break;
                case BookYear:

                    try {
                        Integer year = 0;
                        year = Integer.valueOf(this.filterString);
                        this.books = this.bookBean.getBooksByYear(year, offset, this.getItemsByPage());
                    } catch (Exception e) {
                        this.books = new ArrayList<Book>();
                    }

                    break;
                case BookReview:
                    this.books = this.bookBean.getBooksByReview(this.filterString, offset, this.getItemsByPage());
                    break;
                case BookExcerpt:
                    this.books = this.bookBean.getBooksByExcerpt(this.filterString, offset, this.getItemsByPage());
                    break;
                default:
                    this.addLocalizedFacesMessage("error", FacesMessage.SEVERITY_ERROR, "book_wrong_filter_type");
                    return null;
            }

        } else
            this.books = bookBean.getAllBooks(offset, this.getItemsByPage());

        return this.books;
    }

    /**
     * Retrive less books for search modal
     * 
     * @return
     */
    public List<Book> getBooksForSearch() {
        this.setItemsPerPage(10);
        return this.getBooks();
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    /**
     * Retrieve the number of pages based on parameters
     * 
     * @return
     */
    public int getTotalPages() {
        if (this.totalPages > -1)
            return this.totalPages;

        long count = 0;

        if (this.isFiltered()) {

            switch (this.getBookFilter()) {
                case BookTitle:
                    count = this.bookBean.getBooksByTitleCount(this.filterString);
                    break;
                case BookAuthor:
                    count = this.bookBean.getBooksByAuthorNameCount(this.filterString);
                    break;
                case BookYear:

                    try {
                        Integer year;
                        year = Integer.valueOf(this.filterString);
                        count = this.bookBean.getBooksByYearCount(year);
                    } catch (Exception e) {
                        count = 0;
                    }

                    break;
                case BookReview:
                    count = this.bookBean.getBooksByReviewCount(this.filterString);
                    break;
                case BookExcerpt:
                    count = this.bookBean.getBooksByExcerptCount(this.filterString);
                    break;
                default:
                    this.addLocalizedFacesMessage("error", FacesMessage.SEVERITY_ERROR, "book_wrong_filter_type");
                    return 0;
            }

        } else {
            count = this.bookBean.getAllBooksCount();
        }

        this.totalPages = (int) Math.ceil((float) count / (float) this.getItemsByPage());

        return this.totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<Excerpt> getExcerpts() {
        if (this.model != null)
            return this.bookBean.getExcerptsOfBook(this.model);
        else
            return new ArrayList<Excerpt>();
    }

    public void selectAutorAction(Author author) {
        this.setSelectedAuthor(author);
        this.getModel().setAuthor(author);
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

    public void setModel(Book model) {
        this.model = model;
    }

    public int getCurrentPage() {
        if (this.currentPage < 1)
            this.currentPage = 1;

        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public String getFilterString() {
        return filterString;
    }

    public void setFilterString(String filterString) {
        this.filterString = filterString.trim();
    }

    /**
     * Is the first page?
     * 
     * @return
     */
    public boolean isFirstPage() {
        return this.getCurrentPage() == 1;
    }

    /**
     * Is the last page?
     * 
     * @return
     */
    public boolean isLastPage() {
        return this.getCurrentPage() >= this.getTotalPages();
    }

    public BookFilterType getBookFilter() {
        return bookFilter;
    }

    public void setBookFilter(BookFilterType bookFilter) {
        this.bookFilter = bookFilter;
    }

    /**
     * Retrieves book's filter type
     * 
     * @return
     */
    public List<BookFilterType> getBooksFilter() {
        return Arrays.asList(BookFilterType.values());
    }

    /**
     * Retrieves book's filters for selecion modal
     * 
     * @return
     */
    public List<BookFilterType> getBookSelectFitler() {
        return bookSelectFitler;
    }

    /**
     * Number of items per page
     * 
     * @return
     */
    public int getItemsByPage() {
        return itemsPerPage;
    }

    /**
     * Number of items per page
     * 
     * @param itemsPerPage
     */
    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }
}
