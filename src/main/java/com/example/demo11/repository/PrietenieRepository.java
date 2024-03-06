package com.example.demo11.repository;

import com.example.demo11.domain.Prietenie;
import com.example.demo11.domain.Tuple;
import com.example.demo11.validator.PrietenieValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PrietenieRepository<P extends Prietenie> {
    List<P> friendships;
    PrietenieValidator validator;

    /***
     * Constructor pentru utilizator
     * @param validator: validatorul care imi valideaza prieteniile
     */
    public PrietenieRepository(PrietenieValidator validator)
    {
        this.validator = validator;
        this.friendships = new ArrayList<P>();
    }

    /***
     * Salveaza o prietenie
     * @param friendship: prietenia de salvat
     * @return: acel obiect Prietenie frienship
     */
    public P save(P friendship){
//        if(friendship == null)
//            throw new IllegalArgumentException("Frienship must be not null");
//        Tuple<Long, Long> id = friendship.getId();
//        validator.validate(id.getLeft(), id.getRight());
//        Tuple<Long, Long> idp = friendship.getId();
//        Long id1 = idp.getLeft();
//        Long id2 = idp.getRight();
//
//        for(P friends : friendships){
//            Tuple<Long, Long> idf = friends.getId();
//            if(idf.getLeft().equals(id2) && idf.getRight().equals(id1))
//                throw new IllegalArgumentException("Deja avem prietenie intre utilizatorii cu id-urile  " + id1 + " si " + id2);
//        }
//        if(friendships.contains(friendship) != false)
//            return friendship;
//
//        else{
//            friendships.add(friendship);
//        }
//        return null;

        if(friendship == null){
            throw new IllegalArgumentException("Friendship must not be null");
        }
        Tuple<Long, Long> id = friendship.getId();
        //validator.validate(id.getLeft(), id.getRight());
        Long id1 = id.getRight();
        Long id2 = id.getLeft();
//        Prietenie reversed_fr = Prietenie.create(id1, id2);
//        Tuple<Long, Long> idp = reversed_fr.getId();
//
//        if(friendships.stream().anyMatch(friends->friends.getId().equals(idp) || friends.getId().equals(id))) {
//            throw new IllegalArgumentException("Deja avem prietenie intre utilizatorii cu id-urile");
//        }
        if(!friendships.contains(friendship)){
            friendships.add(friendship);
            return friendship;
        }else{
            return null;
        }
    }

    /***
     * Returneaza toata lista de prietenii
     * @return: list of friendships
     */
    public List<P> findAll(){
        return friendships;
    }

    /***
     * Sterge o prietenie
     * @param frienship: prietenie de sters
     * @return: P friendship
     */
    public boolean delete(P frienship) {
        if (!friendships.contains(frienship))
            throw new IllegalArgumentException("Nu exista prietenia!");
        return friendships.remove(frienship);
    }

    /***
     * Sterge prieteniile pentru utilizatorul sters
     * @param user: id-ul userului sters
     */

    public void deleteFriendshipsUtilizator(Long user) {
//        List newFriendships = new ArrayList();
//        for(P prieten : friendships) {
//            Tuple<Long, Long> id = prieten.getId();
//            if (!id.getLeft().equals(user) && !id.getRight().equals(user)) {
//                newFriendships.add(prieten);
//            }
//        }
//        for(P prieten : friendships)
//            this.friendships.remove(prieten);
//        this.friendships = newFriendships;
        List<P> newFrienships = friendships.stream()
                .filter(prieten->{
                    Tuple<Long, Long> id = prieten.getId();
                    return !id.getLeft().equals(user) && !id.getRight().equals(user);
                })
                .collect(Collectors.toList());

        this.friendships = newFrienships;

    }

}
