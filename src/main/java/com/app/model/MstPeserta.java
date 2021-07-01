package com.app.model;

import java.io.Serializable;
import java.util.Date;

public class MstPeserta implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7527950540848563577L;
	
	private String reg_spaj;
	private Integer lsre_id;
	private String nama;
	private Date tgl_lahir;
	private Integer kelamin;
	private Integer umur;
	private Long premi;
	private Integer lsbs_id;
	private Integer lsdbs_number;
	
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public Integer getLsre_id() {
		return lsre_id;
	}
	public void setLsre_id(Integer lsre_id) {
		this.lsre_id = lsre_id;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public Date getTgl_lahir() {
		return tgl_lahir;
	}
	public void setTgl_lahir(Date tgl_lahir) {
		this.tgl_lahir = tgl_lahir;
	}
	public Integer getKelamin() {
		return kelamin;
	}
	public void setKelamin(Integer kelamin) {
		this.kelamin = kelamin;
	}
	public Integer getUmur() {
		return umur;
	}
	public void setUmur(Integer umur) {
		this.umur = umur;
	}
	public Long getPremi() {
		return premi;
	}
	public void setPremi(Long premi) {
		this.premi = premi;
	}
	public Integer getLsbs_id() {
		return lsbs_id;
	}
	public void setLsbs_id(Integer lsbs_id) {
		this.lsbs_id = lsbs_id;
	}
	public Integer getLsdbs_number() {
		return lsdbs_number;
	}
	public void setLsdbs_number(Integer lsdbs_number) {
		this.lsdbs_number = lsdbs_number;
	}

}
