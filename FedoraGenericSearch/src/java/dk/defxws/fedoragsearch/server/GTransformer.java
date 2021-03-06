//$Id$
/*
 * <p><b>License and Copyright: </b>The contents of this file is subject to the
 * same open source license as the Fedora Repository System at www.fedora-commons.org
 * Copyright &copy; 2006, 2007, 2008 by The Technical University of Denmark.
 * All rights reserved.</p>
 */
package dk.defxws.fedoragsearch.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;

import java.util.Date;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;

import dk.defxws.fedoragsearch.server.errors.ConfigException;
import dk.defxws.fedoragsearch.server.errors.GenericSearchException;

/**
 * performs the stylesheet transformations
 * 
 * @author  gsp@dtv.dk
 * @version 
 */
public class GTransformer {
    
    private static final Logger logger =
        Logger.getLogger(GTransformer.class);
    int debuglength = 500;
    
    public GTransformer() {
    }
    
    /**
     * 
     *
     * @throws TransformerConfigurationException, TransformerException.
     */
    public Transformer getTransformer(String xsltPath) 
    throws ConfigException {
        return getTransformer(xsltPath, null);
    }
    
    public Transformer getTransformer(String xsltPath, URIResolver uriResolver) 
    throws ConfigException {
        Transformer transformer = null;
        String xsltPathName = "/"+xsltPath+".xslt";
        try {
            InputStream stylesheet = Config.class.getResourceAsStream(xsltPathName);
            if (stylesheet==null) {
                throw new ConfigException(xsltPathName+" not found");
            }
            TransformerFactory tfactory = TransformerFactory.newInstance();
            StreamSource xslt = new StreamSource(stylesheet);
            transformer = tfactory.newTransformer(xslt);
            if (uriResolver!=null)
            	transformer.setURIResolver(uriResolver);
        } catch (TransformerConfigurationException e) {
            throw new ConfigException("getTransformer "+xsltPathName+":\n", e);
        } catch (TransformerFactoryConfigurationError e) {
            throw new ConfigException("getTransformerFactory "+xsltPathName+":\n", e);
        }
        return transformer;
    }

    public StringBuffer transform(String xsltPath, StringBuffer sb, String[] params, String systemId) 
    throws ConfigException {
    	if (logger.isDebugEnabled())
    		logger.debug("transform xsltPath="+xsltPath+" sb="+getDebugString(sb.toString())+" systemId="+systemId);
        StringReader sr = new StringReader(sb.toString());
        StreamSource sourceStream = new StreamSource(sr, systemId);
        String xsltPathName = "/"+xsltPath+".xslt";
        URL stylesheet = GTransformer.class.getResource(xsltPathName);
        if (stylesheet==null) {
            throw new ConfigException("transform "+xsltPathName+" not found");
        }
        TransformerFactory tfactory = null;
		try {
			tfactory = TransformerFactory.newInstance();
		} catch (TransformerFactoryConfigurationError e) {
            throw new ConfigException("transform "+xsltPathName+":\n", e);
		}
        StreamSource xslt = null;
		try {
			xslt = new StreamSource(stylesheet.openStream(), stylesheet.toString());
		} catch (IOException e) {
            throw new ConfigException("transform "+xsltPathName+":\n", e);
		}
        Transformer transformer = null;
        try {
			transformer = tfactory.newTransformer(xslt);
		} catch (TransformerConfigurationException e) {
            throw new ConfigException("transform "+xsltPathName+":\n", e);
		}
        for (int i=0; i<params.length; i=i+2) {
            Object value = params[i+1];
            if (value==null) value = "";
            transformer.setParameter((String)params[i], value);
        }
        transformer.setParameter("DATETIME", new Date());
        StreamResult destStream = new StreamResult(new StringWriter());
        try {
            transformer.transform(sourceStream, destStream);
        } catch (TransformerException e) {
            throw new ConfigException("transform "+xsltPathName+":\n", e);
        }
        StringWriter sw = (StringWriter)destStream.getWriter();
        StringBuffer result = sw.getBuffer();
        if (logger.isDebugEnabled())
        	logger.debug("transform result=\n"+getDebugString(result.toString()));
        return result;
    }
    
    /**
     * 
     *
     * @throws TransformerConfigurationException, TransformerException.
     */
    public void transform(String xsltName, StreamSource sourceStream, StreamResult destStream) 
    throws GenericSearchException {
        Transformer transformer = getTransformer(xsltName);
        try {
            transformer.transform(sourceStream, destStream);
        } catch (TransformerException e) {
            throw new GenericSearchException("transform "+xsltName+".xslt:\n", e);
        }
    }

    public StringBuffer transform(String xsltName, Source sourceStream, Object[] params) 
    throws GenericSearchException {
        return transform (xsltName, sourceStream, null, params);
    }

    public StringBuffer transform(String xsltName, Source sourceStream, URIResolver uriResolver, Object[] params) 
    throws GenericSearchException {
        if (logger.isDebugEnabled())
            logger.debug("xsltName="+xsltName);
        Transformer transformer = getTransformer(xsltName, uriResolver);
        for (int i=0; i<params.length; i=i+2) {
            Object value = params[i+1];
            if (value==null) value = "";
            transformer.setParameter((String)params[i], value);
        }
        transformer.setParameter("DATETIME", new Date());
        StreamResult destStream = new StreamResult(new StringWriter());
        try {
            transformer.transform(sourceStream, destStream);
        } catch (TransformerException e) {
            throw new GenericSearchException("transform "+xsltName+".xslt:\n", e);
        }
        StringWriter sw = (StringWriter)destStream.getWriter();
//      if (logger.isDebugEnabled())
//      logger.debug("sw="+sw.getBuffer().toString());
        return sw.getBuffer();
    }
    
    /**
     * 
     *
     * @throws TransformerConfigurationException, TransformerException.
     */
    public void transformToFile(String xsltName, StreamSource sourceStream, Object[] params, String filePath) 
    throws GenericSearchException {
        if (logger.isDebugEnabled())
            logger.debug("xsltName="+xsltName);
        Transformer transformer = getTransformer(xsltName);
        for (int i=0; i<params.length; i=i+2) {
            Object value = params[i+1];
            if (value==null) value = "";
            transformer.setParameter((String)params[i], value);
        }
        transformer.setParameter("DATETIME", new Date());
        StreamResult destStream = new StreamResult(new File(filePath));
        try {
            transformer.transform(sourceStream, destStream);
        } catch (TransformerException e) {
            throw new GenericSearchException("transform "+xsltName+".xslt:\n", e);
        }
    }
    
    /**
     * 
     *
     * @throws TransformerConfigurationException, TransformerException.
     */
    public StringBuffer transform(String xsltName, StreamSource sourceStream) 
    throws GenericSearchException {
        return transform(xsltName, sourceStream, new String[]{});
    }
    
    /**
     * 
     *
     * @throws TransformerConfigurationException, TransformerException.
     */
    public StringBuffer transform(String xsltName, StringBuffer sb, String[] params) 
    throws GenericSearchException {
//      if (logger.isDebugEnabled())
//      logger.debug("sb="+sb);
        StringReader sr = new StringReader(sb.toString());
        StringBuffer result = transform(xsltName, new StreamSource(sr), params);
//      if (logger.isDebugEnabled())
//      logger.debug("xsltName="+xsltName+" result="+result);
        return result;
    }
    
    /**
     * 
     *
     * @throws TransformerConfigurationException, TransformerException.
     */
    public StringBuffer transform(String xsltName, StringBuffer sb) 
    throws GenericSearchException {
        return transform(xsltName, sb, new String[]{});
    }
    
    private String getDebugString(String debugString) {
    	String result = debugString;
    	if (debugString.length()>debuglength) {
    		result = result.substring(0,debuglength)+"...\n...";
    	}
    	return result;
    }
    
    public static void main(String[] args) {
        int argCount=2;
        try {
            if (args.length==argCount) {
                File f = new File(args[1]);
                StreamSource ss = new StreamSource(new File(args[1]));
                GTransformer gt = new GTransformer();
                StreamResult destStream = new StreamResult(new StringWriter());
                gt.transform(args[0], ss, destStream);
                StringWriter sw = (StringWriter)destStream.getWriter();
                System.out.print(sw.getBuffer().toString());
            } else {
                throw new IOException("Must supply " + argCount + " arguments.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(
            "Usage: GTransformer xsltName xmlFileName");
        }
    }
    
//  /**
//   * This is an unfinished attempt at caching entities for efficiency.
//   *
//   * @throws TransformerConfigurationException, TransformerException.
//   */
//  public StringBuffer transform(String xsltName, StreamSource sourceStream, Object[] params) 
//  throws GenericSearchException {
//      if (logger.isDebugEnabled())
//          logger.debug("xsltName="+xsltName);
//      Transformer transformer = getTransformer(xsltName);
//      for (int i=0; i<params.length; i=i+2) {
//          Object value = params[i+1];
//          if (value==null) value = "";
//          transformer.setParameter((String)params[i], value);
//      }
//      transformer.setParameter("DATETIME", new Date());
//      StreamResult destStream = new StreamResult(new StringWriter());  
//
//      InputSource src = new InputSource(sourceStream.getInputStream());
//      src.setSystemId(sourceStream.getSystemId());
//
//      XMLReader rdr = null;
//		try {
//			rdr = XMLReaderFactory.createXMLReader(javax.xml.parsers.SAXParserFactory.newInstance().newSAXParser().getXMLReader().getClass().getName());
//		} catch (SAXException e) {
//			throw new GenericSearchException("transform "+xsltName+".xslt:\n", e);
//		} catch (ParserConfigurationException e) {
//			throw new GenericSearchException("transform "+xsltName+".xslt:\n", e);
//		}
////		FIX ME, this will not reuse earlier cached entities
//      rdr.setEntityResolver(new CachedEntityResolver());
//
//      Source s = new SAXSource(rdr, src);
//      
//      try {
//          transformer.transform(s, destStream);
//      } catch (TransformerException e) {
//          throw new GenericSearchException("transform "+xsltName+".xslt:\n", e);
//      }
//      StringWriter sw = (StringWriter)destStream.getWriter();
////    if (logger.isDebugEnabled())
////    logger.debug("sw="+sw.getBuffer().toString());
//      return sw.getBuffer();
//  }
//    
//    private static class CachedEntityResolver implements EntityResolver {
//        private final Map cache = new HashMap();
//
//        public InputSource resolveEntity(String publicId, String systemId) throws IOException {
//          byte[] res = (byte[]) cache.get(systemId);
//          if (res == null) {
//            res = IOUtils.toByteArray(new URL(systemId).openStream());
//            cache.put(systemId, res);
//          }
//
//          InputSource is = new InputSource(new ByteArrayInputStream(res));
//          is.setPublicId(publicId);
//          is.setSystemId(systemId);
//
//          return is;
//        }
//      }
    
}
