package nl.rubix.codefest.orderprocessing.orderinfoservice;

import nl.rubix.orderprocessing.orderinfoservice.GetOrderByIDRequest;
import nl.rubix.orderprocessing.orderinfoservice.GetOrderByIDResponse;
import nl.rubix.orderprocessing.orderinfoservice.GetOrderIdsByDistrictRequest;
import nl.rubix.orderprocessing.orderinfoservice.GetOrderIdsByDistrictResponse;
import nl.rubix.orderprocessing.orderinfoservice.ObjectFactory;

import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.junit.Test;

public class OrderInfoServiceXmlTest extends CamelBlueprintTestSupport {

	@Test
	public void testGetOrderIdsByDistrict() throws Exception {
		//initialize the response object
		ObjectFactory objFac = new ObjectFactory();
		GetOrderIdsByDistrictRequest testRequest = objFac.createGetOrderIdsByDistrictRequest();
		testRequest.setCountryCode("NL");
		
		GetOrderIdsByDistrictResponse response = template.requestBodyAndHeader("cxf:http://localhost:1234/orderprocessing/orderinfoservice/?serviceClass=nl.rubix.orderprocessing.orderinfoservice.OrderInfoService", testRequest,"operationName","getOrderIdsByDistrict", GetOrderIdsByDistrictResponse.class);
		// Validate our expectations, since the database is empty we expect an empty soap response
		assertNotNull(response);
	}
	
	@Test
	public void testGetOrderById() throws Exception {
		//initialize the response object
		ObjectFactory objFac = new ObjectFactory();
		GetOrderByIDRequest testRequest = objFac.createGetOrderByIDRequest();
		testRequest.setID("1");;
		
		GetOrderByIDResponse response = template.requestBodyAndHeader("cxf:http://localhost:1234/orderprocessing/orderinfoservice/?serviceClass=nl.rubix.orderprocessing.orderinfoservice.OrderInfoService", testRequest,"operationName","getOrderByID", GetOrderByIDResponse.class);
		// Validate our expectations, since the database is empty we expect an empty soap response
		assertNotNull(response);
	}

	
	@Override
	protected String getBlueprintDescriptor() {
		return "/OSGI-INF/blueprint/OrderInfoService.xml,/OSGI-INF/blueprint/shared-resources.xml";
	}

}
