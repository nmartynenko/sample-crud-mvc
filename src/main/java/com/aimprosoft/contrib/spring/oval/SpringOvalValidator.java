package com.aimprosoft.contrib.spring.oval;

import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;
import net.sf.oval.context.FieldContext;
import net.sf.oval.context.OValContext;
import net.sf.oval.exception.ValidationFailedException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

public class SpringOvalValidator implements org.springframework.validation.Validator, InitializingBean {

    private Validator validator;

    public SpringOvalValidator() {
        validator = new Validator();
    }

    public SpringOvalValidator(Validator validator) {
        this.validator = validator;
    }

    @SuppressWarnings("unchecked")
    public boolean supports(Class clazz) {
        return true;
    }

    public void validate(Object target, Errors errors) {
        doValidate(target, errors, "");
    }

    @SuppressWarnings("unchecked")
    private void doValidate(Object target, Errors errors, String fieldPrefix) {
        try {
            for (ConstraintViolation violation : validator.validate(target)) {
                OValContext ctx = violation.getContext();
                String errorCode = violation.getErrorCode();
                String errorMessage = violation.getMessage();

                if (ctx instanceof FieldContext) {
                    String fieldName = fieldPrefix + ((FieldContext) ctx).getField().getName();
                    errors.rejectValue(fieldName, errorCode, errorMessage);
                } else {
                    errors.reject(errorCode, errorMessage);
                }
            }

            try {
                List<Field> fields = getFields(target);
                for (Field field : fields) {
                    SpringValidateNestedProperty validate = field.getAnnotation(SpringValidateNestedProperty.class);
                    if (validate != null) {
                        if (!field.isAccessible()) {
                            field.setAccessible(true);
                        }
                        Object nestedProperty = field.get(target);
                        if (nestedProperty != null) {
                            String name = field.getName();
                            if (nestedProperty instanceof Collection<?>) {
                                // valueToValidate is a collection
                                Collection<?> col = (Collection<?>) nestedProperty;

                                int index = 0;
                                for (Object object : col) {
                                    doValidate(object, errors, name + "[" + index + "].");
                                    index++;
                                }
                            } else if (nestedProperty instanceof Map<?, ?>){
                                //valueToValidate is a map, but only as a string key
                                Map<?,?> map = (Map<?, ?>) nestedProperty;

                                for (Map.Entry<?, ?> entry: map.entrySet()){
                                    Object key = entry.getKey();
                                    if (!(key instanceof String)){
                                        throw new IllegalArgumentException("Map as a nested property supports only String keys for validation");
                                    }

                                    doValidate(entry.getValue(), errors, name + "['" + key + "']");
                                }
                            }
                            else if (nestedProperty.getClass().isArray()) {
                                // valueToValidate is an array
                                int length = Array.getLength(nestedProperty);
                                for (int i = 0; i < length; i++) {
                                    Object o = Array.get(nestedProperty, i);
                                    doValidate(o, errors, name + "[" + i + "].");
                                }
                            } else {
                                // valueToValidate is other object
                                doValidate(nestedProperty, errors, name + ".");
                            }
                        }
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        } catch (final ValidationFailedException ex) {
            errors.reject(ex.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private List<Field> getFields(Object target) {
        return doGetFields(target.getClass());
    }

    @SuppressWarnings("unchecked")
    private List<Field> doGetFields(Class clazz) {
        ArrayList<Field> list = new ArrayList<Field>();
        Field[] fields = clazz.getDeclaredFields();
        list.addAll(Arrays.asList(fields));
        if (clazz.getSuperclass() != null) {
            list.addAll(doGetFields(clazz.getSuperclass()));
        }
        return list;
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(validator, "Property [validator] is not set");
    }

    public Validator getValidator() {
        return validator;
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }
}