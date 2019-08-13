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
import org.dom4j.QName;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EFactory")
public class EFactory extends EModelElement {

    @XmlAttribute(name = "ePackage")
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object ePackage;

    @Override
    public void subCompartmentalize(Path parent, Element topParent, Path topPath) throws IOException {
        String name = "Factory" + Wizard.factorySuffix++;
        Path dir = parent.resolve(name);
        Files.createDirectory(dir);
        Path xml = parent.resolve(name + ".xml");
        Files.createFile(xml);

        Element ref = topParent.addElement(Wizard.REF);
        ref.addAttribute(Wizard.HREF, topPath.relativize(xml).toString());
        Document doc = DocumentHelper.createDocument();
        Element me = doc.addElement(new QName("EFactory", Wizard.EC)); //TODO find a factory
        if (ePackage != null) {
            me.addAttribute("ePackage", ePackage.toString());
        }

        if (eAnnotations != null) {
            for (EAnnotation an : eAnnotations) {
                an.subCompartmentalize(dir, me, xml);
            }
        }

        Wizard.writeDocument(xml, doc);
    }

    public Object getEPackage() {
        return ePackage;
    }

    public void setEPackage(Object value) {
        this.ePackage = value;
    }
}
