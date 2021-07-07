package com.app.services;

import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import com.app.mapper.dao.SpicaDAO;
import com.app.model.Account_recur;
import com.app.model.AgentValidationProfile;
import com.app.model.BeneficiaryRelationValidProfile;
import com.app.model.DepositPremium;
import com.app.model.DocumentStatus;
import com.app.model.GenderValidationProfile;
import com.app.model.HighRiskCountriesPp;
import com.app.model.LstDetBisnis;
import com.app.model.LstProdset;
import com.app.model.LstProdsetCalc;
import com.app.model.LstSpicaCondition;
import com.app.model.LstSpicaField;
import com.app.model.LstSpicaForm;
import com.app.model.LstSpicaList;
import com.app.model.LstSpicaPf;
import com.app.model.LstSpicaRule;
import com.app.model.LstSpicaVariable;
import com.app.model.LstValidationAgeParticipant;
import com.app.model.MedicalTableProfile;
import com.app.model.MstPeserta;
import com.app.model.MstProductInsured;
import com.app.model.MstSpicaConditionHistory;
import com.app.model.MstSpicaFieldHistory;
import com.app.model.MstSpicaFormHistory;
import com.app.model.MstSpicaRuleHistory;
import com.app.model.MstSpicaVariableHistory;
import com.app.model.MstWfDocument;
import com.app.model.ParticipantValidationProfile;
import com.app.model.Payment;
import com.app.model.PeriodValidationProfile;
import com.app.model.Policy;
import com.app.model.Powersave;
import com.app.model.PremiUlink;
import com.app.model.PremiumValidationProfile;
import com.app.model.Product;
import com.app.model.ProductAgeValidationProfile;
import com.app.model.SumInsuredValidationProfile;
import com.app.model.TopUpSingleValidationProfile;
import com.app.utils.CommonUtils;

@Service
public class SpicaServices {
	
	@Autowired
	private SqlSession sqlSession;
	
	private static SqlSession sqlSession1;
	
	 @PostConstruct
	    public void init() {
		 SpicaServices.sqlSession1 = sqlSession;
	    }
	
	//Insert
	
	public Integer insertLstSpicaForm(String form_type, String form_description, String form_source, String form_primary_attribute, Integer user_id) {
		SpicaDAO dao = sqlSession.getMapper(SpicaDAO.class);
		LstSpicaForm lstSpicaForm = new LstSpicaForm();
		lstSpicaForm.setLspc_form_type(form_type);
		lstSpicaForm.setLspc_form_description(form_description);
		lstSpicaForm.setLspc_form_source(form_source);
		lstSpicaForm.setLspc_form_primary_attribute(form_primary_attribute);
		lstSpicaForm.setLspc_form_created_by(user_id);
		
		dao.insertLstSpicaForm(lstSpicaForm);
		
		return lstSpicaForm.getLspc_form_id();
	}
	
	public void insertFormField(Integer form_id, String form_source, Integer user_id) {
		SpicaDAO dao = sqlSession.getMapper(SpicaDAO.class);
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("lspc_form_id", form_id);
		param.put("lspc_form_source", form_source);
		param.put("lspc_field_created_by", user_id);
		
		dao.insertFormField(param);
	}
	
	public void insertLstSpicaVariable(Integer form_id, String variable_name, String variable_type,
			String variable_description, String variable_expression, Integer user_id) {
		SpicaDAO dao = sqlSession.getMapper(SpicaDAO.class);
		LstSpicaVariable lstSpicaVariable = new LstSpicaVariable();
		lstSpicaVariable.setLspc_form_id(form_id);
		lstSpicaVariable.setLspc_variable_name(variable_name);
		lstSpicaVariable.setLspc_variable_type(variable_type);
		lstSpicaVariable.setLspc_variable_description(variable_description);
		lstSpicaVariable.setLspc_variable_expression(variable_expression);
		lstSpicaVariable.setLspc_variable_created_by(user_id);
		
		dao.insertLstSpicaVariable(lstSpicaVariable);
	}
	
	public void insertLstSpicaRule(Integer form_id, String rule_name, String rule_description, String rule_expression,
			String rule_error_msg, Integer user_id, String rule_prerequisites, Integer rule_category_id) {
		SpicaDAO dao = sqlSession.getMapper(SpicaDAO.class);
		LstSpicaRule lstSpicaRule = new LstSpicaRule();
		lstSpicaRule.setLspc_form_id(form_id);
		lstSpicaRule.setLspc_rule_name(rule_name);
		lstSpicaRule.setLspc_rule_description(rule_description);
		lstSpicaRule.setLspc_rule_expression(rule_expression);
		lstSpicaRule.setLspc_rule_error_msg(rule_error_msg);
		lstSpicaRule.setLspc_rule_created_by(user_id);
		lstSpicaRule.setLspc_rule_prerequisites(rule_prerequisites);
		lstSpicaRule.setLspc_rule_category_id(rule_category_id);
		
		dao.insertLstSpicaRule(lstSpicaRule);
	}
	
	public void insertLstSpicaCondition(Integer form_id, String rule_name, String condition_name, String condition_expression,
			String condition_description, Integer user_id) {
		SpicaDAO dao = sqlSession.getMapper(SpicaDAO.class);
		LstSpicaCondition lstSpicaCondition = new LstSpicaCondition();
		lstSpicaCondition.setLspc_form_id(form_id);
		lstSpicaCondition.setLspc_rule_name(rule_name);
		lstSpicaCondition.setLspc_condition_name(condition_name);
		lstSpicaCondition.setLspc_condition_expression(condition_expression);
		lstSpicaCondition.setLspc_condition_description(condition_description);
		lstSpicaCondition.setLspc_condition_created_by(user_id);
		
		dao.insertLstSpicaCondition(lstSpicaCondition);
		
	}
	
	public void insertLstSpicaList(String list_name, String list_description, String list_source,
			String list_field_name, String list_field_type, Integer user_id) {
		SpicaDAO dao = sqlSession.getMapper(SpicaDAO.class);
		LstSpicaList lstSpicaList = new LstSpicaList();
		lstSpicaList.setLspc_list_name(list_name);
		lstSpicaList.setLspc_list_description(list_description);
		lstSpicaList.setLspc_list_source(list_source);
		lstSpicaList.setLspc_list_field_name(list_field_name);
		lstSpicaList.setLspc_list_field_type(list_field_type);
		lstSpicaList.setLspc_list_created_by(user_id);
		
		dao.insertLstSpicaList(lstSpicaList);
	}
	
	public void insertMstSpicaFormHistory(Integer form_id, String primary_attribute, Date time_started, Date time_finished, Integer user_id, String spica_result, Integer process_number) {
		SpicaDAO dao = sqlSession.getMapper(SpicaDAO.class);
		MstSpicaFormHistory mstSpicaFormHistory = new MstSpicaFormHistory();
		mstSpicaFormHistory.setLspc_form_id(form_id);
		mstSpicaFormHistory.setMspc_form_hist_primary_att(primary_attribute);
		mstSpicaFormHistory.setMspc_form_hist_time_started(time_started);
		mstSpicaFormHistory.setMspc_form_hist_time_finished(time_finished);
		mstSpicaFormHistory.setMspc_form_hist_created_by(user_id);
		mstSpicaFormHistory.setMspc_form_hist_result(spica_result);
		mstSpicaFormHistory.setMspc_form_hist_process_number(process_number);
		
		dao.insertMstSpicaFormHistory(mstSpicaFormHistory);
	}
	
	public void insertMstSpicaFieldHistory(Integer form_id, String primary_attribute, String lspc_field_name, String fieldValue, Integer user_id, Integer process_number) {
		SpicaDAO dao = sqlSession.getMapper(SpicaDAO.class);
		MstSpicaFieldHistory mstSpicaFieldHistory = new MstSpicaFieldHistory();
		mstSpicaFieldHistory.setLspc_form_id(form_id);
		mstSpicaFieldHistory.setMspc_form_hist_primary_att(primary_attribute);
		mstSpicaFieldHistory.setLspc_field_name(lspc_field_name);
		mstSpicaFieldHistory.setMspc_field_hist_value(fieldValue);
		mstSpicaFieldHistory.setMspc_field_hist_created_by(user_id);
		mstSpicaFieldHistory.setMspc_form_hist_process_number(process_number);
		
		dao.insertMstSpicaFieldHistory(mstSpicaFieldHistory);
	}
	
	public void insertMstSpicaVariableHistory(Integer form_id, String primary_attribute, String variable_name, String variable_expression,
			String variable_value, Integer user_id, Integer process_number) {
		SpicaDAO dao = sqlSession.getMapper(SpicaDAO.class);
		MstSpicaVariableHistory mstSpicaVariableHistory = new MstSpicaVariableHistory();
		mstSpicaVariableHistory.setLspc_form_id(form_id);
		mstSpicaVariableHistory.setMspc_form_hist_primary_att(primary_attribute);
		mstSpicaVariableHistory.setLspc_variable_name(variable_name);
		mstSpicaVariableHistory.setLspc_variable_expression(variable_expression);
		mstSpicaVariableHistory.setMspc_variable_hist_value(variable_value);
		mstSpicaVariableHistory.setMspc_variable_hist_created_by(user_id);
		mstSpicaVariableHistory.setMspc_form_hist_process_number(process_number);
		
		dao.insertMstSpicaVariableHistory(mstSpicaVariableHistory);
	}
	
	public void insertMstSpicaRuleHistory(Integer form_id, String primary_attribute, String lspc_rule_name,
			String lspc_rule_expression, String rule_result, Integer user_id, Integer rule_category_id, Integer process_number) {
		SpicaDAO dao = sqlSession.getMapper(SpicaDAO.class);
		MstSpicaRuleHistory mstSpicaRuleHistory = new MstSpicaRuleHistory();
		mstSpicaRuleHistory.setLspc_form_id(form_id);
		mstSpicaRuleHistory.setMspc_form_hist_primary_att(primary_attribute);
		mstSpicaRuleHistory.setLspc_rule_name(lspc_rule_name);
		mstSpicaRuleHistory.setLspc_rule_expression(lspc_rule_expression);
		mstSpicaRuleHistory.setMspc_rule_hist_result(rule_result);
		mstSpicaRuleHistory.setMspc_rule_hist_created_by(user_id);
		mstSpicaRuleHistory.setLspc_rule_category_id(rule_category_id);
		mstSpicaRuleHistory.setMspc_form_hist_process_number(process_number);
		
		dao.insertMstSpicaRuleHistory(mstSpicaRuleHistory);
	}
	
	public void insertMstSpicaConditionHistory(Integer form_id, String primary_attribute, String lspc_rule_name,
			String lspc_condition_name, String lspc_condition_expression, String condition_result, Integer user_id, Integer process_number) {
		SpicaDAO dao = sqlSession.getMapper(SpicaDAO.class);
		MstSpicaConditionHistory mstSpicaConditionHistory = new MstSpicaConditionHistory();
		mstSpicaConditionHistory.setLspc_form_id(form_id);
		mstSpicaConditionHistory.setMspc_form_hist_primary_att(primary_attribute);
		mstSpicaConditionHistory.setLspc_rule_name(lspc_rule_name);
		mstSpicaConditionHistory.setLspc_condition_name(lspc_condition_name);
		mstSpicaConditionHistory.setLspc_condition_expression(lspc_condition_expression);
		mstSpicaConditionHistory.setMspc_condition_hist_result(condition_result);
		mstSpicaConditionHistory.setMspc_condition_hist_created_by(user_id);
		mstSpicaConditionHistory.setMspc_form_hist_process_number(process_number);
		
		dao.insertMstSpicaConditionHistory(mstSpicaConditionHistory);
	}
	
	public void insertMstPositionSpaj(String reg_spaj, Integer lspd_id, Integer lssp_id, Integer lssa_id, Integer lus_id, String description) {
		SpicaDAO dao = sqlSession.getMapper(SpicaDAO.class);
		
		Map param = new HashMap();
		param.put("reg_spaj", reg_spaj);
		param.put("lspd_id", lspd_id);
		param.put("lssp_id", lssp_id);
		param.put("lssa_id", lssa_id);
		param.put("lus_id", lus_id);
		param.put("description", description);
		
		dao.insertMstPositionSpaj(param);

	}
	
	public void insertMstPositionSpajStatic(String reg_spaj, Integer lspd_id, Integer lssp_id, Integer lssa_id, Integer lus_id, String description) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		Map param = new HashMap();
		param.put("reg_spaj", reg_spaj);
		param.put("lspd_id", lspd_id);
		param.put("lssp_id", lssp_id);
		param.put("lssa_id", lssa_id);
		param.put("lus_id", lus_id);
		param.put("description", description);
		
		dao.insertMstPositionSpaj(param);

	}
	
	public void insertMstCntPolis(String lcaId, Integer lsbsId, Long ldNo) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		Map insParam=new HashMap();
		insParam.put("as_cab",lcaId);
		insParam.put("as_bisnis",lsbsId);
		insParam.put("ld_no",ldNo);
		
		dao.insertMstCntPolis(insParam);
	}
	
	public void insertMstPremiUlink(PremiUlink premiUlink) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		dao.insertMstPremiUlink(premiUlink);
	}
	
	public void insertMstBilling(String spaj,Integer liThKe,Integer liPremiKe,Date ldtBegDate,Date ldtEndDate,
			Date ldtDueDate,String lkuId,Double ldecBPolis,Double hcrPolicyCost, Double ttlCardCost,Double stamp,
			Double ldecPremi,Integer liPaid,Integer msbiActive,Integer msbiPrint,Integer addBill,String lusId,Integer lspdId,
			String lcaId,String lwkId,String lsrgId,Integer flagTopup,Integer msbiThBak,Integer msbiPremiBak,Integer msbiPersenPaid,Date tglDebet) throws DataAccessException {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		Map insBill=new HashMap();
		insBill.put("reg_spaj",spaj);
		insBill.put("msbi_tahun_ke",liThKe);
		insBill.put("msbi_premi_ke",liPremiKe);
		insBill.put("msbi_beg_date",ldtBegDate);
		insBill.put("msbi_end_date",ldtEndDate);
		
		insBill.put("msbi_due_date",ldtDueDate);
		insBill.put("msbi_aktif_date",ldtBegDate);
		insBill.put("lku_id",lkuId);
		insBill.put("msbi_policy_cost",ldecBPolis);
		insBill.put("msbi_hcr_policy_cost",hcrPolicyCost);
		
		insBill.put("msbi_ttl_card_cost",ttlCardCost);
		insBill.put("msbi_stamp",stamp);
		insBill.put("msbi_remain",ldecPremi);
		insBill.put("msbi_paid",liPaid);
		insBill.put("msbi_active",msbiActive);
		
		insBill.put("msbi_print",msbiPrint);
		insBill.put("msbi_add_bill",addBill);
		insBill.put("lus_id",lusId);
		insBill.put("lspd_id",lspdId);
		insBill.put("lca_id",lcaId);
		insBill.put("lwk_id",lwkId);
		insBill.put("lsrg_id",lsrgId);
		insBill.put("msbi_flag_topup",flagTopup);
		insBill.put("msbi_th_bak", msbiThBak);
		insBill.put("msbi_premi_bak", msbiPremiBak);
		insBill.put("msbi_persen_paid", msbiPersenPaid);
		insBill.put("tgl_debet",tglDebet);
		
		dao.insertMstBilling(insBill);
	}
	
	public void insertMstDetBillingSpaj(String spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		Map insDetBill=new HashMap();
		insDetBill.put("reg_spaj",spaj);
		
		dao.insertMstDetBillingSpaj(insDetBill);
	}
	
	public void insertMstDetBilling2(String spaj,Integer tahunKe,Integer premiKe,Integer detKe,
			Integer lsbsId,Integer lsdbsNumber, Double premium, Double disc) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		Map param=new HashMap();
		param.put("reg_spaj", spaj);
		param.put("msbi_tahun_ke", tahunKe);
		param.put("msbi_premi_ke", premiKe);
		param.put("msdb_det_ke", detKe);
		param.put("lsbs_id", lsbsId);
		param.put("lsdbs_number", lsdbsNumber);
		param.put("msdb_premium", premium);
		param.put("msdb_discount", disc);
		
		dao.insertMstDetBilling2(param);
	}
	
	public void insertMstPayment(String lsPayId, Double ldecKurs,DepositPremium u) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		Payment insPayment=new Payment();
		insPayment.setMspa_payment_id(lsPayId);
		insPayment.setLku_id(u.getLku_id());
		insPayment.setLsjb_id(u.getLsjb_id());
		insPayment.setClient_bank(u.getClient_bank());
		insPayment.setReg_spaj(u.getReg_spaj());
		insPayment.setMsdp_number(u.getMsdp_number());
		insPayment.setMspa_no_rek(u.getMsdp_no_rek());
		insPayment.setMspa_pay_date(u.getMsdp_pay_date());
		insPayment.setMspa_due_date(u.getMsdp_due_date());
		insPayment.setMspa_date_book(u.getMsdp_date_book());
		insPayment.setMspa_payment(u.getMsdp_payment());
		insPayment.setMspa_input_date(u.getMsdp_input_date());
		insPayment.setMspa_old_policy(u.getMsdp_old_policy());
		insPayment.setMspa_desc(u.getMsdp_desc());
		insPayment.setLus_id(u.getLus_id());
		insPayment.setMspa_active(u.getMsdp_active());
		insPayment.setLsrek_id(u.getLsrek_id());
		insPayment.setMspa_no_pre(u.getMsdp_no_pre());
		insPayment.setMspa_jurnal(u.getMsdp_jurnal());
		insPayment.setMspa_nilai_kurs(ldecKurs);
		insPayment.setMspa_no_voucher(u.getMsdp_no_voucher());
		insPayment.setMspa_flag_merchant(u.getMsdp_flag_merchant());
		
		dao.insertMstPayment(insPayment);
	}
	
	public void insertMstTagPayment(String spaj,String lsPayId,Integer tahunKe,Integer premiKe,DepositPremium u) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		Map insTagPayment=new HashMap();
		insTagPayment.put("reg_spaj",spaj);
		insTagPayment.put("msbi_tahun_ke",tahunKe);
		insTagPayment.put("msbi_premi_ke",premiKe);
		insTagPayment.put("mstp_value",u.getMsdp_payment());
		insTagPayment.put("mspa_payment_id",lsPayId);
		
		dao.insertMstTagPayment(insTagPayment);
	}
	
	public void insertMstTransHistory(Map map) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		dao.insertMstTransHistory(map);
	}
	
	//Select
	
	public LstSpicaForm selectForm(Integer form_id) {
		SpicaDAO dao = sqlSession.getMapper(SpicaDAO.class);
		LstSpicaForm lstSpicaForm = new LstSpicaForm();
		lstSpicaForm.setLspc_form_id(form_id);
		
		return dao.selectForm(lstSpicaForm);
	}

	public ArrayList<LstSpicaField> selectFormField(Integer form_id) {
		SpicaDAO dao = sqlSession.getMapper(SpicaDAO.class);
		LstSpicaField lstSpicaField = new LstSpicaField();
		lstSpicaField.setLspc_form_id(form_id);
		
		return dao.selectFormField(lstSpicaField);
	}
	
	public HashMap<String, Object> getValueField(String select_command, String lspc_form_source,
			String lspc_form_primary_attribute, Object primary_attribute) {
		SpicaDAO dao = sqlSession.getMapper(SpicaDAO.class);
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("select_command", select_command);
		param.put("lspc_form_source", lspc_form_source);
		param.put("lspc_form_primary_attribute", lspc_form_primary_attribute);
		param.put("primary_attribute", primary_attribute);
		
		return dao.getValueField(param);
	}

	public ArrayList<LstSpicaVariable> selectFormVariable(Integer form_id) {
		SpicaDAO dao = sqlSession.getMapper(SpicaDAO.class);
		LstSpicaVariable lstSpicaVariable = new LstSpicaVariable();
		lstSpicaVariable.setLspc_form_id(form_id);
		
		return dao.selectFormVariable(lstSpicaVariable);
	}

	public ArrayList<LstSpicaRule> selectFormRule(Integer form_id) {
		SpicaDAO dao = sqlSession.getMapper(SpicaDAO.class);
		LstSpicaRule lstSpicaRule = new LstSpicaRule();
		lstSpicaRule.setLspc_form_id(form_id);
		
		return dao.selectFormRule(lstSpicaRule);
	}

	public ArrayList<LstSpicaCondition> selectRuleCondition(Integer form_id, String lspc_rule_name) {
		SpicaDAO dao = sqlSession.getMapper(SpicaDAO.class);
		LstSpicaCondition lstSpicaCondition = new LstSpicaCondition();
		lstSpicaCondition.setLspc_form_id(form_id);
		lstSpicaCondition.setLspc_rule_name(lspc_rule_name);
		
		return dao.selectRuleCondition(lstSpicaCondition);
	}

	public LstSpicaVariable checkFormVariableExist(Integer form_id, String variable_name) {
		SpicaDAO dao = sqlSession.getMapper(SpicaDAO.class);
		LstSpicaVariable lstSpicaVariable = new LstSpicaVariable();
		lstSpicaVariable.setLspc_form_id(form_id);
		lstSpicaVariable.setLspc_variable_name(variable_name);
		
		return dao.checkFormVariableExist(lstSpicaVariable);
	}

	public LstSpicaRule checkFormRuleExist(Integer form_id, String rule_name) {
		SpicaDAO dao = sqlSession.getMapper(SpicaDAO.class);
		LstSpicaRule lstSpicaRule = new LstSpicaRule();
		lstSpicaRule.setLspc_form_id(form_id);
		lstSpicaRule.setLspc_rule_name(rule_name);
		
		return dao.checkFormRuleExist(lstSpicaRule);
	}

	public LstSpicaCondition checkFormRuleConditionExist(Integer form_id, String rule_name, String condition_name) {
		SpicaDAO dao = sqlSession.getMapper(SpicaDAO.class);
		LstSpicaCondition lstSpicaCondition = new LstSpicaCondition();
		lstSpicaCondition.setLspc_form_id(form_id);
		lstSpicaCondition.setLspc_rule_name(rule_name);
		lstSpicaCondition.setLspc_condition_name(condition_name);
		
		return dao.checkFormRuleConditionExist(lstSpicaCondition);
	}

	public LstSpicaPf selectPredefinedFunction(String predefinedFunctionName) {
		SpicaDAO dao = sqlSession.getMapper(SpicaDAO.class);
		LstSpicaPf lstSpicaPf = new LstSpicaPf();
		lstSpicaPf.setLspc_pf_name(predefinedFunctionName);
		
		return dao.selectPredefinedFunction(lstSpicaPf);
	}
	
	public LstSpicaList selectList(String listName) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		LstSpicaList lstSpicaList = new LstSpicaList();
		lstSpicaList.setLspc_list_name(listName);
		
		return dao.selectList(lstSpicaList);
	}

	public ArrayList<Object> getListFieldValue(String list_source, String list_field_name, String fieldValue) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("list_source", list_source);
		param.put("list_field_name", list_field_name);
		param.put("fieldValue", fieldValue);
		
		return dao.getListFieldValue(param);
	}
	
	public ArrayList<Object> getListUniqueFieldValue(String list_source, String list_field_name, String fieldValue) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("list_source", list_source);
		param.put("list_field_name", list_field_name);
		param.put("fieldValue", fieldValue);
		
		return dao.getListUniqueFieldValue(param);
	}
	
	public ArrayList<String> selectOfacScreenStatusNotPassed(String reg_spaj, String mofs_type) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("reg_spaj", reg_spaj);
		param.put("mofs_type", mofs_type);
		
		return dao.selectOfacScreenStatusNotPassed(param);
	}
	
	public String selectScreenDescNotPassed(String reg_spaj, String screening_name, String profile_target) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("reg_spaj", reg_spaj);
		param.put("screening_name", screening_name);
		param.put("profile_target", profile_target);
		
		return dao.selectScreenDescNotPassed(param);
	}
	
	public ArrayList<String> selectAllRegSpajInforceByIdSimultan(String mcl_id) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectAllRegSpajInforceByIdSimultan(mcl_id);
	}
	
	public MstProductInsured selectPremiAndKurs(String reg_spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectPremiAndKurs(reg_spaj);
	}
	
	public ArrayList<BeneficiaryRelationValidProfile> selectBeneficiaryRelationValidProfile(String reg_spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectBeneficiaryRelationValidProfile(reg_spaj);
	}
	
	public ArrayList<String> selectYesAnswerFromQuestionnaire(String reg_spaj, Integer option_group) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("reg_spaj", reg_spaj);
		param.put("option_group", option_group);
		
		return dao.selectYesAnswerFromQuestionnaire(param);
	}
	
	public ArrayList<String> selectChurningSpajByPolicyHolder(String mcl_id, Date churningLimitDate) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("mcl_id", mcl_id);
		param.put("churning_limit_date", churningLimitDate);
		
		return dao.selectChurningSpajByPolicyHolder(param);
	}
	
	public ArrayList<String> selectChurningSpajByInsured(String mcl_id, Date churningLimitDate) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("mcl_id", mcl_id);
		param.put("churning_limit_date", churningLimitDate);
		
		return dao.selectChurningSpajByInsured(param);
	}
	
	public MstWfDocument selectDocByDocNumber(String reg_spaj) {
		SpicaDAO dao = sqlSession.getMapper(SpicaDAO.class);
		
		return dao.selectDocByDocNumber(reg_spaj);
	}
	
	public ProductAgeValidationProfile selectProductAgeValidationPP(String reg_spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectProductAgeValidationPP(reg_spaj);
	}
	
	public LstProdset selectProductAgeValidation(Integer lsbs_id, Integer lsdbs_number) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		LstProdset lstProdset = new LstProdset();
		lstProdset.setLsbs_id(lsbs_id);
		lstProdset.setLsdbs_number(lsdbs_number);
		
		return dao.selectProductAgeValidation(lstProdset);
	}
	
	public ProductAgeValidationProfile selectProductAgeValidationTTG(String reg_spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectProductAgeValidationTTG(reg_spaj);
	}
	
	public ArrayList<ProductAgeValidationProfile> selectRiderAgeValidationPP(String reg_spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectRiderAgeValidationPP(reg_spaj);
	}
	
	public LstProdset selectRiderAgeValidation(Integer lsbs_id, Integer lsdbs_number) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		LstProdset lstProdset = new LstProdset();
		lstProdset.setLsbs_id(lsbs_id);
		lstProdset.setLsdbs_number(lsdbs_number);
		
		return dao.selectRiderAgeValidation(lstProdset);
	}
	
	public ArrayList<ProductAgeValidationProfile> selectRiderAgeValidationTTG(String reg_spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectRiderAgeValidationTTG(reg_spaj);
	}
	
	public PeriodValidationProfile selectProductEndDateValidation(String reg_spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectProductEndDateValidation(reg_spaj);
	}
	
	public LstProdset selectInsuredPeriod(Integer lsbs_id, Integer lsdbs_number) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		LstProdset lstProdset = new LstProdset();
		lstProdset.setLsbs_id(lsbs_id);
		lstProdset.setLsdbs_number(lsdbs_number);
		
		return dao.selectInsuredPeriod(lstProdset);
	}
	
	public String selectQueryPerhitungan(String param) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectQueryPerhitungan(param);
	}
	
	public BigDecimal getPerhitungan(Map param) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.getPerhitungan(param);
	}
	
	public PeriodValidationProfile selectProductInsuredPeriodValidation(String reg_spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectProductInsuredPeriodValidation(reg_spaj);
	}
	
	public PeriodValidationProfile selectProductPayPeriodValidation(String reg_spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectProductPayPeriodValidation(reg_spaj);
	}
	
	public PeriodValidationProfile selectProductPremiumHolidayPeriodValidation(String reg_spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectProductPremiumHolidayPeriodValidation(reg_spaj);
	}
	
	public ArrayList<PeriodValidationProfile> selectRiderEndDateValidation(String reg_spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectRiderEndDateValidation(reg_spaj);
	}
	
	public ArrayList<PeriodValidationProfile> selectRiderInsuredPeriodValidation(String reg_spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectRiderInsuredPeriodValidation(reg_spaj);
	}
	
	public MedicalTableProfile selectMedicalTableProfileHolder(String reg_spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectMedicalTableProfileHolder(reg_spaj);
	}
	
	public MedicalTableProfile selectMedicalTableProfileInsured(String reg_spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectMedicalTableProfileInsured(reg_spaj);
	}
	
	public String selectMedicalTableHolder(String reg_spaj, Integer flag_vip, String lku_id, Integer lstb_id,
			Integer mspo_age, Date mste_beg_date, Long sar_medis) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		MedicalTableProfile medicalTableProfile = new MedicalTableProfile();
		medicalTableProfile.setReg_spaj(reg_spaj);
		medicalTableProfile.setFlag_vip(flag_vip);
		medicalTableProfile.setLku_id(lku_id);
		medicalTableProfile.setLstb_id(lstb_id);
		medicalTableProfile.setMspo_age(mspo_age);
		medicalTableProfile.setMste_beg_date(mste_beg_date);
		medicalTableProfile.setSar_medis(sar_medis);
		
		return dao.selectMedicalTableHolder(medicalTableProfile);
	}
	
	public HashMap<String, Object> selectMedicalTableInsured(String reg_spaj, Integer flag_vip, String lku_id, Integer lstb_id, Integer mste_age,
			Date mste_beg_date, Long sar_medis) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		MedicalTableProfile medicalTableProfile = new MedicalTableProfile();
		medicalTableProfile.setReg_spaj(reg_spaj);
		medicalTableProfile.setFlag_vip(flag_vip);
		medicalTableProfile.setLku_id(lku_id);
		medicalTableProfile.setLstb_id(lstb_id);
		medicalTableProfile.setMste_age(mste_age);
		medicalTableProfile.setMste_beg_date(mste_beg_date);
		medicalTableProfile.setSar_medis(sar_medis);
		
		return dao.selectMedicalTableInsured(medicalTableProfile);
	}
	
	public SumInsuredValidationProfile selectProductSumInsuredValidation(String reg_spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectProductSumInsuredValidation(reg_spaj);
	}
	
	public LstProdsetCalc selectProdSetCalc(Integer lsbs_id, Integer lsdbs_number, String lku_id) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		LstProdsetCalc lstProdsetCalc = new LstProdsetCalc();
		lstProdsetCalc.setLsbs_id(lsbs_id);
		lstProdsetCalc.setLsdbs_number(lsdbs_number);
		lstProdsetCalc.setLku_id(lku_id);
		
		return dao.selectProdSetCalc(lstProdsetCalc);
	}
	
	public PremiumValidationProfile selectProductPremiumValidation(String reg_spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectProductPremiumValidation(reg_spaj);
	}
	
	public TopUpSingleValidationProfile selectProductTopUpSingleValidation(String reg_spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectProductTopUpSingleValidation(reg_spaj);
	}
	
	public ArrayList<GenderValidationProfile> selectRiderGenderValidation(String reg_spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectRiderGenderValidation(reg_spaj);
	}
	
	public LstProdset selectFlagGender(Integer lsbs_id, Integer lsdbs_number) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		LstProdset lstProdset = new LstProdset();
		lstProdset.setLsbs_id(lsbs_id);
		lstProdset.setLsdbs_number(lsdbs_number);
		
		return dao.selectFlagGender(lstProdset);
	}
	
	public Long selectRateIdr(String conf_name) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectRateIdr(conf_name);
	}
	
	public ArrayList<ParticipantValidationProfile> selectParticipantAgeValidationProfile(String reg_spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectParticipantAgeValidationProfile(reg_spaj);
	}
	
	public LstValidationAgeParticipant selectLstValidationAgeParticipant(Integer lsbs_id, Integer lsdbs_number,
			Integer lsre_id) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		Map param = new HashMap();
        param.put("rider_id",lsbs_id);
        param.put("rider_number",lsdbs_number);
        param.put("lsre_id",lsre_id);
		
		return dao.selectLstValidationAgeParticipant(param);
	}
	
	public MstProductInsured selectLsbsIdAndLsdbsNumber(String reg_spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectLsbsIdAndLsdbsNumber(reg_spaj);
	}
	
	public ArrayList<String> selectAllRegSpajInforceByIdSimultanInsured(String reg_spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectAllRegSpajInforceByIdSimultanInsured(reg_spaj);
	}
	
	public ArrayList<String> selectAllRegSpajInforceByIdSimultanHolder(String reg_spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectAllRegSpajInforceByIdSimultanHolder(reg_spaj);
	}
	
	public Integer selectLsbsLinebus(String resultSpaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectLsbsLinebus(resultSpaj);
	}
	
	public AgentValidationProfile selectClosingAgentValidProfile(String reg_spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectClosingAgentValidProfile(reg_spaj);
	}
	
	public String selectMsagIdBancass(Integer lsbs_id, Integer lsdbs_number) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		AgentValidationProfile agentValidationProfile = new AgentValidationProfile();
		agentValidationProfile.setLsbs_id(lsbs_id);
		agentValidationProfile.setLsdbs_number(lsdbs_number);
		
		return dao.selectMsagIdBancass(agentValidationProfile);
	}
	
	public String selectMsagId(String msag_id) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectMsagId(msag_id);
	}
	
	public String selectMsagIdAO(String msag_id) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectMsagIdAO(msag_id);
	}
	
	public AgentValidationProfile selectBCAgentValidProfile(String reg_spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectBCAgentValidProfile(reg_spaj);
	}
	
	public AgentValidationProfile selectReferralAgentValidProfile(String reg_spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectReferralAgentValidProfile(reg_spaj);
	}
	
	public String selectLrbId(String lrb_id) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectLrbId(lrb_id);
	}
	
	public MstProductInsured selectPremiKursAndCaraBayar(String resultSpaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectPremiKursAndCaraBayar(resultSpaj);
	}
	
	public AgentValidationProfile selectAOAgentValidProfile(String reg_spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectAOAgentValidProfile(reg_spaj);
	}
	
	public MstProductInsured selectProductGioValid(String reg_spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectProductGioValid(reg_spaj);
	}
	
	public MstProductInsured selectMstProductInsured(String resultSpaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectMstProductInsured(resultSpaj);
	}
	
	public Integer selectMaxFormHistProcessNumber(String reg_spaj) {
		SpicaDAO dao = sqlSession.getMapper(SpicaDAO.class);
		
		return dao.selectMaxFormHistProcessNumber(reg_spaj);
	}
	
	public DocumentStatus selectDocumentStatus(String reg_spaj) {
		SpicaDAO dao = sqlSession.getMapper(SpicaDAO.class);
		
		return dao.selectDocumentStatus(reg_spaj);
	}
	
	public DocumentStatus selectDocumentStatusStatic(String reg_spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectDocumentStatus(reg_spaj);
	}
	
	public Integer selectClaimHistory(String nama_tertanggung, Date dob_tertanggung) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		Map param = new HashMap();
        param.put("nama_tertanggung", "%"+nama_tertanggung+"%");
        param.put("dob_tertanggung", dob_tertanggung);
		
		return dao.selectClaimHistory(param);
	}
	
	public ArrayList<MstProductInsured> selectAllProductInsured(String reg_spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectAllProductInsured(reg_spaj);
	}
	
	public String selectLbsSarPP(String lku_id, Integer lsbs_id) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		Map param = new HashMap();
        param.put("lku_id", lku_id);
        param.put("lsbs_id", lsbs_id);
		
		return dao.selectLbsSarPP(param);
	}
	
	public ArrayList<String> selectAllRegSpajByIdSimultanHolder(String reg_spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectAllRegSpajByIdSimultanHolder(reg_spaj);
	}
	
	public ArrayList<String> selectAllRegSpajByIdSimultanInsured(String reg_spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectAllRegSpajByIdSimultanInsured(reg_spaj);
	}
	
	public String selectRegSpajStatusAcceptHistory(String reg_spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectRegSpajStatusAcceptHistory(reg_spaj);
	}
	
	public ArrayList<HashMap<String, Object>> getSpicaHistory(String reg_spaj) {
		SpicaDAO dao = sqlSession.getMapper(SpicaDAO.class);
		
		return dao.getSpicaHistory(reg_spaj);
	}
	
	public ArrayList<HashMap<String, Object>> getAMLResult(String reg_spaj) {
		SpicaDAO dao = sqlSession.getMapper(SpicaDAO.class);
		
		return dao.getAMLResult(reg_spaj);
	}
	
	public ArrayList<HashMap<String, Object>> getOFACResult(String reg_spaj) {
		SpicaDAO dao = sqlSession.getMapper(SpicaDAO.class);
		
		return dao.getOFACResult(reg_spaj);
	}
	
	public String selectAgentBlacklist() {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectAgentBlacklist();
	}
	
	public ArrayList<HashMap<String, String>> selectDataCustomer(String no_hp, String no_account, String email) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		Map param = new HashMap();
        param.put("no_hp", no_hp);
        param.put("no_account", no_account);
        param.put("email", email);
        
		return dao.selectDataCustomer(param);
	}
	
	public ArrayList<HashMap<String, String>> selectDataAgent(String no_hp, String no_account, String email) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		Map param = new HashMap();
        param.put("no_hp", no_hp);
        param.put("no_account", no_account);
        param.put("email", email);
        
		return dao.selectDataAgent(param);
	}
	
	public Integer selectLstDetBisnisFlagClean(Integer lsbs_id, Integer lsdbs_number) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		Map param = new HashMap();
        param.put("lsbs_id", lsbs_id);
        param.put("lsdbs_number", lsdbs_number);
        
		return dao.selectLstDetBisnisFlagClean(param);
	}
	
	public ArrayList<MstPeserta> selectPeserta(String reg_spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
        
		return dao.selectPeserta(reg_spaj);
	}
	
	public Integer selectFlagAutodebet(String reg_spaj) {
		SpicaDAO dao = sqlSession.getMapper(SpicaDAO.class);
		
		return dao.selectFlagAutodebet(reg_spaj);
	}
	
	public String selectLcaId(String reg_spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectLcaId(reg_spaj);
	}
	
	public String selectPolicyNoFromSpajManualMstTempDMTM(String reg_spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectPolicyNoFromSpajManualMstTempDMTM(reg_spaj);
	}
	
	public Date selectSysdateTrunc() {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectSysdateTrunc();
	}
	
	public Map selectMstCntPolis(String lcaId, Integer lsbsId) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		Map param=new HashMap();
		param.put("as_cab",lcaId);
		param.put("as_bisnis",lsbsId);
		
		return dao.selectMstCntPolis(param);
	}
	
	public String selectMstPolicyRegSpaj(String lsNopol) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectMstPolicyRegSpaj(lsNopol);
	}
	
	public Integer selectMstDetRekruter(String msagId) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectMstDetRekruter(msagId);
	}
	
	public Policy selectDw1Underwriting(String spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		Map param = new HashMap();
		param.put("spaj",spaj);
//		param.put("lspdId",lspdId);
//		param.put("lstbId",lstbId);
		
		return dao.selectDw1Underwriting(param);
	}
	
	public List selectMstDepositPremium(String spaj, Integer flag) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		Map param = new HashMap();
		param.put("spaj",spaj);
		param.put("flag",flag);
		
		return dao.selectMstDepositPremium(param);
	}
	
	public Map selectMstInsuredBegDateEndDate(String spaj, Integer insured) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		Map param=new HashMap();
		param.put("reg_spaj",spaj);
		param.put("mste_insured_no",insured);
		
		return dao.selectMstInsuredBegDateEndDate(param);
	}
	
	public Account_recur select_account_recur(String spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.select_account_recur(spaj);
	}
	
	public Double selectMstProductInsuredPremiSmartSave(String spaj, Integer insured, Integer active) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		Map param=new HashMap();
		param.put("reg_spaj",spaj);
		param.put("mste_insured_no",insured);
		param.put("mspr_active",active);
		
		return dao.selectMstProductInsuredPremiSmartSave(param);
	}
	
	public Double selectMstProductInsuredPremiDiscount(String spaj, Integer insured, Integer active) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		Map param=new HashMap();
		param.put("reg_spaj",spaj);
		param.put("mste_insured_no",insured);
		param.put("mspr_active",active);
		
		return dao.selectMstProductInsuredPremiDiscount(param);
	}
	
	public Double selectMstDefaultMsdefNumeric(Integer liId) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectMstDefaultMsdefNumeric(liId);
	}
	
	public Map selectTertanggung(String reg_spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectTertanggung(reg_spaj);	
	}
	
	public Integer selectJenisTerbitPolis(String reg_spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		Map result = dao.selectJenisTerbitPolis(reg_spaj);	
		
		if(result != null) { 
			return Integer.parseInt(result.get("MSPO_JENIS_TERBIT").toString());
		} else {
			return -1;
		}
	}
	
	public String selectValidasiAutodebetPremiPertama(String reg_spaj) {
		SpicaDAO dao = sqlSession.getMapper(SpicaDAO.class);
		
		return dao.selectValidasiAutodebetPremiPertama(reg_spaj);	
	}
	
	public LstDetBisnis selectLstDetBisnis(Integer lsbs_id, Integer lsdbs_number) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		LstDetBisnis lstDetBisnis = new LstDetBisnis();
		lstDetBisnis.setLsbs_id(lsbs_id);
		lstDetBisnis.setLsdbs_number(lsdbs_number);
		
		return dao.selectLstDetBisnis(lstDetBisnis);	
	}
	
	public Integer selectUmurTertanggungUtama(String reg_spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectUmurTertanggungUtama(reg_spaj);
	}
	
	public String selectBusinessId(String spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectBusinessId(spaj);
	}
	
	public List<Map> selectInfoStableLink(String spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectInfoStableLink(spaj);
	}
	
	public ArrayList<HashMap> selectMstUlinkTopupNewForDetBilling(String spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectMstUlinkTopupNewForDetBilling(spaj);
	}
	
	public List selectDetailBisnis(String reg_spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectDetailBisnis(reg_spaj);
	}
	
	public Integer selectLstPayModeLscbTtlMonth(Integer liPmode) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectLstPayModeLscbTtlMonth(liPmode);
	}
	
	public Double selectLkhKursJual(Map param) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectLkhKursJual(param);
	}
	
	public Double selectLkhKursBeli(Map param) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectLkhKursBeli(param);
	}
	
	public Double selectLkhCurrency(Map param) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectLkhCurrency(param);
	}
	
	public List<Product> selectMstProductInsuredExtra(String spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectMstProductInsuredExtra(spaj);
	}
	
	public List selectLstMerchantFee(Integer flag_merchant) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectLstMerchantFee(flag_merchant);
	}
	
	public Double getBiaAkuisisi(Integer lsbsid,Integer lsdbsNumber,Integer lscbId,Integer liPperiod,Integer tahunKe) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		DecimalFormat f3 = new DecimalFormat ("000");
		
		Map param=new HashMap();
		String kode="116, 118, 159, 140, 160, 138, 153, 217,218";
		String bisnisId=f3.format(lsbsid);
		if(kode.indexOf(bisnisId)>0 && lscbId!=0)
			liPperiod=80;
		if(bisnisId.equals("162") && lscbId!=0)
			liPperiod=88;
		
		param.put("lsbs_id", lsbsid);
		param.put("lsdbs_number", lsdbsNumber);
		param.put("lscb_id", lscbId);
		param.put("lbu_lbayar", liPperiod);
		param.put("tahun_ke", tahunKe);
		
		return dao.getBiaAkuisisi(param);
	}
	
	public Integer selectCountMstBill(String reg_spaj,Integer msbi_tahun_ke , Integer msbi_premi_ke) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		Map map = new HashMap();
		map.put("reg_spaj",reg_spaj);
		map.put("msbi_tahun_ke", msbi_tahun_ke);
		map.put("msbi_premi_ke", msbi_premi_ke);
		
		return dao.selectCountMstBill(map);
	}
	
	public List<Map> selectRiderSave(String spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectRiderSave(spaj);
	}
	
	public Powersave select_powersaver(String spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.select_powersaver(spaj);
	}
	
	public Powersave select_powersaver_baru(String spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.select_powersaver_baru(spaj);
	}
	
	public Double selectSumPremiRiderInMstRiderSave(String spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectSumPremiRiderInMstRiderSave(spaj);
	}
	
	public Double selectRateRider(String lku, int umurTertanggung, int umurPemegang, int lsbs, int jenis) throws DataAccessException {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		int ii_sex_tt_cor = 9;
				
		Map params = new HashMap();
		params.put("lku", lku);
		params.put("umurTertanggung", umurTertanggung);
		params.put("umurPemegang", umurPemegang);
		params.put("lsbs", lsbs);
		params.put("jenis", jenis);
		params.put("sex", ii_sex_tt_cor); /** IGA - PROJECT NEW COR **/
		
		return dao.selectRateRider(params);
	}
	
	public Object selectStatusPaidBilling(String reg_spaj, int msbi_premi_ke, int msbi_tahun_ke) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		Map m = new HashMap();
		m.put("reg_spaj", reg_spaj);
		m.put("msbi_tahun_ke", msbi_tahun_ke);
		m.put("msbi_premi_ke", msbi_premi_ke);
		
		return dao.selectStatusPaidBilling(m);
	}
	
	public Double selectMrsKurangBayarRiderSave(String reg_spaj , String lsbs_id, String lsdbs_number) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		Map param = new HashMap();
		param.put("reg_spaj", reg_spaj);
		param.put("lsbs_id", lsbs_id);
		param.put("lsdbs_number", lsdbs_number);
		
		return dao.selectMrsKurangBayarRiderSave(param);
	}
	
	public Integer validationTopup(String spaj) throws DataAccessException {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		Integer result = dao.validationTopup(spaj);
		if (result == null)
			result = new Integer(-1);
		
		return result;
	}
	
	public Integer selectMaxMstDetBillingDetKe(String spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectMaxMstDetBillingDetKe(spaj);
	}
	
	public String selectSeqPaymentId() {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectSeqPaymentId();
	}
	
	public Date selectSysdate() {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectSysdate();
	}
	
	public Integer selectMstTransHist(String reg_spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectMstTransHist(reg_spaj);
	}
	
	public MstProductInsured selectProdukAkuisisi100Persen(String reg_spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectProdukAkuisisi100Persen(reg_spaj);
	}
	
	public BigDecimal selectPremiumDeficit(String reg_spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectPremiumDeficit(reg_spaj);
	}
	
	public BigDecimal selectTopUpMuKe2(String reg_spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectTopUpMuKe2(reg_spaj);
	}
	
	public PremiumValidationProfile selectPremiPokok(String reg_spaj) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		return dao.selectPremiPokok(reg_spaj);
	}

	
	//Update
	
	public void updateForm(Integer form_id, String form_type, String form_description, String form_source, String form_primary_attribute,
			Integer user_id) {
		SpicaDAO dao = sqlSession.getMapper(SpicaDAO.class);
		LstSpicaForm lstSpicaForm = new LstSpicaForm();
		lstSpicaForm.setLspc_form_id(form_id);
		lstSpicaForm.setLspc_form_type(form_type);
		lstSpicaForm.setLspc_form_description(form_description);
		lstSpicaForm.setLspc_form_source(form_source);
		lstSpicaForm.setLspc_form_primary_attribute(form_primary_attribute);
		lstSpicaForm.setLspc_form_modified_by(user_id);
		
		dao.updateForm(lstSpicaForm);
	}
	
	public void updateFormField(Integer form_id, String form_source, Integer user_id) {
		SpicaDAO dao = sqlSession.getMapper(SpicaDAO.class);
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("lspc_form_id", form_id);
		param.put("lspc_form_source", form_source);
		param.put("lspc_field_modified_by", user_id);
		
		dao.updateFormField(param);
	}
	
	public void updateLstSpicaVariable(Integer form_id, String variable_name, String variable_type,
			String variable_description, String variable_expression, Integer user_id) {
		SpicaDAO dao = sqlSession.getMapper(SpicaDAO.class);
		LstSpicaVariable lstSpicaVariable = new LstSpicaVariable();
		lstSpicaVariable.setLspc_form_id(form_id);
		lstSpicaVariable.setLspc_variable_name(variable_name);
		lstSpicaVariable.setLspc_variable_type(variable_type);
		lstSpicaVariable.setLspc_variable_description(variable_description);
		lstSpicaVariable.setLspc_variable_expression(variable_expression);
		lstSpicaVariable.setLspc_variable_modified_by(user_id);
		
		dao.updateLstSpicaVariable(lstSpicaVariable);
		
	}
	
	public void updateLstSpicaRule(Integer form_id, String rule_name, String rule_description, String rule_expression,
			String rule_error_msg, Integer user_id, String rule_prerequisites, Integer rule_category_id) {
		SpicaDAO dao = sqlSession.getMapper(SpicaDAO.class);
		LstSpicaRule lstSpicaRule = new LstSpicaRule();
		lstSpicaRule.setLspc_form_id(form_id);
		lstSpicaRule.setLspc_rule_name(rule_name);
		lstSpicaRule.setLspc_rule_description(rule_description);
		lstSpicaRule.setLspc_rule_expression(rule_expression);
		lstSpicaRule.setLspc_rule_error_msg(rule_error_msg);
		lstSpicaRule.setLspc_rule_modified_by(user_id);
		lstSpicaRule.setLspc_rule_prerequisites(rule_prerequisites);
		lstSpicaRule.setLspc_rule_category_id(rule_category_id);
		
		dao.updateLstSpicaRule(lstSpicaRule);
	}
	
	public void updateLstSpicaCondition(Integer form_id, String rule_name, String condition_name,
			String condition_expression, String condition_description, Integer user_id) {
		SpicaDAO dao = sqlSession.getMapper(SpicaDAO.class);
		LstSpicaCondition lstSpicaCondition = new LstSpicaCondition();
		lstSpicaCondition.setLspc_form_id(form_id);
		lstSpicaCondition.setLspc_rule_name(rule_name);
		lstSpicaCondition.setLspc_condition_name(condition_name);
		lstSpicaCondition.setLspc_condition_expression(condition_expression);
		lstSpicaCondition.setLspc_condition_description(condition_description);
		lstSpicaCondition.setLspc_condition_modified_by(user_id);
		
		dao.updateLstSpicaCondition(lstSpicaCondition);
	}
	
	public void updateLstSpicaList(String list_name, String list_description, String list_source,
			String list_field_name, String list_field_type, Integer user_id) {
		SpicaDAO dao = sqlSession.getMapper(SpicaDAO.class);
		LstSpicaList lstSpicaList = new LstSpicaList();
		lstSpicaList.setLspc_list_name(list_name);
		lstSpicaList.setLspc_list_description(list_description);
		lstSpicaList.setLspc_list_source(list_source);
		lstSpicaList.setLspc_list_field_name(list_field_name);
		lstSpicaList.setLspc_list_field_type(list_field_type);
		lstSpicaList.setLspc_list_modified_by(user_id);
		
		dao.updateLstSpicaList(lstSpicaList);
		
	}
	
	public void updateMstPolicyLspdId(String reg_spaj, Integer lspd_id) {
		SpicaDAO dao = sqlSession.getMapper(SpicaDAO.class);
		
		Map param = new HashMap();
		param.put("reg_spaj", reg_spaj);
		param.put("lspd_id", lspd_id);
		
		dao.updateMstPolicyLspdId(param);
	}

	public void updateMstInsured(String reg_spaj, Integer lspd_id, Integer lssa_id, Integer flag_speedy,
			Integer flag_qa) {
		SpicaDAO dao = sqlSession.getMapper(SpicaDAO.class);
		
		Map param = new HashMap();
		param.put("reg_spaj", reg_spaj);
		param.put("lspd_id", lspd_id);
		param.put("lssa_id", lssa_id);
		param.put("flag_speedy", flag_speedy);
		param.put("flag_qa", flag_qa);
		
		dao.updateMstInsured(param);
	}
	
	public void updateJnsMedis(String reg_spaj, Integer ltm_id) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		Map param = new HashMap();
		param.put("reg_spaj", reg_spaj);
		param.put("ltm_id", ltm_id);
		
		dao.updateJnsMedis(param);
	}
	
	public void updateMstCntPolis(String lcaId, Integer lsbsId, Long ldNo) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		Map upParam = new HashMap();
		upParam.put("ld_no",ldNo);
		upParam.put("as_cab",lcaId);
		upParam.put("as_bisnis",lsbsId);
	}
	
	public void updateMstCntPolicy(Integer lsbsId, String lcaId, Integer ldNo) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		Map up = new HashMap();
		up.put("as_bisnis", lsbsId);
		up.put("as_cab", lcaId);
		up.put("ld_no", ldNo);
		
		dao.updateMstCntPolicy(up);
	}
	
	public void updateMstPolicyFormated(String spaj, String lsNopol, String lsNopolFormated) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		Map up = new HashMap();
		up.put("txtnospaj",spaj);
		up.put("ls_nopol",lsNopol);
		up.put("ls_nopol_formated",lsNopolFormated);
		
		dao.updateMstPolicyFormated(up);
	}
	
	public void updateMstPolicyMspoPreExixting(String spaj, Integer value, Integer lspdId, Integer lstbId) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		Map map = new HashMap();
		map.put("txtnospaj",spaj);
		map.put("value",value);
		map.put("lspdId",lspdId);
		map.put("lstbId",lstbId);
		
		dao.updateMstPolicyMspoPreExixting(map);
	}
	
	public void updateRiderSavePaidPremi(String reg_spaj , String lsbs_id, String lsdbs_number, Double premi_bayar, Double premi_kurang_bayar) throws DataAccessException {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		Map param=new HashMap();
		param.put("reg_spaj",reg_spaj);
		param.put("lsbs_id",lsbs_id);
		param.put("lsdbs_number",lsdbs_number);
		param.put("premi_kurang_bayar", premi_kurang_bayar);
		param.put("premi_bayar", premi_bayar);
		
		dao.updateRiderSavePaidPremi(param);
	}
	
	public void updateMstBillingRemain(String spaj, Integer tahun_ke, Integer premi_ke, Double ldecPremi, Integer li_paid) throws DataAccessException {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("tahun_ke",tahun_ke);
		param.put("premi_ke",premi_ke);
		param.put("ldecPremi", ldecPremi);
		param.put("li_paid", li_paid);
		
		dao.updateMstBillingRemain(param);
	}
	
	public void updateMstPolicyMspoNextBill(String spaj, Integer lstbId, Date nextBill) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		Map dw_1=new HashMap();
		dw_1.put("mspo_next_bill",nextBill);
		dw_1.put("lstb_id",lstbId);
		dw_1.put("reg_spaj",spaj);
		
		dao.updateMstPolicyMspoNextBill(dw_1);
	}
	
	public void updateMstTransHistory(Map map) {
		SpicaDAO dao = sqlSession1.getMapper(SpicaDAO.class);
		
		dao.updateMstTransHistory(map);
	}
	
	//Delete
	
	public void deleteFormField(Integer form_id) {
		SpicaDAO dao = sqlSession.getMapper(SpicaDAO.class);
		LstSpicaField lstSpicaField = new LstSpicaField();
		lstSpicaField.setLspc_form_id(form_id);
		
		dao.deleteFormField(lstSpicaField);
	}
}
