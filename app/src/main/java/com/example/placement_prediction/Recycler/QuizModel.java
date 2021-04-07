package com.example.placement_prediction.Recycler;

import com.google.firebase.firestore.DocumentId;

public class QuizModel {
    private String desc,image,visiblity,name;
    private Long questions;

    @DocumentId
    private String quiz_id;

    public QuizModel(){

    }

    public QuizModel(String desc, String image, String visiblity, String name, Long questions, String quiz_id) {
        this.desc = desc;
        this.image = image;
        this.visiblity = visiblity;
        this.name = name;
        this.questions = questions;
        this.quiz_id = quiz_id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVisiblity() {
        return visiblity;
    }

    public void setVisiblity(String visiblity) {
        this.visiblity = visiblity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getQuestions() {
        return questions;
    }

    public void setQuestions(Long questions) {
        this.questions = questions;
    }

    public String getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(String quiz_id) {
        this.quiz_id = quiz_id;
    }
}
