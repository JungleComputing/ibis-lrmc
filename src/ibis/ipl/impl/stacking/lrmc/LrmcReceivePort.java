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
import ibis.ipl.ReceivePortConnectUpcall;
import ibis.ipl.ReceivePortIdentifier;
import ibis.ipl.SendPortIdentifier;

public class LrmcReceivePort implements
        ibis.ipl.ReceivePort {
      
    private final PortType portType;
    private final LrmcReceivePortIdentifier identifier;
    private final LrmcIbis ibis;
    private final MessageUpcall upcall;

    public LrmcReceivePort(PortType type, LrmcIbis ibis, String name,
            MessageUpcall upcall, ReceivePortConnectUpcall connectUpcall,
            Properties properties) throws IOException {
        if (connectUpcall != null) {
            throw new IbisConfigurationException("Connection upcalls are not supported.");
        }
        portType = type;
        identifier = new LrmcReceivePortIdentifier(ibis.identifier(), name);
        this.ibis = ibis;
        this.upcall = upcall;
        if (upcall != null && ! portType.hasCapability(PortType.RECEIVE_AUTO_UPCALLS)) {
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
        // Not supported.
        return null;
    }

    public void disableConnections() {
        // Connection downcalls/upcalls not supported.
    }

    public void disableMessageUpcalls() {
        // TODO
    }

    public void enableConnections() {
//      Connection downcalls/upcalls not supported.
    }

    public void enableMessageUpcalls() {
        // TODO Auto-generated method stub
    }

    public PortType getPortType() {
        return portType;
    }

    public ReceivePortIdentifier identifier() {
        // TODO Auto-generated method stub
        return identifier;
    }

    public SendPortIdentifier[] lostConnections() {
        // Connection downcalls/upcalls not supported.
        return null;
    }

    public String name() {
        return identifier.name;
    }

    public SendPortIdentifier[] newConnections() {
        // Connection downcalls/upcalls not supported.
        return null;
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
        if (timeout > 0 &&
                ! portType.hasCapability(PortType.RECEIVE_TIMEOUT)) {
            throw new IbisConfigurationException(
                    "This port is not configured for receive() with timeout");
        }

        // TODO Auto-generated method stub
        return null;
    }

    public String getManagementProperty(String key) throws NoSuchPropertyException {
        throw new NoSuchPropertyException("No properties in LRMCReceivePort");
    }

    public Map<String, String> managementProperties() {
        return new HashMap<String, String>();
    }

    public void printManagementProperties(PrintStream stream) {
    }

    public void setManagementProperties(Map<String, String> properties) throws NoSuchPropertyException {
        throw new NoSuchPropertyException("No properties in LRMCReceivePort");
    }

    public void setManagementProperty(String key, String value) throws NoSuchPropertyException {
        throw new NoSuchPropertyException("No properties in LRMCReceivePort");
    }
}
