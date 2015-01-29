package nl.rubix.codefest.orderprocessing.mappings;

import java.util.List;
import java.util.Map;

import nl.rubix.orderprocessing.orderinfoservice.GetOrderIdsByDistrictResponse;
import nl.rubix.orderprocessing.orderinfoservice.GetOrderIdsByDistrictResponse.Orders;
import nl.rubix.orderprocessing.orderinfoservice.GetOrderIdsByDistrictResponse.Orders.Order;
import nl.rubix.orderprocessing.orderinfoservice.ObjectFactory;

public class SqlToSelectOrderIdsResponse {

	public GetOrderIdsByDistrictResponse SqlToGetOrderByDistrictMapping(List<Map<String, Object>> request){
		//initialize the response object
		ObjectFactory objFac = new ObjectFactory();
		GetOrderIdsByDistrictResponse response = objFac.createGetOrderIdsByDistrictResponse();
		Orders orders = objFac.createGetOrderIdsByDistrictResponseOrders();
		//the List object of the request contains an entry for each row returned by the SQL statement. We need to loop through every row and create an order object
		for (Map<String, Object> col : request) {
			String id = (String) col.get("id");
			Order order = objFac.createGetOrderIdsByDistrictResponseOrdersOrder();
			order.setID(id);
			orders.getOrder().add(order);
		}
		response.setOrders(orders);
		
		return response;
	}

}
