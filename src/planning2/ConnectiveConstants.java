package planning2;

public enum ConnectiveConstants {

	OR("OR", false), AND("AND", false), NOT("NOT", true);

	private String connectiveName;
	private boolean singleArgument;

	ConnectiveConstants(String connectiveName, boolean singleArgument) {
		this.setConnectiveName(connectiveName);
		this.setSingleArgument(singleArgument);
	}

	public boolean truthtable(boolean value1, boolean value2) {
		if (this.equals(OR)) {
			return value1 | value2;
		} else if (this.equals(AND)) {
			return value1 & value2;
		} else {
			return value1;
		}
	}

	public void setConnectiveName(String connectiveName) {
		this.connectiveName = connectiveName;
	}

	public String getConnectiveName() {
		return connectiveName;
	}

	public void setSingleArgument(boolean singleArgument) {
		this.singleArgument = singleArgument;
	}

	public boolean isSingleArgument() {
		return singleArgument;
	}
}
