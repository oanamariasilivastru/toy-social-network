package com.example.demo11.domain;


//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;


public class Comunitate {
    private static short NUMBERS = 1;
    private List<Utilizator> userlist = new ArrayList();
    private static int numarComunitati = 0;
    Map<Long, Integer> vizitat = new HashMap();
    List<Utilizator> useri_comunitate = new ArrayList();

    public Comunitate() {
    }

    public List<Utilizator> getUserlist() {
        return this.userlist;
    }

    public int getNumarComunitati() {
        return numarComunitati;
    }

    public void setUserlist(List<Utilizator> userlist) {
        this.userlist = userlist;
    }

    public void addUserList(Utilizator utilizator) {
        this.userlist.add(utilizator);
    }

    public void NewUserList() {
        this.userlist.clear();
    }

    public void deleteUserList(Utilizator utilizator) {
        Optional var10000 = Optional.ofNullable(utilizator);
        List var10001 = this.userlist;
        Objects.requireNonNull(var10001);
        var10000.ifPresent(var10001::remove);
    }

    public String toString() {
        List<String> ListaNume = (List)this.userlist.stream().map((user) -> {
            String var10000 = user.getFirstName();
            return var10000 + "," + user.getLastName();
        }).collect(Collectors.toList());
        String var10000 = String.valueOf(ListaNume);
        return "Comunitate{userlist=" + var10000 + ", numarComunitati=" + numarComunitati + "}";
    }

    public void initializare_vizitat() {
        this.vizitat = (Map)this.userlist.stream().collect(Collectors.toMap(Entity::getId, (user) -> {
            return 0;
        }));
    }

    public void dfs(Utilizator user, int comunitate) {
        this.vizitat.put((Long)user.getId(),comunitate);
        this.userlist.stream().filter((u) -> {
            return (Integer)this.vizitat.get(u.getId()) == 0 && u.isfriend(user);
        }).forEach((u) -> {
            this.dfs(u, comunitate);
        });
    }

    public int nr_comunitati() {
        this.initializare_vizitat();
        numarComunitati = 0;
        numarComunitati = (int)this.userlist.stream().filter((user) -> {
            if ((Integer)this.vizitat.get(user.getId()) == 0) {
                this.dfs(user, numarComunitati + 1);
                ++numarComunitati;
                return true;
            } else {
                return false;
            }
        }).count();
        numarComunitati = (int)((long)numarComunitati - this.userlist.stream().filter((user) -> {
            return user.getFriends() == null || user.getFriends() != null && user.getFriends().size() == 0;
        }).count());
        System.out.println(this.userlist);
        System.out.println(this.vizitat);
        return numarComunitati;
    }

    public List<Utilizator> sociabil() {
        int numarComunitati = this.nr_comunitati();
        long numarComunitatinule = this.userlist.stream().filter((user) -> {
            return user.getFriends() == null;
        }).count();
        int var10000 = (int)((long)numarComunitati + numarComunitatinule);
        Map<Integer, Long> communityCounts = (Map)this.userlist.stream().collect(Collectors.groupingBy((user) -> {
            return (Integer)this.vizitat.get(user.getId());
        }, Collectors.counting()));
        int idComunitate = (Integer)((Entry)Collections.max(communityCounts.entrySet(), Entry.comparingByValue())).getKey();
        List<Utilizator> useriComunitate = (List)this.userlist.stream().filter((user) -> {
            return (Integer)this.vizitat.get(user.getId()) == idComunitate;
        }).collect(Collectors.toList());
        return useriComunitate;
    }
}
