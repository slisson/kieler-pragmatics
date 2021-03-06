/*
 * KIELER - Kiel Integrated Environment for Layout Eclipse RichClient
 *
 * http://www.informatik.uni-kiel.de/rtsys/kieler/
 * 
 * Copyright 2013 by
 * + Kiel University
 *   + Department of Computer Science
 *     + Real-Time and Embedded Systems Group
 * 
 * This code is provided under the terms of the Eclipse Public License (EPL).
 * See the file epl-v10.html for the license text.
 */
package de.cau.cs.kieler.ptolemy.attachmenteval.editors.attachment;

import org.eclipse.elk.core.klayoutdata.KLayoutData;
import org.eclipse.elk.core.options.CoreOptions;
import org.eclipse.elk.graph.KNode;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.ptolemy.moml.EntityType;
import org.ptolemy.moml.PropertyType;

import de.cau.cs.kieler.ptolemy.attachmenteval.PtolemyAttachmentEvalPlugin;
import de.cau.cs.kieler.ptolemy.klighd.PtolemyProperties;

/**
 * Label provider that provides labels for Ptolemy model elements.
 * 
 * @author cds
 */
final class AttachmentLabelProvider implements ITableLabelProvider {
    
    /** The editor that uses this label provider. */
    private CommentAttachmentEditor editor = null;
    /** Image used for compound actors. */
    private Image containerIcon = null;
    /** Image used for comment nodes. */
    private Image commentIcon = null;
    
    
    /**
     * Creates a new instance for the given attachment editor.
     * 
     * @param editor the attachment editor using this label provider.
     */
    public AttachmentLabelProvider(final CommentAttachmentEditor editor) {
        this.editor = editor;
        
        containerIcon = PtolemyAttachmentEvalPlugin
                .getImageDescriptor("icons/compoundActor.gif").createImage();
        commentIcon = PtolemyAttachmentEvalPlugin
                .getImageDescriptor("icons/comment.gif").createImage();
    }
    

    /**
     * {@inheritDoc}
     */
    public String getColumnText(Object element, int columnIndex) {
        if (element instanceof KNode) {
            KNode knode = (KNode) element;
            KLayoutData layoutData = knode.getData(KLayoutData.class);
            
            switch (columnIndex) {
            case 0:
                if (layoutData.getProperty(CoreOptions.COMMENT_BOX)) {
                    // Retrieve the comment text
                    return layoutData.getProperty(PtolemyProperties.COMMENT_TEXT);
                } else {
                    // Retrieve the label of the source model object
                    if (editor.getKlighdViewContext() != null) {
                        Object sourceElement = editor.getKlighdViewContext().getSourceElement(knode);
                        
                        if (sourceElement instanceof EntityType) {
                            return ((EntityType) sourceElement).getName();
                        }
                    }
                }
                
                return knode.toString();
                
            case 1:
                // If the node represents a comment, this column contains the attached node
                if (layoutData.getProperty(CoreOptions.COMMENT_BOX)) {
                    // Retrieve the attached actor, if any
                    KNode attachedActor = editor.getCurrentAssociations().get(knode);
                    
                    if (attachedActor != null) {
                        Object sourceElement =
                                editor.getKlighdViewContext().getSourceElement(attachedActor);
                        
                        if (sourceElement instanceof EntityType) {
                            return ((EntityType) sourceElement).getName();
                        } else if (sourceElement instanceof PropertyType) {
                            return ((PropertyType) sourceElement).getName();
                        }
                    } else {
                        // No attachment
                        return "";
                    }
                } else {
                    // No comment node, no attachment
                    return "";
                }
            }
        }
        
        return "LE ERROR!";
    }

    /**
     * {@inheritDoc}
     */
    public Image getColumnImage(Object element, int columnIndex) {
        if (element instanceof KNode) {
            KNode knode = (KNode) element;
            KLayoutData layoutData = knode.getData(KLayoutData.class);
            
            if (columnIndex == 0) {
                if (layoutData.getProperty(CoreOptions.COMMENT_BOX)) {
                    return commentIcon;
                } else {
                    return containerIcon;
                }
            }
        }
        
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isLabelProperty(Object element, String property) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public void addListener(ILabelProviderListener listener) {
    }

    /**
     * {@inheritDoc}
     */
    public void removeListener(ILabelProviderListener listener) {
    }

    /**
     * {@inheritDoc}
     */
    public void dispose() {
        if (containerIcon != null && !containerIcon.isDisposed()) {
            containerIcon.dispose();
        }

        if (commentIcon != null && !commentIcon.isDisposed()) {
            commentIcon.dispose();
        }
    }
    
}
