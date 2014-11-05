package com.sebosystem.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
import com.sebosystem.ejb.AuthorBeanLocal;
import com.sebosystem.i18n.I18NFacesUtils;

@ManagedBean(name = "authorControlBean")
@ViewScoped
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
public class AuthorControlBean extends AbstractControlBean implements Serializable {

    private static final long serialVersionUID = -8343262156418728493L;

    @EJB
    protected AuthorBeanLocal authorBean;

    @URLQueryParameter("name")
    protected String filterName = "";
    protected int currentPage = 1;

    protected Author model;

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
        if (!this.isAuthenticated()) {
            this.addFacesMessage("error", FacesMessage.SEVERITY_ERROR, "You must be logged on to use this function !");
            return null;
        }

        try {
            this.authorBean.save(this.getModel());
        } catch (Exception e) {
            this.addFacesMessage("error", FacesMessage.SEVERITY_ERROR, e.getLocalizedMessage());
            return null;
        }

        return "pretty:author_view";
    }

    public String remove() {

        if (!this.isAuthenticated() || !this.hasRole("moderator")) {
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

    public String markAsDuplicated() {
        // TODO Write the content of Mark As Duplicated method

        try {
            this.authorBean.reportDuplicated(this.model);

            this.addFacesMessage("info", FacesMessage.SEVERITY_INFO, "report_author_duplicated_sucess");

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage("error", new FacesMessage(e.getLocalizedMessage()));
        }

        return null;
    }

    public List<Book> getBooksOfAuthor() {
        if (this.model == null)
            return new ArrayList<Book>();
        else {
            return this.authorBean.getBooksOfAuthor(this.model);
        }
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

    /**
     * Return if the parameter <code>author</code> is the same of the
     * <code>model</code> attribute
     * 
     * @param author
     * @return
     * 
     * @see #model
     */
    public boolean isModel(Author author) {
        if (this.model == null)
            return false;

        return this.model.getOid() == author.getOid();
    }

    public void setAuthorOid(String oid) {
        this.setModel(this.authorBean.getAuthorByOid(Long.parseLong(oid)));
    }

    public void setAuthorOid(long oid) {
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

    public boolean isFirstPage() {
        return this.getCurrentPage() == 1;
    }

    public boolean isLastPage() {
        return this.getCurrentPage() >= this.getTotalPages();
    }
}
