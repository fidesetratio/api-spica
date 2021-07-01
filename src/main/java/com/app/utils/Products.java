package com.app.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.app.services.SpicaServices;

@Component
public class Products {

	private boolean isProductInCategory(String kategori, String businessID) {
		Properties props = new Properties();
		
		String[] array = kategori.split(",");
		for (int i = 0; i < array.length; i++)
			array[i] = array[i].trim();
		Set set = (Set) new HashSet(Arrays.asList(array));
		if (set.contains(FormatString.rpad("0", businessID, 3))) {
			return true;
		} else
			return false;		
	}
	
	public boolean muamalat(String reg_spaj) {
		SpicaServices services = new SpicaServices();
		
		ArrayList detBisnis = Common.serializableList(services.selectDetailBisnis(reg_spaj));
		int lsbs_id = Integer.valueOf((String) (Common.serializableMap((Map) detBisnis.get(0)).get("BISNIS")));
		int lsdbs_number = Integer.valueOf((String) (Common.serializableMap((Map) detBisnis.get(0)).get("DETBISNIS")));
		if((lsbs_id == 153 && lsdbs_number == 5) || lsbs_id == 170 || (lsbs_id == 171 && lsdbs_number == 1)) {
			return true;
		}else return false;
	}
	
	//termasuk didalamnya stable save dan stable save manfaat bulanan (beda dgn stable save premi bulanan)
		public boolean stableSave(int lsbs_id, int lsdbs_number){
			String stableSave = "184";
					
			if(isProductInCategory(stableSave, FormatString.rpad("0", String.valueOf(lsbs_id), 3))) {
				return true;
			}else{
				if(lsbs_id == 143 && (lsdbs_number == 4 || lsdbs_number == 5 || lsdbs_number == 6)){
					return true;
				}else if(lsbs_id == 144 && lsdbs_number == 4){
					return true;
				}else if(lsbs_id == 158 && (lsdbs_number == 13 || lsdbs_number == 15 || lsdbs_number == 16)){
					return true;
				}else if(lsbs_id == 184){
					return true;
				}else{
					return false;
				}
			}
		}
	
	public boolean stableLink(String businessID){
		String stableLink = "164,174,186";
		return isProductInCategory(stableLink, businessID);
	}
	
	public boolean powerSave(String businessID){
		String powerSave = "086,094,123,124,142,143,144,155,158,175,176,177,184,188,207";
		return isProductInCategory(powerSave, businessID);
	}
	
	public boolean unitLink(String businessId) {
		String unitLink = "077,084,087,097,100,101,102,107,113,115,116,117,118,119,120,121,122,127,128,129,134,138,139,140,141,152,153,154,159,160,162,164,165,166,174,186,190,191,199,200,202,213,215,216,217,218,220,224";
		return isProductInCategory(unitLink, businessId);
	}
	
	public boolean SuperSejahtera(String businessID){
		String superSejahtera = "039,185";
		return isProductInCategory(superSejahtera, businessID);
	}

}
