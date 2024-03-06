package com.example.demo11.repository;

import com.example.demo11.MessageController;
import com.example.demo11.domain.Invite;
import com.example.demo11.domain.Message;
import com.example.demo11.domain.Utilizator;
import com.example.demo11.repository.paging.Page;
import com.example.demo11.repository.paging.PageImplementation;
import com.example.demo11.repository.paging.Pageable;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MessageDBRepository {
    private String url;
    private String username;

    private String password;

    public UserDBRepository userDateBase;

    public MessageDBRepository(String url, String username, String password, UserDBRepository userDateBase) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.userDateBase = userDateBase;
    }


    public Message save(Message message) {
        String sql = "INSERT INTO messages(id_from, id_to, message, data, reply_message) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, message.getFrom().getId());

            // Setează id-urile destinatarilor
            String toUserIds = message.getTo().stream()
                    .map(utilizator -> String.valueOf(utilizator.getId()))
                    .collect(Collectors.joining(" "));
            statement.setString(2, toUserIds);

            statement.setString(3, message.getMessage());
            statement.setTimestamp(4, Timestamp.valueOf(message.getData()));

            if (message.getReplyMessage() != null) {
                statement.setInt(5, Math.toIntExact(message.getReplyMessage().getId()));
            } else {
                statement.setNull(5, Types.INTEGER);
            }

            int response = statement.executeUpdate();

            if (response == 0) {
                return message;
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    message.setId(generatedKeys.getLong(1));
                    System.out.println(message.getId());
                } else {
                    throw new SQLException("Crearea mesajului a eșuat, nu s-a obținut niciun ID.");
                }
            }

            return message;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public List<Message> findAll() {
        List<Message> messagesList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM messages");
             ResultSet messagesSet = statement.executeQuery();
        ) {
            while (messagesSet.next()) {
                List<Utilizator> users = new ArrayList<>(); // Move inside the loop

                Long id_from = messagesSet.getLong("id_from");
                Optional<Utilizator> u = userDateBase.findOne(id_from);

                if (u.isPresent()) {
                    String toUsers = messagesSet.getString("id_to");
                    String message = messagesSet.getString("message");
                    Timestamp timestamp = messagesSet.getTimestamp("data");
                    LocalDateTime localDateTime = timestamp.toLocalDateTime();
                    Long messageId = messagesSet.getLong("id");
                    Long messageReplyId = messagesSet.getLong("reply_message");


                    String[] toUsersIds = toUsers.split(" ");
                    for (String userId : toUsersIds) {
                        try {
                            Long id = Long.parseLong(userId);
                            Optional<Utilizator> userTo = userDateBase.findOne(id);
                            userTo.ifPresent(users::add);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    Message messageReply = findMessageById(messageReplyId);
                    Message message1 = new Message(u.get(), users, message, localDateTime, messageReply);
                    message1.setId(messageId);
                    messagesList.add(message1);
                }
            }

            return messagesList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Message> findAllFromIds(Long userId1, Long userId2) {
        List<Message> messagesList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM messages WHERE (id_from = ? AND id_to LIKE ?) OR (id_from = ? AND id_to LIKE ?)");
        ) {
            statement.setLong(1, userId1);
            statement.setString(2, "%" + userId2 + "%");
            statement.setLong(3, userId2);
            statement.setString(4, "%" + userId1 + "%");

            try (ResultSet messagesSet = statement.executeQuery()) {
                while (messagesSet.next()) {
                    Long id_from = messagesSet.getLong("id_from");
                    Optional<Utilizator> u = userDateBase.findOne(id_from);

                    if (u.isPresent()) {
                        List<Utilizator> users = new ArrayList<>();

                        String toUsers = messagesSet.getString("id_to");
                        String message = messagesSet.getString("message");
                        Timestamp timestamp = messagesSet.getTimestamp("data");
                        LocalDateTime localDateTime = timestamp.toLocalDateTime();
                        Long messageId = messagesSet.getLong("id");
                        Long messageReplyId = messagesSet.getLong("reply_message");

                        String[] toUsersIds = toUsers.split(" ");
                        for (String userId : toUsersIds) {
                            try {
                                Long id = Long.parseLong(userId);
                                Optional<Utilizator> userTo = userDateBase.findOne(id);
                                userTo.ifPresent(users::add);
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }

                        Message messageReply = findMessageById(messageReplyId);
                        Message message1 = new Message(u.get(), users, message, localDateTime, messageReply);
                        message1.setId(messageId);
                        System.out.println(message1);
                        messagesList.add(message1);
                    }
                }
            }

            return messagesList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Page<Message> findAllFromIdsPaging(Long userId1, Long userId2, Pageable pageable) {
        List<Message> messagesList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM messages WHERE (id_from = ? AND id_to LIKE ?) OR (id_from = ? AND id_to LIKE ?) limit ? offset ?");
        ) {
            statement.setLong(1, userId1);
            statement.setString(2, "%" + userId2 + "%");
            statement.setLong(3, userId2);
            statement.setString(4, "%" + userId1 + "%");
            statement.setInt(5, pageable.getPageSize());
            statement.setInt(6, pageable.getPageSize() * (pageable.getPageNumber() - 1));
            try (ResultSet messagesSet = statement.executeQuery()) {
                while (messagesSet.next()) {
                    Long id_from = messagesSet.getLong("id_from");
                    Optional<Utilizator> u = userDateBase.findOne(id_from);

                    if (u.isPresent()) {
                        List<Utilizator> users = new ArrayList<>();

                        String toUsers = messagesSet.getString("id_to");
                        String message = messagesSet.getString("message");
                        Timestamp timestamp = messagesSet.getTimestamp("data");
                        LocalDateTime localDateTime = timestamp.toLocalDateTime();
                        Long messageId = messagesSet.getLong("id");
                        Long messageReplyId = messagesSet.getLong("reply_message");

                        String[] toUsersIds = toUsers.split(" ");
                        for (String userId : toUsersIds) {
                            try {
                                Long id = Long.parseLong(userId);
                                Optional<Utilizator> userTo = userDateBase.findOne(id);
                                userTo.ifPresent(users::add);
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }

                        Message messageReply = findMessageById(messageReplyId);
                        Message message1 = new Message(u.get(), users, message, localDateTime, messageReply);
                        message1.setId(messageId);
                        //System.out.println(message1);
                        messagesList.add(message1);
                    }
                }
            }

            //return messagesList;
            return new PageImplementation<Message>(pageable, messagesList.stream());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public Message findMessageByUserIdsAndText(Long senderId, Long recipientId, String messageText) {
        String sql = "SELECT * FROM messages WHERE id_from = ? AND id_to = ? AND message = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, senderId);
            statement.setString(2, recipientId.toString()); // Convertim ID-ul destinatarului la șir de caractere
            statement.setString(3, messageText);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    List<Utilizator> users = new ArrayList<>();

                    Long id_from = resultSet.getLong("id_from");
                    Optional<Utilizator> sender = userDateBase.findOne(id_from);

                    if (sender.isPresent()) {
                        String toUsers = resultSet.getString("id_to");
                        Timestamp timestamp = resultSet.getTimestamp("data");
                        LocalDateTime localDateTime = timestamp.toLocalDateTime();

                        String[] toUsersIds = toUsers.split(" ");
                        for (String userId : toUsersIds) {
                            try {
                                Long id = Long.parseLong(userId);
                                Optional<Utilizator> userTo = userDateBase.findOne(id);
                                userTo.ifPresent(users::add);
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                        Message message = new Message(sender.get(), users, messageText, localDateTime, null);
                        message.setId(resultSet.getLong("id"));
                        return message;
                    }
                }
            }

            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Message findMessageById(Long messageId) {
        String sql = "SELECT * FROM messages WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, messageId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    List<Utilizator> users = new ArrayList<>();

                    Long id_from = resultSet.getLong("id_from");
                    Optional<Utilizator> sender = userDateBase.findOne(id_from);

                    if (sender.isPresent()) {
                        String toUsers = resultSet.getString("id_to");
                        Timestamp timestamp = resultSet.getTimestamp("data");
                        LocalDateTime localDateTime = timestamp.toLocalDateTime();

                        String[] toUsersIds = toUsers.split(" ");
                        for (String userId : toUsersIds) {
                            try {
                                Long id = Long.parseLong(userId);
                                Optional<Utilizator> userTo = userDateBase.findOne(id);
                                userTo.ifPresent(users::add);
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                        Message message = new Message(sender.get(), users, resultSet.getString("message"), localDateTime, null);
                        message.setId(resultSet.getLong("id"));
                        return message;
                    }
                }
            }

            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

