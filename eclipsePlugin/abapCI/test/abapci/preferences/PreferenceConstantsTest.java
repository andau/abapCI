package abapci.preferences;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Ignore;
import org.junit.Test;

public class PreferenceConstantsTest {

	@Test
	@Ignore
	public void testPreferenceConstantsUnique() throws IllegalArgumentException, IllegalAccessException {
		final Field[] declaredFields = PreferenceConstants.class.getDeclaredFields();

		for (final Field declaredField : declaredFields) {
			assertTrue(java.lang.reflect.Modifier.isStatic(declaredField.getModifiers()));
		}

		final List<Field> fields = new ArrayList<>(Arrays.asList(declaredFields));
		final List<String> fieldValues = fields.stream().map(item -> {
			try {
				return (String) item.get(null);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				fail();
			}
			return null;
		}).collect(Collectors.toList());

		final Set<String> fieldSet = new HashSet<>();
		for (final String fieldValue : fieldValues) {
			assertFalse(fieldSet.contains(fieldValue));
			fieldSet.add(fieldValue);
		}

	}
}
