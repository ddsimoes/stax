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
 * Copyright (c) 2000-2002 The Apache Software Foundation.  All rights
 * reserved.
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
 */

package stax.impl.xerces.util;

/**
 * Synchronized symbol table.
 *
 * This class moved into the util package since it's needed by multiple
 * other classes (CachingParserPool, XMLGrammarCachingConfiguration).
 *
 * @author Andy Clark, IBM
 * @version $Id: SynchronizedSymbolTable.java,v 1.3 2007-07-19 22:33:16 ofung Exp $
 */

public final class SynchronizedSymbolTable
    extends SymbolTable {
        
    //
    // Data
    //

    /** Main symbol table. */
    protected SymbolTable fSymbolTable;

    //
    // Constructors
    //

    /** Constructs a synchronized symbol table. */
    public SynchronizedSymbolTable(SymbolTable symbolTable) {
        fSymbolTable = symbolTable;
    } // <init>(SymbolTable) 

    // construct synchronized symbol table of default size
    public SynchronizedSymbolTable() {
        fSymbolTable = new SymbolTable();
    } // init()

    // construct synchronized symbol table of given size
    public SynchronizedSymbolTable(int size) {
        fSymbolTable = new SymbolTable(size);
    } // init(int)

    //
    // SymbolTable methods
    //
        
    /**
     * Adds the specified symbol to the symbol table and returns a
     * reference to the unique symbol. If the symbol already exists, 
     * the previous symbol reference is returned instead, in order
     * guarantee that symbol references remain unique.
     * 
     * @param symbol The new symbol.
     */
    public String addSymbol(String symbol) {

        synchronized (fSymbolTable) {
            return fSymbolTable.addSymbol(symbol);
        }

    } // addSymbol(String)

    /**
     * Adds the specified symbol to the symbol table and returns a
     * reference to the unique symbol. If the symbol already exists, 
     * the previous symbol reference is returned instead, in order
     * guarantee that symbol references remain unique.
     * 
     * @param buffer The buffer containing the new symbol.
     * @param offset The offset into the buffer of the new symbol.
     * @param length The length of the new symbol in the buffer.
     */
    public String addSymbol(char[] buffer, int offset, int length) {

        synchronized (fSymbolTable) {
            return fSymbolTable.addSymbol(buffer, offset, length);
        }

    } // addSymbol(char[],int,int):String

    /** 
     * Returns true if the symbol table already contains the specified
     * symbol.
     *
     * @param symbol The symbol to look for.
     */
    public boolean containsSymbol(String symbol) {

        synchronized (fSymbolTable) {
            return fSymbolTable.containsSymbol(symbol);
        }

    } // containsSymbol(String):boolean

    /** 
     * Returns true if the symbol table already contains the specified
     * symbol.
     *
     * @param buffer The buffer containing the symbol to look for.
     * @param offset The offset into the buffer.
     * @param length The length of the symbol in the buffer.
     */
    public boolean containsSymbol(char[] buffer, int offset, int length) {

        synchronized (fSymbolTable) {
            return fSymbolTable.containsSymbol(buffer, offset, length);
        }

    } // containsSymbol(char[],int,int):boolean

} // class SynchronizedSymbolTable
