package org.paul.restlearning.dao;

import org.paul.restlearning.model.Account;
import org.paul.restlearning.util.ConnectionUtil;

import java.sql.*;

public class AccountDaoImpl implements IAccountDao{
    @Override
    public Account createAccount(Account account) {
        String sql = "INSERT INTO Account (username, password) VALUES (?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConnectionUtil.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());

            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                return new Account(id, account.getUsername(), account.getPassword());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(ps);
            close(conn);
        }

        return null;
    }

    @Override
    public Account findByUsername(String username) {
        String sql = "SELECT account_id, username, password FROM Account WHERE username = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConnectionUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);

            rs = ps.executeQuery();
            if (rs.next()) {
                return mapRowToAccount(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(ps);
            close(conn);
        }

        return null;
    }

    @Override
    public Account findByUsernameAndPassword(String username, String password) {
        String sql = "SELECT account_id, username, password FROM Account WHERE username = ? AND password = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConnectionUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);

            rs = ps.executeQuery();
            if (rs.next()) {
                return mapRowToAccount(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(ps);
            close(conn);
        }

        return null;
    }

    @Override
    public Account findById(int accountId) {
        String sql = "SELECT account_id, username, password FROM Account WHERE account_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConnectionUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, accountId);

            rs = ps.executeQuery();
            if (rs.next()) {
                return mapRowToAccount(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(ps);
            close(conn);
        }
        return null;
    }

    private Account mapRowToAccount(ResultSet rs) throws SQLException {
        return new Account(
                rs.getInt("account_id"),
                rs.getString("username"),
                rs.getString("password")
        );
    }
    private void close(AutoCloseable ac) {
        if (ac != null) {
            try {
                ac.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
