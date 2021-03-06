/*
 * $Id: DTDGrammarUtil.java,v 1.5 2007-07-19 22:33:13 ofung Exp $
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

/*
 * The Apache Software License, Version 1.1
 *
 *
 * Copyright (c) 1999-2002 The Apache Software Foundation.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Xerces" and "Apache Software Foundation" must
 *    not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    nor may "Apache" appear in their name, without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation and was
 * originally based on software copyright (c) 1999, International
 * Business Machines, Inc., http://www.apache.org.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package stax.impl.dtd;

import stax.impl.dtd.nonvalidating.*;
import stax.impl.Constants;
import stax.impl.XMLEntityManager;
import stax.impl.XMLErrorReporter;
import stax.impl.xerces.util.SymbolTable;
import stax.impl.xerces.util.XMLChar;
import stax.impl.xerces.util.XMLSymbols;
import stax.impl.xerces.xni.Augmentations;
import stax.impl.xerces.xni.QName;
import stax.impl.xerces.xni.NamespaceContext;
import stax.impl.xerces.xni.XMLAttributes;
import stax.impl.xerces.xni.XMLDocumentHandler;
import stax.impl.xerces.xni.XMLLocator;
import stax.impl.xerces.xni.XMLString;
import stax.impl.xerces.xni.XNIException;
import stax.impl.xerces.xni.parser.XMLComponentManager;
import stax.impl.xerces.xni.parser.XMLConfigurationException;
import stax.impl.xerces.xni.parser.XMLDocumentSource;
import javax.xml.XMLConstants;

 /*
  * @author Eric Ye, IBM
  * @author Andy Clark, IBM
  * @author Jeffrey Rodriguez IBM
  * @author Neil Graham, IBM
  * @author Sunitha Reddy, Sun Microsystems
  */

public class DTDGrammarUtil{
    
    
    /** Property identifier: symbol table. */
    protected static final String SYMBOL_TABLE =
    Constants.XERCES_PROPERTY_PREFIX + Constants.SYMBOL_TABLE_PROPERTY;
    
    protected static final String NAMESPACES =
    Constants.SAX_FEATURE_PREFIX + Constants.NAMESPACES_FEATURE;
    
    
    /** Compile to true to debug attributes. */
    private static final boolean DEBUG_ATTRIBUTES = false;
    
    /** Compile to true to debug element children. */
    private static final boolean DEBUG_ELEMENT_CHILDREN = false;
    
    protected DTDGrammar fDTDGrammar = null;
    /** Namespaces. */
    protected boolean fNamespaces;
    
    /** Symbol table. */
    protected SymbolTable fSymbolTable = null;
    
    /** Current element index. */
    private int fCurrentElementIndex = -1;
    
    /** Current content spec type. */
    private int fCurrentContentSpecType = -1;
    
    private boolean fInCDATASection = false;
    
    /** Content spec type stack. */
    private boolean[] fElementContentState = new boolean[8];
    
    /** Element depth. */
    private int fElementDepth = -1;
    
    /** True if inside of element content. */
    private boolean fInElementContent = false;
    
    /** Temporary atribute declaration. */
    private XMLAttributeDecl fTempAttDecl = new XMLAttributeDecl();
    
    /** Temporary qualified name. */
    private QName fTempQName = new QName();
    
    /** Temporary string buffers. */
    private StringBuffer fBuffer = new StringBuffer();
    
    private NamespaceContext fNamespaceContext = null;
    
    /** Default constructor. */
    public DTDGrammarUtil( SymbolTable symbolTable) {
        fSymbolTable = symbolTable;
    }
    
    public DTDGrammarUtil(DTDGrammar grammar,SymbolTable symbolTable) {
        fDTDGrammar = grammar;
        fSymbolTable = symbolTable;
    }
    
    public DTDGrammarUtil(DTDGrammar grammar, SymbolTable symbolTable, 
            NamespaceContext namespaceContext) {
        fDTDGrammar = grammar;
        fSymbolTable = symbolTable;
        fNamespaceContext = namespaceContext;
    }
    
    /*
     * Resets the component. The component can query the component manager
     * about any features and properties that affect the operation of the
     * component.
     *
     * @param componentManager The component manager.
     *
     * @throws SAXException Thrown by component on finitialization error.
     *                      For example, if a feature or property is
     *                      required for the operation of the component, the
     *                      component manager may throw a
     *                      SAXNotRecognizedException or a
     *                      SAXNotSupportedException.
     */
    public void reset(XMLComponentManager componentManager)
    throws XMLConfigurationException {
        
        fDTDGrammar = null;
        fInCDATASection = false;
        fInElementContent = false;
        fCurrentElementIndex = -1;
        fCurrentContentSpecType = -1;
        try {
            fNamespaces = componentManager.getFeature(NAMESPACES);
        }
        catch (XMLConfigurationException e) {
            fNamespaces = true;
        }
        fSymbolTable = (SymbolTable)componentManager.getProperty(Constants.XERCES_PROPERTY_PREFIX+Constants.SYMBOL_TABLE_PROPERTY);
        fElementDepth = -1;
    }
    
    
    /**
     * The start of an element.
     *
     * @param element    The name of the element.
     * @param attributes The element attributes.
     * @param augs   Additional information that may include infoset augmentations
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
    public void startElement(QName element, XMLAttributes attributes)  throws XNIException {
        handleStartElement(element, attributes);
    }
    
    /**
     * The end of an element.
     *
     * @param element The name of the element.
     * @param augs   Additional information that may include infoset augmentations
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
    public void endElement(QName element) throws XNIException {
        handleEndElement(element);
    }
    
    /**
     * The start of a CDATA section.
     * @param augs   Additional information that may include infoset augmentations
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
    public void startCDATA(Augmentations augs) throws XNIException {
        fInCDATASection = true;
    }
    
    /**
     * The end of a CDATA section.
     * @param augs   Additional information that may include infoset augmentations
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
    public void endCDATA(Augmentations augs) throws XNIException {
        fInCDATASection = false;
    }
    
    
    
    /** Add default attributes and validate. */
    public void addDTDDefaultAttrs(QName elementName, XMLAttributes attributes)
    throws XNIException {
        
        int elementIndex;
        elementIndex = fDTDGrammar.getElementDeclIndex(elementName);
        // is there anything to do?
        if (elementIndex == -1 || fDTDGrammar == null) {
            return;
        }
        
        //
        // Check after all specified attrs are scanned
        // (1) report error for REQUIRED attrs that are missing (V_TAGc)
        // (2) add default attrs (FIXED and NOT_FIXED)
        //
        int attlistIndex = fDTDGrammar.getFirstAttributeDeclIndex(elementIndex);
        
        while (attlistIndex != -1) {
            
            fDTDGrammar.getAttributeDecl(attlistIndex, fTempAttDecl);
            
            if (DEBUG_ATTRIBUTES) {
                if (fTempAttDecl != null) {
                    XMLElementDecl elementDecl = new XMLElementDecl();
                    fDTDGrammar.getElementDecl(elementIndex, elementDecl);
                    System.out.println("element: "+(elementDecl.name.localpart));
                    System.out.println("attlistIndex " + attlistIndex + "\n"+
                    "attName : '"+(fTempAttDecl.name.localpart) + "'\n"
                    + "attType : "+fTempAttDecl.simpleType.type + "\n"
                    + "attDefaultType : "+fTempAttDecl.simpleType.defaultType + "\n"
                    + "attDefaultValue : '"+fTempAttDecl.simpleType.defaultValue + "'\n"
                    + attributes.getLength() +"\n"
                    );
                }
            }
            String attPrefix = fTempAttDecl.name.prefix;
            String attLocalpart = fTempAttDecl.name.localpart;
            String attRawName = fTempAttDecl.name.rawname;
            String attType = getAttributeTypeName(fTempAttDecl);
            
            int attDefaultType =fTempAttDecl.simpleType.defaultType;
            String attValue = null;
            
            if (fTempAttDecl.simpleType.defaultValue != null) {
                attValue = fTempAttDecl.simpleType.defaultValue;
            }
            boolean specified = false;
            boolean required = attDefaultType == XMLSimpleType.DEFAULT_TYPE_REQUIRED;
            boolean cdata = attType == XMLSymbols.fCDATASymbol;
            
            if (!cdata || required || attValue != null) {
                
                //check whether attribute is a namespace declaration
                if (fNamespaceContext != null && attRawName.startsWith(XMLConstants.XMLNS_ATTRIBUTE)) {
                    String prefix = "";
                    int pos = attRawName.indexOf(':');
                    if (pos != -1) {
                        prefix = attRawName.substring(0, pos);
                    } else {
                        prefix = attRawName;
                    }
                    prefix = fSymbolTable.addSymbol(prefix);
                    if (!((stax.impl.xerces.util.
                            NamespaceSupport) fNamespaceContext).
                            containsPrefixInCurrentContext(prefix)) {
                        fNamespaceContext.declarePrefix(prefix, attValue);
                    }
                    specified = true;
                } else {
                    int attrCount = attributes.getLength();
                    for (int i = 0; i < attrCount; i++) {
                        if (attributes.getQName(i) == attRawName) {
                            specified = true;
                            break;
                        }
                    }
                }
                
            }
            
            if (!specified) {
                if (attValue != null) {
                    if (fNamespaces) {
                        int index = attRawName.indexOf(':');
                        if (index != -1) {
                            attPrefix = attRawName.substring(0, index);
                            attPrefix = fSymbolTable.addSymbol(attPrefix);
                            attLocalpart = attRawName.substring(index + 1);
                            attLocalpart = fSymbolTable.addSymbol(attLocalpart);
                        }
                    }
                    fTempQName.setValues(attPrefix, attLocalpart, attRawName, fTempAttDecl.name.uri);
                    int newAttr = attributes.addAttribute(fTempQName, attType, attValue);
                }
            }
            attlistIndex = fDTDGrammar.getNextAttributeDeclIndex(attlistIndex);
        }
        
        // now iterate through the expanded attributes for
        // 1. if every attribute seen is declared in the DTD
        // 2. check if the VC: default_fixed holds
        // 3. validate every attribute.
        int attrCount = attributes.getLength();
        for (int i = 0; i < attrCount; i++) {
            String attrRawName = attributes.getQName(i);
            boolean declared = false;
            int attDefIndex = -1;
            int position =
            fDTDGrammar.getFirstAttributeDeclIndex(elementIndex);
            while (position != -1) {
                fDTDGrammar.getAttributeDecl(position, fTempAttDecl);
                if (fTempAttDecl.name.rawname == attrRawName) {
                    // found the match att decl,
                    attDefIndex = position;
                    declared = true;
                    break;
                }
                position = fDTDGrammar.getNextAttributeDeclIndex(position);
            }
            if (!declared) {
                continue;
            }
            
            String type = getAttributeTypeName(fTempAttDecl);
            attributes.setType(i, type);
            
            boolean changedByNormalization = false;
            String oldValue = attributes.getValue(i);
            String attrValue = oldValue;
            if (attributes.isSpecified(i) && type != XMLSymbols.fCDATASymbol) {
                changedByNormalization = normalizeAttrValue(attributes, i);
                attrValue = attributes.getValue(i);
            }
        } // for all attributes
        
    } // addDTDDefaultAttrsAndValidate(int,XMLAttrList)
    
    
    /**
     * Normalize the attribute value of a non CDATA attributes collapsing
     * sequences of space characters (x20)
     *
     * @param attributes The list of attributes
     * @param index The index of the attribute to normalize
     */
    private boolean normalizeAttrValue(XMLAttributes attributes, int index) {
        // vars
        boolean leadingSpace = true;
        boolean spaceStart = false;
        boolean readingNonSpace = false;
        int count = 0;
        int eaten = 0;
        String attrValue = attributes.getValue(index);
        char[] attValue = new char[attrValue.length()];
        
        fBuffer.setLength(0);
        attrValue.getChars(0, attrValue.length(), attValue, 0);
        for (int i = 0; i < attValue.length; i++) {
            
            if (attValue[i] == ' ') {
                
                // now the tricky part
                if (readingNonSpace) {
                    spaceStart = true;
                    readingNonSpace = false;
                }
                
                if (spaceStart && !leadingSpace) {
                    spaceStart = false;
                    fBuffer.append(attValue[i]);
                    count++;
                }
                else {
                    if (leadingSpace || !spaceStart) {
                        eaten ++;
                    }
                }
                
            }
            else {
                readingNonSpace = true;
                spaceStart = false;
                leadingSpace = false;
                fBuffer.append(attValue[i]);
                count++;
            }
        }
        
        // check if the last appended character is a space.
        if (count > 0 && fBuffer.charAt(count-1) == ' ') {
            fBuffer.setLength(count-1);
            
        }
        String newValue = fBuffer.toString();
        attributes.setValue(index, newValue);
        return ! attrValue.equals(newValue);
    }
    
    
    
    /** convert attribute type from ints to strings */
    private String getAttributeTypeName(XMLAttributeDecl attrDecl) {
        
        switch (attrDecl.simpleType.type) {
            case XMLSimpleType.TYPE_ENTITY: {
                return attrDecl.simpleType.list ? XMLSymbols.fENTITIESSymbol : XMLSymbols.fENTITYSymbol;
            }
            case XMLSimpleType.TYPE_ENUMERATION: {
                StringBuffer buffer = new StringBuffer();
                buffer.append('(');
                for (int i=0; i<attrDecl.simpleType.enumeration.length ; i++) {
                    if (i > 0) {
                        buffer.append("|");
                    }
                    buffer.append(attrDecl.simpleType.enumeration[i]);
                }
                buffer.append(')');
                return fSymbolTable.addSymbol(buffer.toString());
            }
            case XMLSimpleType.TYPE_ID: {
                return XMLSymbols.fIDSymbol;
            }
            case XMLSimpleType.TYPE_IDREF: {
                return attrDecl.simpleType.list ? XMLSymbols.fIDREFSSymbol : XMLSymbols.fIDREFSymbol;
            }
            case XMLSimpleType.TYPE_NMTOKEN: {
                return attrDecl.simpleType.list ? XMLSymbols.fNMTOKENSSymbol : XMLSymbols.fNMTOKENSymbol;
            }
            case XMLSimpleType.TYPE_NOTATION: {
                return XMLSymbols.fNOTATIONSymbol;
            }
        }
        return XMLSymbols.fCDATASymbol;
        
    }
    
    
    /** ensure element stack capacity */
    private void ensureStackCapacity( int newElementDepth) {
        if (newElementDepth == fElementContentState.length) {
            boolean[] newStack = new boolean[newElementDepth * 2];
            System.arraycopy(this.fElementContentState, 0, newStack, 0, newElementDepth );
            fElementContentState = newStack;
        }
    }
    
    
    
    /** Handle element
     * @return true if validator is removed from the pipeline
     */
    protected void handleStartElement(QName element, XMLAttributes attributes) throws XNIException {
        
        if (fDTDGrammar == null) {
            fCurrentElementIndex = -1;
            fCurrentContentSpecType = -1;
            fInElementContent = false;
            return;
        }
        else {
            fCurrentElementIndex = fDTDGrammar.getElementDeclIndex(element);
            fCurrentContentSpecType = fDTDGrammar.getContentSpecType(fCurrentElementIndex);
            //handleDTDDefaultAttrs(element,attributes);
            addDTDDefaultAttrs(element,attributes);
        }
        
        fInElementContent = fCurrentContentSpecType == XMLElementDecl.TYPE_CHILDREN;
        fElementDepth++;
        ensureStackCapacity(fElementDepth);
        fElementContentState[fElementDepth] = fInElementContent;
    }
    
    
    /** Handle end element. */
    protected void handleEndElement(QName element) throws XNIException {
        fElementDepth--;
        if (fElementDepth < -1) {
            throw new RuntimeException("FWK008 Element stack underflow");
        }
        if (fElementDepth < 0) {
            fCurrentElementIndex = -1;
            fCurrentContentSpecType = -1;
            fInElementContent = false;
            return;
        }
        fInElementContent =  fElementContentState[fElementDepth];
    }
    
    public boolean isInElementContent(){
        return fInElementContent;
    }
    
    public boolean isIgnorableWhiteSpace(XMLString text){
        if (isInElementContent()){
            for (int i=text.offset; i< text.offset+text.length; i++) {
                if (!XMLChar.isSpace(text.ch[i])) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
