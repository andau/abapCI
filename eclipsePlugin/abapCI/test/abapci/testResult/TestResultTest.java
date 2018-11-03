package abapci.testResult;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import abapci.activation.Activation;
import abapci.domain.InvalidItem;
import abapci.testResult.TestResult;

public class TestResultTest {

	TestResult testResult;

	Collection<Activation> currentActivations;
	Collection<Activation> newActivations;

	Activation activation1;
	Activation activation2;
	Activation activation3;

	@Before
	public void before() {
		activation1 = PowerMockito.mock(Activation.class); 
		activation2 = PowerMockito.mock(Activation.class); 
		activation3 = PowerMockito.mock(Activation.class); 
		PowerMockito.when(activation1.getObjectName()).thenReturn("Activation1");
		PowerMockito.when(activation2.getObjectName()).thenReturn("Activation2");
		PowerMockito.when(activation3.getObjectName()).thenReturn("Activation3");

		currentActivations = new ArrayList<Activation>();
		currentActivations.add(activation1);
		testResult = new TestResult(true, 1, new ArrayList<InvalidItem>(), currentActivations);
		newActivations = new ArrayList<Activation>();
		newActivations.add(activation1);
		newActivations.add(activation2);

	}

	@Test
	public void addMissingItemsCountTest() {

		Assert.assertEquals(1, testResult.getNumItems());
		testResult.addMissingItemsCount(newActivations);
		Assert.assertEquals(2, testResult.getNumItems());
	}

}
