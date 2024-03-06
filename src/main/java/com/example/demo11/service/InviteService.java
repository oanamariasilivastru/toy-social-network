package com.example.demo11.service;

import com.example.demo11.domain.Invite;
import com.example.demo11.domain.Prietenie;
import com.example.demo11.domain.Utilizator;
import com.example.demo11.repository.InviteDBRepository;
import com.example.demo11.repository.RepositoryException;
import com.example.demo11.repository.paging.Page;
import com.example.demo11.repository.paging.Pageable;
import com.example.demo11.utils.*;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class InviteService implements Observable<InviteChangeEvent> {

    private InviteDBRepository inviteDBRepository;

    public InviteService(InviteDBRepository inviteDBRepository) {
        this.inviteDBRepository = inviteDBRepository;
    }

    public Invite createInvite(Long user1, Long user2, String status){
        Invite invite = new Invite(user1, user2, "PENDING");
        try {
            Invite invite1 = inviteDBRepository.save(invite);
            if(invite1 != null)
                notifyObservers(new InviteChangeEvent(ChangeEventType.ADD_REQUEST, invite1));
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
        return invite;
    }

    public List<Invite> getInvitations(){
        return inviteDBRepository.findAll();
    }

    public Invite updateInvite(Invite invitation, String status){
        invitation.setStatus(status);
        return inviteDBRepository.update(invitation);
    }


    public Page<Utilizator> getInvitationsPaging(Pageable pageable, Utilizator utilizator){
        return inviteDBRepository.findAllPaging(pageable, utilizator.getId());
    }

    private List<Observer<InviteChangeEvent>> observers = new ArrayList<>();
    public void notifyObservers(InviteChangeEvent u){
        observers.forEach(x ->x.update(u));
    }

    public void removeObserver(Observer<InviteChangeEvent> u){
        observers.remove(u);
    }

    public void addObserver(Observer<InviteChangeEvent> u){
        observers.add(u);
    }
}
