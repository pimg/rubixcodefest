package nl.rubix.codefest.orderprocessing.mappings;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Map;

import nl.rubix.codefest.orderprocessing.message.CsvOrderModel;
import nl.rubix.orderprocessing.order._1.ObjectFactory;
import nl.rubix.orderprocessing.order._1.Order;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

public class CsvOrderModelToCDM implements Processor{

	@Override
	public void process(Exchange exchange) throws Exception {
		// retrieve the in message from the Camel exchange
		Message in = exchange.getIn();
		// retrieve the body of the in message, this body contains the split output from the bindy unmarshal, which is a Map<String, CsvOrderModel>
		Map<String, CsvOrderModel> body = (Map<String, CsvOrderModel>) in.getBody();
		// retrieve the CsvOrderModel object from the map
		CsvOrderModel csvOrder = body.get(CsvOrderModel.class.getName());
		
		//create the data transformation of from the CsvOrderModel to the generated Order class
		
		// use the generated object factory to create a new order object
		ObjectFactory objFac = new ObjectFactory();
		Order order = objFac.createOrder();
		
		// create the data mepping by assinging the setters of the order object to the getters of the csvOrder object
		order.setID(csvOrder.getOrderId());
		// since totalPrice is a BigDecimal and price is a Double we need to convert the type
		BigDecimal bdPrice = new BigDecimal(csvOrder.getPrice(), MathContext.DECIMAL64);
		order.setTotalPrice(bdPrice);
		
		// since totalAmount is a BigInteger and amount is an int we need to convert the type
		BigInteger biAmount = BigInteger.valueOf(csvOrder.getAmount());
		order.setTotalAmount(biAmount);
		order.setCountryCode(csvOrder.getCountry());
		
		// it is a best practice to use the in message in the exchange, when instictively you want to use the out message. THe reason for using the in message
		// is because all headers and properties of the message are deleted if you use the out message. Using the in message preserves all the properties and headers
		in.setBody(order);
		
	}

}
