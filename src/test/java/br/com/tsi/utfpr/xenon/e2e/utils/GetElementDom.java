package br.com.tsi.utfpr.xenon.e2e.utils;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.IOException;
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

    public GetElementDom navigation(String parentElement, String attribute, String valueAttribute) {
        String xPath = String.format("//%s[@%s='%s']", parentElement, attribute, valueAttribute);
        element = htmlPage.getFirstByXPath(xPath);
        return instance;
    }

    public GetElementDom getChildUl(String attribute, String valueAttribute) {
        element = getUl(attribute, valueAttribute);
        return instance;
    }

    public GetElementDom getChildLi(String attribute, String valueAttribute) {
        element = getLi(attribute, valueAttribute);
        return instance;
    }

    public GetElementDom getChildAnchor(String attribute, String valueAttribute) {
        element = getAnchor(attribute, valueAttribute);
        return instance;
    }

    public HtmlElement getUl(String attribute, String valueAttribute) {
        String xPath = String.format("//ul[@%s='%s']", attribute, valueAttribute);
        return element;
    }

    public HtmlElement getLi(String attribute, String valueAttribute) {
        String xPath = String.format("//li[@%s='%s']", attribute, valueAttribute);
        element = element.<HtmlListItem>getFirstByXPath(xPath);
        return element;
    }

    public HtmlElement getAnchor(String attribute, String valueAttribute) {
        String xPath = String.format("//a[@%s='%s']", attribute, valueAttribute);
        element = element.getFirstByXPath(xPath);
        return element;
    }

    public GetElementDom setAttributeInElement(String attribute, String valueAttribute) {
        element.setAttribute(attribute, valueAttribute);
        return instance;
    }

    public HtmlElement getElement() {
        return element;
    }

    public HtmlPage getHtmlPage() {
        return htmlPage;
    }

    public HtmlPage redirectOnClick() throws IOException {
        return element.click();
    }
}
