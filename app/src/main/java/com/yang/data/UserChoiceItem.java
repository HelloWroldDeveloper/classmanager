package com.yang.data;

public class UserChoiceItem {
    private int question_id;
    private int user_id;
    private int user_choice;

    public UserChoiceItem(int question_id, int user_id,int user_choice) {
        this.question_id=question_id;
        this.user_id=user_id;
        this.user_choice=user_choice;
    }

    public int getQuestion_id() {return this.question_id;}
    public int getUser_id() {return this.user_id;}
    public int getUser_choice() {return this.user_choice;}
}
