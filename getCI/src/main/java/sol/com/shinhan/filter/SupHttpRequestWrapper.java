package sol.com.shinhan.filter;

import org.apache.commons.io.IOUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SupHttpRequestWrapper extends HttpServletRequestWrapper {

    private byte[] bodyData;

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    public SupHttpRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);

        InputStream inputStream = super.getInputStream();
        bodyData = IOUtils.toByteArray(inputStream);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bodyData);
        return new SupServletInputStreamWrapper(byteArrayInputStream);
    }
}
