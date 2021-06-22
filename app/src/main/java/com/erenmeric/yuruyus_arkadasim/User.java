package com.erenmeric.yuruyus_arkadasim;

public class User {

    String name, surname, city, mail, password, uid;
    int age, gender;
     // 0 -> erkek, 1 -> kız


    public User() {

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public User(String name, String surname, String city, String mail, String password, int age, int gender,
                String uid) {
        this.name = name;
        this.surname = surname;
        this.city = city;
        this.mail = mail;
        this.password = password;
        this.age = age;
        this.gender = gender;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String toString() {
        return "User: " + name + " " + surname + " Age: " + age+ " Gender: " + gender;
    }
}
