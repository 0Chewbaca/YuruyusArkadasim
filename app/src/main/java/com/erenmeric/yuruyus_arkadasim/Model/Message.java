package com.erenmeric.yuruyus_arkadasim.Model;

import java.util.Calendar;

public class Message {

    private String id;
    private String text;
    private long timestamp;
    private boolean seen;
    private String author;

    public Message(){}

    public Message(String id, String text, long timestamp, boolean seen, String author) {
        this.id = id;
        this.text = text;
        this.timestamp = timestamp;
        this.seen = seen;
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimeToString(){
        Calendar cal = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        String res ="";
        if(now.get(Calendar.DAY_OF_YEAR) == cal.get(Calendar.DAY_OF_YEAR) + 1)
            res = "yesterday";
        else if(now.get(Calendar.DAY_OF_YEAR) == cal.get(Calendar.DAY_OF_YEAR) ){
            String hour, min;
            if(cal.get(Calendar.HOUR_OF_DAY) <= 9){
                hour = "0" + cal.get(Calendar.HOUR_OF_DAY);
            } else
                hour = ""+cal.get(Calendar.HOUR_OF_DAY);

            if(cal.get(Calendar.MINUTE) <= 9){
                min = "0" + cal.get(Calendar.MINUTE);
            } else
                min = ""+cal.get(Calendar.MINUTE);

            res = hour + ":" + min;
        } else {
            res = cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR);
        }
        return res;
    }

    public boolean getSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", timestamp=" + timestamp +
                ", seen=" + seen +
                ", author='" + author + '\'' +
                '}';
    }

}
