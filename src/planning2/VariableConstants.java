package planning2;

public class VariableConstants {

	@Override
	public String toString() {
		return "VariableConstants [symbolName=" + symbolName + ", free=" + free
				+ "]";
	}

	public static VariableConstants[] variables;

	// CAKE("cake"),
	//
	// SPARE("spare"), AXLE("axle"), TYRE("tyre"), TRUNK("trunk"),
	// GROUND("ground"), FLAT("flat");
	private String symbolName;
	private boolean free;

	VariableConstants(String symbolName, boolean free) {
		this.setSymbolName(symbolName);
		this.free = free;
	}

	public void setSymbolName(String symbolName) {
		this.symbolName = symbolName;
	}

	public String getSymbolName() {
		return symbolName;
	}

	public void setFree(boolean free) {
		this.free = free;
	}

	public boolean isFree() {
		return free;
	}

}
