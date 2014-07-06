package com.sebosystem.control;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import com.sebosystem.dao.Author;
import com.sebosystem.ejb.AuthorBeanLocal;

@ManagedBean(name = "AuthorRequestBean")
@RequestScoped
@URLMappings(mappings = {
        @URLMapping(id = "author_view_inter", pattern = "/author/#{ /[0-9]+/ AuthorRequestBean.authorOid }", viewId = "/faces/author/view.xhtml"),
})
public class AuthorRequestBean {

    @EJB
    protected AuthorBeanLocal authorBean;

    protected String filterName = "";
    protected int currentPage = 1;

    protected Author model;

    protected boolean newModel = false;
    protected int itemsByPage = 10;

    public List<Author> getAuthors() {
        if (this.filterName.isEmpty())
            return this.authorBean.getAllAuthors((this.getCurrentPage() - 1) * this.getItemsByPage(), this.getItemsByPage());
        else
            return this.authorBean.getAuthorsByName(this.filterName, (this.getCurrentPage() - 1) * this.getItemsByPage(), this.getItemsByPage());
    }

    public String save() {
        try {
            this.authorBean.save(this.getModel());
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage("error", new FacesMessage(e.getLocalizedMessage()));
            return null;
        }

        return "pretty:author_view_inter";
    }

    public String remove() {
        try {
            this.authorBean.remove(this.getModel());
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage("error", new FacesMessage(e.getLocalizedMessage()));
            return null;
        }

        return "pretty:author_index";
    }

    public Author getModel() {
        if (this.model == null) {
            this.newModel = true;
            this.model = new Author();
        }

        return model;
    }

    public void setModel(Author model) {
        this.model = model;
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

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName.trim();
    }

    public void setCurrentPage(int page) {
        if (page > 0)
            this.currentPage = page;
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

    public long getTotalPages() {
        if (this.filterName.isEmpty())
            return (long) Math.ceil((float) this.authorBean.getAuthorsTotalRows() / (float) this.getItemsByPage());
        else
            return (long) Math.ceil((float) this.authorBean.getAuthorsByNameTotalRows(this.filterName) / (float) this.getItemsByPage());
    }

    public boolean isFiltered() {
        return this.filterName != null && !this.filterName.isEmpty();
    }

}
