package com.zvosframework.schedule.executor.sample.handler;

import com.zvosframework.schedule.core.handler.annotation.Schedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author xuxueli 2019-12-11 21:52:51
 */
@Component
public class ParamJobHandler {
    private static Logger logger = LoggerFactory.getLogger(ParamJobHandler.class);

    /**
     * String parameter
     */
    @Schedule("paramStringJob")
    public void paramStringJob(String param) {
        logger.info("XXL-JOB, String parameter: {}", param);
    }

    /**
     * Integer parameter
     */
    @Schedule("paramIntegerJob")
    public void paramIntegerJob(Integer param) {
        logger.info("XXL-JOB, Integer parameter: {}", param);
    }

    /**
     * Long parameter
     */
    @Schedule("paramLongJob")
    public void paramLongJob(Long param) {
        logger.info("XXL-JOB, Long parameter: {}", param);
    }

    /**
     * Boolean parameter
     */
    @Schedule("paramBooleanJob")
    public void paramBooleanJob(Boolean param) {
        logger.info("XXL-JOB, Boolean parameter: {}", param);
    }

    /**
     * Double parameter
     */
    @Schedule("paramDoubleJob")
    public void paramDoubleJob(Double param) {
        logger.info("XXL-JOB, Double parameter: {}", param);
    }

    /**
     * Custom object parameter (JSON)
     */
    @Schedule("paramObjectJob")
    public void paramObjectJob(User param) {
        logger.info("XXL-JOB, Object parameter: name={}, age={}", param.getName(), param.getAge());
    }

    /**
     * User class for testing
     */
    public static class User {
        private String name;
        private int age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

}
