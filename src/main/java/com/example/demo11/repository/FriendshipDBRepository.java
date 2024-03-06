package com.example.demo11.repository;
import com.example.demo11.domain.Prietenie;
import com.example.demo11.domain.Utilizator;
import com.example.demo11.repository.paging.Page;
import com.example.demo11.repository.paging.PageImplementation;
import com.example.demo11.repository.paging.Pageable;
import com.example.demo11.repository.paging.PagingRepository;
import com.example.demo11.validator.PrietenieValidator;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FriendshipDBRepository {
    private String url;

    private String username;

    private String password;

    private PrietenieValidator validator;

    public FriendshipDBRepository(String url, String username, String password, PrietenieValidator validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    public Prietenie save(Prietenie frienship) throws RepositoryException{
        if(frienship.getFriend1() > frienship.getFriend2())
        {
            Long aux = frienship.getFriend1();
            frienship.setFriend1(frienship.getFriend2());
            frienship.setFriend2(aux);
        }
        validator.validate(frienship.getFriend1(), frienship.getFriend2(), frienship.getDate());
        if(existaPrietenie(frienship.getFriend1(), frienship.getFriend2()))
            throw new RepositoryException("Prietenia intre acesti utilizatori deja exita");

        String sql = "insert into friendships(id1, id2, data) values(?, ?, ?)";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(sql);)
        {
            System.out.println(frienship.getFriend1());
            System.out.println(frienship.getFriend2());
            statement.setInt(1, Math.toIntExact(frienship.getFriend1()));
            statement.setInt(2, Math.toIntExact(frienship.getFriend2()));
            statement.setDate(3, Date.valueOf(frienship.getDate()));

            int response = statement.executeUpdate();

            return response == 0 ? frienship : null;
        } catch(SQLException e){
            throw new RuntimeException(e);
        }

    }

    public Prietenie delete(Prietenie frienship){
        String sql = "delete from friendships WHERE id1 = ? and id2 = ?";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement Statement = connection.prepareStatement(sql);)
        {
            Statement.setInt(1, Math.toIntExact(frienship.getFriend1()));
            Statement.setInt(2, Math.toIntExact(frienship.getFriend2()));

            int response = Statement.executeUpdate();

            return response == 0 ? frienship : null;

        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    public Prietenie findOne(Long id1, Long id2){
        if(!existaPrietenie(id1, id2))
            throw new IllegalArgumentException("Nu exista prietenie intre acesti utilizatori!");
        String sql = "select * from friendships where id1 = ? and id2 = ?";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(sql);
        ){
            statement.setInt(1, Math.toIntExact(id1));
            statement.setInt(2, Math.toIntExact(id2));
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                LocalDate data = resultSet.getDate("data").toLocalDate();
                Prietenie f = new Prietenie(id1, id2, data);
                return f;
            }

        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }
    public List<Prietenie> findAll(){
        List<Prietenie> friendships = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select * from friendships");
            ResultSet friendshipsSet = statement.executeQuery();
        ){
            while (friendshipsSet.next()){
                Long id1 = friendshipsSet.getLong("id1");
                Long id2 = friendshipsSet.getLong("id2");
                //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate friendsFrom = friendshipsSet.getDate("data").toLocalDate();
                Prietenie prietenie = new Prietenie(id1, id2, friendsFrom);
                friendships.add(prietenie);
            }
            return friendships;
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public Page<Utilizator> findFriendsByUserIdPaging(Long userId, Pageable pageable) {
        List<Utilizator> friends = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT u.id, u.first_name, u.last_name " +
                             "FROM friendships f " +
                             "JOIN users u ON (f.id1 = u.id OR f.id2 = u.id) " +
                             "WHERE (f.id1 = ? OR f.id2 = ?) AND u.id <> ? AND (f.id1 <> ? OR f.id2 <> ?) " +
                             " LIMIT ? OFFSET ?")) {

            statement.setLong(1, userId);
            statement.setLong(2, userId);
            statement.setLong(3, userId);
            statement.setLong(4, userId);
            statement.setLong(5, userId);
            statement.setInt(6, pageable.getPageSize());
            statement.setInt(7, pageable.getPageSize() * (pageable.getPageNumber() - 1));

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String password = resultSet.getString("user_password");

                Utilizator friend = new Utilizator(firstName, lastName,password);
                friend.setId(id);
                friends.add(friend);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //return friends;
        //System.out.println(friends);
        return new PageImplementation<Utilizator>(pageable, friends.stream());
    }

    public void deleteFrienshipsUtilizator(Long user){
        String sql = "DELETE FROM friendships where id1 = ? or id2 = ?";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement Statement = connection.prepareStatement(sql);)
        {
            Statement.setInt(1, Math.toIntExact(user));
            Statement.setInt(2, Math.toIntExact(user));
            Statement.executeUpdate();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
    public boolean existaPrietenie(Long id1, Long id2){
        List<Prietenie> frienships = findAll();
        return frienships.stream()
                .anyMatch(user ->
                        (user.getFriend1().equals(id1) && user.getFriend2().equals(id2)));
    }


}
