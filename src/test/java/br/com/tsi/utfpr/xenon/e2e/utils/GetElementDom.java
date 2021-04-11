package br.com.tsi.utfpr.xenon.e2e.utils;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlUnorderedList;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import lombok.Setter;

public class GetElementDom {

    private static GetElementDom instance;

    @Setter
    private HtmlPage htmlPage;

    private HtmlElement element;

    public static GetElementDom start(WebClient client, String url) throws IOException {
        instance = new GetElementDom();
        var htmlPage = client.<HtmlPage>getPage(url);
        instance.setHtmlPage(htmlPage);
        return instance;
    }

    public static GetElementDom start(HtmlPage page) {
        instance = new GetElementDom();
        instance.setHtmlPage(page);
        return instance;
    }

    public GetElementDom navigation(String parentElement, String attribute, String valueAttribute) {
        String xPath = String.format("//%s[@%s='%s']", parentElement, attribute, valueAttribute);
        element = htmlPage.getFirstByXPath(xPath);
        return instance;
    }

    public GetElementDom getChildUl(String attribute, String valueAttribute) {
        String xPath = String.format("//ul[@%s='%s']", attribute, valueAttribute);
        element = element.<HtmlUnorderedList>getFirstByXPath(xPath);
        return instance;
    }

    public GetElementDom getChildLi(String attribute, String valueAttribute) {
        String xPath = String.format("//li[@%s='%s']", attribute, valueAttribute);
        element = element.<HtmlListItem>getFirstByXPath(xPath);
        return instance;
    }

    public GetElementDom getChildAnchor(String attribute, String valueAttribute) {
        String xPath = String.format("//a[@%s='%s']", attribute, valueAttribute);
        element = element.getFirstByXPath(xPath);
        return instance;
    }

    public GetElementDom getDiv(String attribute, String valueAttribute) {
        var xPath = String.format("//div[@%s='%s']", attribute, valueAttribute);
        element = element.getFirstByXPath(xPath);
        return instance;
    }

    public GetElementDom setAttributeInElement(String attribute, String valueAttribute) {
        element.setAttribute(attribute, valueAttribute);
        return instance;
    }

    public HtmlElement getElement() {
        return element;
    }

    public List<DomElement> getListChildSpan(String attribute, String valueAttribute) {
        var xPath = String.format("//span[@%s='%s']", attribute, valueAttribute);
        return element.getByXPath(xPath);
    }

    public DomNodeList<HtmlElement> getListChildAnchor() {
        return element.getElementsByTagName("a");
    }

    public HtmlPage getHtmlPage() {
        return htmlPage;
    }

    public HtmlPage redirectOnClick() throws IOException {
        return element.click();
    }

    public GetElementDom executeAssertion(Consumer<HtmlElement> assertion) {
        assertion.accept(element);
        return instance;
    }
}
