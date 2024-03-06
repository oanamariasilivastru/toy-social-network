package com.example.demo11.service;

import com.example.demo11.domain.Comunitate;
import com.example.demo11.domain.Prietenie;
import com.example.demo11.domain.Utilizator;
import com.example.demo11.repository.FriendshipDBRepository;
import com.example.demo11.repository.RepositoryException;
import com.example.demo11.repository.paging.Page;
import com.example.demo11.repository.paging.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PrietenService {
    // private PrietenieRepository<Prietenie> repoFriendships;
    Comunitate comunitate = new Comunitate();
    private FriendshipDBRepository friendshipDBRepository;

    private UtilizatoriService utilizatoriService;

    /***
     * Constructor pentru service
     * @param friendshipDBRepository: repo care gestioneaza prieteniile
     */
    public PrietenService(FriendshipDBRepository friendshipDBRepository, UtilizatoriService utilizatoriService) {
        this.friendshipDBRepository = friendshipDBRepository;
        this.utilizatoriService = utilizatoriService;
    }

    /***
     * Creeaza o prietenie intre 2 useri
     * @param user1: user1 long
     * @param user2: user2 long
     */
    public boolean createFriendship(Long user1, Long user2, String data) {
        try {
            friendshipDBRepository.save(Prietenie.create(user1, user2, data));
            return true;
        } catch (RepositoryException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /***
     * Sterge o prietenie intre 2 useri
     * @param user1: user1 long
     * @param user2: user2 long
     */
    public void deleteFriendship(Long user1, Long user2) {

        if (friendshipDBRepository.findOne(user1, user2) != null)
            friendshipDBRepository.delete(friendshipDBRepository.findOne(user1, user2));
    }

    /***
     * Sterge din lista de prieteni a unui utilizator acei useri cu care nu e prieten
     * @param user: userul de sters
     */
    public void deleteFrienshipsUtilizator(Long user) {
        friendshipDBRepository.deleteFrienshipsUtilizator(user);
    }

    /***
     * gaseste toti prietenii pentru un user
     * @return: lista de prieteni
     */
    public List<Prietenie> getFriendshipsList() {
        return friendshipDBRepository.findAll();

    }

    public Page<Utilizator> getFriendshipsPaging(Pageable pageable, Utilizator utilizator) {
        return friendshipDBRepository.findFriendsByUserIdPaging(utilizator.getId(), pageable);

    }
}
