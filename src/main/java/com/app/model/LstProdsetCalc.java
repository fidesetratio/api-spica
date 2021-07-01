package com.app.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class LstProdsetCalc implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7710840000761454430L;
	
	private Integer pset_id;
	private Integer lsbs_id;
	private Integer lsdbs_number;
	private String lku_id;
	private Integer flag_factor_premium;
	private BigDecimal min_premium;
	private BigDecimal max_premium;
	private Integer flag_minup;
	private Integer flag_maxup;
	private BigDecimal min_up;
	private BigDecimal max_up;
	private Integer lpf_id_min_up;
	private Integer lpf_id_max_up;
	private BigDecimal min_topup;
	private Long biaya_admin_ulink;
	private Integer lpf_Id_min_premi;
	private Integer lpf_id_max_premi;
	private Integer lpf_coi_rider;
	private Integer lpf_coi_rider_tambahan;
	private Integer lpf_policy_value;
	private Integer flag_min_premipokok;
	private Integer lpf_id_min_premipokok;
	private Integer min_premipokok_persen;
	
	public Integer getPset_id() {
		return pset_id;
	}
	public void setPset_id(Integer pset_id) {
		this.pset_id = pset_id;
	}
	public String getLku_id() {
		return lku_id;
	}
	public void setLku_id(String lku_id) {
		this.lku_id = lku_id;
	}
	public Integer getFlag_factor_premium() {
		return flag_factor_premium;
	}
	public void setFlag_factor_premium(Integer flag_factor_premium) {
		this.flag_factor_premium = flag_factor_premium;
	}
	public BigDecimal getMin_premium() {
		return min_premium;
	}
	public void setMin_premium(BigDecimal min_premium) {
		this.min_premium = min_premium;
	}
	public BigDecimal getMax_premium() {
		return max_premium;
	}
	public void setMax_premium(BigDecimal max_premium) {
		this.max_premium = max_premium;
	}
	public Integer getFlag_minup() {
		return flag_minup;
	}
	public void setFlag_minup(Integer flag_minup) {
		this.flag_minup = flag_minup;
	}
	public Integer getFlag_maxup() {
		return flag_maxup;
	}
	public void setFlag_maxup(Integer flag_maxup) {
		this.flag_maxup = flag_maxup;
	}
	public BigDecimal getMin_up() {
		return min_up;
	}
	public void setMin_up(BigDecimal min_up) {
		this.min_up = min_up;
	}
	public BigDecimal getMax_up() {
		return max_up;
	}
	public void setMax_up(BigDecimal max_up) {
		this.max_up = max_up;
	}
	public Integer getLpf_id_min_up() {
		return lpf_id_min_up;
	}
	public void setLpf_id_min_up(Integer lpf_id_min_up) {
		this.lpf_id_min_up = lpf_id_min_up;
	}
	public Integer getLpf_id_max_up() {
		return lpf_id_max_up;
	}
	public void setLpf_id_max_up(Integer lpf_id_max_up) {
		this.lpf_id_max_up = lpf_id_max_up;
	}
	public BigDecimal getMin_topup() {
		return min_topup;
	}
	public void setMin_topup(BigDecimal min_topup) {
		this.min_topup = min_topup;
	}
	public Long getBiaya_admin_ulink() {
		return biaya_admin_ulink;
	}
	public void setBiaya_admin_ulink(Long biaya_admin_ulink) {
		this.biaya_admin_ulink = biaya_admin_ulink;
	}
	public Integer getLpf_Id_min_premi() {
		return lpf_Id_min_premi;
	}
	public void setLpf_Id_min_premi(Integer lpf_Id_min_premi) {
		this.lpf_Id_min_premi = lpf_Id_min_premi;
	}
	public Integer getLpf_id_max_premi() {
		return lpf_id_max_premi;
	}
	public void setLpf_id_max_premi(Integer lpf_id_max_premi) {
		this.lpf_id_max_premi = lpf_id_max_premi;
	}
	public Integer getLpf_coi_rider() {
		return lpf_coi_rider;
	}
	public void setLpf_coi_rider(Integer lpf_coi_rider) {
		this.lpf_coi_rider = lpf_coi_rider;
	}
	public Integer getLpf_coi_rider_tambahan() {
		return lpf_coi_rider_tambahan;
	}
	public void setLpf_coi_rider_tambahan(Integer lpf_coi_rider_tambahan) {
		this.lpf_coi_rider_tambahan = lpf_coi_rider_tambahan;
	}
	public Integer getLpf_policy_value() {
		return lpf_policy_value;
	}
	public void setLpf_policy_value(Integer lpf_policy_value) {
		this.lpf_policy_value = lpf_policy_value;
	}
	public Integer getFlag_min_premipokok() {
		return flag_min_premipokok;
	}
	public void setFlag_min_premipokok(Integer flag_min_premipokok) {
		this.flag_min_premipokok = flag_min_premipokok;
	}
	public Integer getLpf_id_min_premipokok() {
		return lpf_id_min_premipokok;
	}
	public void setLpf_id_min_premipokok(Integer lpf_id_min_premipokok) {
		this.lpf_id_min_premipokok = lpf_id_min_premipokok;
	}
	public Integer getMin_premipokok_persen() {
		return min_premipokok_persen;
	}
	public void setMin_premipokok_persen(Integer min_premipokok_persen) {
		this.min_premipokok_persen = min_premipokok_persen;
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
