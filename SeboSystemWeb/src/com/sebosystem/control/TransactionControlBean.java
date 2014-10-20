package com.sebosystem.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import com.sebosystem.control.base.AbstractControlBean;
import com.sebosystem.dao.Transaction;
import com.sebosystem.dao.User;
import com.sebosystem.ejb.TransactionBeanLocal;

@ManagedBean(name = "transactionControlBean")
@ViewScoped
@URLMappings(mappings = {
        @URLMapping(id = "my_transactions", parentId = "index", viewId = "/faces/transaction/index.xhtml",
                pattern = "my/transactions"),
        @URLMapping(id = "my_transactions_paged", parentId = "my_transactions", viewId = "/faces/transaction/index.xhtml",
                pattern = "/page/#{ /[0-9]+/ page : transactionControlBean.currentPage}"),
})
// TODO revisar a lógica de impressão no my_transactions
public class TransactionControlBean extends AbstractControlBean implements Serializable {

    private static final long serialVersionUID = -4091111635943989599L;

    public static final String BLANK = "";

    @EJB
    private TransactionBeanLocal transactionBean;

    private User user;
    private int currentPage;

    private int propertyFilter = 1;

    public String filter() {

        this.setCurrentPage(1);

        if (this.getUser() == null || this.getUserOid() == this.getCurrentUser().getOid())
            return "pretty:my_transaction";
        else
            return "pretty:transaction_index";
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

    public int getPropertyFilter() {
        return propertyFilter;
    }

    public void setPropertyFilter(int propertyFilter) {
        this.propertyFilter = propertyFilter;
    }
}
