package com.WilsonQdop.Chamadas.models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("TECH")
public class Technical extends Person {

    @OneToMany(mappedBy = "technical")
    List<Called> calleds;

    public Technical () {
        calleds = new ArrayList<>();
    }

    public List<Called> getCalleds() {
        return calleds;
    }

    public void setCalleds(List<Called> calleds) {
        this.calleds = calleds;
    }
}
