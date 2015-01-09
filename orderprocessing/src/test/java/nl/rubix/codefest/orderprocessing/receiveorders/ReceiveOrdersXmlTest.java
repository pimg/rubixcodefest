package nl.rubix.codefest.orderprocessing.receiveorders;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import nl.rubix.codefest.orderprocessing.message.CsvOrderModel;
import nl.rubix.orderprocessing.order._1.Order;

import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.apache.camel.util.CastUtils;
import org.junit.Test;

public class ReceiveOrdersXmlTest extends CamelBlueprintTestSupport {

	@Override
	public boolean isUseAdviceWith() {
		return true;
	}

	@Test
	public void testCsvUnmarshal() throws Exception {
		
		
		context.getRouteDefinition("receiveOrderCsv.transport").adviceWith(context, new AdviceWithRouteBuilder() {
			@Override		
			public void configure() throws Exception {
				replaceFromWith("direct:csv.unmarshal");
					}
				});
		context.start();

		// een dummy test request
		final String request = "1,10,100,nl\n" + "2,1,10,us\n"
				+ "3,100,1000,nl\n";

		// roep de camel route aan en bewaar de response in een variabele
		final List<Map<String, CsvOrderModel>> response = CastUtils
				.cast(template.requestBody("direct:csv.unmarshal", request,
						List.class));

		// we maken drie objecten aan van het CsvOrder model, deze gaan we
		// vergelijken met de objecten die de Camel route heeft gemaakt in de
		// unmarshal
		CsvOrderModel order1 = new CsvOrderModel();
		order1.setOrderId("1");
		order1.setAmount(10);
		order1.setPrice(100.0);
		order1.setCountry("nl");

		CsvOrderModel order2 = new CsvOrderModel();
		order2.setOrderId("2");
		order2.setAmount(1);
		order2.setPrice(10.0);
		order2.setCountry("us");

		CsvOrderModel order3 = new CsvOrderModel();
		order3.setOrderId("3");
		order3.setAmount(100);
		order3.setPrice(1000.0);
		order3.setCountry("nl");

		Map<String, CsvOrderModel> response1 = response.get(0);
		assertEquals(1, response1.size());
		Map.Entry<String, CsvOrderModel> entry1 = response1.entrySet()
				.iterator().next();
		assertEquals(CsvOrderModel.class.getName(), entry1.getKey());
		assertEquals("1", entry1.getValue().getOrderId());

		Map<String, CsvOrderModel> response2 = response.get(1);
		assertEquals(1, response2.size());
		Map.Entry<String, CsvOrderModel> entry2 = response2.entrySet()
				.iterator().next();
		assertEquals(CsvOrderModel.class.getName(), entry2.getKey());
		assertEquals("2", entry2.getValue().getOrderId());

		Map<String, CsvOrderModel> response3 = response.get(2);
		assertEquals(1, response3.size());
		Map.Entry<String, CsvOrderModel> entry3 = response3.entrySet()
				.iterator().next();
		assertEquals(CsvOrderModel.class.getName(), entry3.getKey());
		assertEquals("3", entry3.getValue().getOrderId());

	}
	
	@Test
	public void testReceiveOrderCsvTransportRoute() throws Exception{
		context.getRouteDefinition("receiveOrderCsv.transport").adviceWith(context, new AdviceWithRouteBuilder() {
			@Override		
			public void configure() throws Exception {
				replaceFromWith("direct:csv.unmarshal");
				interceptSendToEndpoint("direct:routeOrders").skipSendToOriginalEndpoint().to("mock:out");
					}
				});
		context.start();
		
		// een dummy test request
		final String request = "1,10,100,nl\n" + "2,1,10,us\n" + "3,100,1000,nl\n";
		
		MockEndpoint mockOut = getMockEndpoint("mock:out");
		
		//create an assertion that the mockOut endpoint receives 3 messages
		mockOut.expectedMessageCount(3);
		
		//send the test message to the route
		template.sendBody("direct:csv.unmarshal",request);
		
		//check one message for expected output
		Order order = mockOut.getExchanges().get(0).getIn().getBody(Order.class);
		assertEquals(order.getID(),"1");
		
		assertMockEndpointsSatisfied();
	}
	
	@Test
	public void testRouteOrders() throws Exception{
		// intercept the activemq endpoint so we can use it in the unittest
		context.getRouteDefinition("routeOrders").adviceWith(context, new AdviceWithRouteBuilder() {
			@Override		
			public void configure() throws Exception {
				interceptSendToEndpoint("activemq:queue:orders.nl").skipSendToOriginalEndpoint().to("mock:out");
					}
				});
		context.start();
		
		//create a test message
		Order testRequest = new Order();
		testRequest.setID("1");
		testRequest.setTotalAmount(new BigInteger("100"));
		testRequest.setTotalPrice(new BigDecimal(100.1));
		testRequest.setCountryCode("nl");
		
		MockEndpoint mockOut = getMockEndpoint("mock:out");
		
		mockOut.setExpectedMessageCount(1);
		
		template.sendBody("direct:routeOrders", testRequest);
		
		assertMockEndpointsSatisfied();
		
	}

	@Override
	protected String getBlueprintDescriptor() {
		return "/OSGI-INF/blueprint/ReceiveOrders.xml,/OSGI-INF/blueprint/shared-resources.xml";
	}

}
