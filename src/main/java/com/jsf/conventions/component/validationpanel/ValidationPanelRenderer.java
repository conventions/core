/*
 * Copyright 2012 rmpestano.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jsf.conventions.component.validationpanel;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitHint;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author rmpestano
 */
public class ValidationPanelRenderer extends Renderer {

    private static final Set<VisitHint> VISIT_HINTS = EnumSet.of(VisitHint.SKIP_UNRENDERED);
    
    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ValidationPanel panel = (ValidationPanel) component;
        String id = panel.getClientId();
        ResponseWriter writer = context.getResponseWriter();
        writer.writeAttribute("id", id, "id");
        writer.startElement("div", panel);

        if (panel.isRendered() && panel.getChildCount() > 0) {
            EditableValueHoldersVisitCallback visitCallback = new EditableValueHoldersVisitCallback();
            panel.visitTree(VisitContext.createVisitContext(context, null, VISIT_HINTS),
                    visitCallback);
            //gather editable value holders via visitor
            final List<EditableValueHolder> editableValueHolders = visitCallback.getEditableValueHolders();

            if (!this.isAllChildrenValid(editableValueHolders, context)) {
                //has invalid children so set errorStyle
                if (!panel.hasErrorStyle()) {//if no errorStyle provided use default one
                    writer.writeAttribute("style", ValidationPanel.defaulErrorStyle, "errorStyle");
                } else { //has errorStyle to be applied
                    if (StringUtils.isNotEmpty(panel.getErrorStyle())) {
                        writer.writeAttribute("style", panel.getErrorStyle(), "style");
                    }
                    if (StringUtils.isNotEmpty(panel.getErrorStyleClass())) {
                        writer.writeAttribute("class", panel.getErrorStyleClass(), "errorStyleClass");
                    }
                }
            } else {//all children are valid
                if (StringUtils.isNotEmpty(panel.getStyle())) {
                    writer.writeAttribute("style", panel.getStyle(), "style");
                }
                if (StringUtils.isNotEmpty(panel.getStyleClass())) {
                    writer.writeAttribute("class", panel.getStyleClass(), "styleClass");
                }
            }
            renderChildren(context, panel);
        }
        writer.endElement("div");
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        //Do nothing
    }

    /**
     * check if all EditableValueHolder components nested in ValidationPanel are
     * valid
     *
     * @param children
     * @return
     * <code>true</code> if all EditableValueHolder are valid
     * <code>false</code> if one or more EditableValueHolder are invalid
     */
    private Boolean isAllChildrenValid(List<EditableValueHolder> children, FacesContext context) throws IOException {

        for (EditableValueHolder child : children) {
            if (!child.isValid()) {
                return false;//if one component is invalid so the panel is considered invalid
            }
        }
        return true;//all components inside ValidationPanel are valid
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    private void renderChildren(FacesContext facesContext, UIComponent component) throws IOException {
        for (Iterator<UIComponent> iterator = component.getChildren().iterator(); iterator.hasNext();) {
            UIComponent child = (UIComponent) iterator.next();
            renderChild(facesContext, child);
        }
    }

    private void renderChild(FacesContext facesContext, UIComponent child) throws IOException {
        if (!child.isRendered()) {
            return;
        }

        child.encodeBegin(facesContext);

        if (child.getRendersChildren()) {
            child.encodeChildren(facesContext);
        } else {
            renderChildren(facesContext, child);
        }
        child.encodeEnd(facesContext);
    }
}
