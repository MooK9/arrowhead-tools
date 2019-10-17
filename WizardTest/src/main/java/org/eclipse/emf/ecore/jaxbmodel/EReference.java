//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.08.08 at 01:05:25 PM CEST 
//

package org.eclipse.emf.ecore.jaxbmodel;

import com.incquerylabs.arrowhead.tools.magic.Wizard;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EReference")
public class EReference extends EStructuralFeature {

    @XmlAttribute(name = "containment")
    protected String containment;
    @XmlAttribute(name = "container")
    protected String container;
    @XmlAttribute(name = "resolveProxies")
    protected String resolveProxies;
    @XmlAttribute(name = "eOpposite")
    @XmlSchemaType(name = "anyURI")
    protected String eOpposite;
    @XmlAttribute(name = "eReferenceType")
    @XmlSchemaType(name = "anyURI")
    protected String eReferenceType;
    @XmlAttribute(name = "eKeys")
    protected List<String> eKeys;

    @Override
    public void subCompartmentalize(Path parent, Element topParent, Path topPath) throws IOException {
        if(name == null){
            name = "Unnamed" + Wizard.unnamedSuffix++;
        }
        String filename = Wizard.sanitizeFilename(name);
        Path xml = parent.resolve(filename + ".xml");
        if(Files.exists(xml)){
            filename = Wizard.degenarilzeName(parent, name);
            xml = parent.resolve(filename + ".xml");
        }
        Path dir = parent.resolve(filename);
        Files.createDirectory(dir);
        Files.createFile(xml);

        Element ref = topParent.addElement(Wizard.REF);
        ref.addAttribute(Wizard.HREF, topPath.relativize(xml).toString());
        Document doc = DocumentHelper.createDocument();
        Element me = doc.addElement("eReferences");
        me.addAttribute(Wizard.TYPE, "ecore:EReference");
        me.addAttribute("name", name);
        Wizard.writeETypedElementAttributes(me, this);
        Wizard.writeEStructuralFeatureAttributes(me, this);
        me.addAttribute("containment", containment);
        me.addAttribute("container", container);
        me.addAttribute("resolveProxies", resolveProxies);
        me.addAttribute("eOpposite", eOpposite);
        me.addAttribute("eReferenceType", eReferenceType);
        Wizard.addListAttribute(me, "eKeys", eKeys);

        if (eAnnotations != null) {
            for (EAnnotation an : eAnnotations) {
                an.subCompartmentalize(dir, me, xml);
            }
        }
        if (eGenericType != null) {
            eGenericType.subCompartmentalize(dir, me, xml);
        }

        Wizard.writeDocument(xml, doc);
    }

    public String getContainment() {
        return containment;
    }

    public void setContainment(String value) {
        this.containment = value;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String value) {
        this.container = value;
    }

    public String getResolveProxies() {
        if (resolveProxies == null) {
            return "true";
        } else {
            return resolveProxies;
        }
    }

    public void setResolveProxies(String value) {
        this.resolveProxies = value;
    }

    public String getEOpposite() {
        return eOpposite;
    }

    public void setEOpposite(String value) {
        this.eOpposite = value;
    }

    public String getEReferenceType() {
        return eReferenceType;
    }

    public void setEReferenceType(String value) {
        this.eReferenceType = value;
    }

    public List<String> getEKeys() {
        if (eKeys == null) {
            eKeys = new ArrayList<String>();
        }
        return this.eKeys;
    }
}
