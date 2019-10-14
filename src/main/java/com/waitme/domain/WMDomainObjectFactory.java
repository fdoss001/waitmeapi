package com.waitme.domain;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This object factory is used to generate WMDomainObjects.
 * @author Fernando Dos Santos
 * @version 1.0 2019-08-16
 * @since 1.0 2019-08-16
 */
public class WMDomainObjectFactory {
	
	/**
	 * This method will generate a blank WMDomainObject object down to the primitive level.
	 * It will take an object instance of WMDomainObject and initialize it.
	 * All primitives, wrappers, and non WMDomainObjects will be initialized as null.
	 * All lists will be created as empty lists.
	 * All maps will be created as empty maps.
	 * All inner WMDomainObjects will be recursively empty initialized the same way
	 * @param obj the object to initialize
	 * @return the empty initialized object
	 */
	public static WMDomainObject getEmptyObject(WMDomainObject obj) {
		try {
			Class<?> clazz = obj.getClass();
			for (Field f : clazz.getDeclaredFields()) {
				String className = f.getType().getSimpleName();	
				//this checks that the field is 1. A class, 2. a wm domain object, 3. Not annotated with json ignore
				if (!isPrimitiveOrWrapper(className) && f.getAnnotation(JsonIgnore.class) == null) {
					String setter = "set" + f.getName().substring(0,1).toUpperCase() + f.getName().substring(1);
					Class<?> theClass = Class.forName(f.getType().getName());
					Method method = clazz.getMethod(setter, theClass);
					if (isList(className)) {
						Class<?> concreteType = Class.forName("java.util.ArrayList");
						method.invoke(obj, concreteType.getConstructor().newInstance());
					} else if (isMap(className)) {
						Class<?> concreteType = Class.forName("java.util.HashMap");
						method.invoke(obj, concreteType.getConstructor().newInstance());
					} else if (WMDomainObject.class.isAssignableFrom(f.getType())) {
						method.invoke(obj, getEmptyObject((WMDomainObject) theClass.getConstructor().newInstance()));
					}
				}
			}
		} catch(Exception e) {
			System.out.println("Error while initializing empty WMDomainObject");
			e.printStackTrace();
		}
		return obj;
	}
	
	private static boolean isPrimitiveOrWrapper(String type) {
		if (type.equalsIgnoreCase("double") || 
				type.equalsIgnoreCase("float") ||
				type.equalsIgnoreCase("long") ||
				type.equalsIgnoreCase("int") || type.equalsIgnoreCase("integer") ||
				type.equalsIgnoreCase("short") ||
				type.equalsIgnoreCase("char") || type.equalsIgnoreCase("character") ||
				type.equalsIgnoreCase("byte") ||
				type.equalsIgnoreCase("boolean") ||
				type.equalsIgnoreCase("string")) {
			return true;
		}
		return false;
	}
	
	private static boolean isList(String type) {
		if (type.equalsIgnoreCase("list")) {return true;}
		return false;
	}
	
	private static boolean isMap(String type) {
		if (type.equalsIgnoreCase("map")) {return true;}
		return false;
	}
}
