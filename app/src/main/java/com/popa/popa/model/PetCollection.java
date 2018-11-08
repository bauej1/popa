package com.popa.popa.model;

import java.util.ArrayList;

public class PetCollection {

    private Pet currentPet;
  private  ArrayList<Pet> petList;
    public PetCollection(){
    petList = new ArrayList<>();
        Pet gunter = new Pet("Gunter",0,0,12,0,"agi");
        Pet kuzco = new Pet("Kuzco",0,10,2,0,"dex");
        Pet bjorn = new Pet("Bjorn",10,0,2,0,"str");
        Pet grimbal = new Pet("Grimbal",0,0,2,10,"intel");

        petList.add(gunter);
        petList.add(kuzco);
        petList.add(bjorn);
        petList.add(grimbal);
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
