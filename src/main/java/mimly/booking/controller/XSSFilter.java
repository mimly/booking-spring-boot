package mimly.booking.controller;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;

@Slf4j
public class XSSFilter extends OncePerRequestFilter {

    @SneakyThrows
    private static String sanitize(String raw) {
        String unsafe = raw == null ? null : URLDecoder.decode(raw, "UTF-8");
        String safe = unsafe == null ? null : Jsoup.clean(unsafe, Whitelist.none());
        log.info(String.format("\n    raw: %s\n unsafe: %s\n   safe: %s", raw, unsafe, safe));
        return safe;
    }

    private static class XSSRequestWrapper extends HttpServletRequestWrapper {

        /**
         * Constructs a request object wrapping the given request.
         *
         * @param request The request to wrap
         * @throws IllegalArgumentException if the request is null
         */
        public XSSRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String[] getParameterValues(String name) {
            String[] values = super.getParameterValues(name);
            if (values == null) return null;
            for (int i = 0; i < values.length; ++i) {
                values[i] = sanitize(values[i]);
            }
            return values;
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        filterChain.doFilter(new XSSRequestWrapper(request), response);
    }
}