package com.harman.rtnm.model.response.clientresponse;

import java.util.ArrayList;
import java.util.List;

public class ClientResponse {
	private String timestamp;

	private List<Node> nodes = new ArrayList<>();

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}



	@Override
	public String toString() {
		return "ClientResponse [timestamp=" + timestamp + ", nodes=" + nodes + "]";
	}

	
	
}
