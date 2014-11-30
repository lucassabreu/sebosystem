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
import com.sebosystem.control.base.AbstractControlBean;
import com.sebosystem.dao.Book;
import com.sebosystem.dao.Transaction;
import com.sebosystem.dao.TransactionStatus;
import com.sebosystem.dao.User;
import com.sebosystem.ejb.TransactionBeanLocal;

@ManagedBean(name = "transactionControlBean")
@ViewScoped
@URLMappings(mappings = {
        @URLMapping(id = "my_transactions", parentId = "index", viewId = "/faces/transaction/index.xhtml",
                pattern = "my/transactions"),
        @URLMapping(id = "my_transactions_paged", parentId = "my_transactions", viewId = "/faces/transaction/index.xhtml",
                pattern = "/page/#{ /[0-9]+/ page : transactionControlBean.currentPage}"),
        @URLMapping(id = "transaction_view", parentId = "index", viewId = "/faces/transaction/view.xhtml",
                pattern = "trans/#{ /[0-9]+/ oid : transactionControlBean.transactionOid }"),
        @URLMapping(id = "transaction_edit", parentId = "transaction_view", viewId = "/faces/transaction/edit.xhtml",
                pattern = "/edit"),
})
// TODO revisar a lógica de impressão no my_transactions
public class TransactionControlBean extends AbstractControlBean implements Serializable {

    private static final long serialVersionUID = -4091111635943989599L;

    @EJB
    private TransactionBeanLocal transactionBean;

    private User user;
    private int currentPage;

    private int propertyFilter = 1;

    private Transaction model;

    private Book book;

    public String startSellTrade() {

        this.model = new Transaction();
        this.model.setStatus(TransactionStatus.Open);

        if (this.getBook() == null) {
            this.addLocalizedFacesMessage("error", FacesMessage.SEVERITY_ERROR, "transaction_book_required");
            return null;
        }

        this.addNewBook();

        if (this.save() == null) {
            return null;
        }

        return "pretty:transaction_edit";
    }

    public String save() {

        try {
            this.model = this.transactionBean.save(this.getModel());
        } catch (Exception e) {
            this.addExceptionToFacesMessage("error", FacesMessage.SEVERITY_ERROR, e);
            return null;
        }

        return "pretty:transaction_view";

    }

    public String addNewBook() {

        if (this.getBook() == null) {
            this.addLocalizedFacesMessage("error", FacesMessage.SEVERITY_ERROR, "transaction_book_required");
            return null;
        }

        return null;
    }

    public String filter() {

        this.setCurrentPage(1);

        return "pretty:my_transaction";
    }

    public List<Transaction> getTransactions() {
        if (this.getUsableUser() == null)
            return new ArrayList<Transaction>();

        // TODO Implementar filtro para transaction

        return this.transactionBean.getTransactionsByUser(this.getUsableUser());
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
        this.user = this.transactionBean.getUserByOid(oid);
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

    public boolean isFirstPage() {
        // TODO Implementar a parte de paginação para a sessão de transações
        return true;
    }

    public boolean isLastPage() {
        // TODO Implementar a parte de paginação para a sessão de transações
        return true;
    }

    public void setTransactionOid(String oid) {
        this.setTransactionOid(Long.parseLong(oid));
    }

    public void setTransactionOid(long oid) {
        if (oid != 0)
            this.setModel(this.transactionBean.getTransactionByOid(oid));
    }

    public long getTransactionOid() {
        if (this.getModel() == null)
            return 0;
        else
            return this.getModel().getOid();
    }

    public int getPropertyFilter() {
        return propertyFilter;
    }

    public void setPropertyFilter(int propertyFilter) {
        this.propertyFilter = propertyFilter;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Transaction getModel() {
        return model;
    }

    public void setModel(Transaction model) {
        this.model = model;
    }
}
