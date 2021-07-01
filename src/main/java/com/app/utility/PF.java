package com.app.utility;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Months;
import org.joda.time.Seconds;
import org.joda.time.Years;
import org.joda.time.chrono.GregorianChronology;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.app.constant.SpicaCons;
import com.app.model.AgentValidationProfile;
import com.app.model.BeneficiaryRelationValidProfile;
import com.app.model.DocumentStatus;
import com.app.model.GenderValidationProfile;
import com.app.model.HighRiskCountriesPp;
import com.app.model.HouseInquiry;
import com.app.model.InterBankInquiry;
import com.app.model.LstDetBisnis;
import com.app.model.LstProdset;
import com.app.model.LstProdsetCalc;
import com.app.model.LstSpicaList;
import com.app.model.LstValidationAgeParticipant;
import com.app.model.MedicalTableProfile;
import com.app.model.MstPeserta;
import com.app.model.MstProductInsured;
import com.app.model.ParticipantValidationProfile;
import com.app.model.PeriodValidationProfile;
import com.app.model.PremiumValidationProfile;
import com.app.model.ProductAgeValidationProfile;
import com.app.model.SumInsuredValidationProfile;
import com.app.model.TopUpSingleValidationProfile;
import com.app.request.HouseInquiryRequest;
import com.app.request.InterBankInquiryRequest;
import com.app.response.ApiResult;
import com.app.services.BniPaymentService;
import com.app.services.SpicaServices;
import com.app.services.TokenService;
import com.app.utils.Util;

import me.xdrop.fuzzywuzzy.FuzzySearch;
import springfox.documentation.service.ResponseMessage;

@Component
public class PF {
	
	@Autowired
	SqlSession sqlSession;
	
	@Value("${spring.urlDukcapil}")
    private String urlDukcapil;
	
	@Value("${spring.tokenDukcapil}")
    private String tokenDukcapil;
	
	private static String dukcapilUrl;
	private static String dukcapilToken;
	
	 @PostConstruct
	 public void init() {
		 PF.dukcapilUrl = urlDukcapil;
		 PF.dukcapilToken = tokenDukcapil;
	 }
	
	public static String NumberToText(Integer number) {
		
		String result = null;
		
		if(number != null) {
			result = number.toString();
		}
		
		return result;
	}
	
	public static String DateToText(Date date) {
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String result = null;
		
		if(date != null) {
			result = df.format(date);
		}
		
		return result;
	}
	
	public static String TimeToText(Time time) {
		
		String result = null;
		
		if(time != null) {
			LocalTime localTime = time.toLocalTime();
			
			result = localTime.toString();
		}
		
		return result;
	}
	
	public static String BooleanToText(Boolean input) {
		
		String result = null;
		
		if(input != null) {
			result = input.toString();
		}
		
		return result;
	}
	
	public static String Concatenate(String text1, String text2, String text3, String text4, String text5) {
		
		String result = null;
		
		if(text1 != "" && text1 != null) {
			result = text1;
			
			if(text2 != "" && text2 != null) {
				result += text2;
				
				if(text3 != "" && text3 != null) {
					result += text3;
					
					if(text4 != "" && text4 != null) {
						result += text4;
						
						if(text5 != "" && text5 != null) {
							result += text5;
						}
					}
				}
			}
		}
		
		return result;
	}
	
	public static String Replace(String text, String find, String replace) {
		
		String result = null;
		
		if(text != null && find != null && replace != null && text != "" && find != "" && replace != "") {
			result = text.replace(find, replace);
		}
		
		return result;
	}
	
	public static String Left(String text, Integer length) {
		
		String result = null;
		
		if(text != null && text != "" && length != null) {
			if(text.length() > length) {
				
				result = text.substring(0, length);
			}
			else {
				result = text;
			}
		}
		
		return result;
	}
	
	public static String Mid(String text, Integer index, Integer length) {
		
		String result = null;
		
		if(text != null && text != "" && index != null && length != null) {
			try {
				result = text.substring(index, length + 1);
			} catch (Exception e) {
				result = e.toString();
			}
		}
		
		return result;
	}
	
	public static String Right(String text, Integer length) {
		
		String result = null;
		
		if(text != null && text != "" && length != null) {
			if(text.length() > length) {
				
				result = text.substring(text.length() - length);
			}
			else {
				result = text;
			}
		}
		
		return result;
	}
	
	public static String PadLeft(String text, Integer length, String fill) {
		
		String result = null;
		
		try {
			result = StringUtils.leftPad(text, length, fill);
			
		} catch(Exception e) {
			result = e.toString();
		}
		
		return result;
	}
	
	public static String PadRight(String text, Integer length, String fill) {
		
		String result = null;
		
		if(text != null && text != "" && length != null && fill != null && fill != "") {
			try {
				result = StringUtils.rightPad(text, length, fill);
			} catch(Exception e) {
				result = e.toString();
			}
		}
		
		return result;
	}
	
	public static String Trim(String text) {
		
		String result = null;
		
		if(text != null && text != "") {
			result = text.trim();
		}
		
		return result;
	}
	
	public static String LTrim(String text) {
		
		String result = null;
		
		if(text != null && text != "") {
			result = StringUtils.stripStart(text, null);
		}
		
		return result;
	}
	
	public static String RTrim(String text) {
		
		String result = null;
		
		if(text != null && text != "") {
			result = StringUtils.stripEnd(text, null);
		}
		
		return result;
	}
	
	public static Integer TextToNumber(String text) {
		
		Integer result = null;
		
		if(text != null && text != "") {
			result = Integer.valueOf(text);
		}
		
		return result;
	}
	
	public static Integer DateNumber(Date date) {
		
		DateFormat df = new SimpleDateFormat("dd");
		Integer result = null;
		
		if(date != null) {
			result = Integer.valueOf(df.format(date));
		}
		
		return result;
	}
	
	public static Integer MonthNumber(Date date) {
		
		DateFormat df = new SimpleDateFormat("MM");
		Integer result = null;
		
		if(date != null) {
			result = Integer.valueOf(df.format(date));
		}
		
		return result;
	}

	public static Integer YearNumber(Date date) {
	
		DateFormat df = new SimpleDateFormat("yyyy");
		Integer result = null;
		
		if(date != null) {
			result = Integer.valueOf(df.format(date));
		}
		
		return result;
	}
	
	public static Integer HourNumber(Time time) {
		
		Integer result = null;
		
		if(time != null) {
			LocalTime localTime = time.toLocalTime();
			
			result = localTime.getHour();
		}
		
		return result;
	}
	
	public static Integer MinuteNumber(Time time) {
		
		Integer result = null;
		
		if(time != null) {
			LocalTime localTime = time.toLocalTime();
			
			result = localTime.getMinute();
		}
		
		return result;
	}
	
	public static Integer SecondNumber(Time time) {
		
		Integer result = null;
		
		if(time != null) {
			LocalTime localTime = time.toLocalTime();
			
			result = localTime.getSecond();
		}
		
		return result;
	}
	
	public static Integer DateDiff(Date date1, Date date2) {
		
		Integer result = null;
	     
		if(date1 != null && date2 != null) {
		     DateTime firstDate = new DateTime(date1, GregorianChronology.getInstance());
		     DateTime secondDate = new DateTime(date2, GregorianChronology.getInstance());
		     
		     Days diffInDays = Days.daysBetween(secondDate, firstDate);
		     
		     result = diffInDays.getDays();
		}
	    
		return result;
	}
	
	public static Integer MonthDiff(Date date1, Date date2) {
		
		Integer result = null;
	     
		if(date1 != null && date2 != null) {
		     DateTime firstDate = new DateTime(date1, GregorianChronology.getInstance());
		     DateTime secondDate = new DateTime(date2, GregorianChronology.getInstance());
		     
		     Months diffInDays = Months.monthsBetween(secondDate, firstDate);
		     
		     result = diffInDays.getMonths();
		}
	    
		return result;
	}
	
	public static Integer YearDiff(Date date1, Date date2) {
		
		Integer result = null;
	     
		if(date1 != null && date2 != null) {
		     DateTime firstDate = new DateTime(date1, GregorianChronology.getInstance());
		     DateTime secondDate = new DateTime(date2, GregorianChronology.getInstance());
		     
		     Years diffInDays = Years.yearsBetween(secondDate, firstDate);
		     
		     result = diffInDays.getYears();
		}
	    
		return result;
	}
	
	public static Integer HourDiff(Time time1, Time time2) {
		
		Integer result = null;
	     
		if(time1 != null && time2 != null) {
		     DateTime firstDate = new DateTime(time1, GregorianChronology.getInstance());
		     DateTime secondDate = new DateTime(time2, GregorianChronology.getInstance());
		     
		     Hours diffInDays = Hours.hoursBetween(secondDate, firstDate);
		     
		     result = diffInDays.getHours();
		}
	    
		return result;
	}
	
	public static Integer MinuteDiff(Time time1, Time time2) {
		
		Integer result = null;
	     
		if(time1 != null && time2 != null) {
		     DateTime firstDate = new DateTime(time1, GregorianChronology.getInstance());
		     DateTime secondDate = new DateTime(time2, GregorianChronology.getInstance());
		     
		     Minutes diffInDays = Minutes.minutesBetween(secondDate, firstDate);
		     
		     result = diffInDays.getMinutes();
		}
	    
		return result;
	}
	
	public static Integer SecondDiff(Time time1, Time time2) {
		
		Integer result = null;
	     
		if(time1 != null && time2 != null) {
		     DateTime firstDate = new DateTime(time1, GregorianChronology.getInstance());
		     DateTime secondDate = new DateTime(time2, GregorianChronology.getInstance());
		     
		     Seconds diffInDays = Seconds.secondsBetween(secondDate, firstDate);
		     
		     result = diffInDays.getSeconds();
		}
	    
		return result;
	}
	
	public static Long Add(Long number1, Long number2) {
		
		Long result = null;
		
		if(number1 != null && number2 != null) {
			result = number1 + number2;
		}
		
		return result;
	}
	
	public static Long Substract(Long number1, Long number2) {
		
		Long result = null;
		
		if(number1 != null && number2 != null) {
			result = number1 - number2;
		}
		
		return result;
	}
	
	public static Long Multiply(Long number1, Long number2) {
		
		Long result = null;
		
		if(number1 != null && number2 != null) {
			result = number1 * number2;
		}
		
		return result;
	}
	
	public static Long Divide(Long number1, Long number2) {
		
		Long result = null;
		
		if(number1 != null && number2 != null) {
			result = number1 / number2;
		}
		
		return result;
	}
	
	public static Integer Power(Integer number1, Integer number2) {
		
		Integer result = null;
		
		if(number1 != null && number2 != null) {
			result = 1;
			
			while (number2 != 0)
	        {
	            result *= number1;
	            --number2;
	        }
		}
		
		return result;
	}
	
	public static Double Ceil(Double number, Integer digit) {
		
		Double result = null;
		
		if(number != null && digit != null) {
			BigDecimal bigDecimal = new BigDecimal(Double.toString(number));
	        bigDecimal = bigDecimal.setScale(digit, RoundingMode.CEILING);
	        
	        result = bigDecimal.doubleValue();
		}
		
		return result;
	}
	
	public static Double Floor(Double number, Integer digit) {
		
		Double result = null;
		
		if(number != null && digit != null) {
			BigDecimal bigDecimal = new BigDecimal(Double.toString(number));
	        bigDecimal = bigDecimal.setScale(digit, RoundingMode.FLOOR);
	        
	        result = bigDecimal.doubleValue();
		}
		
		return result;
	}
	
	public static Integer Length(String text) {
		
		Integer result = null;
		
		if(text != null & text != "") {
			result = text.length();
		}
		
		return result;
	}
	
	public static Double Round(Double number, Integer digit) {
		
		Double result = null;
		
		if(number != null & digit != null) {
			BigDecimal bigDecimal = new BigDecimal(Double.toString(number));
	        bigDecimal = bigDecimal.setScale(digit, RoundingMode.HALF_EVEN);
	        
	        result = bigDecimal.doubleValue();
		}
		
		return result;
	}
	
	public static Integer Sum(Integer number1, Integer number2, Integer number3, Integer number4, Integer number5,
			Integer number6, Integer number7, Integer number8, Integer number9, Integer number10) {
		
		Integer result = null;
		
		if(number1 != null) {
			result = number1;
			
			if(number2 != null) {
				result += number2;
				
				if(number3 != null) {
					result += number3;
					
					if(number4 != null) {
						result += number4;
						
						if(number5 != null) {
							result += number5;
							
							if(number6 != null) {
								result += number6;
								
								if(number7 != null) {
									result += number7;
									
									if(number8 != null) {
										result += number8;
										
										if(number9 != null) {
											result += number9;
											
											if(number10 != null) {
												result += number10;
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		return result;
	}
	
	public static Date Today(){
		
		Date result = null;
		
		LocalDate localDate = LocalDate.now();
	    result = java.sql.Date.valueOf(localDate);
		
		return result;
	}
	
	public static Date TextToDate(String date) throws ParseException {
		
		Date result = null;
		
		if(date != null && date != "") {
			LocalDate localDate = LocalDate.parse(date);
			result = java.sql.Date.valueOf(localDate); 
		}
		
		return result;
	}
	
	public static Date NumbersToDate(Integer date, Integer month, Integer year) {
		
		Date result = null;
		
		if(date != null && month != null && year != null) {
			LocalDate localDate = LocalDate.of(year, month, date);
			result = java.sql.Date.valueOf(localDate);
		}
		
		return result;
		
	}
	
	public static Date DateAdd(Date date, Integer number) {
		
		Date result = null;
		
		if(date != null & number != null) {
			DateTime dateTime = new DateTime(date);
			dateTime = dateTime.plusDays(number);
			
			result = dateTime.toDate();
		}
		
		return result;
	}
	
	public static Date MonthAdd(Date date, Integer number) {
		
		Date result = null;
		
		if(date != null && number != null) {
			DateTime dateTime = new DateTime(date);
			dateTime = dateTime.plusMonths(number);
			
			result = dateTime.toDate();
		}
		
		return result;
	}
	
	public static Date YearAdd(Date date, Integer number) {
		
		Date result = null;
		
		if(date != null && number != null) {
			DateTime dateTime = new DateTime(date);
			dateTime = dateTime.plusYears(number);
			
			result = dateTime.toDate();
		}
		
		return result;
	}
	
	public static Time Now(){
		
		Time result = null;
		
		LocalTime time = LocalTime.now();
	    result = Time.valueOf(time);
		
		return result;
	}
	
	public static Time TextToTime(String time) throws ParseException {
		
		Time result = null;
		DateFormat formatter = new SimpleDateFormat("HH:mm");
		
		if(time != null && time != "") {
			result = new java.sql.Time(formatter.parse(time).getTime());
		}
		
		return result;
	}
	
	public static Time NumbersToTime(Integer hour, Integer minute, Integer second) {
		
		Time result = null;
		
		if(hour != null && minute != null && second != null) {
			LocalTime time = LocalTime.of(hour, minute, second);
			result = Time.valueOf(time);
		}
		
		return result;
		
	}
	
	public static Time HourAdd(Time time, Integer number) {
		
		Time result = null;
		
		if(time != null && number != null) {
			LocalTime localTime = time.toLocalTime();
			
			result = Time.valueOf(localTime.plusHours(number));
		}
		
		return result;
	}
	
	public static Time MinuteAdd(Time time, Integer number) {
		
		Time result = null;
		
		if(time != null && number != null) {
			LocalTime localTime = time.toLocalTime();
			
			result = Time.valueOf(localTime.plusMinutes(number));
		}
		
		return result;
	}

	public static Time SecondAdd(Time time, Integer number) {
	
		Time result = null;
		
		if(time != null && number != null) {
			LocalTime localTime = time.toLocalTime();
			
			result = Time.valueOf(localTime.plusSeconds(number));
		}
		
		return result;
	}
	
	public static Boolean TextToBoolean(String text) {
		
		Boolean result = null;
		
		if(text != null && text != "") {
			result = Boolean.valueOf(text);
		}
		
		return result;
	}
	
	public static Boolean TextEquals(String text1, String text2) {
		
		Boolean result = false;
		
		if(text1 != null && text2 != null && text1 != "" && text2 != "") {
			result = text1.equalsIgnoreCase(text2);
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Boolean BooleanIsTrue(Boolean bool) {
		
		Boolean result = false;
		
		if(bool != null) {
			if(bool == true) {
				result = true;
			} else {
				result = false;
			}
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Boolean Contains(String text, String subtext) {
		
		Boolean result = null;
		
		if(text != null && subtext != null && text != "" && subtext != "") {
			result = text.contains(subtext);
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Boolean BeginsWith(String text, String subtext) {
		
		Boolean result = null;
		
		if(text != null && subtext != null && text != "" && subtext != "") {
			result = text.startsWith(subtext);
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Boolean EndsWith(String text, String subtext) {
		
		Boolean result = null;
		
		if(text != null && subtext != null && text != "" && subtext != "") {
			result = text.endsWith(subtext);
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Boolean NumberEquals(Long number1, Long number2) {
		
		Boolean result = false;
		
		if (number1 != null && number2 != null) {
			result = number1.equals(number2);
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Boolean IsGreaterThan(Long number1, Long number2) {
		
		Boolean result = null;
		
		if(number1 != null && number2 != null) {
			if(number1 > number2) {
				result = true;
			} else {
				result = false;
			}
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Boolean IsGreaterThanOrEquals(Long number1, Long number2) {
		
		Boolean result = null;
		
		if(number1 != null && number2 != null) {
			if(number1 >= number2) {
				result = true;
			} else {
				result = false;
			}
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Boolean IsLessThan(Long number1, Long number2) {
		
		Boolean result = null;
		
		if(number1 != null && number2 != null) {
			if(number1 < number2) {
				result = true;
			} else {
				result = false;
			}
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Boolean IsLessThanOrEquals(Long number1, Long number2) {
		
		Boolean result = false;
		
		if(number1 != null && number2 != null) {
			if(number1 <= number2) {
				result = true;
			} else {
				result = false;
			}
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Boolean DateEquals(Date date1, Date date2) {
		
		Boolean result = null;
		
		if (date1 != null && date2 != null) {
			LocalDate localDate1 = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate localDate2 = date2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			
			result = localDate1.isEqual(localDate2);
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Boolean DateIsAfter(Date date1, Date date2) {
		
		Boolean result = null;
		
		if (date1 != null && date2 != null) {
			LocalDate localDate1 = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate localDate2 = date2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			
			result = localDate1.isAfter(localDate2);
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Boolean DateIsBefore(Date date1, Date date2) {
		
		Boolean result = null;
		
		if (date1 != null && date2 != null) {
			LocalDate localDate1 = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate localDate2 = date2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			
			result = localDate1.isBefore(localDate2);
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Boolean DateIsBetween(Date date, Date dateFrom, Date dateTo) {
		
		Boolean result = null;
		
		if (date != null && dateFrom != null && dateTo != null) {
			LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate localDateFrom = dateFrom.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate localDateTo = dateTo.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			
			result = localDate.isAfter(localDateFrom) && localDate.isBefore(localDateTo);
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Boolean TimeEquals(Time time1, Time time2) {
		
		Boolean result = null;
		
		if(time1 != null && time2 != null) {
			Integer diff = null;
			
			LocalTime localTime1 = time1.toLocalTime();
			LocalTime localTime2 = time2.toLocalTime();
			
			diff = localTime1.compareTo(localTime2);
			
			if(diff == 0) {
				result = true;
			} else {
				result = false;
			}
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Boolean TimeIsAfter(Time time1, Time time2) {
		
		Boolean result = null;
		
		if(time1 != null && time2 != null) {
			LocalTime localTime1 = time1.toLocalTime();
			LocalTime localTime2 = time2.toLocalTime();
			
			result = localTime1.isAfter(localTime2);
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Boolean TimeIsBefore(Time time1, Time time2) {
		
		Boolean result = null;
		
		if(time1 != null && time2 != null) {
			LocalTime localTime1 = time1.toLocalTime();
			LocalTime localTime2 = time2.toLocalTime();
			
			result = localTime1.isBefore(localTime2);
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Boolean TimeIsBetween(Time time, Time timeFrom, Time timeTo) {
		
		Boolean result = null;
		
		if(time != null && timeFrom != null && timeTo != null) {
			LocalTime localTime = time.toLocalTime();
			LocalTime localTimeFrom = timeFrom.toLocalTime();
			LocalTime localTimeTo = timeTo.toLocalTime();
			
			result = localTime.isAfter(localTimeFrom) && localTime.isBefore(localTimeTo);
		} else {
			result = false;
		}
		
		return result;
	}
	
	//Predefined Function Tambahan
	
	public static Double Percentage(Long number1, Long number2) {
		
		Double result = null;
		
		if(number1 != null && number2 != null) {
			Double double1 = number1.doubleValue();
			Double double2 = number2.doubleValue();
			
			result = double1 / double2;
			result = result * 100;
		}
		
		return result;
	}
	
	public static Boolean ListContains(String fieldValue, String listName) {
		
		Boolean result = null;
		
		if (fieldValue != null && fieldValue != "") {
			SpicaServices services = new SpicaServices();
			
			String list_source;
			String list_field_name;
			
			LstSpicaList lstSpicaList = services.selectList(listName);
			
			list_source = lstSpicaList.getLspc_list_source();
			list_field_name = lstSpicaList.getLspc_list_field_name();
			
			ArrayList<Object> value = services.getListFieldValue(list_source, list_field_name, fieldValue);
	
			if (!value.isEmpty()) {
				result = true;
			} else {
				result = false;
			}
		} else {
			result = false;
		}
		
		return result;
		
	}
	
	public static Boolean ListUniqueContains(String fieldValue, String listName) {
		
		Boolean result = null;
		
		if (fieldValue != null && fieldValue != "") {
			SpicaServices services = new SpicaServices();
	
			String list_source;
			String list_field_name;
			
			LstSpicaList lstSpicaList = services.selectList(listName);
			
			list_source = lstSpicaList.getLspc_list_source();
			list_field_name = lstSpicaList.getLspc_list_field_name();
			
			ArrayList<Object> value = services.getListUniqueFieldValue(list_source, list_field_name, fieldValue);
			
			//if value == 1 then list validation passed, else not passed
			if (value.size() == 1 ) {
				result = true;
			} else {
				result = false;
			}
		} else {
			result = false;
		}
		
		return result;
		
	}
	
	public static Boolean DukcapilValid(String nik, String name, Date dob, String gender, String reg_spaj) throws Exception {
		
		Boolean result = null;
		
		if(nik != null && nik != "" && name != null && name != "" && dob != null && gender != null && gender != "" && reg_spaj != null && reg_spaj != "" ) {
			final String uri = dukcapilUrl;
		    RestTemplate restTemplate = new RestTemplate();
		    JSONObject request = new JSONObject();
		    
		    String pattern = "dd-MM-yyyy";
	    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
	    	
	    	String stringDOB = simpleDateFormat.format(dob);
	    	String genderFormatted;
	    	
	    	//set gender menyesuaikan format dukcapil
	    	if(gender.equalsIgnoreCase("Pria")) {
	    		genderFormatted = "Laki-Laki";
	    	} else {
	    		genderFormatted = "Perempuan";
	    	}
		     
		    HttpHeaders headers = new HttpHeaders();
		    headers.add("Authorization", dukcapilToken);
		    headers.add("Content-Type", "application/json");
		    
		    request.put("USER_ID", "0");
			request.put("KODE_DATA", "1");
			request.put("REG_SPAJ", reg_spaj);
			request.put("NIK", nik);
			request.put("NAMA_LGKP", name);
			request.put("TGL_LHR", stringDOB);
			request.put("JENIS_KLMIN", genderFormatted);
		 
			HttpEntity<String> entity = new HttpEntity<>(request.toString(), headers);
		     
		    ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
		    
		    if(response.getStatusCodeValue() == 200) {
		    
			    JSONObject jsonResponse = new JSONObject(response.getBody());
			    
			    if(jsonResponse.getBoolean("error") == false){
				    String hasil_nama = jsonResponse.getJSONObject("data").getString("NAMA_LGKP").substring(0,jsonResponse.getJSONObject("data").getString("NAMA_LGKP").indexOf(" "));
				    String hasil_dob = jsonResponse.getJSONObject("data").getString("TGL_LHR");
				    String hasil_gender = jsonResponse.getJSONObject("data").getString("JENIS_KLMIN");				    
				    
				    if(hasil_nama.equalsIgnoreCase("Sesuai") && hasil_dob.equalsIgnoreCase("Sesuai") && hasil_gender.equalsIgnoreCase("Sesuai")) {
				    	result = true;
				    } else {
				    	result = false;
				    }
			    } else {
			    	result = false;
			    }
		    } else {
		    	result = false;
		    	throw new Exception("Error Hit API Dukcapil"); 
		    }
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Boolean OfacScreenValid(String reg_spaj, String mofs_type) {
		
		Boolean result = null;
		
		if(reg_spaj != null && reg_spaj != "" && mofs_type != null && mofs_type != "") {
			SpicaServices services = new SpicaServices();
			
			ArrayList<String> screenNotPassed = services.selectOfacScreenStatusNotPassed(reg_spaj, mofs_type);
			
			if(screenNotPassed.isEmpty()) {
				result = true;
			} else {
				result = false;
			}
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Boolean ScreenValid(String reg_spaj, String screening_name, String profile_target) {
		
		Boolean result = null;
		
		if(reg_spaj != null && reg_spaj != "" && screening_name != null && screening_name != "" && profile_target != null && profile_target != "") {
			SpicaServices services = new SpicaServices();
			
			String screenNotPassed = services.selectScreenDescNotPassed(reg_spaj, screening_name, profile_target);
			
			if(screenNotPassed == null) {
				result = true;
			} else {
				result = false;
			}
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Long TotalPremiInforce(String mcl_id) {
		
		Long result = (long) 0;
		
		if(mcl_id != null && mcl_id != "") {
			SpicaServices services = new SpicaServices();
			SpicaCons cons = new SpicaCons();
			
			//SELECT NILAI KONVERSI KE IDR UNTUK PREMI USD
			Long rate_idr = services.selectRateIdr(cons.RUPIAH_RATE);
			
			//SELECT REG SPAJ EXISTING
			ArrayList<String> reg_spaj_existing = services.selectAllRegSpajInforceByIdSimultan(mcl_id);
			
			if(!reg_spaj_existing.isEmpty()) {
				for(String resultSpaj : reg_spaj_existing) {
					Long premi;
					MstProductInsured mstProductInsuredInforce = services.selectPremiKursAndCaraBayar(resultSpaj);
					
					if(mstProductInsuredInforce.getLku_id().contentEquals("01")) {
						premi = mstProductInsuredInforce.getMspr_premium();
					} else {
						premi = mstProductInsuredInforce.getMspr_premium() * rate_idr;
					}
					
					if(mstProductInsuredInforce.getLscb_id() == 1) {
						premi = premi * 4;
					} else if(mstProductInsuredInforce.getLscb_id() == 2) {
						premi = premi * 2;
					} else if(mstProductInsuredInforce.getLscb_id() == 6) {
						premi = premi * 12;
					}
					
					result += premi;
				}
			}
		}
		
		return result;
	}
	
	public static Boolean ValueIsNull(Object param) {
		
		Boolean result = null;
		
		if(param == null) {
			result = true;
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Boolean BeneficiaryRelationValid(String reg_spaj) {
		
		Boolean result = null;
		
		if(reg_spaj != null && reg_spaj != "") {
			SpicaServices services = new SpicaServices();
			
			LocalDate currentDate = LocalDate.now();
			
			ArrayList<BeneficiaryRelationValidProfile> profile = services.selectBeneficiaryRelationValidProfile(reg_spaj);
			
			if(profile.isEmpty()) {
				result = true;
			} else {
				
				LocalDate birthDate = profile.get(0).getMspe_date_birth().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				
				Integer usiaTU = Period.between(birthDate, currentDate).getYears();
				
				Integer mspe_sts_mrt = profile.get(0).getMspe_sts_mrt();

				for(BeneficiaryRelationValidProfile res : profile) {
					//USIA sampai 17 tahun, else usia lebih dari 17 tahun
					if(usiaTU <= 17) {
						if(res.getLsre_relation().equalsIgnoreCase("Orang tua kandung") || res.getLsre_relation().equalsIgnoreCase("Adik/Kakak Kandung") ||
								res.getLsre_relation().equalsIgnoreCase("Paman/Bibi") || res.getLsre_relation().equalsIgnoreCase("Nenek/Kakek Kandung")) {
							result = true;
						} else {
							result = false;
							break;
						}
					} else {
						if(mspe_sts_mrt == 1) {
							if(res.getLsre_relation().equalsIgnoreCase("Orang tua kandung") || res.getLsre_relation().equalsIgnoreCase("Adik/Kakak Kandung") ||
									res.getLsre_relation().equalsIgnoreCase("Paman/Bibi") || res.getLsre_relation().equalsIgnoreCase("Nenek/Kakek Kandung")) {
								result = true;
							} else {
								result = false;
								break;
							}
						} else if(mspe_sts_mrt == 2) {
							if(res.getLsre_relation().equalsIgnoreCase("Anak kandung") || res.getLsre_relation().equalsIgnoreCase("Suami/Istri")) {
								result = true;
							} else {
								result = false;
								break;
							}
						} else if(mspe_sts_mrt == 3) {
							if(res.getLsre_relation().equalsIgnoreCase("Orang tua kandung") || res.getLsre_relation().equalsIgnoreCase("Adik/Kakak Kandung") ||
									res.getLsre_relation().equalsIgnoreCase("Paman/Bibi") || res.getLsre_relation().equalsIgnoreCase("Nenek/Kakek Kandung") ||
									res.getLsre_relation().equalsIgnoreCase("Anak kandung")) {
								result = true;
							} else {
								result = false;
								break;
							}
						} else if(mspe_sts_mrt == 4) {
							if(res.getLsre_relation().equalsIgnoreCase("Orang tua kandung") || res.getLsre_relation().equalsIgnoreCase("Adik/Kakak Kandung") ||
									res.getLsre_relation().equalsIgnoreCase("Paman/Bibi") || res.getLsre_relation().equalsIgnoreCase("Nenek/Kakek Kandung") ||
									res.getLsre_relation().equalsIgnoreCase("Anak kandung")) {
								result = true;
							} else {
								result = false;
								break;
							}
						}
					}
				}
			}
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Boolean QuestionnaireValid(String reg_spaj, Integer option_group) {
		
		Boolean result = null;
		
		if(reg_spaj != null && reg_spaj != "" && option_group != null) {
			SpicaServices services = new SpicaServices();
			
			ArrayList<String> yesAnswer = services.selectYesAnswerFromQuestionnaire(reg_spaj, option_group);
			
			if(yesAnswer.isEmpty()) {
				result = true;
			} else {
				for(String res : yesAnswer) {
					if(res != null) {
						if(res.equalsIgnoreCase("1")) {
							result = false;
							break;
						} else {
							result = true;
						}
					} else {
						result = true;
					}
				}
			}	
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Boolean MedicalHistoryValid(String reg_spaj) {
		
		Boolean result = true;
		
		if(reg_spaj != null && reg_spaj != "") {
			SpicaServices services = new SpicaServices();
			Boolean holder_cover = false;
			
			ArrayList<String> reg_spaj_inforce = services.selectAllRegSpajInforceByIdSimultanInsured(reg_spaj);
			
			ArrayList<MstProductInsured> allProductInsured = services.selectAllProductInsured(reg_spaj);
			
			if(!allProductInsured.isEmpty()) {
				for(MstProductInsured result_product : allProductInsured) {
					String lbs_sar_pp = services.selectLbsSarPP(result_product.getLku_id(), result_product.getLsbs_id());
					
					if(lbs_sar_pp != null) {
						holder_cover = true;
						break;
					}
				}
			}
			
			if(!reg_spaj_inforce.isEmpty()) {
				for(String resultSpaj : reg_spaj_inforce) {
					ArrayList<String> yesAnswer = services.selectYesAnswerFromQuestionnaire(resultSpaj, 2);
					
					if(!yesAnswer.isEmpty()) {
						for(String res : yesAnswer) {
							if(res != null) {
								if(res.equalsIgnoreCase("1")) {
									result = false;
									break;
								}
							}
						}
						
						if(result == false) {
							break;
						}
					}
				}
			}
			
			if(result == true && holder_cover == true) {
				ArrayList<String> reg_spaj_inforce_holder = services.selectAllRegSpajInforceByIdSimultanHolder(reg_spaj);
				
				if(!reg_spaj_inforce_holder.isEmpty()) {
					for(String resultSpajHolder : reg_spaj_inforce_holder) {
						ArrayList<String> yesAnswer = services.selectYesAnswerFromQuestionnaire(resultSpajHolder, 2);
						
						if(!yesAnswer.isEmpty()) {
							for(String res : yesAnswer) {
								if(res != null) {
									if(res.equalsIgnoreCase("1")) {
										result = false;
										break;
									}
								}
							}
							
							if(result == false) {
								break;
							}
						}
					}
				}
			}
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Boolean ChurningValid(String mcl_id, Date spaj_date) {
		
		Boolean result = null;
		
		if(mcl_id != null && mcl_id != "" && spaj_date != null) {
			SpicaServices services = new SpicaServices();
			
			Date churningLimitDate = new DateTime(spaj_date).minusMonths(6).toDate();
			
			ArrayList<String> spajByPolicyHolder = services.selectChurningSpajByPolicyHolder(mcl_id, churningLimitDate);
			
			if(spajByPolicyHolder.isEmpty()) {
				result = true;
				
				ArrayList<String> spajByInsured = services.selectChurningSpajByInsured(mcl_id, churningLimitDate);
				
				if(spajByInsured.isEmpty()) {
					result = true;
				} else {
					result = false;
				}
			} else {
				result = false;
			}
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Boolean BeneficiaryLegalEntityRelationValid(String reg_spaj) {
		
		Boolean result = null;
		
		if(reg_spaj != null && reg_spaj != "") {
			SpicaServices services = new SpicaServices();
			
			ArrayList<BeneficiaryRelationValidProfile> relation = services.selectBeneficiaryRelationValidProfile(reg_spaj);
			
			if(relation.isEmpty()) {
				result = true;
			} else {
				for(BeneficiaryRelationValidProfile res : relation) {
					if(res.getLsre_relation().equalsIgnoreCase("Orang tua kandung") || res.getLsre_relation().equalsIgnoreCase("Adik/Kakak Kandung") ||
							res.getLsre_relation().equalsIgnoreCase("Anak kandung") || res.getLsre_relation().equalsIgnoreCase("Suami/Istri") ||
							res.getLsre_relation().equalsIgnoreCase("Kerja/Majikan-Karyawan")) {
						result = true;
					} else {
						result = false;
						break;
					}
				}
			}
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Boolean MedicalTableValid(String reg_spaj) {
		
		Boolean result = true;
		
		if(reg_spaj != null && reg_spaj != "") {
			SpicaServices services = new SpicaServices();
			String description;
			String tabel_medis_insured = null;
			Integer ltm_id = null;
			
			//SELECT SAR MEDIS PEMEGANG
			MedicalTableProfile pemegang = services.selectMedicalTableProfileHolder(reg_spaj);
			
			//SELECT SAR MEDIS TERTANGGUNG UTAMA
			MedicalTableProfile tertanggung_utama = services.selectMedicalTableProfileInsured(reg_spaj);
			
			//SELECT STATUS DOCUMENT
			DocumentStatus document_status = services.selectDocumentStatusStatic(reg_spaj);
			
			//Jika pemegang polis adalah tertanggung utama atau sar medis pemegang tidak di hitung maka hanya proses table medis tertanggung utama
			if(pemegang.getLsre_id() == 1 || pemegang.getSar_medis() <= 0) {
				HashMap<String, Object> map_tabel_medis_insured = services.selectMedicalTableInsured(reg_spaj, tertanggung_utama.getFlag_vip(), tertanggung_utama.getLku_id(), tertanggung_utama.getLstb_id(), tertanggung_utama.getMste_age(), tertanggung_utama.getMste_beg_date(), tertanggung_utama.getSar_medis());
				
				ltm_id = Integer.parseInt(map_tabel_medis_insured.get("LTM_ID").toString());
				tabel_medis_insured = map_tabel_medis_insured.get("TIPE_MEDIS").toString();
				
				//IF Medical Table found, else return false
				if(tabel_medis_insured != null) {
					if(!tabel_medis_insured.equalsIgnoreCase("NM")) {
						result = false;
					}
				} else {
					result = false;
					
					if(tertanggung_utama.getMste_age() <= 5) {
						tabel_medis_insured = "NA";
					} else {
						tabel_medis_insured = "TABEL MEDIS TIDAK ADA";
					}
				}
				
				//TABEL MEDIS REGULER, ELSE VIP
				if(pemegang.getFlag_vip() == 0) {
					description = "TU: SAR MEDIS " + tertanggung_utama.getSar_medis() + " REGULER " + tabel_medis_insured;
				} else {		
					description = "TU: SAR MEDIS " + tertanggung_utama.getSar_medis() + " VIP " + tabel_medis_insured;
				}
				
				//INSERT INTO MST POSITION SPAJ FOR UW INFO
				services.insertMstPositionSpajStatic(reg_spaj, document_status.getLspd_id(), document_status.getLssp_id(), document_status.getLssa_id(), 0, description);
				
				//UPDATE MST_INSURED.JNS_MEDIS
				services.updateJnsMedis(reg_spaj, ltm_id);
				
			} else {
				String tabel_medis_holder = services.selectMedicalTableHolder(reg_spaj, pemegang.getFlag_vip(), pemegang.getLku_id(), pemegang.getLstb_id(), pemegang.getMspo_age(), pemegang.getMste_beg_date(), pemegang.getSar_medis());
				
				HashMap<String, Object> map_tabel_medis_insured = services.selectMedicalTableInsured(reg_spaj, tertanggung_utama.getFlag_vip(), tertanggung_utama.getLku_id(), tertanggung_utama.getLstb_id(), tertanggung_utama.getMste_age(), tertanggung_utama.getMste_beg_date(), tertanggung_utama.getSar_medis());
				
				ltm_id = Integer.parseInt(map_tabel_medis_insured.get("LTM_ID").toString());
				tabel_medis_insured = map_tabel_medis_insured.get("TIPE_MEDIS").toString();
				
				//IF Medical Table found, else return false
				if(tabel_medis_holder != null && tabel_medis_insured != null) {
					if(!tabel_medis_holder.equalsIgnoreCase("NM")) {
						result = false;
					}
				} else {
					result = false;
					
					if(pemegang.getMspo_age() <= 5) {
						tabel_medis_holder = "NA";
					} else {
						tabel_medis_holder = "TABEL MEDIS TIDAK ADA";
					}
				}
				
				if(tabel_medis_insured != null) {
					if(!tabel_medis_insured.equalsIgnoreCase("NM")) {
						result = false;
					}
				} else {
					result = false;
					
					if(tertanggung_utama.getMste_age() <= 5) {
						tabel_medis_insured = "NA";
					} else {
						tabel_medis_insured = "TABEL MEDIS TIDAK ADA";
					}
				}
				
				//TABEL MEDIS REGULER, ELSE VIP
				if(pemegang.getFlag_vip() == 0) {
					description = "PP: SAR MEDIS " + pemegang.getSar_medis() + " REGULER " + tabel_medis_holder + ", TU: SAR MEDIS " + tertanggung_utama.getSar_medis() + " REGULER " + tabel_medis_insured;
				} else {	
					description = "PP: SAR MEDIS " + pemegang.getSar_medis() + " VIP " + tabel_medis_holder + ", TU: SAR MEDIS " + tertanggung_utama.getSar_medis() + " VIP " + tabel_medis_insured;
				}
				
				//INSERT INTO MST POSITION SPAJ FOR UW INFO
				services.insertMstPositionSpajStatic(reg_spaj, document_status.getLspd_id(), document_status.getLssp_id(), document_status.getLssa_id(), 0, description);
				
				//UPDATE MST_INSURED.JNS_MEDIS
				services.updateJnsMedis(reg_spaj, ltm_id);
			}
			
			//UNTUK TESTING TABEL MEDIS
//			System.out.println("UMUR PP: " + pemegang.getMspo_age());
//			System.out.println("UMUR TTG: " + tertanggung_utama.getMste_age());
//			System.out.println(description);
			
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Boolean BankInquiry(String account_name, String account_number, String bank_code, Integer thresholdFuzzy) throws Exception {
		
		Boolean result = null;
		
		if(account_name != null && account_name != "" && account_number != null && account_number != "" && bank_code != null && bank_code != "" && 
				thresholdFuzzy != null) {
			    
			//CHECK IF BANK CODE IS BNI
			if(bank_code.equalsIgnoreCase("009") || bank_code.equalsIgnoreCase("427")) {
				
				ResponseEntity<Object> inhouseInquiryResult = GetInhouseInquiry(account_number);
				
				if(inhouseInquiryResult.getStatusCodeValue() == 200) {
				
					JSONObject jsonResult = new JSONObject(inhouseInquiryResult.getBody());
					
					String account_status = jsonResult.getJSONObject("result").getString("accountStatus");
					
					if(account_status.equalsIgnoreCase("BUKA")) {
						
						String name_result = jsonResult.getJSONObject("result").getString("customerName");
						
						//check name fuzzy
				    	Integer fuzzyNameResult = FuzzySearch.tokenSetRatio(account_name, name_result);
				    	
				    	if(fuzzyNameResult > thresholdFuzzy) {
				    		result = true;
				    	} else {
				    		result = false;
				    	}
					} else {
						result = false;
					}
				} else {
					result = false;
				}
				
			 } else {
				 
				 ResponseEntity<Object> interbankInquiryResult = GetInterBankInquiry(account_number, bank_code);
				 
				 if(interbankInquiryResult.getStatusCodeValue() == 200) {
					 
					 JSONObject jsonResult = new JSONObject(interbankInquiryResult.getBody());
					 
					 String name_result = jsonResult.getJSONObject("result").getString("destinationAccountName");
						
						//check name fuzzy
				    	Integer fuzzyNameResult = FuzzySearch.tokenSetRatio(account_name, name_result);
				    	
				    	if(fuzzyNameResult > thresholdFuzzy) {
				    		result = true;
				    	} else {
				    		result = false;
				    	}
				 } else {
					 result = false;
				 }	
			 }  
		} else {
			result = false;
		}
		
		return result;
		
	}
	
	public static Boolean ClaimHistoryValid(String nama_tertanggung, Date dob_tertanggung) {
		
		Boolean result = null;
		
		if(nama_tertanggung != null && nama_tertanggung != "" && dob_tertanggung != null) {
			SpicaServices services = new SpicaServices();
			
			Integer claim_count = services.selectClaimHistory(nama_tertanggung, dob_tertanggung);
			
			if(claim_count < 1) {
				result = true;
			} else {
				result = false;
			}
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Boolean PolicyHistoryValid(String reg_spaj) {
		
		Boolean result = true;
		
		if(reg_spaj != null && reg_spaj != "") {
			SpicaServices services = new SpicaServices();
			Boolean holder_cover = false;
			
			ArrayList<MstProductInsured> allProductInsured = services.selectAllProductInsured(reg_spaj);
			
			if(!allProductInsured.isEmpty()) {
				for(MstProductInsured result_product : allProductInsured) {
					String lbs_sar_pp = services.selectLbsSarPP(result_product.getLku_id(), result_product.getLsbs_id());
					
					if(lbs_sar_pp != null) {
						holder_cover = true;
						break;
					}
				}
				
				if(holder_cover == true) {
					ArrayList<String> reg_spaj_holder = services.selectAllRegSpajByIdSimultanHolder(reg_spaj);
					
					if(!reg_spaj_holder.isEmpty()) {
						for(String resultSpajHolder : reg_spaj_holder) {
							String spaj_not_valid = services.selectRegSpajStatusAcceptHistory(resultSpajHolder);
							
							if(spaj_not_valid != null) {
								result = false;
								break;
							}
						}
					}
					
					if(result == true) {
						ArrayList<String> reg_spaj_insured = services.selectAllRegSpajByIdSimultanInsured(reg_spaj);
						
						if(!reg_spaj_insured.isEmpty()) {
							for(String resultSpajInsured : reg_spaj_insured) {
								String spaj_not_valid = services.selectRegSpajStatusAcceptHistory(resultSpajInsured);
								
								if(spaj_not_valid != null) {
									result = false;
									break;
								}
							}
						}
					}
				} else {
					ArrayList<String> reg_spaj_insured = services.selectAllRegSpajByIdSimultanInsured(reg_spaj);
					
					if(!reg_spaj_insured.isEmpty()) {
						for(String resultSpajInsured : reg_spaj_insured) {
							String spaj_not_valid = services.selectRegSpajStatusAcceptHistory(resultSpajInsured);
							
							if(spaj_not_valid != null) {
								result = false;
								break;
							}
						}
					}
				}
			} else {
				result = false;
			}
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Boolean CustomerDataValidation(String nama, String no_hp, String no_account, String email, Integer fuzzy_threshold) {
		
		Boolean result = true;
		
		if(nama != null && nama != "" && no_hp != null && no_hp != "" && no_account != null && no_account != "" && email != null && email != "" && fuzzy_threshold != null) {
			SpicaServices services = new SpicaServices();
			
			//SELECT APAKAH ADA DATA CUSTOMER LAIN BERDASARKAN NO HP, NO REK & EMAIL PP
			ArrayList<HashMap<String,String>> data_customer = services.selectDataCustomer(no_hp, no_account, email); 
			
			if(!data_customer.isEmpty()) {
				for(HashMap<String,String> result_data : data_customer) {
					Integer result_fuzzy = FuzzySearch.tokenSetRatio(nama, result_data.get("NM_PP").toString());
					
					//BANDINGKAN NAMA DENGAN FUZZY, JIKA BERBEDA MAKA TIDAK LOLOS VALIDASI
					if(result_fuzzy < fuzzy_threshold) {
						result = false;
						break;
					}
				}
			}
			
			//CHECK APAKAH DATA PP SAMA DENGAN LIST DATA AGENT
			if(result == true) {
				ArrayList<HashMap<String,String>> data_agent = services.selectDataAgent(no_hp, no_account, email);
				
				if(!data_agent.isEmpty()) {
					result = false;
				}
			}
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Boolean MedicalPolicyValid(String reg_spaj) {
		
		Boolean result = true;
		
		if(reg_spaj != null && reg_spaj != "") {
			SpicaServices services = new SpicaServices();
			Integer lsbs_id;
			Integer lsdbs_number;
			Integer umur_peserta;
			Integer mste_age;
			
			ArrayList<MstProductInsured> mstProductInsured = services.selectAllProductInsured(reg_spaj);
			mste_age = services.selectUmurTertanggungUtama(reg_spaj);
			
			if(!mstProductInsured.isEmpty()) {
				for(MstProductInsured product_result : mstProductInsured) {
					lsbs_id = product_result.getLsbs_id();
					lsdbs_number = product_result.getLsdbs_number();
					
					LstDetBisnis lst_det_bisnis = services.selectLstDetBisnis(lsbs_id, lsdbs_number);
					
					if(lsbs_id == 183 || lsbs_id == 189 || lsbs_id == 201 || lsbs_id == 823 || lsbs_id == 825 
							|| lsbs_id == 832 || lsbs_id == 833 || lsbs_id == 848) {
						if(lst_det_bisnis.getLsdbs_name().contains("PLAN A") || lst_det_bisnis.getLsdbs_name().contains("PLAN B") ||
								lst_det_bisnis.getLsdbs_name().contains("PLAN C") || lst_det_bisnis.getLsdbs_name().contains("PLAN D")) {
							//UNTUK PLAN A-D JIKA USIA LEBIH DARI 40 TAHUN MAKA NOT PASSED
							if(mste_age > 40) {
								result = false;
								break;
							}
						} else {
							//UNTUK PLAN DIATAS PLAN D JIKA USIA LEBIH DARI 50 TAHUN MAKA NOT PASSED
							if(mste_age > 50) {
								result = false;
								break;
							}
						}
					} else {
						//SELAIN SMILE MEDICAL MAKA CEK FLAG CLEAN --> UNTUK SMILE HOSPITAL
						if(lst_det_bisnis.getFlag_clean() != 1) {
							result = false;
							break;
						}
					}
				}
			}
			
			if(result == true) {
				ArrayList<MstPeserta> peserta = services.selectPeserta(reg_spaj);
				
				if(!peserta.isEmpty()) {
					for(MstPeserta peserta_result :peserta) {
						lsbs_id = peserta_result.getLsbs_id();
						lsdbs_number = peserta_result.getLsdbs_number();
						umur_peserta = peserta_result.getUmur();
						
						LstDetBisnis lst_det_bisnis = services.selectLstDetBisnis(lsbs_id, lsdbs_number);
						
						if(lsbs_id == 183 || lsbs_id == 189 || lsbs_id == 201 || lsbs_id == 823 || lsbs_id == 825 
								|| lsbs_id == 832 || lsbs_id == 833 || lsbs_id == 848) {
							if(lst_det_bisnis.getLsdbs_name().contains("PLAN A") || lst_det_bisnis.getLsdbs_name().contains("PLAN B") ||
									lst_det_bisnis.getLsdbs_name().contains("PLAN C") || lst_det_bisnis.getLsdbs_name().contains("PLAN D")) {
								//UNTUK PLAN A-D JIKA USIA LEBIH DARI 40 TAHUN MAKA NOT PASSED
								if(umur_peserta > 40) {
									result = false;
									break;
								}
							} else {
								//UNTUK PLAN DIATAS PLAN D JIKA USIA LEBIH DARI 50 TAHUN MAKA NOT PASSED
								if(umur_peserta > 50) {
									result = false;
									break;
								}
							}
						} else {
							//SELAIN SMILE MEDICAL MAKA CEK FLAG CLEAN --> UNTUK SMILE HOSPITAL
							if(lst_det_bisnis.getFlag_clean() != 1) {
								result = false;
								break;
							}
						}
					}
				}
			}
		} else {
			result = false;
		}
		
		return result;
	}
	
	//PRODUCT VALIDATION
	
	public static Boolean ProductAgeValidationHolder(String reg_spaj) {
		
		Boolean result = null;
		
		if(reg_spaj != null && reg_spaj != "") {
			SpicaServices services = new SpicaServices();
			
			//SELECT DATA PEMEGANG
			ProductAgeValidationProfile profilePP = services.selectProductAgeValidationPP(reg_spaj);
			
			if(profilePP != null) {
				Integer lsbs_id = profilePP.getLsbs_id();
				Integer lsdbs_number = profilePP.getLsdbs_number();
				
				LstProdset lstProdset = services.selectProductAgeValidation(lsbs_id, lsdbs_number);
				
				if(lstProdset != null) {
					Calendar calBegDate = Calendar.getInstance();
			        Calendar calDateBirth = Calendar.getInstance();
			        calBegDate.setTime(profilePP.getMspr_beg_date());
			        calDateBirth.setTime(profilePP.getMspe_date_birth());
			        Integer usia = calBegDate.get(Calendar.YEAR) - calDateBirth.get(Calendar.YEAR);
			        
			        //PEMBULATAN USIA
			        if (calBegDate.get(Calendar.MONTH) - calDateBirth.get(Calendar.MONTH) >= 6) {
			            usia++;
			        }
			        
			        Integer usiaPP = usia;
			        
			        if (usiaPP.compareTo(lstProdset.getHolder_age_from()) < 0) {
			        	result = false;
			        } else if (usiaPP.compareTo(lstProdset.getHolder_age_to()) > 0) {
			        	result = false;
			        } else {
			        	result = true;
			        }
			        
				} else {
					result = false;
				}
			} else {
				result = false;
			}
			
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Boolean ProductAgeValidationInsured(String reg_spaj) {
		
		Boolean result = null;
		
		if(reg_spaj != null && reg_spaj != "") {
			SpicaServices services = new SpicaServices();
			
			//SELECT DATA TERTANGGUNG UTAMA
			ProductAgeValidationProfile profileTTG = services.selectProductAgeValidationTTG(reg_spaj);
			
			if(profileTTG != null) {
				Integer lsbs_id = profileTTG.getLsbs_id();
				Integer lsdbs_number = profileTTG.getLsdbs_number();
				
				LstProdset lstProdset = services.selectProductAgeValidation(lsbs_id, lsdbs_number);
				
				if(lstProdset != null) {
					Integer validate;
					Calendar calBegDate = Calendar.getInstance();
			        Calendar calDateBirth = Calendar.getInstance();
			        calBegDate.setTime(profileTTG.getMspr_beg_date());
			        calDateBirth.setTime(profileTTG.getMspe_date_birth());
			        
			        Integer usiaTTG = calBegDate.get(Calendar.YEAR) - calDateBirth.get(Calendar.YEAR);
			        
			        if (lstProdset.getInsured_age_from_flag() == 0) {
			            validate = calBegDate.get(Calendar.YEAR) - calDateBirth.get(Calendar.YEAR);
			        } else if (lstProdset.getInsured_age_from_flag() == 1) {
			            validate = calBegDate.get(Calendar.MONTH) - calDateBirth.get(Calendar.MONTH);
			        } else {
			            validate = calBegDate.get(Calendar.DATE) - calDateBirth.get(Calendar.DATE);
			        }
			        
			        if (validate.compareTo(lstProdset.getInsured_age_from()) < 0 && usiaTTG == 0) {
			        	result = false;
			        } else if (usiaTTG.compareTo(lstProdset.getInsured_age_to()) > 0) {
			        	result = false;
			        } else {
			        	result = true;
			        }
			        
				} else {
					result = false;
				}
			} else {
				result = false;
			}
			
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Boolean RiderAgeValidationHolder(String reg_spaj) {
		
		Boolean result = true;
		
		if(reg_spaj != null && reg_spaj != "") {
			SpicaServices services = new SpicaServices();
			
			//SELECT DATA PEMEGANG
			ArrayList<ProductAgeValidationProfile> profilePP = services.selectRiderAgeValidationPP(reg_spaj);
			
			if(!profilePP.isEmpty()) {
				
				for(ProductAgeValidationProfile rider : profilePP) {
					Integer lsbs_id = rider.getLsbs_id();
					Integer lsdbs_number = rider.getLsdbs_number();
					
					LstProdset lstProdset = services.selectRiderAgeValidation(lsbs_id, lsdbs_number);
					
					if(lstProdset != null) {
						Calendar calBegDate = Calendar.getInstance();
				        Calendar calDateBirth = Calendar.getInstance();
				        calBegDate.setTime(rider.getMspr_beg_date());
				        calDateBirth.setTime(rider.getMspe_date_birth());
				        Integer usia = calBegDate.get(Calendar.YEAR) - calDateBirth.get(Calendar.YEAR);
				        
				        //PEMBULATAN USIA
				        if (calBegDate.get(Calendar.MONTH) - calDateBirth.get(Calendar.MONTH) >= 6) {
				            usia++;
				        }
				        
				        Integer usiaPP = usia;
				        
				        if (usiaPP.compareTo(lstProdset.getHolder_age_from()) < 0) {
				        	result = false;
				        } else if (usiaPP.compareTo(lstProdset.getHolder_age_to()) > 0) {
				        	result = false;
				        }      
					} else {
						result = false;
					}
				}
			}
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Boolean RiderAgeValidationInsured(String reg_spaj) {
		
		Boolean result = true;
		
		if(reg_spaj != null && reg_spaj != "") {
			SpicaServices services = new SpicaServices();
			
			//SELECT DATA TERTANGGUNG UTAMA
			ArrayList<ProductAgeValidationProfile> profileTTG = services.selectRiderAgeValidationTTG(reg_spaj);
			
			if(!profileTTG.isEmpty()) {
				
				for(ProductAgeValidationProfile rider : profileTTG) {
					Integer lsbs_id = rider.getLsbs_id();
					Integer lsdbs_number = rider.getLsdbs_number();
					
					LstProdset lstProdset = services.selectRiderAgeValidation(lsbs_id, lsdbs_number);
					
					if(lstProdset != null) {
						Integer validate;
						Calendar calBegDate = Calendar.getInstance();
				        Calendar calDateBirth = Calendar.getInstance();
				        calBegDate.setTime(rider.getMspr_beg_date());
				        calDateBirth.setTime(rider.getMspe_date_birth());
			        
				        Integer usiaTTG = calBegDate.get(Calendar.YEAR) - calDateBirth.get(Calendar.YEAR);
				        
				        if (lstProdset.getInsured_age_from_flag() == 0) {
				            validate = calBegDate.get(Calendar.YEAR) - calDateBirth.get(Calendar.YEAR);
				        } else if (lstProdset.getInsured_age_from_flag() == 1) {
				            validate = calBegDate.get(Calendar.MONTH) - calDateBirth.get(Calendar.MONTH);
				        } else {
				            validate = calBegDate.get(Calendar.DATE) - calDateBirth.get(Calendar.DATE);
				        }
				        
				        if (validate.compareTo(lstProdset.getInsured_age_from()) < 0 && usiaTTG == 0) {
				        	result = false;
				        } else if (usiaTTG.compareTo(lstProdset.getInsured_age_to()) > 0) {
				        	result = false;
				        }      
					} else {
						result = false;
					}
				}
			}
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Boolean ProductEndDateValidation(String reg_spaj) {
		
		Boolean result = null;
		
		if(reg_spaj != null && reg_spaj != "") {
			SpicaServices services = new SpicaServices();
			
			//SELECT INSURED PROFILE
			PeriodValidationProfile profile = services.selectProductEndDateValidation(reg_spaj);
			
			if(profile != null) {
				Integer lsbs_id = profile.getLsbs_id();
				Integer lsdbs_number = profile.getLsdbs_number();
				
				LstProdset lstProdset = services.selectInsuredPeriod(lsbs_id, lsdbs_number);
				
				if(lstProdset != null) {
					BigDecimal masaPertanggungan;
					
					if(lstProdset.getInsured_period_flag() == 1) {
						masaPertanggungan = lstProdset.getInsured_period();
					} else {
						Map param = new HashMap();
						param.put("insured_period", lstProdset.getInsured_period().toString());
						param.put("umur_tt", profile.getMste_age().toString());
			            
			            masaPertanggungan = hasilHitungQueryProdsetForm(param, "103");
					}
					
					Calendar calendarMsprEndDate = Calendar.getInstance();
					calendarMsprEndDate.setTime(profile.getMspr_end_date());
					
					Calendar calendarEndDate = Calendar.getInstance();
					calendarEndDate.setTime(profile.getMspr_beg_date());
					calendarEndDate.add(Calendar.YEAR, Integer.parseInt(masaPertanggungan.toString()));
			        calendarEndDate.add(Calendar.DATE, -1);
			        
			        if(calendarEndDate.compareTo(calendarMsprEndDate) == 0) {
			        	result = true;
			        } else {
			        	result = false;
			        }
				} else {
					result = false;
				}
			} else {
				result = false;
			}
		} else {
			result = false;
		}
		
		return result;
	}

	private static BigDecimal hasilHitungQueryProdsetForm(Map param, String value) {
		SpicaServices services = new SpicaServices();
		
		String query = services.selectQueryPerhitungan(value);
		query = query.replace("$P{", "#{").replace("}", ":VARCHAR}").replace("&&", "AND");
        param.put("query", query);
        return services.getPerhitungan(param);
	}
	
	public static Boolean ProductInsuredPeriodValidation(String reg_spaj) {
		
		Boolean result = null;
		
		if(reg_spaj != null && reg_spaj != "") {
			SpicaServices services = new SpicaServices();
			
			//SELECT INSURED PROFILE
			PeriodValidationProfile profile = services.selectProductInsuredPeriodValidation(reg_spaj);
			
			if(profile != null) {
				Integer lsbs_id = profile.getLsbs_id();
				Integer lsdbs_number = profile.getLsdbs_number();
				
				LstProdset lstProdset = services.selectInsuredPeriod(lsbs_id, lsdbs_number);
				
				if(lstProdset != null) {
					BigDecimal masaPertanggungan;
					
					
					if(lstProdset.getInsured_period_flag() == 1) {
						masaPertanggungan = lstProdset.getInsured_period();
					} else {
						Map param = new HashMap();
						param.put("insured_period", lstProdset.getInsured_period().toString());
						param.put("umur_tt", profile.getMste_age().toString());
			            
			            masaPertanggungan = hasilHitungQueryProdsetForm(param, "103");
					}
					
					if(profile.getMspr_ins_period() == Integer.parseInt(masaPertanggungan.toString())) {
						result = true;
					} else {
						result = false;
					}
				} else {
					result = false;
				}
			} else {
				result = false;
			}
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Boolean ProductPayPeriodValidation(String reg_spaj) {
		
		Boolean result = null;
		
		if(reg_spaj != null && reg_spaj != "") {
			SpicaServices services = new SpicaServices();
			
			//SELECT INSURED PROFILE
			PeriodValidationProfile profile = services.selectProductPayPeriodValidation(reg_spaj);
			
			if(profile != null) {
				Integer lsbs_id = profile.getLsbs_id();
				Integer lsdbs_number = profile.getLsdbs_number();
				
				LstProdset lstProdset = services.selectInsuredPeriod(lsbs_id, lsdbs_number);
				
				if(lstProdset != null) {
					BigDecimal masaPertanggungan;
					Integer masaBayar;
					
					if(lstProdset.getInsured_period_flag() == 1) {
						masaPertanggungan = lstProdset.getInsured_period();
					} else {
						Map param = new HashMap();
						param.put("insured_period", lstProdset.getInsured_period());
						param.put("umur_tt", profile.getMste_age());
			            
			            masaPertanggungan = hasilHitungQueryProdsetForm(param, "103");
					}
					
					if(lstProdset.getPay_period_flag() == 1) {
						masaBayar = lstProdset.getPay_period();
					} else {
						masaBayar = Integer.parseInt(masaPertanggungan.toString());
					}
					
					if(profile.getMspo_pay_period() == masaBayar) {
						result = true;
					} else {
						result = false;
					}
				} else {
					result = false;
				}
			} else {
				result = false;
			}
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Boolean ProductPremiumHolidayPeriodValidation(String reg_spaj) {
		
		Boolean result = null;
		
		if(reg_spaj != null && reg_spaj != "") {
			SpicaServices services = new SpicaServices();
			
			//SELECT INSURED PROFILE
			PeriodValidationProfile profile = services.selectProductPremiumHolidayPeriodValidation(reg_spaj);
			
			if(profile != null) {
				Integer lsbs_id = profile.getLsbs_id();
				Integer lsdbs_number = profile.getLsdbs_number();
				
				LstProdset lstProdset = services.selectInsuredPeriod(lsbs_id, lsdbs_number);
				
				if(lstProdset != null) {
					Integer masaCuti;
					
					//PREMIUM_HOLIDAY_FLAG == 1 diisi oleh user jadi tidak perlu validasi
					if (lstProdset.getPremium_holiday_flag() == 1) {
			            result = true;
			        } else {
			            masaCuti = lstProdset.getPremium_holiday();
			            if(profile.getMspo_installment() == masaCuti) {
							result = true;
						} else {
							result = false;
						}
			        }
				} else {
					result = false;
				}
			} else {
				result = false;
			}
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Boolean RiderEndDateValidation(String reg_spaj) {
		
		Boolean result = true;
		
		if(reg_spaj != null && reg_spaj != "") {
			SpicaServices services = new SpicaServices();
			
			//SELECT INSURED PROFILE
			ArrayList<PeriodValidationProfile> profile = services.selectRiderEndDateValidation(reg_spaj);
			
			if(!profile.isEmpty()) {
				
				for(PeriodValidationProfile rider : profile) {
					Integer lsbs_id = rider.getLsbs_id();
					Integer lsdbs_number = rider.getLsdbs_number();
					
					LstProdset lstProdset = services.selectInsuredPeriod(lsbs_id, lsdbs_number);
					
					if(lstProdset != null) {
						BigDecimal masaPertanggungan;
						
						if(lstProdset.getInsured_period_flag() == 1) {
							masaPertanggungan = lstProdset.getInsured_period();
						} else {
							Map param = new HashMap();
							param.put("insured_period", lstProdset.getInsured_period());
							param.put("umur_tt", rider.getMste_age());
				            
				            masaPertanggungan = hasilHitungQueryProdsetForm(param, "103");
						}
						
						Calendar calendarMsprEndDate = Calendar.getInstance();
						calendarMsprEndDate.setTime(rider.getMspr_end_date());
						
						Calendar calendarEndDate = Calendar.getInstance();
						calendarEndDate.setTime(rider.getMspr_beg_date());
						calendarEndDate.add(Calendar.YEAR, Integer.parseInt(masaPertanggungan.toString()));
				        calendarEndDate.add(Calendar.DATE, -1);
				        
				        if(calendarEndDate.compareTo(calendarMsprEndDate) != 0) {
				        	result = false;
				        }
					} else {
						result = false;
					}
				}
			}
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Boolean RiderInsuredPeriodValidation(String reg_spaj) {
		
		Boolean result = true;
		
		if(reg_spaj != null && reg_spaj != "") {
			SpicaServices services = new SpicaServices();
			
			//SELECT INSURED PROFILE
			ArrayList<PeriodValidationProfile> profile = services.selectRiderInsuredPeriodValidation(reg_spaj);
			
			if(!profile.isEmpty()) {
				
				for(PeriodValidationProfile rider : profile) {
					Integer lsbs_id = rider.getLsbs_id();
					Integer lsdbs_number = rider.getLsdbs_number();
					
					LstProdset lstProdset = services.selectInsuredPeriod(lsbs_id, lsdbs_number);
					
					if(lstProdset != null) {
						BigDecimal masaPertanggungan;
						
						if(lstProdset.getInsured_period_flag() == 1) {
							masaPertanggungan = lstProdset.getInsured_period();
						} else {
							Map param = new HashMap();
							param.put("insured_period", lstProdset.getInsured_period());
							param.put("umur_tt", rider.getMste_age());
				            
				            masaPertanggungan = hasilHitungQueryProdsetForm(param, "103");
						}
						
						if(rider.getMspr_ins_period() != Integer.parseInt(masaPertanggungan.toString())) {
							result = false;
						}
					} else {
						result = false;
					}
				}
			}
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Boolean ProductSumInsuredValidation(String reg_spaj) {
		
		Boolean result = null;
		
		if(reg_spaj != null && reg_spaj != "") {
			SpicaServices services = new SpicaServices();
			
			//SELECT INSURED PROFILE
			SumInsuredValidationProfile profile = services.selectProductSumInsuredValidation(reg_spaj);
			
			if(profile != null) {
				Integer lsbs_id = profile.getLsbs_id();
				Integer lsdbs_number = profile.getLsdbs_number();
				String lku_id = profile.getLku_id();
				
				LstProdsetCalc lstProdsetCalc = services.selectProdSetCalc(lsbs_id, lsdbs_number, lku_id);
				
				if(lstProdsetCalc != null) {
					BigDecimal maxUP = new BigDecimal (0);
					BigDecimal minUP = new BigDecimal (0);

					if (lstProdsetCalc.getFlag_maxup() == 1) {
			            maxUP = lstProdsetCalc.getMax_up().setScale(0, RoundingMode.HALF_UP);
			        } else {
			        	Map param = new HashMap();
						param.put("BASE_PREMIUM", profile.getMspr_premium().toString());
						param.put("LI_KALI", profile.getLscb_kali().toString());
						param.put("LSBS_ID", lsbs_id.toString());
						param.put("LSDBS_NUMBER", lsdbs_number.toString());
						param.put("LI_USIA_TT", profile.getMste_age().toString());
						
			            maxUP = hasilHitungQueryProdsetForm(param, lstProdsetCalc.getLpf_id_max_up().toString());
			        }
					
			        if (lstProdsetCalc.getFlag_minup() == 1) {
			            minUP = lstProdsetCalc.getMin_up().setScale(0, RoundingMode.HALF_UP);
			        } else {
			        	Map param = new HashMap();
						param.put("BASE_PREMIUM", profile.getMspr_premium().toString());
						param.put("LI_KALI", profile.getLscb_kali().toString());
						
			            minUP = hasilHitungQueryProdsetForm(param, lstProdsetCalc.getLpf_id_min_up().toString());
			        }
			        if (profile.getMspr_tsi().compareTo(minUP) < 0) {
			            result = false;
			        } else if (profile.getMspr_tsi().compareTo(maxUP) > 0) {
			            result = false;
			        } else {
			        	result = true;
			        } 
				} else {
					result = false;
				}
			} else {
				result = false;
			}
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Boolean ProductPremiumValidation(String reg_spaj) {
		
		Boolean result = true;
		
		if(reg_spaj != null && reg_spaj != "") {
			SpicaServices services = new SpicaServices();
			
			//SELECT INSURED PROFILE
			PremiumValidationProfile profile = services.selectProductPremiumValidation(reg_spaj);
			
			if(profile != null) {
				Integer lsbs_id = profile.getLsbs_id();
				Integer lsdbs_number = profile.getLsdbs_number();
				String lku_id = profile.getLku_id();
				
				LstProdsetCalc lstProdsetCalc = services.selectProdSetCalc(lsbs_id, lsdbs_number, lku_id);
				
				if(lstProdsetCalc != null) {
					if(lstProdsetCalc.getLpf_id_max_premi() == 0 && lstProdsetCalc.getLpf_Id_min_premi() == 0) {
						result = true;
					} else {
						BigDecimal minPremi;
						BigDecimal maxPremi;
						
						Map param = new HashMap();
						param.put("lsbs_id", lsbs_id.toString());
						param.put("lsdbs_number", lsdbs_number.toString());
						param.put("LI_KALI", profile.getLscb_kali() != null ? profile.getLscb_kali().toString() : null);
						param.put("MIN_PREMIUM", lstProdsetCalc.getMin_premium() != null ? lstProdsetCalc.getMin_premium().toString() : null);
						param.put("MAX_PREMIUM", lstProdsetCalc.getMax_premium() != null ? lstProdsetCalc.getMax_premium().toString() : null);
						param.put("LI_USIA_TT", profile.getMste_age() != null ? profile.getMste_age().toString() : null);
						param.put("LSCB_KALI_OLD", profile.getLscb_kali_old() != null ? profile.getLscb_kali_old().toString() : null);
						param.put("FLAG_FACTOR_PREMIUM", lstProdsetCalc.getFlag_factor_premium() != null ? lstProdsetCalc.getFlag_factor_premium().toString() : null);
						
						minPremi = hasilHitungQueryProdsetForm(param, lstProdsetCalc.getLpf_Id_min_premi().toString());
				        maxPremi = hasilHitungQueryProdsetForm(param, lstProdsetCalc.getLpf_id_max_premi().toString());
				        
				        if (profile.getMspr_premium().compareTo(minPremi) < 0) {
				            result = false;
				        } else if (profile.getMspr_premium().compareTo(maxPremi) > 1) {
				            result = false;
				        }
				        
				        if(lstProdsetCalc.getLpf_id_min_premipokok() != 0) {
				        	BigDecimal minPremiPokok;
				        	
				        	param.put("MIN_PREMIPOKOK_PERSEN", lstProdsetCalc.getMin_premipokok_persen() != null ? lstProdsetCalc.getMin_premipokok_persen().toString() : null);
				        	param.put("VALIDASI_MIN_PREMI", lstProdsetCalc.getMin_premium() != null ? lstProdsetCalc.getMin_premium().toString() : null);
				        	
				        	minPremiPokok = hasilHitungQueryProdsetForm(param, lstProdsetCalc.getLpf_id_min_premipokok().toString());
				        	
				        	if (profile.getMu_jlh_premi().compareTo(minPremiPokok) < 0) {
				                result = false;
				            }
				        }
					}
				} else {
					result = false;
				}
			} else {
				result = false;
			}
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Boolean ProductTopUpSingleValidation(String reg_spaj) {
		
		Boolean result = null;
		
		if(reg_spaj != null && reg_spaj != "") {
			SpicaServices services = new SpicaServices();
			
			//SELECT INSURED PROFILE
			TopUpSingleValidationProfile profile = services.selectProductTopUpSingleValidation(reg_spaj);
			
			//Jika ada top up single maka validasi dulu, else tidak perlu validasi & lolos
			//MU_JLH_PREMI ADALAH NILAI TOPUP SINGLE
			if(profile != null) {
				Integer lsbs_id = profile.getLsbs_id();
				Integer lsdbs_number = profile.getLsdbs_number();
				String lku_id = profile.getLku_id();
				
				LstProdsetCalc lstProdsetCalc = services.selectProdSetCalc(lsbs_id, lsdbs_number, lku_id);
				
				if(lstProdsetCalc != null) {
					BigDecimal minTopUp = lstProdsetCalc.getMin_topup();
					
					if (profile.getMu_jlh_premi().compareTo(minTopUp) < 0) {
			            result = false;
			        } else {
			            result = true;
			        } 
				} else {
					result = false;
				}
			} else {
				result = true;
			}
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Boolean RiderGenderValidation(String reg_spaj) {
		
		Boolean result = true;
		
		if(reg_spaj != null && reg_spaj != "") {
			SpicaServices services = new SpicaServices();
			
			//SELECT INSURED PROFILE
			//0 = Wanita, 1 = Pria
			ArrayList<GenderValidationProfile> profile = services.selectRiderGenderValidation(reg_spaj);
			
			if(!profile.isEmpty()) {
				
				for(GenderValidationProfile rider : profile) {
					Integer lsbs_id = rider.getLsbs_id();
					Integer lsdbs_number = rider.getLsdbs_number();
					
					//SELECT FLAG GENDER
					//0 = Hanya boleh perempuan, 1 = Hanya boleh pria, 2 = Boleh keduanya
					LstProdset lstProdset = services.selectFlagGender(lsbs_id, lsdbs_number);
					
					if(lstProdset != null) {
						//Validasi gender jika flag gender state hanya boleh salah satu gender
						if(lstProdset.getFlag_gender() != 2) {
							if(rider.getMspe_sex() != lstProdset.getFlag_gender()) {
								result = false;
							}
						}
					} else {
						result = false;
					}
				}
			}
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Boolean ParticipantAgeValidation(String reg_spaj) {
		
		Boolean result = true;
		
		if(reg_spaj != null && reg_spaj != "") {
			SpicaServices services = new SpicaServices();
			
			//SELECT PARTICIPANT PROFILE
			ArrayList<ParticipantValidationProfile> profile = services.selectParticipantAgeValidationProfile(reg_spaj);
			
			if(!profile.isEmpty()) {
				
				for(ParticipantValidationProfile rider : profile) {
					Integer lsbs_id = rider.getLsbs_id();
					Integer lsdbs_number = rider.getLsdbs_number();
					Integer lsre_id = rider.getLsre_id();				
					
					//SELECT RIDER VALIDATION
					LstValidationAgeParticipant lstValidationAgeParticipant = services.selectLstValidationAgeParticipant(lsbs_id, lsdbs_number, lsre_id);
					
					if(lstValidationAgeParticipant != null) {
						Integer validate;
						Calendar calBegDate = Calendar.getInstance();
				        Calendar calDateBirth = Calendar.getInstance();
				        calBegDate.setTime(rider.getMspo_beg_date());
				        calDateBirth.setTime(rider.getTgl_lahir());
				        Integer usia = calBegDate.get(Calendar.YEAR) - calDateBirth.get(Calendar.YEAR);
				        
				        //PEMBULATAN USIA
				        if (calBegDate.get(Calendar.MONTH) - calDateBirth.get(Calendar.MONTH) >= 6) {
				            usia++;
				        }
				        
				        Integer usiaParticipant = usia;
				        
				        if (usiaParticipant > 0) {
				            if (usiaParticipant.compareTo(lstValidationAgeParticipant.getInsured_age_from()) < 0) {
				                result = false;
				            }
				            else if (usiaParticipant.compareTo(lstValidationAgeParticipant.getInsured_age_to()) > 0) {
				            	result = false;
				            }
				        } else if (lstValidationAgeParticipant.getInsured_age_from_flag() == 1) {
				            validate = calBegDate.get(Calendar.MONTH) - calDateBirth.get(Calendar.MONTH);
				            if (validate.compareTo(lstValidationAgeParticipant.getInsured_age_from()) < 0) {
				                result = false;
				            }
				            else if (usiaParticipant.compareTo(lstValidationAgeParticipant.getInsured_age_to()) > 0) {
				                result = false;
				            }
				        } else {
				            validate = calBegDate.get(Calendar.DATE) - calDateBirth.get(Calendar.DATE);
				            if (validate.compareTo(lstValidationAgeParticipant.getInsured_age_from()) < 0) {
				                result = false;
				            }
				            else if (usiaParticipant.compareTo(lstValidationAgeParticipant.getInsured_age_to()) > 0) {
				                result = false;
				            }
				        }
					} else {
						result = false;
					}
				}
			}
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Boolean SimasPrimeLinkValid(String reg_spaj) {
		
		Boolean result = false;
		
		if(reg_spaj != null && reg_spaj != "") {
			SpicaServices services = new SpicaServices();
			//maks 1 polis being processed + 3 polis inforced
			Integer max_inforce_counter = 0;
			
			//SELECT LSBS_ID & LSDBS_NUMBER
			MstProductInsured mstProductInsured = services.selectLsbsIdAndLsdbsNumber(reg_spaj);
			
			//Jika selain produk SIMAS PRIME LINK maka tidak perlu di cek & return true
			if(mstProductInsured.getLsbs_id() == 134 && mstProductInsured.getLsdbs_number() == 5) {
				max_inforce_counter++;
				
				ArrayList<String> reg_spaj_inforce = services.selectAllRegSpajInforceByIdSimultanInsured(reg_spaj);
				
				//Jika jumlah inforce kurang dari 3 maka tidak perlu di cek & return true
				if(reg_spaj_inforce.size() < 3) {
					result = true;
				} else {
					for(String resultSpaj : reg_spaj_inforce) {
//						//SELECT LINEBUS, untuk check konven/syariah(3 adalah syariah, selain itu konven)
//						Integer lsbs_linebus = services.selectLsbsLinebus(resultSpaj);
						
						//SELECT LSBS_ID & LSDBS_NUMBER SPAJ INFORCE
						MstProductInsured mstProductInsuredInforce = services.selectLsbsIdAndLsdbsNumber(resultSpaj);
						
						//TAMBAH COUNTER JIKA HANYA PRODUK PRIME LINK BAIK KONVEN/SYARIAH
						if(mstProductInsuredInforce.getLsbs_id() == 134 && mstProductInsuredInforce.getLsdbs_number() == 5 || 
								mstProductInsuredInforce.getLsbs_id() == 215 && mstProductInsuredInforce.getLsdbs_number() == 1) {
							max_inforce_counter++;
						}
					}
					
					//MAKS HANYA BOLEH 4 POLIS YANG INFORCE DENGAN TERTANGGUNG YANG SAMA
					if(max_inforce_counter <= 4) {
						result = true;
					} else {
						result = false;
					}
				}
			} else if(mstProductInsured.getLsbs_id() == 215 && mstProductInsured.getLsdbs_number() == 1) {
				max_inforce_counter++;
				
				ArrayList<String> reg_spaj_inforce = services.selectAllRegSpajInforceByIdSimultanInsured(reg_spaj);
				
				//Jika jumlah inforce kurang dari 3 maka tidak perlu di cek & return true
				if(reg_spaj_inforce.size() < 3) {
					result = true;
				} else {
					for(String resultSpaj : reg_spaj_inforce) {
//						//SELECT LINEBUS, untuk check konven/syariah(3 adalah syariah, selain itu konven)
//						Integer lsbs_linebus = services.selectLsbsLinebus(resultSpaj);
						
						//SELECT LSBS_ID & LSDBS_NUMBER SPAJ INFORCE
						MstProductInsured mstProductInsuredInforce = services.selectLsbsIdAndLsdbsNumber(resultSpaj);
						
						//TAMBAH COUNTER JIKA HANYA PRODUK PRIME LINK BAIK KONVEN/SYARIAH
						if(mstProductInsuredInforce.getLsbs_id() == 134 && mstProductInsuredInforce.getLsdbs_number() == 5 || 
								mstProductInsuredInforce.getLsbs_id() == 215 && mstProductInsuredInforce.getLsdbs_number() == 1) {
							max_inforce_counter++;
						}
					}
					
					//MAKS HANYA BOLEH 4 POLIS YANG INFORCE DENGAN TERTANGGUNG YANG SAMA
					if(max_inforce_counter <= 4) {
						result = true;
					} else {
						result = false;
					}
				}
			} else {
				result = true;
			}
			
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Boolean ClosingAgentValid(String reg_spaj) {
		
		Boolean result = null;
		
		if(reg_spaj != null && reg_spaj != "") {
			SpicaServices services = new SpicaServices();
			
			AgentValidationProfile profile = services.selectClosingAgentValidProfile(reg_spaj);
			
			if(profile != null) {
				//VALIDASI KHUSUS UNTUK AGENT PENTUTUP BANCASS
				if(profile.getId_jal() == 2 || profile.getId_jal() == 11) {
					//SELECT KODE AGEN UNTUK BANCASS
					String msag_id_bancass = services.selectMsagIdBancass(profile.getLsbs_id(), profile.getLsdbs_number());
					
					if(profile.getMsag_id().equals(msag_id_bancass)) {
						result = true;
					} else {
						result = false;
					}
				} else {
					//KHUSUS SELAIN BANCASS, CHECK LISENSI, AKTIF & WAKTU BERLAKU AGENT
					String msag_id = services.selectMsagId(profile.getMsag_id());
					
					if(msag_id != null) {
						if(services.selectAgentBlacklist().contains(profile.getMsag_id())) {
							result = false;
						} else {
							result = true;
						}
					} else {
						result = false;
					}
				}
			} else {
				result = false;
			}
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Boolean BCAgentValid(String reg_spaj) {
		
		Boolean result = null;
		
		if(reg_spaj != null && reg_spaj != "") {
			SpicaServices services = new SpicaServices();
			
			AgentValidationProfile profile = services.selectBCAgentValidProfile(reg_spaj);
			
			if(profile != null) {
				//VALIDASI KHUSUS UNTUK JALUR BANCASS
				if(profile.getId_jal() == 2 || profile.getId_jal() == 11) {
					//CHECK IF AGENT BC AKTIF, BERLISENSI & TIDAK EXPIRED
					String lrb_id = services.selectLrbId(profile.getLrb_id());
					
					if (lrb_id != null) {
						if(services.selectAgentBlacklist().contains(profile.getLrb_id())) {
							result = false;
						} else {
							result = true;
						}
					} else {
						result = false;
					}
				} else {
					result = true;
				}
			} else {
				result = false;
			}
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Boolean ReferralAgentValid(String reg_spaj) {
		
		Boolean result = null;
		
		if(reg_spaj != null && reg_spaj != "") {
			SpicaServices services = new SpicaServices();
			
			AgentValidationProfile profile = services.selectReferralAgentValidProfile(reg_spaj);
			
			if(profile != null) {
				//VALIDASI KHUSUS UNTUK JALUR BANCASS
				if(profile.getId_jal() == 2 || profile.getId_jal() == 11) {
					//CHECK IF AGENT REFERRAL AKTIF, BERLISENSI & TIDAK EXPIRED
					String lrb_id = services.selectLrbId(profile.getReff_id());
					
					if (lrb_id != null) {
						result = true;
					} else {
						result = false;
					}
				} else {
					result = true;
				}
			} else {
				result = false;
			}
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Boolean AOAgentValid(String reg_spaj) {
		
		Boolean result = null;
		
		if(reg_spaj != null && reg_spaj != "") {
			SpicaServices services = new SpicaServices();
			
			AgentValidationProfile profile = services.selectAOAgentValidProfile(reg_spaj);
			
			if(profile != null) {
				//VALIDASI KHUSUS UNTUK JALUR BANCASS
				if(profile.getId_jal() == 2 || profile.getId_jal() == 11) {
					//VALIDASI HANYA UNTUK PRODUK SIMAS KID KONVEN/SYARIAH
					if(profile.getLsbs_id() == 208 || profile.getLsbs_id() == 219) {
						String msag_id = services.selectMsagIdAO(profile.getMspo_ao());
						
						if(msag_id != null) {
							result = true;
						} else {
							result = false;
						}
					} else {
						result = true;
					}
				} else {
					result = true;
				}
			} else {
				result = false;
			}
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static Boolean ProductGioValid(String reg_spaj) {
		
		Boolean result = null;
		
		if(reg_spaj != null && reg_spaj != "") {
			SpicaServices services = new SpicaServices();
			SpicaCons cons = new SpicaCons();
			
			//SELECT NILAI KONVERSI KE IDR UNTUK PREMI USD
			Long rate_idr = services.selectRateIdr(cons.RUPIAH_RATE);
			
			MstProductInsured spajProcessProduct = services.selectProductGioValid(reg_spaj);
			
			if(spajProcessProduct.getFlag_gio() == 1) {
				if(spajProcessProduct.getLsbs_id() == 212) {
					Long totalUP;
					
					if(spajProcessProduct.getLku_id().contentEquals("01")) {
						totalUP = spajProcessProduct.getMspr_tsi();
					} else {
						totalUP = spajProcessProduct.getMspr_tsi() * rate_idr;
					}
					
					ArrayList<String> reg_spaj_inforce = services.selectAllRegSpajInforceByIdSimultanInsured(reg_spaj);
					
					for(String resultSpaj : reg_spaj_inforce) {
						Long UP;
						//SELECT LKU_ID, MSPR_TSI, LSBS_ID, LSDBS_NUMBER SPAJ INFORCE
						MstProductInsured mstProductInsuredInforce = services.selectMstProductInsured(resultSpaj);
						
						//TAMBAH UP KE TOTAL UP HANYA JIKA LSBS ID SAMA
						if(mstProductInsuredInforce.getLsbs_id() == 212) {
							if(mstProductInsuredInforce.getLku_id().contentEquals("01")) {
								UP = mstProductInsuredInforce.getMspr_tsi();
							} else {
								UP = mstProductInsuredInforce.getMspr_tsi() * rate_idr;
							}
							
							totalUP += UP;
						}						
					}
					
					if(totalUP <= cons.MAX_UP_LSBS_ID_212) {
						result = true;
					} else {
						result = false;
					}
				} else if(spajProcessProduct.getLsbs_id() == 208) {
					Long totalUP;
					
					if(spajProcessProduct.getLku_id().contentEquals("01")) {
						totalUP = spajProcessProduct.getMspr_tsi();
					} else {
						totalUP = spajProcessProduct.getMspr_tsi() * rate_idr;
					}
					
					if(totalUP <= cons.MAX_UP_LSBS_ID_208) {
						result = true;
					} else {
						result = false;
					}
				} else if(spajProcessProduct.getLsbs_id() == 223) {
					Long totalUP;
					
					if(spajProcessProduct.getLku_id().contentEquals("01")) {
						totalUP = spajProcessProduct.getMspr_tsi();
					} else {
						totalUP = spajProcessProduct.getMspr_tsi() * rate_idr;
					}
					
					ArrayList<String> reg_spaj_inforce = services.selectAllRegSpajInforceByIdSimultanInsured(reg_spaj);
					
					for(String resultSpaj : reg_spaj_inforce) {
						Long UP;
						//SELECT LKU_ID, MSPR_TSI, LSBS_ID, LSDBS_NUMBER SPAJ INFORCE
						MstProductInsured mstProductInsuredInforce = services.selectMstProductInsured(resultSpaj);
						
						//TAMBAH UP KE TOTAL UP HANYA JIKA LSBS ID SAMA
						if(mstProductInsuredInforce.getLsbs_id() == 223) {
							if(mstProductInsuredInforce.getLku_id().contentEquals("01")) {
								UP = mstProductInsuredInforce.getMspr_tsi();
							} else {
								UP = mstProductInsuredInforce.getMspr_tsi() * rate_idr;
							}
							
							totalUP += UP;
						}						
					}
					
					if(totalUP <= cons.MAX_UP_LSBS_ID_223) {
						result = true;
					} else {
						result = false;
					}
				} else if(spajProcessProduct.getLsbs_id() == 219) {
					Long totalUP;
					
					if(spajProcessProduct.getLku_id().contentEquals("01")) {
						totalUP = spajProcessProduct.getMspr_tsi();
					} else {
						totalUP = spajProcessProduct.getMspr_tsi() * rate_idr;
					}
					
					if(totalUP <= cons.MAX_UP_LSBS_ID_208) {
						result = true;
					} else {
						result = false;
					}
				} else if(spajProcessProduct.getLsbs_id() == 227) {
					Long totalUP;
					
					if(spajProcessProduct.getLku_id().contentEquals("01")) {
						totalUP = spajProcessProduct.getMspr_tsi();
					} else {
						totalUP = spajProcessProduct.getMspr_tsi() * rate_idr;
					}
					
					ArrayList<String> reg_spaj_inforce = services.selectAllRegSpajInforceByIdSimultanInsured(reg_spaj);
					
					for(String resultSpaj : reg_spaj_inforce) {
						Long UP;
						//SELECT LKU_ID, MSPR_TSI, LSBS_ID, LSDBS_NUMBER SPAJ INFORCE
						MstProductInsured mstProductInsuredInforce = services.selectMstProductInsured(resultSpaj);
						
						//TAMBAH UP KE TOTAL UP HANYA JIKA LSBS ID SAMA
						if(mstProductInsuredInforce.getLsbs_id() == 227) {
							if(mstProductInsuredInforce.getLku_id().contentEquals("01")) {
								UP = mstProductInsuredInforce.getMspr_tsi();
							} else {
								UP = mstProductInsuredInforce.getMspr_tsi() * rate_idr;
							}
							
							totalUP += UP;
						}						
					}
					
					if(totalUP <= cons.MAX_UP_LSBS_ID_227) {
						result = true;
					} else {
						result = false;
					}
				} else if(spajProcessProduct.getLsbs_id() == 134) {
					//maks 1 polis being processed + 3 polis inforced
					Integer max_inforce_counter = 1;
					
					ArrayList<String> reg_spaj_inforce = services.selectAllRegSpajInforceByIdSimultanInsured(reg_spaj);
					
					//Jika jumlah inforce kurang dari 3 maka tidak perlu di cek & return true
					if(reg_spaj_inforce.size() < 3) {
						result = true;
					} else {
						for(String resultSpaj : reg_spaj_inforce) {
//							//SELECT LINEBUS, untuk check konven/syariah(3 adalah syariah, selain itu konven)
//							Integer lsbs_linebus = services.selectLsbsLinebus(resultSpaj);
							
							//SELECT LSBS_ID & LSDBS_NUMBER SPAJ INFORCE
							MstProductInsured mstProductInsuredInforce = services.selectLsbsIdAndLsdbsNumber(resultSpaj);
							
							//TAMBAH COUNTER JIKA HANYA PRODUK PRIME LINK BAIK KONVEN/SYARIAH
//							if(mstProductInsuredInforce.getLsbs_id() == 134 && mstProductInsuredInforce.getLsdbs_number() == 5 || 
//									mstProductInsuredInforce.getLsbs_id() == 215 && mstProductInsuredInforce.getLsdbs_number() == 1) {
//								max_inforce_counter++;
//							}
							
							//TAMBAH COUNTER JIKA LSBS ID SAMA
							if(mstProductInsuredInforce.getLsbs_id() == 134) {
								max_inforce_counter++;
							}
						}
						
						//MAKS HANYA BOLEH 4 POLIS YANG INFORCE DENGAN TERTANGGUNG YANG SAMA
						if(max_inforce_counter <= cons.MAX_POLIS_PRIME_LINK) {
							result = true;
						} else {
							result = false;
						}
					}
				} else if(spajProcessProduct.getLsbs_id() == 215) {
					//maks 1 polis being processed + 3 polis inforced
					Integer max_inforce_counter = 1;
					
					ArrayList<String> reg_spaj_inforce = services.selectAllRegSpajInforceByIdSimultanInsured(reg_spaj);
					
					//Jika jumlah inforce kurang dari 3 maka tidak perlu di cek & return true
					if(reg_spaj_inforce.size() < 3) {
						result = true;
					} else {
						for(String resultSpaj : reg_spaj_inforce) {
//							//SELECT LINEBUS, untuk check konven/syariah(3 adalah syariah, selain itu konven)
//							Integer lsbs_linebus = services.selectLsbsLinebus(resultSpaj);
							
							//SELECT LSBS_ID & LSDBS_NUMBER SPAJ INFORCE
							MstProductInsured mstProductInsuredInforce = services.selectLsbsIdAndLsdbsNumber(resultSpaj);
							
							//TAMBAH COUNTER JIKA HANYA PRODUK PRIME LINK BAIK KONVEN/SYARIAH
//							if(mstProductInsuredInforce.getLsbs_id() == 134 && mstProductInsuredInforce.getLsdbs_number() == 5 || 
//									mstProductInsuredInforce.getLsbs_id() == 215 && mstProductInsuredInforce.getLsdbs_number() == 1) {
//								max_inforce_counter++;
//							}
							
							//TAMBAH COUNTER JIKA LSBS ID SAMA
							if(mstProductInsuredInforce.getLsbs_id() == 215) {
								max_inforce_counter++;
							}
						}
						
						//MAKS HANYA BOLEH 4 POLIS YANG INFORCE DENGAN TERTANGGUNG YANG SAMA
						if(max_inforce_counter <= cons.MAX_POLIS_PRIME_LINK) {
							result = true;
						} else {
							result = false;
						}
					}
				} else {
					result = true;
				}
			} else {
				result = false;
			}	
		} else {
			result = false;
		}
		
		return result;
	}
	
	//BNI INTEGRATION
	
	public static ResponseEntity<Object> GetInhouseInquiry(String account_number) {
		
		ApiResult apiResult = new ApiResult();
		HttpStatus httpStatus = HttpStatus.OK;
		HouseInquiry objHouseInquiry = new HouseInquiry();
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String> result = new HashMap<String, String>();
		
		try {
			String token = null;
			HouseInquiryRequest houseInqReq = new HouseInquiryRequest();
			houseInqReq.setAccountNo(account_number);

			token = Util.getToken();
			
			map = BniPaymentService.getInHouseInquiryMap(token, houseInqReq);

			if (!map.get("httpStatus").equals(HttpStatus.OK)) {
				httpStatus = (HttpStatus) map.get("httpStatus");
			} else {
				objHouseInquiry = (HouseInquiry) map.get("inHouseInquiry");
				result.put("accountCurrency", objHouseInquiry.getAccountCurrency());
			result.put("accountNumber", objHouseInquiry.getAccountNumber());
				result.put("accountStatus", objHouseInquiry.getAccountStatus());
				result.put("accountType", objHouseInquiry.getAccountType());
				result.put("customerName", objHouseInquiry.getCustomerName());

				apiResult.setResponseTimestamp(objHouseInquiry.getResponseTimestamp());
				apiResult.setResponseMessage(objHouseInquiry.getResponseMessage());
				apiResult.setResponseCode(objHouseInquiry.getResponseCode());
				apiResult.setResult(result);
			}

		} catch (Exception e) {
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Object>(apiResult, httpStatus);
		
	}
	
	public static ResponseEntity<Object> GetInterBankInquiry(String account_number, String bank_code) {
		
		ApiResult apiResult = new ApiResult();
		BniPaymentService bniPaymentService = new BniPaymentService();
		HttpStatus httpStatus = HttpStatus.OK;
		InterBankInquiry objInterBankInquiry = new InterBankInquiry();
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String> result = new HashMap<String, String>();
		
		try {
			String token = null;
			InterBankInquiryRequest interBankReq = new InterBankInquiryRequest();
			interBankReq.setDestinationAccountNum(account_number);
			interBankReq.setDestinationBankCode(bank_code);

			token = Util.getToken();
			
			map = BniPaymentService.getInterBankInquiry(token, interBankReq);

			if (!map.get("httpStatus").equals(HttpStatus.OK)) {
				httpStatus = (HttpStatus) map.get("httpStatus");
			} else {
				objInterBankInquiry = (InterBankInquiry) map.get("interBankInquiry");
				result.put("destinationAccountNum", objInterBankInquiry.getDestinationAccountNum());
				result.put("destinationAccountName", objInterBankInquiry.getDestinationAccountName());
				result.put("destinationBankName", objInterBankInquiry.getDestinationBankName());
				result.put("retrievalReffNum", objInterBankInquiry.getRetrievalReffNum());

				apiResult.setResponseTimestamp(objInterBankInquiry.getResponseTimestamp());
				apiResult.setResponseMessage(objInterBankInquiry.getResponseMessage());
				apiResult.setResponseCode(objInterBankInquiry.getResponseCode());
				apiResult.setResult(result);
			}

		} catch (Exception e) {
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Object>(apiResult, httpStatus);
		
	}
	
}
