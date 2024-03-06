package com.example.demo11.repository;

import com.example.demo11.Main;
import com.example.demo11.domain.Invite;
import com.example.demo11.domain.InviteStatus;
import com.example.demo11.domain.Utilizator;
import com.example.demo11.repository.paging.Page;
import com.example.demo11.repository.paging.PageImplementation;
import com.example.demo11.repository.paging.Pageable;

import java.sql.*;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class InviteDBRepository {

    private String url;

    private String username;

    private String password;

    public InviteDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public Invite save(Invite invitation) {
            List<Invite> invitations = findAll();
            for (Invite inv : invitations) {
                if (inv.getFromInvite().equals(invitation.getToInvite()) &&
                        inv.getToInvite().equals(invitation.getFromInvite()) &&
                        inv.getStatus().equals("PENDING")) {
                    throw new IllegalArgumentException("Exista deja o cerere de prietenie!");
                }
            }

            String sql = "insert into invitations(id_1, id_2, status) values (?, ?, ?)";
            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1, Math.toIntExact(invitation.getFromInvite()));
                statement.setInt(2, Math.toIntExact(invitation.getToInvite()));
                statement.setString(3, invitation.getStatus());

                int response = statement.executeUpdate();

                if (response == 0) {
                    return invitation;
                }

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        invitation.setId(generatedKeys.getLong(1));
                    } else {
                        throw new SQLException("Creating user failed, no ID obtained.");
                    }
                }

                return invitation;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
    }

    public List<Invite> findAll(){
        List<Invite> invitations = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select * from invitations");
            ResultSet invitationsSet = statement.executeQuery();

        ){
            while(invitationsSet.next()){
                Long id1 = invitationsSet.getLong("id_1");
                Long id2 = invitationsSet.getLong("id_2");
                String status = invitationsSet.getString("status");
                Invite invite = new Invite(id1,id2,status);
                invitations.add(invite);
            }
            return invitations;
        }catch (SQLException e){
            throw new RuntimeException();
        }
    }

    public Page<Utilizator> findAllPaging(Pageable pageable, Long id){
        List<Utilizator> users = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("SELECT u.* FROM invitations i " +
                    "JOIN users u ON i.id_1 = u.id  WHERE i.status = 'PENDING' AND i.id_2 = ? limit ? offset ?" );

        ){
            statement.setInt(1, Math.toIntExact(id));
            statement.setInt(2, pageable.getPageSize());
            statement.setInt(3, pageable.getPageSize() * (pageable.getPageNumber() - 1));
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                Long userId = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String password = resultSet.getString("user_password");
                Utilizator user = new Utilizator(firstName, lastName, password);
                user.setId(userId);
                users.add(user);
            }
            //return invitations;
            return new PageImplementation<Utilizator>(pageable, users.stream());
        }catch (SQLException e){
            throw new RuntimeException();
        }
    }


    public Invite update(Invite invitation){
        String sql = "UPDATE invitations SET status = ? WHERE id_1 = ? AND id_2 = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, invitation.getStatus());
            statement.setInt(2, Math.toIntExact(invitation.getFromInvite()));
            statement.setInt(3, Math.toIntExact(invitation.getToInvite()));

            int response = statement.executeUpdate();
            return response == 0 ? invitation : null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
