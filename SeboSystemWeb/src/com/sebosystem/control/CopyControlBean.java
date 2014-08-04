package com.sebosystem.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.shiro.subject.Subject;

import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import com.ocpsoft.pretty.faces.annotation.URLQueryParameter;
import com.sebosystem.dao.Book;
import com.sebosystem.dao.Copy;
import com.sebosystem.dao.User;
import com.sebosystem.ejb.CopyBeanLocal;
import com.sebosystem.i18n.I18NFacesUtils;

@ManagedBean(name = "copyControlBean")
@ViewScoped
@URLMappings(mappings = {
        @URLMapping(id = "copy_index", parentId = "index", viewId = "/faces/copy/index.xhtml",
                pattern = "user/#{/[0-9]+/ oid : copyControlBean.userOid}/copies"),
        @URLMapping(id = "copy_index_paged", parentId = "copy_index", viewId = "/faces/copy/index.xhtml",
                pattern = "/page/#{ /[0-9]+/ page : copyControlBean.currentPage}"),
        @URLMapping(id = "my_copies", parentId = "index", viewId = "/faces/copy/index.xhtml",
                pattern = "my/copies"),
        @URLMapping(id = "my_copies_paged", parentId = "my_copies", viewId = "/faces/copy/index.xhtml",
                pattern = "/page/#{ /[0-9]+/ page : copyControlBean.currentPage}"),
})
public class CopyControlBean implements Serializable {

    private static final long serialVersionUID = -4091111635943989599L;

    public static final String BLANK = "";

    @Inject
    private CopyBeanLocal copyBean;

    @Inject
    private Subject currentUser;

    private User user;
    private int currentPage;

    @URLQueryParameter("title")
    protected String filterTitle = "";

    @URLQueryParameter("type")
    private int propertyFilter;

    public String filter() {
        if (!this.filterTitle.isEmpty() && this.filterTitle.length() < 3) {
            this.filterTitle = "";
            FacesContext.getCurrentInstance().addMessage("warning",
                    new FacesMessage(FacesMessage.SEVERITY_WARN, I18NFacesUtils.getLocalizedString("min_title_filter_string"), BLANK));
            return null;
        }

        this.setCurrentPage(1);

        if (this.getUser() == null || this.getUserOid() == this.getCurrentUser().getOid())
            return "pretty:my_copy";
        else
            return "pretty:copy_index";
    }

    public void toogleOwnedCopy(String oid) {
        Book book = this.copyBean.getBookByOid(Long.parseLong(oid));

        if (book == null)
            return;

        Copy c = this.copyBean.getCopyByUserAndBook(this.getCurrentUser(), book);

        try {
            if (c == null || !c.isOwned()) {
                c = this.copyBean.addBookToUser(book, this.getCurrentUser());
                FacesContext.getCurrentInstance().addMessage("info",
                        new FacesMessage(I18NFacesUtils.getLocalizedString("copy_added", book.getTitle())));
            } else {
                c = this.copyBean.removeBookOfUser(book, this.getCurrentUser());
                FacesContext.getCurrentInstance().addMessage("info",
                        new FacesMessage(I18NFacesUtils.getLocalizedString("copy_removed", book.getTitle())));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage("error", new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getLocalizedMessage(), BLANK));
            e.printStackTrace();
        }
    }

    public boolean userHasBook(Book book) {
        if (!this.currentUser.isAuthenticated())
            return false;

        Copy c = this.copyBean.getCopyByUserAndBook(this.getCurrentUser(), book);
        return c != null && c.isOwned();
    }

    public boolean isUserPage() {
        return this.getUserOid() == 0 || this.getUserOid() == this.getCurrentUser().getOid();
    }

    public List<Copy> getCopies() {
        if (this.getUsableUser() == null)
            return new ArrayList<Copy>();

        // TODO Implementar filtro para Copy

        return this.copyBean.getOwnedCopiesByUser(this.getUsableUser());
    }

    public User getCurrentUser() {
        return (User) this.currentUser.getPrincipal();
    }

    public User getUsableUser() {
        if (this.user == null) {
            System.out.println("Entered here...");
            return this.getCurrentUser();
        } else
            return this.getUser();
    }

    public User getUser() {
        return user;
    }

    public void setUserOid(long oid) {
        this.user = this.copyBean.getUserByOid(oid);
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
        // TODO Implementar a parte de paginação para a sessão de cópias
        return true;
    }

    public boolean isLastPage() {
        // TODO Implementar a parte de paginação para a sessão de copias
        return true;
    }

    public int getPropertyFilter() {
        return propertyFilter;
    }

    public void setPropertyFilter(int propertyFilter) {
        this.propertyFilter = propertyFilter;
    }
}
