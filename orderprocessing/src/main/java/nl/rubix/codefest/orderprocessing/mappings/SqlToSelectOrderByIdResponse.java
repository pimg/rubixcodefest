package nl.rubix.codefest.orderprocessing.mappings;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import nl.rubix.orderprocessing.orderinfoservice.GetOrderByIDResponse;
import nl.rubix.orderprocessing.orderinfoservice.GetOrderByIDResponse.Order;
import nl.rubix.orderprocessing.orderinfoservice.ObjectFactory;


public class SqlToSelectOrderByIdResponse{
	
	public GetOrderByIDResponse SqlToSelectOrderByIdMapping(List<Map<String, Object>> request){
		//CXF and JAXB use object factories to instantiate objects, but to use the object factory we first have to declare it
		ObjectFactory objFac = new ObjectFactory();
		
		GetOrderByIDResponse response = objFac.createGetOrderByIDResponse();
		Order order = objFac.createGetOrderByIDResponseOrder();
		
		//since the ID is unique (primary key constraint in the database) at most one record can be returned. So, no need for looping through the list
		//however we must check if the query returned a valid result
		if (request.size() > 0){
			order.setOrderId((String) request.get(0).get("id"));
			
			//since casting an Integer to BigInteger does not work, we have to explicitly type convert
			BigInteger biAmount = BigInteger.valueOf(new Integer((Integer) request.get(0).get("totalAmount")).intValue());
			order.setTotalAmount(biAmount);
			
			//since casting an String to BigDecimal does not work we have to explicitly type convert
			DecimalFormatSymbols symbols = new DecimalFormatSymbols();
			symbols.setGroupingSeparator(',');
			symbols.setDecimalSeparator('.');
			String pattern = "#,##0.0#";
			DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
			decimalFormat.setParseBigDecimal(true);
			
			// parse the string
			BigDecimal bdPrice = null;
			try {
				bdPrice = (BigDecimal) decimalFormat.parse((String) request.get(0).get("totalPrice"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			order.setTotalPrice(bdPrice);
			order.setCountryCode((String) request.get(0).get("countryCode"));
		}
		 
		//set the updated order object to the response
		response.setOrder(order);
		
		return response;
		
	}

	

}
