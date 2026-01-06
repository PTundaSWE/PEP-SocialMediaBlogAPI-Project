package org.paul.restlearning.dao;

import org.paul.restlearning.model.Account;

public interface IAccountDao {
    Account createAccount(Account account);
    Account findByUsername(String username);
    Account findByUsernameAndPassword(String username, String password);
    Account findById(int accountId);
}
