package es.tecnalia.ittxartela.ws.server.interceptor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuditInputInterceptor extends AbstractPhaseInterceptor<Message> {

    public AuditInputInterceptor() {
        super(Phase.RECEIVE);
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        try {
            InputStream is = message.getContent(InputStream.class);
            if (is == null) return;

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int n;
            while ((n = is.read(buffer)) != -1) {
                baos.write(buffer, 0, n);
            }

            String body = baos.toString("UTF-8");
            log.debug(">>> [IN] Petición recibida:\n" + body);

            // Restaurar el InputStream
            message.setContent(InputStream.class, new ByteArrayInputStream(baos.toByteArray()));
        } catch (Exception e) {
            throw new Fault(e);
        }
    }
}
