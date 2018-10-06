package abapci.result;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import abapci.activation.Activation;
import abapci.domain.InvalidItem;

@RunWith(PowerMockRunner.class)
public class TestResultTest {

	TestResult testResult;

	Collection<Activation> currentActivations;
	Collection<Activation> newActivations;

	@Mock
	Activation activation1;
	@Mock
	Activation activation2;
	@Mock
	Activation activation3;

	@Before
	public void before() {
		currentActivations = new ArrayList<Activation>();
		currentActivations.add(activation1);
		testResult = new TestResult(true, 1, new ArrayList<InvalidItem>(), currentActivations);
		newActivations = new ArrayList<Activation>();
		newActivations.add(activation1);
		newActivations.add(activation2);

		PowerMockito.when(activation1.getObjectName()).thenReturn("Activation1");
		PowerMockito.when(activation2.getObjectName()).thenReturn("Activation2");
		PowerMockito.when(activation3.getObjectName()).thenReturn("Activation3");
	}

	@Test
	public void addMissingItemsCountTest() {

		Assert.assertEquals(1, testResult.getNumItems());
		testResult.addMissingItemsCount(newActivations);
		Assert.assertEquals(2, testResult.getNumItems());
	}

}
