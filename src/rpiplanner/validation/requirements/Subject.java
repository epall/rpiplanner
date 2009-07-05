package rpiplanner.validation.requirements;

public class Subject {

    int minLevel;
    int maxLevel;
    int minNum;
    int maxNum;
    String prefix;


    Subject(String prefix, int minLevel, int maxLevel, int minNum, int maxNum) {
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.prefix = prefix;
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

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
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
