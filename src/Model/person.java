package Model;

public class person {
    private int numberOfDays;
    private boolean employee = false;

    public person(){
        numberOfDays=0;
    }

    public void daysOfWork(){
        if(this.employee){
            numberOfDays++;
        }
    }

    public void startWorking(){
        this.employee = true;
    }

}
