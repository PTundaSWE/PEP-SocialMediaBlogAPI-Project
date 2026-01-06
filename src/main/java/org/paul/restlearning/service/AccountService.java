package org.paul.restlearning.service;

import org.paul.restlearning.dao.IAccountDao;
import org.paul.restlearning.model.Account;

public class AccountService {
    private final IAccountDao accountDao;

    public AccountService(IAccountDao accountDao) {
        this.accountDao = accountDao;
    }

    /**
     * Requirement #1: Register
     * Successful iff:
     * - username is not blank
     * - password length >= 4
     * - username is unique
     *
     * @param newAccount account from request body (no account_id)
     * @return persisted account with generated account_id, or null if invalid
     */
    public Account register(Account newAccount) {
        if (newAccount == null) return null;

        String username = safeTrim(newAccount.getUsername());
        String password = newAccount.getPassword(); // do not trim passwords

        if (username == null || username.isEmpty()) return null;
        if (password == null || password.length() < 4) return null;

        // username must be unique
        Account existing = accountDao.findByUsername(username);
        if (existing != null) return null;

        // persist (use trimmed username to avoid " user " duplicates)
        newAccount.setUsername(username);
        return accountDao.createAccount(newAccount);
    }

    /**
     * Requirement #2: Login
     * Successful iff username/password match an existing account.
     *
     * @param credentials account from request body (no account_id)
     * @return account (with account_id) if valid, otherwise null
     */
    public Account login(Account credentials) {
        if (credentials == null) return null;

        String username = safeTrim(credentials.getUsername());
        String password = credentials.getPassword();

        if (username == null || username.isEmpty()) return null;
        if (password == null) return null;

        return accountDao.findByUsernameAndPassword(username, password);
    }

    /**
     * Helper for Message story (#3): posted_by must refer to a real, existing user.
     *
     * @param accountId posted_by value
     * @return true if account exists
     */
    public boolean accountExists(int accountId) {
        return accountDao.findById(accountId) != null;
    }

    private String safeTrim(String s) {
        return s == null ? null : s.trim();
    }
}
