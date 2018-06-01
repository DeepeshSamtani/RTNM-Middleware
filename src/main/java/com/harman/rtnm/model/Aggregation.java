package com.harman.rtnm.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "AGGREGATION")
@Entity
public class Aggregation implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6957412797591614848L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private int id;
	
	@Column(name = "HOUR_SUM")
	private int hourSum;
	
	@Column(name = "HOUR_MIN")
	private int hourMin;
	
	@Column(name = "HOUR_MAX")
	private int hourMax;
	
	@Column(name = "HOUR_AVG")
	private int hourAvg;
	
	@Column(name = "HOUR_FORMULA")
	private String hourFormula;
	
	@Column(name = "DAY_SUM")
	private int daySum;
	
	@Column(name = "DAY_MIN")
	private int dayMin;
	
	@Column(name = "DAY_MAX")
	private int dayMax;
	
	@Column(name = "DAY_AVG")
	private int dayAvg;
	
	@Column(name = "DAY_FORMULA")
	private String dayFormula;
	
	@Column(name = "WEEK_SUM")
	private int weekSum;
	
	@Column(name = "WEEK_MIN")
	private int weekMin;
	
	@Column(name = "WEEK_MAX")
	private int weekMax;
	
	@Column(name = "WEEK_AVG")
	private int weekAvg;
	
	@Column(name = "WEEK_FORMULA")
	private String weekFormula;
	
	@Column(name = "MONTH_SUM")
	private int monthSum;
	
	@Column(name = "MONTH_MIN")
	private int monthMin;
	
	@Column(name = "MONTH_MAX")
	private int monthMax;
	
	@Column(name = "MONTH_AVG")
	private int monthAvg;
	
	@Column(name = "MONTH_FORMULA")
	private String monthFormula;
	
	@Column(name = "YEAR_SUM")
	private int yearSum;
	
	@Column(name = "YEAR_MIN")
	private int yearMin;
	
	@Column(name = "YEAR_MAX")
	private int yearMax;
	
	@Column(name = "YEAR_AVG")
	private int yearAvg;
	
	@Column(name = "YEAR_FORMULA")
	private String yearFormula;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getHourSum() {
		return hourSum;
	}

	public void setHourSum(int hourSum) {
		this.hourSum = hourSum;
	}

	public int getHourMin() {
		return hourMin;
	}

	public void setHourMin(int hourMin) {
		this.hourMin = hourMin;
	}

	public int getHourMax() {
		return hourMax;
	}

	public void setHourMax(int hourMax) {
		this.hourMax = hourMax;
	}

	public int getHourAvg() {
		return hourAvg;
	}

	public void setHourAvg(int hourAvg) {
		this.hourAvg = hourAvg;
	}

	public String getHourFormula() {
		return hourFormula;
	}

	public void setHourFormula(String hourFormula) {
		this.hourFormula = hourFormula;
	}

	public int getDaySum() {
		return daySum;
	}

	public void setDaySum(int daySum) {
		this.daySum = daySum;
	}

	public int getDayMin() {
		return dayMin;
	}

	public void setDayMin(int dayMin) {
		this.dayMin = dayMin;
	}

	public int getDayMax() {
		return dayMax;
	}

	public void setDayMax(int dayMax) {
		this.dayMax = dayMax;
	}

	public int getDayAvg() {
		return dayAvg;
	}

	public void setDayAvg(int dayAvg) {
		this.dayAvg = dayAvg;
	}

	public String getDayFormula() {
		return dayFormula;
	}

	public void setDayFormula(String dayFormula) {
		this.dayFormula = dayFormula;
	}

	public int getWeekSum() {
		return weekSum;
	}

	public void setWeekSum(int weekSum) {
		this.weekSum = weekSum;
	}

	public int getWeekMin() {
		return weekMin;
	}

	public void setWeekMin(int weekMin) {
		this.weekMin = weekMin;
	}

	public int getWeekMax() {
		return weekMax;
	}

	public void setWeekMax(int weekMax) {
		this.weekMax = weekMax;
	}

	public int getWeekAvg() {
		return weekAvg;
	}

	public void setWeekAvg(int weekAvg) {
		this.weekAvg = weekAvg;
	}

	public String getWeekFormula() {
		return weekFormula;
	}

	public void setWeekFormula(String weekFormula) {
		this.weekFormula = weekFormula;
	}

	public int getMonthSum() {
		return monthSum;
	}

	public void setMonthSum(int monthSum) {
		this.monthSum = monthSum;
	}

	public int getMonthMin() {
		return monthMin;
	}

	public void setMonthMin(int monthMin) {
		this.monthMin = monthMin;
	}

	public int getMonthMax() {
		return monthMax;
	}

	public void setMonthMax(int monthMax) {
		this.monthMax = monthMax;
	}

	public int getMonthAvg() {
		return monthAvg;
	}

	public void setMonthAvg(int monthAvg) {
		this.monthAvg = monthAvg;
	}

	public String getMonthFormula() {
		return monthFormula;
	}

	public void setMonthFormula(String monthFormula) {
		this.monthFormula = monthFormula;
	}

	public int getYearSum() {
		return yearSum;
	}

	public void setYearSum(int yearSum) {
		this.yearSum = yearSum;
	}

	public int getYearMin() {
		return yearMin;
	}

	public void setYearMin(int yearMin) {
		this.yearMin = yearMin;
	}

	public int getYearMax() {
		return yearMax;
	}

	public void setYearMax(int yearMax) {
		this.yearMax = yearMax;
	}

	public int getYearAvg() {
		return yearAvg;
	}

	public void setYearAvg(int yearAvg) {
		this.yearAvg = yearAvg;
	}

	public String getYearFormula() {
		return yearFormula;
	}

	public void setYearFormula(String yearFormula) {
		this.yearFormula = yearFormula;
	}

	@Override
	public String toString() {
		return "Aggregation [id=" + id + ", hourSum=" + hourSum + ", hourMin=" + hourMin + ", hourMax=" + hourMax
				+ ", hourAvg=" + hourAvg + ", hourFormula=" + hourFormula + ", daySum=" + daySum + ", dayMin=" + dayMin
				+ ", dayMax=" + dayMax + ", dayAvg=" + dayAvg + ", dayFormula=" + dayFormula + ", weekSum=" + weekSum
				+ ", weekMin=" + weekMin + ", weekMax=" + weekMax + ", weekAvg=" + weekAvg + ", weekFormula="
				+ weekFormula + ", monthSum=" + monthSum + ", monthMin=" + monthMin + ", monthMax=" + monthMax
				+ ", monthAvg=" + monthAvg + ", monthFormula=" + monthFormula + ", yearSum=" + yearSum + ", yearMin="
				+ yearMin + ", yearMax=" + yearMax + ", yearAvg=" + yearAvg + ", yearFormula=" + yearFormula + "]";
	}
}
