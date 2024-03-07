public class Cell {

    boolean isBomb = false;
    boolean isMarked = false;
    boolean isRevealed = false;
    int number;

    public Cell() {
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        if (!isBomb) this.number = number;
    }

    public void setBomb(boolean bomb) {
        isBomb = bomb;
    }

    public void setMarked(boolean marked) {
        isMarked = marked;
    }

    public void setRevealed(boolean revealed) {
        isRevealed = revealed;
    }
}
