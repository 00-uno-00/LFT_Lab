public class NumberTok extends Token {
	public final int value;
	public NumberTok(int v) { super(Tag.NUM); value = v; }
	public String toString() { return "<" + tag + ", " + value + ">"; }
}
