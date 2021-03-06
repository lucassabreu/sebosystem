package com.sebosystem.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import com.ocpsoft.pretty.faces.annotation.URLQueryParameter;
import com.sebosystem.control.base.AbstractControlBean;
import com.sebosystem.dao.Book;
import com.sebosystem.dao.Request;
import com.sebosystem.dao.User;
import com.sebosystem.ejb.RequestBeanLocal;

@ManagedBean(name = "requestControlBean")
@ViewScoped
@URLMappings(mappings = {
        @URLMapping(id = "my_requests", parentId = "index", viewId = "/faces/request/user.xhtml",
                pattern = "my/requests"),
        @URLMapping(id = "my_requests_index_paged", parentId = "my_requests", viewId = "/faces/request/user.xhtml",
                pattern = "/page/#{ /[0-9]+/ page : requestControlBean.currentPage}"),
        @URLMapping(id = "request_index", parentId = "index", viewId = "/faces/request/index.xhtml",
                pattern = "request"),
        @URLMapping(id = "request_index_paged", parentId = "request_index", viewId = "/faces/request/index.xhtml",
                pattern = "/page/#{ /[0-9]+/ page : requestControlBean.currentPage}"),
        @URLMapping(id = "request_add", parentId = "request_index", viewId = "/faces/request/edit.xhtml",
                pattern = "/add"),
        @URLMapping(id = "request_view", parentId = "request_index", viewId = "/faces/request/view.xhtml",
                pattern = "/#{ /[0-9]+/ oid : requestControlBean.requestOid }"),
        @URLMapping(id = "request_edit", parentId = "request_view", viewId = "/faces/request/edit.xhtml",
                pattern = "/edit")
})
public class RequestControlBean extends AbstractControlBean implements Serializable {

    private static final long serialVersionUID = 5932467761709478063L;

    @EJB
    private RequestBeanLocal requestBean;

    private int currentPage;

    private User user;

    private Request model;

    private Book selectedBook;

    @URLQueryParameter("onlyOpen")
    private boolean onlyOpen = true;

    @URLQueryParameter("onlyWithoutModerator")
    private boolean onlyWithoutModerator = true;

    private List<Book> relatedBooks = null;

    public String filter(String userView) {
        return filter(Boolean.valueOf(userView));
    }

    /**
     * Filter the content based on users parameters
     * 
     * @param userView
     * @return
     */
    public String filter(boolean userView) {
        this.setCurrentPage(1);

        if (userView)
            return "pretty:my_requests";
        else
            return "pretty:request_index";
    }

    public String takeOn() {
        try {
            this.requestBean.takeOn(this.getModel());
        } catch (Exception e) {
            this.addExceptionToFacesMessage("error", FacesMessage.SEVERITY_ERROR, e);
            return null;
        }

        return "pretty:request_edit";
    }

    public String accept() {
        try {

            if (this.save() == null)
                return null;

            this.requestBean.accept(this.getModel());
        } catch (Exception e) {
            this.addExceptionToFacesMessage("error", FacesMessage.SEVERITY_ERROR, e);
            return null;
        }

        return "pretty:request_index";
    }

    /**
     * Save the state of the current {@link Request}
     * 
     * @return
     */
    public String save() {

        if (this.model == null)
            return null;

        if (this.model.isBookDuplicated()) {
            this.model.setRelatedBooks(this.getRelatedBooks());
            System.out.println(this.model.getRelatedBooks());
        }

        try {
            this.requestBean.save(this.model);
        } catch (Exception e) {
            this.addExceptionToFacesMessage("error", FacesMessage.SEVERITY_ERROR, e);
            e.printStackTrace();
            return null;
        }

        return "pretty:request_view";
    }

    public String reject() {
        try {
            this.requestBean.reject(this.getModel());
        } catch (Exception e) {
            this.addExceptionToFacesMessage("error", FacesMessage.SEVERITY_ERROR, e);
            e.printStackTrace();
            return null;
        }

        return "pretty:request_index";
    }

    public String cancel() {
        try {
            this.requestBean.cancel(this.getModel());
        } catch (Exception e) {
            this.addExceptionToFacesMessage("error", FacesMessage.SEVERITY_ERROR, e);
            return null;
        }

        return "pretty:my_requests";
    }

    /**
     * Add the selected book to the relatedbook list
     * 
     * @param book
     */
    public void selectBookAction(Book book) {

        if (this.getRelatedBooks().contains(book) || this.getModel().getBook().equals(book)) {
            this.addLocalizedFacesMessage("warn", FacesMessage.SEVERITY_WARN, "request_book_already_related", book.getOid(), book.getTitle());
            return;
        }

        if (book.isMarkedAsDuplicated()) {
            this.addLocalizedFacesMessage("warn", FacesMessage.SEVERITY_WARN, "request_book_duplicated_already_in_progress", //
                    book.getTitle(), book.getOid());
            return;
        }

        this.getRelatedBooks().add(book);
    }

    public void removeBook() {
        if (this.model == null || this.selectedBook == null)
            return;

        System.out.println("loucura");

        if (this.model.isBookDuplicated()) {

            for (Book book : this.getRelatedBooks()) {
                if (book.getOid() == this.selectedBook.getOid()) {
                    this.getRelatedBooks().remove(book);
                    break; // leave the for books
                }
            }

            this.addLocalizedFacesMessage("info", FacesMessage.SEVERITY_INFO, "request_book_removed_success", this.getSelectedBook().getTitle());
            this.setSelectedBook(null);

        } else {
            this.addLocalizedFacesMessage("warn", FacesMessage.SEVERITY_WARN, "request_was_not_book_duplicated", this.model.getOid());
        }
    }

    public List<Request> getRequests() {

        if (this.isOnlyOpen() && this.isOnlyWithoutModerator()) {
            return this.requestBean.getOpenRequestsWithoutModerator();
        }

        if (this.isOnlyOpen())
            return this.requestBean.getOpenRequests();

        if (this.isOnlyWithoutModerator())
            return this.requestBean.getRequestsWithoutModerator();

        return this.requestBean.getAllRequests();
    }

    public List<Request> getRequestsByRequester() {
        return this.requestBean.getRequestsByRequester(getCurrentUser());
    }

    public List<Request> getRequestsByModerator() {
        return this.requestBean.getRequestsByModerator(getCurrentUser());
    }

    public Request getModel() {
        return model;
    }

    public void setModel(Request model) {
        this.model = model;
    }

    /**
     * Keeps the related books in the session
     * 
     * @return
     */
    public List<Book> getRelatedBooks() {
        if (this.relatedBooks == null) {
            this.relatedBooks = new ArrayList<Book>(this.getModel().getRelatedBooks());
        }

        return relatedBooks;
    }

    public void setRelatedBooks(List<Book> relatedBooks) {
        this.relatedBooks = relatedBooks;
    }

    public void setRequestOid(long oid) {
        if (oid != 0)
            this.setModel(this.requestBean.getRequestByOid(oid));
    }

    public long getRequestOid() {
        return this.model == null ? 0 : this.model.getOid();
    }

    public boolean isUserPage() {
        return this.getUserOid() == 0 || this.getUserOid() == this.getCurrentUser().getOid();
    }

    public User getCurrentUser() {
        return this.getPrincipalAsUser();
    }

    public User getUsableUser() {
        if (this.user == null) {
            return this.getCurrentUser();
        } else
            return this.getUser();
    }

    public User getUser() {
        return user;
    }

    public void setUserOid(long oid) {
        this.user = this.requestBean.getUserByOid(oid);
    }

    public long getUserOid() {
        if (this.getUser() == null)
            return 0;
        else
            return this.getUser().getOid();
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public boolean isFiltered() {
        // TODO Implementar controles de filtro para requisições
        return false;
    }

    public boolean isFirstPage() {
        // TODO Implementar a parte de paginação para a sessão de requisições
        return true;
    }

    public boolean isLastPage() {
        // TODO Implementar a parte de paginação para a sessão de requisições
        return true;
    }

    public boolean isOnlyWithoutModerator() {
        return onlyWithoutModerator;
    }

    public void setOnlyWithoutModerator(boolean withoutModerator) {
        this.onlyWithoutModerator = withoutModerator;
    }

    public boolean isOnlyOpen() {
        return onlyOpen;
    }

    public void setOnlyOpen(boolean onlyOpen) {
        this.onlyOpen = onlyOpen;
    }

    /**
     * Retrive the current selected book
     * 
     * @return
     */
    public Book getSelectedBook() {
        return selectedBook;
    }

    /**
     * Sets the current selected book
     * 
     * @param selectedBook
     */
    public void setSelectedBook(Book selectedBook) {
        this.selectedBook = selectedBook;
    }
}
