package com.example.workprofil.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
@Entity
public class Worker implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    @ColumnInfo(name="photoWorker")
    private String photoWorker;
    @ColumnInfo(name="nameWorker")
    private String nameWorker;
    @ColumnInfo(name="skillWorker")
    private String skillWorker;
    @ColumnInfo(name="textPhotoPathWorker")
    private String textPhotoPathWorker;

    public Worker(Integer id, String photoWorker, String nameWorker, String skillWorker, String textPhotoPathWorker) {
        this.id = id;
        this.photoWorker = photoWorker;
        this.nameWorker = nameWorker;
        this.skillWorker = skillWorker;
        this.textPhotoPathWorker = textPhotoPathWorker;
    }

    public  Worker(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhotoWorker() {
        return photoWorker;
    }

    public void setPhotoWorker(String photoWorker) {
        this.photoWorker = photoWorker;
    }

    public String getNameWorker() {
        return nameWorker;
    }

    public void setNameWorker(String nameWorker) {
        this.nameWorker = nameWorker;
    }

    public String getSkillWorker() {
        return skillWorker;
    }

    public void setSkillWorker(String skillWorker) {
        this.skillWorker = skillWorker;
    }

    public String getTextPhotoPathWorker() {
        return textPhotoPathWorker;
    }

    public void setTextPhotoPathWorker(String textPhotoPathWorker) {
        this.textPhotoPathWorker = textPhotoPathWorker;
    }


}
