package server.messages;

public class Range {
    private int startingIndex;
    private int finishingIndex;

    public Range(int startingIndex, int finishingIndex) {
        this.startingIndex = startingIndex;
        this.finishingIndex = finishingIndex;
    }

    public int getStartingIndex() {
       return startingIndex;
    }

    public int getFinishingIndex() {
       return finishingIndex;
    }
}
