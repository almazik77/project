package ru.itis.carsharing.config;

import lombok.SneakyThrows;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import ru.itis.carsharing.security.config.SecurityConfig;

import javax.servlet.*;

public class ApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    private static final String LOCATION = "D:\\temp\\"; // Temporary location where files will be stored

    private static final long MAX_FILE_SIZE = 5242880; // 5MB : Max file size.
    // Beyond that size spring will throw exception.
    private static final long MAX_REQUEST_SIZE = 20971520; // 20MB : Total request size containing Multi part.

    private static final int FILE_SIZE_THRESHOLD = 0; // Size threshold after which files will be written to disk

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{ApplicationContextConfig.class, SecurityConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebConfig.class, SecurityConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    private MultipartConfigElement getMultipartConfigElement() {
        return new MultipartConfigElement(LOCATION, MAX_FILE_SIZE, MAX_REQUEST_SIZE, FILE_SIZE_THRESHOLD);
    }

    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        registration.setMultipartConfig(getMultipartConfigElement());
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
        appContext.register(ApplicationContextConfig.class);
        appContext.setServletContext(servletContext);

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("SpringDispatcher",
                new DispatcherServlet(appContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
        dispatcher.setAsyncSupported(true);
        dispatcher.setInitParameter("contextClass", appContext.getClass().getName());

        dispatcher.setMultipartConfig(getMultipartConfigElement());
//        servletContext.addListener(new ContextLoaderListener(appContext));

        // UTF8 Charactor Filter.
        FilterRegistration.Dynamic fr = servletContext.addFilter("encodingFilter", CharacterEncodingFilter.class);

        fr.setInitParameter("encoding", "UTF-8");
        fr.setInitParameter("forceEncoding", "true");
        fr.addMappingForUrlPatterns(null, true, "/*");
    }

//    @SneakyThrows
//    @Override
//    public void onStartup(ServletContext servletContext) {
//        AnnotationConfigWebApplicationContext springWebContext = new AnnotationConfigWebApplicationContext();
//        springWebContext.register(ApplicationContextConfig.class);
////        servletContext.addListener(new ContextLoaderListener(springWebContext));
//        ServletRegistration.Dynamic dispatcherServlet = servletContext.addServlet("dispatcher",
//                new DispatcherServlet(springWebContext));
//        dispatcherServlet.setLoadOnStartup(1);
//        dispatcherServlet.addMapping("/");
//
//        dispatcherServlet.setMultipartConfig(new MultipartConfigElement(LOCATION, MAX_FILE_SIZE, MAX_REQUEST_SIZE, FILE_SIZE_THRESHOLD));
//
//
//        DelegatingFilterProxy filter = new DelegatingFilterProxy("springSecurityFilterChain");
//        servletContext.addFilter("springSecurityFilterChain", filter).addMappingForUrlPatterns(null,
//                false, "/*");
//
//    }
}