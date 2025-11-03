package es.tecnalia.ittxartela.ws.server.interceptor;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuditOutputInterceptor extends AbstractPhaseInterceptor<Message> {

    public AuditOutputInterceptor() {
        super(Phase.PRE_STREAM);
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        try {
            OutputStream os = message.getContent(OutputStream.class);
            if (os == null) return;

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            message.setContent(OutputStream.class, new FilterOutputStream(baos) {
                @Override
                public void close() throws IOException {
                    super.close();
                    String body = baos.toString("UTF-8");
                   log.debug("<<< [OUT] Respuesta enviada:\n" + body);
                    os.write(baos.toByteArray());
                    os.close();
                }
            });
        } catch (Exception e) {
            throw new Fault(e);
        }
    }
}
