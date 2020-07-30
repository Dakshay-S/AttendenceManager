package sample;

import javafx.beans.property.*;

public class Course
{
    private StringProperty subject;
    private IntegerProperty noOfPresents;
    private IntegerProperty noOfAbsents;
    private FloatProperty percentage;

    public Course()
    {
        subject = new SimpleStringProperty();
        noOfPresents = new SimpleIntegerProperty();
        noOfAbsents = new SimpleIntegerProperty();
        percentage = new SimpleFloatProperty();

    }

    public Course(String subject, int noOfPresents, int noOfAbsents, float percentage) {
        this.subject = new SimpleStringProperty(subject);
        this.noOfPresents = new SimpleIntegerProperty(noOfPresents);
        this.noOfAbsents = new SimpleIntegerProperty(noOfAbsents);
        this.percentage = new SimpleFloatProperty(percentage);
    }



    public String getSubject() {
        return subject.get();
    }

    public StringProperty subjectProperty() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject.set(subject);
    }

    public int getNoOfPresents() {
        return noOfPresents.get();
    }

    public IntegerProperty noOfPresentsProperty() {
        return noOfPresents;
    }

    public void setNoOfPresents(int noOfPresents) {
        this.noOfPresents.set(noOfPresents);
    }

    public int getNoOfAbsents() {
        return noOfAbsents.get();
    }

    public IntegerProperty noOfAbsentsProperty() {
        return noOfAbsents;
    }

    public void setNoOfAbsents(int noOfAbsents) {
        this.noOfAbsents.set(noOfAbsents);
    }

    public float getPercentage() {
        return percentage.get();
    }

    public FloatProperty percentageProperty() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage.set(percentage);
    }
}
