package com.sebosystem.ejb;

import java.util.List;

import javax.ejb.Local;

import com.sebosystem.dao.Request;

@Local
public interface RequestBeanLocal {

    public Request getRequestByOid(long oid);

    public Request save(Request request) throws Exception;

    public Request remove(Request request);

    public List<Request> getAllRequests();

}
