-------------------------------------------------------------------
              FedoraGSearch Version 2.0
-------------------------------------------------------------------

 * License and Copyright: FedoraGSearch is subject to the same open source 
 * license as the Fedora Repository System at www.fedora-commons.org
 * Copyright 2006, 2007, 2008 by The Technical University of Denmark.
 * All rights reserved.
 
 The FedoraGSearch development is funded by 
 
     DEFF, Denmark's Electronic Research Library, http://www.deff.dk .
     
The developer is Gert Schmeltz Pedersen, gsp@dtic.dtu.dk, at 
the Technical Information Center of Denmark at the Technical University of Denmark.

Contact in the Fedora core developer team is Chris Wilper, cwilper@fedora-commons.org .

Feedback, requests, etc. may also be sent to 
  fedora-commons-developers@lists.sourceforge.net or to
  fedora-commons-users@lists.sourceforge.net .

A prototype was released in March 2006, working with Fedora version 2.1b.

Version 1.1 was released in January 2007, working with Fedora version 2.2.

Version 1.1.1 was released in May 2007 with a bug fix concerning snippets 
for search results.

Version 2.0 is released in February 2008, with seven new features requested by users.
The main and overall aim is to exploit more features of Lucene.
It works with Fedora version 2.2.1.

The Fedora System Documentation at http://www.fedora-commons.org/developers
has a link to FedoraGSearch information with introduction, new features, 
installation, configuration and additional architectural information.
 
The same information is available in the source download from sourceforge.net
at src/html/search-service.html, and after installation at
http://localhost:8080/fedoragsearch/index.html .

Briefly, the new features in Version 2.0 are:
				    
- Added a sortFields parameter to gfindObjects for Lucene, sorting
  search results as specified, exploiting Lucene classes for sorting.
  
- Added optimize options for Lucene indexing. The mergeFactor and maxBufferedDocs
  properties will affect performance as explained in the Lucene documentation.
  The optimize action of the updateIndex operation will perform the Lucene method 
  call IndexWriter.optimize(), which merges all segments together into a single segment, 
  optimizing an index for search. The optimize() is no longer called after each updateIndex.

- Added parameters to the indexDocXslt parameter of the updateIndex operation,
  enabling the transfer of param values into the indexing stylesheet.
				    
- Added untokenizedFields property to Lucene index.properties files.
  Adding the property with a list of all untokenized fields will
  ensure that they all select the appropriate analyzer.
				    
- Added properties snippetBegin and snippetEnd, making highlight code configurable.
				    
- Added property for custom URIResolver used by xslt transformers
  for basic authorization and SSL,
  see the example dk.defxws.fedoragsearch.server.URIResolverImpl class.
					
- Removed encoding of special characters in indexFields.
  Snippets now show special characters without modification.
  Indexes should be reindexed.

For examples, see the index.properties file in configTestOnLucene/index/TestOnLucene/.

The next version, presumably called 3.0, will be released with Fedora version 3.0.

-------------------------------------------------------------------