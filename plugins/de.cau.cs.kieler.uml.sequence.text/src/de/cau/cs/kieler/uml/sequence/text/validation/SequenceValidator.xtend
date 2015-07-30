/*
 * generated by Xtext
 */
package de.cau.cs.kieler.uml.sequence.text.validation

import de.cau.cs.kieler.uml.sequence.text.sequence.Interaction
import de.cau.cs.kieler.uml.sequence.text.sequence.Lifeline
import de.cau.cs.kieler.uml.sequence.text.sequence.OneLifelineEndBlock
import de.cau.cs.kieler.uml.sequence.text.sequence.OneLifelineMessage
import de.cau.cs.kieler.uml.sequence.text.sequence.SequenceDiagram
import de.cau.cs.kieler.uml.sequence.text.sequence.SequencePackage
import de.cau.cs.kieler.uml.sequence.text.sequence.TwoLifelineMessage
import java.util.ArrayList
import java.util.HashMap
import java.util.Map
import org.eclipse.emf.ecore.EStructuralFeature
import org.eclipse.xtext.validation.Check

/**
 * Custom validation rules. 
 * 
 * see http://www.eclipse.org/Xtext/documentation.html#validation
 */
class SequenceValidator extends AbstractSequenceValidator {

    private static val EStructuralFeature END_LEFT_COUNT = SequencePackage.Literals.
        TWO_LIFELINE_MESSAGE__SOURCE_END_BLOCK_COUNT
    private static val EStructuralFeature END_LEFT = SequencePackage.Literals.TWO_LIFELINE_MESSAGE__SOURCE_END_BLOCK
    private static val EStructuralFeature END_RIGHT_COUNT = SequencePackage.Literals.
        TWO_LIFELINE_MESSAGE__TARGET_END_BLOCK_COUNT
    private static val EStructuralFeature END_RIGHT = SequencePackage.Literals.TWO_LIFELINE_MESSAGE__TARGET_END_BLOCK
    private static val EStructuralFeature ONE_END_COUNT = SequencePackage.Literals.
        ONE_LIFELINE_MESSAGE__END_BLOCK_COUNT
    private static val EStructuralFeature ONE_END = SequencePackage.Literals.ONE_LIFELINE_MESSAGE__END_BLOCK
    private static val EStructuralFeature END = SequencePackage.Literals.ONE_LIFELINE_END_BLOCK__END_BLOCK_COUNT

    @Check
    def sameLifelineIdName(SequenceDiagram s) {
        val life = new ArrayList();
        for (lifeline : s.lifelines) {
            if (life.contains(lifeline.name)) {
                error("This Identifier is already used", lifeline, SequencePackage.Literals.LIFELINE__NAME)
            } else {
                life.add(lifeline.name)
            }
        }
    }

    //TODO check refinement lifeline names not doubled
    //TODO check no messages after destroy
    
    @Check
    def correctUsageOfBlocks(SequenceDiagram s) {
        val map = new HashMap<Lifeline, Integer>();
        for (interact : s.interactions) {
            correctUsageOfBlocksOnMessage(interact, map);
        }
        if (map.size > 0) {
            //TODO display warning (not working)
            warning("Not all Blocks are closed", s, SequencePackage.Literals.TWO_LIFELINE_MESSAGE__SOURCE_START_BLOCK)
            warning("Not all Blocks are closed", s, SequencePackage.Literals.TWO_LIFELINE_MESSAGE__TARGET_START_BLOCK)
            warning("Not all Blocks are closed", s, SequencePackage.Literals.ONE_LIFELINE_MESSAGE__START_BLOCK)
        }
    }

    private def dispatch correctUsageOfBlocksOnMessage(TwoLifelineMessage m, Map<Lifeline, Integer> map) {
        if (m.sourceStartBlock) {
            startBlock(m.sourceLifeline, 1, map)
        }
        if (m.targetStartBlock) {
            startBlock(m.targetLifeline, 1, map)
        }
        if (m.sourceEndBlock) {
            endBlock(m.sourceLifeline, m.sourceEndBlockCount, map, END_LEFT, END_LEFT_COUNT, m)
        }
        if (m.targetEndBlock) {
            endBlock(m.targetLifeline, m.targetEndBlockCount, map, END_RIGHT, END_RIGHT_COUNT, m)
        }
    }

    private def dispatch correctUsageOfBlocksOnMessage(OneLifelineMessage m, Map<Lifeline, Integer> map) {
        if (m.startBlock) {
            startBlock(m.lifeline, 1, map)
        }
        if (m.endBlock) {
            endBlock(m.lifeline, m.endBlockCount, map, ONE_END, ONE_END_COUNT, m)
        }
    }

    private def dispatch correctUsageOfBlocksOnMessage(OneLifelineEndBlock e, Map<Lifeline, Integer> map) {
        endBlock(e.lifeline, e.endBlockCount, map, END, END, e)
    }

    def startBlock(Lifeline l, Integer add, Map<Lifeline, Integer> map) {
        if (map.containsKey(l)) {
            var temp = map.get(l) + add
            map.put(l, temp)
        } else {
            map.put(l, 1)
        }
    }

    def endBlock(Lifeline l, int endBlockCount, Map<Lifeline, Integer> map, EStructuralFeature feature,
        EStructuralFeature featureCnt, Interaction i) {
        if (map.containsKey(l)) {
            var temp = 0
            if (endBlockCount > 0) {
                temp = map.get(l) - endBlockCount
            } else if (endBlockCount == 0) {
                temp = map.get(l) - 1
            }
            
            if (temp == 0) {
                map.remove(l)
            } else if (temp < 0) {
                if (endBlockCount > 1) {
                    error("The number of blocks closed is higher than the number of created", i, featureCnt)
                } else {
                    error("The number of blocks closed is higher than the number of created", i, feature)
                }
            } else {
                map.put(l, temp)
            }
        } else {
            error("The number of blocks closed is higher than the number of created", i, feature)
        }
    }
}