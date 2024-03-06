package com.example.demo11.service;
import com.example.demo11.domain.Comunitate;
import com.example.demo11.domain.Message;
import com.example.demo11.domain.UserFrienshipTuple;
import com.example.demo11.domain.Utilizator;
import com.example.demo11.repository.MessageDBRepository;
import com.example.demo11.repository.UserDBRepository;
import com.example.demo11.repository.paging.Page;
import com.example.demo11.repository.paging.Pageable;
import com.example.demo11.repository.paging.PageableImplementation;
import com.example.demo11.utils.ChangeEventType;
import com.example.demo11.utils.Observable;
import com.example.demo11.utils.Observer;
import com.example.demo11.utils.UserChangeEvent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class UtilizatoriService  implements Observable<UserChangeEvent> {
    private UserDBRepository userDBRepository;
    private MessageDBRepository messageDBRepository;

    private Message messageReply;

    private Message latestMessage;
    Comunitate comunitate = new Comunitate();

    private List<Utilizator> useri = new ArrayList<>();

    /***
     * Constructor pentru service
     * @param userDBRepository: un repo in care imi gestionez utilizatorii
     */
    public UtilizatoriService(UserDBRepository userDBRepository,MessageDBRepository messageDBRepository) {
        this.userDBRepository = userDBRepository;
        this.messageDBRepository = messageDBRepository;


    }

    public boolean updateUser(Utilizator newuser){
        Utilizator u = this.findUser(newuser.getId());
        if(u != null){
            userDBRepository.update(newuser);
            notifyObservers(new UserChangeEvent(ChangeEventType.UPDATE,newuser));
            return true;
        }
        return false;
    }


    public Utilizator findUserByFirstNameAndPassword(String firstName, String password){
        return userDBRepository.findOneByFirstNameAndPassword(firstName, password);

    }
    /**
     * Salveaza utilizatorul
     * @param firstName: string - primul nume
     * @param LastName: string - al doilea nume
     */
    public Utilizator saveUser(String firstName, String LastName, String password) {
        Utilizator utilizator = new Utilizator(firstName, LastName, password);
        //Am folosit aici un Supplier
        Supplier<Long> generateIdSupplier = () -> UUID.randomUUID().getMostSignificantBits() & Short.MAX_VALUE;
        Long id = generateIdSupplier.get();
        utilizator.setId(id);
        userDBRepository.save(utilizator);
        Utilizator user = this.findUser(id);
        if(user != null)
            notifyObservers(new UserChangeEvent(ChangeEventType.ADD,utilizator));
        return user;

    }

    /**
     * Sterge utilizatorul cu un id datte
     * @param id: id-ul utilizatorului
     */
    public boolean deleteUser(Long id) {
        Optional<Utilizator> u = userDBRepository.delete(id);
        if(u.isPresent()){
            notifyObservers(new UserChangeEvent(ChangeEventType.DELETE, u.get()));
        }
        if(u.isPresent())
            return true;
        else
            return false;
//        for (Utilizator u : lista) {
//            Long k = u.getId();
//            if (user.isPresent() && u.isfriend(user.get()))
//                u.deleteFriend(user.get());
//        }
//        if (user.isPresent()) {
//            comunitate.deleteUserList(user.get());
//            repoMemory.delete(id);
//            return true;
//        }
//        return false;

//        user.ifPresent(userToDelete -> {
//            lista.forEach(u -> {
//                if (u.isfriend(userToDelete)) {
//                    u.deleteFriend(userToDelete);
//                }
//            });
//        });

//
//        return user.map(utilizator -> {
//            userDBRepository.delete(id);
//            return true;
//        }).orElse(false);

    }

    /**
     * Adauga fiecare user in lista fiecaruia de prieteni
     * @param user1: userul1, utilizator
     * @param user2: userul2, utilizator
     */
    public void createFriendship(Long user1, Long user2){
//        Optional<Utilizator>utilizator1 = repoMemory.findOne(user1);
//        Optional<Utilizator> utilizator2 = repoMemory.findOne(user2);
//        if (utilizator1.isPresent() && utilizator2.isPresent()){
//
//            utilizator2.get().addFriend(utilizator1.get());
//            utilizator1.get().addFriend(utilizator2.get());
//        }
        if(!userDBRepository.existaUser(user1) || !userDBRepository.existaUser(user2))
            throw new IllegalArgumentException("Nu exista unul dintre useri sau ambii useri");



    }

    /***
     * Sterge userul din lista de prieteni si invers
     * @param user1: user1, utilizator
     * @param user2: user2, utilizator
     */

    public void deleteFriendship(Long user1, Long user2){
//        Optional<Utilizator>utilizator1 = repoMemory.findOne(user1);
//        Optional<Utilizator> utilizator2 = repoMemory.findOne(user2);
//        if (utilizator1.isPresent() && utilizator2.isPresent()){
//
//            utilizator2.get().deleteFriend(utilizator1.get());
//            utilizator1.get().deleteFriend(utilizator2.get());
//        }
        userDBRepository.findOne(user1).ifPresent(u1->
                userDBRepository.findOne(user2).ifPresent(u2->{
                    u1.deleteFriend(u2);
                    u2.deleteFriend(u1);
                }));
    }
    public Utilizator findUser(Long id){
        for(Utilizator u : this.getUserList()){
            if(u.getId().equals(id))
                return u;
        }
        return null;
    }
    public Utilizator findUserByNames(String firstName, String lastName){
        for(Utilizator u : this.getUserList()){
            if(u.getFirstName().equals(firstName) && u.getLastName().equals(lastName)) {
                return u;
            }
        }
        return null;
    }
    public Utilizator findUserByFirstName(String firstName){
        for(Utilizator u : this.getUserList()){
            if(u.getFirstName().equals(firstName)) {
                return u;
            }
        }
        return null;
    }
    /**
     * returneaza numarul de comunitati
     * @return: int - comunitate
     */
    public int comunitate(){
        comunitate.NewUserList();
        getUserList().forEach(user -> comunitate.addUserList(user));
        return comunitate.nr_comunitati();
    }

    /***
     * Verifica daca exista prietenie intre 2 useri
     * @param user1: userul1, utilizator
     * @param user2: userul2, utilizator
     * @return: boolean
     */
    public boolean nuexistaPrietenie(Long user1, Long user2){
//        Optional<Utilizator> u1 = repoMemory.findOne(user1);
//        Optional<Utilizator> u2 = repoMemory.findOne(user2);
//        return (u1.isPresent()|| u2.isPresent());
        return Stream.of(user1, user2)
                .map(userDBRepository::findOne)
                .anyMatch(Optional::isPresent);
    }

    /***
     * Gaseste cea mai sociabila comunitate
     * @return: Lista de utilizatori care formeaza cea mai sociabila comunitate
     */
    public List<Utilizator> sociabil(){

        comunitate.NewUserList();
        getUserList().forEach(user -> comunitate.addUserList(user));
        return comunitate.sociabil();
    }

    public List<UserFrienshipTuple> getFrienshipsforUser(Long id, String date){
        return userDBRepository.getFrienshipsforUser(id, date);
    }
    /***
     * Gaseste lista de prietenu pentru un user
     * @return: o lista cu prietenii unui user
     */
    public Iterable<Utilizator> getUserList(){
        return userDBRepository.findAll();

    }

    public Page<Utilizator> getUserPaging(Pageable pageable){
        System.out.println("Suntem aici!");
        System.out.println("---------");

        // Undeva într-o altă metodă sau într-o metodă de test
        Page<Utilizator> resultPage = userDBRepository.findAllPaging(pageable);
        resultPage.getContent().forEach(user -> System.out.println(user.getId() + " " + user.getFirstName() + " " + user.getLastName() + " " + user.getPassword()));
        System.out.println(resultPage);
        System.out.println(userDBRepository.findAllPaging(pageable));
        return userDBRepository.findAllPaging(pageable);
    }

    public Page<Utilizator> getAnotherPaging(Pageable pageable, Utilizator utilizator){
        return userDBRepository.findOthersPaging(pageable, utilizator.getId());
    }

    /**
     * O functie care sa mi populeze cu useri by default
     */
    public Message create(Utilizator from, List<Utilizator> to, String message, LocalDateTime data, Message messageReply){
        Message newMessage = new Message(from, to, message, data, messageReply);
        messageDBRepository.save(newMessage);
        this.latestMessage = newMessage;
        return newMessage;
    }

    public Message getMessage(String message){
        messageReply =  messageDBRepository.findMessageByUserIdsAndText(useri.get(1).getId(), useri.get(0).getId(), message);
        System.out.println("In service: " + messageReply);
        return messageReply;
    }
    public void sendUseri(Utilizator user1, Utilizator user2){
        useri.clear();
        useri.add(user1);
        useri.add(user2);

    }

    public Message getMessageReply(){
        return messageReply;
    }

    public List<Message> getAllMessages(){
        return messageDBRepository.findAll();
    }

    public List<Message> getMessagesfromUsers(Long user1, Long user2){
        return messageDBRepository.findAllFromIds(user1, user2);
    }

    public Page<Message> getMessagesFromUsersPaging(Long user1, Long user2, Pageable pageable){
        return messageDBRepository.findAllFromIdsPaging(user1, user2, pageable);
    }
    public Message getLatestMessage(){
        return this.latestMessage;
    }
//    public Message getOneMessage(){
//        for(Message m : getAllMessages()){
//            if(m.getFrom().equals(useri.get(0).getId()) && m.getTo().equals(useri.get(1).getId()) && m.getMessage().equals(message))
//                System.out.println("Hei aici!");
//                return m;
//        }
//        return null;
//    }
    public void populate(){
//        Utilizator u2=new Utilizator("u2FirstName", "u2LastName"); u2.setId(2l);
//        Utilizator u3=new Utilizator("u3FirstName", "u3LastName"); u3.setId(3l);
//        Utilizator u4=new Utilizator("u4FirstName", "u4LastName"); u4.setId(4l);
//        Utilizator u5=new Utilizator("u5FirstName", "u5LastName"); u5.setId(5l);
//        Utilizator u6=new Utilizator("u6FirstName", "u6LastName"); u6.setId(6l);
//        Utilizator u7=new Utilizator("u7FirstName", "u7LastName"); u7.setId(7l);
//
//        userDBRepository.save(u2);
//        comunitate.addUserList(u2);
//        userDBRepository.save(u3);
//        comunitate.addUserList(u3);
//        userDBRepository.save(u4);
//        comunitate.addUserList(u4);
//        userDBRepository.save(u5);
//        comunitate.addUserList(u5);
//        userDBRepository.save(u6);
//        comunitate.addUserList(u6);
//        userDBRepository.save(u7);
//        comunitate.addUserList(u7);
    }

    private List<Observer<UserChangeEvent>> observers = new ArrayList<>();
    public void notifyObservers(UserChangeEvent u){
        observers.forEach(x ->x.update(u));
    }

    public void removeObserver(Observer<UserChangeEvent> u){
        observers.remove(u);
    }

    public void addObserver(Observer<UserChangeEvent> u){
        observers.add(u);
    }

}
