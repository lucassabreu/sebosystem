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
import com.sebosystem.dao.Book;
import com.sebosystem.dao.Review;
import com.sebosystem.dao.User;
import com.sebosystem.ejb.ReviewBeanLocal;
import com.sebosystem.i18n.I18NFacesUtils;

@ManagedBean(name = "reviewControlBean")
@ViewScoped
@URLMappings(mappings = {
        @URLMapping(id = "review_index", parentId = "index", viewId = "/faces/review/index.xhtml",
                pattern = "user/#{/[0-9]+/ oid : reviewControlBean.userOid}/reviews"),
        @URLMapping(id = "review_index_paged", parentId = "review_index", viewId = "/faces/review/index.xhtml",
                pattern = "/page/#{ /[0-9]+/ page : reviewControlBean.currentPage}"),
        @URLMapping(id = "my_reviews", parentId = "index", viewId = "/faces/review/index.xhtml",
                pattern = "my/reviews"),
        @URLMapping(id = "my_reviews_paged", parentId = "my_reviews", viewId = "/faces/review/index.xhtml",
                pattern = "/page/#{ /[0-9]+/ page : reviewControlBean.currentPage}"),
})
public class ReviewControlBean extends AbstractControlBean implements Serializable {

    private static final long serialVersionUID = -4091111635943989599L;

    public static final String BLANK = "";

    @EJB
    private ReviewBeanLocal reviewBean;

    private int currentPage;

    @URLQueryParameter("title")
    protected String filterTitle = "";

    @URLQueryParameter("type")
    private int propertyFilter;

    private User user;

    private Review model;

    private boolean emptyList;

    public String save() {
        // TODO Write the logic of save book in control
        try {
            this.reviewBean.save(this.getModel());
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage("error", new FacesMessage(e.getLocalizedMessage()));
            return null;
        }
        return null;
    }

    public String remove() {
        // TODO Write the logic of save book in control
        try {
            this.reviewBean.remove(this.getModel());
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage("error", new FacesMessage(e.getLocalizedMessage()));
            return null;
        }
        return null;
    }

    public String markAsReported() {
        // TODO Write the logic of save book in control
        try {
            this.reviewBean.report(this.getModel());
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
        return "pretty:my_reviews";
    }

    public boolean isUserPage() {
        return this.getUserOid() == 0 || this.getUserOid() == this.getCurrentUser().getOid();
    }

    public List<Review> getReviews(Book book) {
        List<Review> list;
        
        if (book != null) {
            list = this.reviewBean.getReviewsOfBook(book);
        } else {
            list = new ArrayList<Review>();
        }
        
        emptyList = list.isEmpty();
        return list;
    }

    public List<Review> getReviews() {
        List<Review> list;

        if (this.getUsableUser() == null) {
            list = new ArrayList<Review>();
        } else {
            // TODO Implementar filtro para Review
            list = this.reviewBean.getReviewsOfUser(this.getUsableUser());
        }

        emptyList = list.isEmpty();

        return list;
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
        this.user = this.reviewBean.getUserByOid(oid);
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
        // TODO Implementar a parte de paginação para a sessão de resenhas
        return true;
    }

    public boolean isLastPage() {
        // TODO Implementar a parte de paginação para a sessão de resenhas
        return true;
    }

    public int getPropertyFilter() {
        return propertyFilter;
    }

    public void setPropertyFilter(int propertyFilter) {
        this.propertyFilter = propertyFilter;
    }

    public void setModel(Review model) {
        this.model = model;
    }

    public Review getModel() {
        return model;
    }

    public Review newModel() {
        Review r = new Review();
        r.setPublished(true);
        r.setUser(getCurrentUser());
        return r;
    }

    public Review newModel(Book book) {
        Review r = this.newModel();
        r.setBook(book);
        return r;
    }

    public boolean isEmptyList() {
        return emptyList;
    }
}
