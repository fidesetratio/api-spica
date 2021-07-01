package com.app.services;

import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.validation.BindException;

import com.app.mapper.dao.SpicaDAO;
import com.app.model.Account_recur;
import com.app.model.AgentValidationProfile;
import com.app.model.Akseptasi;
import com.app.model.BeneficiaryRelationValidProfile;
import com.app.model.DepositPremium;
import com.app.model.DocumentStatus;
import com.app.model.GenderValidationProfile;
import com.app.model.HighRiskCountriesPp;
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
import com.app.model.PeriodValidationProfile;
import com.app.model.Policy;
import com.app.model.Powersave;
import com.app.model.PremiUlink;
import com.app.model.PremiumValidationProfile;
import com.app.model.Product;
import com.app.model.ProductAgeValidationProfile;
import com.app.model.SumInsuredValidationProfile;
import com.app.model.TopUpSingleValidationProfile;
import com.app.utils.Common;
import com.app.utils.CommonUtils;
import com.app.utils.F_hit_premi_ke;
import com.app.utils.F_hit_tahun_ke;
import com.app.utils.FormatDate;
import com.app.utils.FormatString;
import com.app.utils.Products;

@Service
public class AutodebetServices {
	
	protected final Log logger = LogFactory.getLog( getClass() );

	public Integer prosesAutoDebetNB(String reg_spaj, Integer lus_id, Integer lspd_id, Integer lssp_id, Integer lssa_id) {
		SpicaServices services = new SpicaServices();
		int setNopol = 0;
		Akseptasi akseptasi = new Akseptasi();
		
		MstProductInsured mstProductInsured = services.selectLsbsIdAndLsdbsNumber(reg_spaj);
		String lca_id = services.selectLcaId(reg_spaj);
		
		akseptasi.setSpaj(reg_spaj);
		akseptasi.setLsbsId(mstProductInsured.getLsbs_id());
		akseptasi.setLsdbsNumber(mstProductInsured.getLsdbs_number());
		akseptasi.setLcaId(lca_id);
		
		setNopol = wf_set_nopol(akseptasi, 1);
		
		if(setNopol>0){//rollback dan tampilkan message
			//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			return setNopol;
		}
		
		//Policy policy = services.selectDw1Underwriting(akseptasi.getSpaj(), 1, 1);
		Policy policy = services.selectDw1Underwriting(akseptasi.getSpaj());
		List lsDp = services.selectMstDepositPremium(akseptasi.getSpaj(), 1);
		BindException err = null;
		
		if(wf_ins_bill(reg_spaj,1,1,akseptasi.getLsbsId(),akseptasi.getLsdbsNumber(),56,1,lsDp,lus_id.toString(),policy,err)){			
			//services.updateMstInsured2(reg_spaj,118);
			//services.updateMstPolicyLspdId(reg_spaj,118);			
			services.insertMstPositionSpajStatic(reg_spaj, lspd_id, lssp_id,lssa_id, 0, "Transfer Proses AutoDebet Ke Finance");
			saveMstTransHistory(reg_spaj, "tgl_transfer_autodebet_nb", null, null, null);
		}
		
		return setNopol;
	}

	private void saveMstTransHistory(String reg_spaj, String kolom_tgl, Date tgl, String kolom_user, String lus_id) {
		SpicaServices services = new SpicaServices();
		if(tgl == null) tgl = services.selectSysdate();
		
		Map map = new HashMap();
		map.put("reg_spaj", reg_spaj);
		map.put("kolom_tgl", kolom_tgl);
		map.put("tgl", tgl);
		map.put("kolom_user", kolom_user);
		map.put("lus_id", lus_id);
		
		Integer exist = services.selectMstTransHist(reg_spaj);
		
		if(exist > 0){
			services.updateMstTransHistory(map);
		}else{
			services.insertMstTransHistory(map);
		}
	}

	private boolean wf_ins_bill(String spaj,Integer insured,Integer active,Integer lsbsId,Integer lsdbsNumber,Integer lspdId,Integer lstbId,List lsDp,
			String lusId,Policy policy,BindException err) throws DataAccessException {
		SpicaServices services = new SpicaServices();
		Products products = new Products();
		FormatString formatString = new FormatString();
		Properties props = new Properties();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		
		DecimalFormat f3 = new DecimalFormat ("000");
		SimpleDateFormat sdfDd=new SimpleDateFormat("dd");
		
		Integer liPmode, liPperiod, liMonth;
		Integer liPremiKe, liThKe, liPaid = 0, liPaidTU = 0;
		Integer liId = new Integer(0); //3 ~ biaya polis = Rp. 20.000,-
		Integer flagmerchant = 0;
		String lsCab, lsKursId, lsPayId, lsWakil, lsRegion, lsKursPolis;
		Double ldecPremi, ldecBpolis, ldecDp, ldecPremiAwal, ldecPremiTopUp, ldecTotalPremi;
		Date ldtBegDate, ldtEndDate, ldtDueDate;
		Boolean lbRet = true, lbTp = false;
		Double ldecKurs = new Double(1);
		Date ldt_tgl_book;
		Date ldt_tgl_debet = null;
		
		lsKursId = policy.getLku_id();
		lsKursPolis = policy.getLku_id();
		liPmode = policy.getLscb_id();
		lsCab = policy.getLca_id();
		liPperiod = policy.getMspo_pay_period();
		lsWakil = policy.getLwk_id();
		lsRegion = policy.getLsrg_id();
		
		Map map = services.selectMstInsuredBegDateEndDate(spaj,insured);
		ldtBegDate = (Date) map.get("MSTE_BEG_DATE");
		ldtEndDate = (Date) map.get("MSTE_END_DATE");
		
		//Deddy (11/6/2012) - Jika autodebet premi pertama dan posisi lspd_id !=1, maka tidak perlu proses billing lagi.
		Account_recur account_recur = services.select_account_recur(spaj);
		if(account_recur!=null){
			if(account_recur.getFlag_autodebet_nb()!=null){
				if(account_recur.getFlag_autodebet_nb()==1 && lspdId !=56){
					ldt_tgl_debet=account_recur.getTgl_debet();
					return true;	
				}
			}
		}
		
		//Antisipasi jika di mst_account_recur tgl debet kosong , diset beg date atau kosong , biar aman. menandakan tgl debet di mst_billing = tgl beg date
//		if(account_recur==null){
//			ldt_tgl_debet=ldtBegDate;
//		}
		
		//Yusuf (16 Oct 2009) - Bila Stable Link, ambil begdate nya itu dari slink, bukan dari insured
		if(products.stableLink(services.selectBusinessId(spaj))) {
			List<Map> daftarStableLink = services.selectInfoStableLink(spaj);
			ldtBegDate = null;
			ldtEndDate = null;
			for(Map info : daftarStableLink) {
				int msl_no = ((BigDecimal) info.get("MSL_NO")).intValue(); 
				if(msl_no == 1) { //apabila 1, maka itu premi pokok
					ldtBegDate=(Date) info.get("MSL_BDATE");
					ldtEndDate=(Date) info.get("MSL_EDATE");
					break;
				}
			}
			if(ldtBegDate == null || ldtEndDate == null) throw new RuntimeException("BEGDATE SLINK ERROR, HARAP HUBUNGI ITwebandmobile@sinarmasmsiglife.co.id");
		}
		//
		if( lsbsId==142 && lsdbsNumber==11){
			ldecPremi = services.selectMstProductInsuredPremiSmartSave(spaj,insured,active);
		}else{
			ldecPremi = services.selectMstProductInsuredPremiDiscount(spaj,insured,active);
		}
		ldecPremiAwal = ldecPremi;
		ldecTotalPremi = ldecPremi;
		if(lsKursId.equals("02"))
			liId = new Integer(0); //4~biaya polis = $5
		//
		ldecBpolis = services.selectMstDefaultMsdefNumeric(liId); //tarik nilai default biaya polis
		//Kalo proteksiNARMAS, biaya polis tidak ada !!! juga pa pti, juga unit-linked
		Integer pos = 0;
		String kode = "061, 073, 077, 084, 079, 080, 081, 087, 088, 090, 091, 092, 095, 097, 098, 100, 101, 102, 103, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 126, 127, 128, 129, 133, 135, 136, 137, 138, 139, 150, 151, 107, 140, 141, 148, 134,152,153,154,155,157,158,159,160,162,164,165,166,174,199,217,218,224";
		String bisnisId = f3.format(lsbsId);
		pos = kode.indexOf(bisnisId);
		Map mapTt = services.selectTertanggung(spaj);
		Integer flagGuthrie = Integer.parseInt(mapTt.get("MSTE_FLAG_GUTHRIE").toString());
		String lcaId = (String) mapTt.get("LCA_ID");
		Integer usia_tt = Integer.parseInt(mapTt.get("MSTE_AGE").toString());
		Integer jenis_terbit = services.selectJenisTerbitPolis(spaj);
		
		if(pos>=0 || flagGuthrie.intValue()==1 || products.powerSave(formatString.rpad("0", lsbsId.toString(), 3))){
			ldecBpolis = new Double(0);
		}
		
		//MANTA - Cek Total Premi (Pokok + Topup)
		if(products.unitLink(bisnisId)) {
			ArrayList<HashMap> daftarTopUp = services.selectMstUlinkTopupNewForDetBilling(spaj);
			if(daftarTopUp.size()>0){
				for(int i=0; i<daftarTopUp.size(); i++){
					Map mapUlink = (HashMap) daftarTopUp.get(i);
					//Double premi_tu = (Double) mapUlink.get("MU_JLH_PREMI");
					Double premi_tu = Double.valueOf(mapUlink.get("MU_JLH_PREMI").toString());
					ldecTotalPremi += premi_tu;
				}
			}
		}
					
				//Multi Invest
				if("96,194".indexOf(lsbsId)>-1){
					ldecBpolis = (double) 0;
				}
				//Untuk cerdas new(121), bebas biaya polis
				else if(lsbsId==121){
					ldecBpolis = (double) 0;
				}
				//khusus produk simas sehat biaya polisnya 37,000
				else if(lsbsId==161){
					//ldecBpolis = new Double(37000);
					ldecBpolis = (double) 0;
				//Yusuf - Stable link, tidak ada biaya polis
				}else if(lsbsId==164) {
					ldecBpolis = (double) 0;
				}
				else if(products.muamalat(spaj)){
					//ldecBpolis = (double) 75000;
					ldecBpolis = (double) 0;
					//khusus mabrur, biayanya beda
					if(lsbsId==153) ldecBpolis = (double) 0;//(double) 40000;
				}
				else if(lsbsId==171){
					ldecBpolis = (double) 0;
				}else if(lsbsId==172){
					ldecBpolis = (double) 0;
				}else if (lsbsId==183 || lsbsId==189 || lsbsId==193) {//Eka Sehat
					if(lsdbsNumber<16){
						ldecBpolis=(double) 37000;
					}else{
						ldecBpolis = (double) 0;
					}
					if(lsbsId==189 || lsbsId==193){
						ldecBpolis = (double) 0;
					}
					//ldecBpolis = new Double(0);
				
				}else if (lsbsId==186) {//Progressive Link
					ldecBpolis = (double) 0;
				}
				
				//super prot, eka protection, biaya polis 20 rebu
				if(lsbsId.intValue() == 45 || lsbsId.intValue()==85|| lsbsId.intValue() == 130) {
					if(jenis_terbit==0){
						if(lsKursId.equals("02")){
							ldecBpolis = (double) 5;
						}else if(lsKursId.equals("01")){
							ldecBpolis = (double) 20000;
						}
					}else{
						ldecBpolis = (double) 0;
					}
					
				//super sejahtera dan (super sehat atau super sehat plus), biaya polis 20rb
				}else if(lsbsId.intValue() == 145 || lsbsId.intValue() == 53 || lsbsId.intValue() == 54 || lsbsId.intValue() == 131 || lsbsId.intValue() == 132){
					ldecBpolis = (double) 20000;
				}
				else if(products.SuperSejahtera(lsbsId.toString())){
						ldecBpolis = (double) 0;
				}
				else if(products.stableSave(lsbsId.intValue(), lsdbsNumber.intValue())){
					ldecBpolis = (double) 0;
				}
		
		if(liPmode!=0){
			liMonth = services.selectLstPayModeLscbTtlMonth(liPmode);
	//		if(logger.isDebugEnabled())logger.debug("ldt enddate ="+defaultDateFormat.format(ldtEndDate));
			Date dTemp=selectAddMonths(formatter.format(ldtBegDate),liMonth);
	//		if(logger.isDebugEnabled())logger.debug("dtemp ="+defaultDateFormat.format(dTemp));
			ldtEndDate=FormatDate.add(dTemp,Calendar.DATE,-1);
	//		if(logger.isDebugEnabled())logger.debug("ldt enddate ="+defaultDateFormat.format(ldtEndDate));
			//Himmia 30/01/2001
			if(sdfDd.format(ldtEndDate).equals(sdfDd.format(ldtBegDate))) {
				ldtEndDate=FormatDate.add(ldtEndDate,Calendar.DATE,-1);
			}
		}
		//
		liThKe = 1;
		liPremiKe = 1;
		Calendar calTemp = new GregorianCalendar(2006,06-1,1);
		
		//yusuf 02062006 due date lebih besar dari 1 juni 2006 ditambah 1 minggu
				if(ldtBegDate.compareTo(calTemp.getTime()) >0){

					boolean smileHospital= lsbsId.intValue() == 195 && (lsdbsNumber >= 73 && lsdbsNumber <= 84);
					boolean smileMedical= lsbsId.intValue() == 183 && (lsdbsNumber >= 106 && lsdbsNumber <= 120);
					
					
					
					if(smileHospital || smileMedical){
						ldtDueDate=FormatDate.add(ldtBegDate,Calendar.DATE,45);
					}else{
						ldtDueDate=FormatDate.add(ldtBegDate,Calendar.DATE,7);
					}
					
					//custom grace period
					if((lsbsId.intValue() == 219 && (lsdbsNumber >= 5 && lsdbsNumber <= 8)) || //helpdesk [138638] produk baru SPP Syariah (219-5~8)
						(lsbsId.intValue() == 226 && (lsdbsNumber >= 1 && lsdbsNumber <= 5)) || //helpdesk [139867] produk baru Simas Legacy Plan (226-1~5)
						(lsbsId.intValue() == 134 && lsdbsNumber == 13) || //helpdesk [142003] produk baru Smart Platinum Link RPUL BEL (134-13)
						(lsbsId.intValue() == 120 && (lsdbsNumber >= 22 && lsdbsNumber <= 24)) || // Produk SIMPOL 120 (22,23,24)
						lsbsId.intValue() == 213 || //Project PEGA Decomm
						(lsbsId.intValue() == 190 && lsdbsNumber == 4)){ //Project PEGA Decomm
						ldtDueDate=FormatDate.add(ldtBegDate, Calendar.DATE, 30);
					}
				}else{
					ldtDueDate = selectAddMonths(formatter.format(ldtBegDate),new Integer(1));
				}
				
				if(ldtDueDate==null){
					err.reject("","Get End-Date Is Not Working Properly (e)");
					return false;
				}
				
				//
				if(!lsDp.isEmpty()){
					for(int i=0;i<lsDp.size();i++){
						DepositPremium depPremi = (DepositPremium) lsDp.get(i);
						ldecDp = new Double(0);
						liId = depPremi.getMsdp_jtp();
						if(liId==1 && depPremi.getMsdp_flag_topup() == null){
							lsKursId = depPremi.getLku_id();
							ldecDp = depPremi.getMsdp_payment();
							if( (!lsKursPolis.equals(lsKursId) ) || (lsKursId.equals("02")) ){
								ldt_tgl_book = depPremi.getMsdp_date_book();
								ldecKurs = selectGetKursJb(ldt_tgl_book,"J");
								if(ldecKurs==null){
									err.reject("","Kurs tgl "+formatter.format(ldt_tgl_book)+" (dd/mm/yyyy) tidak ada");
									return false;
								}
							}
							
							if(!lsKursPolis.equals(lsKursId)) {
								if(lsKursPolis.equals("01"))
									ldecDp = new Double(ldecDp.doubleValue()* ldecKurs.doubleValue());
								else
									ldecDp = new Double(ldecDp.doubleValue()/ ldecKurs.doubleValue());
							}
							
							ldecPremi = new Double(ldecPremi.doubleValue()-ldecDp.doubleValue());
							lbTp = true;
							
							if(flagmerchant == 0){
								if(depPremi.getMsdp_flag_merchant() != null)
									flagmerchant = depPremi.getMsdp_flag_merchant();
							}
						}
					}
				}
				
				ldecPremi = new Double(ldecPremi.doubleValue()+ ldecBpolis.doubleValue());
				
				//(Deddy : penambahan apabila ada extra premi, biling ditambahkan)
				List<Product> listProductExtra = services.selectMstProductInsuredExtra(spaj);
				if(listProductExtra.size()>0){
					Double premiExtra = 0.0;
					for(int i=0; i<listProductExtra.size();i++){
						Product productExtra = (Product) listProductExtra.get(i);
						premiExtra += productExtra.getMspr_premium();
					}
//					if(lcaId.equals("42") && products.unitLink(uwDao.selectBusinessId(spaj))){
//						ldecPremi =new Double(ldecPremi.doubleValue());
//					}else{
						ldecPremi = new Double(ldecPremi.doubleValue() + premiExtra.doubleValue());
//					}
				}
				
				//MANTA
				BigDecimal premiawal = new BigDecimal(ldecTotalPremi);
				BigDecimal bd_limit = new BigDecimal(2.5);
				BigDecimal persen = new BigDecimal(100);
				if(ldecPremi.doubleValue() <= 0 && flagmerchant == 0){
					liPaid=1;
				}else if(flagmerchant != 0){
					Double selisih = new Double(0);
					Double merchant_fee = new Double(0);
					ArrayList<HashMap> listMerchant = Common.serializableList(services.selectLstMerchantFee(flagmerchant));
					
					BigDecimal fee = new BigDecimal(listMerchant.get(0).get("PERSENTASE").toString());
					
					merchant_fee = new Double( ((premiawal.multiply(fee)).divide(persen, RoundingMode.HALF_UP)).doubleValue() );
					selisih = ldecPremi.doubleValue() - merchant_fee.doubleValue();
					if(selisih == new Double(0) || selisih <= new Double(100)) liPaid=1;
					
//					if(ldecPremi.doubleValue() == merchant_fee.doubleValue()) liPaid=1;
				}else{
					Double d_limit = new Double( ((premiawal.multiply(bd_limit)).divide(persen, RoundingMode.HALF_UP)).doubleValue() );
					if(ldecPremi.doubleValue() <= d_limit.doubleValue()) liPaid=1;
				}
				
				//EKALINK FAMILY / ekalink famili syariah
				Integer msbiThBak=null,msbiPremiBak=null;
				String premi_ulink = "115,116,117,118,119,122,138,139,153,159,160,162,164,165,166,174,190,191,199,200,202,213,215,216,217,218,224";
				Integer idxUlink=premi_ulink.indexOf(bisnisId);
				if(idxUlink>=0){
					//
					Double ldecPremiAsli=services.selectMstProductInsuredPremiDiscount(spaj,insured,active);
					msbiThBak=1;
					msbiPremiBak=1;
					if (liPmode == 1 || liPmode == 2 || liPmode == 6 || liPmode == 3)
						liPmode= 3;
					
					Double persenAk = services.getBiaAkuisisi(lsbsId, lsdbsNumber,liPmode, liPperiod, new Integer(1));
					
					PremiUlink premiUlink=new PremiUlink();
					premiUlink.setReg_spaj(spaj);
					premiUlink.setMsbi_tahun_ke(new Integer(1));
					premiUlink.setMsbi_premi_ke(new Integer(1));
					premiUlink.setPremi_ke(new Integer(1));
					premiUlink.setLine_ak(new Integer(1));
					premiUlink.setTh_ak(new Integer(1));
					premiUlink.setPremi(ldecPremiAsli);
					premiUlink.setTotal_premi(ldecPremiAsli);
					premiUlink.setPersen_ak(persenAk);
					services.insertMstPremiUlink(premiUlink);

				}
				
				//himmia 06/03/2007 untuk produk multi invest msbi_persen_paid=100
				Integer msbiPersenPaid=null;
				String multiInvest = "074,076,096,099,135,136,182,194";
				if(bisnisId.indexOf(multiInvest)>0){
					msbiPersenPaid=100;
				}
				Integer countBill= services.selectCountMstBill(spaj, new Integer(liThKe),new Integer(liPremiKe));
				if(lspdId!=56){
					lspdId=4;
				}
				if(countBill<=0){
					if(!products.powerSave(bisnisId)){
						services.insertMstBilling(spaj,new Integer(liThKe),new Integer(liPremiKe),ldtBegDate,ldtEndDate,ldtDueDate,lsKursPolis,
								ldecBpolis,new Double(0),new Double(0),new Double(0),ldecPremi,new Integer(liPaid),new Integer(1),
								new Integer(0),new Integer(0),lusId,lspdId,lsCab,lsWakil,lsRegion,null,msbiThBak,msbiPremiBak,msbiPersenPaid,ldt_tgl_debet);
//						if(lcaId.equals("42") && products.unitLink(uwDao.selectBusinessId(spaj))){
//							insertMstDetBillingNoExtra(spaj);
//						}else{
							services.insertMstDetBillingSpaj(spaj);
//						}
					
					} else if(products.powerSave(bisnisId)){
						List<Map> listRiderPSave = services.selectRiderSave(spaj);
						if(listRiderPSave.size()>0){
							Powersave daftarPSave = (Powersave) services.select_powersaver(spaj);
							if(Integer.parseInt(bisnisId)==188){
								daftarPSave = (Powersave) services.select_powersaver_baru(spaj);
							}
							Map riderPsaveFirst = listRiderPSave.get(0);
							BigDecimal lscb_id_rider = (BigDecimal) riderPsaveFirst.get("LSCB_ID_RIDER");
							BigDecimal mrs_rider_cb = (BigDecimal) riderPsaveFirst.get("MRS_RIDER_CB");
							if(mrs_rider_cb.intValue()==1){
								services.insertMstBilling(spaj,new Integer(liThKe),new Integer(liPremiKe),ldtBegDate,ldtEndDate,ldtDueDate,lsKursPolis,
										ldecBpolis,new Double(0),new Double(0),new Double(0),ldecPremi,new Integer(liPaid),new Integer(1),
										new Integer(0),new Integer(0),lusId,lspdId,lsCab,lsWakil,lsRegion,null,msbiThBak,msbiPremiBak,msbiPersenPaid,ldt_tgl_debet);
//								if(lcaId.equals("42") && products.unitLink(uwDao.selectBusinessId(spaj))){
//									insertMstDetBillingNoExtra(spaj);
//								}else{
									services.insertMstDetBillingSpaj(spaj);
//								}
							}else if(mrs_rider_cb.intValue()==0){
								int masa_bayar_rider=1;
								if(lscb_id_rider.intValue()==1){
									masa_bayar_rider=3;//triwulan
								}else if(lscb_id_rider.intValue()==2){
									masa_bayar_rider=2;//semester
								}else if(lscb_id_rider.intValue()==6){
									masa_bayar_rider=1;//bulanan
								}else if(lscb_id_rider.intValue()==3){
									masa_bayar_rider=12;//tahunan
								}
								
								if(("142").equals(bisnisId) && lsdbsNumber==11){
									masa_bayar_rider=12;
								}
								
								int periodBill = Integer.parseInt(daftarPSave.getMps_jangka_inv())/ masa_bayar_rider;
								Double premiRider = services.selectSumPremiRiderInMstRiderSave(spaj);
								//testing dulu bagian ini(10 Okt 2011)
								
								Double factor=0.0;
								Double diskon =1.0;
								if(lscb_id_rider.intValue()==1){
									factor = 0.270;
								}else if(lscb_id_rider.intValue()==2){
									factor = 0.525;
								}else if(lscb_id_rider.intValue()==6){
									factor = 0.1;
								}else factor = 1.0;
								Double total_premi =0.;
								Date beg_date_bill =ldtBegDate;
								for(int i=0;i<listRiderPSave.size();i++){
									Map riderPsave = listRiderPSave.get(i);
									BigDecimal lsbs_id_rider = (BigDecimal) riderPsave.get("LSBS_ID");
									BigDecimal lsdbs_number_rider = (BigDecimal) riderPsave.get("LSDBS_NUMBER");
									BigDecimal mrs_up =  (BigDecimal) riderPsave.get("MRS_UP");
									Date beg_date_next_bill = selectAddMonths(formatter.format(ldtBegDate),masa_bayar_rider);
									for(int j=0;j<periodBill;j++){
										if(j>0){
											beg_date_bill =selectAddMonths(formatter.format(beg_date_bill),masa_bayar_rider);
											beg_date_next_bill = selectAddMonths(formatter.format(beg_date_next_bill),masa_bayar_rider);
										}
										int premi_ke = 0;
										int tahun_ke = 0;
										if(Integer.parseInt(daftarPSave.getMps_jangka_inv())>12){
											tahun_ke = F_hit_tahun_ke.hitTahunKe(FormatDate.getDateInFirstSecond(FormatDate.add(beg_date_next_bill, Calendar.DATE, -1)), FormatDate.getDateInFirstSecond(ldtBegDate), masa_bayar_rider);
											premi_ke =F_hit_premi_ke.hitPremiKe(FormatDate.getDateInFirstSecond(FormatDate.add(beg_date_next_bill, Calendar.DATE, -1)), FormatDate.getDateInFirstSecond(ldtBegDate), masa_bayar_rider);
										}else{
											tahun_ke = F_hit_tahun_ke.hitTahunKe(FormatDate.getDateInFirstSecond(FormatDate.add(beg_date_next_bill, Calendar.DATE, -1)), FormatDate.getDateInFirstSecond(ldtBegDate), masa_bayar_rider);
											premi_ke = F_hit_premi_ke.hitPremiKe(FormatDate.getDateInFirstSecond(FormatDate.add(beg_date_next_bill, Calendar.DATE, -1)), FormatDate.getDateInFirstSecond(ldtBegDate), masa_bayar_rider);
										}
										
										
										Double rate = services.selectRateRider(lsKursId, usia_tt+(tahun_ke-1), 0, lsbs_id_rider.intValue(), lsdbs_number_rider.intValue());
										Double premi = new Double(0);
										if(lsbs_id_rider.intValue()==846 || lsbs_id_rider.intValue()==847){
											premi = new Double(0);
										}else{
											premi =(rate * mrs_up.doubleValue() / 1000) * factor; 
										}								
										if(lsbs_id_rider.intValue()==819 || lsbs_id_rider.intValue()==820 || lsbs_id_rider.intValue()==823 || lsbs_id_rider.intValue()==825 || lsbs_id_rider.intValue()==826){
											if(lsbs_id_rider.intValue()!=826){
												if(lsdbs_number_rider.intValue()>=16){
													diskon = 0.975;
												}
											}else{
												if(lsdbs_number_rider.intValue()>=11){
													diskon = 0.9;
												}
											}
											premi = rate * diskon;
										}
										if(Common.isEmpty(services.selectStatusPaidBilling(spaj, premi_ke, tahun_ke)) ){
											if(j==0 ){
												total_premi = ldecPremi.doubleValue()+premi;
												if(total_premi <= 0){
													liPaid=1;
												}else{
													liPaid=0;
												}
												Double premi_kurang_bayar = services.selectMrsKurangBayarRiderSave(spaj,lsbs_id_rider.toString(), lsdbs_number_rider.toString());
												if(liPaid==1){
													services.updateRiderSavePaidPremi(spaj,lsbs_id_rider.toString(), lsdbs_number_rider.toString(), premi_kurang_bayar, new Double(0) );
												}
												services.insertMstBilling(spaj,new Integer(liThKe),new Integer(liPremiKe),ldtBegDate,ldtEndDate,ldtDueDate,lsKursPolis,
														ldecBpolis,new Double(0),new Double(0),new Double(0),ldecPremi+premi,new Integer(liPaid),new Integer(1),
														new Integer(0),new Integer(0),lusId,lspdId,lsCab,lsWakil,lsRegion,null,msbiThBak,msbiPremiBak,msbiPersenPaid,ldt_tgl_debet);
											}else{
												services.insertMstBilling(spaj, tahun_ke, premi_ke,
														beg_date_bill,FormatDate.add(beg_date_next_bill, Calendar.DATE, -1),FormatDate.add(beg_date_bill, Calendar.DATE, 7),
														lsKursPolis, new Double(0), new Double(0), new Double(0), new Double(0),
														premi, 0, 1, 0, 0, lusId, 12, 
														lsCab, lsWakil, lsRegion, 1, null, null, msbiPersenPaid,account_recur.getTgl_debet());
											}
										}else{
											total_premi += premi;
											if(total_premi <= 0){
												liPaid=1;
											}else{
												liPaid=0;
											}
											Double premi_kurang_bayar = services.selectMrsKurangBayarRiderSave(spaj,lsbs_id_rider.toString(), lsdbs_number_rider.toString());
											if(liPaid==1){
												services.updateRiderSavePaidPremi(spaj,lsbs_id_rider.toString(), lsdbs_number_rider.toString(), premi_kurang_bayar, new Double(0) );
											}
											services.updateMstBillingRemain(spaj, tahun_ke, premi_ke, premi,liPaid);
											if(i==1 && j==0){
												services.insertMstDetBilling2(spaj, tahun_ke, premi_ke, 1, lsbsId, lsdbsNumber, ldecPremiAwal, new Double(0));
											}
											
										}
										if(i==0 ){
											services.insertMstDetBilling2(spaj, tahun_ke, premi_ke, i+2, lsbs_id_rider.intValue(), lsdbs_number_rider.intValue(), premi, new Double(0));
										}else{
											services.insertMstDetBilling2(spaj, tahun_ke, premi_ke, i+2, lsbs_id_rider.intValue(), lsdbs_number_rider.intValue(), premi, new Double(0));
										}
									}
								}
							}
						}else{
							services.insertMstBilling(spaj,new Integer(liThKe),new Integer(liPremiKe),ldtBegDate,ldtEndDate,ldtDueDate,lsKursPolis,
									ldecBpolis,new Double(0),new Double(0),new Double(0),ldecPremi,new Integer(liPaid),new Integer(1),
									new Integer(0),new Integer(0),lusId,lspdId,lsCab,lsWakil,lsRegion,null,msbiThBak,msbiPremiBak,msbiPersenPaid,ldt_tgl_debet);
								
//							if(lcaId.equals("42") && products.unitLink(uwDao.selectBusinessId(spaj))){
//								insertMstDetBillingNoExtra(spaj);
//							}else{
								services.insertMstDetBillingSpaj(spaj);
//							}
						}
					}
				}
				
				//Yusuf (1/5/08) stable link, insert billing dan det billingnya, diambil dari MST_SLINK
				if(products.stableLink(bisnisId)) {
					List<Map> daftarStableLink = services.selectInfoStableLink(spaj);

					for(Map info : daftarStableLink) {
						//int msl_no = info.get("MSL_NO").intValue(); 
						int msl_no = Integer.parseInt(info.get("MSL_NO").toString());
						if(msl_no>1) { //apabila 1, maka itu premi pokok
							double premi = Double.valueOf(info.get("MSL_PREMI").toString());
							services.insertMstBilling(spaj, 1, msl_no,
									(Date) info.get("MSL_BDATE"),
									(Date) info.get("MSL_EDATE"),
									(Date) info.get("MSL_NEXT_DATE"),
									lsKursPolis, new Double(0), new Double(0), new Double(0), new Double(0),
									premi, 0, 1, 0, 0, lusId, lspdId, 
									lsCab, lsWakil, lsRegion, 1, null, null, msbiPersenPaid,ldt_tgl_debet);
							services.insertMstDetBilling2(spaj, 1, msl_no, 1, lsbsId, lsdbsNumber, premi, new Double(0));
						}
					}					
				//jika unit link
				}else if(products.unitLink(bisnisId)) {
					
//					Integer li_tu=new Integer(0);
//					Map h=selectMstUlinkTopup(spaj,new Integer(1));
//					li_tu=(Integer)h.get("MU_PERIODIC_TU");
//					ldecPremi=(Double)h.get("MU_JLH_TU");
//					if(li_tu==null)
//						li_tu=new Integer(0);
//					if(ldecPremi==null)
//						ldecPremi=new Double(0);
//					
//					if(li_tu >= 1 && ldecPremi.doubleValue() > 0) {
//						insertMstBilling(spaj,new Integer(liThKe), new Integer(2),ldtBegDate,ldtEndDate,ldtDueDate,
//								lsKursPolis,new Double(0),new Double(0),new Double(0),new Double(0),ldecPremi,new Integer(0),
//								new Integer(1),new Integer(0),new Integer(0),lusId,new Integer(4),lsCab,lsWakil,lsRegion,new Integer(1),null,null,msbiPersenPaid);
//						insertMstDetBillingTopup(spaj,ldecPremi);
//					}
					
					//List<Integer> daftarMuKe = uwDao.selectMuKe(spaj);
					List daftarTopUp = services.selectMstUlinkTopupNewForDetBilling(spaj);
					Integer topupBerkala = services.validationTopup(spaj);
					
					if(topupBerkala>=1){
						Integer x = services.selectMaxMstDetBillingDetKe(spaj); 
						for(int d=0; d<daftarTopUp.size(); d++) {
							x++;
							Map mapUlink = (HashMap) daftarTopUp.get(d);
							//Double premi = (Double) mapUlink.get("MU_JLH_PREMI");
							Double premi = Double.valueOf(mapUlink.get("MU_JLH_PREMI").toString());
							ldecPremiTopUp = premi;
							Integer premiKe = Integer.parseInt(mapUlink.get("MU_PREMI_KE").toString());
							Integer topup = Integer.parseInt(mapUlink.get("LT_ID").toString());//topup 2=tunggal 5=berkala
							Integer flagTopup = null, msdpTopup = null;
							Integer flagmerchant_tu = 0;
							if(topup==2){
								flagTopup = 1;
							} else if(topup==5){
								flagTopup = 2;
							}
							
							//MANTA - Cek apakah ada DP untuk Billing Top Up
							if(!lsDp.isEmpty()){
								for(int i=0;i<lsDp.size();i++){
									DepositPremium depPremi = (DepositPremium) lsDp.get(i);
									ldecDp = new Double(0);
									liId = depPremi.getMsdp_jtp();
									msdpTopup = depPremi.getMsdp_flag_topup();
									if(liId==1 && flagTopup.equals(msdpTopup)){
										lsKursId = depPremi.getLku_id();
										ldecDp = depPremi.getMsdp_payment();
										if( (!lsKursPolis.equals(lsKursId) ) || (lsKursId.equals("02")) ){
											ldt_tgl_book = depPremi.getMsdp_date_book();
											ldecKurs = selectGetKursJb(ldt_tgl_book,"J");
											if(ldecKurs==null){
												err.reject("","Kurs tgl "+formatter.format(ldt_tgl_book)+" (dd/mm/yyyy) tidak ada");
												return false;
											}
										}
										
										if(!lsKursPolis.equals(lsKursId)) {
											if(lsKursPolis.equals("01")){
												ldecDp = new Double(ldecDp.doubleValue() * ldecKurs.doubleValue());
											}else{
												ldecDp = new Double(ldecDp.doubleValue() / ldecKurs.doubleValue());
											}
										}
										
										ldecPremiTopUp = new Double(ldecPremiTopUp.doubleValue() - ldecDp.doubleValue());
									
										if(flagmerchant_tu == 0){
											if(depPremi.getMsdp_flag_merchant() != null)
												flagmerchant_tu = depPremi.getMsdp_flag_merchant();
										}
									}
								}
							}
							
							if(ldecPremiTopUp.doubleValue() <= 0 && flagmerchant_tu == 0){
								liPaidTU = 1;
							}else if(flagmerchant_tu != 0){
								Double selisih = new Double(0);
								Double merchant_fee = new Double(0);
								ArrayList<HashMap> listMerchant = Common.serializableList(services.selectLstMerchantFee(flagmerchant_tu));
								BigDecimal fee = new BigDecimal(listMerchant.get(0).get("PERSENTASE").toString());
								
								merchant_fee = new Double( ((premiawal.multiply(fee)).divide(persen, RoundingMode.HALF_UP)).doubleValue() );
								selisih = ldecPremiTopUp.doubleValue() - merchant_fee.doubleValue();
								if(selisih == new Double(0) || selisih <= new Double(1)) liPaidTU=1;

//								if(ldecPremiTopUp.doubleValue() == merchant_fee.doubleValue()) liPaidTU=1;
							}
							
							if(premiKe!=0 && topup!=10){
								services.insertMstBilling(spaj,new Integer(liThKe),d+2,ldtBegDate,ldtEndDate,ldtDueDate,
										lsKursPolis,new Double(0),new Double(0),new Double(0),new Double(0),ldecPremiTopUp,new Integer(liPaidTU),
										new Integer(1),new Integer(0),new Integer(0),lusId,lspdId,lsCab,lsWakil,lsRegion,flagTopup,null,null,msbiPersenPaid,ldt_tgl_debet);
								
								if(premiKe!=1) x = 1;
								services.insertMstDetBilling2(spaj, 1, premiKe, x, lsbsId, lsdbsNumber, premi, new Double(0));
							}
						}
					}	
				}
				
				//Jika ada Deposit Premium
				if(lbTp){
					List daftarTopUp = services.selectMstUlinkTopupNewForDetBilling(spaj);
					for(int i=0;i<lsDp.size();i++){
						DepositPremium depPremi = (DepositPremium) lsDp.get(i);
						Integer i_premike = 1;
						if(depPremi.getMsdp_jtp()==1){
							lsKursId = depPremi.getLku_id();
							ldecKurs = new Double(1);
							if( (!lsKursPolis.equals(lsKursId) ) || (lsKursId.equals("02")) ){
								ldt_tgl_book = depPremi.getMsdp_date_book();
								ldecKurs = selectGetKursJb(ldt_tgl_book,"J");
								if(ldecKurs==null){
									err.reject("","Kurs tgl "+formatter.format(ldt_tgl_book)+" (dd/mm/yyyy) tidak ada");
									return false;
								}
							}
							//(12 May 2014) Deddy - Ditutup karena memakai seq dari Oracle lgsg.
//							String sequence=selectGetCounter(9,lsCab);
//							updateCounter(sequence,9,lsCab);
//							DecimalFormat f10=new DecimalFormat("0000000000");
//							lsPayId=lsCab+f10.format(Integer.parseInt(sequence));
							if(depPremi.getMsdp_flag_topup()==null){
								lsPayId = services.selectSeqPaymentId();
								services.insertMstPayment(lsPayId, ldecKurs, depPremi);
								services.insertMstTagPayment(spaj, lsPayId, new Integer(1), i_premike, depPremi);
							}else{
								for(int j=0;j<daftarTopUp.size();j++) {
									Map mapUlink = (HashMap) daftarTopUp.get(j);
									Integer premiKe = (Integer) mapUlink.get("MU_PREMI_KE");
									Integer topup = (Integer) mapUlink.get("LT_ID");//topup tunggal=2 berkala=5
									Integer flagTopup = null;
									if(topup==2){
										flagTopup = 1;
									}else if(topup==5){
										flagTopup = 2;
									}
									if(depPremi.getMsdp_flag_topup().equals(flagTopup)){
										i_premike = premiKe;
										lsPayId = services.selectSeqPaymentId();
										services.insertMstPayment(lsPayId, ldecKurs, depPremi);
										services.insertMstTagPayment(spaj, lsPayId, new Integer(1), i_premike, depPremi);
									}
								}
							}
						}
					}
				}
				//
				if(lbRet){
					if(liPmode!=0){
						if(logger.isDebugEnabled())logger.debug("ldtEnddate= "+formatter.format(ldtEndDate));
						Date nextBill=FormatDate.add(ldtEndDate,Calendar.DATE,1);
						if(logger.isDebugEnabled())logger.debug("ldtnext bill= "+formatter.format(nextBill));
						services.updateMstPolicyMspoNextBill(spaj,new Integer(1),nextBill);
					}	
				}
		
		return lbRet;
	}

	private int wf_set_nopol(Akseptasi akseptasi, Integer lspd_tujuan) {
		SpicaServices services = new SpicaServices();
		CommonUtils utils = new CommonUtils();
		
		DecimalFormat f1 = new DecimalFormat ("0");
		DecimalFormat f3 = new DecimalFormat ("000");
		String lsNopol;
		String mspo_policy_no_manual = services.selectPolicyNoFromSpajManualMstTempDMTM(akseptasi.getSpaj());
		
		if(mspo_policy_no_manual==null || mspo_policy_no_manual.equals("")){
			lsNopol = f_get_nopolis(akseptasi.getLcaId(),akseptasi.getLsbsId());
		}else{
			lsNopol = mspo_policy_no_manual;
		}
		
		if(lsNopol==null || lsNopol.length()<=0 ){
			return 51; //MESSAGEBOX('iNfo','No Polis is Null!!!')
		}
		
		String lsRegSpaj = services.selectMstPolicyRegSpaj(lsNopol);
		
		if(lsRegSpaj!=null){
			Integer i=lsNopol.length();
			Integer ldNo;
			
			if(akseptasi.getLcaId().equals("62")){
				ldNo=Integer.parseInt(lsNopol.substring(i-7,i));
			}else{
				ldNo=Integer.parseInt(lsNopol.substring(i-9,i));
			}
			
			String s_ld_no=f1.format(ldNo);
			
			if(ldNo>0){
				ldNo++;
				lsNopol = akseptasi.getLcaId()+ f3.format(akseptasi.getLsbsId())+ ldNo;
				
				lsRegSpaj = services.selectMstPolicyRegSpaj(lsNopol);
				if(lsRegSpaj!=null){
					return 52; //MessageBox( 'Pesan', 'Nomor Polis Kembar, Coba tranfer ulang...')
				}else{
					services.updateMstCntPolicy(akseptasi.getLsbsId(),akseptasi.getLcaId(),ldNo);
				}
			}
		}
		
		String lsNopolFormated = utils.nomorPolis(lsNopol);
		
		services.updateMstPolicyFormated(akseptasi.getSpaj(),lsNopol,lsNopolFormated);
		// update no blanko dan statusnya
		//update no blanko
		
		Integer liRekrutan = services.selectMstDetRekruter(akseptasi.getMsagId());
		if(liRekrutan != null)
			if(liRekrutan > 0)
				services.updateMstPolicyMspoPreExixting(akseptasi.getSpaj(), 1, lspd_tujuan, 1);
		
		akseptasi.setNoPolis(lsNopol);
		
		return 0;
	}
	
	public String f_get_nopolis(String lcaId, Integer lsbsId){
		SpicaServices services = new SpicaServices();
		
		Long ld_no = null, ld_max = null;
		long ll_th_now, ll_ctl = 0, ll_th = 0; 
		String  ls_nopol=null ;
		boolean baru;//,up_mst_cnt_polis=false,ins_mst_cnt_polis=false; 
		String s_ld_no="",s_as_cab="",s_th_now;
		String bisnisId;
		int i_ld_no=0,i_as_cab=0;
		baru      = false;
		Date tglNow;
		DecimalFormat fBisnis= new DecimalFormat ("000");
		bisnisId=fBisnis.format(lsbsId.intValue());
		//tglNow=selectSysdate(new Integer(1));
		tglNow=services.selectSysdateTrunc();
		ll_th_now = tglNow.getYear()+1900;
//		if(logger.isDebugEnabled())logger.debug("ll_th_now ="+ll_th_now);
		//
		Map cnt=services.selectMstCntPolis(lcaId,lsbsId);
		
		if(cnt==null){
			baru=true;
			ld_no=new Long(0);
			ld_max=new Long(0);
		}else{
			if(cnt.get("MSCNP_VALUE")==null)
				ld_no=null;
			else
				ld_no=Long.valueOf(cnt.get("MSCNP_VALUE").toString());
			//
			if(cnt.get("MSCNP_MAX")==null)
				ld_max=null;
			else
				ld_max=Long.valueOf(cnt.get("MSCNP_MAX").toString());
			
			if(ld_max.longValue() <= 0){ 
				ls_nopol=null;
				return ls_nopol;
			}else{ 
				if(ld_no==null || ld_max==null){
					ls_nopol=null;
					return ls_nopol;
				}
			}
			

		}
		//
		if(ld_no.longValue() >= ld_max.longValue())
			ld_no = new Long(0);
		//
		if(ld_no.longValue() == 0){
			if(lcaId.equals("62")){
				ll_th = Long.parseLong(Long.toString(ll_th_now).substring(2, 4)) ;
			}else{
				ll_th = ll_th_now;
			}
			ll_ctl = 1;
		}else{
			s_ld_no=String.valueOf(ld_no);
			if (s_ld_no.length()== 9 ){
				i_ld_no=s_ld_no.length();
				if(lcaId.equals("62")){
					s_th_now = Long.toString(ll_th_now);
					ll_th=Long.parseLong(s_ld_no.substring(0,2));
					ll_ctl = Long.parseLong(s_ld_no.substring(i_ld_no-7,i_ld_no));
					
					ll_ctl++;
					if(!s_ld_no.substring(0,2).equals(s_th_now.substring(2,4))){
						ll_th = Long.parseLong(s_th_now.substring(2,4));
						ll_ctl = 1;
					}
				}else{
					ll_th=Long.parseLong(s_ld_no.substring(0,4));
					ll_ctl = Long.parseLong(s_ld_no.substring(i_ld_no-5,i_ld_no));
					
					ll_ctl++;
					if(ll_th != ll_th_now){
						ll_th = ll_th_now;
						ll_ctl = 1;
					}
				}
				
				
			}
		}
		
		if(lcaId.equals("62")){
			ld_no = new Long((ll_th * 10000000) + ll_ctl);
		}else{
			ld_no = new Long((ll_th * 100000) + ll_ctl);
		}
		
		//
		s_as_cab=lcaId.trim();
		i_as_cab=s_as_cab.length();
		lcaId=s_as_cab.substring(i_as_cab-2,i_as_cab);
		
		if(baru){//insert
			services.insertMstCntPolis(lcaId,lsbsId,ld_no);
			
		}else{//update
			services.updateMstCntPolis(lcaId,lsbsId,ld_no);
		}
		//
		NumberFormat fmt = new DecimalFormat ("#");
		s_ld_no=fmt.format(ld_no);
		
		ls_nopol = lcaId+ bisnisId + ld_no;
		
		return ls_nopol;
	}
	
	public Date selectAddMonths(String tanggal,Integer month){
		Date date = null;
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		try{
			Date tanggal2 = (Date) formatter.parse(tanggal);
			date = selectAddDate(tanggal2, "mm", true, month.intValue());
		}catch(Exception e) {
			logger.error("ERROR :", e);
		}
		return date;
	}

	public static Date selectAddDate(Date date, String add, boolean trunc, int nilai) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		if("dd".equals(add)){
			cal.add(Calendar.DATE, nilai);
		}else if("mm".equals(add)){
			cal.add(Calendar.MONTH, nilai);
		}else if("yy".equals(add)){
			cal.add(Calendar.YEAR, nilai);
		}
		
		if(trunc) {
			setTimeToMinimum(cal);
		}
		return cal.getTime();
	}
	
	private static void setTimeToMinimum(Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
	}
	
	public Double selectGetKursJb(Date tglKurs,String asJb){
		SpicaServices services = new SpicaServices();
		Double ldec_kurs = new Double(1);

		Map param= new HashMap();
		param.put("lku_id","02");
		param.put("lkh_date",tglKurs);
		//
		if(asJb.equalsIgnoreCase("J")){ 		
			ldec_kurs=services.selectLkhKursJual(param);
		}else if(asJb.equalsIgnoreCase("B")){
			ldec_kurs=services.selectLkhKursBeli(param);
		}else{
			ldec_kurs=services.selectLkhCurrency(param);
		}
		//MessageBox('Information','Kurs tgl '+String(adt_tgl_kurs,'dd/mm/yyyy')+ ' tidak ada, '+&
		//			'harap hubungi CSF')
		return ldec_kurs;
	}
	
}
