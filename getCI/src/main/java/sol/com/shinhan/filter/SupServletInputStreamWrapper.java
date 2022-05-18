package sol.com.shinhan.filter;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

// 전문 위변조 체크

public class SupServletInputStreamWrapper extends ServletInputStream {

    private InputStream inputStream;

    public SupServletInputStreamWrapper(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public int read() throws IOException {
        return inputStream.read();
    }

    @Override
    public int read(byte[] b) throws IOException {
        return inputStream.read(b);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return inputStream.read(b, off, len);
    }

    @Override
    public int readLine(byte[] b, int off, int len) throws IOException {
        if(len<=0)
        {
            return 0;
        }
        int cnt = 0;
        int c;
        do{
            if((c=read())==-1)
            {
                break;
            }
            b[off++] = (byte) c;
            cnt++;
        }while (c!=10 && cnt!=len);
        return cnt <= 0 ? -1 : cnt;
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void setReadListener(ReadListener listener) {

    }
}
