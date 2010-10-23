package org.eclipse.ecf.osgi.services.remoteserviceadmin;

import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.discovery.IDiscoveryAdvertiser;
import org.eclipse.ecf.discovery.IServiceInfo;
import org.eclipse.ecf.discovery.IServiceProperties;
import org.eclipse.ecf.discovery.ServiceInfo;
import org.eclipse.ecf.discovery.identity.IServiceTypeID;
import org.eclipse.ecf.discovery.identity.ServiceIDFactory;

public abstract class AbstractServiceInfoFactory extends
		AbstractMetadataFactory implements IServiceInfoFactory {

	protected Map<EndpointDescription, IServiceInfo> serviceInfos = new HashMap();

	protected IServiceInfo addServiceInfo(EndpointDescription endpointDescription, IServiceInfo serviceInfo) {
		if (endpointDescription == null) return null;
		synchronized (serviceInfos) {
			return serviceInfos.put(endpointDescription, serviceInfo);
		}
	}
	
	protected IServiceInfo removeServiceInfo(EndpointDescription endpointDescription) {
		if (endpointDescription == null) return null;
		synchronized (serviceInfos) {
			return serviceInfos.remove(endpointDescription);
		}
	}
	
	protected boolean hasServiceInfo(EndpointDescription endpointDescription) {
		if (endpointDescription == null) return false;
		synchronized (serviceInfos) {
			return serviceInfos.containsKey(endpointDescription);
		}
	}
	
	public IServiceInfo createServiceInfoForDiscovery(
			EndpointDescription endpointDescription,  IDiscoveryAdvertiser advertiser) {
		// First check if we already have a serviceInfo for endpointDescription
		IServiceInfo existingServiceInfo = null;
		synchronized (serviceInfos) {
			existingServiceInfo = serviceInfos.get(endpointDescription);
		}
		// If so, then we return null
		if (existingServiceInfo != null) return null;
		
		IServiceTypeID serviceTypeID = createServiceTypeID(endpointDescription,advertiser);
		String serviceName = createServiceName(endpointDescription,advertiser,serviceTypeID);
		URI uri = null;
		try {
			uri = createURI(endpointDescription,advertiser,serviceTypeID,serviceName);
		} catch (URISyntaxException e) {
			String message = "URI could not be created for endpoint description="+endpointDescription;
			logError("createURI", message, e);
			throw new RuntimeException(message,e);
		}
		IServiceProperties serviceProperties = createServiceProperties(endpointDescription,advertiser,serviceTypeID,serviceName,uri);
		return addServiceInfo(endpointDescription,new ServiceInfo(uri,serviceName,serviceTypeID,serviceProperties));
	}

	protected IServiceProperties createServiceProperties(
			EndpointDescription endpointDescription,
			IDiscoveryAdvertiser advertiser, IServiceTypeID serviceTypeID,
			String serviceName, URI uri) {
		// TODO Auto-generated method stub
		return null;
	}

	protected URI createURI(EndpointDescription endpointDescription,
			IDiscoveryAdvertiser advertiser, IServiceTypeID serviceTypeID,
			String serviceName) throws URISyntaxException {
		String path = "/"+serviceName;
		String str = endpointDescription.getID().getName();
		URI uri = null;
		while (true) {
			try {
				uri = new URI(str);
				if (uri.getHost() != null) {
					break;
				} else {
					final String rawSchemeSpecificPart = uri
							.getRawSchemeSpecificPart();
					// make sure we break eventually
					if (str.equals(rawSchemeSpecificPart)) {
						uri = null;
						break;
					} else {
						str = rawSchemeSpecificPart;
					}
				}
			} catch (URISyntaxException e) {
				uri = null;
				break;
			}
		}
		String scheme = RemoteConstants.SERVICE_TYPE;
		int port = 32565;
		if (uri != null) {
			port = uri.getPort();
			if (port == -1)
				port = 32565;
		}
		String host = null;
		if (uri != null) {
			host = uri.getHost();
		} else {
			try {
				host = InetAddress.getLocalHost().getHostAddress();
			} catch (Exception e) {
				logInfo("createURI", //$NON-NLS-1$
						"failed to get local host adress, falling back to \'localhost\'.", e); //$NON-NLS-1$
				host = "localhost"; //$NON-NLS-1$
			}
		}
		return new URI(scheme, null, host, port, path, null, null);
	}

	protected void logInfo(String methodName, String message, Throwable t) {
		// XXX todo
	}
	
	protected void logError(String methodName, String message, Throwable t) {
		// XXX todo
	}

	protected String createServiceName(EndpointDescription endpointDescription,
			IDiscoveryAdvertiser advertiser, IServiceTypeID serviceTypeID) {
		// First create unique default name
		String defaultServiceName = createDefaultServiceName(endpointDescription,advertiser,serviceTypeID);
		// Look for service name that was explicitly set
		String serviceName = getStringPropertyWithDefault(
				endpointDescription.getProperties(),
				RemoteConstants.DISCOVERY_SERVICE_NAME, defaultServiceName);
		return serviceName;
	}

	protected String createDefaultServiceName(EndpointDescription endpointDescription, IDiscoveryAdvertiser advertiser, IServiceTypeID serviceTypeID) {
		return RemoteConstants.DISCOVERY_DEFAULT_SERVICE_NAME_PREFIX
				+ IDFactory.getDefault().createGUID().getName();
	}

	protected IServiceTypeID createServiceTypeID(
			EndpointDescription endpointDescription, IDiscoveryAdvertiser advertiser) {
		Map props = endpointDescription.getProperties();
		String[] scopes = getStringArrayPropertyWithDefault(props, RemoteConstants.DISCOVERY_SCOPE, IServiceTypeID.DEFAULT_SCOPE);
		String[] protocols = getStringArrayPropertyWithDefault(props, RemoteConstants.DISCOVERY_PROTOCOLS, IServiceTypeID.DEFAULT_SCOPE);
		String namingAuthority = getStringPropertyWithDefault(props, RemoteConstants.DISCOVERY_NAMING_AUTHORITY, IServiceTypeID.DEFAULT_NA);
		return ServiceIDFactory.getDefault().createServiceTypeID(advertiser.getServicesNamespace(),
				new String[] { RemoteConstants.SERVICE_TYPE }, scopes,
				protocols, namingAuthority);
	}

	public IServiceInfo createServiceInfoForUndiscovery(
			EndpointDescription endpointDescription,  IDiscoveryAdvertiser advertiser) {
		// XXX todo
		return null;
	}

	public void close() {
		synchronized (serviceInfos) {
			serviceInfos.clear();
		}
		super.close();
	}
}
