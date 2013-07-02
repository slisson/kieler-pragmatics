package de.cau.cs.kieler.klay.tree.util;

import java.util.Iterator;

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;

import de.cau.cs.kieler.klay.tree.graph.TNode;

public class FindNode {

    /**
     * This method returns the leftmost node at the deepest level. This is implemented using a
     * postorder walk of the subtree under given level.
     * 
     * @param currentlevel
     *            a list of nodes at level one
     * @return the leftmost node at the deepest level.
     */
    public static TNode getLeftMost(final Iterable<TNode> currentlevel) {
        return getLeftMost(currentlevel, -1);
    }

    /**
     * This method returns the leftmost node at the given level. This is implemented using a
     * postorder walk of the subtree under given level, depth levels down. Depth here refers to the
     * level below where the leftmost descendant is being found.
     * 
     * If given level is negative it returns the leftmost node at the deepest level.
     * 
     * @param currentlevel
     *            a list of nodes at level one
     * @param depth
     *            the depth to search for
     * @return the leftmost descendant at depth levels down
     */
    public static TNode getLeftMost(final Iterable<TNode> currentlevel, int depth) {
        if (0 < Iterables.size(currentlevel)) {

            // the leftmost descendant at depth levels down
            if (1 < depth) {
                depth--;
                // build empty iterator
                Iterable<TNode> nextLevel = new Iterable<TNode>() {

                    public Iterator<TNode> iterator() {
                        return Iterators.emptyIterator();
                    }
                };

                for (TNode cN : currentlevel) {
                    // append the children of the current node to the next level
                    nextLevel = Iterables.concat(nextLevel, cN.getChildren());
                }
                return getLeftMost(nextLevel, depth);
            }

            // the leftmost node at the deepest level
            if (depth < 0) {
                // build empty iterator
                Iterable<TNode> nextLevel = new Iterable<TNode>() {

                    public Iterator<TNode> iterator() {
                        return Iterators.emptyIterator();
                    }
                };

                for (TNode cN : currentlevel) {
                    // append the children of the current node to the next level
                    nextLevel = Iterables.concat(nextLevel, cN.getChildren());
                }

                //
                if (0 < Iterables.size(nextLevel)) {
                    return getLeftMost(nextLevel, depth);
                }
            }
        }
        // return the leftmost node at the current level
        return Iterables.getFirst(currentlevel, null);
    }

}
