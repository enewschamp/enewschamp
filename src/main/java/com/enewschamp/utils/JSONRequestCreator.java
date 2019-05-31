package com.enewschamp.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.enewschamp.article.domain.entity.NewsArticle;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JSONRequestCreator {

	public static void main(String args[]) {
		new JSONRequestCreator().getSampleJSONRequest(new NewsArticle());
	}

	public String getSampleJSONRequest(Object model) {
		String response = "Done";
		ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.ALWAYS);
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

		
		try {
			//instantiateFields(model);
			
			listCollectionProperties(model);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		try {
//			response = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(model);
//		} catch (JsonProcessingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		System.out.println(response);
		return response;
	}
	
	private void listCollectionProperties(Object o) throws IllegalAccessException {
		Field[] fields = o.getClass().getDeclaredFields();

		for (Field field : fields) {
			//System.out.println("Processing field: " + field.getName() + " -- Type: " + field.getType());
			field.setAccessible(true);

			if (field.get(o) == null) {
				Type type = field.getType();

//				try {
					Class<?> clazz = (Class<?>) type;
					if (clazz == List.class) {
						
						ParameterizedType listType = (ParameterizedType) field.getGenericType();
						Class<?> listClass = (Class<?>) listType.getActualTypeArguments()[0];
						
						System.out.println(field.getName() + " : " + List.class.getName() + " : + " + listClass.getName());
					}
//				} catch (ClassCastException ) {
//					// Handle this or leave field null
//				}
			}
		}
	}

	private void instantiateFields(Object o) throws IllegalAccessException {
		Field[] fields = o.getClass().getDeclaredFields();

		for (Field field : fields) {
			//System.out.println("Processing field: " + field.getName() + " -- Type: " + field.getType());
			field.setAccessible(true);

			if (field.get(o) == null) {
				Type type = field.getType();

				try {
					Class<?> clazz = (Class<?>) type;
					if (clazz == List.class) {
						clazz = ArrayList.class;
					}
					if (clazz == Map.class) {
						clazz = HashMap.class;
					}
					Object instance = null;
					
					if(type.getTypeName().equals("java.lang.String")) {
						instance = "{{" + field.getName() + "}}";
					} else if(type.getTypeName().equals("java.net.URI")) {
						try {
							instance = new URI("http://article-url");
						} catch (URISyntaxException e) {
							e.printStackTrace();
						}
					} else if(type.getTypeName().equals("java.lang.Long")) {
						instance = new Long("1234");
					} else if(type.getTypeName().equals("java.time.LocalDate")) {
						CharSequence sequence = new String("2019-12-12");
						instance = java.time.LocalDate.parse(sequence);
					} else if(field.getType().isEnum()) {
						instance = field.getType().getEnumConstants()[0];
					} else {
						instance = clazz.newInstance();
					}
					
					if (List.class.isAssignableFrom(clazz)) {
						instantiateList(clazz, field, instance);
					}

					if (Map.class.isAssignableFrom(clazz)) {
						instantiateMap(clazz, field, instance);
					}
					
					if (Set.class.isAssignableFrom(clazz)) {
						instantiateSet(clazz, field, instance);
					}

					field.set(o, instance);
					instantiateFields(instance);

				} catch (ClassCastException | InstantiationException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void instantiateList(Class<?> clazz, Field field, Object instance)
			throws IllegalAccessException, InstantiationException {

		ParameterizedType listType = (ParameterizedType) field.getGenericType();
		Class<?> listClass = (Class<?>) listType.getActualTypeArguments()[0];

		Object listTypeInstance = listClass.newInstance();

		if (listClass.getName().equals("java.lang.Object")) {
			listTypeInstance = new String("java.lang.Object");
		} else if (listClass.getName().equals("java.lang.String")) {
			listTypeInstance = new String(field.getName() + "-Item");
		} else {
			instantiateFields(listTypeInstance);
		}

		List<Object> list = (List<Object>) instance;
		list.add(listTypeInstance);
	}
	
	private void instantiateSet(Class<?> clazz, Field field, Object instance)
			throws IllegalAccessException, InstantiationException {

		ParameterizedType setType = (ParameterizedType) field.getGenericType();
		Class<?> setClass = (Class<?>) setType.getActualTypeArguments()[0];

		Object setTypeInstance = setClass.newInstance();

		if (setClass.getName().equals("java.lang.Object")) {
			setTypeInstance = new String("java.lang.Object");
		} else if (setClass.getName().equals("java.lang.String")) {
			setTypeInstance = new String(field.getName() + "-Item");
		} else {
			instantiateFields(setTypeInstance);
		}

		Set<Object> list = (Set<Object>) instance;
		list.add(setTypeInstance);
	}

	private void instantiateMap(Class<?> clazz, Field field, Object instance)
			throws IllegalAccessException, InstantiationException {
		ParameterizedType mapType = (ParameterizedType) field.getGenericType();

		// handle map key
		Class<?> mapKeyClass = (Class<?>) mapType.getActualTypeArguments()[0];
		Object mapKeyTypeInstance = null;
		if (mapKeyClass.getName().equals("java.lang.String")) {
			mapKeyTypeInstance = new String(field.getName() + "-Item");
		} else {
			mapKeyTypeInstance = mapKeyClass.newInstance();
		}
		instantiateFields(mapKeyTypeInstance);

		// handle map value
		Class<?> mapValueClass = (Class<?>) mapType.getActualTypeArguments()[1];
		Object mapValueTypeInstance = null;
		if (mapValueClass.getName().equals("java.lang.Object")) {
			mapValueTypeInstance = new String("java.lang.Object");
		} else if (mapValueClass.getName().equals("java.lang.String")) {
			mapValueTypeInstance = new String(field.getName());
		} else {
			mapValueTypeInstance = mapValueClass.newInstance();
			instantiateFields(mapValueTypeInstance);
		}

		Map<Object, Object> map = (Map<Object, Object>) instance;
		map.put(mapKeyTypeInstance, mapValueTypeInstance);
	}
}