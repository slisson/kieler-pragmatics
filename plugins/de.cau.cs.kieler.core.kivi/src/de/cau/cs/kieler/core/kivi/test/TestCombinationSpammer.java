/*
 * KIELER - Kiel Integrated Environment for Layout Eclipse RichClient
 *
 * http://www.informatik.uni-kiel.de/rtsys/kieler/
 * 
 * Copyright 2011 by
 * + Christian-Albrechts-University of Kiel
 *   + Department of Computer Science
 *     + Real-Time and Embedded Systems Group
 * 
 * This code is provided under the terms of the Eclipse Public License (EPL).
 * See the file epl-v10.html for the license text.
 */
package de.cau.cs.kieler.core.kivi.test;

import de.cau.cs.kieler.core.kivi.AbstractCombination;
import de.cau.cs.kieler.core.kivi.test.TestTriggerSpammer.SpamState;

/**
 * @author haf
 *
 */
public class TestCombinationSpammer extends AbstractCombination {

    public void execute(SpamState state){
        schedule(new PrintEffect(" "+state.getCounter()+" "));
        schedule(new TestEffectSlow(100));
    }
    
}