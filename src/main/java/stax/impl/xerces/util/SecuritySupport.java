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
 * Copyright (c) 2002 The Apache Software Foundation.  All rights 
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
 * 4. The name "Apache Software Foundation" must not be used to endorse or
 *    promote products derived from this software without prior written
 *    permission. For written permission, please contact apache@apache.org.
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
 * originally based on software copyright (c) 1999-2002, Sun Microsystems,
 * Inc., http://www.sun.com.  For more information on the Apache Software
 * Foundation, please see <http://www.apache.org/>.
 */

package stax.impl.xerces.util;

import java.io.*;

/**
 * This class is duplicated for each JAXP subpackage so keep it in sync.
 * It is package private and therefore is not exposed as part of the JAXP
 * API.
 *
 * Base class with security related methods that work on JDK 1.1.
 */
class SecuritySupport {

    /*
     * Make this of type Object so that the verifier won't try to
     * prove its type, thus possibly trying to load the SecuritySupport12
     * class.
     */
    private static final Object securitySupport;

    static {
	SecuritySupport ss = null;
	try {
	    Class c = Class.forName("java.security.AccessController");
	    // if that worked, we're on 1.2.
	    /*
	    // don't reference the class explicitly so it doesn't
	    // get dragged in accidentally.
	    c = Class.forName("javax.mail.SecuritySupport12");
	    Constructor cons = c.getConstructor(new Class[] { });
	    ss = (SecuritySupport)cons.newInstance(new Object[] { });
	    */
	    /*
	     * Unfortunately, we can't load the class using reflection
	     * because the class is package private.  And the class has
	     * to be package private so the APIs aren't exposed to other
	     * code that could use them to circumvent security.  Thus,
	     * we accept the risk that the direct reference might fail
	     * on some JDK 1.1 JVMs, even though we would never execute
	     * this code in such a case.  Sigh...
	     */
	    ss = new SecuritySupport12();
	} catch (Exception ex) {
	    // ignore it
	} finally {
	    if (ss == null)
		ss = new SecuritySupport();
	    securitySupport = ss;
	}
    }

    /**
     * Return an appropriate instance of this class, depending on whether
     * we're on a JDK 1.1 or J2SE 1.2 (or later) system.
     */
    public static SecuritySupport getInstance() {
	return (SecuritySupport)securitySupport;
    }

    public ClassLoader getContextClassLoader() {
	return null;
    }

    public String getSystemProperty(String propName) {
        return System.getProperty(propName);
    }

    public FileInputStream getFileInputStream(File file)
        throws FileNotFoundException
    {
        return new FileInputStream(file);
    }

    public InputStream getResourceAsStream(ClassLoader cl, String name) {
        InputStream ris;
        if (cl == null) {
            ris = ClassLoader.getSystemResourceAsStream(name);
        } else {
            ris = cl.getResourceAsStream(name);
        }
        return ris;
    }
}
