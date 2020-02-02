package com.yang.data;

public class QuestionItem {
    public static final int SUBMITTED=1;//状态: 已提交
    public static final int NOT_SUBMITTED=2;//状态: 未提交
    public static final int OVER_DUE=3;//状态: 已过期

    private int question_id;
    private String question;
    private String date_time;
    private String choice1;
    private String choice2;
    private String choice3;
    private int status=NOT_SUBMITTED;

    public QuestionItem(String question,String date_time,String choice1,String choice2,String choice3,int question_id,int status) {
        this.question=question;
        this.date_time=date_time;
        this.choice1=choice1;
        this.choice2=choice2;
        this.choice3=choice3;
        this.question_id=question_id;
        this.status=status;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public String getQuestion(){
        return question;
    }

    public String getDate_time(){
        return date_time;
    }

    public String getChoice1(){
        return choice1;
    }

    public String getChoice2(){
        return choice2;
    }

    public String getChoice3(){
        return choice3;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status=status;
    }
}
