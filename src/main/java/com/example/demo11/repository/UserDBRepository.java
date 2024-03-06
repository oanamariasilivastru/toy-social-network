package com.example.demo11.repository;


import com.example.demo11.domain.UserFrienshipTuple;
import com.example.demo11.domain.Utilizator;
import com.example.demo11.repository.paging.Page;
import com.example.demo11.repository.paging.PageImplementation;
import com.example.demo11.repository.paging.Pageable;
import com.example.demo11.repository.paging.PagingRepository;
import com.example.demo11.validator.Validator;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
public class UserDBRepository implements  PagingRepository<Long, Utilizator> {
    private String url;

    private String username;

    private String password;

    private Validator<Utilizator> validator;

    public UserDBRepository(String url, String username, String password, Validator<Utilizator> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;

    }


    public Optional<Utilizator> findOne(Long LongID) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from users where id = ?");

        ) {
            statement.setInt(1, Math.toIntExact(LongID));
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String password = resultSet.getString("user_password");
                Utilizator u = new Utilizator(firstName, lastName, password);
                u.setId(LongID);

                String sql1 = "SELECT u.id, u.first_name, u.last_name FROM users u " +
                        "JOIN friendships f ON u.id = f.id2  " +
                        "WHERE (f.id1 = ?)";
                String sql2 = "SELECT u.id, u.first_name, u.last_name FROM users u " +
                        "JOIN friendships f ON u.id = f.id1  " +
                        "WHERE (f.id2 = ?)";

                List<Utilizator> friends = new ArrayList<>();
                loadFriends(u, sql1, friends);
                loadFriends(u, sql2, friends);
                return Optional.ofNullable(u);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();

    }

    public Iterable<Utilizator> findAll() {
        List<Utilizator> userSet = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from users");
             ResultSet resultSet = statement.executeQuery();
        ) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_Name");
                String password = resultSet.getString("user_password");
                Utilizator user = new Utilizator(firstName, lastName, password);
                user.setId(id);
                userSet.add(user);

            }
            String sql1 = "SELECT u.id, u.first_name, u.last_name, u.user_password FROM users u " +
                    "JOIN friendships f ON u.id = f.id2  " +
                    "WHERE (f.id1 = ?)";
            String sql2 = "SELECT u.id, u.first_name, u.last_name, u.user_password FROM users u " +
                    "JOIN friendships f ON u.id = f.id1  " +
                    "WHERE (f.id2 = ?)";
            userSet.forEach(user -> {
                List<Utilizator> friends = new ArrayList<>();
                loadFriends(user, sql1, friends);
                loadFriends(user, sql2, friends);
            });
            return userSet;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Eroare în timpul execuției metodei findAll.", e);
        }
    }

    public Page<Utilizator> findAllPaging(Pageable pageable) {
        List<Utilizator> userSet = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from users limit ? offset ?");

        ) {
            statement.setInt(1, pageable.getPageSize());
            statement.setInt(2, pageable.getPageSize() * (pageable.getPageNumber() - 1));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_Name");
                String password = resultSet.getString("user_password");
                Utilizator user = new Utilizator(firstName, lastName, password);
                user.setId(id);
                userSet.add(user);
            }

            String sql1 = "SELECT u.id, u.first_name, u.last_name, u.user_password FROM users u " +
                    "JOIN friendships f ON u.id = f.id2  " +
                    "WHERE (f.id1 = ?)";
            String sql2 = "SELECT u.id, u.first_name, u.last_name, u.user_password FROM users u " +
                    "JOIN friendships f ON u.id = f.id1  " +
                    "WHERE (f.id2 = ?)";

            userSet.forEach(user -> {
                System.out.println(user);
                List<Utilizator> friends = new ArrayList<>();
                loadFriends(user, sql1, friends);
                loadFriends(user, sql2, friends);
            });
            //return userSet;
            return new PageImplementation<Utilizator>(pageable, userSet.stream());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Eroare în timpul execuției metodei findAll.", e);
        }
    }

    public Page<Utilizator> findOthersPaging(Pageable pageable, Long Id) {
        List<Utilizator> userSet = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users " +
                     "WHERE id <> ? AND id NOT IN (" +
                     "    SELECT id2 FROM friendships WHERE id1 = ? " +
                     "    UNION " +
                     "    SELECT id1 FROM friendships WHERE id2 = ?" +
                     ") " +
                     "LIMIT ? OFFSET ?");

        ) {
            statement.setInt(1, Math.toIntExact(Id));
            statement.setInt(2, Math.toIntExact(Id));
            statement.setInt(3, Math.toIntExact(Id));
            statement.setInt(4, pageable.getPageSize());
            statement.setInt(5, pageable.getPageSize() * (pageable.getPageNumber() - 1));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_Name");
                String password = resultSet.getString("user_password");
                Utilizator user = new Utilizator(firstName, lastName, password);
                user.setId(id);
                userSet.add(user);

            }
            String sql1 = "SELECT u.id, u.first_name, u.last_name, u.user_password FROM users u " +
                    "JOIN friendships f ON u.id = f.id2  " +
                    "WHERE (f.id1 = ?)";
            String sql2 = "SELECT u.id, u.first_name, u.last_name, u.user_password FROM users u " +
                    "JOIN friendships f ON u.id = f.id1  " +
                    "WHERE (f.id2 = ?)";
            userSet.forEach(user -> {
                List<Utilizator> friends = new ArrayList<>();
                loadFriends(user, sql1, friends);
                loadFriends(user, sql2, friends);
            });
            //return userSet;
            return new PageImplementation<Utilizator>(pageable, userSet.stream());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Eroare în timpul execuției metodei findAll.", e);
        }
    }

    public Optional<Utilizator> save(Utilizator entity) {
        validator.validate(entity);

        String sql = "insert into users (id, first_name, last_name, user_password) values(?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement Statement = connection.prepareStatement(sql);) {
            Statement.setInt(1, Math.toIntExact(entity.getId()));
            Statement.setString(2, entity.getFirstName());
            Statement.setString(3, entity.getLastName());
            Statement.setString(4, entity.getPassword());

            int response = Statement.executeUpdate();
            return response == 0 ? Optional.empty() : Optional.of(entity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public Optional<Utilizator> delete(Long LongID) {
        String sql = "delete from users WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement Statement = connection.prepareStatement(sql);) {
            Statement.setInt(1, Math.toIntExact(LongID));
            Utilizator user = findOne(LongID).orElse(null);
            int response = Statement.executeUpdate();

            return response == 0 ? Optional.empty() : Optional.of(user);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public Optional<Utilizator> update(Utilizator entity) {
        String sql = "update users SET first_name = ?, last_name = ? where id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement Statement = connection.prepareStatement(sql);) {
            Statement.setString(1, entity.getFirstName());
            Statement.setString(2, entity.getLastName());
            Statement.setInt(3, Math.toIntExact(entity.getId()));

            int response = Statement.executeUpdate();

            return response == 0 ? Optional.of(entity) : Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Utilizator> loadFriends(Utilizator u, String sql, List<Utilizator> friends) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, Math.toIntExact(u.getId()));
            //statement.setInt(2, Math.toIntExact(u.getId()));

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Utilizator utilizator = new Utilizator(resultSet.getString("first_name"), resultSet.getString("last_name"), resultSet.getString("user_password"));
                utilizator.setId(resultSet.getLong("id"));
                System.out.println(utilizator);
                friends.add(utilizator);
            }
            u.setFriends(friends);
            System.out.println(u.getFriends());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public boolean existaUser(Long id) {
        Iterable<Utilizator> users = findAll();
        return StreamSupport.stream(users.spliterator(), false)
                .anyMatch(user -> user.getId().equals(id));

    }

    public List<UserFrienshipTuple> getFrienshipsforUser(Long id, String date) {
        String sql = "SELECT u.id, u.first_name, u.last_name, f.data FROM users u " +
                "JOIN friendships f ON (u.id = f.id2 OR u.id = f.id1) " +
                "WHERE ((f.id1 = ? OR f.id2 = ?) AND EXTRACT(MONTH FROM f.data) = ?::integer)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, Math.toIntExact(id));
            statement.setInt(2, Math.toIntExact(id));
            statement.setString(3, date);

            ResultSet resultSet = statement.executeQuery();
            List<UserFrienshipTuple> result = new ArrayList<>();
            while (resultSet.next()) {
                Long iduser = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                Date data = resultSet.getDate("data");
                if (iduser != id) {
                    UserFrienshipTuple userFriendshipTuple = new UserFrienshipTuple(firstName, lastName, data);
                    result.add(userFriendshipTuple);
                }

            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Utilizator findOneByFirstNameAndPassword(String first_name, String password1) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from users where first_name = ? and user_password = ?");

        ) {
            statement.setString(1, first_name);
            statement.setString(2, password1);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String password2 = resultSet.getString("user_password");
                Utilizator u = new Utilizator(firstName, lastName, password1);
                u.setId(id);

                String sql1 = "SELECT u.id, u.first_name, u.last_name FROM users u " +
                        "JOIN friendships f ON u.id = f.id2  " +
                        "WHERE (f.id1 = ?)";
                String sql2 = "SELECT u.id, u.first_name, u.last_name FROM users u " +
                        "JOIN friendships f ON u.id = f.id1  " +
                        "WHERE (f.id2 = ?)";

                List<Utilizator> friends = new ArrayList<>();
                loadFriends(u, sql1, friends);
                loadFriends(u, sql2, friends);
                return u;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
