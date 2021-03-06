//$Id:  $
/*
 * <p><b>License and Copyright: </b>The contents of this file is subject to the
 * same open source license as the Fedora Repository System at www.fedora-commons.org
 * Copyright &copy; 2006, 2007, 2008 by The Technical University of Denmark.
 * All rights reserved.</p>
 */
package dk.defxws.fedoragsearch.server;

import fedora.server.management.FedoraAPIM;

/**
 * 
 * @author  gsp@dtic.dtu.dk
 * @version
 */public interface SearchResultFiltering {
	    
	    public String selectIndexNameForPresearch(String fgsUserName, String indexName) throws java.rmi.RemoteException;
	    
	    public String rewriteQueryForInsearch(String fgsUserName, String indexName, String query) throws java.rmi.RemoteException;
	    
	    public StringBuffer filterResultsetForPostsearch(String fgsUserName, StringBuffer resultSetXml, Config config) throws java.rmi.RemoteException;

}
