package com.group3.monitorServer.constraint;

/**
 * The ConstraintKey class is used to associate with a specified
 * {@link Constraint} when it is store in a {@link ConstraintStore}. <br>
 * <br>
 * It uses a combination of an endpoint path definition and an unique node ID to
 * precisely define/identify a {@link Constraint} in the store. Where the nodeID
 * can be omitted to create a general {@link Constraint} for the endpoint
 * instead.
 */
public class ConstraintKey {

	private String endpoint;
	private Integer nodeID;

	/**
	 * Constructor for the ConstraintKey class
	 * 
	 * @param endpoint The path of the endpoint for the Constraint.
	 */
	public ConstraintKey(String endpoint) {
		this(endpoint, null);
	}

	/**
	 * Constructor for the ConstraintKey class
	 * 
	 * @param endpoint The path of the endpoint for the Constraint.
	 * @param nodeID   The ID of the node containing the constrained endpoint.
	 */
	public ConstraintKey(String endpoint, Integer nodeID) {
		this.endpoint = endpoint;
		this.nodeID = nodeID;
	}

	@Override
	public boolean equals(Object obj) {
		// If the other object is not an instance of a ConstraintKey, then they are not
		// equal.
		if (!(obj instanceof ConstraintKey)) {
			return false;
		}
		ConstraintKey other = (ConstraintKey) obj;

		// If the endpoint definition is not the same, then they are not equal.
		if (!this.endpoint.equals(other.getEndpoint())) {
			return false;
		}

		// Check the optional nodeID to check if they are defined and if they are if
		// they are the same.
		if ((this.nodeID == null && other.getNodeID() != null) || (this.nodeID != null && other.getNodeID() == null)
				|| (this.nodeID != other.getNodeID())) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nodeID == null) ? -1 : nodeID.intValue());
		result = prime * result + endpoint.hashCode();
		return result;
	}

	/**
	 * Get the endpoint path of the ConstraintKey.
	 * 
	 * @return The endpoint path of the ConstraintKey.
	 */
	public String getEndpoint() {
		return endpoint;
	}

	/**
	 * Get the node ID of the ConstraintKey.
	 * 
	 * @return The node ID of the ConstraintKey.
	 */
	public Integer getNodeID() {
		return nodeID;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append("[");
		sb.append("endpoint=" + endpoint);
		sb.append(", ");
		sb.append("NodeID=" + nodeID);
		sb.append("]");
		return sb.toString();
	}
}
