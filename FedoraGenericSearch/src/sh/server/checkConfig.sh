#!/bin/sh
#$Id$

# <p><b>License and Copyright: </b>The contents of this file will be subject to the
# same open source license as the Fedora Repository System at www.fedora.info
# It is expected to be released with Fedora version 2.2.

# Copyright &copy; 2006 by The Technical University of Denmark.
# All rights reserved.</p>

# @author  gsp@dtv.dk
# @version 

echo "Starting Config check"

LIB=../WEB-INF/lib
JARS=$LIB/fedora-2.1.1-server.jar:$LIB/log4j-1.2.8.jar:$LIB/activation-1.0.2.jar:$LIB/axis.jar:$LIB/commons-discovery.jar:$LIB/commons-logging.jar:$LIB/jaxrpc.jar:$LIB/mail.jar:$LIB/saaj.jar:$LIB/lucene-core-2.0.0.jar:$LIB/wsdl4j-1.5.1.jar:$LIB/PDFBox-0.7.2.jar:$LIB/xml-apis.jar

java -cp ../WEB-INF/classes:$JARS dk.defxws.fedoragsearch.server.Config
