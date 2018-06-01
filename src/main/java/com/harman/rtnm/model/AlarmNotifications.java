package com.harman.rtnm.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ALARM_NOTIFCATIONS")
public class AlarmNotifications {

	@Column(name="SEVERITY")
	private int severity;
	
	@Column(name="OWNER")
	private String owner;
	
	@Id
	@Column(name="TICKET_NUMBER")
	private String ticketNo;
	
	@Column(name="ACKNOWLEDGED")
	private String acknowledged;
	
	@Column(name="CLASS_NAME")
	private String className;
	
	@Column(name="PROPERTY_NAME")
	private String propertyName;
	
	@Column(name="EVENT_NAME")
	private String eventName;
	
	@Column(name="SOURCE")
	private String source;
	
	@Column(name="OCCURRENCE")
	private String occurrence;
	
	@Column(name="FIRST_NOTIFIED")
	private Date firstNotify;
	
	@Column(name="LAST_NOTIFIED")
	private Date lastChanged;

	public int getSeverity() {
		return severity;
	}

	public void setSeverity(int severity) {
		this.severity = severity;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getTicketNo() {
		return ticketNo;
	}

	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}

	public String getAcknowledged() {
		return acknowledged;
	}

	public void setAcknowledged(String acknowledged) {
		this.acknowledged = acknowledged;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getOccurrence() {
		return occurrence;
	}

	public void setOccurrence(String occurrence) {
		this.occurrence = occurrence;
	}

	public Date getFirstNotify() {
		return firstNotify;
	}

	public void setFirstNotify(Date firstNotify) {
		this.firstNotify = firstNotify;
	}

	public Date getLastChanged() {
		return lastChanged;
	}

	public void setLastChanged(Date lastChanged) {
		this.lastChanged = lastChanged;
	}

	public AlarmNotifications(int severity, String owner, String ticketNo, String acknowledged, String className,
			String propertyName, String eventName, String source, String occurrence, Date firstNotify,
			Date lastChanged) {
		this.severity = severity;
		this.owner = owner;
		this.ticketNo = ticketNo;
		this.acknowledged = acknowledged;
		this.className = className;
		this.propertyName = propertyName;
		this.eventName = eventName;
		this.source = source;
		this.occurrence = occurrence;
		this.firstNotify = firstNotify;
		this.lastChanged = lastChanged;
	}

	public AlarmNotifications() {
	}
	

	
}
