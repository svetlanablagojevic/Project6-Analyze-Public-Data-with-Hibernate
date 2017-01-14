package com.teamtreehouse.project6.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Country {
    @Id
    private String code;

    @Column
    private String name;

    @Column
    public Double internetUsers;

    @Column
    public Double adultLiteracyRate;

    /**default constructor**/
    public Country() {}


    /** **/
    public Country (CountrytBuilder builder ) {
        this.code=builder.code;
        this.name=builder.name;
        this.internetUsers=builder.internetUsers;
        this.adultLiteracyRate=builder.adultLiteracyRate;


    }


    /**getters and setters**/

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getInternetUsers() {
        return internetUsers;
    }

    public void setInternetUsers(Double internetUsers) {
        this.internetUsers = internetUsers;
    }

    public Double getAdultLiteracyRate() {
        return adultLiteracyRate;
    }

    public void setAdultLiteracyRate(Double adultLiteracyRate) {
        this.adultLiteracyRate = adultLiteracyRate;
    }

    /** class Builder**/
    public static class CountrytBuilder {
        private String code;
        private String name;
        private Double internetUsers;
        private Double adultLiteracyRate;


        public CountrytBuilder(String code, String name ) {
            this.code=code;
            this.name=name;

        }

        public CountrytBuilder withInternetUsers(Double internetUsers) {
            this.internetUsers=internetUsers;
            return this;

        }

        public  CountrytBuilder withAdultLiteracyRate(Double adultLiteracyRate) {
            this.adultLiteracyRate=adultLiteracyRate;
            return this;

        }

        public  Country build () {
            return new Country(this);
        }


    }


    @Override
    public String toString() {
        return "Country{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", internetUsers=" + internetUsers +
                ", adultLiteracyRate=" + adultLiteracyRate +
                '}';
    }
}
