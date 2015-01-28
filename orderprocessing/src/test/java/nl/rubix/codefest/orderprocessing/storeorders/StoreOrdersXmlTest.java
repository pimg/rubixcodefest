package nl.rubix.codefest.orderprocessing.storeorders;

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
				replaceFromWith("direct:storeOrderNl");
					}
				});
		context.start();
		
		// For now, let's just wait for some messages// TODO Add some expectations here
		
		
		String test = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><order xmlns=\"http://www.rubix.nl/orderprocessing/order/1\"><orderId>1</orderId><totalAmount>100</totalAmount><totalPrice>100.09</totalPrice><countryCode>nl</countryCode></order>";
		
		template.sendBody("direct:storeOrderNl", test);
		// Validate our expectations
		assertMockEndpointsSatisfied();
	}
	
	@Test
	public void testReceiveIntOrdersRoute() throws Exception {
		context.getRouteDefinition("receiveIntOrders").adviceWith(context, new AdviceWithRouteBuilder() {
			@Override		
			public void configure() throws Exception {
				replaceFromWith("direct:storeOrderInt");
				}
			});
		context.start();
		// For now, let's just wait for some messages// TODO Add some expectations here
		
		String test = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><order xmlns=\"http://www.rubix.nl/orderprocessing/order/1\"><orderId>1</orderId><totalAmount>100</totalAmount><totalPrice>100.09</totalPrice><countryCode>us</countryCode></order>";
		
		template.sendBody("direct:storeOrderInt", test);
		// Validate our expectations
		assertMockEndpointsSatisfied();
	}

	@Override
	protected String getBlueprintDescriptor() {
		return "/OSGI-INF/blueprint/storeOrders.xml,/OSGI-INF/blueprint/shared-resources.xml";
	}

}
