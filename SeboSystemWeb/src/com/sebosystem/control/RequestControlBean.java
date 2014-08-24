package com.sebosystem.control;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.inject.Inject;

import org.apache.shiro.subject.Subject;

import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import com.sebosystem.dao.Request;
import com.sebosystem.dao.User;
import com.sebosystem.ejb.RequestBeanLocal;

@ManagedBean(name = "requestControlBean")
@URLMappings(mappings = {
        @URLMapping(id = "my_request", parentId = "index", viewId = "/faces/request/index.xhtml",
                pattern = "my/requests"),
        @URLMapping(id = "my_request_index_paged", parentId = "my_request", viewId = "/faces/request/index.xhtml",
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
public class RequestControlBean implements Serializable {

    private static final long serialVersionUID = 5932467761709478063L;

    @Inject
    private RequestBeanLocal requestBean;

    @Inject
    private Subject currentUser;

    private int currentPage;

    private User user;

    private Request model;

    public String takeOn() {
        System.out.println("Teste01: " + (this.model == null));

        return "pretty:request_edit";
    }

    public List<Request> getRequests() {
        return this.requestBean.getAllRequests();
    }

    public boolean isUserPage() {
        return this.getUserOid() == 0 || this.getUserOid() == this.getCurrentUser().getOid();
    }

    public User getCurrentUser() {
        return (User) this.currentUser.getPrincipal();
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

    public void setRequestOid(long oid) {
        if (oid != 0)
            this.setModel(this.requestBean.getRequestByOid(oid));
    }

    public long getRequestOid() {
        return this.model == null ? 0 : this.model.getOid();
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
        // TODO Implementar controles de filtro
        return false;
    }

    public boolean isFirstPage() {
        // TODO Implementar a parte de paginação para a sessão de cópias
        return true;
    }

    public boolean isLastPage() {
        // TODO Implementar a parte de paginação para a sessão de copias
        return true;
    }

    public Request getModel() {
        return model;
    }

    public void setModel(Request model) {
        System.out.println(model);
        this.model = model;
    }
}
