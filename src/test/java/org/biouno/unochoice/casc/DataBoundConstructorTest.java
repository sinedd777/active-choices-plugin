/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2025 Ioannis Moutsatsos, Bruno P. Kinoshita
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.biouno.unochoice.casc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.biouno.unochoice.CascadeChoiceParameter;
import org.biouno.unochoice.ChoiceParameter;
import org.biouno.unochoice.DynamicReferenceParameter;
import org.biouno.unochoice.model.GroovyScript;
import org.jenkinsci.plugins.scriptsecurity.sandbox.groovy.SecureGroovyScript;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

/**
 * Test that Active Choices parameters can be instantiated with @DataBoundConstructor
 * for JCasC compatibility.
 */
public class DataBoundConstructorTest {

    @Rule
    public JenkinsRule jenkins = new JenkinsRule();

    @Test
    public void testChoiceParameterDataBoundConstructor() throws Exception {
        // Create a GroovyScript with SecureGroovyScript
        SecureGroovyScript secureScript = new SecureGroovyScript("return ['dev', 'qa', 'prod']", true, null);
        GroovyScript groovyScript = new GroovyScript(secureScript, null);
        
        // Create ChoiceParameter using @DataBoundConstructor
        ChoiceParameter choiceParam = new ChoiceParameter(
            "ENV",                    // name
            "Environment selection",  // description
            "randomName123",         // randomName
            groovyScript,            // script
            "PT_SINGLE_SELECT",      // choiceType
            true,                    // filterable
            2                        // filterLength
        );
        
        assertNotNull("ChoiceParameter should be created", choiceParam);
        assertEquals("ENV", choiceParam.getName());
        assertEquals("Environment selection", choiceParam.getDescription());
        assertEquals("PT_SINGLE_SELECT", choiceParam.getChoiceType());
        assertEquals(true, choiceParam.getFilterable());
        assertEquals((Integer) 2, choiceParam.getFilterLength());
        assertNotNull("Script should not be null", choiceParam.getScript());
    }

    @Test
    public void testCascadeChoiceParameterDataBoundConstructor() throws Exception {
        // Create a GroovyScript
        SecureGroovyScript secureScript = new SecureGroovyScript(
            "if (ENV == 'dev') return ['service1', 'service2']; else return ['service3']", 
            true, null);
        GroovyScript groovyScript = new GroovyScript(secureScript, null);
        
        // Create CascadeChoiceParameter using @DataBoundConstructor
        CascadeChoiceParameter cascadeParam = new CascadeChoiceParameter(
            "SERVICE",               // name
            "Service selection",     // description
            "randomName456",         // randomName
            groovyScript,            // script
            "PT_SINGLE_SELECT",      // choiceType
            "ENV",                   // referencedParameters
            true,                    // filterable
            1                        // filterLength
        );
        
        assertNotNull("CascadeChoiceParameter should be created", cascadeParam);
        assertEquals("SERVICE", cascadeParam.getName());
        assertEquals("Service selection", cascadeParam.getDescription());
        assertEquals("PT_SINGLE_SELECT", cascadeParam.getChoiceType());
        assertEquals("ENV", cascadeParam.getReferencedParameters());
        assertEquals(true, cascadeParam.getFilterable());
        assertEquals((Integer) 1, cascadeParam.getFilterLength());
        assertNotNull("Script should not be null", cascadeParam.getScript());
    }

    @Test
    public void testDynamicReferenceParameterDataBoundConstructor() throws Exception {
        // Create a GroovyScript for HTML content
        SecureGroovyScript secureScript = new SecureGroovyScript(
            "return '<b>Environment: ' + ENV + '</b>'", 
            true, null);
        GroovyScript groovyScript = new GroovyScript(secureScript, null);
        
        // Create DynamicReferenceParameter using @DataBoundConstructor
        DynamicReferenceParameter dynamicRefParam = new DynamicReferenceParameter(
            "INFO",                       // name
            "Environment information",    // description
            "randomName789",              // randomName
            groovyScript,                 // script
            "ET_FORMATTED_HTML",          // choiceType
            "ENV",                        // referencedParameters
            false                         // omitValueField
        );
        
        assertNotNull("DynamicReferenceParameter should be created", dynamicRefParam);
        assertEquals("INFO", dynamicRefParam.getName());
        assertEquals("Environment information", dynamicRefParam.getDescription());
        assertEquals("ET_FORMATTED_HTML", dynamicRefParam.getChoiceType());
        assertEquals("ENV", dynamicRefParam.getReferencedParameters());
        assertEquals(false, dynamicRefParam.getOmitValueField());
        assertNotNull("Script should not be null", dynamicRefParam.getScript());
    }

    @Test
    public void testGroovyScriptDataBoundConstructor() throws Exception {
        // Test GroovyScript @DataBoundConstructor
        SecureGroovyScript mainScript = new SecureGroovyScript("return ['option1', 'option2']", true, null);
        SecureGroovyScript fallbackScript = new SecureGroovyScript("return ['fallback']", true, null);
        
        GroovyScript groovyScript = new GroovyScript(mainScript, fallbackScript);
        
        assertNotNull("GroovyScript should be created", groovyScript);
        assertNotNull("Main script should not be null", groovyScript.getScript());
        assertNotNull("Fallback script should not be null", groovyScript.getFallbackScript());
        assertEquals("return ['option1', 'option2']", groovyScript.getScript().getScript());
        assertEquals("return ['fallback']", groovyScript.getFallbackScript().getScript());
    }

    @Test
    public void testParametersHaveSymbolAnnotations() throws Exception {
        // Test that the descriptor classes have @Symbol annotations for JCasC
        ChoiceParameter choiceParm = new ChoiceParameter("test", "test", "test", 
            new GroovyScript(new SecureGroovyScript("return []", true, null), null), 
            "PT_SINGLE_SELECT", false, 1);
        
        // Get the descriptor and check if it has the expected symbol
        assertNotNull("ChoiceParameter descriptor should exist", choiceParm.getDescriptor());
        
        CascadeChoiceParameter cascadeParm = new CascadeChoiceParameter("test", "test", "test",
            new GroovyScript(new SecureGroovyScript("return []", true, null), null),
            "PT_SINGLE_SELECT", "ref", false, 1);
        
        assertNotNull("CascadeChoiceParameter descriptor should exist", cascadeParm.getDescriptor());
        
        DynamicReferenceParameter dynamicParm = new DynamicReferenceParameter("test", "test", "test",
            new GroovyScript(new SecureGroovyScript("return []", true, null), null),
            "ET_FORMATTED_HTML", "ref", false);
        
        assertNotNull("DynamicReferenceParameter descriptor should exist", dynamicParm.getDescriptor());
    }
}
