package com.dws.challenge.common.predicates;

import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;


public class GenericPredicates {

	private GenericPredicates() {
		throw new IllegalStateException("Utility class");
	} //SonarLint Alert

	/**
	 * Validates if an object is Null or Empty
	 */
	public static Predicate<Object> checkIfNullOrEmpty = obj -> {

		boolean conditionA;
		// Check Null Objects
		conditionA = (obj == null);
		if (conditionA) return true;
		// Check Arrays
		conditionA = (obj.getClass().isArray());
		boolean conditionB;
		conditionB = (conditionA && ((Object[]) obj).length == 0);
		if (conditionB) return true;
		// Check Strings
		conditionA = (obj instanceof String);
		conditionB = (conditionA && "".equalsIgnoreCase((String) obj));
		if (conditionB) return true;
		// Check List , Vectors, etc.
		conditionA = (obj instanceof Collection);
		conditionB = (conditionA && ((Collection<?>) obj).isEmpty());
		if (conditionB) return true;
		// 05. Check Maps
		conditionA = (obj instanceof Map);
		conditionB = (conditionA && ((Map<?, ?>) obj).size() == 0);

		return conditionB;

	};


}
