package nl.rubix.codefest.orderprocessing.mappings;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nl.rubix.orderprocessing.orderinfoservice.GetOrderIdsByDistrictResponse;
import nl.rubix.orderprocessing.orderinfoservice.GetOrderIdsByDistrictResponse.Orders;
import nl.rubix.orderprocessing.orderinfoservice.GetOrderIdsByDistrictResponse.Orders.Order;
import nl.rubix.orderprocessing.orderinfoservice.ObjectFactory;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

public class SqlToSelectOrderIdsResponse implements Processor{

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
		GetOrderIdsByDistrictResponse response = objFac.createGetOrderIdsByDistrictResponse();
		Orders orders = objFac.createGetOrderIdsByDistrictResponseOrders();
		for (Map<String, Object> col : request) {
			String id = (String) col.get("id");
			Order order = objFac.createGetOrderIdsByDistrictResponseOrdersOrder();
			order.setID(id);
			orders.getOrder().add(order);
		}
		response.setOrders(orders);
		in.setBody(response);
	
	}

}
