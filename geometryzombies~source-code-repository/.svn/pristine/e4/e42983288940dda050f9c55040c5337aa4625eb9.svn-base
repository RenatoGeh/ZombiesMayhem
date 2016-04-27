package geometryzombiesmayhem;

/**
 * <p>A mutable Wrapper class for <code>integer</code>s.</p>
 * 
 * @author Renato Lui Geh
 */
public class MutableInteger {
    private int n = 0;

    public MutableInteger(){}
    public MutableInteger(int n) {this.n = n;}
    public MutableInteger(MutableInteger e) {this.n = e.n;}

    public int getValue() {return n;}
    public void setValue(int n) {this.n = n;}

    public void increment() {n++;}
    public void decrement() {n--;}

    public void add(int n) {this.n+=n;}
    public void subtract(int n) {this.n-=n;}

    public void multiply(int n) {this.n*=n;}
    public void divide(int n) {this.n/=n;}

    @Override
    public String toString() {return String.valueOf(n);}
    @Override
    public boolean equals(Object e) {
        if(e instanceof MutableInteger)
            return ((MutableInteger)e).n == this.n;
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.n;
        return hash;
    }
}