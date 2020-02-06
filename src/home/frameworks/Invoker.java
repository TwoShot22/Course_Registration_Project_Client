package home.frameworks;

import java.io.Serializable;
import java.lang.reflect.Method;

public class Invoker implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String className;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameters;

    public Invoker(String className, String methodName, Class<?>[] parameterTypes, Object[] parameters) {
    	this.className = className;
    	this.methodName = methodName;
    	this.parameterTypes = parameterTypes;
        this.parameters = parameters;
    }
    
    public String getClassName() {
    	return this.className;
    }

    public String getMethodName() {
		return methodName;
	}

	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}

	public Object[] getParameters() {
        return parameters;
    }
}
