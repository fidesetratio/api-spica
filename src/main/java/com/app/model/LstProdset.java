package com.app.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class LstProdset implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4482232594448702707L;
	
	private Integer pset_id;
	private Integer lsbs_id;
	private Integer lsdbs_number;
	private String no_sk;
	private Integer jenis_app;
	private Integer flag_ulink;
	private Integer flag_rider;
	private Integer flag_rider_add;
	private Integer flag_rider_med;
	private Integer flag_lock_uppremi;
	private Integer flag_active;
	private Date date_active;
	private Integer insured_period_flag;
	private BigDecimal insured_period;
	private Integer lpf_id_insured_period;
	private Integer pay_period_flag;
	private Integer pay_period;
	private Integer lpf_id_pay_period;
	private Integer holder_age_from;
	private Integer holder_age_to;
	private Integer insured_age_from_flag;
	private Integer insured_age_from;
	private Integer insured_age_to;
	private Integer premium_holiday;
	private Integer premium_holiday_flag;
	private Integer flag_age_validation;
	private Integer flag_invest_fund;
	private Integer pay_mode_list;
	private Integer flag_currency;
	private Integer flag_komb_premi;
	private Integer flag_dependent;
	private Integer flag_gender;
	private Integer flag_hcode;
	private Integer holder_age_flag;
	private Integer holder_age_lpf_id;
	private Integer proposal_format_id;
	private Integer product_classification;
	private Integer flag_cover_pp;
	private Integer flag_cover_tu;
	private Integer flag_cover_tt;
	
	public Integer getPset_id() {
		return pset_id;
	}
	public void setPset_id(Integer pset_id) {
		this.pset_id = pset_id;
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
	public String getNo_sk() {
		return no_sk;
	}
	public void setNo_sk(String no_sk) {
		this.no_sk = no_sk;
	}
	public Integer getJenis_app() {
		return jenis_app;
	}
	public void setJenis_app(Integer jenis_app) {
		this.jenis_app = jenis_app;
	}
	public Integer getFlag_ulink() {
		return flag_ulink;
	}
	public void setFlag_ulink(Integer flag_ulink) {
		this.flag_ulink = flag_ulink;
	}
	public Integer getFlag_rider() {
		return flag_rider;
	}
	public void setFlag_rider(Integer flag_rider) {
		this.flag_rider = flag_rider;
	}
	public Integer getFlag_rider_add() {
		return flag_rider_add;
	}
	public void setFlag_rider_add(Integer flag_rider_add) {
		this.flag_rider_add = flag_rider_add;
	}
	public Integer getFlag_rider_med() {
		return flag_rider_med;
	}
	public void setFlag_rider_med(Integer flag_rider_med) {
		this.flag_rider_med = flag_rider_med;
	}
	public Integer getFlag_lock_uppremi() {
		return flag_lock_uppremi;
	}
	public void setFlag_lock_uppremi(Integer flag_lock_uppremi) {
		this.flag_lock_uppremi = flag_lock_uppremi;
	}
	public Integer getFlag_active() {
		return flag_active;
	}
	public void setFlag_active(Integer flag_active) {
		this.flag_active = flag_active;
	}
	public Date getDate_active() {
		return date_active;
	}
	public void setDate_active(Date date_active) {
		this.date_active = date_active;
	}
	public Integer getInsured_period_flag() {
		return insured_period_flag;
	}
	public void setInsured_period_flag(Integer insured_perior_flag) {
		this.insured_period_flag = insured_perior_flag;
	}
	public BigDecimal getInsured_period() {
		return insured_period;
	}
	public void setInsured_period(BigDecimal insured_period) {
		this.insured_period = insured_period;
	}
	public Integer getLpf_id_insured_period() {
		return lpf_id_insured_period;
	}
	public void setLpf_id_insured_period(Integer lpf_id_insured_period) {
		this.lpf_id_insured_period = lpf_id_insured_period;
	}
	public Integer getPay_period_flag() {
		return pay_period_flag;
	}
	public void setPay_period_flag(Integer pay_period_flag) {
		this.pay_period_flag = pay_period_flag;
	}
	public Integer getPay_period() {
		return pay_period;
	}
	public void setPay_period(Integer pay_period) {
		this.pay_period = pay_period;
	}
	public Integer getLpf_id_pay_period() {
		return lpf_id_pay_period;
	}
	public void setLpf_id_pay_period(Integer lpf_id_pay_period) {
		this.lpf_id_pay_period = lpf_id_pay_period;
	}
	public Integer getHolder_age_from() {
		return holder_age_from;
	}
	public void setHolder_age_from(Integer holder_age_from) {
		this.holder_age_from = holder_age_from;
	}
	public Integer getHolder_age_to() {
		return holder_age_to;
	}
	public void setHolder_age_to(Integer holder_age_to) {
		this.holder_age_to = holder_age_to;
	}
	public Integer getInsured_age_from_flag() {
		return insured_age_from_flag;
	}
	public void setInsured_age_from_flag(Integer insured_age_from_flag) {
		this.insured_age_from_flag = insured_age_from_flag;
	}
	public Integer getInsured_age_from() {
		return insured_age_from;
	}
	public void setInsured_age_from(Integer insured_age_from) {
		this.insured_age_from = insured_age_from;
	}
	public Integer getInsured_age_to() {
		return insured_age_to;
	}
	public void setInsured_age_to(Integer insured_age_to) {
		this.insured_age_to = insured_age_to;
	}
	public Integer getPremium_holiday() {
		return premium_holiday;
	}
	public void setPremium_holiday(Integer premium_holiday) {
		this.premium_holiday = premium_holiday;
	}
	public Integer getPremium_holiday_flag() {
		return premium_holiday_flag;
	}
	public void setPremium_holiday_flag(Integer premium_holiday_flag) {
		this.premium_holiday_flag = premium_holiday_flag;
	}
	public Integer getFlag_age_validation() {
		return flag_age_validation;
	}
	public void setFlag_age_validation(Integer flag_age_validation) {
		this.flag_age_validation = flag_age_validation;
	}
	public Integer getFlag_invest_fund() {
		return flag_invest_fund;
	}
	public void setFlag_invest_fund(Integer flag_invest_fund) {
		this.flag_invest_fund = flag_invest_fund;
	}
	public Integer getPay_mode_list() {
		return pay_mode_list;
	}
	public void setPay_mode_list(Integer pay_mode_list) {
		this.pay_mode_list = pay_mode_list;
	}
	public Integer getFlag_currency() {
		return flag_currency;
	}
	public void setFlag_currency(Integer flag_currency) {
		this.flag_currency = flag_currency;
	}
	public Integer getFlag_komb_premi() {
		return flag_komb_premi;
	}
	public void setFlag_komb_premi(Integer flag_komb_premi) {
		this.flag_komb_premi = flag_komb_premi;
	}
	public Integer getFlag_dependent() {
		return flag_dependent;
	}
	public void setFlag_dependent(Integer flag_dependent) {
		this.flag_dependent = flag_dependent;
	}
	public Integer getFlag_gender() {
		return flag_gender;
	}
	public void setFlag_gender(Integer flag_gender) {
		this.flag_gender = flag_gender;
	}
	public Integer getFlag_hcode() {
		return flag_hcode;
	}
	public void setFlag_hcode(Integer flag_hcode) {
		this.flag_hcode = flag_hcode;
	}
	public Integer getHolder_age_flag() {
		return holder_age_flag;
	}
	public void setHolder_age_flag(Integer holder_age_flag) {
		this.holder_age_flag = holder_age_flag;
	}
	public Integer getHolder_age_lpf_id() {
		return holder_age_lpf_id;
	}
	public void setHolder_age_lpf_id(Integer holder_age_lpf_id) {
		this.holder_age_lpf_id = holder_age_lpf_id;
	}
	public Integer getProposal_format_id() {
		return proposal_format_id;
	}
	public void setProposal_format_id(Integer proposal_format_id) {
		this.proposal_format_id = proposal_format_id;
	}
	public Integer getProduct_classification() {
		return product_classification;
	}
	public void setProduct_classification(Integer product_classification) {
		this.product_classification = product_classification;
	}
	public Integer getFlag_cover_pp() {
		return flag_cover_pp;
	}
	public void setFlag_cover_pp(Integer flag_cover_pp) {
		this.flag_cover_pp = flag_cover_pp;
	}
	public Integer getFlag_cover_tu() {
		return flag_cover_tu;
	}
	public void setFlag_cover_tu(Integer flag_cover_tu) {
		this.flag_cover_tu = flag_cover_tu;
	}
	public Integer getFlag_cover_tt() {
		return flag_cover_tt;
	}
	public void setFlag_cover_tt(Integer flag_cover_tt) {
		this.flag_cover_tt = flag_cover_tt;
	}

}
