package com.sebosystem.control;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.inject.Inject;

import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import com.sebosystem.dao.Request;
import com.sebosystem.ejb.RequestBeanLocal;

@ManagedBean(name = "RequestRequestBean")
@URLMappings(mappings = {
        @URLMapping(id = "my_requests", pattern = "/my/requests", viewId = "/faces/request/index.xhtml"),
        @URLMapping(id = "request_index", pattern = "/requests", viewId = "/faces/request/index.xhtml"),
})
public class RequestRequestBean implements Serializable {

    private static final long serialVersionUID = 5932467761709478063L;

    @Inject
    private RequestBeanLocal requestBean;

    public List<Request> getRequests() {
        return this.requestBean.getAllRequests();
    }
}
