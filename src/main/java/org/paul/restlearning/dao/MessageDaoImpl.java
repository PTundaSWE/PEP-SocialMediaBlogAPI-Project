package org.paul.restlearning.dao;

import org.paul.restlearning.model.Message;
import org.paul.restlearning.util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDaoImpl implements IMessageDao{
    @Override
    public Message createMessage(Message message) {
        String sql = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConnectionUtil.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());

            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                return new Message(
                        id,
                        message.getPosted_by(),
                        message.getMessage_text(),
                        message.getTime_posted_epoch()
                );
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
    public List<Message> findAllMessages() {
        String sql = "SELECT * FROM Message";
        List<Message> messages = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConnectionUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                messages.add(mapRowToMessage(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(ps);
            close(conn);
        }

        return messages;
    }

    @Override
    public Message findMessageById(int messageId) {
        String sql = "SELECT * FROM Message WHERE message_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConnectionUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, messageId);

            rs = ps.executeQuery();
            if (rs.next()) {
                return mapRowToMessage(rs);
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
    public Message deleteMessageById(int messageId) {
        Message existing = findMessageById(messageId);
        if (existing == null) {
            return null;
        }

        String sql = "DELETE FROM Message WHERE message_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConnectionUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, messageId);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(ps);
            close(conn);
        }

        return existing;
    }

    @Override
    public Message updateMessageText(int messageId, String newMessageText) {
        Message existing = findMessageById(messageId);
        if (existing == null) {
            return null;
        }

        String sql = "UPDATE Message SET message_text = ? WHERE message_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConnectionUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, newMessageText);
            ps.setInt(2, messageId);
            ps.executeUpdate();

            existing.setMessage_text(newMessageText);
            return existing;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(ps);
            close(conn);
        }

        return null;
    }

    @Override
    public List<Message> findMessagesByAccountId(int accountId) {
        String sql = "SELECT * FROM Message WHERE posted_by = ?";
        List<Message> messages = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConnectionUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, accountId);

            rs = ps.executeQuery();
            while (rs.next()) {
                messages.add(mapRowToMessage(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(ps);
            close(conn);
        }

        return messages;
    }

    private Message mapRowToMessage(ResultSet rs) throws SQLException {
        return new Message(
                rs.getInt("message_id"),
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch")
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
