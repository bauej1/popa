package com.popa.popa.model;

import java.util.ArrayList;

public class PetCollection {

    private Pet currentPet;
  private  ArrayList<Pet> petList;
    public PetCollection(){
    petList = new ArrayList<>();
        Pet Gunter = new Pet("Gunter",0,0,12,0,"agi");
        Pet Kuzco = new Pet("Kuzco",0,10,2,0,"dex");
        Pet Bjorn = new Pet("Bjorn",10,0,2,0,"str");
        Pet Grimbal = new Pet("Grimbal",0,0,2,10,"intel");

        petList.add(Gunter);
        petList.add(Kuzco);
        petList.add(Bjorn);
        petList.add(Grimbal);
    }

    public Pet getPet(int ID){
        return petList.get((ID));
    }

    public void setCurrentPet(Pet currentPet) {
        this.currentPet = currentPet;
    }

    public Pet getCurrentPet() {
        return currentPet;
    }
}
