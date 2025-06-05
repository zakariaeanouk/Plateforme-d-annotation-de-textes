package com.AnnotationPlatform.Core.bo;

import jakarta.persistence.*;
import lombok.*;

@Entity
public class ModelTrainingLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String dateExecution;
    private String utilisateur;
    private String hyperparametres;
    private String score;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDateExecution() {
        return dateExecution;
    }

    public void setDateExecution(String dateExecution) {
        this.dateExecution = dateExecution;
    }

    public String getHyperparametres() {
        return hyperparametres;
    }

    public void setHyperparametres(String hyperparametres) {
        this.hyperparametres = hyperparametres;
    }

    public String getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(String utilisateur) {
        this.utilisateur = utilisateur;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
