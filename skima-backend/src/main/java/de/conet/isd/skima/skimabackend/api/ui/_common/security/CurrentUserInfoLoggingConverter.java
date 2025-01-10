//package de.conet.isd.skima.skimabackend.api.ui._common.security;
//
//
//
// NOTE: add sth like this if privacy in log statements is a concern
//
//
//
//import ch.qos.logback.core.pattern.Converter;
//import de.conet.isd.skima.skimabackend.service._common.config.AppConfig;
//import org.springframework.context.annotation.Configuration;
//
//
//@Configuration
//public class CurrentUserInfoLoggingConverter extends Converter<Object> {
//
//    private final AppConfig appConfig;
//
//    public CurrentUserInfoLoggingConverter(AppConfig appConfig) {
//        this.appConfig = appConfig;
//    }
//
//    @Override
//    public String convert(Object event) {
//        if (event instanceof CurrentUserInfo currentUser) {
//            return appConfig.loggingPrivacyProtection ? currentUser.toAnonymisedString() : currentUser.toString();
//        }
//        return event.toString();
//    }
//}
