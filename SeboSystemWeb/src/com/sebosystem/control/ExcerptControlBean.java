package com.sebosystem.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import com.ocpsoft.pretty.faces.annotation.URLQueryParameter;
import com.sebosystem.control.base.AbstractControlBean;
import com.sebosystem.dao.Book;
import com.sebosystem.dao.Excerpt;
import com.sebosystem.dao.User;
import com.sebosystem.ejb.ExcerptBeanLocal;
import com.sebosystem.i18n.I18NFacesUtils;

@ManagedBean(name = "excerptControlBean")
@ViewScoped
@URLMappings(mappings = {
        @URLMapping(id = "excerpt_index", parentId = "index", viewId = "/faces/excerpt/index.xhtml",
                pattern = "user/#{/[0-9]+/ oid : excerptControlBean.userOid}/excerpts"),
        @URLMapping(id = "excerpt_index_paged", parentId = "excerpt_index", viewId = "/faces/excerpt/index.xhtml",
                pattern = "/page/#{ /[0-9]+/ page : excerptControlBean.currentPage}"),
        @URLMapping(id = "my_excerpts", parentId = "index", viewId = "/faces/excerpt/index.xhtml",
                pattern = "my/excerpts"),
        @URLMapping(id = "my_excerpts_paged", parentId = "my_excerpts", viewId = "/faces/excerpt/index.xhtml",
                pattern = "/page/#{ /[0-9]+/ page : excerptControlBean.currentPage}"),
})
public class ExcerptControlBean extends AbstractControlBean implements Serializable {

    private static final long serialVersionUID = -4091111635943989599L;

    public static final String BLANK = "";

    @Inject
    private ExcerptBeanLocal excerptBean;

    /*@Inject
    private Subject currentUser;*/

    private int currentPage;

    @URLQueryParameter("title")
    protected String filterTitle = "";

    @URLQueryParameter("type")
    private int propertyFilter;

    private User user;

    private Excerpt model;

    public String save() {
        // TODO Write the logic of save book in control
        try {
            this.excerptBean.save(this.getModel());
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage("error", new FacesMessage(e.getLocalizedMessage()));
            return null;
        }
        return null;
    }

    public String remove() {
        // TODO Write the logic of save book in control
        try {
            this.excerptBean.remove(this.getModel());
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage("error", new FacesMessage(e.getLocalizedMessage()));
            return null;
        }
        return null;
    }

    public String markAsReported() {
        // TODO Write the logic of save book in control
        try {
            this.excerptBean.report(this.getModel());
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage("error", new FacesMessage(e.getLocalizedMessage()));
            return null;
        }
        return null;
    }

    public String filter() {
        if (!this.filterTitle.isEmpty() && this.filterTitle.length() < 3) {
            this.filterTitle = "";
            FacesContext.getCurrentInstance().addMessage("warning",
                    new FacesMessage(FacesMessage.SEVERITY_WARN, I18NFacesUtils.getLocalizedString("min_title_filter_string"), BLANK));
            return null;
        }

        this.setCurrentPage(1);
        return "pretty:my_excerpts";
    }

    public boolean isUserPage() {
        return this.getUserOid() == 0 || this.getUserOid() == this.getCurrentUser().getOid();
    }

    public List<Excerpt> getExcerpts(Book book) {
        if (book != null)
            return this.excerptBean.getExcerptsOfBook(book);
        else
            return new ArrayList<Excerpt>();
    }

    public List<Excerpt> getExcerpts() {
        if (this.getUsableUser() == null)
            return new ArrayList<Excerpt>();

        // TODO Implementar filtro para excerpt
        return this.excerptBean.getExcerptsOfUser(this.getUsableUser());
    }

    public Excerpt newModel() {
        Excerpt e = new Excerpt();
        e.setPublished(true);
        e.setUser(getCurrentUser());
        return e;
    }

    public Excerpt newModel(Book book) {
        Excerpt e = this.newModel();
        e.setBook(book);
        return e;
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
        this.user = this.excerptBean.getUserByOid(oid);
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

    public void setModel(Excerpt model) {
        this.model = model;
    }

    public Excerpt getModel() {
        return model;
    }

}
