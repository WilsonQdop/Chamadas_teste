package com.WilsonQdop.Chamadas.models;

import com.WilsonQdop.Chamadas.enums.StatusEnum;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class CalledHistory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  id;

    private String observation;
    private LocalDateTime createdAt;

    private StatusEnum status;

    @ManyToOne
    @JoinColumn(name = "called_id")
    private Called called;
    @ManyToOne
    private Technical technical;

    public CalledHistory() {
        this.createdAt = LocalDateTime.now();
    }

    public StatusEnum getStatus() {return status; }

    public void setStatus(StatusEnum status) {this.status = status; }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Called getCalled() {
        return called;
    }

    public void setCalled(Called called) {
        this.called = called;
    }

    public Technical getTechnical() {
        return technical;
    }

    public void setTechnical(Technical technical) {
        this.technical = technical;
    }
}
