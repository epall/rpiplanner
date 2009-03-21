package rpiplanner.validation;

public class Subject {
    Integer level;
    int maxNum;

    String subject;


    Subject(String subject, int level,int maxNum,int maxCred) {
        this.level = level;
        this.subject = subject;
        this.maxNum = maxNum;
    }
}
