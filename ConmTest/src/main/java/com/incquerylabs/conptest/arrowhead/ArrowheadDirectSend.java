package com.incquerylabs.conptest.arrowhead;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.Response;

import com.incquerylabs.conptest.Sender;

import eu.arrowhead.client.common.Utility;
import eu.arrowhead.client.common.model.ArrowheadService;
import eu.arrowhead.client.common.model.ArrowheadSystem;
import eu.arrowhead.client.common.model.OrchestrationForm;
import eu.arrowhead.client.common.model.OrchestrationResponse;
import eu.arrowhead.client.common.model.ServiceRequestForm;

public class ArrowheadDirectSend implements Sender{
	
	public static final String ORCH_IP =  "0.0.0.0";
	public static final int ORCH_PORT =  8440;
	public static final String SERVICE_NAME = "conptest";
	public static final String INTERFACE =  "TCP";
	File file;
	
	public ArrowheadDirectSend(File File) {
		file = File;
	}
	
	@Override
	public void send() {
		String orchUri = Utility.getUri(ORCH_IP, ORCH_PORT, "orchestrator/orchestration", false, false);
		ArrowheadSystem me = new ArrowheadSystem("ArrowheadDirectSender", "0.0.0.0", 1, null);
		Set<String> interfaces = new HashSet<String>();
		interfaces.add(INTERFACE);
		Map<String, String> serviceMetadata = new HashMap<String, String>();
		ArrowheadService service = new ArrowheadService(SERVICE_NAME, interfaces, serviceMetadata);
		Map<String, Boolean> flags = new HashMap<String, Boolean>();
		flags.put("overrideStore", true);
		
		ServiceRequestForm srf = new ServiceRequestForm.Builder(me).requestedService(service).orchestrationFlags(flags).build();
		Response r = null;
		r = Utility.sendRequest(orchUri, "POST", srf);	
		OrchestrationResponse or = r.readEntity(OrchestrationResponse.class);
		OrchestrationForm of =  or.getResponse().get(0);
		
		ArrowheadSystem provider = of.getProvider();
		
		Socket socket = null;
		try {
			socket = new Socket(provider.getAddress(), provider.getPort());
			FileInputStream fip = new FileInputStream(file);
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			while(fip.available() > 0) {
				out.write(fip.read());
			}
			fip.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}	
	}
}