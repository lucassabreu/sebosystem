package com.sebosystem.control;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.shiro.subject.Subject;

import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import com.ocpsoft.pretty.faces.annotation.URLQueryParameter;
import com.sebosystem.dao.Author;
import com.sebosystem.ejb.AuthorBeanLocal;
import com.sebosystem.i18n.I18NFacesUtils;

@ManagedBean(name = "authorControlBean")
@RequestScoped
@URLMappings(mappings = {
        @URLMapping(id = "author_index", parentId = "index", viewId = "/faces/author/index.xhtml",
                pattern = "author"),
        @URLMapping(id = "author_index_paged", parentId = "author_index", viewId = "/faces/author/index.xhtml",
                pattern = "/page/#{ /[0-9]+/ page : authorControlBean.currentPage}"),
        @URLMapping(id = "author_add", parentId = "author_index", viewId = "/faces/author/edit.xhtml",
                pattern = "/add"),
        @URLMapping(id = "author_view", parentId = "author_index",
                viewId = "/faces/author/view.xhtml",
                pattern = "/#{ /[0-9]+/ oid : authorControlBean.authorOid }"),
        @URLMapping(id = "author_edit", parentId = "author_view", viewId = "/faces/author/edit.xhtml",
                pattern = "/edit")
})
public class AuthorControlBean {

    @EJB
    protected AuthorBeanLocal authorBean;

    @Inject
    private Subject currentUser;

    @URLQueryParameter("name")
    protected String filterName = "";
    protected int currentPage = 1;

    protected Author model;
    protected Author duplicated;

    protected int itemsByPage = 10;

    public String filter() {
        if (!this.filterName.isEmpty() && this.filterName.length() < 3) {
            this.filterName = "";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(I18NFacesUtils.getLocalizedString("min_author_filter_string")));
            return null;
        }

        this.setCurrentPage(1);
        return "pretty:author_index";
    }

    public String save() {
        if (!this.currentUser.isAuthenticated()) {
            FacesContext.getCurrentInstance().addMessage("error", new FacesMessage("You must be logged on to use this function !"));
            return null;
        }

        try {
            this.authorBean.save(this.getModel());
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage("error", new FacesMessage(e.getLocalizedMessage()));
            return null;
        }

        return "pretty:author_view";
    }

    public String remove() {

        if (!this.currentUser.isAuthenticated() || !this.currentUser.hasRole("moderator")) {
            FacesContext.getCurrentInstance().addMessage("error", new FacesMessage("Only Moderators or Administrators can remove a Author !"));
            return null;
        }

        try {
            this.authorBean.remove(this.getModel());
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage("error", new FacesMessage(e.getLocalizedMessage()));
            return null;
        }

        return "pretty:author_index";
    }

    public List<Author> getAuthors() {
        String filterName = this.filterName;

        if (filterName != null) {
            filterName = filterName.trim();

            if (!filterName.isEmpty() && filterName.length() < 3)
                filterName = "";
        }

        if (filterName.isEmpty())
            return this.authorBean.getAllAuthors((this.getCurrentPage() - 1) * this.getItemsByPage(), this.getItemsByPage());
        else
            return this.authorBean.getAuthorsByName(filterName, (this.getCurrentPage() - 1) * this.getItemsByPage(), this.getItemsByPage());
    }

    public Author getModel() {
        if (this.model == null) {
            this.model = new Author();
        }

        return model;
    }

    public void setModel(Author model) {
        this.model = model;
    }

    public void setAuthorOid(long oid) {
        System.out.println("Has been called?");
        this.setModel(this.authorBean.getAuthorByOid(oid));
    }

    public long getAuthorOid() {
        if (this.model == null)
            return 0;
        else
            return this.getModel().getOid();
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName.trim();
    }

    public String getFilterName() {
        return filterName;
    }

    public void setCurrentPage(int page) {
        if (page > 0) {
            if (page > this.getTotalPages())
                this.currentPage = this.getTotalPages();
            else
                this.currentPage = page;
        }
        else
            this.currentPage = 1;
    }

    public int getCurrentPage() {
        if (this.currentPage < 1)
            this.setCurrentPage(1);

        return currentPage;
    }

    public int getItemsByPage() {
        return itemsByPage;
    }

    public void setItemsByPage(int itemsByPage) {
        if (this.itemsByPage < 10)
            itemsByPage = 10;

        this.itemsByPage = itemsByPage;
    }

    public int getTotalPages() {
        if (this.filterName.isEmpty())
            return (int) Math.ceil((float) this.authorBean.getAuthorsTotalRows() / (float) this.getItemsByPage());
        else
            return (int) Math.ceil((float) this.authorBean.getAuthorsByNameTotalRows(this.filterName) / (float) this.getItemsByPage());
    }

    public boolean isFiltered() {
        return this.filterName != null && !this.filterName.isEmpty();
    }

    public Author getDuplicated() {
        return duplicated;
    }

    public void setDuplicated(Author duplicated) {
        this.duplicated = duplicated;
    }

    public boolean isFirstPage() {
        return this.getCurrentPage() == 1;
    }

    public boolean isLastPage() {
        return this.getCurrentPage() == this.getTotalPages();
    }
}
