package br.com.caelum.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Demo {

	public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Test obj = new Test();
		
		
		Class<? extends Test> cls = obj.getClass();
		
		Constructor<? extends Test> constructor = cls.getConstructor();
		
		Method[] methods = cls.getMethods();
		
		for (Method method : methods) {
			System.out.println(method.getName());
		}
		
		
		Method meth = cls.getMethod("fuckTodoMundo");
		
		System.out.println(meth.invoke(obj));
		
		
	}
	
	
	
}
