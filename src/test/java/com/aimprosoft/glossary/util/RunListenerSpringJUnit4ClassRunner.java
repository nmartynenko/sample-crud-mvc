package com.aimprosoft.glossary.util;

import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

public class RunListenerSpringJUnit4ClassRunner extends SpringJUnit4ClassRunner{

    private RunListener runListener;

    /**
     * Constructs a new {@code SpringJUnit4ClassRunner} and initializes a
     * {@link TestContextManager} to provide Spring testing functionality to
     * standard JUnit tests.
     *
     * @param clazz the test class to be run
     * @see #createTestContextManager(Class)
     */
    public RunListenerSpringJUnit4ClassRunner(Class<?> clazz) throws InitializationError {
        super(clazz);

        WithRunListener annotation = clazz.getAnnotation(WithRunListener.class);
        if (annotation != null){
            try {
                runListener = annotation.value().newInstance();
            } catch (Exception e) {
                throw new InitializationError(e);
            }
        }
    }

    @Override
    public void run(RunNotifier notifier) {
        if (runListener != null){
            notifier.addListener(runListener);
        }

        super.run(notifier);
    }
}
