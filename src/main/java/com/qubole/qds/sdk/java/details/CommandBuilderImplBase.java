package com.qubole.qds.sdk.java.details;

import com.qubole.qds.sdk.java.api.InvokableBuilder;
import com.qubole.qds.sdk.java.client.QdsClient;
import com.qubole.qds.sdk.java.entities.CommandResponse;
import org.codehaus.jackson.node.ObjectNode;
import javax.ws.rs.client.InvocationCallback;
import java.io.IOException;
import java.util.concurrent.Future;

abstract class CommandBuilderImplBase extends InvocationCallbackBase<CommandResponse> implements InvokableBuilder<CommandResponse>
{
    private final QdsClient client;
    private final ClientEntity.Method method;

    @Override
    public final Future<CommandResponse> invoke()
    {
        ClientEntity entity = makeJsonEntity(getEntity(), method);
        return invokeRequest(client, null, entity, CommandResponse.class, "commands");
    }

    static ClientEntity makeJsonEntity(ObjectNode node, ClientEntity.Method method)
    {
        String json;
        try
        {
            json = QdsClientImpl.getMapper().writeValueAsString(node);
        }
        catch ( IOException e )
        {
            throw new RuntimeException("Could not serialize " + node, e);
        }
        return new ClientEntity(json, method);
    }

    protected abstract ObjectNode getEntity();

    protected CommandBuilderImplBase(QdsClient client)
    {
        this(client, ClientEntity.Method.POST);
    }

    protected CommandBuilderImplBase(QdsClient client, ClientEntity.Method method)
    {
        this.client = client;
        this.method = method;
    }
}
