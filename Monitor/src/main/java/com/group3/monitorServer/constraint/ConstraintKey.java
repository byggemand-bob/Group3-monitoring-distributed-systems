package com.group3.monitorServer.constraint;

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
	 * @param nodeID The ID of the node containing the constrained endpoint.
	 */
	public ConstraintKey(String endpoint, Integer nodeID) {
		this.endpoint = endpoint;
		this.nodeID = nodeID;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ConstraintKey)) {
			return false;
		}
		ConstraintKey other = (ConstraintKey) obj;
		
		if (!this.endpoint.equals(other.getEndpoint())) {
			return false;
		}
		
		if ((this.nodeID == null && other.getNodeID() != null) ||
				(this.nodeID != null && other.getNodeID() == null) ||
				(this.nodeID != other.getNodeID())) {
			return false;
		}
		
		return true;
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

}
