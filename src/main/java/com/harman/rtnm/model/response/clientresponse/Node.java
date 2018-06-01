package com.harman.rtnm.model.response.clientresponse;

import java.util.List;
import java.util.Map;

import com.harman.dyns.model.common.Attribute;

public class Node {

	private String nodeName;
	private String SubElementID;
	private List<Map<String, Object>> attributes;
	private List<Node> nodes;

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getSubElementID() {
		return SubElementID;
	}

	public void setSubElementID(String subElementID) {
		SubElementID = subElementID;
	}

	public List<Map<String, Object>> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Map<String, Object>> attributes) {
		this.attributes = attributes;
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}

	@Override
	public String toString() {
		return "Node [nodeName=" + nodeName + ", SubElementID=" + SubElementID + ", attributes=" + attributes
				+ ", nodes=" + nodes + "]";
	}

}
