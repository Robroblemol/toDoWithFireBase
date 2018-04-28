package co.edu.ucc.todoapp.data.entidades;

public class TaskEntity {

    private  String date;
    private String description;
    private boolean done;

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getDescription() {
        return description;
    }

    public  void setDate(String date){this.date=date;}

    public String getDate(){return date;}

    public void setDescription(String description) {
        this.description = description;
    }
}
