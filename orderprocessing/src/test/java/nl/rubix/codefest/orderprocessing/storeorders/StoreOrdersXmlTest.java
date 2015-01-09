package nl.rubix.codefest.orderprocessing.storeorders;

import java.math.BigDecimal;
import java.math.BigInteger;

import nl.rubix.orderprocessing.order._1.Order;

import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.junit.Test;

public class StoreOrdersXmlTest extends CamelBlueprintTestSupport {
	
	@Override
	public boolean isUseAdviceWith() {
		return true;
	}

	@Test
	public void testReceiveNlOrdersRoute() throws Exception {
		context.getRouteDefinition("receiveNlOrders").adviceWith(context, new AdviceWithRouteBuilder() {
			@Override		
			public void configure() throws Exception {
				replaceFromWith("direct:storeOrder");
					}
				});
		context.start();
		// Define some expectations
		
		// For now, let's just wait for some messages// TODO Add some expectations here
		Order testRequest = new Order();
		testRequest.setOrderId("1");
		testRequest.setTotalAmount(new BigInteger("100"));
		testRequest.setTotalPrice(new BigDecimal(100.1));
		testRequest.setCountryCode("nl");
		
		
		String test = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><order xmlns=\"http://www.rubix.nl/orderprocessing/order/1\"><orderId>1</orderId><totalAmount>100</totalAmount><totalPrice>100.09</totalPrice><countryCode>nl</countryCode></order>";
		
		template.sendBody("direct:storeOrder", test);
		// Validate our expectations
		assertMockEndpointsSatisfied();
	}

	@Override
	protected String getBlueprintDescriptor() {
		return "/OSGI-INF/blueprint/storeOrders.xml,/OSGI-INF/blueprint/shared-resources.xml";
	}

}
