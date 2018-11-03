package abapci.preferences;

import static org.junit.Assert.*;

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
		Field[] declaredFields = PreferenceConstants.class.getDeclaredFields();

		for (Field declaredField : declaredFields) {
			assertTrue(java.lang.reflect.Modifier.isStatic(declaredField.getModifiers()));
		}

		List<Field> fields = new ArrayList<>(Arrays.asList(declaredFields));
		List<String> fieldValues = fields.stream().map(item -> {
			try {
				return (String) item.get(null);
			} catch (IllegalArgumentException | IllegalAccessException e) {
                 fail(); 
			}
			return null;
		}).collect(Collectors.toList());

		Set<String> fieldSet = new HashSet<>();
		for (String fieldValue : fieldValues) {
			assertFalse(fieldSet.contains(fieldValue));
			fieldSet.add(fieldValue);
		}

	}
}
