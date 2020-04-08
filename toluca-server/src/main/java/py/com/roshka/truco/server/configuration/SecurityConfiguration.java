package py.com.roshka.truco.server.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import py.com.roshka.truco.api.TrucoPrincipal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

@Configuration
public class SecurityConfiguration implements WebMvcConfigurer {

    Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(createHandlerInterceptorAdapter()).addPathPatterns("/api/**");
    }

    private HandlerInterceptorAdapter createHandlerInterceptorAdapter() {
        return new HandlerInterceptorAdapter() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                String authentication = request.getHeader("Authentication");
                logger.debug("Checking Security Token [" + authentication + "]");
                if (!StringUtils.isEmpty(authentication)) {
                    SecurityContextHolder.getContext().setAuthentication(new Authentication() {
                        @Override
                        public Collection<? extends GrantedAuthority> getAuthorities() {
                            return null;
                        }

                        @Override
                        public Object getCredentials() {
                            return null;
                        }

                        @Override
                        public Object getDetails() {
                            return null;
                        }

                        @Override
                        public Object getPrincipal() {
                            return new TrucoPrincipal(authentication);
                        }

                        @Override
                        public boolean isAuthenticated() {
                            return false;
                        }

                        @Override
                        public void setAuthenticated(boolean b) throws IllegalArgumentException {

                        }

                        @Override
                        public String getName() {
                            return null;
                        }
                    });
                    return super.preHandle(request, response, handler);
                } else {
                    throw new RuntimeException("Authentication is required");
                }

            }

            @Override
            public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
                super.postHandle(request, response, handler, modelAndView);
            }

            @Override
            public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
                super.afterCompletion(request, response, handler, ex);
            }

            @Override
            public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                super.afterConcurrentHandlingStarted(request, response, handler);
            }
        };
    }
}
