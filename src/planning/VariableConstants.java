package planning;

public enum VariableConstants {

	CAKE("cake"),

	SPARE("spare"), AXLE("axle"), TYRE("tyre"), TRUNK("trunk"), GROUND("ground"), FLAT(
			"flat");
	private String symbolName;

	VariableConstants(String symbolName) {
		this.setSymbolName(symbolName);
	}

	public void setSymbolName(String symbolName) {
		this.symbolName = symbolName;
	}

	public String getSymbolName() {
		return symbolName;
	}

}
