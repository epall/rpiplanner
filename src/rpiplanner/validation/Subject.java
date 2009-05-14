package rpiplanner.validation;

public class Subject {

    int level;
    int minNum;
    int maxNum;
    String subject;


    Subject(String subject, int level,int minNum, int maxNum,int maxCred) {
        this.level = level;
        this.subject = subject;
        this.maxNum = maxNum;
        this.minNum = minNum
    }
    
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getMinNum() {
        return minNum;
    }

    public void setMinNum(int minNum) {
        this.minNum = minNum;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }
}
