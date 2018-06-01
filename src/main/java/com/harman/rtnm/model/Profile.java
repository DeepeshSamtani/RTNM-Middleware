package com.harman.rtnm.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Table(name="PROFILE")
@Entity
public class Profile implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1223546912212330573L;

	@Id
	@Column(name = "PROFILE_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long profileId;
	
	@Column(name="PROFILE_NAME")
	private String profileName;

	@Column(name="COMMUNITY_STRING")
	private String communityString;
	
	@Column(name="DATA_ACCESS")
	private String dataAccess;

	@ManyToMany(cascade = { CascadeType.PERSIST }, fetch = FetchType.EAGER)
	@JoinTable(name = "PROFILE_ACCESS", joinColumns = @JoinColumn(name = "PROFILE_ID"), inverseJoinColumns = @JoinColumn(name = "ACCESS_ID"))
	private List<Access> accesses;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinTable(name = "PROFILE_DASHBOARD", joinColumns = @JoinColumn(name = "PROFILE_ID"), inverseJoinColumns = @JoinColumn(name = "DASHBOARD_ID",unique = false),uniqueConstraints = {@UniqueConstraint(columnNames={"DASHBOARD_ID", "PROFILE_ID"})} )
	private List<Dashboard> dashboards;

	
	@OneToMany(fetch = FetchType.EAGER ,cascade=CascadeType.ALL )
	@JoinTable(name="APPLICABLE_PROFILES",
	joinColumns={@JoinColumn(name="APPLICABLE_PROFILE_ID")},
	inverseJoinColumns={@JoinColumn(name="PROFILE_ID")})
	private Set<Profile> applicableProfiles = new HashSet<Profile>();

	public Long getProfileId() {
		return profileId;
	}

	public void setProfileId(Long profileId) {
		this.profileId = profileId;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public String getCommunityString() {
		return communityString;
	}

	public void setCommunityString(String communityString) {
		this.communityString = communityString;
	}

	public String getDataAccess() {
		return dataAccess;
	}

	public void setDataAccess(String dataAccess) {
		this.dataAccess = dataAccess;
	}

	public List<Access> getAccesses() {
		return accesses;
	}

	public void setAccesses(List<Access> accesses) {
		this.accesses = accesses;
	}

	public List<Dashboard> getDashboards() {
		return dashboards;
	}

	public void setDashboards(List<Dashboard> dashboards) {
		this.dashboards = dashboards;
	}

	public Set<Profile> getApplicableProfiles() {
		return applicableProfiles;
	}

	public void setApplicableProfiles(Set<Profile> applicableProfiles) {
		this.applicableProfiles = applicableProfiles;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	


	
	
}
