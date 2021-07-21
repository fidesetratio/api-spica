package com.app.mapper.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.SelectProvider;

import com.app.model.Account_recur;
import com.app.model.AgentValidationProfile;
import com.app.model.BeneficiaryRelationValidProfile;
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
import com.app.model.MstRekeningCustomer;
import com.app.model.MstSpicaConditionHistory;
import com.app.model.MstSpicaFieldHistory;
import com.app.model.MstSpicaFormHistory;
import com.app.model.MstSpicaProcessHistory;
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
import com.app.model.RekeningBeneficiary;
import com.app.model.RekeningPayor;
import com.app.model.SumInsuredValidationProfile;
import com.app.model.TopUpSingleValidationProfile;
import com.app.utils.SqlBuilderQuery;

public interface SpicaDAO {
	
	//Insert
	
	public Integer insertLstSpicaForm(LstSpicaForm lstSpicaForm);
	
	public void insertFormField(HashMap<String, Object> param);
	
	public void insertLstSpicaVariable(LstSpicaVariable lstSpicaVariable);
	
	public void insertLstSpicaRule(LstSpicaRule lstSpicaRule);
	
	public void insertLstSpicaCondition(LstSpicaCondition lstSpicaCondition);
	
	public void insertLstSpicaList(LstSpicaList lstSpicaList);
	
	public void insertMstSpicaFormHistory(MstSpicaFormHistory mstSpicaFormHistory);
	
	public void insertMstSpicaFieldHistory(MstSpicaFieldHistory mstSpicaFieldHistory);
	
	public void insertMstSpicaVariableHistory(MstSpicaVariableHistory mstSpicaVariableHistory);
	
	public void insertMstSpicaRuleHistory(MstSpicaRuleHistory mstSpicaRuleHistory);
	
	public void insertMstSpicaConditionHistory(MstSpicaConditionHistory mstSpicaConditionHistory);
	
	public void insertMstPositionSpaj(Map param);
	
	public void insertMstCntPolis(Map insParam);
	
	public void insertMstPremiUlink(PremiUlink premiUlink);
	
	public void insertMstBilling(Map insBill);
	
	public void insertMstDetBillingSpaj(Map insDetBill);
	
	public void insertMstDetBilling2(Map param);
	
	public void insertMstPayment(Payment insPayment);
	
	public void insertMstTagPayment(Map insTagPayment);
	
	public void insertMstTransHistory(Map map);
	
	public void insertSpicaProcessHistory(MstSpicaProcessHistory mstSpicaProcessHistory);
	
	public void insertMstRekeningCustomer(Map param);
	
	//Select
	
	public LstSpicaForm selectForm(LstSpicaForm lstSpicaForm);

	public ArrayList<LstSpicaField> selectFormField(LstSpicaField lstSpicaField);
	
	public HashMap<String, Object> getValueField(HashMap<String, Object> param);

	public ArrayList<LstSpicaVariable> selectFormVariable(LstSpicaVariable lstSpicaVariable);

	public ArrayList<LstSpicaRule> selectFormRule(LstSpicaRule lstSpicaRule);

	public ArrayList<LstSpicaCondition> selectRuleCondition(LstSpicaCondition lstSpicaCondition);

	public LstSpicaVariable checkFormVariableExist(LstSpicaVariable lstSpicaVariable);

	public LstSpicaRule checkFormRuleExist(LstSpicaRule lstSpicaRule);

	public LstSpicaCondition checkFormRuleConditionExist(LstSpicaCondition lstSpicaCondition);

	public LstSpicaPf selectPredefinedFunction(LstSpicaPf lstSpicaPf);
	
	public LstSpicaList selectList(LstSpicaList lstSpicaList);
	
	public ArrayList<Object> getListFieldValue(HashMap<String, Object> param);
	
	public ArrayList<Object> getListUniqueFieldValue(HashMap<String, Object> param);
	
	public ArrayList<String> selectOfacScreenStatusNotPassed(HashMap<String, Object> param);
	
	public String selectScreenDescNotPassed(HashMap<String, Object> param);
	
	public ArrayList<String> selectAllRegSpajInforceByIdSimultan(String mcl_id);
	
	public MstProductInsured selectPremiAndKurs(String reg_spaj);
	
	public ArrayList<BeneficiaryRelationValidProfile> selectBeneficiaryRelationValidProfile(String reg_spaj);
	
	public ArrayList<String> selectYesAnswerFromQuestionnaire(HashMap<String, Object> param);
	
	public ArrayList<String> selectChurningSpajByPolicyHolder(HashMap<String, Object> param);
	
	public ArrayList<String> selectChurningSpajByInsured(HashMap<String, Object> param);
	
	public MstWfDocument selectDocByDocNumber(String reg_spaj);
	
	public ProductAgeValidationProfile selectProductAgeValidationPP(String reg_spaj);
	
	public LstProdset selectProductAgeValidation(LstProdset lstProdset);
	
	public ProductAgeValidationProfile selectProductAgeValidationTTG(String reg_spaj);
	
	public ArrayList<ProductAgeValidationProfile> selectRiderAgeValidationPP(String reg_spaj);
	
	public LstProdset selectRiderAgeValidation(LstProdset lstProdset);
	
	public ArrayList<ProductAgeValidationProfile> selectRiderAgeValidationTTG(String reg_spaj);
	
	public PeriodValidationProfile selectProductEndDateValidation(String reg_spaj);
	
	public LstProdset selectInsuredPeriod(LstProdset lstProdset);

	public String selectQueryPerhitungan(String param);
	
	@SelectProvider(type = SqlBuilderQuery.class, method = "getPerhitungan")
    BigDecimal getPerhitungan(Map param);
	
	public PeriodValidationProfile selectProductInsuredPeriodValidation(String reg_spaj);
	
	public PeriodValidationProfile selectProductPayPeriodValidation(String reg_spaj);
	
	public PeriodValidationProfile selectProductPremiumHolidayPeriodValidation(String reg_spaj);
	
	public ArrayList<PeriodValidationProfile> selectRiderEndDateValidation(String reg_spaj);
	
	public ArrayList<PeriodValidationProfile> selectRiderInsuredPeriodValidation(String reg_spaj);
	
	public MedicalTableProfile selectMedicalTableProfileHolder(String reg_spaj);
	
	public MedicalTableProfile selectMedicalTableProfileInsured(String reg_spaj);
	
	public String selectMedicalTableHolder(MedicalTableProfile medicalTableProfile);
	
	public HashMap<String, Object> selectMedicalTableInsured(MedicalTableProfile medicalTableProfile);
	
	public SumInsuredValidationProfile selectProductSumInsuredValidation(String reg_spaj);
	
	public LstProdsetCalc selectProdSetCalc(LstProdsetCalc lstProdsetCalc);
	
	public PremiumValidationProfile selectProductPremiumValidation(String reg_spaj);
	
	public TopUpSingleValidationProfile selectProductTopUpSingleValidation(String reg_spaj);
	
	public ArrayList<GenderValidationProfile> selectRiderGenderValidation(String reg_spaj);
	
	public LstProdset selectFlagGender(LstProdset lstProdset);
	
	public Long selectRateIdr(String conf_name);
	
	public ArrayList<ParticipantValidationProfile> selectParticipantAgeValidationProfile(String reg_spaj);
	
	public LstValidationAgeParticipant selectLstValidationAgeParticipant(Map param);
	
	public MstProductInsured selectLsbsIdAndLsdbsNumber(String reg_spaj);
	
	public ArrayList<String> selectAllRegSpajInforceByIdSimultanInsured(String reg_spaj);
	
	public Integer selectLsbsLinebus(String resultSpaj);
	
	public AgentValidationProfile selectClosingAgentValidProfile(String reg_spaj);
	
	public String selectMsagIdBancass(AgentValidationProfile agentValidationProfile);
	
	public String selectMsagId(String msag_id);
	
	public String selectMsagIdAO(String msag_id);
	
	public AgentValidationProfile selectBCAgentValidProfile(String reg_spaj);
	
	public AgentValidationProfile selectReferralAgentValidProfile(String reg_spaj);
	
	public String selectLrbId(String lrb_id);
	
	public MstProductInsured selectPremiKursAndCaraBayar(String resultSpaj);
	
	public AgentValidationProfile selectAOAgentValidProfile(String reg_spaj);
	
	public MstProductInsured selectProductGioValid(String reg_spaj);
	
	public MstProductInsured selectMstProductInsured(String resultSpaj);
	
	public Integer selectMaxFormHistProcessNumber(String reg_spaj);
	
	public DocumentStatus selectDocumentStatus(String reg_spaj);
	
	public Integer selectClaimHistory(Map param);
	
	public ArrayList<MstProductInsured> selectAllProductInsured(String reg_spaj);
	
	public String selectLbsSarPP(Map param);
	
	public ArrayList<String> selectAllRegSpajByIdSimultanHolder(String reg_spaj);
	
	public ArrayList<String> selectAllRegSpajByIdSimultanInsured(String reg_spaj);
	
	public String selectRegSpajStatusAcceptHistory(String reg_spaj);
	
	public ArrayList<HashMap<String, Object>> getSpicaHistory(String reg_spaj);
	
	public ArrayList<HashMap<String, Object>> getAMLResult(String reg_spaj);
	
	public ArrayList<HashMap<String, Object>> getOFACResult(String reg_spaj);
	
	public String selectAgentBlacklist();
	
	public ArrayList<String> selectAllRegSpajInforceByIdSimultanHolder(String reg_spaj);
	
	public ArrayList<HashMap<String, String>> selectDataCustomer(Map param);
	
	public ArrayList<HashMap<String, String>> selectDataAgent(Map param);
	
	public Integer selectLstDetBisnisFlagClean(Map param);
	
	public ArrayList<MstPeserta> selectPeserta(String reg_spaj);
	
	public Integer selectFlagAutodebet(String reg_spaj);
	
	public String selectLcaId(String reg_spaj);
	
	public String selectPolicyNoFromSpajManualMstTempDMTM(String reg_spaj);
	
	public Date selectSysdateTrunc();
	
	public Map selectMstCntPolis(Map param);
	
	public String selectMstPolicyRegSpaj(String lsNopol);
	
	public Integer selectMstDetRekruter(String msagId);
	
	public Policy selectDw1Underwriting(Map param);
	
	public List selectMstDepositPremium(Map param);
	
	public Map selectMstInsuredBegDateEndDate(Map param);
	
	public Account_recur select_account_recur(String spaj);
	
	public Double selectMstProductInsuredPremiSmartSave(Map param);
	
	public Double selectMstProductInsuredPremiDiscount(Map param);
	
	public Double selectMstDefaultMsdefNumeric(Integer liId);
	
	public Map selectTertanggung(String reg_spaj);
	
	public Map selectJenisTerbitPolis(String reg_spaj);
	
	public String selectValidasiAutodebetPremiPertama(String reg_spaj);
	
	public LstDetBisnis selectLstDetBisnis(LstDetBisnis lstDetBisnis);
	
	public Integer selectUmurTertanggungUtama(String reg_spaj);
	
	public String selectBusinessId(String spaj);
	
	public List<Map> selectInfoStableLink(String spaj);
	
	public ArrayList<HashMap> selectMstUlinkTopupNewForDetBilling(String spaj);
	
	public List selectDetailBisnis(String reg_spaj);
	
	public Integer selectLstPayModeLscbTtlMonth(Integer liPmode);
	
	public Double selectLkhKursJual(Map param);
	
	public Double selectLkhKursBeli(Map param);
	
	public Double selectLkhCurrency(Map param);
	
	public List<Product> selectMstProductInsuredExtra(String spaj);
	
	public List selectLstMerchantFee(Integer flag_merchant);
	
	public Double getBiaAkuisisi(Map param);
	
	public Integer selectCountMstBill(Map map);
	
	public List<Map> selectRiderSave(String spaj);
	
	public Powersave select_powersaver(String spaj);
	
	public Powersave select_powersaver_baru(String spaj);
	
	public Double selectSumPremiRiderInMstRiderSave(String spaj);
	
	public Double selectRateRider(Map params);
	
	public Object selectStatusPaidBilling(Map m);
	
	public Double selectMrsKurangBayarRiderSave(Map param);
	
	public Integer validationTopup(String spaj);
	
	public Integer selectMaxMstDetBillingDetKe(String spaj);
	
	public String selectSeqPaymentId();
	
	public Date selectSysdate();
	
	public Integer selectMstTransHist(String reg_spaj);
	
	public MstProductInsured selectProdukAkuisisi100Persen(String reg_spaj);
	
	public BigDecimal selectInvestmenAllocationDeficit(String reg_spaj);
	
	public BigDecimal selectTopUpMuKe2(String reg_spaj);
	
	public PremiumValidationProfile selectPremiPokok(String reg_spaj);
	
	public RekeningBeneficiary selectRekeningBeneficiary(String reg_spaj);
	
	public RekeningPayor selectRekeningPayor(String reg_spaj);
	
	public MstRekeningCustomer selectMstRekeningCustomer(String mar_acc_no);
	
	public String selectRegSpajMstBilling(String reg_spaj);
	
	//Update

	public void updateForm(LstSpicaForm lstSpicaForm);
	
	public void updateFormField(HashMap<String, Object> param);
	
	public void updateLstSpicaVariable(LstSpicaVariable lstSpicaVariable);
	
	public void updateLstSpicaRule(LstSpicaRule lstSpicaRule);
	
	public void updateLstSpicaCondition(LstSpicaCondition lstSpicaCondition);
	
	public void updateLstSpicaList(LstSpicaList lstSpicaList);
	
	public void updateMstPolicyLspdId(Map param);
	
	public void updateMstInsured(Map param);
	
	public void updateJnsMedis(Map param);
	
	public void updateMstCntPolicy(Map up);
	
	public void updateMstPolicyFormated(Map up);
	
	public void updateMstPolicyMspoPreExixting(Map map);
	
	public void updateRiderSavePaidPremi(Map param);
	
	public void updateMstBillingRemain(Map param);
	
	public void updateMstPolicyMspoNextBill(Map dw_1);
	
	public void updateMstTransHistory(Map map);

	//Delete

	public void deleteFormField(LstSpicaField lstSpicaField);
	
}
