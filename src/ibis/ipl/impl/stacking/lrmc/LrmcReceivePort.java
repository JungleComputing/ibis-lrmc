package ibis.ipl.impl.stacking.lrmc;

import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import ibis.ipl.IbisConfigurationException;
import ibis.ipl.MessageUpcall;
import ibis.ipl.NoSuchPropertyException;
import ibis.ipl.PortType;
import ibis.ipl.ReadMessage;
import ibis.ipl.ReceivePortIdentifier;
import ibis.ipl.SendPortIdentifier;

// TODO: create thread that actually reads ...

public class LrmcReceivePort implements ibis.ipl.ReceivePort {

    private final PortType portType;
    private final LrmcReceivePortIdentifier identifier;
    private final MessageUpcall upcall;
    private final Multicaster om;

    public LrmcReceivePort(Multicaster om, LrmcIbis ibis, MessageUpcall upcall,
            Properties properties) throws IOException {
        portType = om.portType;
        this.om = om;
        identifier = new LrmcReceivePortIdentifier(ibis.identifier(), om.name);
        this.upcall = upcall;
        if (upcall != null
                && !portType.hasCapability(PortType.RECEIVE_AUTO_UPCALLS)) {
            throw new IbisConfigurationException(
                    "no connection upcalls requested for this port type");
        }
    }

    public void close() throws IOException {
        // TODO Auto-generated method stub
    }

    public void close(long timeoutMillis) throws IOException {
        close();
    }

    public SendPortIdentifier[] connectedTo() {
        throw new IbisConfigurationException("connection downcalls not supported");
    }

    public void disableConnections() {
        throw new IbisConfigurationException("connection upcalls not supported");
    }

    public void disableMessageUpcalls() {
        // TODO
    }

    public void enableConnections() {
        throw new IbisConfigurationException("connection upcalls not supported");
    }

    public void enableMessageUpcalls() {
        // TODO Auto-generated method stub
    }

    public PortType getPortType() {
        return portType;
    }

    public ReceivePortIdentifier identifier() {
        return identifier;
    }

    public SendPortIdentifier[] lostConnections() {
        throw new IbisConfigurationException("connection downcalls not supported");
    }

    public String name() {
        return identifier.name;
    }

    public SendPortIdentifier[] newConnections() {
        throw new IbisConfigurationException("connection downcalls not supported");
    }

    public ReadMessage poll() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    public ReadMessage receive() throws IOException {
        return receive(0);
    }

    public ReadMessage receive(long timeout) throws IOException {
        if (upcall != null) {
            throw new IbisConfigurationException(
                    "Configured Receiveport for upcalls, downcall not allowed");
        }

        if (timeout < 0) {
            throw new IOException("timeout must be a non-negative number");
        }
        if (timeout > 0 && !portType.hasCapability(PortType.RECEIVE_TIMEOUT)) {
            throw new IbisConfigurationException(
                    "This port is not configured for receive() with timeout");
        }

        // TODO Auto-generated method stub
        return null;
    }

    public String getManagementProperty(String key)
            throws NoSuchPropertyException {
        throw new NoSuchPropertyException("No properties in LRMCReceivePort");
    }

    public Map<String, String> managementProperties() {
        return new HashMap<String, String>();
    }

    public void printManagementProperties(PrintStream stream) {
    }

    public void setManagementProperties(Map<String, String> properties)
            throws NoSuchPropertyException {
        throw new NoSuchPropertyException("No properties in LRMCReceivePort");
    }

    public void setManagementProperty(String key, String value)
            throws NoSuchPropertyException {
        throw new NoSuchPropertyException("No properties in LRMCReceivePort");
    }
}
