package rpiplanner.validation;

public class Subject {

    int minLevel;
    int maxLevel;
    int minNum;
    int maxNum;
    String subject;


    Subject(String subject, int minLevel, int maxLevel, int minNum, int maxNum) {
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.subject = subject;
        this.maxNum = maxNum;
        this.minNum = minNum;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public int getMinLevel() {
        return minLevel;
    }

    public void setMinLevel(int minLevel) {
        this.minLevel = minLevel;
    }
    
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
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
