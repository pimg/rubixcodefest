package nl.rubix.codefest.orderprocessing.mappings;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nl.rubix.orderprocessing.orderinfoservice.GetOrderByIDResponse;
import nl.rubix.orderprocessing.orderinfoservice.GetOrderByIDResponse.Order;
import nl.rubix.orderprocessing.orderinfoservice.ObjectFactory;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

public class SqlToSelectOrderByIdResponse implements Processor{

	@Override
	public void process(Exchange exchange) throws Exception {
		// retrieve the in message from the Camel exchange
				Message in = exchange.getIn();
				
				//retrieve the object from the message, this contains the response of the query
				//The results will be mapped to a List (one entry for each row) of Maps (one entry for each column using the column name as the key). 
				//Each element in the list will be of the form returned by this interface's queryForMap() methods.
				List<Map<String, Object>> request = in.getBody(ArrayList.class);
				//initialize the response object
				ObjectFactory objFac = new ObjectFactory();
				
				GetOrderByIDResponse response = objFac.createGetOrderByIDResponse();
				Order order = objFac.createGetOrderByIDResponseOrder();
				
				//since the ID is unique (primary key constraint in the database at most one record can be returned. So, no need for looping through the list
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
					BigDecimal bdPrice = (BigDecimal) decimalFormat.parse((String) request.get(0).get("totalPrice"));
					
					order.setTotalPrice(bdPrice);
					order.setCountryCode((String) request.get(0).get("countryCode"));
				}
				 
				//set the updated order object to the response
				response.setOrder(order);
				
				//set the response object as the body of the exchange
				in.setBody(response);
	}

}
