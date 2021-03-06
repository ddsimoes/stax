/*
 * $Id: StaxEntityResolverWrapper.java,v 1.3 2007-07-19 22:33:12 ofung Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package stax.impl;

import java.io.InputStream;
import stax.XMLEventReader;
import stax.XMLResolver;
import stax.XMLStreamException;
import stax.XMLStreamReader;
import stax.impl.xerces.xni.XMLResourceIdentifier;
import stax.impl.xerces.xni.XNIException;
import stax.impl.xerces.xni.parser.XMLInputSource;

/**
 *
 * @author  Neeraj Bajaj
 */
public class StaxEntityResolverWrapper {
    
    XMLResolver fStaxResolver ;
    
    /** Creates a new instance of StaxEntityResolverWrapper */
    public StaxEntityResolverWrapper(XMLResolver resolver) {
        fStaxResolver = resolver ;
    }
    
    public void setStaxEntityResolver(XMLResolver resolver ){
        fStaxResolver = resolver ;
    }
    
    public XMLResolver getStaxEntityResolver(){
        return fStaxResolver ;
    }
    
    public StaxXMLInputSource resolveEntity(XMLResourceIdentifier resourceIdentifier)
    throws XNIException, java.io.IOException {
        Object object = null ;
        try{
            object = fStaxResolver.resolveEntity(resourceIdentifier.getPublicId(), resourceIdentifier.getLiteralSystemId(),
            resourceIdentifier.getBaseSystemId(), null);
            return getStaxInputSource(object) ;
        }catch(XMLStreamException streamException){
            throw new XNIException(streamException) ;
        }
    }
    
    StaxXMLInputSource getStaxInputSource(Object object){
        if(object == null) return null ;
        
        if(object  instanceof java.io.InputStream){
            return new StaxXMLInputSource(new XMLInputSource(null, null, null, (InputStream)object, null));
        }
        else if(object instanceof XMLStreamReader){
            return new StaxXMLInputSource((XMLStreamReader)object) ;
        }else if(object instanceof XMLEventReader){
            return new StaxXMLInputSource((XMLEventReader)object) ;
        }
        
        return null ;
    }
}//class StaxEntityResolverWrapper
