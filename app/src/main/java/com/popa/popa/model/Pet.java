package com.popa.popa.model;

import java.util.Random;

public class Pet {

    private int level;
    private int experience;
    private String name;
    private int str;
    private int dex;
    private int agi;
    private int intel;
    private int luck;
    private String primaryAttribute;

    public Pet(String name, int str, int dex, int agi, int intel, String primaryAttribute){
        this.level = 1;
        this.experience =0;
        this.name = name;
        this.str = str;
        this.dex = dex;
        this.agi = agi;
        this.intel = intel;
        this.luck = 10;
        this.primaryAttribute = primaryAttribute;
    }

    public int getAgi() {
        return agi;
    }

    public int getDex() {
        return dex;
    }

    public int getIntel() {
        return intel;
    }

    public int getStr() {
        return str;
    }

    public int getExperience() {
        return experience;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public int getLuck() {
        return luck;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }


    public void setAgi(int agi) {
        this.agi = agi;
    }

    public void setDex(int dex) {
        this.dex = dex;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStr(int str) {
        this.str = str;
    }

    public void setIntel(int intel) {
        this.intel = intel;
    }

    public String getPrimaryAttribute() {
        return primaryAttribute;
    }
}
